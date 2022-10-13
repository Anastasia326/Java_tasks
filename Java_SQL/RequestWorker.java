import java.io.IOException;
import java.sql.SQLException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

public class RequestWorker {
    final SQLWorker source;

    private String SQL(String name) throws IOException {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        Main.class.getResourceAsStream(name),
                        StandardCharsets.UTF_8))) {
            return br.lines().collect(Collectors.joining("\n"));
        }
    }

    //Код с лекции(похожий)
    private String City(String string) {
        String[] words = string.split(" ");
        return words[3].substring(1, words[3].length() - 2);
    }

    //Задача A. Загрузка данных
    //1. Создать базу данных и все необходимые таблицы
    public void create() throws SQLException, IOException {
        String sql = SQL("tables.sql");
        source.statement(stmt -> {
            stmt.execute(sql);
        });
    }

    public RequestWorker(SQLWorker source) {
        this.source = source;
    }

    //2. Заполнить БД данными из скачанных файлов.
    public void fill() throws IOException, SQLException {
        FillTables fillTables = new FillTables();
        fillTables.fillAircrafts(source);
        fillTables.fillAirports(source);
        fillTables.fillBoardingPases(source);
        fillTables.fillBookings(source);
        fillTables.fillFlights(source);
        fillTables.fillSeats(source);
        fillTables.fillTicketFlights(source);
        fillTables.fillTickets(source);
    }

    //Задача B. Работа с БД
    //1. Вывести города, в которых несколько аэропортов.
    public Pair<ArrayList<String>, ArrayList<FormatOfAnswerTwoColumns<String, String>>> CitiesWithMoreThenOneAirports() throws IOException, SQLException {
        String sql = SQL("query1.sql");
        ArrayList<FormatOfAnswerTwoColumns<String, String>> answer = new ArrayList<>();

        ArrayList<String> headers = new ArrayList<>(Arrays.asList("City", "Airports"));

        source.statement(statement -> {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                answer.add(new FormatOfAnswerTwoColumns<String, String>(
                        City(resultSet.getString("city")),
                        resultSet.getString("airports_in_city")
                ));
            }
        });

        return new Pair<>(headers, answer);
    }

    //2. Вывести города, из которых чаще всего отменяли рейсы.
    public Pair<ArrayList<String>, ArrayList<FormatOfAnswerTwoColumns<String, String>>> CitiesWithProblems() throws IOException, SQLException {
        String sql = SQL("query2.sql");
        ArrayList<FormatOfAnswerTwoColumns<String, String>> answer = new ArrayList<>();
        ArrayList<String> headers = new ArrayList<>(Arrays.asList("Город", "Количество отмененных рейсов"));

        source.statement(statement -> {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                answer.add(new FormatOfAnswerTwoColumns<String, String>(
                        City(resultSet.getString("city")),
                        resultSet.getString("cnt")
                ));
            }
        });

        return new Pair<>(headers, answer);
    }

    //3. В каждом городе вылета найти самый короткий маршрут. Отсортировать по продолжительности.
    public Pair<ArrayList<String>, ArrayList<FormatOfAnswerThreeColumns<String, String, Long>>> ShortWay() throws IOException, SQLException {
        String sql = SQL("query3.sql");
        ArrayList<FormatOfAnswerThreeColumns<String, String, Long>> answer = new ArrayList<>();
        ArrayList<String> headers = new ArrayList<>(Arrays.asList("Город", "Пункт прибытия", "Средняя продолжительность полета в минутах"));

        source.statement(statement -> {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                answer.add(new FormatOfAnswerThreeColumns<String, String, Long>(
                        City(resultSet.getString("from_air")),
                        City(resultSet.getString("to_air")),
                        Long.valueOf(resultSet.getString("avg_time"))
                ));
            }
        });

        return new Pair<>(headers, answer);
    }

    //4. Найти кол-во отмен рейсов по месяцам.
    public Pair<ArrayList<String>, ArrayList<FormatOfAnswerTwoColumns<String, String>>> FligthsProblems() throws IOException, SQLException {
        String sql = SQL("query4.sql");
        ArrayList<FormatOfAnswerTwoColumns<String, String>> answer = new ArrayList<>();
        ArrayList<String> headers = new ArrayList<>(Arrays.asList("Месяц", "Количество отмененных рейсов"));

        source.statement(statement -> {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                answer.add(new FormatOfAnswerTwoColumns<String, String>(
                        resultSet.getString("month"),
                        resultSet.getString("problem_raice")
                ));
            }
        });

        return new Pair<>(headers, answer);
    }

    //5. Выведите кол-во рейсов в Москву
    public Pair<ArrayList<String>, ArrayList<FormatOfAnswerTwoColumns<String, String>>> FligthsToMoscow() throws IOException, SQLException {
        String sql = SQL("query5.sql");
        ArrayList<FormatOfAnswerTwoColumns<String, String>> answer = new ArrayList<>();
        ArrayList<String> headers = new ArrayList<>(Arrays.asList("День", "Количество рейсов"));

        source.statement(statement -> {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                answer.add(new FormatOfAnswerTwoColumns<String, String>(
                        resultSet.getString("day"),
                        resultSet.getString("flights")
                ));
            }
        });

        return new Pair<>(headers, answer);
    }

    //и из Москвы по дням недели за весь наблюдаемый период.
    public Pair<ArrayList<String>, ArrayList<FormatOfAnswerTwoColumns<String, String>>> FligthsFromMoscow() throws IOException, SQLException {
        String sql = SQL("query5_1.sql");
        ArrayList<FormatOfAnswerTwoColumns<String, String>> answer = new ArrayList<>();
        ArrayList<String> headers = new ArrayList<>(Arrays.asList("День", "Количество рейсов"));

        source.statement(statement -> {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                answer.add(new FormatOfAnswerTwoColumns<String, String>(
                        resultSet.getString("day"),
                        resultSet.getString("flights")
                ));
            }
        });

        return new Pair<>(headers, answer);
    }

    //6. Отменить все рейсы самолета, заданной модели (модель - параметр).
    // Все билеты, относящиеся к удаленным рейсам - удалить.
    public void DeleteFlight(String model) throws IOException, SQLException {
        String sql = SQL("query6.sql");
        source.preparedStatement(sql, statement -> {
            statement.setString(1, model);
            statement.execute();
        });
    }

    //7. В связи с пандемией COVID-2017 все рейсы, прибывающие в Москву и отбывающие из неё, запланированные на даты в
    // интервале, заданном параметром (например,  [01.08.17, 15.08.17]), были отменены.
    // Перевести соответствующие рейсы в Cancelled, а также посчитать убыток, который теряют компании-перевозчики по дням.
    public Pair<ArrayList<String>, ArrayList<FormatOfAnswerTwoColumns<String, Long>>> Covid2017(String fromDate, String toDate) throws IOException, SQLException {
        String sql = SQL("query7.sql");
        String updateSql = SQL("update7.sql");
        ArrayList<FormatOfAnswerTwoColumns<String, Long>> answer = new ArrayList<>();
        ArrayList<String> headers = new ArrayList<>(Arrays.asList("День", "Убытки"));
        source.connection(connection -> {
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(sql);
            PreparedStatement statementUpdate = connection.prepareStatement(updateSql);

            statementUpdate.setDate(1, Date.valueOf(fromDate));
            statementUpdate.setDate(2, Date.valueOf(toDate));
            statement.setDate(1, Date.valueOf(fromDate));
            statement.setDate(2, Date.valueOf(toDate));

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                answer.add(new FormatOfAnswerTwoColumns<String, Long>(
                        resultSet.getString("day"),
                        (Double.valueOf(resultSet.getString("loss")).longValue())
                ));
            }
            statementUpdate.executeUpdate();
            connection.commit();
        });
        return new Pair<>(headers, answer);
    }


    //8. Вывести города, в которых несколько аэропортов.
    void AddTicket(String flightId, String numberSeat, String condition, String id,
                   String bookingDate, String ticketNumber, String name, String boardingNumber) throws Exception {
        String sql = SQL("query8.sql");
        String updateSql = SQL("update8.sql");
        String insertSqlTicket = SQL("insertTicket8.sql");
        String insertSqlBooard = SQL("insertBookings.sql");
        String insertSql = SQL("insert8.sql");

        if (flightId == null || numberSeat == null || condition == null || id == null ||
                bookingDate == null || ticketNumber == null || name == null || boardingNumber == null) {
            throw new IllegalArgumentException("null argument");
        }

        source.connection(connection -> {
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(sql);

            PreparedStatement statementUpdate = connection.prepareStatement(updateSql);

            PreparedStatement statementInsertTickets = connection.prepareStatement(insertSqlTicket);
            PreparedStatement statementInsertBoard = connection.prepareStatement(insertSqlBooard);
            PreparedStatement statementInsert = connection.prepareStatement(insertSql);

            statementUpdate.setDate(1, Date.valueOf(bookingDate));

            statement.setString(1, flightId);
            statement.setString(2, numberSeat);
            statement.setString(3, condition);

            statementInsertTickets.setString(1, ticketNumber);
            statementInsertTickets.setString(2, "1");
            statementInsertTickets.setString(3, id);
            statementInsertTickets.setString(4, name);
            statementInsertTickets.setDate(5, Date.valueOf(bookingDate));

            statementInsertBoard.setString(1, ticketNumber);
            statementInsertBoard.setString(2, flightId);
            statementInsertBoard.setString(3, boardingNumber);
            statementInsertBoard.setString(4, numberSeat);

            statementInsert.setString(1, ticketNumber);
            statementInsert.setString(2, flightId);
            statementInsert.setString(3, condition);
            statementInsert.setString(4, "1");

            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw  new IllegalArgumentException("Неверные данные");
            }

            statementUpdate.executeUpdate();
            statementInsertTickets.executeUpdate();
            statementInsertBoard.executeUpdate();
            statementInsert.executeUpdate();
            connection.commit();
        });
    }


}
