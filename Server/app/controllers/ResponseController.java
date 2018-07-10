package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Field;
import models.Response;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import repositories.ResponseRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static play.libs.Json.toJson;



public class ResponseController extends Controller {
    private final ResponseRepository responseRepository;
    private final HttpExecutionContext ec;

    @Inject
    public ResponseController(ResponseRepository responseRepository, HttpExecutionContext ec) {
        this.responseRepository = responseRepository;
        this.ec = ec;
    }

    public Result index() {
        return ok(views.html.index.render());
    }

    public CompletionStage<Result> list() {
        return responseRepository.list().thenApplyAsync(responses -> {
            final List<Response> responseList = responses.collect(Collectors.toList());
            return ok(Json.toJson(responseList));
        }, ec.current());
    }

    public CompletionStage<Result> listByField() {
        JsonNode json = request().body().asJson();
        final Field field = Json.fromJson(json, Field.class);
        return responseRepository.listByField(field).thenApplyAsync(responses -> {
            final List<Response> responseList = responses.collect(Collectors.toList());
            return ok(Json.toJson(responseList));
        }, ec.current());
    }

    public CompletionStage<Result> create() {
        JsonNode json = request().body().asJson();
        final Response response = Json.fromJson(json, Response.class);
        return responseRepository.add(response).thenApplyAsync(savedResource -> {
            return created(Json.toJson(savedResource));
        }, ec.current());
    }

    public CompletionStage<Result> update(String id) {
        JsonNode json = request().body().asJson();
        Response response = Json.fromJson(json, Response.class);
        return responseRepository.update(Long.parseLong(id), response).thenApplyAsync(optionalResource -> {
            return ok(Json.toJson(optionalResource));
        }, ec.current());
    }
}
