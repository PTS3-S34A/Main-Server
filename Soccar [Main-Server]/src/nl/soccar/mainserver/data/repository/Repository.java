package nl.soccar.mainserver.data.repository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.LoggerFactory;

/**
 * Astract class that serves as base for all data repositories. It provides a
 * cached thread pool that is used to prevent blocking the main thread with
 * database query's.
 *
 * @author PTS34A
 */
public abstract class Repository {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Repository.class);

    private final ExecutorService pool;

    /**
     * Constructor that serves as base for all data repositories. It creates a
     * cached thread pool.
     */
    public Repository() {
        pool = Executors.newCachedThreadPool();
        LOGGER.info("Threadpool " + this.getClass().getSimpleName() + " initialized.");
    }

    /**
     * Shuts the cached thread pool down.
     */
    public void close() {
        pool.shutdown();
        LOGGER.info("Threadpool " + this.getClass().getSimpleName() + " shut down.");
    }

    /**
     * Gets the cached thread pool.
     *
     * @return The cached thread pool.
     */
    public ExecutorService getPool() {
        return pool;
    }

}
