package core;

public class City {
    private String name;
    private boolean chargingPoint;

    public City(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean hasChargingPoint() {
        return chargingPoint;
    }

    void addChargingPoint() {
        this.chargingPoint = true;
    }

    void removeChargingPoint() {
        this.chargingPoint = false;
    }
}
