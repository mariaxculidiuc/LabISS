package com.example.proiectiss.domain;

import java.util.Objects;

public class MedicamentComandatDTO {

    private String name;
    private Integer quantity;
    private StatusComanda status;

    public MedicamentComandatDTO(String name, Integer quantity, StatusComanda status) {
        this.name = name;
        this.quantity = quantity;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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
        MedicamentComandatDTO that = (MedicamentComandatDTO) o;
        return Objects.equals(name, that.name) && Objects.equals(quantity, that.quantity) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, quantity, status);
    }

    @Override
    public String toString() {
        return "MedicamentComandatDTO{" +
                "name='" + name + '\'' +
                ", quantity=" + quantity +
                ", status=" + status +
                '}';
    }
}
