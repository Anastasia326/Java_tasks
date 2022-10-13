import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class Dao<T> {
    public abstract T CreateData(ResultSet resultSet) throws SQLException;

    public abstract void SaveData(Collection<T> dao) throws SQLException;

    public Set<T> GetData(String sqlRequest, SQLWorker source) throws SQLException {
        return source.statement(statement -> {
            Set<T> answer = new HashSet<>();
            ResultSet resultSet = statement.executeQuery(sqlRequest);
            while (resultSet.next()) {
                answer.add(CreateData(resultSet));
            }
            return answer;
        });
    }
}