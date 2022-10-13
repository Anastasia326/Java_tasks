import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class DaoSeats extends Dao<Seats> {
    private final SQLWorker source;

    public DaoSeats(SQLWorker new_source) {
        source = new_source;
    }

    @Override
    public Seats CreateData(ResultSet resultSet) throws SQLException {
        return new Seats(resultSet.getString("aircraft_code"),
                resultSet.getString("seat_no"),
                resultSet.getString("fare_conditions"));
    }

    @Override
    public void SaveData(Collection<Seats> aircrafts) throws SQLException {
        source.preparedStatement("insert into seats(aircraft_code, seat_no, fare_conditions) values (?, ?, ?)", insertAircraft -> {
            for (Seats aircraft : aircrafts) {
                insertAircraft.setString(1, aircraft.getAircraftCode());
                insertAircraft.setString(2, aircraft.getSeatNo());
                insertAircraft.setString(3, aircraft.getFareConditions());
                insertAircraft.execute();
            }
        });
    }

    String forGetData = "select * from seats";
}
