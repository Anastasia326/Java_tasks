/*select city, count(flight_id) as cnt
from airports
inner join flights on airports.airport_code = flights.departure_airport
group by city;

where(flights.status = 'Cancelled')
group by city
order by cnt desc
limit 5;*/
select city, count(airport_code) as cnt
from (
    select departure_airport
    from flights where status like 'Cancelled') as fda
inner join airports on airports.airport_code = fda.departure_airport
group by city
order by cnt desc;