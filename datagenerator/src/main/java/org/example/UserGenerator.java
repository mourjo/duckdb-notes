package org.example;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.example.entities.User;
import org.example.utils.RandomStringGenerator;

public class UserGenerator {

    private final Set<String> userIds;
    private final List<String> cities = List.of(
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

    private final List<String> names = List.of(
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

    private final List<String> tiers = List.of(
        "vip",
        "club",
        "plus",
        "normal"
    );
    Random r = new Random();

    public UserGenerator(int number) {
        userIds = new HashSet<>();
        while (number-- > 0) {
            String id = RandomStringGenerator.generateRandomString("user", 5);
            userIds.add(id);
        }
    }

    List<User> generate() {
        var users = new ArrayList<User>();
        for (String id : userIds) {
            String name = names.get(r.nextInt(names.size()));
            String city = cities.get(r.nextInt(cities.size()));
            String tier = tiers.get(r.nextInt(tiers.size()));
            ZonedDateTime created = ZonedDateTime.now().minusHours(r.nextInt(1000) + 10);
            ZonedDateTime updated = created.plusMinutes(10000);
            users.add(new User(id, name, city, tier, created, updated));
        }

        return users;
    }

}
