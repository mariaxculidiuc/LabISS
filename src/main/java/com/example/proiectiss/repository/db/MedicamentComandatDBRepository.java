package com.example.proiectiss.repository.db;

import com.example.proiectiss.domain.Comanda;
import com.example.proiectiss.domain.Medicament;
import com.example.proiectiss.domain.MedicamentComandat;
import com.example.proiectiss.repository.IComandaRepository;
import com.example.proiectiss.repository.IMedicamentComandatRepository;
import com.example.proiectiss.repository.IMedicamentRepository;
import com.example.proiectiss.repository.IUtilizatorRepository;
import com.example.proiectiss.utils.JdbcUtils;
import javafx.util.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

public class MedicamentComandatDBRepository implements IMedicamentComandatRepository {

    private JdbcUtils jdbcUtils;
    private IMedicamentRepository medicamentRepository;
    private IComandaRepository comandaRepository;

    public MedicamentComandatDBRepository(Properties properties,IMedicamentRepository medicamentRepository,IComandaRepository comandaRepository) {
        jdbcUtils = new JdbcUtils(properties);
        this.medicamentRepository = medicamentRepository;
        this.comandaRepository = comandaRepository;
    }

    @Override
    public void save(MedicamentComandat elem) {
        try(Connection connection = jdbcUtils.getConnection();
            PreparedStatement insertStmt = connection
                    .prepareStatement("insert into medicamenteComandate(medicamentId, comandaId,cantitate,numeMedicament) values (?, ?, ?, ?)")) {
            insertStmt.setString(4, elem.getDrugName());
            insertStmt.setInt(3, elem.getQuantity());
            insertStmt.setInt(2, elem.getOrder().getID());
            insertStmt.setInt(1, elem.getDrug().getID());
            insertStmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error occurred to save method MedicamentComandat: " + e.getMessage());
        }
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
        Collection<MedicamentComandat> orderItems = new ArrayList<>();
        try(Connection connection = jdbcUtils.getConnection();
            PreparedStatement findAllStmt = connection
                    .prepareStatement("select * from medicamenteComandate")) {
            ResultSet resultSet = findAllStmt.executeQuery();
            while (resultSet.next()) {
                String drugName = resultSet.getString("numeMedicament");
                Integer quantity = resultSet.getInt("cantitate");
                Integer orderID = resultSet.getInt("comandaId");
                Integer drugID = resultSet.getInt("medicamentId");
                Medicament medicament = medicamentRepository.find(drugID);
                Comanda comanda = comandaRepository.find(orderID);
                orderItems.add(new MedicamentComandat(drugName, quantity,medicament,comanda));
            }
        } catch (SQLException e) {
            System.err.println("Error occurred to findAll method OrderItems: " + e.getMessage());
        }
        return orderItems;
    }

}