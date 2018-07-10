package repositories;

import context.DatabaseExecutionContext;
import models.Login;
import models.Password;
import models.Profile;
import net.jodah.failsafe.CircuitBreaker;
import net.jodah.failsafe.Failsafe;
import org.mindrot.jbcrypt.BCrypt;
import play.db.jpa.JPAApi;
import play.mvc.Result;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static play.mvc.Results.badRequest;
import static play.mvc.Results.ok;

public class JPAProfileRepository  implements ProfileRepository {
    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;
    private final CircuitBreaker circuitBreaker = new CircuitBreaker().withFailureThreshold(1).withSuccessThreshold(3);

    @Inject
    public JPAProfileRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }

    @Override
    public CompletionStage<Profile> add(Profile profile) {
        return supplyAsync(() -> wrap(em -> insert(em, profile)), executionContext);
    }

    @Override
    public CompletionStage<Profile> update(Long id, Profile profile) {
        return supplyAsync(() -> wrap(em -> Failsafe.with(circuitBreaker).get(() -> modify(em, id, profile))));
    }

    @Override
    public CompletionStage<Profile> updatePassword(Long id, Password password) {
        return supplyAsync(() -> wrap(em -> Failsafe.with(circuitBreaker).get(() -> modifyPassword(em, id, password))));
    }

    @Override
    public CompletionStage<Result> delete(Long id) {
        return supplyAsync(() -> wrap(em -> Failsafe.with(circuitBreaker).get(() -> delete(em, id))));
    }

    @Override
    public CompletionStage<Stream<Profile>> list() {
        return supplyAsync(() -> wrap(em -> list(em)), executionContext);
    }

    @Override
    public CompletionStage<Optional<Profile>> getByEmail(String email) {
        return supplyAsync(() -> wrap(em -> getUser(em, email)), executionContext);
    }

    @Override
    public CompletionStage<Optional<Profile>> isUser(Login login) {
        return supplyAsync(() -> wrap(em -> Failsafe.with(circuitBreaker).get(() -> isUserExist(em, login))));
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        try{
            return jpaApi.withTransaction(function);
        } catch (RollbackException ex) {
            return null;
        }
    }

    private Profile insert(EntityManager em, Profile profile) {
       em.persist(profile);
       return profile;

    }

    private Profile modify(EntityManager em, Long id, Profile profile) throws InterruptedException {
        final Profile data = em.find(Profile.class, id);
        if (data != null) {
            data.email = profile.email;
            data.password = profile.password;
            data.firstName = profile.firstName;
            data.lastName = profile.lastName;
            data.phone = profile.phone;
        }
        return data;
    }

    private Profile modifyPassword(EntityManager em, Long id, Password password) throws InterruptedException {
        final Profile data = em.find(Profile.class, id);
        if(BCrypt.checkpw(password.password, data.password)){
            data.password = BCrypt.hashpw(password.newPassword, BCrypt.gensalt());
            return data;
        }
        return null;

    }

    private Result delete(EntityManager em, Long id) {
        final Profile profile = em.find(Profile.class, id);
        if(profile != null) {
            em.remove(profile);
        }
        return ok();
    }

    private Stream<Profile> list(EntityManager em) {
        List<Profile> profiles = em.createQuery("SELECT p FROM Profile p", Profile.class).getResultList();
        return profiles.stream();
    }


    // Authorization
    public Optional<Profile> getUser(EntityManager em, String email) {
        Profile profile = em.createQuery("SELECT p FROM Profile p WHERE p.email="+email, Profile.class).getSingleResult();
        return Optional.ofNullable(profile);
    }

    public Optional<Profile> isUserExist(EntityManager em, Login login) {
        try {
            Profile profile = em.createQuery("SELECT p FROM Profile p WHERE p.email=" + "'" + login.email + "'", Profile.class).getSingleResult();
            if(BCrypt.checkpw(login.password, profile.password)){
                return Optional.ofNullable(profile);
            }
        } catch (Exception ex) {
            return null;
        }
        return null;
    }
}
