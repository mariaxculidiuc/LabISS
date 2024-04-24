package com.example.proiectiss.utils;

import com.example.proiectiss.domain.Medicament;

public class MedEvent implements Event{
    private MedType type;
    private Medicament data,oldData;

    public MedEvent(MedType type, Medicament data){
        this.type = type;
        this.data = data;
    }

    public MedEvent(MedType type, Medicament data, Medicament oldData){
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public MedType getType() {
        return type;
    }

    public Medicament getData() {
        return data;
    }

    public Medicament getOldData() {
        return oldData;
    }
}