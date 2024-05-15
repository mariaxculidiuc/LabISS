package com.example.proiectiss.controller;

import com.example.proiectiss.domain.*;
import com.example.proiectiss.service.Service;
import com.example.proiectiss.utils.Event;
import com.example.proiectiss.utils.MedEvent;
import com.example.proiectiss.utils.MessageAlert;
import com.example.proiectiss.utils.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;

public class SectieController implements Observer<Event> {

    Stage mainStage;
    Utilizator rootUser;
    Service service;

    ObservableList<Medicament> modelAllMedicamente = FXCollections.observableArrayList();
    ObservableList<MedicamentComandat> modelMedicamenteComandate = FXCollections.observableArrayList();

    @FXML
    TableView<Medicament> tableViewMedicamente;
    @FXML
    TableColumn<Medicament, String> tableColumnNume;
    @FXML
    TableColumn<Medicament, Float> tableColumnPret;
    @FXML
    TableColumn<Medicament, String> tableColumnDescriere;
    @FXML
    TextField cantitateText;

    @FXML
    TableView<MedicamentComandat> tableMedicamenteComandate;
    @FXML
    TableColumn<MedicamentComandat, String> tableColumnMedNume;
    @FXML
    TableColumn<MedicamentComandat, Integer> tableColumnMedCant;



    public void setServices(Stage primaryStage, Service service, Utilizator rootUser){
        this.mainStage = primaryStage;
        this.service = service;
        this.rootUser = rootUser;
        service.addObserver(this);
        initModelAllMedicamente();
    }

    @FXML
    void initialize(){
        tableColumnNume.setCellValueFactory(new PropertyValueFactory<Medicament, String >("name"));
        tableColumnPret.setCellValueFactory(new PropertyValueFactory<Medicament, Float>("price"));
        tableColumnDescriere.setCellValueFactory(new PropertyValueFactory<Medicament, String>("description"));
        tableViewMedicamente.setItems(modelAllMedicamente);

        tableColumnMedNume.setCellValueFactory(new PropertyValueFactory<MedicamentComandat, String>("drugName"));
        tableColumnMedCant.setCellValueFactory(new PropertyValueFactory<MedicamentComandat, Integer>("quantity"));
        tableMedicamenteComandate.setItems(modelMedicamenteComandate);
    }

    private void initModelAllMedicamente(){
        service.findAllMedicamente().stream().forEach(System.out::println);
        modelAllMedicamente.setAll(service.findAllMedicamente());
    }

    @Override
    public void update(Event event) {
        if(event instanceof MedEvent)
            initModelAllMedicamente();
    }

    @FXML
    private void handleAddMedicamentLaComanda(){
        Medicament drug = tableViewMedicamente.getSelectionModel().getSelectedItem();
        if(drug == null)
            return;
        try{
            Integer quantity = Integer.valueOf(cantitateText.getText());
            MedicamentComandat orderItem = new MedicamentComandat(new Pair<>(drug.getID(), null), drug.getName(), quantity, drug, null);
            modelMedicamenteComandate.add(orderItem);
        }catch (RuntimeException ex){
            MessageAlert.showErrorMessage(mainStage, ex.getMessage());
        }
    }

    @FXML
    private void handleAddComanda(){
        Integer totalQuantity = 0;
        for(MedicamentComandat orderItem: modelMedicamenteComandate) {
            totalQuantity += orderItem.getQuantity();
        }
        try{
            Comanda order = new Comanda(totalQuantity, rootUser, StatusComanda.PENDING);
            service.addComanda(order, modelMedicamenteComandate);
            MessageAlert.showMessage(mainStage, Alert.AlertType.INFORMATION, "Add Order", "The order was added successfully !");
        }catch (RuntimeException ex){
            MessageAlert.showErrorMessage(mainStage, ex.getMessage());
        }
    }

    @FXML
    private void handleRemoveMedicamentComandat(){
        MedicamentComandat orderItem = tableMedicamenteComandate.getSelectionModel().getSelectedItem();
        if(orderItem == null)
            return;
        modelMedicamenteComandate.remove(orderItem);
    }

    @FXML
    private void handleComenzi() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SectieController.class.getResource("/com/example/proiectiss/comandasectie-view.fxml"));
        Parent root = fxmlLoader.load();
        Scene newScene = new Scene(root);
        Scene previousScene = mainStage.getScene();
        mainStage.setScene(newScene);
        ComandaSectieController orderSectionController = fxmlLoader.getController();
        orderSectionController.setServices(mainStage, service, this.rootUser, previousScene);
        mainStage.show();
    }

    @FXML
    private void handleLogout(){
        try{
            service.logout(rootUser);
            mainStage.close();
        }catch (RuntimeException ex){
            MessageAlert.showErrorMessage(mainStage, ex.getMessage());
        }
    }

}
