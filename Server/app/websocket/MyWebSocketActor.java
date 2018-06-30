package websocket;
import akka.actor.*;
import v1.field.*;
import akka.actor.*;
import akka.japi.*;

public class MyWebSocketActor extends AbstractActor {

    public static Props props(ActorRef out) {
        return Props.create(MyWebSocketActor.class, out);
    }

    private final ActorRef out;

    public MyWebSocketActor(ActorRef out) {
        this.out = out;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(FieldData.class, message ->
                        out.tell("I received your message: " + message, self())
                )
                .build();
    }


}