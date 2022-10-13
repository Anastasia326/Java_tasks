with cancelled_flights as (
    select flight_id, scheduled_departure
    from flights
    where (departure_airport in (select airport_code
                                 from airports
                                 where airports.city like '%Moscow%')
        or arrival_airport IN (select airport_code
                               from airports
                               where airports.city like '%Moscow%'))
      and status not like 'Cancelled' and status not like 'Arrived' and status not like 'Departed'
      and scheduled_departure >= ?
      and scheduled_arrival <= ?
)
select to_char(scheduled_departure, 'yyyy-mm-dd') as day, sum(ticket_flights.amount) as loss
from ticket_flights
join cancelled_flights on ticket_flights.flight_id = cancelled_flights.flight_id
group by day
order by day;




