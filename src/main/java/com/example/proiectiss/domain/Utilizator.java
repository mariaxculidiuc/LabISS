package com.example.proiectiss.domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Utilizator extends Entity<Integer> implements Serializable {

    private String username;
    private String password;
    private TipUtilizator tipUtilizator;

    public Utilizator() {}

    public Utilizator(Integer ID,String username, String password, TipUtilizator tipUtilizator) {
        this.ID = ID;
        this.username = username;
        this.password = password;
        this.tipUtilizator = tipUtilizator;
    }

    public Utilizator(String username, String password, TipUtilizator tipUtilizator) {
        this.username = username;
        this.password = password;
        this.tipUtilizator = tipUtilizator;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public TipUtilizator getTipUtilizator() {
        return tipUtilizator;
    }

    public void setTipUtilizator(TipUtilizator tipUtilizator) {
        this.tipUtilizator = tipUtilizator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utilizator that = (Utilizator) o;
        return Objects.equals(username, that.username) && Objects.equals(password, that.password) && tipUtilizator == that.tipUtilizator;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, tipUtilizator);
    }

    @Override
    public String toString() {
        return "Utilizator{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", tipUtilizator=" + tipUtilizator +
                '}';
    }
}