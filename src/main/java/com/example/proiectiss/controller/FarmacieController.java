package com.example.proiectiss.controller;

import com.example.proiectiss.domain.Comanda;
import com.example.proiectiss.domain.MedicamentComandat;
import com.example.proiectiss.domain.StatusComanda;
import com.example.proiectiss.domain.Utilizator;
import com.example.proiectiss.service.Service;
import com.example.proiectiss.utils.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.Collection;

public class FarmacieController implements Observer<Event> {

    Stage mainStage;
    Utilizator rootUser;
    Service services;

    ObservableList<Comanda> modelAllOrders = FXCollections.observableArrayList();
    ObservableList<MedicamentComandat> modelOrderItems = FXCollections.observableArrayList();

    @FXML
    TableView<Comanda> tableViewComenzi;
    @FXML
    TableColumn<Comanda, Utilizator> tableColumnSectie;
    @FXML
    TableColumn<Comanda, Integer> tableColumnCantitate;
    @FXML
    TableColumn<Comanda, StatusComanda> tableColumnStatus;

    @FXML
    TableView<MedicamentComandat> tableViewMedicamenteComandate;
    @FXML
    TableColumn<MedicamentComandat, String> tableColumnMedNume;
    @FXML
    TableColumn<MedicamentComandat, Integer> tableColumnMedCantitate;


    public void setServices(Stage primaryStage, Service service, Utilizator rootUser){
        this.mainStage = primaryStage;
        this.services = service;
        this.rootUser = rootUser;
        service.addObserver(this);
        updateOrderList();
    }

    @FXML
    void initialize(){
        tableColumnSectie.setCellValueFactory(new PropertyValueFactory<Comanda, Utilizator>("user"));
        tableColumnCantitate.setCellValueFactory(new PropertyValueFactory<Comanda, Integer>("quantity"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<Comanda, StatusComanda>("status"));
        tableViewComenzi.setItems(modelAllOrders);

        tableColumnMedNume.setCellValueFactory(new PropertyValueFactory<MedicamentComandat, String>("drugName"));
        tableColumnMedCantitate.setCellValueFactory(new PropertyValueFactory<MedicamentComandat, Integer>("quantity"));
        tableViewMedicamenteComandate.setItems(modelOrderItems);

        tableViewComenzi.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal != null){
                Collection<MedicamentComandat> orderItems = services.getAllMedicamenteComandatePeComanda(newVal);
                modelOrderItems.setAll(orderItems);
            }
        });

    }

    private void updateOrderList(){
        Collection<Comanda> orders = services.getComandaByStatus(StatusComanda.PENDING);
        modelAllOrders.setAll(orders);
    }

    @Override
    public void update(Event event) {
        if(event instanceof MedEvent || event instanceof ComandaEvent)
            updateOrderList();
    }

    @FXML
    private void handleOnoreazaComanda(){
        Comanda order = tableViewComenzi.getSelectionModel().getSelectedItem();
        if(order == null)
            return;
        try{
            services.onoreazaComanda(order);
            MessageAlert.showMessage(mainStage, Alert.AlertType.INFORMATION, "Honor Order", "The order was honored!");
        }catch (RuntimeException ex){
            MessageAlert.showErrorMessage(mainStage, ex.getMessage());
        }
    }

    @FXML
    private void handleRefuzaComanda(){
        Comanda order = tableViewComenzi.getSelectionModel().getSelectedItem();
        if(order == null)
            return;
        try{
            services.refuzaComanda(order);
            MessageAlert.showMessage(mainStage, Alert.AlertType.INFORMATION, "Refuse Order", "The order was rejected!");
        }catch (RuntimeException ex){
            MessageAlert.showErrorMessage(mainStage, ex.getMessage());
        }
    }

    @FXML
    private void handleLogout(){
        try{
            services.logout(rootUser);
            mainStage.close();
        }catch (RuntimeException ex){
            MessageAlert.showErrorMessage(mainStage, ex.getMessage());
        }
    }
}