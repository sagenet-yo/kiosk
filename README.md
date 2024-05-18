# Notes

## Ran a postgres data base server 
```
docker run -v /home/yobro/work/pgdata:/var/lib/postgresql/data -e POSTGRES_PASSWORD=yo -e POSTGRES_USER=yo -e POSTGRES_DB=yo -p 15432:5432 postgres:latest
```
## Work Log 5/17/2024
- Implemented SQL code when creating and managing tables 
- Downloaded firstnames,lastnames, and addresses data from github
- Read from these files in Java code
- Data from these file were converted into records in coresponding tables
- Created 2 independent tables called Person and Address
- Created a many to many link table between the 2 indepedent tables
-s Ids from person and addrress were connected by the link table