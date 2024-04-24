package com.example.proiectiss.repository.db;

import com.example.proiectiss.domain.Medicament;
import com.example.proiectiss.repository.IMedicamentRepository;
import com.example.proiectiss.utils.JdbcUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MedicamentDBRepository implements IMedicamentRepository {

    private JdbcUtils jdbcUtils;

    public MedicamentDBRepository(Properties properties) {
        jdbcUtils = new JdbcUtils(properties);
    }

    @Override
    public void save(Medicament elem) {
        try(Connection connection = jdbcUtils.getConnection();
            PreparedStatement insertStmt = connection
                    .prepareStatement("insert into medicamente(nume,pret,descriere) values (?,?,?)")) {
            insertStmt.setString(1, elem.getName());
            insertStmt.setFloat(2, elem.getPrice());
            insertStmt.setString(3, elem.getDescription());
            insertStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer ID) {
        try(Connection connection = jdbcUtils.getConnection();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("delete from medicamente where id=?")) {
            preparedStatement.setInt(1, ID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Medicament elem) {
        try(Connection connection = jdbcUtils.getConnection();
            PreparedStatement updateStmt = connection.prepareStatement("update medicamente set nume=?, pret=?, descriere=? where id=?")) {
            updateStmt.setString(1, elem.getName());
            updateStmt.setFloat(2, elem.getPrice());
            updateStmt.setString(3, elem.getDescription());
            updateStmt.setInt(4, elem.getID());
            updateStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Medicament find(Integer ID) {
        try(Connection connection = jdbcUtils.getConnection();
            PreparedStatement findStmt = connection
                    .prepareStatement("select * from medicamente where id=?")) {
            findStmt.setInt(1, ID);
            ResultSet resultSet = findStmt.executeQuery();
            if(resultSet.next() == false)
                return null;
            Integer id = resultSet.getInt("id");
            String name = resultSet.getString("nume");
            Float price = resultSet.getFloat("pret");
            String description = resultSet.getString("descriere");
            Medicament drug = new Medicament(id, name, price, description);
            return drug;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Collection<Medicament> findAll() {
        Collection<Medicament> drugs = new ArrayList<>();
        try(Connection connection = jdbcUtils.getConnection();
            PreparedStatement findStmt = connection
                    .prepareStatement("select * from medicamente")) {
            ResultSet resultSet = findStmt.executeQuery();
            while(resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String name = resultSet.getString("nume");
                Float price = resultSet.getFloat("pret");
                String description = resultSet.getString("descriere");
                Medicament drug = new Medicament(id, name, price, description);
                drugs.add(drug);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  drugs;
    }
}