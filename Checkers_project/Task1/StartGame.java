import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//класс, делящий входные данные на белые и черные шашки для удобства
class PairChess {
    String black;
    String white;

    public PairChess(String first, String second) {
        this.white = first;
        this.black = second;
    }

}

//класс, отвечающий за запуск игры, хранит в себе все данные
public class StartGame {
    private ChessBoard my_board;
    private final PairChess position;
    private final List<String> should_go;

    public StartGame(List<String> data) {
        //обрабатываем входные данные
        position = new PairChess(data.get(0), data.get(1));
        List<String> should_go = new ArrayList<>();
        this.should_go = should_go;
        int size = data.size();
        for (int i = 2; i < size; ++i) {
            should_go.add(data.get(i));
        }
    }

    //запуск игры, прокидывает исключение, если во время проведения игры была ошибка
    public void run() throws ExceptionInvalidMove {
        KillChess worker = new KillChess();
        //начинаем проходить игру, создаем пустую доску
        my_board = new ChessBoard();
        //размещаем исходные шашки
        my_board.placement(position);
        //заполняем информацией, какие шашки могут есть других
        worker.prepareBoard(my_board.board);
        //выполняет все ходы
        my_board.changes(should_go);
    }

    //показывает позицию шашек в данный момент
    public List<List<String>> Position() {
        return (my_board.showPosition());
    }
}
