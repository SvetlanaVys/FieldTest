package v1.field;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.*;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@With(FieldAction.class)
public class FieldController extends Controller {

    private HttpExecutionContext ec;
    private FieldResourceHandler handler;

    @Inject
    public FieldController(HttpExecutionContext ec, FieldResourceHandler handler) {
        this.ec = ec;
        this.handler = handler;
    }

    public CompletionStage<Result> list() {
        return handler.find().thenApplyAsync(fields -> {
            final List<FieldResource> fieldList = fields.collect(Collectors.toList());
            return ok(Json.toJson(fieldList));
        }, ec.current());
    }

    public CompletionStage<Result> show(String id) {
        return handler.lookup(id).thenApplyAsync(optionalResource -> {
            return optionalResource.map(resource ->
                    ok(Json.toJson(resource))
            ).orElseGet(() ->
                    notFound()
            );
        }, ec.current());
    }

    public CompletionStage<Result> update(String id) {
        JsonNode json = request().body().asJson();
        FieldResource resource = Json.fromJson(json, FieldResource.class);
        System.out.print(id + "UPDATE");
        return handler.update(id, resource).thenApplyAsync(optionalResource -> {
            return optionalResource.map(r ->
                    ok(Json.toJson(r))
            ).orElseGet(() ->
                    notFound()
            );
        }, ec.current());
    }

    public CompletionStage<Result> create() {
        JsonNode json = request().body().asJson();
        final FieldResource resource = Json.fromJson(json, FieldResource.class);
        return handler.create(resource).thenApplyAsync(savedResource -> {
            return created(Json.toJson(savedResource));
        }, ec.current());
    }


}