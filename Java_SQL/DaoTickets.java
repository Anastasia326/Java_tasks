import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class DaoTickets extends Dao<Tickets>{
    private final SQLWorker source;

    public DaoTickets(SQLWorker new_source) {
        source = new_source;
    }

    @Override
    public Tickets CreateData(ResultSet resultSet) throws SQLException {
        return new Tickets(resultSet.getString("ticket_no"),
                resultSet.getString("book_ref"),
                resultSet.getString("passenger_id"),
                resultSet.getString("passenger_name"),
                resultSet.getString("contact_data"));
    }

    @Override
    public void SaveData(Collection<Tickets> bookings) throws SQLException {
        source.preparedStatement("insert into tickets(ticket_no, book_ref, passenger_id, passenger_name, contact_data) values (?, ?, ?, ?, ?)", insertAircraft -> {
            for (Tickets oneBooking : bookings) {
                insertAircraft.setString(1, oneBooking.getTicketNo());
                insertAircraft.setString(2, oneBooking.getTicketBookRef());
                insertAircraft.setString(3, oneBooking.getTicketPassengerId());
                insertAircraft.setString(4, oneBooking.getTicketPassengerName());
                insertAircraft.setString(5, oneBooking.getTicketContactDate());
                insertAircraft.execute();
            }
        });
    }

    String forGetData = "select * from ticket_flights";
}
