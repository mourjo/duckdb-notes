package me.mourjo;

import me.mourjo.db.MySQLData;
import me.mourjo.db.PGData;

public class Main {

    public static void main(String[] args) {
        var users = new UserGenerator(2000).generate();
        var orders = new OrderGenerator().generate(users, 10000);
        PGData.createTable();
        PGData.insertData(users);
        MySQLData.createTable();
        MySQLData.insertOrders(orders);
    }
}
