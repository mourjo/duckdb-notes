package me.mourjo.utils;

import static me.mourjo.utils.DataCollections.devices;
import static me.mourjo.utils.DataCollections.transactionFailureReasons;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import me.mourjo.entities.Order;
import me.mourjo.entities.User;

public class TransactionAdjustmentCsvGenerator {

    private static final Random r = new Random();

    // user id, city, user segment, order_id, transaction_id, failure_reason, timestamp, device_type
    public static void generateCSV(String filename, List<User> users, Set<Order> orders, int n) {
        var orderList = new ArrayList<>(orders);
        Collections.shuffle(orderList);

        int orderCount = 0;
        Map<String, List<Order>> userOrderMapping = new HashMap<>();
        for (User user : users) {
            userOrderMapping.putIfAbsent(user.id(), new ArrayList<>());
        }

        for (var order : orders) {
            userOrderMapping.putIfAbsent(order.createdBy(), new ArrayList<>());
            userOrderMapping.get(order.createdBy()).add(order);
        }

        for (var userOrder : userOrderMapping.entrySet()) {
            Collections.shuffle(userOrder.getValue());
        }

        try (FileWriter f = new FileWriter(filename);
            BufferedWriter br = new BufferedWriter(f);
            PrintWriter csv = new PrintWriter(br)) {
            csv.println(
                "user_id, city, user_tier, order_id, transaction_id, reason, timestamp, device_type");
            while (n-- > 0 && orderCount < orders.size()) {
                var user = users.get(randExponentialInt(100, users.size()));
                var userOrders = userOrderMapping.get(user.id());
                if (userOrders.isEmpty()) {
                    continue;
                }
                var order = userOrders.get(r.nextInt(userOrders.size()));

                var transactionId = UUID.randomUUID();
                var minutesOffset = (r.nextBoolean() ? -1 : 1) * r.nextInt(1000);
                var timestamp = order.createdAt()
                    .toLocalDateTime()
                    .atZone(ZoneOffset.UTC)
                    .plusMinutes(minutesOffset)
                    .plusNanos(r.nextInt(10000))
                    .format(DateTimeFormatter.ISO_DATE_TIME);
                var device = devices.get(r.nextInt(devices.size()));
                var reason = transactionFailureReasons.get(randExponentialInt(
                    0.5,
                    transactionFailureReasons.size())
                );

                csv.println("%s,%s,%s,%s,%s,%s,%s,%s".formatted(
                    user.id(),
                    user.city(),
                    user.tier(),
                    order.id(),
                    transactionId,
                    reason,
                    timestamp,
                    device)
                );
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    private static int randExponentialInt(double scale, int bound) {
        return ((int) (-scale * Math.log(r.nextDouble()))) % bound;
    }
}
