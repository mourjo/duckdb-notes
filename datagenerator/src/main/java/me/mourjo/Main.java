package me.mourjo;

import me.mourjo.db.MySQLData;
import me.mourjo.db.PGData;
import me.mourjo.utils.TransactionCsvGenerator;
import me.mourjo.utils.OrderGenerator;
import me.mourjo.utils.UserGenerator;

public class Main {

    public static void main(String[] args) {
        var users = new UserGenerator(5000).generate();
        var orders = new OrderGenerator().generate(users, 20000);
        PGData.createTable();
        PGData.insertData(users);
        MySQLData.createTable();
        MySQLData.insertOrders(orders);
        TransactionCsvGenerator.generateCSV("adjusted_transactions.csv", users, orders,
            100000);
        System.out.println("All done!");
    }
}
