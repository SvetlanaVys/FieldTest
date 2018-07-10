package repositories;

import context.DatabaseExecutionContext;
import models.Field;
import models.Response;
import net.jodah.failsafe.CircuitBreaker;
import net.jodah.failsafe.Failsafe;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class JPAResponseRepository implements ResponseRepository {
    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;
    private final CircuitBreaker circuitBreaker = new CircuitBreaker().withFailureThreshold(1).withSuccessThreshold(3);

    @Inject
    public JPAResponseRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }

    @Override
    public CompletionStage<Response> add(Response response) {
        return supplyAsync(() -> wrap(em -> insert(em, response)), executionContext);
    }


    @Override
    public CompletionStage<Response> update(Long id, Response response) {
        return supplyAsync(() -> wrap(em -> Failsafe.with(circuitBreaker).get(() -> modify(em, id, response))));
    }

    @Override
    public CompletionStage<Stream<Response>> list() {
        return supplyAsync(() -> wrap(em -> list(em)), executionContext);
    }

    @Override
    public CompletionStage<Stream<Response>> listByField(Field field) {
        return supplyAsync(() -> wrap(em -> listByField(em, field)), executionContext);
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    private Response insert(EntityManager em, Response response) {
        em.persist(response);
        return response;
    }

    private Response modify(EntityManager em, Long id, Response response) throws InterruptedException {
        final Response data = em.find(Response.class, id);
        if (data != null) {
            data.content = response.content;
            data.row = response.row;
            data.field = response.field;
        }
        return data;
    }

    private Stream<Response> list(EntityManager em) {
        List<Response> responses = em.createQuery("SELECT r FROM Response r WHERE r.field.id!=null", Response.class).getResultList();
        return responses.stream();
    }

    private Stream<Response> listByField(EntityManager em, Field field) {
        List<Response> responses = em.createQuery("SELECT r FROM Response r WHERE r.field.id="+field.id, Response.class).getResultList();
        return responses.stream();
    }


}
