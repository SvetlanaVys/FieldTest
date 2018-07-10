package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Field;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import repositories.FieldRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static play.libs.Json.toJson;

public class FieldController extends Controller {

    private final FieldRepository fieldRepository;
    private final HttpExecutionContext ec;

    @Inject
    public FieldController(FieldRepository fieldRepository, HttpExecutionContext ec) {
        this.fieldRepository = fieldRepository;
        this.ec = ec;
    }

    public Result index() {
        return ok(views.html.index.render());
    }

    public CompletionStage<Result> list() {
        return fieldRepository.list().thenApplyAsync(fields -> {
            final List<Field> fieldList = fields.collect(Collectors.toList());
            return ok(Json.toJson(fieldList));
        }, ec.current());
    }

    public CompletionStage<Result> listAlive() {
        return fieldRepository.listAlive().thenApplyAsync(fields -> {
            final List<Field> fieldList = fields.collect(Collectors.toList());
            return ok(Json.toJson(fieldList));
        }, ec.current());
    }

    public CompletionStage<Result> create() {
        JsonNode json = request().body().asJson();
        final Field field = Json.fromJson(json, Field.class);
        return fieldRepository.add(field).thenApplyAsync(savedResource -> {
            return created(Json.toJson(savedResource));
        }, ec.current());
    }

    public CompletionStage<Result> update(String id) {
        JsonNode json = request().body().asJson();
        Field field = Json.fromJson(json, Field.class);
        return fieldRepository.update(Long.parseLong(id), field).thenApplyAsync(optionalResource -> {
            return ok(Json.toJson(optionalResource));
        }, ec.current());
    }

    public CompletionStage<Result> delete(String id) {
        return fieldRepository.delete(Long.parseLong(id));
    }

}
