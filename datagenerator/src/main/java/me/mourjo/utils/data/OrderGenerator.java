package me.mourjo.utils.data;

import static me.mourjo.utils.data.DataCollections.currencies;
import static me.mourjo.utils.data.DataCollections.streets;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import me.mourjo.entities.Order;
import me.mourjo.entities.User;
import me.mourjo.utils.RandomStringGenerator;

public class OrderGenerator {
    private final Random r = new Random();

    private String streetFromCity(String city) {
        return "%d %s, %s".formatted(r.nextInt(1, 20), streets.get(r.nextInt(streets.size())),
            city);
    }

    public Set<Order> generate(List<User> users, int number) {
        Set<Order> orders = new HashSet<>();
        while (number-- > 0) {
            User user = users.get(r.nextInt(users.size()));
            var id = RandomStringGenerator.generateRandomString("ord", 5);
            String source = streetFromCity(user.city());
            String destination = streetFromCity(user.city());
            String createdBy = user.id();
            double totalAmount = r.nextInt(0, 1000) + 1000;
            String currency = currencies.get(r.nextInt(currencies.size()));
            if (!currency.equals("INR")) {
                totalAmount = totalAmount / 100;
            }

            double deliveryCharge = totalAmount * r.nextDouble(0, 0.3);

            if (user.tier().equals("plus") && r.nextInt(5) <= 2) {
                continue;
            }

            if (user.tier().equals("club") && r.nextInt(5) <= 3) {
                continue;
            }

            if (user.tier().equals("vip")) {
                if (r.nextInt(5) != 1) {
                    continue;
                }
                deliveryCharge = totalAmount * r.nextDouble(0, 0.1);
            }

            double tax = totalAmount * r.nextDouble(0, 0.2);
            ZonedDateTime createdAt = ZonedDateTime.now().minusMinutes(r.nextInt(10000));
            ZonedDateTime deliveredAt = createdAt.plusMinutes(r.nextInt(40) + 10);
            ZonedDateTime updatedAt = deliveredAt.plusSeconds(r.nextInt(60));

            orders.add(
                new Order(
                    id, source, destination,
                    createdBy, totalAmount, deliveryCharge, tax,
                    currency, createdAt, deliveredAt, updatedAt
                )
            );

            if (currency.equals("INR") && r.nextBoolean()) {
                orders.add(
                    new Order(
                    RandomStringGenerator.generateRandomString("ord", 5),
                    source, destination, createdBy,
                    totalAmount, deliveryCharge, tax,
                    currency, createdAt, deliveredAt, updatedAt
                    )
                );
            }
        }
        return orders;
    }
}
