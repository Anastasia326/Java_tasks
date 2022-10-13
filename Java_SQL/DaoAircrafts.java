import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DaoAircrafts extends Dao<Aircrafts> {
    private final SQLWorker source;

    public DaoAircrafts(SQLWorker new_source) {
        source = new_source;
    }

    @Override
    public Aircrafts CreateData(ResultSet resultSet) throws SQLException {
        return new Aircrafts(resultSet.getString("aircraft_code"),
                resultSet.getString("model"),
                resultSet.getInt("range"));
    }

    @Override
    public void SaveData(Collection<Aircrafts> aircrafts) throws SQLException {
        source.preparedStatement("insert into aircrafts(aircraft_code, model, range) values (?, ?, ?)", insertAircraft -> {
            for (Aircrafts aircraft : aircrafts) {
                insertAircraft.setString(1, aircraft.getAircraftCode());
                insertAircraft.setString(2, aircraft.getModel());
                insertAircraft.setInt(3, aircraft.getRange());
                insertAircraft.execute();
            }
        });
    }

    String forGetData = "select * from aircrafts";
}

