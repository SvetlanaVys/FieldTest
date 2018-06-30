package v1.field;

import com.palominolabs.http.url.UrlBuilder;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;

import javax.inject.Inject;
import java.nio.charset.CharacterCodingException;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

/**
 * Handles presentation of Field resources, which map to JSON.
 */
public class FieldResourceHandler {

    private final FieldRepository repository;
    private final HttpExecutionContext ec;

    @Inject
    public FieldResourceHandler(FieldRepository repository, HttpExecutionContext ec) {
        this.repository = repository;
        this.ec = ec;
    }

    public CompletionStage<Stream<FieldResource>> find() {
        return repository.list().thenApplyAsync(postDataStream -> {
            return postDataStream.map(data -> new FieldResource(data, link(data)));
        }, ec.current());
    }

//    public CompletionStage<Stream<FieldResource>> find() {
//        return repository.list().thenApplyAsync(postDataStream -> {
//            return postDataStream.map(data -> new FieldResource(data, link(data)));
//        });
//    }

    public CompletionStage<FieldResource> create(FieldResource resource) {
        final FieldData data = new FieldData(resource.getLabel(), resource.getType(),
                resource.getRequired(), resource.getIsActive(), resource.getIsDeleted());
        return repository.create(data).thenApplyAsync(savedData -> {
            return new FieldResource(savedData, link(savedData));
        }, ec.current());
    }


    public CompletionStage<Optional<FieldResource>> lookup(String id) {
        return repository.get(Long.parseLong(id)).thenApplyAsync(optionalData -> {
            return optionalData.map(data -> new FieldResource(data, link(data)));
        }, ec.current());
    }

    public CompletionStage<Optional<FieldResource>> update(String id, FieldResource resource) {
        final FieldData data = new FieldData(resource.getLabel(), resource.getType(),
                resource.getRequired(), resource.getIsActive(), resource.getIsDeleted());
        return repository.update(Long.parseLong(id), data).thenApplyAsync(optionalData -> {
            return optionalData.map(op -> new FieldResource(op, link(op)));
        }, ec.current());
    }

    private String link(FieldData data) {
        // Make a point of using request context here
        final Http.Request request = Http.Context.current().request();
        final String[] hostPort = request.host().split(":");
        String host = hostPort[0];
        int port = (hostPort.length == 2) ? Integer.parseInt(hostPort[1]) : -1;
        final String scheme = request.secure() ? "https" : "http";
        try {
            return UrlBuilder.forHost(scheme, host, port)
                    .pathSegments("v1", "fields", data.id.toString())
                    .toUrlString();
        } catch (CharacterCodingException e) {
            throw new IllegalStateException(e);
        }
    }
}
