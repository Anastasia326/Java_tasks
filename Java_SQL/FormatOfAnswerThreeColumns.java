import java.util.ArrayList;
import java.util.List;

public class FormatOfAnswerThreeColumns<T, V, S> extends Format {
    private final T firstValue;
    private final V secondValue;
    private final S thirdValue;


    public FormatOfAnswerThreeColumns(T firstValue, V secondValue, S thirdValue) {
        this.firstValue = firstValue;
        this.secondValue = secondValue;
        this.thirdValue = thirdValue;
    }

    public ArrayList<String> printHeaders(String firstColumn, String secondColumn, String thirColumn) {
        String[] names ={firstColumn, secondColumn, thirColumn};
        return new ArrayList<>(List.of(names));
    }

    @Override
    public ArrayList<Object> getData() {
        return new ArrayList<>(List.of(firstValue, secondValue, thirdValue));
    }
}
