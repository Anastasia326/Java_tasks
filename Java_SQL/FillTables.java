import java.io.IOException;
import java.sql.SQLException;

import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class FillTables {
    // делаем из строки json файл
    private String MakeJSON(String first, String second) {
        String first_new = first.substring(1) + ",";
        String second_new = second.substring(0, second.length() - 1).replaceAll("\"\"", "\\\"");
        return (first_new + second_new);
    }

    // заполняем таблицу
    void fillAircrafts(SQLWorker source) throws IOException, SQLException {
        DaoAircrafts aircraft = new DaoAircrafts(source);
        String dataUrl = "https://storage.yandexcloud.net/airtrans-small/aircrafts.csv";
        URL url = new URL(dataUrl);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
        String nextString = "";
        ArrayList<Aircrafts> aircrafts = new ArrayList<>();
        // считываем до пустой строки
        while ((nextString = bufferedReader.readLine()) != null) {
            String[] read_data = nextString.split(",");
            aircrafts.add(new Aircrafts(read_data[0],
                    MakeJSON(read_data[1], read_data[2]),
                    Integer.valueOf(read_data[3])));
        }
        aircraft.SaveData(aircrafts);
    }

    void fillAirports(SQLWorker source) throws IOException, SQLException {
        DaoAirports airport = new DaoAirports(source);
        String dataUrl = "https://storage.yandexcloud.net/airtrans-small/airports.csv";
        URL url = new URL(dataUrl);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
        String nextString = "";
        ArrayList<Airports> airports = new ArrayList<>();
        while ((nextString = bufferedReader.readLine()) != null) {
            String[] read_data = nextString.split(",");
            airports.add(new Airports(read_data[0], MakeJSON(read_data[1], read_data[2]),
                    MakeJSON(read_data[3], read_data[4]),
                    read_data[5], read_data[6]));
        }
        airport.SaveData(airports);
    }

    void fillBoardingPases(SQLWorker source) throws IOException, SQLException {
        DaoBoardingPases passDao = new DaoBoardingPases(source);
        String urlStr = "https://storage.yandexcloud.net/airtrans-small/boarding_passes.csv";
        URL url = new URL(urlStr);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
        String nextString = "";
        ArrayList<BoardingPases> passes = new ArrayList<>();
        while ((nextString = bufferedReader.readLine()) != null) {
            String[] read_data = nextString.split(",");
            passes.add(new BoardingPases(read_data[0], Integer.valueOf(read_data[1]),
                    Integer.valueOf(read_data[2]), read_data[3]));
        }
        passDao.SaveData(passes);
    }

    void fillBookings(SQLWorker source) throws IOException, SQLException {
        DaoBookings passDao = new DaoBookings(source);
        String urlStr = "https://storage.yandexcloud.net/airtrans-small/bookings.csv";
        URL url = new URL(urlStr);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
        String nextString = "";
        ArrayList<Bookings> bookings = new ArrayList<>();
        while ((nextString = bufferedReader.readLine()) != null) {
            String[] read_data = nextString.split(",");
            Timestamp t = Timestamp.valueOf(read_data[1].split("\\+")[0]);
            bookings.add(new Bookings(read_data[0], t, new BigDecimal(read_data[2])));
        }
        passDao.SaveData(bookings);
    }

    void fillFlights(SQLWorker source) throws IOException, SQLException {
        DaoFlights passDao = new DaoFlights(source);
        String urlStr = "https://storage.yandexcloud.net/airtrans-small/flights.csv";
        URL url = new URL(urlStr);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
        String nextString = "";
        ArrayList<Flights> flights = new ArrayList<>();
        while ((nextString = bufferedReader.readLine()) != null) {
            String[] read_data = nextString.split(",");
            Timestamp ts1 = (read_data.length > 8) ? Timestamp.valueOf(read_data[8].split("\\+")[0]) : null;
            Timestamp ts2 = (read_data.length > 9) ? Timestamp.valueOf(read_data[9].split("\\+")[0]) : null;

            flights.add(new Flights(Long.parseLong(read_data[0]), read_data[1],
                    Timestamp.valueOf(read_data[2].split("\\+")[0]),
                    Timestamp.valueOf(read_data[3].split("\\+")[0]),
                    read_data[4], read_data[5], read_data[6], read_data[7], ts1, ts2));
        }
        passDao.SaveData(flights);
    }

    void fillSeats(SQLWorker source) throws IOException, SQLException {
        DaoSeats seatsDao = new DaoSeats(source);
        String urlStr = "https://storage.yandexcloud.net/airtrans-small/seats.csv";
        URL url = new URL(urlStr);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
        String nextString = "";
        ArrayList<Seats> seats = new ArrayList<>();
        while ((nextString = bufferedReader.readLine()) != null) {
            String[] read_data = nextString.split(",");
            seats.add(new Seats(read_data[0], read_data[1], read_data[2]));
        }
        seatsDao.SaveData(seats);
    }

    void fillTicketFlights(SQLWorker source) throws IOException, SQLException {
        DaoTicketFlights seatsDao = new DaoTicketFlights(source);
        String urlStr = "https://storage.yandexcloud.net/airtrans-small/ticket_flights.csv";
        URL url = new URL(urlStr);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
        String nextString = "";
        ArrayList<TicketFlights> ticketFlights = new ArrayList<>();
        while ((nextString = bufferedReader.readLine()) != null) {
            String[] read_data = nextString.split(",");
            ticketFlights.add(new TicketFlights(read_data[0], Integer.valueOf(read_data[1]),
                    read_data[2], new BigDecimal(read_data[3])));
        }
        seatsDao.SaveData(ticketFlights);
    }

    void fillTickets(SQLWorker source) throws IOException, SQLException {
        DaoTickets seatsDao = new DaoTickets(source);
        String urlStr = "https://storage.yandexcloud.net/airtrans-small/tickets.csv";
        URL url = new URL(urlStr);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
        String nextString = "";
        ArrayList<Tickets> tickets = new ArrayList<>();
        while ((nextString = bufferedReader.readLine()) != null) {
            String[] read_data = nextString.split(",");
            String extra = "";
            for (int i = 4; i < read_data.length - 1; i++) {
                extra += read_data[i];
            }
            String data = (read_data.length > 4) ? extra : null;
            tickets.add(new Tickets(read_data[0], read_data[1], read_data[2], read_data[3], data));
        }
        seatsDao.SaveData(tickets);
    }
}
