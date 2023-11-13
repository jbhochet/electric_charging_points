package core;

import exceptions.AccessibilityException;
import graph.Graph;

public class UrbanCommunity {
    private Graph graph;
    private City[] cities;

    public UrbanCommunity(City[] cities) {
        this.cities = cities;
        graph = new Graph(cities.length);
    }

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

    public void addChargingPoint(String city) {
        int indexCity = getCityIndex(city);

        if (indexCity == -1) {
            throw new IllegalArgumentException("The parameter is not in the list 'cities'");
        }

        cities[indexCity].addChargingPoint();
    }

    public void removeChargingPoint(String city) throws AccessibilityException {
        int indexCity = getCityIndex(city);

        if (indexCity == -1) {
            throw new IllegalArgumentException("The parameter is not in the list 'cities'");
        }

        int[] neighbors = graph.neighbors(indexCity);
        boolean hasNeighborsPossessingChargingPoint = false;

        for (int indexNeighbor : neighbors) {
            if (cities[indexNeighbor].hasCharginPoint()) {
                hasNeighborsPossessingChargingPoint = true;
                break;
            }
        }

        if (!hasNeighborsPossessingChargingPoint) {
            throw new AccessibilityException("You cannot remove the charging point of this city because it does not" +
                "have a neighbor possessing a charging point");
        }

        cities[indexCity].removeChargingPoint();
    }

    public void naiveAlgorithm() {
        for (City city : cities) {
            city.addChargingPoint();
        }
    }

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

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Display of the cities from this urban community :\n");
        for (City city : cities) {
            str.append(city.getName()).append(" : ");
            if (city.hasCharginPoint()) {
                str.append("does have a charging point\n");
            } else
                str.append("does not have a charging point\n");
        }

        return String.valueOf(str);
    }
}
