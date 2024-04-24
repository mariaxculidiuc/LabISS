package com.example.proiectiss.controller;

import com.example.proiectiss.domain.Medicament;
import com.example.proiectiss.domain.TipUtilizator;
import com.example.proiectiss.domain.Utilizator;
import com.example.proiectiss.domain.ValidationException;
import com.example.proiectiss.service.Service;
import com.example.proiectiss.utils.Event;
import com.example.proiectiss.utils.MedEvent;
import com.example.proiectiss.utils.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class AdminController implements Observer<Event> {

    Stage mainStage;
    Utilizator rootUser;
    Service services;
    TipUtilizator typeUserSignup;
    @FXML
    TextField signupUsernameText;
    @FXML
    TextField signupPasswordText;
    @FXML
    Button signupBut;
    @FXML
    RadioButton pharmacyRadioSignup;
    @FXML
    RadioButton sectionRadioSignup;
    @FXML
    RadioButton adminRadioSignup;
    ObservableList<Medicament> modelAllDrugs = FXCollections.observableArrayList();

    @FXML
    TableView<Medicament> tableViewDrugs;
    @FXML
    TableColumn<Medicament, String> tableColumnName;
    @FXML
    TableColumn<Medicament, Float> tableColumnPrice;
    @FXML
    TableColumn<Medicament, String> tableColumnDescription;
    @FXML
    TextField nameTextField;
    @FXML
    TextField priceTextField;
    @FXML
    TextField descriptionTextField;

    public void setServices(Stage primaryStage, Service service, Utilizator rootUser){
        this.mainStage = primaryStage;
        this.services = service;
        this.rootUser = rootUser;
        initModelAllDrugs();
        service.addObserver(this);
    }

    @FXML
    void initialize(){

        tableColumnName.setCellValueFactory(new PropertyValueFactory<Medicament, String >("name"));
        tableColumnPrice.setCellValueFactory(new PropertyValueFactory<Medicament, Float>("price"));
        tableColumnDescription.setCellValueFactory(new PropertyValueFactory<Medicament, String>("description"));
        tableViewDrugs.setItems(modelAllDrugs);

        tableViewDrugs.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal != null){
                nameTextField.setText(newVal.getName());
                priceTextField.setText(newVal.getPrice().toString());
                descriptionTextField.setText(newVal.getDescription());
            }
        });
    }

    private void initModelAllDrugs(){
        modelAllDrugs.setAll(services.findAllMedicamente());
    }

    @Override
    public void update(Event event) {
        if(event instanceof MedEvent)
            initModelAllDrugs();
    }

    @FXML
    void handleSignup(){
        String username = signupUsernameText.getText();
        String password = signupPasswordText.getText();
        try{
            Utilizator signupUser = new Utilizator(username, password, typeUserSignup);
            services.signUp(signupUser);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Hospital");
            alert.setHeaderText("Registration");
            alert.setContentText("The user was registered !");
            alert.showAndWait();
            clearSignup();
        }catch (ValidationException ex){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Travel Agency");
            alert.setHeaderText("Authentication failure");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
            clearSignup();
        }
    }

    private void clearSignup(){
        signupUsernameText.clear();
        signupPasswordText.clear();
    }

    @FXML
    void handlePharmacyRadioSignup(){
        typeUserSignup = TipUtilizator.FARMACIE;
    }

    @FXML
    void handleSectionRadioSignup(){
        typeUserSignup = TipUtilizator.SECTIE;
    }

    @FXML
    void handleAdminRadoSignup(){
        typeUserSignup = TipUtilizator.ADMIN;
    }

    @FXML
    void handleAddDrug(){
            String name = nameTextField.getText();
            Float price = Float.valueOf(priceTextField.getText());
            String description = descriptionTextField.getText();
            Medicament addedDrug = new Medicament(name, price, description);
            services.addMedicament(addedDrug);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Add");
            alert.setHeaderText("Medicament");
            alert.setContentText("Medicament adaugat cu succes!");
    }

    @FXML
    void handleUpdateDrug(){
        Medicament updetedDrug = tableViewDrugs.getSelectionModel().getSelectedItem();
        if(updetedDrug == null)
            return;
            String name = nameTextField.getText();
            Float price = Float.valueOf(priceTextField.getText());
            String description = descriptionTextField.getText();
                Medicament updatedDrug = new Medicament(updetedDrug.getID(), name, price, description);
            services.updateMedicament(updatedDrug);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Update");
            alert.setHeaderText("Medicament");
            alert.setContentText("Medicament actualizat cu succes!");
    }

    @FXML
    void handleDeleteDrug(){
        Medicament deletedDrug = tableViewDrugs.getSelectionModel().getSelectedItem();
        if(deletedDrug == null)
            return;
        services.deleteMedicament(deletedDrug);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Delete");
        alert.setHeaderText("Medicament");
        alert.setContentText("Medicament sters cu succes!");
    }


    @FXML
    void handleLogOut() {
        services.logout(rootUser);
        mainStage.close();
    }

}