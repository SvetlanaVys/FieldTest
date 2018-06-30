package v1.field;

import net.jodah.failsafe.CircuitBreaker;
import net.jodah.failsafe.Failsafe;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * A repository that provides a non-blocking API with a custom execution context
 * and circuit breaker.
 */
@Singleton
public class JPAFieldRepository implements FieldRepository {

    private final JPAApi jpaApi;
    private final FieldExecutionContext ec;
    private final CircuitBreaker circuitBreaker = new CircuitBreaker().withFailureThreshold(1).withSuccessThreshold(3);

    @Inject
    public JPAFieldRepository(JPAApi api, FieldExecutionContext ec) {
        this.jpaApi = api;
        this.ec = ec;
    }

    @Override
    public CompletionStage<Stream<FieldData>> list() {
        return supplyAsync(() -> wrap(em -> select(em)), ec);
    }

    @Override
    public CompletionStage<FieldData> create(FieldData fieldData) {
        return supplyAsync(() -> wrap(em -> insert(em, fieldData)), ec);
    }

    @Override
    public CompletionStage<Optional<FieldData>> get(Long id) {
        return supplyAsync(() -> wrap(em -> Failsafe.with(circuitBreaker).get(() -> lookup(em, id))), ec);
    }

    @Override
    public CompletionStage<Optional<FieldData>> update(Long id, FieldData fieldData) {
        return supplyAsync(() -> wrap(em -> Failsafe.with(circuitBreaker).get(() -> modify(em, id, fieldData))), ec);
    }


    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    private Optional<FieldData> lookup(EntityManager em, Long id) throws SQLException {
        throw new SQLException("Call this to cause the circuit breaker to trip");
        //return Optional.ofNullable(em.find(FieldData.class, id));
    }

    private Stream<FieldData> select(EntityManager em) {
        TypedQuery<FieldData> query = em.createQuery("SELECT f FROM FieldData f WHERE f.isDeleted = false", FieldData.class);
        return query.getResultList().stream();
    }

    private Optional<FieldData> modify(EntityManager em, Long id, FieldData fieldData) throws InterruptedException {
        final FieldData data = em.find(FieldData.class, id);
        if (data != null) {
            data.label = fieldData.label;
            data.type = fieldData.type;
            data.required = fieldData.required;
            data.isActive = fieldData.isActive;
        }
        Thread.sleep(10000L);
        return Optional.ofNullable(data);
    }

    private FieldData insert(EntityManager em, FieldData fieldData) { return em.merge(fieldData); }
}
