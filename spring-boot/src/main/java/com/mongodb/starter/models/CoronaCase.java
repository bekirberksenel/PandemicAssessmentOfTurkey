package com.mongodb.starter.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@JsonInclude(Include.NON_NULL)
public class CoronaCase {

    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private int vakaSayisi;
    private int vefatSayisi;
    private int taburcuSayisi;
    private String city;
    private LocalDate date;



    @Override
    public int hashCode() {
        return Objects.hash(id, city, taburcuSayisi, vefatSayisi, vakaSayisi);
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public int getVakaSayisi() {
        return vakaSayisi;
    }

    public void setVakaSayisi(int vakaSayisi) {
        this.vakaSayisi = vakaSayisi;
    }

    public int getVefatSayisi() {
        return vefatSayisi;
    }

    public void setVefatSayisi(int vefatSayisi) {
        this.vefatSayisi = vefatSayisi;
    }

    public int getTaburcuSayisi() {
        return taburcuSayisi;
    }

    public void setTaburcuSayisi(int taburcuSayisi) {
        this.taburcuSayisi = taburcuSayisi;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}