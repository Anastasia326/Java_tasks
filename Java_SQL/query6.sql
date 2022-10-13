delete from flights
where flights.aircraft_code like ?;
delete from ticket_flights
where ticket_flights.flight_id not in (select flight_id
    from flights);
delete from tickets
where tickets.ticket_no not in (select ticket_no
from ticket_flights);