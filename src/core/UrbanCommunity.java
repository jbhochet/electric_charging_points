package core;

import exceptions.AccessibilityException;
import graph.Graph;

import java.util.ArrayList;
import java.util.Random;

/**
 * Represents an urban community.
 *
 * <p>
 * It has an attribute named graph of type Graph that stores the connections
 * between the cities (the roads). It initializes when the constructor is
 * called and create un new Graph object.
 *
 * <p>
 * It has an attribute named cities of type City that stores the cities
 * of the urban community in a list.
 *
 * <p>
 * This class can add roads between cities, add or remove a charging point to a
 * city.
 *
 * @author Jean-Baptiste Hochet
 * @author Pablo Rican
 *
 * @see Graph
 * @see City
 */
public class UrbanCommunity {

    /**
     * Stores the connections (the roads) between the cities of the urban community.
     */
    private Graph graph;

    /**
     * Stores the cities of the urban community.
     */
    private City[] cities;

    /**
     * The constructor of the class UrbanCommunity.
     *
     * <p>
     * Creates a new UrbanCommunity object with the cities given in parameter.
     *
     * @param cities
     *               The cities in the urban community.
     */
    public UrbanCommunity(City[] cities) {
        this.cities = cities;
        graph = new Graph(cities.length);
    }

    /**
     * Add a road between two cities given in parameter.
     *
     * <p>
     * If one of the two cities given in parameter is not in the list "cities",
     * he method throws an Exception.
     *
     * <p>
     * If the cities given in parameter are the same cities (they have the same
     * index),
     * the methode throws an Exception.
     *
     * @param city1
     *              The name of the first city.
     *
     * @param city2
     *              The name of the second city.
     *
     * @throws IllegalArgumentException
     *                                  If one of the two cities given in parameter
     *                                  is not in the list "cities".
     *                                  If the cities given in parameter are the
     *                                  same cities (they have the same index).
     */
    public void addRoad(String city1, String city2) {
        int city1Index = getCityIndex(city1);
        int city2Index = getCityIndex(city2);

        if (city1Index == -1 || city2Index == -1) {
            throw new IllegalArgumentException("One of the parameters is not in the list 'cities'");
        }
        if (city1Index == city2Index) {
            throw new IllegalArgumentException("You cannot add a road between the same city");
        }

        graph.addEdge(city1Index, city2Index);
    }

    /**
     * Add a charging point to the city whose name is given in parameter.
     *
     * @param city
     *             The name of the city.
     */
    public void addChargingPoint(String city) {
        int indexCity = getCityIndex(city);

        if (indexCity == -1) {
            throw new IllegalArgumentException("The parameter is not in the list 'cities'");
        }

        if (cities[indexCity].hasChargingPoint()) {
            throw new IllegalArgumentException("This city already has a charging point");
        }

        cities[indexCity].addChargingPoint();
    }

    /**
     * Check if this city have a neighbor with a charging point
     * 
     * @param indexCity
     *                 The index of the city in the cities array
     *
     * @return True if this city have a neighbor with charging point
     */
    private boolean hasNeighborWithChargingPoint(int indexCity) {
        if (indexCity == -1) {
            throw new IllegalArgumentException("The parameter is not in the list 'cities'");
        }

        int[] neighbors = graph.neighbors(indexCity);
        boolean hasNeighborsPossessingChargingPoint = false;

        for (int indexNeighbor : neighbors) {
            if (cities[indexNeighbor].hasChargingPoint()) {
                hasNeighborsPossessingChargingPoint = true;
                break;
            }
        }

        return hasNeighborsPossessingChargingPoint;
    }

    /**
     * Remove the charging point to the city whose name is given in parameter.
     *
     * @param city
     *             The name of the city.
     *
     * @throws AccessibilityException
     *                                If the city you want to remove the charging
     *                                point from
     *                                does not have a neighbor possessing a charging
     *                                point.
     */
    public void removeChargingPoint(String city) throws AccessibilityException {
        int indexCity = getCityIndex(city);

        if (indexCity == -1) {
            throw new IllegalArgumentException("The parameter is not in the list 'cities'");
        }

        if (!cities[indexCity].hasChargingPoint()) {
            throw new IllegalArgumentException("This city has no charging point to remove");
        }

        if (hasNeighborWithChargingPoint(indexCity)) {
            cities[indexCity].removeChargingPoint();
            ArrayList<Integer> dependentCitiesIndex = new ArrayList<>();
            for (int neighbor : graph.neighbors(indexCity)) {
                if (!cities[neighbor].hasChargingPoint() && !hasNeighborWithChargingPoint(neighbor)) {
                    dependentCitiesIndex.add(neighbor);
                }
            }
            cities[indexCity].addChargingPoint();
            if (!dependentCitiesIndex.isEmpty()) {
                StringBuilder errorMessage = new StringBuilder("You cannot remove the charging point of this city ");
                errorMessage.append("because the following(s) neighbor depend(s) on this city :\n");
                for (int dependentCities : dependentCitiesIndex) {
                    errorMessage.append("- ").append(cities[dependentCities].getName()).append("\n");
                }
                throw new AccessibilityException(errorMessage.toString());
            }
        } else {
            throw new AccessibilityException("You cannot remove the charging point of this city because " +
                    "it does not have a neighbor possessing a charging point");
        }

        cities[indexCity].removeChargingPoint();
    }

    public void addAllChargingPoint() {
        for (City city : cities) {
            city.addChargingPoint();
        }
    }

    public void naiveAlgorithm(int numberIteration) throws AccessibilityException {
        int i = 0;
        while (i < numberIteration) {
            int randomIndex = new Random().nextInt(cities.length);
            City randomCity = cities[randomIndex];
            if (randomCity.hasChargingPoint()) {
                removeChargingPoint(randomCity.getName());
            } else {
                addChargingPoint(randomCity.getName());
            }
            i++;
        }
    }

    public void lessNaiveAlgorithm(int numberIteration) {
        int i = 0;
        int currentScore = urbanCommunityScore();

        while (i < numberIteration) {
            int randomIndex = new Random().nextInt(cities.length);
            City randomCity = cities[randomIndex];

            if (randomCity.hasChargingPoint()) {
                try {
                    removeChargingPoint(randomCity.getName());
                } catch (AccessibilityException ignored) {
                }
            } else {
                addChargingPoint(randomCity.getName());
            }

            if (urbanCommunityScore() < currentScore) {
                i = 0;
                currentScore = urbanCommunityScore();
            }
            i++;
        }
    }

    public int urbanCommunityScore() {
        int score = 0;
        for (City city : cities) {
            if (city.hasChargingPoint()) {
                score++;
            }
        }
        return score;
    }

    /**
     * Get the index of the city whose name is given in parameter.
     * If the name of the city given in parameters do not match any of the names of
     * the city in the list cities,
     * the method returns -1
     *
     * @param city
     *             The name of the city.
     *
     * @return - The integer -1 if the city does not exist<br>
     *         - The integer of the index else
     */
    public int getCityIndex(String city) {
        int cityIndex = -1;
        for (int i = 0; i < cities.length; i++) {
            if (cities[i].getName().equalsIgnoreCase(city)) {
                cityIndex = i;
                return cityIndex;
            }
        }

        return cityIndex;
    }

    /**
     * toString method of the class UrbanCommunity.
     *
     * <p>
     * The formatting of the String it returns is as follows :<br>
     * NameOfTheCity1 : does (or does not) have a charging point<br>
     * NameOfTheCity2 : does (or does not) have a charging point<br>
     * ...
     *
     * @return A string formatted to display the cities of the urban community,
     *         specifying if the city has a charging point.
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Display of the cities from this urban community :\n");
        for (City city : cities) {
            str.append(city.getName()).append(" : ");
            if (city.hasChargingPoint()) {
                str.append("does have a charging point\n");
            } else
                str.append("does not have a charging point\n");
        }

        return String.valueOf(str);
    }
}
