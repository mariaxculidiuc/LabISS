package com.example.proiectiss.domain;

import java.io.Serializable;
import java.util.Objects;

public class Medicament extends Entity<Integer> {

    private String nume;
    private Float pret;
    private String descriere;

    public Medicament(){}

    public Medicament(Integer ID,String name, Float price, String description) {
        this.ID = ID;
        this.nume = name;
        this.pret = price;
        this.descriere = description;
    }

    public Medicament(String name, Float price, String description) {
        this.nume = name;
        this.pret = price;
        this.descriere = description;
    }

    public String getName() {
        return nume;
    }

    public void setName(String name) {
        this.nume = name;
    }

    public Float getPrice() {
        return pret;
    }

    public void setPrice(Float price) {
        this.pret = price;
    }

    public String getDescription() {
        return descriere;
    }

    public void setDescription(String description) {
        this.descriere = description;
    }


    @Override
    public String toString() {
        return "Drug{" +
                "id " + ID + '\'' +
                "name='" + nume + '\'' +
                ", price=" + pret +
                ", description='" + descriere + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Medicament that = (Medicament) o;
        return Objects.equals(nume, that.nume) && Objects.equals(pret, that.pret) && Objects.equals(descriere, that.descriere);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nume, pret, descriere);
    }
}
