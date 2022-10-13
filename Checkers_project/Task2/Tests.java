import org.junit.*;

import java.util.ArrayList;
import java.util.List;


public class Tests {
    //проверяем правильно показывает позиции
    @Test
    public void workOfShow() {
        String[] my_data = {"b2_ww c1_w d2_w d4_wwwb f2_w",
                "c5_bw c7_bbb f6_bww f8_b g5_bb h6_bbw h8_b"};
        ArrayList<String> data = new ArrayList<>(List.of(my_data));
        ChessBoard my_board = new ChessBoard();
        PairChess pos = new PairChess(data.get(0), data.get(1));
        my_board.Placement(pos);
        ArrayList<ArrayList<String>> program_answer = my_board.showPosition();
        String[] white = {"b2_ww", "c1_w", "d2_w", "d4_wwwb", "f2_w"};
        String[] black = {"c5_bw", "c7_bbb", "f6_bww", "f8_b", "g5_bb", "h6_bbw", "h8_b"};

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
        String my_data = "b2_ww";
        Parser my_parser = new Parser();
        ArrayList<ArrayList<Integer>> program_answer = my_parser.doParse(my_data);
        Integer[] answer = {1, 6, -3, -3};
        ArrayList<ArrayList<Integer>> right_answer = new ArrayList<>();
        right_answer.add(new ArrayList<>(List.of(answer)));
        for (int i = 0; i < right_answer.size(); ++i) {
            Assert.assertEquals(right_answer.get(i), program_answer.get(i));
        }
    }

    //и обратный
    @Test
    public void workBackParser() {
        Parser my_parser = new Parser();
        Cell first_cell = new Cell(CellState.white_chess, CellColor.black, CellLady.normal_chess,
                1, 2, new ArrayList<>(List.of(new SmallChess(CellState.black_chess, CellLady.normal_chess))));
        Cell second_cell = new Cell(CellState.black_chess, CellColor.black, CellLady.is_lady,
                0, 7, new ArrayList<>(List.of(new SmallChess(CellState.white_chess, CellLady.is_lady))));
        String first_program_answer = my_parser.backParser(first_cell);
        String second_program_answer = my_parser.backParser(second_cell);
        String first_right_answer = "b6_b";
        String second_right_answer = "a1_W";
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
        ArrayList<Cell> to = new ArrayList<>();
        ArrayList<Integer> should_do = new ArrayList<>();
        should_do.add(-1);
        to.add(new Cell(new ArrayList<>(List.of(2, 3, -4))));
        KillChess chess = new KillChess();
        chess.update(board.board, 4, 3);
        ChessBoard.motion motion = new ChessBoard.Motion(new Cell(new ArrayList<>(List.of(3, 4, -3))), to,
                should_do, CellState.white_chess, CellLady.normal_chess);
        Assert.assertThrows(ExceptionInvalidMove.class, () -> {
            board.move(motion);
        });
    }

}

