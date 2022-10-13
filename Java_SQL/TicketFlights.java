import java.math.BigDecimal;
public class TicketFlights {
    private final String ticketNo;
    private final Integer flightId;
    private final String fareConditions;
    private final BigDecimal amount;

    public TicketFlights(String ticketNo, Integer flightId, String fareConditions, BigDecimal amount) {
        this.ticketNo = ticketNo;
        this.flightId = flightId;
        this.fareConditions = fareConditions;
        this.amount = amount;
    }

    public String getTicketNo() {
        return ticketNo;
    }

    public Integer getFlightId() {
        return flightId;
    }

    public String getFareConditions() {
        return fareConditions;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}