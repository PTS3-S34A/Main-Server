package nl.soccar.mainserver.data.repository;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import nl.soccar.library.Statistics;
import nl.soccar.mainserver.data.contract.IStatisticsDataContract;
import org.slf4j.LoggerFactory;

/**
 * A StatisticsRepository object is used for manipulation of statistics data in
 * a persistence service.
 *
 * @author PTS34A
 */
public class StatisticsRepository extends Repository {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(StatisticsRepository.class);

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
     * Method that adds a given amount of goals to the given player stored in
     * the persistency service.
     *
     * @param username The username of the player whose amount of goals needs to
     * be increased.
     * @param goals The amount of goals that need to be added to the player.
     */
    public void addGoals(String username, int goals) {
        super.getPool().execute(() -> {
            context.addGoals(username, goals);
        });
    }

    /**
     * Method that adds a given amount of assists to the given player stored in
     * the persistency service.
     *
     * @param username The username of the player whose amount of assists needs
     * to be increased.
     * @param assists The amount of assists that need to be added to the player.
     */
    public void addAssists(String username, int assists) {
        super.getPool().execute(() -> {
            context.addAssists(username, assists);
        });
    }

    /**
     * Method that increments the amount of games won by a player stored in the
     * persistency service.
     *
     * @param username The username of the player whose amount of won games
     * needs to be incremented.
     */
    public void incrementGamesWon(String username) {
        super.getPool().execute(() -> {
            context.incrementGamesWon(username);
        });
    }

    /**
     * Method that increments the amount of games lost by a player stored in the
     * persistency service.
     *
     * @param username The username of the player whose amount of lost games
     * needs to be incremented.
     */
    public void incrementGamesLost(String username) {
        super.getPool().execute(() -> {
            context.incrementGamesLost(username);
        });
    }

    /**
     * Method that increments the amount of games played by a player stored in
     * the persistency service.
     *
     * @param username The username of the player whose amount of played games
     * needs to be incremented.
     */
    public void incrementGamesPlayed(String username) {
        super.getPool().execute(() -> {
            context.incrementGamesPlayed(username);
        });
    }

    /**
     * Method that gets the game statistics of a player stored in the
     * persistency service.
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
            LOGGER.warn("An error occurred while submitting a callable in the getStatistics method.", e);
            return null;
        }
    }

    /**
     * Method that gets the game statistics of all players from the persistency
     * service.
     *
     * @return A collection of all game statistics.
     */
    public ArrayList<Statistics> getAllStatistics() {
        try {
            Future<ArrayList<Statistics>> f = super.getPool().submit(() -> context.getAllStatistics());
            return f.get();
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.warn("An error occurred while submitting a callable in the getAllStatistics method.", e);
            return null;
        }
    }

}
