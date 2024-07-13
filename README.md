# duckdb-notes
This is a companion project that show cases some of [DuckDb's](http://duckdb.org) capbilities.

## Multiple files and databases
This repository reads data from multiple files and DBs:
- MySQL
- Postgres
- CSV file

## Set up MySQL and PostgreSQL using Docker
To showcase reading data from multiple files and DBs, first start MySQL and PostgreSQL using the 
docker compose command
```bash
docker compose up
```
This starts up
- MySQL on port 3306
- PostgreSQL on port 5432

## Generate data
The datagenerator project inserts made-up food delivery data into `MySQL`, `PostgreSQL` and
generates a CSV file `adjusted_transactions.csv` that tries to mimic a report sent by email after
the fact about adjusted transactions.

Kick off the data generation using this (takes around a minute):
```bash
cd datagenerator
mvn compile exec:java -Dexec.mainClass="me.mourjo.Main"
```

## Query the CSV file using SQL
Let's try to find the reasons for transaction adjustments using traditional shell commands:
```bash
awk -F',' \
  'NR > 1 {count[$6]++} END \
  {for (value in count) print value, count[value]}' \
  datagenerator/adjusted_transactions.csv | sort
CUSTOMER_SUPPORT_REFUND 8494
INSUFFICIENT_FUNDS 1232
MANUAL_ADJUSTMENT 162
REVERSED_PAYMENT 62815
```

I argue the above is harder to read when compared to SQL with `duckdb`:

```sql
select reason, count(*) adjustment_reason from 'datagenerator/adjusted_transactions.csv'
       group by 1 order by 2 desc;
┌─────────────────────────┬───────────────────┐
│         reason          │ adjustment_reason │
│         varchar         │       int64       │
├─────────────────────────┼───────────────────┤
│ REVERSED_PAYMENT        │             62815 │
│ CUSTOMER_SUPPORT_REFUND │              8494 │
│ INSUFFICIENT_FUNDS      │              1232 │
│ MANUAL_ADJUSTMENT       │               162 │
└─────────────────────────┴───────────────────┘
```

## Join tables from MySQL with tables from PostgreSQL

Use DuckDB to query across the two databases inside a `duckdb` shell:
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
│ club    │       1173 │  0.14673977784384093 │
│ normal  │       5631 │  0.14944278316837237 │
│ plus    │       2249 │  0.15303252050300867 │
│ vip     │       1206 │  0.04881044937146628 │
└─────────┴────────────┴──────────────────────┘
```

## Join multiple tables from multiple databases with static files 

Take the CSV file sent to us by email to join across production data in different databases inside a `duckdb` shell:

```sql
select reason,
       user_id,
       tier,
       total_amount,
       order_id,
       tax_amount,
       delivery_charge,
       currency,
       adj.timestamp
from pg_db.users u join mysql_db.orders o on u.id = o.created_by 
     join 'datagenerator/adjusted_transactions.csv' adj on  adj.user_id = u.id
order by o.id, adj.timestamp limit 10;

┌──────────────────┬────────────┬─────────┬───────────────┬───────────┬───────────────┬─────────────────┬──────────┬────────────────────────────┐
│      reason      │  user_id   │  tier   │ total_amount  │ order_id  │  tax_amount   │ delivery_charge │ currency │         timestamp          │
│     varchar      │  varchar   │ varchar │ decimal(10,2) │  varchar  │ decimal(10,2) │  decimal(10,2)  │ varchar  │         timestamp          │
├──────────────────┼────────────┼─────────┼───────────────┼───────────┼───────────────┼─────────────────┼──────────┼────────────────────────────┤
│ REVERSED_PAYMENT │ user_ffwoh │ vip     │       1805.00 │ ord_4p0w0 │        291.86 │          147.54 │ INR      │ 2024-07-09 05:18:26.619605 │
│ REVERSED_PAYMENT │ user_ffwoh │ vip     │       1805.00 │ ord_4p0w0 │        291.86 │          147.54 │ INR      │ 2024-07-09 05:53:26.619602 │
│ REVERSED_PAYMENT │ user_ffwoh │ vip     │       1805.00 │ ord_4p0w0 │        291.86 │          147.54 │ INR      │ 2024-07-09 06:17:26.619601 │
│ REVERSED_PAYMENT │ user_ffwoh │ vip     │       1805.00 │ ord_4p0w0 │        291.86 │          147.54 │ INR      │ 2024-07-09 06:26:26.619601 │
│ REVERSED_PAYMENT │ user_ffwoh │ vip     │       1805.00 │ ord_4p0w0 │        291.86 │          147.54 │ INR      │ 2024-07-09 06:37:26.619606 │
│ REVERSED_PAYMENT │ user_ffwoh │ vip     │       1805.00 │ ord_4p0w0 │        291.86 │          147.54 │ INR      │ 2024-07-09 07:16:26.619606 │
│ REVERSED_PAYMENT │ user_ffwoh │ vip     │       1805.00 │ ord_4p0w0 │        291.86 │          147.54 │ INR      │ 2024-07-09 07:23:26.619606 │
│ REVERSED_PAYMENT │ user_ffwoh │ vip     │       1805.00 │ ord_4p0w0 │        291.86 │          147.54 │ INR      │ 2024-07-09 07:26:26.619607 │
│ REVERSED_PAYMENT │ user_ffwoh │ vip     │       1805.00 │ ord_4p0w0 │        291.86 │          147.54 │ INR      │ 2024-07-09 07:40:26.619609 │
│ REVERSED_PAYMENT │ user_ffwoh │ vip     │       1805.00 │ ord_4p0w0 │        291.86 │          147.54 │ INR      │ 2024-07-09 07:45:26.619602 │
├──────────────────┴────────────┴─────────┴───────────────┴───────────┴───────────────┴─────────────────┴──────────┴────────────────────────────┤
│ 10 rows                                                                                                                             9 columns │
└───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘
```
