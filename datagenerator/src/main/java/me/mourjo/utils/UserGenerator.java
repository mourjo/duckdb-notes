package me.mourjo.utils;

import static me.mourjo.utils.DataCollections.cities;
import static me.mourjo.utils.DataCollections.personNames;
import static me.mourjo.utils.DataCollections.userTiers;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import me.mourjo.entities.User;

public class UserGenerator {

    private final Set<String> userIds;
    private Random r = new Random();

    public UserGenerator(int number) {
        userIds = new HashSet<>();
        while (number-- > 0) {
            String id = RandomStringGenerator.generateRandomString("user", 5);
            userIds.add(id);
        }
    }

    public List<User> generate() {
        var users = new ArrayList<User>();
        for (String id : userIds) {
            String name = personNames.get(r.nextInt(personNames.size()));
            String city = cities.get(r.nextInt(cities.size()));
            String tier = userTiers.get(r.nextInt(userTiers.size()));
            ZonedDateTime created = ZonedDateTime.now().minusHours(r.nextInt(1000) + 10);
            ZonedDateTime updated = created.plusMinutes(10000);
            users.add(new User(id, name, city, tier, created, updated));
        }

        return users;
    }

}
