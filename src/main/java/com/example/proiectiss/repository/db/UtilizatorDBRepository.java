package com.example.proiectiss.repository.db;

import com.example.proiectiss.domain.TipUtilizator;
import com.example.proiectiss.domain.Utilizator;
import com.example.proiectiss.domain.ValidationException;
import com.example.proiectiss.repository.IUtilizatorRepository;
import com.example.proiectiss.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

public class UtilizatorDBRepository implements IUtilizatorRepository {

    private JdbcUtils jdbcUtils;

    public UtilizatorDBRepository(Properties properties) {
        jdbcUtils = new JdbcUtils(properties);
    }

    @Override
    public void save(Utilizator elem) {
        try(Connection connection = jdbcUtils.getConnection();
            PreparedStatement insertStmt = connection
                    .prepareStatement("insert into utilizatori(username, password, tip) values (?,?,?)")) {
            insertStmt.setString(1, elem.getUsername());
            insertStmt.setString(2, elem.getPassword());
            insertStmt.setString(3, elem.getTipUtilizator().toString());
            insertStmt.executeUpdate();
        } catch (SQLException e) {
            throw new ValidationException("The username is already used!");
        }
    }

    @Override
    public void delete(Integer ID) {
        try(Connection connection = jdbcUtils.getConnection();
            PreparedStatement deleteStmt = connection
                    .prepareStatement("delete from utilizatori where id = ?")) {
            deleteStmt.setInt(1, ID);
            deleteStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Utilizator elem) {
        try(Connection connection = jdbcUtils.getConnection();
            PreparedStatement updateStmt = connection
                    .prepareStatement("update utilizatori set username=?, password=?, tip=?" +
                            "where id=?")) {
            updateStmt.setString(1, elem.getUsername());
            updateStmt.setString(2, elem.getPassword());
            updateStmt.setString(3, elem.getTipUtilizator().toString());
            updateStmt.setInt(4, elem.getID());
            updateStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Utilizator find(Integer ID) {
        try(Connection connection = jdbcUtils.getConnection();
            PreparedStatement findStmt = connection
                    .prepareStatement("select * from utilizatori where id=?")) {
            findStmt.setInt(1, ID);
            ResultSet resultSet = findStmt.executeQuery();
            if(resultSet.next() == false)
                return null;
            Integer id = resultSet.getInt("id");
            String username = resultSet.getString("username");
            String password = resultSet.getString("password");
            TipUtilizator type = TipUtilizator.valueOf(resultSet.getString("tip"));
            Utilizator user = new Utilizator(id, username, password, type);
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Collection<Utilizator> findAll() {
        Collection<Utilizator> users = new ArrayList<>();
        try(Connection connection = jdbcUtils.getConnection();
            PreparedStatement stmt = connection.prepareStatement("select * from utilizatori")) {
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()){
                Integer id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                TipUtilizator type = TipUtilizator.valueOf(resultSet.getString("tip"));
                Utilizator user = new Utilizator(id, username, password, type);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public Utilizator filterByUsernameAndPassword(Utilizator user) {
        try(Connection connection = jdbcUtils.getConnection();
            PreparedStatement filterStmt = connection
                    .prepareStatement("select * from utilizatori where username = ? and password = ? and " +
                            "tip = ?")) {
            filterStmt.setString(1, user.getUsername());
            filterStmt.setString(2, user.getPassword());
            filterStmt.setString(3, user.getTipUtilizator().toString());
            ResultSet resultSet = filterStmt.executeQuery();
            if(resultSet.next() == false)
                return null;
            Integer id = resultSet.getInt("id");
            user.setID(id);
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}