/*with cnt as (
    select count(airport_code) as cnt_air, city
    from airports
    group by city
)
select airports.city as city, group_concat(airport_code) as airports_in_city, cnt_air
from airports
inner join cnt on airports.city = cnt.city
group by airports.city
 */
with cnt as (
    select count(airport_code) as cnt_air, city
    from airports
    group by city
)
select city, cnt_air as airports_in_city
from cnt
where cnt_air > 1




