select distinct from_airport.city as from_air, to_airport.city as to_air,
                flights.scheduled_arrival - flights.scheduled_departure as scheduled,
                avg(datediff(mi, flights.scheduled_departure, flights.scheduled_arrival)) as avg_time
from flights
inner join airports as from_airport on flights.departure_airport = from_airport.airport_code
inner join airports as to_airport on flights.arrival_airport = to_airport.airport_code
where (from_airport.city, flights.scheduled_arrival - flights.scheduled_departure)
          in (select distinct airports.city, min(flights.scheduled_arrival - flights.scheduled_departure) as shortest
              from flights
              inner join airports on flights.departure_airport = airports.airport_code
              where flights.status = 'Arrived'
              group by airports.city
              order by shortest)
group by from_airport.city, to_airport.city, flights.scheduled_arrival - flights.scheduled_departure
order by avg_time;
