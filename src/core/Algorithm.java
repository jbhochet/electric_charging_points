package core;

import exceptions.AccessibilityException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public abstract class Algorithm {

    public static void addAllChargingPoint(UrbanCommunity urbanCommunity) {
        for (City city : urbanCommunity.getCities()) {
            city.addChargingPoint();
        }
    }

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

    private static Set<City> citiesWithChargingPoint(UrbanCommunity urbanCommunity) {
        Set<City> res = new HashSet<>();
        for (City city : urbanCommunity.getCities())
            if (city.hasChargingPoint())
                res.add(city);
        return res;
    }

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

    public static void algoOpti(UrbanCommunity urbanCommunity) {
        City[] cities = sortCitiesByDegree(urbanCommunity, urbanCommunity.getCities(), true);

        for (City city : cities) {
            City[] neighbors = sortCitiesByDegree(urbanCommunity, urbanCommunity.getNeighbors(city.getName()), false);
            for (City neighbor : neighbors) {
                if (neighbor.hasChargingPoint()) {
                    try {
                        urbanCommunity.removeChargingPoint(neighbor.getName());
                    } catch (AccessibilityException ignored) {
                    }
                }
            }
        }
    }
}
