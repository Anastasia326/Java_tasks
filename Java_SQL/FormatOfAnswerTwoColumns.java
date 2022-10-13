import java.util.ArrayList;
import java.util.List;

public class FormatOfAnswerTwoColumns<T, V> extends Format{
    private final T firstValue;
    private final V secondValue;

    public FormatOfAnswerTwoColumns(T firstValue, V secondValue) {
        this.firstValue = firstValue;
        this.secondValue = secondValue;
    }

    public T getFirstValue() {
        return firstValue;
    }

    public V getSecondValue() {
        return secondValue;
    }

    @Override
    public ArrayList<Object> getData() {
        return new ArrayList<>(List.of(firstValue, secondValue));
    }
}
