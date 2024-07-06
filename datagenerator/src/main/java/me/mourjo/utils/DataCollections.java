package me.mourjo.utils;

import java.util.List;

public class DataCollections {

    public static final List<String> cities = List.of(
        "Delhi",
        "Mumbai",
        "Kolkata",
        "Bangalore",
        "Tokyo",
        "Boston",
        "Amsterdam",
        "London",
        "Paris"
    );


    public static final List<String> streets = List.of(
        "Boulevard Avenue",
        "Southern Avenue",
        "MG Road",
        "Lower East Street",
        "Docklane",
        "Broadway Av",
        "Second Street"
    );


    public static final List<String> currencies = List.of(
        "INR",
        "EUR",
        "CHF",
        "USD"
    );


//    curl -s https://raw.githubusercontent.com/hadley/data-baby-names/master/baby-names-by-state.csv | duckdb -c "
//    COPY (
//        select name from read_csv('/dev/stdin', ignore_errors=true, columns = {'state':'VARCHAR', year:'varchar', 'name': 'VARCHAR', 'number': 'int128', 'sex':'VARCHAR',})
//    group by name
//    order by sum(number) desc
//    limit 10
//   ) to '/dev/stdout' (FORMAT CSV)
//  "
    public static final List<String> personNames = List.of(
        "Michael",
        "David",
        "James",
        "John",
        "Christopher",
        "Robert",
        "Matthew",
        "Jennifer",
        "William",
        "Daniel"
    );


    public static final List<String> userTiers = List.of(
        "vip",
        "club",
        "plus",
        "normal"
    );


//    curl -s https://raw.githubusercontent.com/IgorMinar/foodme/master/server/data/restaurants.csv | duckdb -c " COPY (
//    select \"Restaurant name\" from read_csv('/dev/stdin')
//        ) TO '/dev/stdout' (FORMAT CSV)
//        "
    public static List<String> restaurants = List.of(
        "Esther's German Saloon",
        "Aminia",
        "Super Wonton Express",
        "Naan Sequitur",
        "KFC",
        "Curry Galore",
        "Tropical Jeeve's Cafe",
        "Zardoz Cafe",
        "Mainland China",
        "Luigi's House of Pies",
        "Thick and Thin"
    );
}
