package com.example.proiectiss.controller;

import com.example.proiectiss.domain.*;
import com.example.proiectiss.service.Service;
import com.example.proiectiss.utils.ComandaEvent;
import com.example.proiectiss.utils.Event;
import com.example.proiectiss.utils.MedEvent;
import com.example.proiectiss.utils.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.Collection;
import java.util.stream.Collectors;


public class ComandaSectieController implements Observer<Event> {

    Stage mainStage;
    Scene previousScene;
    Utilizator rootUser;
    Service services;
    ObservableList<ComboBox<MedicamentComandatDTO>> modelComenzi = FXCollections.observableArrayList();
    ObservableList<ComboBox<MedicamentComandatDTO>> modelComenziFacute = FXCollections.observableArrayList();

    @FXML
    ListView<ComboBox<MedicamentComandatDTO>> listViewComenzi;

    @FXML
    ListView<ComboBox<MedicamentComandatDTO>> listViewComenziFacute;

    public void setServices(Stage primaryStage, Service service, Utilizator rootUser, Scene previousScene){
        this.mainStage = primaryStage;
        this.services = service;
        this.rootUser = rootUser;
        this.previousScene = previousScene;
        service.addObserver(this);
        initModelAllComenzi();
    }

    @FXML
    void initialize(){
        listViewComenzi.setItems(modelComenzi);
        listViewComenziFacute.setItems(modelComenziFacute);
    }

    @Override
    public void update(Event event) {
        if(event instanceof MedEvent || event instanceof ComandaEvent)
            initModelAllComenzi();
    }

    private void initModelAllComenzi(){
        modelComenzi.clear();
        Collection<Pair<Comanda, Collection<MedicamentComandat>>> orderItems = services.getAllComenziPeSectie(rootUser);
        orderItems.forEach(orderCollectionPair -> {
            Comanda order = orderCollectionPair.getKey();
            Collection<MedicamentComandatDTO> orderItemCollection = orderCollectionPair.getValue().stream()
                    .map(orderItem -> {
                        String name = orderItem.getDrugName();
                        Integer price = orderItem.getQuantity();
                        StatusComanda status = orderItem.getOrder().getStatus();
                        return new MedicamentComandatDTO(name, price, status);
                    }).collect(Collectors.toList());
            ObservableList<MedicamentComandatDTO> modelOrderItems = FXCollections.observableArrayList();
            ComboBox<MedicamentComandatDTO> comboBoxSpecificOrder = new ComboBox<>();
            comboBoxSpecificOrder.setPromptText("Status: " + order.getStatus() + " Total Quantity: " + order.getQuantity());
            comboBoxSpecificOrder.setItems(modelOrderItems);
            modelOrderItems.setAll(orderItemCollection);
            modelComenzi.add(comboBoxSpecificOrder);
        });

    }

    private void filterComenziByStatus(StatusComanda statusReceived){
        modelComenziFacute.clear();
        Collection<Pair<Comanda, Collection<MedicamentComandat>>> orderItemsOrders = services.filterComandabyStatus(statusReceived);
        orderItemsOrders.forEach(orderItemsOrder -> {
            Comanda order = orderItemsOrder.getKey();
            Collection<MedicamentComandat> orderItems = orderItemsOrder.getValue();
            Collection<MedicamentComandatDTO> orderItemDTOS = orderItems.stream()
                    .map(orderItem -> {
                        String name = orderItem.getDrugName();
                        Integer price = orderItem.getQuantity();
                        StatusComanda status = orderItem.getOrder().getStatus();
                        return new MedicamentComandatDTO(name, price, status);
                    }).collect(Collectors.toList());

            ObservableList<MedicamentComandatDTO> modelOrderItems = FXCollections.observableArrayList();
            ComboBox<MedicamentComandatDTO> comboBox = new ComboBox<MedicamentComandatDTO>();
            comboBox.setPromptText("Status: " + order.getStatus() + " Total Quantity: " + order.getQuantity());
            comboBox.setItems(modelOrderItems);
            modelOrderItems.setAll(orderItemDTOS);
            modelComenziFacute.add(comboBox);
        });
    }

    @FXML
    private void filterComenziRefuzate(){
        filterComenziByStatus(StatusComanda.REFUZATA);
    }

    @FXML
    private void filterPendingOrders(){
        filterComenziByStatus(StatusComanda.PENDING);
    }

    @FXML
    private void filterComenziOnorate(){
        filterComenziByStatus(StatusComanda.ONORATA);
    }

    @FXML
    void handleQuit(){
        mainStage.setScene(previousScene);
    }
}