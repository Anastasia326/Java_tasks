import org.junit.*;

import java.util.ArrayList;
import java.util.List;


public class Tests {
    //проверяем правильно показывает позиции
    @Test
    public void workOfShow() {
        String[] my_data = {"a1 a3 b2 c1 c3 d2 e1 e3 f2 g1 g3 h2",
                "a7 b6 b8 c7 d6 d8 e7 f6 f8 g7 h6 h8"};
        ArrayList<String> data = new ArrayList<>(List.of(my_data));
        ChessBoard my_board = new ChessBoard();
        PairChess pos = new PairChess(data.get(0), data.get(1));
        my_board.placement(pos);
        ArrayList<ArrayList<String>> program_answer = my_board.ShowPosition();
        String[] white = {"a1", "a3", "b2", "c1", "c3", "d2", "e1", "e3", "f2", "g1", "g3", "h2"};
        String[] black = {"a7", "b6", "b8", "c7", "d6", "d8", "e7", "f6", "f8", "g7", "h6", "h8"};

        ArrayList<ArrayList<String>> right_answer = new ArrayList<>();
        right_answer.add(new ArrayList<>(List.of(white)));
        right_answer.add(new ArrayList<>(List.of(black)));
        for (int i = 0; i < right_answer.size(); ++i) {
            Assert.assertEquals(right_answer.get(i), program_answer.get(i));
        }
    }

    //проверяем, работает ли парсер данных

    @Test
    public void workParser() {
        String my_data = "a1, a3 b2 c1 c3 d2 e1 e3 f2 g1 g3 h2";
        Parser my_parser = new Parser();
        ArrayList<Integer> program_answer = my_parser.DoParse(my_data);
        Integer[] answer = {0, 7, 0, 5, 1, 6, 2, 7, 2, 5, 3, 6, 4, 7, 4, 5, 5, 6, 6, 7, 6, 5, 7, 6};
        ArrayList<Integer> right_answer = new ArrayList<>(List.of(answer));
        for (int i = 0; i < right_answer.size(); ++i) {
            Assert.assertEquals(right_answer.get(i), program_answer.get(i));
        }
    }

    //и обратный
    @Test
    public void workBackParser() {
        Parser my_parser = new Parser();
        Cell first_cell = new Cell(CellState.white_chess, CellColor.black, CellLady.normal_chess, 1, 2);
        Cell second_cell = new Cell(CellState.black_chess, CellColor.black, CellLady.is_lady, 0, 7);
        String first_program_answer = my_parser.backParser(1, 2, first_cell);
        String second_program_answer = my_parser.backParser(0, 7, second_cell);
        String first_right_answer = "c7";
        String second_right_answer = "H8";
        Assert.assertEquals(first_program_answer, first_right_answer);
        Assert.assertEquals(second_program_answer, second_right_answer);
    }

    //проверка Update
    @Test
    public void workUpdate() {
        ChessBoard board = new ChessBoard();
        PairChess position = new PairChess("d4", "c5 e5");
        board.placement(position);
        KillChess chess = new KillChess();
        chess.update(board.board, 4, 3);
        Assert.assertTrue(board.board.get(4).get(3).can_kill.contains(board.board.get(3).get(2)));
        Assert.assertTrue(board.board.get(4).get(3).can_kill.contains(board.board.get(3).get(4)));
    }

    //проверка бросания исключения
    @Test
    public void workMove() {
        ChessBoard board = new ChessBoard();
        PairChess position = new PairChess("d4", "a7 e5");
        board.placement(position);
        ArrayList<Pair<Integer, Integer>> to = new ArrayList<>();
        ArrayList<Integer> should_do = new ArrayList<>();
        should_do.add(-1);
        to.add(new Pair<>(2, 3));
        KillChess chess = new KillChess();
        chess.update(board.board, 4, 3);
        ChessBoard.Motion motion = new ChessBoard.Motion(new Pair<>(3, 4), to,
                should_do, CellState.white_chess, CellLady.normal_chess);
        Assert.assertThrows(ExceptionInvalidMove.class, () -> {
            board.move(motion);
        });
    }
}

