import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DaoAirports extends Dao<Airports>{
    private final SQLWorker source;

    @Override
    public Airports CreateData(ResultSet resultSet) throws SQLException {
        return new Airports(resultSet.getString("airport_code"),
                resultSet.getString("airport_name"),
                resultSet.getString("city"),
                resultSet.getString("coordinates"),
                resultSet.getString("timezone"));
    }

    @Override
    public void SaveData(Collection<Airports> airports) throws SQLException {
        source.preparedStatement("insert into airports(airport_code, airport_name, city, coordinates, timezone) values (?, ?, ?, ?, ?)", insertAircraft -> {
            for (Airports airport : airports) {
                insertAircraft.setString(1, airport.getAirportCode());
                insertAircraft.setString(2, airport.getAirportName());
                insertAircraft.setString(3, airport.getAirportCity());
                insertAircraft.setString(4, airport.getAirportCoordinates());
                insertAircraft.setString(5, airport.getAirportTimezone());
                insertAircraft.execute();
            }
        });
    }

    public Set<Airports> GetData(String sqlRequest) throws SQLException {
        return source.statement(statement -> {
            Set<Airports> answer = new HashSet<>();
            ResultSet resultSet = statement.executeQuery(sqlRequest);
            while (resultSet.next()) {
                answer.add(CreateData(resultSet));
            }
            return answer;
        });
    }

    String forGetData = "select * from airports";

    public DaoAirports(SQLWorker new_source) {
        source = new_source;
    }
}

