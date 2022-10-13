select dayname(scheduled_departure) as day, count(*) as flights
from flights
where departure_airport in (select airport_code
                          from airports
                          where city like '%Moscow%')
group by day
order by flights desc;