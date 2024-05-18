# Notes

## Ran a postgres data base server 
```
docker run -v /home/yobro/work/pgdata:/var/lib/postgresql/data -e POSTGRES_PASSWORD=yo -e POSTGRES_USER=yo -e POSTGRES_DB=yo -p 15432:5432 postgres:latest
```
