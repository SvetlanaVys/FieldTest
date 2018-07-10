package repositories;
import com.google.inject.ImplementedBy;
import models.Field;
import models.Option;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

@ImplementedBy(JPAOptionRepository.class)
public interface OptionRepository {
    CompletionStage<Option> add(Option option);

    CompletionStage<Option> update(Long id, Option option);

    CompletionStage<Result> delete(Long id);

    CompletionStage<Stream<Option>> list();

    CompletionStage<Stream<Option>> listByField(Field field);
}
