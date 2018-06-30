package v1.option;

import net.jodah.failsafe.CircuitBreaker;
import net.jodah.failsafe.Failsafe;
import org.hibernate.annotations.Parameter;
import play.db.jpa.JPAApi;
import v1.field.FieldData;

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
public class JPAOptionRepository implements OptionRepository {
    private final JPAApi jpaApi;
    private final OptionExecutionContext ec;
    private final CircuitBreaker circuitBreaker = new CircuitBreaker().withFailureThreshold(1).withSuccessThreshold(3);

    @Inject
    public JPAOptionRepository(JPAApi api, OptionExecutionContext ec) {
        this.jpaApi = api;
        this.ec = ec;
    }

    @Override
    public CompletionStage<Stream<OptionData>> listOptions() {
        return supplyAsync(() -> wrap(em -> selectOptions(em)), ec);
    }

    @Override
    public CompletionStage<Stream<OptionData>> list(FieldData field) {
        return supplyAsync(() -> wrap(em -> select(em, field)), ec);
    }

    @Override
    public CompletionStage<OptionData> create(OptionData postData) {
        return supplyAsync(() -> wrap(em -> insert(em, postData)), ec);
    }

    @Override
    public CompletionStage<Optional<OptionData>> get(Long id) {
        return supplyAsync(() -> wrap(em -> Failsafe.with(circuitBreaker).get(() -> lookup(em, id))), ec);
    }

    @Override
    public CompletionStage<Optional<OptionData>> update(Long id, OptionData postData) {
        return supplyAsync(() -> wrap(em -> Failsafe.with(circuitBreaker).get(() -> modify(em, id, postData))), ec);
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    private Optional<OptionData> lookup(EntityManager em, Long id) throws SQLException {
        throw new SQLException("Call this to cause the circuit breaker to trip");
        //return Optional.ofNullable(em.find(PostData.class, id));
    }

    private Stream<OptionData> select(EntityManager em, FieldData field) {
        TypedQuery<OptionData> query = em.createQuery("SELECT o FROM OptionData o WHERE o.field != null AND o.field.id="+field.id, OptionData.class);
        return query.getResultList().stream();
    }

    private Stream<OptionData> selectOptions(EntityManager em) {
        TypedQuery<OptionData> query = em.createQuery("SELECT o FROM OptionData o", OptionData.class);
        return query.getResultList().stream();
    }

    private Optional<OptionData> modify(EntityManager em, Long id, OptionData optionData) throws InterruptedException {
        final OptionData data = em.find(OptionData.class, id);
        if (data != null) {
            data.name = optionData.name;
//            data.field = optionData.field;
        }
        Thread.sleep(10000L);
        return Optional.ofNullable(data);
    }

    private OptionData insert(EntityManager em, OptionData optionData) {
        return em.merge(optionData);
    }
}
