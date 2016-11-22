package nl.soccar.mainserver.util;

/**
 * Class that constains constant values regarding to RMI network communication.
 *
 * @author PTS34A
 */
public class RmiConstants {

    /**
     * Constructor that is intentionally marked private so a RmiConstants object
     * can never be initiated outside this class.
     */
    private RmiConstants() {
    }

    /**
     * Binding name constants.
     */
    public static final String BINDING_NAME_CLIENT = "main_server_for_client";
    public static final String BINDING_NAME_GAME_SERVER = "main_server_for_game_server";

    /**
     * Port number constants.
     */
    public static final int PORT_NUMBER_CLIENT = 1044;
    public static final int PORT_NUMBER_GAME_SERVER = 1045;

}
