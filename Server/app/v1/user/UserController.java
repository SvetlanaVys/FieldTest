package v1.user;

import com.fasterxml.jackson.databind.JsonNode;
//import org.mindrot.jbcrypt.BCrypt;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.*;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@With(UserAction.class)
public class UserController extends Controller {

    private HttpExecutionContext ec;
    private UserResourceHandler handler;

    @Inject
    public UserController(HttpExecutionContext ec, UserResourceHandler handler) {
        this.ec = ec;
        this.handler = handler;
    }

    public CompletionStage<Result> list() {
        return handler.find().thenApplyAsync(users -> {
            final List<UserResource> userList = users.collect(Collectors.toList());
            return ok(Json.toJson(userList));
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

    public CompletionStage<Result> showByEmail() {
        JsonNode json = request().body().asJson();
        UserLoginResource resource = Json.fromJson(json, UserLoginResource.class);
        return handler.lookupByEmail(resource).thenApplyAsync(users -> {
            final List<UserResource> userList = users.collect(Collectors.toList());
            return ok(Json.toJson(userList));
        }, ec.current());
    }

    public CompletionStage<Result> update(String id) {
        JsonNode json = request().body().asJson();
        UserResource resource = Json.fromJson(json, UserResource.class);
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
        final UserResource resource = Json.fromJson(json, UserResource.class);
//        resource.setPassword(BCrypt.hashpw(resource.getPassword(), BCrypt.gensalt()));
        return handler.create(resource).thenApplyAsync(savedResource -> {
            return created(Json.toJson(savedResource));
        }, ec.current());

    }
}