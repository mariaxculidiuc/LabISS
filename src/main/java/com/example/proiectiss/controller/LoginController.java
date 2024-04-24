package com.example.proiectiss.controller;

import com.example.proiectiss.domain.TipUtilizator;
import com.example.proiectiss.domain.Utilizator;
import com.example.proiectiss.domain.ValidationException;
import com.example.proiectiss.service.Service;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    Stage mainStage;
    Service services;
    TipUtilizator typeUser;
    TipUtilizator typeUserSignup;
    Utilizator rootUser;

    @FXML
    TextField loginUsernameText;
    @FXML
    TextField loginPasswordText;
    @FXML
    TextField signupUsernameText;
    @FXML
    TextField signupPasswordText;
    @FXML
    TextField signupConfirmPasswordText;
    @FXML
    Button loginBut;
    @FXML
    Button signupBut;
    @FXML
    RadioButton pharmacyRadioLogin;
    @FXML
    RadioButton sectionRadioLogin;
    @FXML
    RadioButton adminRadioLogin;

    public void setServices(Stage primaryStage, Service service){
        this.mainStage = primaryStage;
        this.services = service;
    }

    @FXML
    public void initialize(){

    }
    public void init(Service generalService) {
        this.services = generalService;
    }

    @FXML
    void handleLogin() throws IOException {
        String username = loginUsernameText.getText();
        String password = loginPasswordText.getText();

        if (username.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("Username or password cannot be empty");
        }

        Utilizator rootUser = new Utilizator(username, password, typeUser);
        try{
            this.rootUser = services.login(rootUser);

            if(typeUser.equals(TipUtilizator.SECTIE)) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("sectie-view.fxml"));
                Parent root = loader.load();
                Scene newScene = new Scene(root);
                mainStage.setScene(newScene);
                SectieController sectionController = loader.getController();
                //sectionController.setServices(mainStage, services, this.rootUser);
                mainStage.show();
            }
            else
            if(typeUser.equals(TipUtilizator.FARMACIE)) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("farmacie-view.fxml"));
                Parent root = loader.load();
                Scene newScene = new Scene(root);
                mainStage.setScene(newScene);
                FarmacieController pharmacyController = loader.getController();
                //pharmacyController.setServices(mainStage, services, this.rootUser);
                mainStage.show();
            }
            else
            if(typeUser.equals(TipUtilizator.ADMIN)) {
                FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("/com/example/proiectiss/admin-view.fxml"));
                Parent root = fxmlLoader.load();
                Scene newScene = new Scene(root);
                mainStage.setScene(newScene);
                AdminController adminController = fxmlLoader.getController();
                adminController.setServices(mainStage, services, this.rootUser);
                mainStage.show();
            }
        }
        catch (ValidationException ex){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Travel Agency");
            alert.setHeaderText("Authentication failure");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
            clearLogin();
        }
    }

    @FXML
    void handleSignup(){
        String username = signupUsernameText.getText();
        String password = signupPasswordText.getText();
        String confirmPassword = signupConfirmPasswordText.getText();
        if(!confirmPassword.equals(password)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Travel Agency");
            alert.setHeaderText("Authentication failure");
            alert.setContentText("Password doesn't match !");
            alert.showAndWait();
            signupUsernameText.clear();
            signupPasswordText.clear();
            signupConfirmPasswordText.clear();
            clearSignup();
            return;
        }
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

    private void clearLogin(){
        loginUsernameText.clear();
        loginPasswordText.clear();
    }

    private void clearSignup(){
        signupUsernameText.clear();
        signupPasswordText.clear();
        signupConfirmPasswordText.clear();
    }

    @FXML
    void handlePharmacyRadioLogin(){
        typeUser = TipUtilizator.FARMACIE;
    }

    @FXML
    void handleSectionRadioLogin(){
        typeUser = TipUtilizator.SECTIE;
    }

    @FXML
    void handleAdminRadoLogin(){
        typeUser = TipUtilizator.ADMIN;
    }
}