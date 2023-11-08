package ui;

import core.CA;
import core.City;

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
    private CA ca;

    /**
     * Create a new instance with this Scanner used to read input.
     * @param sc The Scanner instance for reading input.
     */
    public Cli(Scanner sc) {
        this.sc = sc;
    }

    /**
     * Get an integer from the user.
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
     * @return The array of cities.
     */
    private City[] readCities() {
        int nbCities;
        City[] cities;

        // Ask the number of cities
        do {
            nbCities = readInt("How many city do you want to have?");
            // Check if the number is valid
            if(nbCities < 1 || nbCities > 26) {
                System.out.println("The number of cities must be between 1 and 26 included!");
                nbCities = 0; // We will ask again
            }
        } while (nbCities == 0);

        cities = new City[nbCities];

        // Create cities with letters
        for(char c='A'; c<'A'+nbCities; c++) {
            cities[c-'A'] = new City(String.valueOf(c));
        }

        return cities;
    }

    private void roadManagerMenu() {}

    private void chargingPointManagerMenu() {}

    private void showCitiesWithChargingPoint() {}

    /**
     * Start the command line interface to interact with the user.
     */
    public void start() {
        City[] cities;

        // Get cities from the user
        cities = readCities();

        // Create CA

        // Launch road manager menu
        roadManagerMenu();
    }
}
