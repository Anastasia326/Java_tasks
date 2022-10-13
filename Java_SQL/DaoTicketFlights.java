import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class DaoTicketFlights extends Dao<TicketFlights>{
    private final SQLWorker source;

    public DaoTicketFlights(SQLWorker new_source) {
        source = new_source;
    }

    @Override
    public TicketFlights CreateData(ResultSet resultSet) throws SQLException {
        return new TicketFlights(resultSet.getString("ticket_no"),
                resultSet.getInt("flight_id"),
                resultSet.getString("fare_conditions"),
                resultSet.getBigDecimal("amount"));
    }

    @Override
    public void SaveData(Collection<TicketFlights> bookings) throws SQLException {
        source.preparedStatement("insert into ticket_flights(ticket_no, flight_id, fare_conditions, amount) values (?, ?, ?, ?)", insertAircraft -> {
            for (TicketFlights oneBooking : bookings) {
                insertAircraft.setString(1, oneBooking.getTicketNo());
                insertAircraft.setInt(2, oneBooking.getFlightId());
                insertAircraft.setString(3, oneBooking.getFareConditions());
                insertAircraft.setBigDecimal(4, oneBooking.getAmount());
                insertAircraft.execute();
            }
        });
    }

    String forGetData = "select * from ticket_flights";
}
