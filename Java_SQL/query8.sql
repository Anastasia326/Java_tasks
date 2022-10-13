select seat_no
from seats
where (aircraft_code in (select aircraft_code
                         from flights
                         where flight_id = ?) and (seat_no = ?) and (fare_conditions = ?))