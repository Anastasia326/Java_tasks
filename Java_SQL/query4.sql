select monthname(scheduled_departure) as month, count(*) as problem_raice
from flights
where status = 'Cancelled'
group by month;