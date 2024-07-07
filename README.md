# duckdb-notes
This is a companion project that show cases some of [DuckDb's](http://duckdb.org) capbilities.

## Reading multiple databases
To showcase reading data from multiple DBs, first start MySQL and PostgreSQL using the docker compose command
```bash
docker compose up
```
This starts up
- MySQL on port 3306
- PostgreSQL on port 5432

Then generate data using the Java project (takes around a minute):
```bash
cd datagenerator
mvn compile exec:java -Dexec.mainClass="me.mourjo.Main"
```

Now use DuckDB to query across the two databases inside a `duckdb` shell:
```sql
ATTACH 'host=localhost port=3306 database=flock user=swan password=mallard' 
AS mysql_db (TYPE mysql_scanner, READ_ONLY);

ATTACH 'host=localhost port=5432 dbname=flock user=swan password=mallard' 
AS pg_db (TYPE postgres_scanner, READ_ONLY);

select tier,
    count(o.id) AS num_orders,
    sum(o.delivery_charge)/sum(total_amount) AS delivery_charge_rate
    from pg_db.users u join mysql_db.orders o
    on u.id = o.created_by
    group by tier order by 1;
┌─────────┬────────────┬──────────────────────┐
│  tier   │ num_orders │ delivery_charge_rate │
│ varchar │   int64    │        double        │
├─────────┼────────────┼──────────────────────┤
│ club    │       1291 │  0.15176342595439124 │
│ normal  │       6126 │  0.14463984959467316 │
│ plus    │       2350 │   0.1463608355957877 │
│ vip     │       1342 │  0.05073097112889566 │
└─────────┴────────────┴──────────────────────┘
```
