package me.mourjo.entities;


import java.time.ZonedDateTime;

public record User(String id, String name, String city, String tier, ZonedDateTime createdAt,
                   ZonedDateTime updatedAt) {

}
