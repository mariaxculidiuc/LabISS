package com.example.proiectiss.repository;

import com.example.proiectiss.domain.Entity;

import java.util.Collection;

public interface IRepository< T extends Entity<TID>, TID > {
    void save(T elem);
    void delete(TID ID);
    void update(T elem);
    T find(TID ID);
    Collection<T> findAll();
}
