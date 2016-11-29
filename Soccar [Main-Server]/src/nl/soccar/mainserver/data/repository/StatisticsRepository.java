package nl.soccar.mainserver.data.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.soccar.library.Statistics;
import nl.soccar.mainserver.data.contract.IStatisticsDataContract;

/**
 * A StatisticsRepository object is used for manipulation of statistics data in
 * a persistence service.
 *
 * @author PTS34A
 */
public class StatisticsRepository extends Repository {

    private static final Logger LOGGER = Logger.getLogger(StatisticsRepository.class.getSimpleName());

    private final IStatisticsDataContract context;

    /**
     * Constructor used for initiation of a StatisticsGameServerDataRepository
     * object that can be used for manipulation of statistics data in a
     * persistence service that is passed in as argument by a Game server.
     *
     * @param context The persistance service (context) that this repository
     * communicates with.
     */
    public StatisticsRepository(IStatisticsDataContract context) {
        this.context = context;
    }

    /**
     * Adds a given amount of goals to the given player stored in the
     * persistency service.
     *
     * @param username The username of the player whose amount of goals needs to
     * be increased.
     * @param goals The amount of goals that need to be added to the player.
     */
    public void addGoals(String username, int goals) {
        super.getPool().execute(() -> context.addGoals(username, goals));
    }

    /**
     * Adds a given amount of assists to the given player stored in the
     * persistency service.
     *
     * @param username The username of the player whose amount of assists needs
     * to be increased.
     * @param assists The amount of assists that need to be added to the player.
     */
    public void addAssists(String username, int assists) {
        super.getPool().execute(() -> context.addAssists(username, assists));
    }

    /**
     * Increments the amount of games won by a player stored in the persistency
     * service.
     *
     * @param username The username of the player whose amount of won games
     * needs to be incremented.
     */
    public void incrementGamesWon(String username) {
        super.getPool().execute(() -> context.incrementGamesWon(username));
    }

    /**
     * Increments the amount of games lost by a player stored in the persistency
     * service.
     *
     * @param username The username of the player whose amount of lost games
     * needs to be incremented.
     */
    public void incrementGamesLost(String username) {
        super.getPool().execute(() -> context.incrementGamesLost(username));
    }

    /**
     * Increments the amount of games played by a player stored in the
     * persistency service.
     *
     * @param username The username of the player whose amount of played games
     * needs to be incremented.
     */
    public void incrementGamesPlayed(String username) {
        super.getPool().execute(() -> context.incrementGamesPlayed(username));
    }

    /**
     * Gets the game statistics of a player stored in the persistency service.
     *
     * @param username The username of the player whose statistics needs to be
     * retrieved from the persistency service.
     * @return The statistics of the player.
     */
    public Statistics getStatistics(String username) {
        try {
            Future<Statistics> f = super.getPool().submit(() -> context.getStatistics(username));
            return f.get();
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.log(Level.WARNING, "An error occurred while submitting a callable in the getStatistics method.", e);
        }
        
        return null;
    }

    /**
     * Gets the game statistics of all players from the persistency service.
     *
     * @return A collection of all game statistics.
     */
    public List<Statistics> getAllStatistics() {
        List<Statistics> statistics = new ArrayList<>();
        try {
            Future<List<Statistics>> f = super.getPool().submit(context::getAllStatistics);
            statistics = f.get();
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.log(Level.WARNING, "An error occurred while submitting a callable in the getAllStatistics method.", e);
        }
        
        return statistics;
    }

}
