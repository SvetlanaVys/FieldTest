package repositories;

import com.google.inject.ImplementedBy;
import models.Field;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

@ImplementedBy(JPAFieldRepository.class)
public interface FieldRepository {
    CompletionStage<Field> add(Field field);

    CompletionStage<Field> update(Long id, Field field);

    CompletionStage<Result> delete(Long id);

    CompletionStage<Stream<Field>> list();

    CompletionStage<Stream<Field>> listAlive();
}
