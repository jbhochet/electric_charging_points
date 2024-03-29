package ui;

import core.Algorithm;
import core.UrbanCommunity;
import core.City;
import core.ConfigParser;
import exceptions.AccessibilityException;

import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Cli {
    /**
     * An instance of Scanner used to read user input.
     */
    private Scanner sc;

    /**
     * The agglomeration community to work with.
     */
    private UrbanCommunity urbanCommunity;

    /**
     * Create a new instance with this Scanner used to read input.
     * 
     * @param sc The Scanner instance for reading input.
     */
    public Cli(Scanner sc) {
        this(sc, null);
    }

    public Cli(Scanner sc, UrbanCommunity urbanCommunity) {
        this.sc = sc;
        this.urbanCommunity = urbanCommunity;
    }

    /**
     * Get an integer from the user.
     * 
     * @param message The message that prints on the console for the user.
     * @return The integer entered by the user.
     */
    private int readInt(String message) {
        int res = 0;
        boolean valid = false;

        while (!valid) {
            try {
                System.out.println(message);
                res = sc.nextInt();
                sc.nextLine();
                valid = true;
            } catch (InputMismatchException err) {
                System.out.println("You must provide an integer!");
                sc.nextLine();
            }
        }

        return res;
    }

    /**
     * Get cities from the user and return an array.
     * 
     * @return The array of cities.
     */
    private City[] readCities() {
        int nbCities;
        City[] cities;

        // Ask the number of cities
        do {
            nbCities = readInt("How many cities do you want to have?");
            // Check if the number is valid
            if (nbCities < 1 || nbCities > 26) {
                System.out.println("The number of cities must be between 1 and 26 included!");
                nbCities = 0; // We will ask again
            }
            System.out.println();
        } while (nbCities == 0);

        cities = new City[nbCities];

        // Create cities with letters
        for (char c = 'A'; c < 'A' + nbCities; c++) {
            cities[c - 'A'] = new City(String.valueOf(c));
        }

        return cities;
    }

    /**
     * Lets the user create road between cities.
     */
    private void roadManagerMenu() {
        int choice;

        do {
            System.out.println("Do you want to add roads?");
            System.out.println("1) Add a road");
            System.out.println("2) Finish");
            choice = readInt("Enter your selection:");
            switch (choice) {
                case 1:
                    String city1, city2;
                    System.out.println("What is the first city?");
                    city1 = sc.nextLine();
                    System.out.println("What is the second city?");
                    city2 = sc.nextLine();
                    try {
                        urbanCommunity.addRoad(city1, city2);
                        System.out.printf("Road added between %s and %s.%n", city1, city2);
                    } catch (IllegalArgumentException err) {
                        System.out.println("The cities are not valid!");
                    }
                    break;
                case 2:
                    System.out.println("Roads are defined!");
                    break;
                default:
                    System.out.println("Unknown action!");
            }
            System.out.println();
        } while (choice != 2);
    }

    /**
     * Lets the user add charging points.
     */
    private void chargingPointManagerMenu() {
        int choice;
        String city;

        do {
            System.out.println(urbanCommunity);
            System.out.println("Do you want to edit charging point?");
            System.out.println("1) Add a charging point");
            System.out.println("2) Remove a charging point");
            System.out.println("3) Finish");

            choice = readInt("Enter your selection:");
            switch (choice) {
                case 1:
                    // User wants to add a charging point
                    System.out.println("Enter the name of the city:");
                    city = sc.nextLine();
                    try {
                        urbanCommunity.addChargingPoint(city);
                    } catch (IllegalArgumentException err) {
                        System.out.println(err.getMessage());
                    }
                    break;
                case 2:
                    // User wants to remove a charging point
                    System.out.println("Enter the name of the city:");
                    city = sc.nextLine();
                    try {
                        urbanCommunity.removeChargingPoint(city);
                    } catch (IllegalArgumentException | AccessibilityException err) {
                        System.out.println(err.getMessage());
                    }
                    break;
                case 3:
                    System.out.println("Charging points are defined!");
                    break;
                default:
                    System.out.println("Unknown action!");
            }
            System.out.println();
        } while (choice != 3);
        System.out.println(urbanCommunity);
    }

    private void chooseAlgo() {
        int choice;
        int nbIter;

        do {
            System.out.println("Which algo do you want to use?");
            System.out.println("1) Naive algo");
            System.out.println("2) Less naive algo");
            System.out.println("3) More optimized algo");
            System.out.println("4) Finish");

            choice = readInt("Enter your selection:");

            switch (choice) {
                case 1:
                    Algorithm.addAllChargingPoint(urbanCommunity);
                    nbIter = readInt("How many iterations?");
                    Algorithm.naiveAlgorithm(urbanCommunity, nbIter);
                    break;
                case 2:
                    Algorithm.addAllChargingPoint(urbanCommunity);
                    nbIter = readInt("How many iterations?");
                    Algorithm.lessNaiveAlgorithm(urbanCommunity, nbIter);
                    break;
                case 3:
                    Algorithm.addAllChargingPoint(urbanCommunity);
                    Algorithm.algoOpti(urbanCommunity);
                    break;
                default:
                    System.out.println("Invalid action!");
            }

            System.out.println();
        } while (choice < 1 || choice > 4);
    }

    /**
     * Lets the user choose what to do.
     */
    private void mainMenu() {
        int choice;

        do {
            System.out.println(urbanCommunity);

            System.out.println("What do you want to do?");
            System.out.println("1) Resolve manually");
            System.out.println("2) Resolve automatically");
            System.out.println("3) Save");
            System.out.println("4) Show urban community");
            System.out.println("5) Finish");

            choice = readInt("Enter your selection:");

            switch (choice) {
                case 1:
                    if (!urbanCommunity.isValid())
                        Algorithm.addAllChargingPoint(urbanCommunity);
                    chargingPointManagerMenu();
                    break;
                case 2:
                    chooseAlgo();
                    break;
                case 3:
                    // get the file name
                    String filename;
                    do {
                        System.out.println("Enter the name of the file: ");
                        filename = sc.nextLine();
                        if (filename.length() == 0) {
                            System.out.println("The file name could not be empty!");
                            filename = null;
                        }
                    } while (filename == null);
                    // save to the file
                    try {
                        ConfigParser.saveConfigFile(new File(filename), urbanCommunity);
                        System.out.printf("Urban community saved in '%s'!%n", filename);
                    } catch (IOException err) {
                        System.err.println("Can't access this file!");
                    }
                    break;
                case 4:
                    GraphVizUtil.displayGraph(urbanCommunity.toDot());
                    break;
                case 5:
                    System.out.println("Good bye!");
                    break;
                default:
                    System.out.println("Invalid action!");
            }
            System.out.println();
        } while (choice != 5);
    }

    /**
     * Start the command line interface to interact with the user.
     */
    public void start() {
        if (urbanCommunity == null) {
            City[] cities;

            // Get cities from the user
            cities = readCities();

            // Create UrbanCommunity
            urbanCommunity = new UrbanCommunity(cities);

            // Launch road manager menu
            roadManagerMenu();

            Algorithm.addAllChargingPoint(urbanCommunity);
        }

        mainMenu();
    }
}
