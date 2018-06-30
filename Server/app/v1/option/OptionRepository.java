package v1.option;

import v1.field.FieldData;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

public interface OptionRepository {

    CompletionStage<Stream<OptionData>> listOptions();

    CompletionStage<Stream<OptionData>> list(FieldData field);

    CompletionStage<OptionData> create(OptionData optionData);

    CompletionStage<Optional<OptionData>> get(Long id);

    CompletionStage<Optional<OptionData>> update(Long id, OptionData optionData);
}
