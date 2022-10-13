public class Airports {
    private final String airportCode;
    private final String airportName;
    private final String city;
    private final String coordinates;
    private final String timezone;

    public Airports(String airportCode, String airportName, String city, String coordinates, String timezone) {
        this.airportCode = airportCode;
        this.airportName = airportName;
        this.city = city;
        this.coordinates = coordinates;
        this.timezone = timezone;
    }

    public String getAirportCode() {
        return airportCode;
    }

    public String getAirportName() {
        return airportName;
    }

    public String getAirportCity() {
        return city;
    }

    public String getAirportCoordinates() {
        return airportName;
    }

    public String getAirportTimezone() {
        return timezone;
    }
}