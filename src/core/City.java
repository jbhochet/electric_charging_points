package core;

/**
 * Represents a city.
 *
 * <p>The city has a name stored with a String object.
 *
 * <p>The city does or does not have a charging point.
 * This value is stored in a boolean.
 *
 * @author Jean-Baptiste Hochet
 * @author Pablo Rican
 */
public class City {

    /**
     * The name of the city.
     */
    private String name;

    /**
     * Whether it has a charging point or not.
     */
    private boolean chargingPoint;

    /**
     * The constructor of the class City.
     *
     * <p>Creates a new City object with the name given in parameter.
     *
     * @param name
     *        The name of the city
     */
    public City(String name) {
        this.name = name;
    }

    /**
     * The getter of the name attribute.
     *
     * @return The name of the city
     */
    public String getName() {
        return name;
    }

    /**
     * The getter of the chargingPoint attribute.
     *
     * @return True if the city does have a charging point.<br>
     *         Else if the city does not have a charging point.
     */
    public boolean hasChargingPoint() {
        return chargingPoint;
    }

    /**
     * The setter of the chargingPoint attribute.
     *
     * <p>Sets the chargingPoint attribute to true.
     */
    void addChargingPoint() {
        this.chargingPoint = true;
    }

    /**
     * The setter of the chargingPoint attribute.
     *
     * <p>Sets the chargingPoint attribute to false.
     */
    void removeChargingPoint() {
        this.chargingPoint = false;
    }
}
