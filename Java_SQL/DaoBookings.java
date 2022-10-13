import java.security.Timestamp;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class DaoBookings extends Dao<Bookings>{
    private final SQLWorker source;

    public DaoBookings(SQLWorker new_source) {
        source = new_source;
    }

    @Override
    public Bookings CreateData(ResultSet resultSet) throws SQLException {
        return new Bookings(resultSet.getString("book_ref"),
                resultSet.getTimestamp("book_date"),
                resultSet.getBigDecimal("total_amount"));
    }

    @Override
    public void SaveData(Collection<Bookings> bookings) throws SQLException {
        source.preparedStatement("insert into bookings(book_ref, book_date, total_amount) values (?, ?, ?)", insertAircraft -> {
            for (Bookings oneBooking : bookings) {
                insertAircraft.setString(1, oneBooking.getBookRef());
                insertAircraft.setTimestamp(2, oneBooking.getBookDate());
                insertAircraft.setBigDecimal(3, oneBooking.getTotalAmount());
                insertAircraft.execute();
            }
        });
    }

    String forGetData = "select * from bookings";
}
