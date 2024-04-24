package com.example.proiectiss.domain;

import javafx.util.Pair;

import java.io.Serializable;
import java.util.Objects;

public class MedicamentComandat extends Entity<Pair<Integer,Integer>> implements Serializable {

    private String drugName;
    private Integer quantity;
    private Medicament drug;
    private Comanda order;

    public MedicamentComandat() {}

    public MedicamentComandat(Pair<Integer,Integer> ID,String drugName, Integer quantity, Medicament drug, Comanda order) {
        this.ID = ID;
        this.drugName = drugName;
        this.quantity = quantity;
        this.drug = drug;
        this.order = order;
    }

    public MedicamentComandat(String drugName, Integer quantity, Medicament drug, Comanda order) {
        this.drugName = drugName;
        this.quantity = quantity;
        this.drug = drug;
        this.order = order;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Medicament getDrug() {
        return drug;
    }

    public void setDrug(Medicament drug) {
        this.drug = drug;
    }

    public Comanda getOrder() {
        return order;
    }

    public void setOrder(Comanda order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "ID=" + ID +
                ", drugName='" + drugName + '\'' +
                ", quantity=" + quantity +
                ", drug=" + drug +
                ", order=" + order +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicamentComandat that = (MedicamentComandat) o;
        return Objects.equals(drugName, that.drugName) && Objects.equals(quantity, that.quantity) && Objects.equals(drug, that.drug) && Objects.equals(order, that.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(drugName, quantity, drug, order);
    }
}