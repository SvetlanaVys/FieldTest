package filters;

        import play.mvc.EssentialAction;
        import play.mvc.EssentialFilter;

        import javax.inject.Inject;
        import javax.inject.Singleton;
        import java.util.concurrent.Executor;

/**
 * This is a simple filter that adds a header to all requests.
 */
@Singleton
public class ExampleFilter extends EssentialFilter {

    private final Executor exec;

    /**
     * @param exec This class is needed to execute code asynchronously.
     */
    @Inject
    public ExampleFilter(Executor exec) {
        this.exec = exec;
    }

    @Override
    public EssentialAction apply(EssentialAction next) {
        return EssentialAction.of(request ->
                next.apply(request).map(result ->
                        result.withHeader("Access-Control-Allow-Origin", "http://localhost:4200"), exec)
                        .map(result ->
                                result.withHeader("Access-Control-Allow-Credentials", "true"), exec)
        );
    }
}
