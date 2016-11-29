package nl.soccar.mainserver.data.repository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Astract class that serves as base for all data repositories. It provides a
 * cached thread pool that is used to prevent blocking the main thread with
 * database query's.
 *
 * @author PTS34A
 */
public abstract class Repository {

    private static final Logger LOGGER = Logger.getLogger(Repository.class.getSimpleName());

    private final ExecutorService pool;

    /**
     * Constructor that serves as base for all data repositories. It creates a
     * cached thread pool.
     */
    public Repository() {
        pool = Executors.newCachedThreadPool();
        LOGGER.log(Level.INFO, "Threadpool {0} initialized.", this.getClass().getSimpleName());
    }

    /**
     * Shuts the cached thread pool down.
     */
    public void close() {
        pool.shutdown();
        LOGGER.log(Level.INFO, "Threadpool {0} shut down.", this.getClass().getSimpleName());
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
