package repositories;

import context.DatabaseExecutionContext;
import models.Field;
import models.Response;
import net.jodah.failsafe.CircuitBreaker;
import net.jodah.failsafe.Failsafe;
import play.db.jpa.JPAApi;
import play.libs.Json;
import play.mvc.Result;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static play.mvc.Results.ok;

public class JPAFieldRepository implements FieldRepository {
    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;

    private final CircuitBreaker circuitBreaker = new CircuitBreaker().withFailureThreshold(1).withSuccessThreshold(3);

    @Inject
    public JPAFieldRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }

    @Override
    public CompletionStage<Field> add(Field field) {
        return supplyAsync(() -> wrap(em -> insert(em, field)), executionContext);
    }

    @Override
    public CompletionStage<Field> update(Long id, Field field) {
        return supplyAsync(() -> wrap(em -> Failsafe.with(circuitBreaker).get(() -> modify(em, id, field))));
    }

    @Override
    public CompletionStage<Result> delete(Long id) {
        return supplyAsync(() -> wrap(em -> Failsafe.with(circuitBreaker).get(() -> delete(em, id))));
    }

    @Override
    public CompletionStage<Stream<Field>> list() {
        return supplyAsync(() -> wrap(em -> list(em)), executionContext);
    }

    @Override
    public CompletionStage<Stream<Field>> listAlive() {
        return supplyAsync(() -> wrap(em -> listAlive(em)), executionContext);
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    private Field insert(EntityManager em, Field field) {
        em.persist(field);
        return field;
    }

    private Field modify(EntityManager em, Long id, Field field) throws InterruptedException {
        final Field data = em.find(Field.class, id);
        if (data != null) {
            data.label = field.label;
            data.type = field.type;
            data.required = field.required;
            data.isActive = field.isActive;
            data.rowNumber = field.rowNumber;
        }
        return data;
    }

    private Result delete(EntityManager em, Long id) {
        final Field field = em.find(Field.class, id);
        if(field != null) {
            em.remove(field);
        }
        return ok();
    }

    private Stream<Field> list(EntityManager em) {
        List<Field> fields = em.createQuery("SELECT f FROM Field f", Field.class).getResultList();
        return fields.stream();
    }

    private Stream<Field> listAlive(EntityManager em) {
        List<Field> fields = em.createQuery("SELECT f FROM Field f WHERE f.isActive = true", Field.class).getResultList();
        return fields.stream();
    }
}
