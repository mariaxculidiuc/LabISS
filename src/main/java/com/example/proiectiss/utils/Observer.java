package com.example.proiectiss.utils;

public interface Observer<E extends Event> {
    void update(E event);
}