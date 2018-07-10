package controllers;

import models.Field;
import play.mvc.*;
import akka.actor.*;
import akka.stream.*;
import javax.inject.Inject;

public class HomeController extends Controller {

    private final ActorSystem actorSystem;
    private final Materializer materializer;
    private Field fieldData;

    @Inject
    public HomeController(ActorSystem actorSystem, Materializer materializer) {
        this.actorSystem = actorSystem;
        this.materializer = materializer;
    }


    public Result index() {
        Http.Request request = request();
        return Results.ok(views.html.index.render());
    }

    public Result options(String path ) {
        return ok("")
                .withHeader("Access-Control-Allow-Origin",  "*")
                .withHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .withHeader("Access-Control-Allow-Headers", "Accept, Origin, Content-type, X-Json, X-Prototype-Version, X-Requested-With")
                .withHeader("Access-Control-Allow-Credentials", "true");

    }

}

