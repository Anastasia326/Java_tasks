import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;

public class DaoFlights extends Dao<Flights> {
    private final SQLWorker source;

    public DaoFlights(SQLWorker new_source) {
        source = new_source;
    }

    @Override
    public Flights CreateData(ResultSet resultSet) throws SQLException {
        return new Flights(resultSet.getLong("flight_id"),
                resultSet.getString("flight_no"),
                resultSet.getTimestamp("scheduled_departure"),
                resultSet.getTimestamp("scheduled_arrival"),
                resultSet.getString("departure_airport"),
                resultSet.getString("arrival_airport"),
                resultSet.getString("status"),
                resultSet.getString("aircraft_code"),
                resultSet.getTimestamp("actual_departure"),
                resultSet.getTimestamp("actual_arrival"));
    }

    @Override
    public void SaveData(Collection<Flights> bookings) throws SQLException {
        source.preparedStatement("insert into flights(flight_id, flight_no, scheduled_departure, " +
                "scheduled_arrival, departure_airport, arrival_airport, status, aircraft_code, actual_departure, actual_arrival)" +
                " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", insertAircraft -> {
            for (Flights oneBooking : bookings) {
                insertAircraft.setLong(1, oneBooking.getFlightId());
                insertAircraft.setString(2, oneBooking.getFlightNo());
                insertAircraft.setTimestamp(3, oneBooking.getScheduledDeparture());
                insertAircraft.setTimestamp(4, oneBooking.getScheduledArrival());
                insertAircraft.setString(5, oneBooking.getDepartureAirport());
                insertAircraft.setString(6, oneBooking.getArrivalAirport());
                insertAircraft.setString(7, oneBooking.getStatus());
                insertAircraft.setString(8, oneBooking.getAircraftCode());
                insertAircraft.setTimestamp(9, oneBooking.getActualDeparture());
                insertAircraft.setTimestamp(10, oneBooking.getActualArrival());
                insertAircraft.execute();
            }
        });
    }

    String forGetData = "select * from flights";
}
