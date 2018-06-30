package v1.field;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

public interface FieldRepository {

    CompletionStage<Stream<FieldData>> list();

    CompletionStage<FieldData> create(FieldData fieldData);

    CompletionStage<Optional<FieldData>> get(Long id);

    CompletionStage<Optional<FieldData>> update(Long id, FieldData fieldData);
}
