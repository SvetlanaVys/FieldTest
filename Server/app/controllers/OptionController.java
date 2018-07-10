package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Field;
import models.Option;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import repositories.OptionRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static play.libs.Json.toJson;

public class OptionController extends Controller {
    private final OptionRepository optionRepository;
    private final HttpExecutionContext ec;

    @Inject
    public OptionController(OptionRepository optionRepository, HttpExecutionContext ec) {
        this.optionRepository = optionRepository;
        this.ec = ec;
    }

    public Result index() {
        return ok(views.html.index.render());
    }

    public CompletionStage<Result> list() {
        return optionRepository.list().thenApplyAsync(options -> {
            final List<Option> optionList = options.collect(Collectors.toList());
            return ok(Json.toJson(optionList));
        }, ec.current());
    }

    public CompletionStage<Result> listByField() {
        JsonNode json = request().body().asJson();
        final Field field = Json.fromJson(json, Field.class);
        return optionRepository.listByField(field).thenApplyAsync(options -> {
            final List<Option> optionList = options.collect(Collectors.toList());
            return ok(Json.toJson(optionList));
        }, ec.current());
    }

    public CompletionStage<Result> create() {
        JsonNode json = request().body().asJson();
        final Option option = Json.fromJson(json, Option.class);
        return optionRepository.add(option).thenApplyAsync(savedResource -> {
            return created(Json.toJson(savedResource));
        }, ec.current());
    }

    public CompletionStage<Result> update(String id) {
        JsonNode json = request().body().asJson();
        Option option = Json.fromJson(json, Option.class);
        return optionRepository.update(Long.parseLong(id), option).thenApplyAsync(optionalResource -> {
            return ok(Json.toJson(optionalResource));
        }, ec.current());
    }

    public CompletionStage<Result> delete(String id) {
        return optionRepository.delete(Long.parseLong(id));
    }

    public CompletionStage<Result> getOptions() {
        return optionRepository.list().thenApplyAsync(optionStream -> {
            return ok(toJson(optionStream.collect(Collectors.toList())));
        }, ec.current());
    }
}
