package com.example.proiectiss.utils;

import com.example.proiectiss.domain.Comanda;

public class ComandaEvent implements Event{
    private ComandaEventType type;
    private Comanda data, oldData;

    public ComandaEvent(ComandaEventType type, Comanda data){
        this.type = type;
        this.data = data;
    }

    public ComandaEvent(ComandaEventType type, Comanda data, Comanda oldData){
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ComandaEventType getType() {
        return type;
    }

    public Comanda getData() {
        return data;
    }

    public Comanda getOldData() {
        return oldData;
    }
}