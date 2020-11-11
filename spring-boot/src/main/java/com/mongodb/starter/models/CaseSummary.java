package com.mongodb.starter.models;


import java.time.LocalDate;

public class CaseSummary {
    public int toplamVefat;
    public int toplamVaka;
    public int toplamTaburcu;
    public String city;
    public String date;

    public CaseSummary(String city,LocalDate date) {
        this.toplamVefat = 0;
        this.toplamVaka = 0;
        this.toplamTaburcu = 0;
        this.city = city;
        this.date = date.toString();
    }
}
