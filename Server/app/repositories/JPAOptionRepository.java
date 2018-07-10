package repositories;

import context.DatabaseExecutionContext;
import models.Field;
import models.Option;
import net.jodah.failsafe.CircuitBreaker;
import net.jodah.failsafe.Failsafe;
import org.jboss.logging.Param;
import play.db.jpa.JPAApi;
import play.mvc.Result;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static play.mvc.Results.ok;

public class JPAOptionRepository  implements OptionRepository {
    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;
    private final CircuitBreaker circuitBreaker = new CircuitBreaker().withFailureThreshold(1).withSuccessThreshold(3);

    @Inject
    public JPAOptionRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }

    @Override
    public CompletionStage<Option> add(Option option) {
        return supplyAsync(() -> wrap(em -> insert(em, option)), executionContext);
    }

    @Override
    public CompletionStage<Option> update(Long id, Option option) {
        return supplyAsync(() -> wrap(em -> Failsafe.with(circuitBreaker).get(() -> modify(em, id, option))));
    }

    @Override
    public CompletionStage<Result> delete(Long id) {
        return supplyAsync(() -> wrap(em -> Failsafe.with(circuitBreaker).get(() -> delete(em, id))));
    }

    @Override
    public CompletionStage<Stream<Option>> list() {
        return supplyAsync(() -> wrap(em -> list(em)), executionContext);
    }

    @Override
    public CompletionStage<Stream<Option>> listByField(Field field) {
        return supplyAsync(() -> wrap(em -> listByField(em, field)), executionContext);
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    private Option insert(EntityManager em, Option option) {
        em.persist(option);
        return option;
    }

    private Option modify(EntityManager em, Long id,Option option) throws InterruptedException {
        final Option data = em.find(Option.class, id);
        if (data != null) {
            data.name = option.name;
            data.field = option.field;
        }
        return data;
    }

    private Result delete(EntityManager em, Long id) {
        final Option option = em.find(Option.class, id);
        if(option != null) {
            em.remove(option);
        }
        return ok();
    }

    private Stream<Option> list(EntityManager em) {
        List<Option> options = em.createQuery("SELECT o FROM Option o WHERE o.field != null", Option.class).getResultList();
        return options.stream();
    }

    private Stream<Option> listByField(EntityManager em, Field field) {
        TypedQuery<Option> query = em.createQuery("SELECT o FROM Option o WHERE o.field != null AND o.field.id=" + field.id, Option.class);
        return query.getResultList().stream();
    }
}
