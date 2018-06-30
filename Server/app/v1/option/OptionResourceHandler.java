package v1.option;

import com.palominolabs.http.url.UrlBuilder;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;
import v1.field.FieldData;
import v1.post.PostData;

import javax.inject.Inject;
import java.nio.charset.CharacterCodingException;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

/**
 * Handles presentation of Option resources, which map to JSON.
 */
public class OptionResourceHandler {

    private final OptionRepository repository;
    private final HttpExecutionContext ec;

    @Inject
    public OptionResourceHandler(OptionRepository repository, HttpExecutionContext ec) {
        this.repository = repository;
        this.ec = ec;
    }

    public CompletionStage<Stream<OptionResource>> findOptions() {
        return repository.listOptions().thenApplyAsync(postDataStream -> {
            return postDataStream.map(data -> new OptionResource(data, link(data)));
        }, ec.current());
    }

    public CompletionStage<Stream<OptionResource>> find(FieldData field) {
        return repository.list(field).thenApplyAsync(postDataStream -> {
            return postDataStream.map(data -> new OptionResource(data, link(data)));
        }, ec.current());
    }

    public CompletionStage<OptionResource> create(OptionResource resource) {
        final OptionData data = new OptionData(resource.getName(), resource.getField());
        return repository.create(data).thenApplyAsync(savedData -> {
            return new OptionResource(savedData, link(savedData));
        }, ec.current());
    }

    public CompletionStage<Optional<OptionResource>> lookup(String id) {
        return repository.get(Long.parseLong(id)).thenApplyAsync(optionalData -> {
            return optionalData.map(data -> new OptionResource(data, link(data)));
        }, ec.current());
    }

    public CompletionStage<Optional<OptionResource>> update(String id, OptionResource resource) {
        final OptionData data = new OptionData(resource.getName(), resource.getField());
        return repository.update(Long.parseLong(id), data).thenApplyAsync(optionalData -> {
            return optionalData.map(op -> new OptionResource(op, link(op)));
        }, ec.current());
    }

    private String link(OptionData data) {
        // Make a point of using request context here, even if it's a bit strange
        final Http.Request request = Http.Context.current().request();
        final String[] hostPort = request.host().split(":");
        String host = hostPort[0];
        int port = (hostPort.length == 2) ? Integer.parseInt(hostPort[1]) : -1;
        final String scheme = request.secure() ? "https" : "http";
        try {
            return UrlBuilder.forHost(scheme, host, port)
                    .pathSegments("v1", "options", data.id.toString())
                    .toUrlString();
        } catch (CharacterCodingException e) {
            throw new IllegalStateException(e);
        }
    }
}
