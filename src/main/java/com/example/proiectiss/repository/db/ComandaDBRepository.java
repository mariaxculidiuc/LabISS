package com.example.proiectiss.repository.db;

import com.example.proiectiss.domain.Comanda;
import com.example.proiectiss.domain.StatusComanda;
import com.example.proiectiss.repository.IComandaRepository;
import com.example.proiectiss.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

public class ComandaDBRepository implements IComandaRepository {

    private JdbcUtils jdbcUtils;

    public ComandaDBRepository(Properties properties) {
        jdbcUtils = new JdbcUtils(properties);
    }

    @Override
    public void save(Comanda elem) {
        try(Connection connection = jdbcUtils.getConnection();
            PreparedStatement insertStmt = connection
                    .prepareStatement("insert into comenzi(cantitate, status, utilizatorId) values (?,?,?)")) {
            insertStmt.setInt(1, elem.getQuantity());
            insertStmt.setString(2, elem.getStatus().toString());
            insertStmt.executeUpdate();
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
            PreparedStatement updateStmt = connection
                    .prepareStatement("update from comenzi set cantitate=?, status=?, utilizatorId=?" +
                            "where id=?")) {
            updateStmt.setInt(1, elem.getQuantity());
            updateStmt.setString(2, elem.getStatus().toString());
            updateStmt.setInt(4, elem.getID());
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
                    .prepareStatement("select * from comenzi ")) {
            ResultSet resultSet = findStmt.executeQuery();
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                Integer quantity = resultSet.getInt("cantitate");
                StatusComanda status = StatusComanda.valueOf(resultSet.getString("status"));
                Integer userid = resultSet.getInt("utilizatorId");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
}