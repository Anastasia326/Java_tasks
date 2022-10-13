public class Aircrafts {
    private final String aircraftCode;
    private final String model;
    private final int range;

    public Aircrafts(String aircraftCode, String model, int range) {
        this.aircraftCode = aircraftCode;
        this.model = model;
        this.range = range;
    }

    public String getAircraftCode() {
        return aircraftCode;
    }

    public String getModel() {
        return model;
    }

    public int getRange() {
        return range;
    }
}