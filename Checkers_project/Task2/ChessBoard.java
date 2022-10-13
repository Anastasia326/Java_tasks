import java.awt.*;
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

class SmallChess {
    CellState color;
    CellLady lady;

    SmallChess(CellState my_color, CellLady is_lady) {
        color = my_color;
        lady = is_lady;
    }
}

//класс клетка, состояние клетки: цвет и есть ли на ней пешка, описание пешки(цвет и дамка ли)
class Cell {
    CellState state;
    CellColor color;
    CellLady lady;
    List<Cell> can_kill;
    List<SmallChess> town;
    int word_coordinate;
    int number_coordinate;

    Cell(CellState st, CellColor col, CellLady l, int i, int j, List<SmallChess> my_town) {
        state = st;
        color = col;
        lady = l;
        can_kill = new ArrayList<>();
        word_coordinate = i;
        number_coordinate = j;
        town = my_town;
    }

    Cell(List<Integer> coordinates) {
        color = CellColor.black;
        if ((coordinates.get(2) == -6) || (coordinates.get(2) == -8)) {
            lady = CellLady.is_lady;
        } else {
            lady = CellLady.normal_chess;
        }
        can_kill = new ArrayList<>();
        word_coordinate = coordinates.get(0);
        number_coordinate = coordinates.get(1);
        int size = coordinates.size();
        town = new ArrayList<>();
        for (int j = 2; j < size; ++j) {
            if (coordinates.get(j) == -3) {
                town.add(new SmallChess(CellState.white_chess, CellLady.normal_chess));
            } else if (coordinates.get(j) == -4) {
                town.add(new SmallChess(CellState.black_chess, CellLady.normal_chess));
            } else if (coordinates.get(j) == -6) {
                town.add(new SmallChess(CellState.white_chess, CellLady.is_lady));
            } else if (coordinates.get(j) == -8) {
                town.add(new SmallChess(CellState.black_chess, CellLady.is_lady));
            }
        }
    }


    public void addChess(List<List<Cell>> board, Cell cell, Cell another_cell) {
            SmallChess change_chess = another_cell.town.get(0);
            //добавляем съеденную в начало башни
            cell.town.add(change_chess);
            another_cell.town.remove(change_chess);
            if (another_cell.town.size() == 0) {
                another_cell.state = CellState.no_chess;
                another_cell.can_kill.clear();
                KillChess worker = new KillChess();
                worker.cleanSForOne(board, another_cell.number_coordinate, another_cell.word_coordinate);
                worker.updateAround(board, number_coordinate, another_cell.word_coordinate);
                board.get(another_cell.number_coordinate).get(another_cell.word_coordinate).state = CellState.no_chess;
            } else {
                if (another_cell.town.get(0).lady == CellLady.is_lady) {
                    another_cell.lady = CellLady.is_lady;
                } else {
                    another_cell.lady = CellLady.normal_chess;
                }
                if (another_cell.town.get(0).color == CellState.white_chess) {
                    another_cell.state = CellState.white_chess;
                } else {
                    another_cell.state = CellState.black_chess;
                }
            }
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
                    board.get(i).add(new Cell(CellState.no_chess, CellColor.black, CellLady.normal_chess, j, i, new ArrayList<>()));
                } else {
                    board.get(i).add(new Cell(CellState.no_chess, CellColor.white, CellLady.normal_chess, j, i, new ArrayList<>()));
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
            List<List<Integer>> now_pos;
            if (i == 0) {
                now_pos = parser.doParse(pos.white);
            } else {
                now_pos = parser.doParse(pos.black);
            }
            if (!now_pos.isEmpty()) {
                for (List<Integer> now_element : now_pos) {
                    int word_coordinate = now_element.get(0);
                    int number_coordinate = now_element.get(1);
                    if (word_coordinate >= lady_number) {
                        word_coordinate -= lady_number;
                        board.get(number_coordinate).get(word_coordinate).lady = CellLady.is_lady;
                    }
                    Cell now_cell = board.get(number_coordinate).get(word_coordinate);
                    now_cell.word_coordinate = word_coordinate;
                    now_cell.number_coordinate = number_coordinate;
                    if (i == 0) {
                        now_cell.state = CellState.white_chess;
                    } else {
                        now_cell.state = CellState.black_chess;
                    }
                    for (int j = 2; j < now_element.size(); ++j) {
                        if (now_element.get(j) == -3) {
                            now_cell.town.add(new SmallChess(CellState.white_chess, CellLady.normal_chess));
                        } else if (now_element.get(j) == -4) {
                            now_cell.town.add(new SmallChess(CellState.black_chess, CellLady.normal_chess));
                        } else if (now_element.get(j) == -6) {
                            now_cell.town.add(new SmallChess(CellState.white_chess, CellLady.is_lady));
                        } else if (now_element.get(j) == -8) {
                            now_cell.town.add(new SmallChess(CellState.black_chess, CellLady.is_lady));
                        }
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
            if (motion.to.get(i).word_coordinate >= lady_number) {
                motion.to.get(i).word_coordinate -= lady_number;
            }
        }
        //проверяем корректность всех переходов
        for (Cell go_to : motion.to) {
            int word_coordinate = go_to.word_coordinate;
            int number_coordinate = go_to.number_coordinate;
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
        int word_coordinate = motion.from.word_coordinate;
        int number_coordinate = motion.from.number_coordinate;
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
        worker.cleanSForOne(board, motion.from.number_coordinate, motion.from.word_coordinate);
        worker.updateAround(board, motion.from.number_coordinate, motion.from.word_coordinate);
        board.get(motion.from.number_coordinate).get(motion.from.word_coordinate).state = CellState.no_chess;
        board.get(motion.from.number_coordinate).get(motion.from.word_coordinate).lady = CellLady.no_chess;
        int coordinate_from_x = motion.from.word_coordinate;
        int coordinate_from_y = motion.from.number_coordinate;
        Cell old_cell = motion.from;
        //работа с каждым перемещением
        for (int i = 0; i < size; ++i) {
            //если надо съесть шашку
            if (motion.what_should_do.get(i) == -2) {
                //находим координаты шашки, которую надо съесть
                int coordinate_x_kill = 0;
                int coordinate_y_kill = 0;
                if (motion.lady != CellLady.is_lady) {
                    coordinate_x_kill = (coordinate_from_x + motion.to.get(i).word_coordinate) / 2;
                    coordinate_y_kill = (coordinate_from_y + motion.to.get(i).number_coordinate) / 2;
                } else {
                    int y_coordinate = coordinate_from_y;
                    int x_coordinate = coordinate_from_x;
                    int y_to = motion.to.get(i).number_coordinate;
                    int x_to = motion.to.get(i).word_coordinate;
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

                //убираем съеденную шашку
                our_cell.addChess(board, old_cell, board.get(coordinate_y_kill).get(coordinate_x_kill));
                //теперь идем с нового места
                coordinate_from_y = motion.to.get(i).number_coordinate;
                coordinate_from_x = motion.to.get(i).word_coordinate;
                worker.cleanSForOne(board, coordinate_from_y, coordinate_from_x);
                worker.updateAround(board, coordinate_from_y, coordinate_from_x);

                //если сейчас последний ход, то надо перезаписать координаты ходящей шашки
                if (i == size - 1) {
                    board.get(motion.to.get(i).number_coordinate).get(motion.to.get(i).word_coordinate).state = motion.color;
                    board.get(motion.to.get(i).number_coordinate).get(motion.to.get(i).word_coordinate).town = old_cell.town;
                }

            }
            //если просто переместится, на месте перемещения записываем нашу шашку
            if (motion.what_should_do.get(i) == -1) {
                board.get(motion.to.get(i).number_coordinate).get(motion.to.get(i).word_coordinate).state = motion.color;
                board.get(motion.to.get(i).number_coordinate).get(motion.to.get(i).word_coordinate).lady = motion.lady;
                board.get(motion.to.get(i).number_coordinate).get(motion.to.get(i).word_coordinate).town = old_cell.town;
            }
            //если во врем перемещения шашка одного цвета дошла до конца поля соперника делаем ее дамкой
            if (((motion.color == CellState.white_chess) && (motion.to.get(i).number_coordinate == 0))
                    || (motion.color == CellState.black_chess && (motion.to.get(i).number_coordinate == size_of_board - 1))) {
                old_cell.lady = CellLady.is_lady;
            }
            //если ходы закончились, но она может бить
            our_cell = board.get(motion.to.get(i).number_coordinate).get(motion.to.get(i).word_coordinate);
            if ((i == size - 1) && (!our_cell.can_kill.isEmpty()) && (our_cell.lady == CellLady.is_lady)) {
                for (Cell kill_sell : our_cell.can_kill) {
                    if (kill_sell.state != CellState.no_chess) {
                        throw (new ExceptionInvalidMove("invalid move"));
                    }
                }
            }
        }

        board.get(motion.to.get(size - 1).number_coordinate).get(motion.to.get(size - 1).word_coordinate).lady = old_cell.lady;
        //обновляем список шашек, которые после хода могут убивать шашки(там, куда пришли)
        worker.update(board, motion.to.get(size - 1).number_coordinate, motion.to.get(size - 1).word_coordinate);
        //но так же надо обновить информацию для всех соседних шашек, после хода что-то могло измениться
        worker.update(board, motion.to.get(size - 1).number_coordinate - 1, motion.to.get(size - 1).word_coordinate - 1);
        worker.update(board, motion.to.get(size - 1).number_coordinate + 1, motion.to.get(size - 1).word_coordinate - 1);
        worker.update(board, motion.to.get(size - 1).number_coordinate - 1, motion.to.get(size - 1).word_coordinate + 1);
        worker.update(board, motion.to.get(size - 1).number_coordinate + 1, motion.to.get(size - 1).word_coordinate + 1);
    }

    //класс перемещения(хранит информацию о перемещении)
    static class Motion {
        //координаты клетки отправления
        //первая координата - буква, вторая - цифра
        Cell from;
        //массив с координатами клеток назначений
        List<Cell> to;
        //информация о том, что шашка делает(бьет или просто перемещается)
        List<Integer> what_should_do;
        //цвет шашки, которая перемещается
        CellState color;
        //является ли шашка дамкой
        CellLady lady;

        public Motion(Cell from, List<Cell> to, List<Integer> what_should_do,
                      CellState color, CellLady lady) {
            this.from = from;
            this.to = to;
            this.what_should_do = what_should_do;
            this.color = color;
            this.lady = lady;
        }

        //обновление информации про перемещающуюся шашку, если она вдруг стала дамкой
        public void updateStatus(List<List<Cell>> my_board, Motion motion) {
            if (my_board.get(motion.from.number_coordinate).get(motion.from.word_coordinate).lady == CellLady.is_lady) {
                motion.lady = CellLady.is_lady;
            }
        }
    }

    List<Integer> split(List<Integer> old_list, int start, int finish) {
        List<Integer> answer = new ArrayList<>();
        for (int i = start; i < finish; ++i) {
            answer.add(old_list.get(i));
        }
        return answer;
    }


    //изменение положений шашек
    public void changes(List<String> change) throws ExceptionInvalidMove {
        Parser parser = new Parser();
        //массив, в который будут записываться все перемещения
        List<Motion> motions = new ArrayList<>();

        for (String str : change) {
            List<List<Integer>> position = parser.doParse(str);
            //если 0, то работаем с белой, иначе с черной
            for (int i = 0; i < position.size(); ++i) {
                //переводим координату перемещения
                List<Integer> positions = position.get(i);
                List<Integer> should_go = new ArrayList<>();
                List<List<Integer>> cells_coordinate = new ArrayList<>();
                CellLady lady = CellLady.normal_chess;

                //проходимся по перемещению и делим его на координаты шашек, которые присутствуют, и на то, что надо делать во время хода
                int now_start = 0;
                List<Integer> new_cell = new ArrayList<>();
                for (int k = 0; k < positions.size(); ++k) {
                    if ((k == 0) && (positions.get(k) >= lady_number)) {
                        int new_coordinate = positions.get(k) - 1;
                        positions.set(k, new_coordinate);
                        lady = CellLady.is_lady;
                    }
                    if (positions.get(k) == -1) {
                        should_go.add(-1);
                        new_cell = split(positions, now_start, k);
                        now_start = k + 1;
                        cells_coordinate.add(new_cell);
                    } else if (positions.get(k) == -2) {
                        should_go.add(-2);
                        new_cell = split(positions, now_start, k);
                        now_start = k + 1;
                        cells_coordinate.add(new_cell);
                    }
                }
                new_cell = split(positions, now_start, positions.size());
                cells_coordinate.add(new_cell);

                Cell from = new Cell(cells_coordinate.get(0));
                List<Cell> to = new ArrayList<>();
                for (int k = 1; k < cells_coordinate.size(); ++k) {
                    to.add(new Cell(cells_coordinate.get(k)));
                }

                CellState color = CellState.white_chess;
                if (i == 1) {
                    color = CellState.black_chess;
                }

                motions.add(new Motion(from, to, should_go, color, lady));
            }
        }

        KillChess worker = new KillChess();

        //перемещаемся
        for (Motion motion : motions) {
            worker.clean(board);
            worker.prepareBoard(board);
            motion.updateStatus(board, motion);
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
                        white_position.add(parser.BackParser(board.get(i).get(j)));
                    } else {
                        black_position.add(parser.BackParser(board.get(i).get(j)));
                    }
                }
            }
        }
        Collections.sort(white_position);
        Collections.sort(black_position);
        answer.get(0).addAll(white_position);
        answer.get(1).addAll(black_position);

        return answer;
    }
}
