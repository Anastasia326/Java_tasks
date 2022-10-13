public class Tickets {
    private final String ticketNo;
    private final String bookRef;
    private final String passengerId;
    private final String passenger_name;
    private final String contact_data;

    public Tickets(String ticketNo, String bookRef, String passengerId, String passenger_name, String contact_data) {
        this.ticketNo = ticketNo;
        this.bookRef = bookRef;
        this.passengerId = passengerId;
        this.passenger_name = passenger_name;
        this.contact_data = contact_data;
    }

    public String getTicketNo() {
        return ticketNo;
    }

    public String getTicketBookRef() {
        return bookRef;
    }

    public String getTicketPassengerId() {
        return passengerId;
    }

    public String getTicketPassengerName() {
        return passenger_name;
    }

    public String getTicketContactDate() {
        return contact_data;
    }
}