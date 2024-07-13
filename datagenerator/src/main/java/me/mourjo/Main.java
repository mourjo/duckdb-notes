package me.mourjo;

import me.mourjo.db.MySQLData;
import me.mourjo.db.PGData;
import me.mourjo.utils.AdjustedTransactionGenerator;
import me.mourjo.utils.data.OrderGenerator;
import me.mourjo.utils.data.UserGenerator;

public class Main {

    public static void main(String[] args) {
        var users = new UserGenerator(5000).generate();
        var orders = new OrderGenerator().generate(users, 20000);
        PGData.createTable();
        PGData.insertData(users);
        MySQLData.createTable();
        MySQLData.insertOrders(orders);
        AdjustedTransactionGenerator.generateCSV("adjusted_transactions.csv", users, orders,
            100000);
    }
}
