import org.h2.jdbcx.JdbcConnectionPool;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static DataSource dataSource = JdbcConnectionPool.create("jdbc:h2:mem:database;DB_CLOSE_DELAY=-1",
            "", "");
    private static SQLWorker source = new SQLWorker(dataSource);

    public static void main(String[] args) throws SQLException, IOException, Exception {
        RequestWorker data = new RequestWorker(source);
        data.create();
        data.fill();
        System.out.println("Введите сколько запросов вы хотите совершить.");
        Scanner in = new Scanner(System.in);
        int numberQueries = in.nextInt();

        for (int i = 0; i < numberQueries; ++i) {
            System.out.println("Введите номер запроса(как в задании).");
            int query = in.nextInt();
            if (query == 1) {
                Pair<ArrayList<String>, ArrayList<FormatOfAnswerTwoColumns<String, String>>> answer =  data.CitiesWithMoreThenOneAirports();
                DrawTables<FormatOfAnswerTwoColumns<String, String>> reportGenerator = new DrawTables<>();
                reportGenerator.work(answer, "table_1.xls", answer.getSecond().size());
            } else if (query == 2) {
                Pair<ArrayList<String>, ArrayList<FormatOfAnswerTwoColumns<String, String>>> answer =  data.CitiesWithProblems();
                DrawTables<FormatOfAnswerTwoColumns<String, String>> reportGenerator = new DrawTables<>();
                reportGenerator.work(answer, "table_2.xls", answer.getSecond().size());
            } else if (query == 3) {
                Pair<ArrayList<String>, ArrayList<FormatOfAnswerThreeColumns<String, String, Long>>> answer =  data.ShortWay();
                DrawTables<FormatOfAnswerThreeColumns<String, String, Long>> reportGenerator = new DrawTables<>();
                reportGenerator.work(answer, "table_3.xls", answer.getSecond().size());
            } else if (query == 4) {
                Pair<ArrayList<String>, ArrayList<FormatOfAnswerTwoColumns<String, String>>> answer =  data.FligthsProblems();
                DrawTables<FormatOfAnswerTwoColumns<String, String>> reportGenerator = new DrawTables<>();
                reportGenerator.work(answer, "table_4.xls", answer.getSecond().size());
                DrawGraphics<FormatOfAnswerTwoColumns<String, String>> graphGenerator = new DrawGraphics<>();
                graphGenerator.Draw(answer, "graph_4.png", "Отмененные рейсы по месяцам", answer.getSecond().size());
            } else if (query == 5) {
                Pair<ArrayList<String>, ArrayList<FormatOfAnswerTwoColumns<String, String>>> answer =  data.FligthsToMoscow();
                DrawTables<FormatOfAnswerTwoColumns<String, String>> reportGenerator = new DrawTables<>();
                reportGenerator.work(answer, "table_5_to.xls", answer.getSecond().size());
                DrawGraphics<FormatOfAnswerTwoColumns<String, String>> graphGenerator = new DrawGraphics<>();
                graphGenerator.Draw(answer, "graph_5_to.png", "Рейсы в Москву", answer.getSecond().size());
                answer =  data.FligthsFromMoscow();
                reportGenerator = new DrawTables<>();
                reportGenerator.work(answer, "table_5_from.xls", answer.getSecond().size());
                graphGenerator = new DrawGraphics<>();
                graphGenerator.Draw(answer, "graph_5_from.png", "Рейсы из Москвы", answer.getSecond().size());
            } else if (query == 6) {
                //ТУТ МОЖНО ПОМЕНЯТЬ МОДЕЛЬ
                String model = "KHV";
                data.DeleteFlight(model);
                System.out.println("Успешно.");
            } else if (query == 7) {
                //ТУТ МОЖНО ПОМЕНЯТЬ ДАТЫ
                String start = "2017-07-29";
                String end = "2017-09-29";
                Pair<ArrayList<String>, ArrayList<FormatOfAnswerTwoColumns<String, Long>>> answer =  data.Covid2017(start, end);
                DrawTables<FormatOfAnswerTwoColumns<String, Long>> reportGenerator = new DrawTables<>();
                reportGenerator.work(answer, "table_7.xls", answer.getSecond().size());
                DrawGraphics<FormatOfAnswerTwoColumns<String, Long>> graphGenerator = new DrawGraphics<>();
                graphGenerator.Draw(answer, "graph_7.png", "Убытки по месяцам", answer.getSecond().size());
            } else if (query == 8) {
                //ТУТ МОЖНО ПОМЕНЯТЬ ДАННЫЕ
                try {
                    String flightId = "5548";
                    String numberSeat = "5D";
                    String condition = "Business";
                    String id = "8149";
                    String bookingDate = "2017-06-24";
                    String ticketNumber = "101";
                    String name = "Ivanov Ivan";
                    String boardingNumber = "5";
                    data.AddTicket(flightId, numberSeat, condition, id, bookingDate, ticketNumber, name, boardingNumber);
                    System.out.println("Успешно.");
                } catch (IllegalArgumentException e) {
                    System.out.println("Неверные данные");
                }
            }
        }
    }
}
//KHV