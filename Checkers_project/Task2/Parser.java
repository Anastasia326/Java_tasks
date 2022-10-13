import java.util.ArrayList;
import java.util.List;

//класс, который отвечает за преобразование доски из человеческого формата(буква и число)
//в компьютерный (массив из координат от 0 до 7)
//исключений не может быть, так как он только преобразует, а если дошли до преобразования,
//то подразумевается, что все данные валидные
public class Parser {
    final int lady_number = 10;
    final int size_of_board = 8;

    //парсит буквенно-численную координату шашки в числовые координаты
    public List<List<Integer>> doParse(String str) {
        //char[] strToArray = str.toCharArray();
        String[] small_string = str.split(" ");
        Character[] num = {'1', '2', '3', '4', '5', '6', '7', '8'};
        List<Character> numbers = new ArrayList<>(List.of(num));
        Character[] let = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        List<Character> letter = new ArrayList<>(List.of(let));
        Character[] big = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
        List<Character> big_letter = new ArrayList<>(List.of(big));
        List<List<Integer>> positions = new ArrayList<>();

        for (String string : small_string) {
            char[] strToArray = string.toCharArray();
            List<Integer> now_parse = new ArrayList<>();
            boolean now_town = false;
            //-3 - белая, -4 - черная, -6 белая дамка, -8 - черная дамка, это не костыли, а обозначения для удобной работы)
            for (char symbol : strToArray) {
                if (numbers.contains(symbol)) {
                    // Так как у нас доска перевернута в понимании программы, нумерация ведь снизу вверх
                    now_parse.add(size_of_board - Integer.parseInt(String.valueOf(symbol)));
                } else if (symbol == 'w' && (now_town)) {
                    now_parse.add(-3);
                } else if (symbol == 'b' && (now_town)) {
                    now_parse.add(-4);
                } else if (symbol == 'W' && (now_town)) {
                    now_parse.add(-6);
                } else if (symbol == 'B' && (now_town)) {
                    now_parse.add(-8);
                } else if (letter.contains(symbol)) {
                    int symbol_int = symbol - 'a';
                    now_parse.add(symbol_int);
                }
                //если дамка, то переводим все равно в обычную координату, но запоминаем, что она дома путем добавления 10
                else if (big_letter.contains(symbol)) {
                    int symbol_int = symbol - 'A' + lady_number;
                    now_parse.add(symbol_int);
                } else if (symbol == '-') {
                    now_town = false;
                    now_parse.add(-1);
                } else if (symbol == ':') {
                    now_town = false;
                    now_parse.add(-2);
                } else if (symbol == '_') {
                    now_town = true;
                }
            }
            positions.add(now_parse);
        }
        return positions;
    }

    //парсим обратно в буквенный формат
    public String backParser(Cell cell) {
        StringBuilder answer = new StringBuilder();
        final int size = cell.town.size();
        char word_answer;

        if (cell.lady == CellLady.is_lady) {
            //word_answer = (char) ('A' + cell.word_coordinate);
            answer.append((char) ('a' + cell.word_coordinate));
            cell.town.get(0).lady = CellLady.is_lady;
            //answer.append(word_answer);
        } else {
            answer.append((char) ('a' + cell.word_coordinate));
        }
        answer.append(size_of_board - cell.number_coordinate);
        answer.append('_');
        for (int i = 0; i < size; ++i) {
            if (cell.town.get(i).lady == CellLady.is_lady) {
                if (cell.town.get(i).color == CellState.white_chess) {
                    answer.append('W');
                } else {
                    answer.append('B');
                }
            } else {
                if (cell.town.get(i).color == CellState.white_chess) {
                    answer.append('w');
                } else {
                    answer.append('b');
                }
            }
        }
        return answer.toString();
    }
}
