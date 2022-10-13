import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

//цвета клеток
enum CellColor {
    white,
    black
}

//состояние поля доски (какого цвета на ней пешка и есть ли она вообще)
enum CellState {
    white_chess,
    black_chess,
    no_chess
}

//на клетке поля стоит ли пешка и если да, то дамка она или нет
enum CellLady {
    is_lady,
    normal_chess,
    no_chess
}

//класс клетка, состояние клетки: цвет и есть ли на ней пешка, описание пешки(цвет и дамка ли)
class Cell {
    CellState state;
    CellColor color;
    CellLady lady;
    List<Cell> can_kill;
    int word_coordinate;
    int number_coordinate;

    Cell(CellState st, CellColor col, CellLady l, int i, int j) {
        state = st;
        color = col;
        lady = l;
        can_kill = new ArrayList<>();
        word_coordinate = i;
        number_coordinate = j;
    }
}

//класс пара (как std::pair)
class Pair<FirstT, SecondT> {
    public FirstT getFirst_obj() {
        return first_obj;
    }

    public void setFirst_obj(FirstT first_obj) {
        this.first_obj = first_obj;
    }

    public SecondT getSecond_obj() {
        return second_obj;
    }

    public void setSecond_obj(SecondT second_obj) {
        this.second_obj = second_obj;
    }

    private FirstT first_obj;
    private SecondT second_obj;

    Pair(FirstT first, SecondT second) {
        first_obj = first;
        second_obj = second;
    }
}

class KillChess {
    //размер доски
    int size_of_board = 8;

    //после начальной расстановки смотрим, какие шашки могут съесть других
    public void prepareBoard(List<List<Cell>> board) {
        for (int i = 0; i < size_of_board; ++i) {
            for (int j = 0; j < size_of_board; ++j) {
                if (board.get(i).get(j).state != CellState.no_chess) {
                    board.get(i).get(j).can_kill.clear();
                    update(board, i, j);
                }
            }
        }
    }

    //обновляем информацию для одной клетки
    public void update(List<List<Cell>> board, int i, int j) {
        if ((i >= 0) && (i < size_of_board) && (j >= 0) && (j < size_of_board)) {
            Cell cell = board.get(i).get(j);
            if (cell.state != CellState.no_chess) {
                if ((i + 2 < size_of_board) && (j + 2 < size_of_board) &&
                        (board.get(i + 1).get(j + 1).state != CellState.no_chess) &&
                        (board.get(i + 1).get(j + 1).state != cell.state) &&
                        (board.get(i + 2).get(j + 2).state == CellState.no_chess)) {
                    if (!cell.can_kill.contains(board.get(i + 1).get(j + 1))) {
                        cell.can_kill.add(board.get(i + 1).get(j + 1));
                    }
                }
                if ((i + 2 < size_of_board) && (j - 2 >= 0) &&
                        (board.get(i + 1).get(j - 1).state != CellState.no_chess) &&
                        (board.get(i + 1).get(j - 1).state != cell.state) &&
                        (board.get(i + 2).get(j - 2).state == CellState.no_chess)) {
                    if (!cell.can_kill.contains(board.get(i + 1).get(j - 1))) {
                        cell.can_kill.add(board.get(i + 1).get(j - 1));
                    }
                }
                if ((i - 2 >= 0) && (j + 2 < size_of_board) &&
                        (board.get(i - 1).get(j + 1).state != CellState.no_chess) &&
                        (board.get(i - 1).get(j + 1).state != cell.state) &&
                        (board.get(i - 2).get(j + 2).state == CellState.no_chess)) {
                    if (!cell.can_kill.contains(board.get(i - 1).get(j + 1))) {
                        cell.can_kill.add(board.get(i - 1).get(j + 1));
                    }
                }
                if ((i - 2 >= 0) && (j - 2 >= 0) &&
                        (board.get(i - 1).get(j - 1).state != CellState.no_chess) &&
                        (board.get(i - 1).get(j - 1).state != cell.state) &&
                        (board.get(i - 2).get(j - 2).state == CellState.no_chess)) {
                    if (!cell.can_kill.contains(board.get(i - 1).get(j - 1))) {
                        cell.can_kill.add(board.get(i - 1).get(j - 1));
                    }
                }
            }
        }
    }

    public void updateAround(List<List<Cell>> board, int i, int j) {
        //убираем из списка других шашек шашку, которую они теперь не могут съесть
        Cell now_cell = board.get(i).get(j);
        for (int y = 0; y < size_of_board; ++y) {
            for (int x = 0; x < size_of_board; ++x) {
                if (board.get(y).get(x).lady == CellLady.is_lady || ((Math.abs(i - y) == 1) && (Math.abs(j - x)) == 1)) {
                    board.get(y).get(x).can_kill.remove(now_cell);
                }
            }
        }
    }

    public void clean(List<List<Cell>> board) {
        for (int i = 0; i < size_of_board; ++i) {
            for (int j = 0; j < size_of_board; ++j) {
                if (board.get(i).get(j).state == CellState.no_chess) {
                    cleanSForOne(board, i, j);
                }
            }
        }
    }

    public void cleanSForOne(List<List<Cell>> board, int i, int j) {
        board.get(i).get(j).can_kill.clear();
    }
}

//главный класс -- шахматная доска
public class ChessBoard {
    //размер доски
    int size_of_board = 8;
    //обозначение дамки
    final int lady_number = 10;
    List<List<Cell>> board;

    //создает пустую доску (без шашек)
    public ChessBoard() {
        board = new ArrayList<>(size_of_board);
        for (int i = 0; i < size_of_board; ++i) {
            board.add(new ArrayList());
        }
        for (int i = 0; i < size_of_board; ++i) {
            for (int j = 0; j < size_of_board; ++j) {
                if (((i % 2 == 0) && j % 2 == 1) || (i % 2 == 1 && (j % 2 == 0))) {
                    board.get(i).add(new Cell(CellState.no_chess, CellColor.black, CellLady.normal_chess, j, i));
                } else {
                    board.get(i).add(new Cell(CellState.no_chess, CellColor.white, CellLady.normal_chess, j, i));
                }
            }
        }
    }

    public Pair<List<Cell>, List<Cell>> getWhiteBlackChess() {
        List<Cell> white = new ArrayList<>();
        List<Cell> black = new ArrayList<>();
        for (int i = 0; i < size_of_board; ++i) {
            for (int j = 0; j < size_of_board; ++j) {
                if (board.get(i).get(j).state == CellState.white_chess) {
                    white.add(board.get(i).get(j));
                } else if (board.get(i).get(j).state == CellState.black_chess) {
                    black.add(board.get(i).get(j));
                }
            }
        }
        return new Pair<>(white, black);
    }

    //размещаем исходные пешки
    public void placement(PairChess pos) {
        Parser parser = new Parser();
        //работаем с белыми - 0 и черными - 1 шашками

        for (int i = 0; i < 2; ++i) {
            List<Integer> now_pos;
            if (i == 0) {
                now_pos = parser.DoParse(pos.white);
            } else {
                now_pos = parser.DoParse(pos.black);
            }
            if (!now_pos.isEmpty()) {
                final int number_of_chess = now_pos.size() / 2;
                for (int j = 0; j < 2 * number_of_chess - 1; j += 2) {
                    int next_position = now_pos.get(j);
                    //проверка на дамку, приведение ее координаты к нормальному типу
                    if (next_position >= lady_number) {
                        next_position -= lady_number;
                        board.get(now_pos.get(j + 1)).get(next_position).lady = CellLady.is_lady;
                    }
                    //запоминание цвета шашки на данном поле
                    if (i == 0) {
                        board.get(now_pos.get(j + 1)).get(next_position).state = CellState.white_chess;
                    } else {
                        board.get(now_pos.get(j + 1)).get(next_position).state = CellState.black_chess;
                    }
                }
            }
        }
    }

    //перемещение шашки
    public void move(Motion motion) throws ExceptionInvalidMove {
        KillChess worker = new KillChess();
        //количество координат элементов для перемещения
        final int size = motion.to.size();
        for (int i = 0; i < size; ++i) {
            //обработка координаты клетки назначения, если перемещается дамка
            if (motion.to.get(i).getFirst_obj() >= lady_number) {
                motion.to.get(i).setFirst_obj(motion.to.get(i).getFirst_obj() - lady_number);
            }
        }
        //проверяем корректность всех переходов
        for (Pair<Integer, Integer> go_to : motion.to) {
            Integer word_coordinate = go_to.getFirst_obj();
            Integer number_coordinate = go_to.getSecond_obj();
            Cell our_cell = board.get(number_coordinate).get(word_coordinate);
            //если пытается пойти на белое поле
            if (our_cell.color == CellColor.white) {
                throw (new ExceptionInvalidMove("white cell"));
                //если пытается пойти на занятое поле
            } else if (our_cell.state != CellState.no_chess) {
                throw (new ExceptionInvalidMove("busy cell"));
            }
        }
        //отмечаем нынешнюю шашку
        Integer word_coordinate = motion.from.getFirst_obj();
        Integer number_coordinate = motion.from.getSecond_obj();
        Cell our_cell = board.get(number_coordinate).get(word_coordinate);
        //если не бьет, хотя сама может или кто-то из такого же цвета может
        Pair<List<Cell>, List<Cell>> black_white = getWhiteBlackChess();
        if (!motion.what_should_do.contains(-2) && (our_cell.state == CellState.white_chess)) {
            for (Cell chess : black_white.getFirst_obj()) {
                if (!chess.can_kill.isEmpty()) {
                    throw (new ExceptionInvalidMove("invalid move"));
                }
            }
        } else if (!motion.what_should_do.contains(-2) && (our_cell.state == CellState.black_chess)) {
            for (Cell chess : black_white.getSecond_obj()) {
                if (!chess.can_kill.isEmpty()) {
                    throw (new ExceptionInvalidMove("invalid move"));
                }
            }
        }

        //очищение поле, с которого шашка уходит + объясняем всем, кто может съесть, что она ушла
        worker.cleanSForOne(board, motion.from.getSecond_obj(), motion.from.getFirst_obj());
        worker.updateAround(board, motion.from.getSecond_obj(), motion.from.getFirst_obj());
        board.get(motion.from.getSecond_obj()).get(motion.from.getFirst_obj()).state = CellState.no_chess;
        board.get(motion.from.getSecond_obj()).get(motion.from.getFirst_obj()).lady = CellLady.no_chess;
        int coordinate_from_x = motion.from.getFirst_obj();
        int coordinate_from_y = motion.from.getSecond_obj();
        //работа с каждым перемещением
        for (int i = 0; i < size; ++i) {
            //если надо съесть шашку
            if (motion.what_should_do.get(i) == -2) {
                //находим координаты шашки, которую надо съесть
                int coordinate_x_kill = 0;
                int coordinate_y_kill = 0;
                if (motion.lady != CellLady.is_lady) {
                    coordinate_x_kill = (coordinate_from_x + motion.to.get(i).getFirst_obj()) / 2;
                    coordinate_y_kill = (coordinate_from_y + motion.to.get(i).getSecond_obj()) / 2;
                } else {
                    int y_coordinate = coordinate_from_y;
                    int x_coordinate = coordinate_from_x;
                    int y_to = motion.to.get(i).getSecond_obj();
                    int x_to = motion.to.get(i).getFirst_obj();
                    while (y_coordinate != y_to && x_coordinate != x_to) {
                        if ((y_to < y_coordinate) && (x_to < x_coordinate)) {
                            y_coordinate -= 1;
                            x_coordinate -= 1;
                        } else if ((y_to > y_coordinate) && (x_to > x_coordinate)) {
                            y_coordinate += 1;
                            x_coordinate += 1;
                        } else if ((y_to < y_coordinate) && (x_to > x_coordinate)) {
                            y_coordinate -= 1;
                            x_coordinate += 1;
                        } else if ((y_to > y_coordinate) && (x_to < x_coordinate)) {
                            y_coordinate += 1;
                            x_coordinate -= 1;
                        }
                        if (((y_coordinate != y_to) && (x_coordinate != x_to))
                                && board.get(y_coordinate).get(x_coordinate).state != CellState.no_chess) {
                            coordinate_x_kill = x_coordinate;
                            coordinate_y_kill = y_coordinate;
                        }
                    }
                }
                //убираем съеденную шашку у нее список тех кого она могла съесть, ее из всех списков тоже убираем
                worker.cleanSForOne(board, coordinate_y_kill, coordinate_x_kill);
                worker.updateAround(board, coordinate_y_kill, coordinate_x_kill);
                //теперь идем с нового места
                coordinate_from_y = motion.to.get(i).getSecond_obj();
                coordinate_from_x = motion.to.get(i).getFirst_obj();
                worker.cleanSForOne(board, coordinate_from_y, coordinate_from_x);
                worker.updateAround(board, coordinate_from_y, coordinate_from_x);

                board.get(coordinate_y_kill).get(coordinate_x_kill).state = CellState.no_chess;
                board.get(coordinate_y_kill).get(coordinate_x_kill).lady = CellLady.no_chess;
                //если сейчас последний ход, то надо перезаписать координаты ходящей шашки
                if (i == size - 1) {
                    board.get(motion.to.get(i).getSecond_obj()).get(motion.to.get(i).getFirst_obj()).state = motion.color;
                    board.get(motion.to.get(i).getSecond_obj()).get(motion.to.get(i).getFirst_obj()).lady = motion.lady;
                }

            }
            //если просто переместится, на месте перемещения записываем нашу шашку
            if (motion.what_should_do.get(i) == -1) {
                board.get(motion.to.get(i).getSecond_obj()).get(motion.to.get(i).getFirst_obj()).state = motion.color;
                board.get(motion.to.get(i).getSecond_obj()).get(motion.to.get(i).getFirst_obj()).lady = motion.lady;
            }
            //если во врем перемещения шашка одного цвета дошла до конца поля соперника делаем ее дамкой
            if (((motion.color == CellState.white_chess) && (motion.to.get(i).getSecond_obj() == 0))
                    || (motion.color == CellState.black_chess && (motion.to.get(i).getSecond_obj() == size_of_board - 1))) {
                board.get(motion.to.get(i).getSecond_obj()).get(motion.to.get(i).getFirst_obj()).lady = CellLady.is_lady;
            }
            //если ходы закончились, но она может бить
            our_cell = board.get(motion.to.get(i).getSecond_obj()).get(motion.to.get(i).getFirst_obj());
            if ((i == size - 1) && (!our_cell.can_kill.isEmpty()) && (our_cell.lady == CellLady.is_lady)) {
                for (Cell kill_sell : our_cell.can_kill) {
                    if (kill_sell.state != CellState.no_chess) {
                        throw (new ExceptionInvalidMove("invalid move"));
                    }
                }
            }
        }

        //обновляем список шашек, которые после хода могут убивать шашки(там, куда пришли)
        worker.update(board, motion.to.get(size - 1).getSecond_obj(), motion.to.get(size - 1).getFirst_obj());
        //но так же надо обновить информацию для всех соседних шашек, после хода что-то могло измениться
        worker.update(board, motion.to.get(size - 1).getSecond_obj() - 1, motion.to.get(size - 1).getFirst_obj() - 1);
        worker.update(board, motion.to.get(size - 1).getSecond_obj() + 1, motion.to.get(size - 1).getFirst_obj() - 1);
        worker.update(board, motion.to.get(size - 1).getSecond_obj() - 1, motion.to.get(size - 1).getFirst_obj() + 1);
        worker.update(board, motion.to.get(size - 1).getSecond_obj() + 1, motion.to.get(size - 1).getFirst_obj() + 1);
    }

    //класс перемещения(хранит информацию о перемещении)
    static class Motion {
        //координаты клетки отправления
        //первая координата - буква, вторая - цифра
        Pair<Integer, Integer> from;
        //массив с координатами клеток назначений
        List<Pair<Integer, Integer>> to;
        //информация о том, что шашка делает(бьет или просто перемещается)
        List<Integer> what_should_do;
        //цвет шашки, которая перемещается
        CellState color;
        //является ли шашка дамкой
        CellLady lady;

        public Motion(Pair<Integer, Integer> from, List<Pair<Integer, Integer>> to, List<Integer> what_should_do,
                      CellState color, CellLady lady) {
            this.from = from;
            this.to = to;
            this.what_should_do = what_should_do;
            this.color = color;
            this.lady = lady;
        }

        //обновление информации про перемещающуюся шашку, если она вдруг стала дамкой
        public void updateStatus(List<List<Cell>> my_board) {
            if (my_board.get(from.getSecond_obj()).get(from.getFirst_obj()).lady == CellLady.is_lady) {
                this.lady = CellLady.is_lady;
            }
        }
    }

    //изменение положений шашек
    public void changes(List<String> change) throws ExceptionInvalidMove {
        Parser parser = new Parser();
        //массив, в который будут записываться все перемещения
        List<Motion> motions = new ArrayList<>();

        for (String str : change) {
            //делим строку с перемещениями на перемещение белой и черной
            String[] pos = str.split(" ");

            //если 0, то работаем с белой, иначе с черной
            for (int i = 0; i < pos.length; ++i) {
                String new_change = pos[i];
                //переводим координату перемещения
                List<Integer> positions = parser.DoParse(new_change);
                CellLady our_cell = CellLady.normal_chess;
                Integer positions_0 = positions.get(0);

                int number_of_cells = positions.size();
                CellState color = CellState.black_chess;
                if (i == 0) {
                    color = CellState.white_chess;
                }
                List<Pair<Integer, Integer>> array_of_steps = new ArrayList<>();
                //запоминаем, что шашка делает
                List<Integer> what_should_do = new ArrayList<>();
                what_should_do.add(positions.get(2));
                //добавляем перемещение (первый раз делаем обязательно, так как хоть куда-то
                // шашка обязана перейти)
                int j = 3;
                //если шашка дамка, запоминаем это и приводим ее координату к нормальному виду
                if (positions.get(0) > lady_number) {
                    our_cell = CellLady.is_lady;
                    positions_0 -= lady_number;
                }
                do {
                    //добавляем координаты первого назначения
                    array_of_steps.add(new Pair<>(positions.get(j), positions.get(j + 1)));
                    //если есть еще перемещения, то записываем это
                    if (j + 2 < number_of_cells) {
                        what_should_do.add(positions.get(j + 2));
                    }
                    j += 3;
                } while (j <= number_of_cells - 2);
                //добавляем перемещение
                motions.add(new Motion(new Pair<>(positions_0, positions.get(1)),
                        array_of_steps,
                        what_should_do, color, our_cell));
            }
        }
        KillChess worker = new KillChess();

        //перемещаемся
        for (Motion motion : motions) {
            worker.clean(board);
            worker.prepareBoard(board);
            move(motion);
        }
    }

    //показываем, какие шашки сейчас на доске
    public List<List<String>> showPosition() {
        Parser parser = new Parser();
        final int number_colors = 2;
        List<List<String>> answer = new ArrayList<>(number_colors);
        for (int i = 0; i < number_colors; ++i) {
            answer.add(new ArrayList());
        }
        List<String> white_position = new ArrayList<>();
        List<String> black_position = new ArrayList<>();

        //Пробегаем всю карту и смотрим на оставшиеся шашки, сортировка по букве, поэтому внешний цикл по ним
        for (int j = 0; j < size_of_board; ++j) {
            //Помним, что нумерация у нас перевернутая
            for (int i = size_of_board - 1; i >= 0; --i) {
                if (board.get(i).get(j).state != CellState.no_chess) {
                    if (board.get(i).get(j).state == CellState.white_chess) {
                        white_position.add(parser.BackParser(i, j, board.get(i).get(j)));
                        Collections.sort(white_position);
                    } else {
                        black_position.add(parser.BackParser(i, j, board.get(i).get(j)));
                        Collections.sort(black_position);
                    }
                }
            }
        }

        answer.get(0).addAll(white_position);
        answer.get(1).addAll(black_position);

        return answer;
    }
}
