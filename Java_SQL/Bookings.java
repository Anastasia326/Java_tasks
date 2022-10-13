import java.sql.Timestamp;
import java.math.BigDecimal;

public class Bookings {
    private final String bookRef;
    private final Timestamp bookDate;
    private final BigDecimal totalAmount;

    public Bookings(String bookRef, Timestamp bookDate, BigDecimal totalAmount) {
        this.bookRef = bookRef;
        this.bookDate = bookDate;
        this.totalAmount = totalAmount;
    }

    public String getBookRef() {
        return bookRef;
    }

    public Timestamp getBookDate() {
        return bookDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
}