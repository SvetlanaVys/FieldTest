package v1.field;

import akka.actor.ActorSystem;
import play.libs.concurrent.CustomExecutionContext;

import javax.inject.Inject;

/**
 * Custom execution context wired to "post.repository" thread pool
 */
public class FieldExecutionContext extends CustomExecutionContext {

    @Inject
    public FieldExecutionContext(ActorSystem actorSystem) {
        super(actorSystem, "post.repository");
    }
}
