
## different not equal
select * from employees where City != 'Chittagong';
select * from employees where not City = 'Chittagong';

## null value check
select * from employees where age is null;
select * from employees where age is not null;

# TRUNCATE - delete all data inside a table
TRUNCATE TABLE tableName;

#DISTINCT
select distinct City from employees;
select distinct(City) from employees;

# COUNT
select count(*) as total_employee from employees;
select count(City) from employees;
select count(distinct City) from employees;
select count(*) from employees where age = 28;
select count(*) from employees where salary > 50000;
select count(employees.ID),employees.City from employees GROUP BY employees.City; #List the number of employees in each City.

#GROUP BY
#List the number of employees in each City and order by city with the most customers First.
select count(employees.ID),employees.City from employees GROUP BY employees.City ORDER BY count(employees.ID) desc;
select employees.City from employees GROUP BY employees.City;
select source, al, sum(amount) as amount from report where source = 'mtbl' and al = 'brac';

#BETWEEN
select * from employees where salary between 40000 and 80000;

#NOT BETWEEN
select * from employees where salary not between 40000 and 80000;

#Limit
select * from employees limit 5; #show me 5 from start
select * from employees limit 5,3; #starts from 5th position and show me total 3

# IN
select * from employees where City in ('Chittagong', 'Dhaka'); #both show same results
select * from employees where City='Chittagong' or City='Dhaka'; #both show same results

# NOT IN
select * from employees where City NOT in ('Chittagong', 'Dhaka'); #both show same results
select * from employees where City!='Chittagong' and City!='Dhaka'; #both show same results

#AS (for creating custom column), + operator
select FirstName,age+(100-age) as Age from employees;

#CONCAT
select CONCAT(FirstName,' - ',LastName) as fullName, City from employees;

#SUBSTRING
select FirstName,substring(LastName,1,2) from employees;

update employees set LastName=substring(LastName, 1, 3);

#UPPER, LOWER
select upper(FirstName) as FirstName, lower(LastName) as LastNamee from employees;

#AVG, SUM
select avg(salary) as AvgOfSalary, sum(salary) as SumOfSalary from employees;

select source, al, sum(amount) as amount from report where source = 'mtbl' and al = 'brac';


#SQRT- square root
select FirstName,sqrt(salary) as Sqrt_Of_Salary from employees;

#subquery
select * from employees where salary > (select avg(salary) from employees) order by salary desc;

#add new column
alter table employees add salary int not null after LastName;

#CONCAT, Subquery, AS, MIN, MAX
select concat(LastName,',',salary,' - ',"He is an Intern") as Min_salary,
       concat((select FirstName from employees where salary = (select max(salary) from employees)),',',(select salary from employees where salary = (select max(salary) from employees))) as Max_salary
from employees where salary = (select min(salary) from employees);

select concat(LastName,',',salary,' - ',"He is an Intern") as Min_salary from employees where salary = (select min(salary) from employees);

#bad code but another possible way
select concat((select FirstName from employees where salary = (select max(salary) from employees)),',',(select salary from employees where salary = (select max(salary) from employees)))
           as Max_salary from employees where salary = (select max(salary) from employees);

#LIKE
# here capital and small letter doesn't matter.
select LastName from employees where LastName like 's%'; #employees whose LastName starts with 's'
select LastName from employees where LastName not like 's%'; #employees whose LastName doesn't start with 's'

select LastName from employees where LastName like '%n'; #employees whose LastName ends with 'n'
select LastName from employees where LastName like 'S%n'; #starts with s and ends with n
select LastName from employees where LastName like 's_%_%_%'; #starts with s and has at least 3 characters

select FirstName from employees where FirstName like '_a%' or FirstName like '_o%'; #employees whose LastName's 2nd character is 'a' or 'o'

select FirstName from employees where FirstName like 'a%' or FirstName like 's%' or FirstName like 'm%'; # (SAME) employees whose FirstName starts with 'a' or 's' or 'm'
select FirstName from employees where FirstName rlike '^[asm]'; # (SAME) employees whose FirstName starts with 'a' or 's' or 'm'
select FirstName from employees where left(FirstName,1) in ('a', 's', 'm'); # (SAME) employees whose FirstName starts with 'a' or 's' or 'm'


select LastName from employees where LastName like '%a_'; #employees whose LastName's 2nd character from ending is 'a'

select FirstName from employees where left(FirstName,1) in ('s','t','y'); #LEFT(FirstName,1) is the 1st letter of the FirstName)
select FirstName from employees where left(FirstName,2) in ('sa','ta','yo'); #LEFT(FirstName,2) is the 1st 2 letter of the FirstName)

select * from employees where FirstName like '[^smt]%'; #select all where First letter of FirstName is NOT 's' or 'm' or 't' and THIS WON'T WORK ON MySQL
select * from employees where FirstName like '[a-n]%'; #select all where First letter of FirstName starts with anything from 'a' to 'n' and THIS WON'T WORK ON MySQL
select * from employees where FirstName like '[smt]%'; #select all where First letter of FirstName is 's' or 'm' or 't' and THIS WON'T WORK ON MySQL

## length()
select FirstName, length(FirstName) as Length from employees order by Length, FirstName asc limit 1;
select FirstName, length(FirstName) as Length from employees order by Length desc limit 1;

## Right() - MySQL RIGHT() extracts a specified number of characters from the right side of a string.
SELECT RIGHT(LastName,3) from employees where FirstName='Muhammad';
select FirstName from employees where age > 25 order by right(FirstName, 3);

#Join
select employees.ID, employees.FirstName, orders.order_name, orders.Amount
    from employees,orders
    where employees.ID = orders.employee_id
    order by employees.ID;

#after shorting the above table with "AS" keyword
select em.ID, em.FirstName, ord.order_name, ord.amount
    from employees as em, orders as ord
    where em.ID = ord.employee_id
    order by em.ID;

##### INNER JOIN
select em.ID, em.FirstName, ord.order_name, ord.Amount
    from employees as em inner join orders as ord
    on em.ID = ord.employee_id
    #where ord.order_name = 'laptop'
    order by em.ID;

#### LEFT JOIN
select em.*, ord.order_name, ord.Amount
    from employees as em left outer join orders as ord
    on em.ID = ord.employee_id;

#### RIGHT JOIN
select em.ID, em.FirstName, ord.order_name, ord.Amount
    from employees as em right outer join orders as ord
    on em.ID = ord.employee_id;


## UNION
select em.FirstName, em.LastName, em.salary from employees as em
UNION
select saj.FirstName, saj.LastName, saj.salary from shajguj as saj;

##### VIEW - after creating, it will update automatically when the main table changed.
create view senior_dev as
    select FirstName,salary from employees where employees.salary >= 40000;

create or replace view senior_dev as
    select FirstName,salary,City from employees where salary >= 40000;


################## W3schools

