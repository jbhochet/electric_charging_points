package core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exceptions.InvalidConfigFileException;

public class ConfigParser {
    public static final Pattern PATTERN_CITY = Pattern.compile("ville\\((\\w+)\\).$");
    public static final Pattern PATTERN_ROAD = Pattern.compile("^route\\((\\w+),(\\w+)\\).$");
    public static final Pattern PATTERN_CHARGING_POINT = Pattern.compile("^recharge\\((\\w+)\\).$");

    /**
     * Check if this line match the regex and return the name of the city.
     * 
     * @param line
     * @return The name of the city.
     * @throws InvalidConfigFileException
     */
    private static String parseCity(String line) throws InvalidConfigFileException {
        Matcher matcher = PATTERN_CITY.matcher(line);
        if (!matcher.matches())
            throw new InvalidConfigFileException("This line don't matche pattern!");
        return matcher.group(1);
    }

    /**
     * Check if this line match the regex and return a road.
     * 
     * @param line
     * @return An array of two elements, the source and the destination of this
     *         road.
     * @throws InvalidConfigFileException
     */
    private static String[] parseRoad(String line) throws InvalidConfigFileException {
        Matcher matcher = PATTERN_ROAD.matcher(line);
        if (!matcher.matches())
            throw new InvalidConfigFileException("This line don't matche pattern!");
        return new String[] { matcher.group(1), matcher.group(2) };
    }

    /**
     * Check if this line match the regex and return the city where we will add a
     * charging point.
     * 
     * @param line
     * @return The name of the city.
     * @throws InvalidConfigFileException
     */
    private static String parseChargingPoint(String line) throws InvalidConfigFileException {
        Matcher matcher = PATTERN_CHARGING_POINT.matcher(line);
        if (!matcher.matches())
            throw new InvalidConfigFileException("This line don't matche pattern!");
        return matcher.group(1);
    }

    /**
     * Load the urban community stored in this file.
     * 
     * @param file
     * @return A new urban community instance.
     * @throws IOException
     * @throws InvalidConfigFileException
     */
    public static UrbanCommunity loadConfigFile(File file) throws IOException, InvalidConfigFileException {
        List<City> cities = new ArrayList<>();
        FileReader fileReader = new FileReader(file);
        UrbanCommunity urbanCommunity = null;
        int i = 0;

        try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;

            while (((line = bufferedReader.readLine()) != null) && i < 3) {
                if (i == 0) {
                    // Load cities
                    try {
                        cities.add(new City(parseCity(line)));
                    } catch (InvalidConfigFileException err) {
                        urbanCommunity = new UrbanCommunity(cities.toArray(new City[cities.size()]));
                        i++;
                    }
                }

                if (i == 1) {
                    // Load roads
                    try {
                        String[] road = parseRoad(line);
                        urbanCommunity.addRoad(road[0], road[1]);
                    } catch (IllegalArgumentException err) {
                        throw new InvalidConfigFileException("Try to add road between unknown cities!");
                    } catch (InvalidConfigFileException err) {
                        i++;
                    }
                }

                if (i == 2) {
                    // Load charging points
                    try {
                        String city = parseChargingPoint(line);
                        urbanCommunity.addChargingPoint(city);
                    } catch (IllegalArgumentException err) {
                        throw new InvalidConfigFileException("Try to add a charging point in an unknown cities!");
                    } catch (InvalidConfigFileException err) {
                        i++;
                    }
                }
            }
        }

        // If the file only contains cities, urban community could not be created
        if (urbanCommunity == null)
            urbanCommunity = new UrbanCommunity(cities.toArray(new City[cities.size()]));

        return urbanCommunity;
    }

    /**
     * Save the urban community in the file.
     * 
     * @param file
     * @param urbanCommunity
     * @throws IOException
     */
    public static void saveConfigFile(File file, UrbanCommunity urbanCommunity) throws IOException {
        // Il faut avoir la liste des villes
        // Il faut la liste des voisins d'une ville
        // Il faut savoir si une ville à un point de recharge
        Set<Entry<String, String>> roads = new HashSet<>();
        Set<String> chargingPoints = new HashSet<>();
        StringBuffer sb = new StringBuffer();

        for (City city : urbanCommunity.getCities()) {
            sb.append(String.format("ville(%s).\n", city.getName()));
            for (City neighbor : urbanCommunity.getNeighbors(city.getName())) {
                Entry<String, String> road;
                if (city.getName().compareTo(neighbor.getName()) < 0) {
                    road = Map.entry(city.getName(), neighbor.getName());
                } else {
                    road = Map.entry(neighbor.getName(), city.getName());
                }
                roads.add(road);
            }
            if (city.hasChargingPoint())
                chargingPoints.add(city.getName());
        }

        for (Entry<String, String> road : roads) {
            sb.append(String.format("route(%s,%s).\n", road.getKey(), road.getValue()));
        }

        for (String chargingPoint : chargingPoints)
            sb.append(String.format("recharge(%s).\n", chargingPoint));

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write(sb.toString());
        }
    }
}
