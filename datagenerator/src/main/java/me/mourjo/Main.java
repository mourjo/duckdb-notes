package me.mourjo;

import me.mourjo.db.MySQLData;
import me.mourjo.db.PGData;
import me.mourjo.utils.OrderGenerator;
import me.mourjo.utils.TransactionAdjustmentCsvGenerator;
import me.mourjo.utils.UserGenerator;

public class Main {

    public static void main(String[] args) {
        int nUsers = 5_000;
        int nOrders = 20_000;
        int nTransactionAdjustments = 100_000;

        var users = new UserGenerator(nUsers).generate();
        var orders = new OrderGenerator().generate(users, nOrders);

        PGData.createTable();
        PGData.insertData(users);

        MySQLData.createTable();
        MySQLData.insertOrders(orders);

        TransactionAdjustmentCsvGenerator.generateCSV(
            "adjusted_transactions.csv",
            users,
            orders,
            nTransactionAdjustments);

        System.out.printf(
            "Saved %d users, %d orders, %d transaction adjustments%n",
            nUsers,
            nOrders,
            nTransactionAdjustments
        );
    }
}
