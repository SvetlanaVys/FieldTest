package repositories;

import com.google.inject.ImplementedBy;
import models.Login;
import models.Password;
import models.Profile;
import play.mvc.Result;

import javax.persistence.EntityManager;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

@ImplementedBy(JPAProfileRepository.class)
public interface ProfileRepository {
    CompletionStage<Profile> add(Profile profile);

    CompletionStage<Profile> update(Long id, Profile profile);

    CompletionStage<Profile> updatePassword(Long id, Password password);

    CompletionStage<Result> delete(Long id);

    CompletionStage<Stream<Profile>> list();

    CompletionStage<Optional<Profile>> getByEmail(String email);

    CompletionStage<Optional<Profile>> isUser(Login userLogin);
}
