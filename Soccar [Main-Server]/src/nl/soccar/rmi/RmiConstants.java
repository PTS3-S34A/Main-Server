package nl.soccar.rmi;

/**
 * Class that constains constant values regarding to RMI network communication.
 *
 * @author PTS34A
 */
public class RmiConstants {

    /**
     * Binding name constants.
     */
    public static final String BINDING_NAME_MAIN_SERVER_FOR_CLIENT = "main_server_for_client";
    public static final String BINDING_NAME_MAIN_SERVER_FOR_GAME_SERVER = "main_server_for_game_server";
    public static final String BINDING_NAME_GAME_SERVER_FOR_MAIN_SERVER = "game_server_for_main_server";

    /**
     * Port number constants.
     */
    public static final int PORT_NUMBER_CLIENT = 1044;
    public static final int PORT_NUMBER_GAME_SERVER = 1045;

    /**
     * Constructor that is intentionally marked private so a RmiConstants object
     * can never be initiated outside this class.
     */
    private RmiConstants() {
    }

}
