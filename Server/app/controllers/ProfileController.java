package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Login;
import models.Password;
import models.Profile;
import org.mindrot.jbcrypt.BCrypt;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import repositories.ProfileRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static play.libs.Json.toJson;

public class ProfileController extends Controller {
    private final ProfileRepository profileRepository;
    private final HttpExecutionContext ec;

    @Inject
    public ProfileController(ProfileRepository profileRepository, HttpExecutionContext ec) {
        this.profileRepository = profileRepository;
        this.ec = ec;
    }

    public Result index() {
        return ok(views.html.index.render());
    }

    public CompletionStage<Result> list() {
        return profileRepository.list().thenApplyAsync(profiles -> {
            final List<Profile> profileList = profiles.collect(Collectors.toList());
            return ok(Json.toJson(profileList));
        }, ec.current());
    }

    public CompletionStage<Result> create() {
        JsonNode json = request().body().asJson();
        final Profile profile = Json.fromJson(json, Profile.class);
        profile.password = BCrypt.hashpw(profile.password, BCrypt.gensalt());
        return profileRepository.add(profile).thenApplyAsync(savedResource -> {
            if(savedResource != null){
                return created(Json.toJson(savedResource));
            } else {
                return badRequest();
            }

        }, ec.current());
    }

    public CompletionStage<Result> update(String id) {
        JsonNode json = request().body().asJson();
        Profile profile = Json.fromJson(json, Profile.class);
        return profileRepository.update(Long.parseLong(id), profile).thenApplyAsync(optionalResource -> {
            return ok(Json.toJson(optionalResource));
        }, ec.current());
    }

    public CompletionStage<Result> updatePassword(String id) {
        JsonNode json = request().body().asJson();
        Password password = Json.fromJson(json, Password.class);
        return profileRepository.updatePassword(Long.parseLong(id), password).thenApplyAsync(optionalResource -> {
            if(optionalResource != null){
                return ok(Json.toJson(optionalResource));
            } else {
                return badRequest();
            }
        }, ec.current());
    }

    public CompletionStage<Result> delete(String id) {
        return profileRepository.delete(Long.parseLong(id));
    }

    public CompletionStage<Result> getProfiles() {
        return profileRepository.list().thenApplyAsync(optionStream -> {
            return ok(toJson(optionStream.collect(Collectors.toList())));
        }, ec.current());
    }

    // Authenticated
    public CompletionStage<Result> logIn() {
        JsonNode json = request().body().asJson();
        Login login = Json.fromJson(json, Login.class);
        return profileRepository.isUser(login).thenApplyAsync(resource -> {
                if (resource != null) {
                    session().clear();
                    session("email", login.email);
                    return ok(Json.toJson(resource));
                }
                return badRequest();
        }, ec.current());
    }

    public Result logout() {
        session().clear();
        return ok();
    }
}
