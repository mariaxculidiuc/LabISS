package com.example.proiectiss.domain;

import java.io.Serializable;
import java.util.Objects;

public class Comanda extends Entity<Integer> implements Serializable {

    private Integer quantity;
    private Utilizator user;
    private StatusComanda status;

    public Comanda() {}

    public Comanda(Integer ID,Integer quantity, Utilizator user, StatusComanda status) {
        this.ID = ID;
        this.quantity = quantity;
        this.user = user;
        this.status = status;
    }

    public Comanda(Integer quantity, Utilizator user, StatusComanda status) {
        this.quantity = quantity;
        this.user = user;
        this.status = status;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Utilizator getUser() {
        return user;
    }

    public void setUser(Utilizator user) {
        this.user = user;
    }

    public StatusComanda getStatus() {
        return status;
    }

    public void setStatus(StatusComanda status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comanda comanda = (Comanda) o;
        return Objects.equals(quantity, comanda.quantity) && Objects.equals(user, comanda.user) && status == comanda.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity, user, status);
    }

    @Override
    public String toString() {
        return "Comanda{" +
                "quantity=" + quantity +
                ", user=" + user +
                ", status=" + status +
                '}';
    }
}
