package com.example.payments_kafka.model;

public class BalanceResponse {

    private String card_id;
    private double total;

    public BalanceResponse(String card_id, double total) {
        this.card_id = card_id;
        this.total = total;
    }

    public String getCard_id() {
        return card_id;
    }

    public double getTotal() {
        return total;
    }
}