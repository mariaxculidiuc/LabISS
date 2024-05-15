package com.example.proiectiss.repository.db;

import com.example.proiectiss.domain.Comanda;
import com.example.proiectiss.domain.Medicament;
import com.example.proiectiss.domain.StatusComanda;
import com.example.proiectiss.domain.Utilizator;
import com.example.proiectiss.repository.IComandaRepository;
import com.example.proiectiss.repository.IUtilizatorRepository;
import com.example.proiectiss.utils.JdbcUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

public class ComandaDBRepository implements IComandaRepository {

    private JdbcUtils jdbcUtils;

    private IUtilizatorRepository utilizatorRepository;

    public ComandaDBRepository(Properties properties, IUtilizatorRepository utilizatorRepository) {
        jdbcUtils = new JdbcUtils(properties);
        this.utilizatorRepository = utilizatorRepository;
    }

    @Override
    public void save(Comanda elem) {
        try(Connection connection = jdbcUtils.getConnection();
            PreparedStatement insertStmt = connection
                    .prepareStatement("insert into comenzi(cantitate, status, utilizatorId) values (?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            insertStmt.setInt(1, elem.getQuantity());
            insertStmt.setString(2, elem.getStatus().toString());
            insertStmt.setInt(3, elem.getUser().getID());
            insertStmt.executeUpdate();

            try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    elem.setID(generatedKeys.getInt(1));
                }
                else {
                    throw new SQLException("Creating Comanda failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer ID) {
        try(Connection connection = jdbcUtils.getConnection();
            PreparedStatement deleteStmt = connection
                    .prepareStatement("delete from comenzi where id=?")) {
            deleteStmt.setInt(1, ID);
            deleteStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Comanda elem) {
        try(Connection connection = jdbcUtils.getConnection();
            PreparedStatement updateStmt = connection.prepareStatement("update comenzi set status=? where id=?")) {
            updateStmt.setString(1, elem.getStatus().toString());
            updateStmt.setInt(2, elem.getID());
            updateStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Comanda find(Integer ID) {
        try(Connection connection = jdbcUtils.getConnection();
            PreparedStatement findStmt = connection
                    .prepareStatement("select * from comenzi where id=?")) {
            findStmt.setInt(1, ID);
            ResultSet resultSet = findStmt.executeQuery();
            if(resultSet.next() == false)
                return  null;
            Integer id = resultSet.getInt("id");
            Integer quantity = resultSet.getInt("cantitate");
            StatusComanda status = StatusComanda.valueOf( resultSet.getString("status") );
            Integer userid = resultSet.getInt("utilizatorId");
            Utilizator utilizator = utilizatorRepository.find(userid);
            Comanda comanda = new Comanda(id, quantity, utilizator, status);
            return comanda;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Collection<Comanda> findAll() {
        Collection<Comanda> orders = new ArrayList<>();
        try(Connection connection = jdbcUtils.getConnection();
            PreparedStatement findStmt = connection
                    .prepareStatement("select * from comenzi")) {
            ResultSet resultSet = findStmt.executeQuery();
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                Integer quantity = resultSet.getInt("cantitate");
                StatusComanda status = StatusComanda.valueOf(resultSet.getString("status"));
                Integer userid = resultSet.getInt("utilizatorId");
                Utilizator user = utilizatorRepository.find(userid);
                Comanda order = new Comanda(id, quantity, user, status);
                orders.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(orders.size());
        return orders;
    }
}