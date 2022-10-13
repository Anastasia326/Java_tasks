update flights
set status = 'Cancelled'
where (departure_airport in (select airport_code
                             from airports
                             where airports.city like '%Moscow%')
    or arrival_airport IN (select airport_code
                           from airports
                           where airports.city like '%Moscow%'))
and status not like 'Cancelled' and status not like 'Arrived' and status not like 'Departed'
and scheduled_departure >= ?
and scheduled_arrival <= ?;
