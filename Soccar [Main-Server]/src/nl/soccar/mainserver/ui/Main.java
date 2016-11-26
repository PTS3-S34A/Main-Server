package nl.soccar.mainserver.ui;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import nl.soccar.mainserver.rmi.server.MainServerForClient;
import nl.soccar.mainserver.rmi.server.MainServerForGameServer;
import nl.soccar.mainserver.util.DatabaseUtilities;
import nl.soccar.mainserver.util.RmiConstants;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.LoggerFactory;

/**
 * Entry point of the Main server application for te Soccar game.
 *
 * @author PTS34
 */
public class Main implements Runnable {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static final String EXIT_STRING = "exit";
    private static Thread t;

    private Registry r;
    private MainServerForClient mainServerForClient;
    private MainServerForGameServer mainServerForGameServer;

    /**
     * Constructor used for initiation of a Main object. Network communication
     * ports are being registered and stub-objects are created and bound for RMI
     * network communication.
     */
    public Main() {
        try {
            DatabaseUtilities.init();

            mainServerForClient = new MainServerForClient();
            r = LocateRegistry.createRegistry(RmiConstants.PORT_NUMBER_CLIENT);
            r.rebind(RmiConstants.BINDING_NAME_CLIENT, mainServerForClient);
            LOGGER.info("Registered MainServerForClient binding.");

            mainServerForGameServer = new MainServerForGameServer();
            r = LocateRegistry.createRegistry(RmiConstants.PORT_NUMBER_GAME_SERVER);
            r.rebind(RmiConstants.BINDING_NAME_GAME_SERVER, mainServerForGameServer);
            LOGGER.info("Registered MainServerForGameServer binding.");

        } catch (RemoteException e) {
            LOGGER.error("An error occurred while locating and/or binding the registry.", e);
        }
    }

    /**
     * Start the application, prints the welcome message, configures the logger
     * and starts a new thread that listens for keyboard input for closing the
     * application.
     *
     * @param args Commandline arguments that are not used.
     */
    public static void main(String[] args) {
        printWelcomeMessage();

        BasicConfigurator.configure();

        t = new Thread(new Main());
        t.start();
    }

    // Thread that scans keyboard input for exit keyword to quit the application.
    @Override
    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("TYPE 'EXIT' TO STOP THE SERVER");
            printDevider();
            while (true) {
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase(EXIT_STRING)) {
                    mainServerForClient.close();
                    mainServerForGameServer.close();
                    DatabaseUtilities.close();
                    break;
                }
            }
        }
    }

    /**
     * Prints the welcome message of the main server that includes the
     * IP-address of the host machine.
     */
    private static void printWelcomeMessage() {
        printDevider();
        System.out.println("MAIN SERVER STARTED");
        printIpAddress();
    }

    /**
     * Prints the IP-address of the host machine.
     */
    private static void printIpAddress() {
        try {
            System.out.printf("IP ADDRESS: %s %n", InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            LOGGER.error("An error occurred while printing the IP address.", e);
        }
    }

    /**
     * Prints a deviding line of dashes.
     */
    private static void printDevider() {
        System.out.println("------------------------------");
    }

}
