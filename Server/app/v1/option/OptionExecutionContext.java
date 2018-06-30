package v1.option;

import akka.actor.ActorSystem;
import play.libs.concurrent.CustomExecutionContext;

import javax.inject.Inject;

/**
 * Custom execution context wired to "post.repository" thread pool
 */
public class OptionExecutionContext extends CustomExecutionContext {
    @Inject
    public OptionExecutionContext(ActorSystem actorSystem) {
        super(actorSystem, "post.repository");
    }
}
