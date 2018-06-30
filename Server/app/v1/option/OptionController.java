package v1.option;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.*;
import v1.field.FieldData;
import v1.post.PostResource;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@With(OptionAction.class)
public class OptionController extends Controller {

    private HttpExecutionContext ec;
    private OptionResourceHandler handler;

    @Inject
    public OptionController(HttpExecutionContext ec, OptionResourceHandler handler) {
        this.ec = ec;
        this.handler = handler;
    }

    public CompletionStage<Result> listOptions() {
        return handler.findOptions().thenApplyAsync(posts -> {
            final List<OptionResource> optionList = posts.collect(Collectors.toList());
            return ok(Json.toJson(optionList));
        }, ec.current());
    }

    public CompletionStage<Result> list() {
        JsonNode json = request().body().asJson();
        FieldData data = Json.fromJson(json, FieldData.class);
        return handler.find(data).thenApplyAsync(posts -> {
            final List<OptionResource> optionList = posts.collect(Collectors.toList());
            return ok(Json.toJson(optionList));
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
        OptionResource resource = Json.fromJson(json, OptionResource.class);
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
        final OptionResource resource = Json.fromJson(json, OptionResource.class);
        System.out.println(resource + "CREATE");
        return handler.create(resource).thenApplyAsync(savedResource -> {
            return created(Json.toJson(savedResource));
        }, ec.current());
    }
}
