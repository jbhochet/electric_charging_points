package core;

import exceptions.AccessibilityException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public abstract class Algorithm {

    /**
     * The first and most naive solution to solve the problem. Add a charging point
     * in each city.
     * 
     * @param urbanCommunity The urban community.
     */
    public static void addAllChargingPoint(UrbanCommunity urbanCommunity) {
        for (City city : urbanCommunity.getCities()) {
            city.addChargingPoint();
        }
    }

    /**
     * Another solution to resolve the problem, remove charging points from a random
     * city at each iteration.
     * 
     * @param urbanCommunity  The urban community.
     * @param numberIteration The number of iterations, higher than the number of
     *                        cities is recommended.
     */
    public static void naiveAlgorithm(UrbanCommunity urbanCommunity, int numberIteration) {
        int i = 0;
        while (i < numberIteration) {
            int randomIndex = new Random().nextInt(urbanCommunity.getCities().length);
            City randomCity = urbanCommunity.getCities()[randomIndex];
            if (randomCity.hasChargingPoint()) {
                try {
                    urbanCommunity.removeChargingPoint(randomCity.getName());
                } catch (AccessibilityException ignored) {
                }
            } else {
                urbanCommunity.addChargingPoint(randomCity.getName());
            }
            i++;
        }
    }

    /**
     * Returns the list of city in the urban community who had a charging point.
     * 
     * @param urbanCommunity The urban community
     * @return The list of cities
     */
    private static Set<City> citiesWithChargingPoint(UrbanCommunity urbanCommunity) {
        Set<City> res = new HashSet<>();
        for (City city : urbanCommunity.getCities())
            if (city.hasChargingPoint())
                res.add(city);
        return res;
    }

    /**
     * An improved version of naiveAlgorithm. We take the result with the minimal
     * number of charging points.
     * 
     * @param urbanCommunity  The urban community.
     * @param numberIteration The number of iterations, higher than the number of
     *                        cities is recommended.
     */
    public static void lessNaiveAlgorithm(UrbanCommunity urbanCommunity, int numberIteration) {
        int i = 0;
        Set<City> citiesCharged = citiesWithChargingPoint(urbanCommunity); // our score is the size of this set

        // Search for the best score
        while (i < numberIteration) {
            int randomIndex = new Random().nextInt(urbanCommunity.getCities().length);
            City randomCity = urbanCommunity.getCities()[randomIndex];

            if (randomCity.hasChargingPoint()) {
                try {
                    urbanCommunity.removeChargingPoint(randomCity.getName());
                } catch (AccessibilityException ignored) {
                }
            } else {
                urbanCommunity.addChargingPoint(randomCity.getName());
            }

            if (citiesWithChargingPoint(urbanCommunity).size() < citiesCharged.size()) {
                i = 0;
                citiesCharged = citiesWithChargingPoint(urbanCommunity);
            } else {
                i++;
            }
        }

        // Apply the best solutions
        for (City city : urbanCommunity.getCities()) {
            // bypass the constraint but it's fine ;)
            if (citiesCharged.contains(city)) {
                city.addChargingPoint();
            } else {
                city.removeChargingPoint();
            }
        }
    }

    /**
     * Sorts cities by degree and choose the order.
     * 
     * @param urbanCommunity The urban community.
     * @param city           The cities array to sort.
     * @param decr           If true, the array is sorted in the descending order.
     * @return
     */
    private static City[] sortCitiesByDegree(UrbanCommunity urbanCommunity, City[] city, boolean decr) {
        City[] res = urbanCommunity.getCities().clone();
        Arrays.sort(res, (x, y) -> {
            int degreeX = urbanCommunity.getNeighbors(x.getName()).length;
            int degreeY = urbanCommunity.getNeighbors(y.getName()).length;
            if (decr)
                return degreeY - degreeX;
            else
                return degreeX - degreeY;
        });

        return res;
    }

    /**
     * A better solution to find the solution. It's use a heuristic.
     * 
     * @param urbanCommunity The urban community.
     */
    public static void algoOpti(UrbanCommunity urbanCommunity) {
        City[] cities = sortCitiesByDegree(urbanCommunity, urbanCommunity.getCities(), false);

        for (City city : cities) {
            if (city.hasChargingPoint()) {
                try {
                    urbanCommunity.removeChargingPoint(city.getName());
                } catch (AccessibilityException ignored) {
                }
            }
        }
    }
}
