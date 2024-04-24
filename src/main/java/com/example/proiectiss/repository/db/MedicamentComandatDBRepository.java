package com.example.proiectiss.repository.db;

import com.example.proiectiss.domain.MedicamentComandat;
import com.example.proiectiss.repository.IMedicamentComandatRepository;
import com.example.proiectiss.utils.JdbcUtils;
import javafx.util.Pair;

import java.util.Collection;
import java.util.Properties;

public class MedicamentComandatDBRepository implements IMedicamentComandatRepository {

    private JdbcUtils jdbcUtils;

    public MedicamentComandatDBRepository(Properties properties) {
        jdbcUtils = new JdbcUtils(properties);
    }

    @Override
    public void save(MedicamentComandat elem) {

    }

    @Override
    public void delete(Pair<Integer,Integer> ID) {

    }

    @Override
    public void update(MedicamentComandat elem) {

    }

    @Override
    public MedicamentComandat find(Pair<Integer,Integer> ID) {
        return null;
    }

    @Override
    public Collection<MedicamentComandat> findAll() {
        return null;
    }
}