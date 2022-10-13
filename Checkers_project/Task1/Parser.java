import java.util.ArrayList;
import java.util.List;

//класс, который отвечает за преобразование доски из человеческого формата(буква и число)
//в компьютерный (массив из координат от 0 до 7)
//исключений не может быть, так как он только преобразует, а если дошли до преобразования,
//то подразумевается, что все данные валидные
public class Parser {
    final int lady_number = 10;
    final int size_of_board = 8;

    //парсит буквенно-численную координату шашки в числовие координаты
    public List<Integer> doParse(String str) {
        char[] strToArray = str.toCharArray();
        Character[] num = {'1', '2', '3', '4', '5', '6', '7', '8'};
        List<Character> numbers = new ArrayList<>(List.of(num));
        Character[] let = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        List<Character> letter = new ArrayList<>(List.of(let));
        Character[] big = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
        List<Character> big_letter = new ArrayList<>(List.of(big));
        List<Integer> positions = new ArrayList<>();

        for (char symbol : strToArray) {
            if (numbers.contains(symbol)) {
                // Так как у нас доска перевернута в понимании программы, нумерация ведь снизу вверх
                positions.add(size_of_board - Integer.parseInt(String.valueOf(symbol)));
            } else if (letter.contains(symbol)) {
                int symbol_int = symbol - 'a';
                positions.add(symbol_int);
            }
            //если дамка, то переводим все равно в обычную координату, но запоминаем, что она дома путем добавления 10
            else if (big_letter.contains(symbol)) {
                int symbol_int = symbol - 'A' + lady_number;
                positions.add(symbol_int);
            } else if (symbol == '-') {
                positions.add(-1);
            } else if (symbol == ':') {
                positions.add(-2);
            }
        }
        return positions;
    }

    //парсим обратно в буквенный формат
    public String backParser(int i, int j, Cell cell) {
        String answer = "";
        if (cell.lady == CellLady.is_lady) {
            char my_char = (char) ('A' + j);
            answer += my_char;
        } else {
            answer += (char) ('a' + j);
        }
        answer += (size_of_board - i);
        return answer;
    }
}
