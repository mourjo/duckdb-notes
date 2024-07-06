package me.mourjo.entities;

import java.time.ZonedDateTime;

public record Order(String id, String source, String destination, String createdBy,
                    double totalAmount, double deliveryCharge, double taxAmount, String currency,
                    ZonedDateTime createdAt, ZonedDateTime deliveredAt, ZonedDateTime updatedAt) {

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        return id.equals(((Order) o).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
