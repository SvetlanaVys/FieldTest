//package controllers;
//
//import akka.NotUsed;
//import akka.actor.ActorSystem;
//import akka.event.LoggingAdapter;
//import akka.japi.Pair;
//import akka.japi.pf.PFBuilder;
//import akka.stream.Materializer;
//import akka.stream.javadsl.*;
//import play.libs.F;
//import play.mvc.*;
//
//import java.util.List;
//import java.util.stream.Collectors;
//import play.libs.Json;
//import akka.event.Logging;
//
//import javax.inject.Inject;
//import java.net.URI;
//import java.util.concurrent.CompletableFuture;
//
//import org.webjars.play.WebJarsUtil;
//
//import v1.field.*;
///**
// * A very simple chat client using websockets.
// */
//
//public class HomeController extends Controller {
//
//    private final Flow<String, String, NotUsed> userFlow;
//    private final WebJarsUtil webJarsUtil;
//
//    private FieldResourceHandler handler;
//
//    @Inject
//    public HomeController(ActorSystem actorSystem,
//                          Materializer mat,
//                          WebJarsUtil webJarsUtil,
//                          FieldResourceHandler handler) {
//        this.handler = handler;
//        org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
//        LoggingAdapter logging = Logging.getLogger(actorSystem.eventStream(), logger.getName());
//
//        //noinspection unchecked
//        Source<String, Sink<String, NotUsed>> source = MergeHub.of(String.class)
//                .log("source", logging)
//                .recoverWithRetries(-1, new PFBuilder().match(Throwable.class, e -> Source.empty()).build());
//        Sink<String, Source<String, NotUsed>> sink = BroadcastHub.of(String.class);
//
//        Pair<Sink<String, NotUsed>, Source<String, NotUsed>> sinkSourcePair = source.toMat(sink, Keep.both()).run(mat);
//        Sink<String, NotUsed> chatSink = sinkSourcePair.first();
//        Source<String, NotUsed> chatSource = sinkSourcePair.second();
//        this.userFlow = Flow.fromSinkAndSource(chatSink, chatSource).log("userFlow", logging);
//
//        this.webJarsUtil = webJarsUtil;
//    }
//
//    public Result index() {
//        Http.Request request = request();
//        String url = routes.HomeController.chat().webSocketURL(request);
//        return Results.ok(views.html.index.render(url, webJarsUtil));
//    }
//
//    public WebSocket chat() {
//        return WebSocket.Text.acceptOrResult(request -> {
//            if (sameOriginCheck(request)) {
//                return CompletableFuture.completedFuture(F.Either.Right(userFlow));
//            } else {
//                return CompletableFuture.completedFuture(F.Either.Left(forbidden()));
//            }
//        });
//    }
//
//
//    /**
//     * Checks that the WebSocket comes from the same origin.  This is necessary to protect
//     * against Cross-Site WebSocket Hijacking as WebSocket does not implement Same Origin Policy.
//     *
//     * See https://tools.ietf.org/html/rfc6455#section-1.3 and
//     * http://blog.dewhurstsecurity.com/2013/08/30/security-testing-html5-websockets.html
//     */
//    private boolean sameOriginCheck(Http.RequestHeader request) {
//        String[] origins = request.headers().get("Origin");
////        String[] origins = request.headers().get("Access-Control-Allow-Origin");
//        if (origins.length > 1) {
//            // more than one origin found
//            return false;
//        }
//        String origin = origins[0];
//        return originMatches(origin);
//    }
//
//    private boolean originMatches(String origin) {
//        if (origin == null) return false;
//        try {
//            URI url = new URI(origin);
//            return url.getHost().equals("localhost")
//                    && (url.getPort() == 9000 || url.getPort() == 19001 || url.getPort() == 4200);
//        } catch (Exception e ) {
//            return false;
//        }
//    }
//
//}

package controllers;

import play.libs.streams.ActorFlow;
import play.mvc.*;
import akka.actor.*;
import akka.stream.*;
import javax.inject.Inject;
import play.mvc.WebSocket;
import websocket.MyWebSocketActor;
import akka.stream.javadsl.Flow;
import v1.field.FieldData;
import java.util.concurrent.CompletableFuture;
import play.libs.F;
import java.util.concurrent.CompletionStage;
import com.fasterxml.jackson.databind.JsonNode;
import akka.NotUsed;
import play.libs.F.Either;
/**
 * A very simple chat client using websockets.
 */

public class HomeController extends Controller {

    private final ActorSystem actorSystem;
    private final Materializer materializer;
    private FieldData fieldData;

    @Inject
    public HomeController(ActorSystem actorSystem, Materializer materializer) {
        this.actorSystem = actorSystem;
        this.materializer = materializer;
    }

    public WebSocket socket() {
        System.err.println("Connect");
        return WebSocket.Json.accept(request ->
                ActorFlow.actorRef(MyWebSocketActor::props,
                        actorSystem, materializer));
//        return WebSocket.Text.accept(request -> {
//
//            // log the message to stdout and send response back to client
//            return Flow.<String>create().map(msg -> {
//                System.out.println(msg);
//                return "I received your message: " + msg;
//            });
//        });
    }

    public Result index() {
        Http.Request request = request();
//        String url = routes.HomeController.chat().webSocketURL(request);
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

