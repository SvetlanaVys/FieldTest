package repositories;

import com.google.inject.ImplementedBy;
import models.Field;
import models.Response;

import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

@ImplementedBy(JPAResponseRepository.class)
public interface ResponseRepository {

    CompletionStage<Response> add(Response response);

    CompletionStage<Response> update(Long id, Response response);

    CompletionStage<Stream<Response>> list();

    CompletionStage<Stream<Response>> listByField(Field field);
}
