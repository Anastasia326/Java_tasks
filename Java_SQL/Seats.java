public class Seats {
    private final String aircraft_code;
    private final String seat_no;
    private final String fare_conditions;

    public Seats(String aircraft_code, String seat_no, String fare_conditions) {
        this.aircraft_code = aircraft_code;
        this.seat_no = seat_no;
        this.fare_conditions = fare_conditions;
    }

    public String getAircraftCode() {
        return aircraft_code;
    }

    public String getSeatNo() {
        return seat_no;
    }

    public String getFareConditions() {
        return fare_conditions;
    }
}