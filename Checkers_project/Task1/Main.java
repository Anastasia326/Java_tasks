import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;


public class Main {

    public static void main(String[] args) {
        try(Scanner scan = new Scanner(System.in)){
            List<String> data = new ArrayList<>();
            //считываем данные
            while (scan.hasNext()) {
                data.add(scan.nextLine());
            }
            //пытаемся начать игру, ловим ошибки, если есть
            StartGame new_game = new StartGame(data);
            try {
                new_game.run();
            } catch (ExceptionInvalidMove e) {
                System.out.println(e.getMessage());
                return;
            } 
            //выводим координаты оставшихся координат
            for (List<String> position : new_game.position()) {
                for (String coordinate : position) {
                    System.out.print(coordinate + " ");
                }
                System.out.println();
            }
        }
    }
}
