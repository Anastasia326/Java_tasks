import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DaoBoardingPases {
    private final SQLWorker source;

    private BoardingPases CreateData(ResultSet resultSet) throws SQLException {
        return new BoardingPases(resultSet.getString("ticket_no"),
                resultSet.getInt("flight_id"),
                resultSet.getInt("boarding_no"),
                resultSet.getString("seat_no"));
    }

    public void SaveData(Collection<BoardingPases> boardingPasses) throws SQLException {
        source.preparedStatement("insert into boarding_passes(ticket_no, flight_id, boarding_no, seat_no) values (?, ?, ?, ?)", insertBoardingPases -> {
            for (BoardingPases pas : boardingPasses) {
                insertBoardingPases.setString(1, pas.getTicketNo());
                insertBoardingPases.setInt(2, pas.getFlightId());
                insertBoardingPases.setInt(3, pas.getBoardingNo());
                insertBoardingPases.setString(4, pas.getSeatNo());
                insertBoardingPases.execute();
            }
        });
    }

    public Set<BoardingPases> GetData() throws SQLException {
        return source.statement(statement -> {
            Set<BoardingPases> answer = new HashSet<>();
            ResultSet resultSet = statement.executeQuery("select * from boarding_passes");
            while (resultSet.next()) {
                answer.add(CreateData(resultSet));
            }
            return answer;
        });
    }

    public DaoBoardingPases(SQLWorker new_source) {
        source = new_source;
    }
}
