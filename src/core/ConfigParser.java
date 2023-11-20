package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exceptions.InvalidConfigFileException;

public class ConfigParser {
    public static final Pattern PATTERN_CITY = Pattern.compile("ville\\((\\w+)\\).$");
    public static final Pattern PATTERN_ROAD = Pattern.compile("^route\\((\\w+),(\\w+)\\).$");
    public static final Pattern PATTERN_CHARGING_POINT = Pattern.compile("^recharge\\((\\w+)\\).$");

    private static String parseCity(String line) throws InvalidConfigFileException {
        Matcher matcher = PATTERN_CITY.matcher(line);
        if (!matcher.matches())
            throw new InvalidConfigFileException("This line don't matche pattern!");
        return matcher.group(1);
    }

    private static String[] parseRoad(String line) throws InvalidConfigFileException {
        Matcher matcher = PATTERN_ROAD.matcher(line);
        if (!matcher.matches())
            throw new InvalidConfigFileException("This line don't matche pattern!");
        return new String[] { matcher.group(1), matcher.group(2) };
    }

    private static String parseChargingPoint(String line) throws InvalidConfigFileException {
        Matcher matcher = PATTERN_CHARGING_POINT.matcher(line);
        if (!matcher.matches())
            throw new InvalidConfigFileException("This line don't matche pattern!");
        return matcher.group(1);
    }

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

        return urbanCommunity;
    }

    public void saveConfigFile(File file) {
    }
}
