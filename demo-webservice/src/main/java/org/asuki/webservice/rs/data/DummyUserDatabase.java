package org.asuki.webservice.rs.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.asuki.webservice.rs.model.User;

public class DummyUserDatabase {
    private static Map<Integer, User> users = new HashMap<>();

    static {
        // @formatter:off
        User user1 = User.builder()
                .id(1)
                .firstName("Andy")
                .lastName("L")
                .uri("/users/1")
                .lastModified(new Date())
            .build();
        User user2 = User.builder()
                .id(2)
                .firstName("Tom")
                .lastName("R")
                .uri("/users/2")
                .lastModified(new Date())
            .build();
        // @formatter:on

        users.put(user1.getId(), user1);
        users.put(user2.getId(), user2);
    }

    public static User getUserById(Integer id) {
        return users.get(id);
    }

    public static User updateUser(Integer id) {
        User user = users.get(id);
        user.setLastModified(new Date());
        return user;
    }

    public static String getUserRole(String username, String password) {
        return "andy".equals(username) ? "ADMIN" : "GUEST";
    }

}
