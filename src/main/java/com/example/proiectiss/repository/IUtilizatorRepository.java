package com.example.proiectiss.repository;

import com.example.proiectiss.domain.Utilizator;

public interface IUtilizatorRepository extends IRepository<Utilizator, Integer> {
    Utilizator filterByUsernameAndPassword(Utilizator user);
}
