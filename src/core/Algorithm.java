package core;

import exceptions.AccessibilityException;

import java.util.Random;

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

    public static void lessNaiveAlgorithm(UrbanCommunity urbanCommunity, int numberIteration) {
        int i = 0;
        int currentScore = urbanCommunity.urbanCommunityScore();

        while (i < numberIteration) {
            int randomIndex = new Random().nextInt(urbanCommunity.getCities().length);
            City randomCity = urbanCommunity.getCities()[randomIndex];
            UrbanCommunity temporaryUrbanCommunity;

            if (randomCity.hasChargingPoint()) {
                try {
                    urbanCommunity.removeChargingPoint(randomCity.getName());
                } catch (AccessibilityException ignored) {
                }
            } else {
                urbanCommunity.addChargingPoint(randomCity.getName());
            }

            if (urbanCommunity.urbanCommunityScore() < currentScore) {
                System.out.println(currentScore);
                i = 0;
                currentScore = urbanCommunity.urbanCommunityScore();
            }
            i++;
        }
    }
}
