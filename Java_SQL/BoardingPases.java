public class BoardingPases {
    private final String ticketNo;
    private final Integer flightId;
    private final Integer boardingNo;
    private final String seatNo;

    public Integer getFlightId() {
        return flightId;
    }

    public Integer getBoardingNo() {
        return boardingNo;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public String getTicketNo() {
        return ticketNo;
    }

    public BoardingPases(String ticketNo, Integer flightId, Integer boardingNo, String seatNo) {
        this.ticketNo = ticketNo;
        this.flightId = flightId;
        this.boardingNo = boardingNo;
        this.seatNo = seatNo;
    }
}