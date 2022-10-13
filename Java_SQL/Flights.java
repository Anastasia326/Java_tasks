import java.sql.Timestamp;
import java.math.BigDecimal;

public class Flights {
    private final long flightId;
    private final String flightNo;
    private final Timestamp scheduledDeparture;
    private final Timestamp scheduledArrival;
    private final String departureAirport;
    private final String arrivalAirport;
    private final String status;
    private final String aircraftCode;
    private final Timestamp actualDeparture;
    private final Timestamp actualArrival;

    public long getFlightId() {
        return flightId;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public Timestamp getScheduledDeparture() {
        return scheduledDeparture;
    }

    public Timestamp getScheduledArrival() {
        return scheduledArrival;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public String getStatus() {
        return status;
    }

    public String getAircraftCode() {
        return aircraftCode;
    }

    public Timestamp getActualDeparture() {
        return actualDeparture;
    }

    public Timestamp getActualArrival() {
        return actualArrival;
    }

    public Flights(long flightId, String flightNo, Timestamp scheduledDeparture, Timestamp scheduledArrival,
                   String departureAirport, String arrivalAirport, String status, String aircraftCode,
                   Timestamp actualDeparture, Timestamp actualArrival) {
        this.flightId = flightId;
        this.flightNo = flightNo;
        this.scheduledDeparture = scheduledDeparture;
        this.scheduledArrival = scheduledArrival;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.status = status;
        this.aircraftCode = aircraftCode;
        this.actualDeparture = actualDeparture;
        this.actualArrival = actualArrival;
    }
}