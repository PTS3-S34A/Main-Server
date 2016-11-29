package nl.soccar.mainserver.ui;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.soccar.mainserver.rmi.server.MainServerController;

/**
 * Entry point of the Main server application for te Soccar game.
 *
 * @author PTS34
 */
public class Main implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getSimpleName());
    
    private static final String CONNECT_GAMESERVERS_COMMAND = "connect";
    private static final String COMMAND_EXIT = "exit";

    private final MainServerController controller;
    private static Thread t;

    /**
     * Constructor used for initiation of a Main object. The logger is being
     * configured, the welcome message is printed and the MainServerController
     * is instantiated.
     */
    public Main() {
        printWelcomeMessage();
        controller = new MainServerController();
    }

    /**
     * Start the application and starts a new thread that listens for keyboard
     * input for closing the application.
     *
     * @param args Commandline arguments that are not used.
     */
    public static void main(String[] args) {
        t = new Thread(new Main());
        t.start();
    }

    // Thread that scans keyboard input for exit keyword to quit the application.
    @Override
    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            printDevider();
            while (true) {
                String input = scanner.nextLine();
                if (processInput(input)) {
                    break;
                }
            }
        }
    }

    /**
     * Processes the input that the user types in the command line.
     *
     * @param input The input that the user types in the command line.
     * @return True when the exit command is recognized.
     */
    private boolean processInput(String input) {
        if (input.equalsIgnoreCase(COMMAND_EXIT)) {
            controller.close();
            return true;
        } else if (input.equalsIgnoreCase(CONNECT_GAMESERVERS_COMMAND)) {
            if (controller.connectGameServers()) {
                LOGGER.log(Level.INFO, "Game server(s) connected successfully.");
            } else {
                LOGGER.log(Level.WARNING, "Game servers could not be connected successfully.");
            }
        }
        return false;
    }

    /**
     * Prints the welcome message of the main server that includes the
     * IP-address of the host machine.
     */
    private static void printWelcomeMessage() {
        printDevider();
        System.out.println("MAIN SERVER STARTED");
        printIpAddress();
        System.out.println("TYPE 'EXIT' TO STOP THE SERVER");
        printDevider();
    }

    /**
     * Prints the IP-address of the host machine.
     */
    private static void printIpAddress() {
        try {
            System.out.printf("IP ADDRESS: %s %n", InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while printing the IP address.", e);
        }
    }

    /**
     * Prints a deviding line of dashes.
     */
    private static void printDevider() {
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
    }

}
