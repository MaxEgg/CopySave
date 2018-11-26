
package copybox.singleton;


/**
 * Collection of all the singletons. Got tired of instantiating them everywhere.
 */
public class Singleton {
    protected Settings settings = Settings.getInstance();
    protected Stages stages = Stages.getInstance();
    protected Storage storage = Storage.getInstance();
}
