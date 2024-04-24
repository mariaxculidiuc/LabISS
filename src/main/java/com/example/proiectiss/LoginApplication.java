package com.example.proiectiss;

import com.example.proiectiss.controller.LoginController;
import com.example.proiectiss.repository.IComandaRepository;
import com.example.proiectiss.repository.IMedicamentComandatRepository;
import com.example.proiectiss.repository.IMedicamentRepository;
import com.example.proiectiss.repository.IUtilizatorRepository;
import com.example.proiectiss.repository.db.ComandaDBRepository;
import com.example.proiectiss.repository.db.MedicamentComandatDBRepository;
import com.example.proiectiss.repository.db.MedicamentDBRepository;
import com.example.proiectiss.repository.db.UtilizatorDBRepository;
import com.example.proiectiss.service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class LoginApplication extends Application {
    Service service;
    @Override
    public void start(Stage stage) throws IOException {
        Properties props=new Properties();
        try {
            props.load(new FileReader("C:\\Users\\culid\\OneDrive\\Desktop\\ProiectISS\\bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }
        IUtilizatorRepository userRepository = new UtilizatorDBRepository(props);
        IMedicamentRepository medicamentRepository = new MedicamentDBRepository(props);
        IComandaRepository comandaRepository = new ComandaDBRepository(props);
        IMedicamentComandatRepository medicamentComandatRepository = new MedicamentComandatDBRepository(props);
        service = new Service(userRepository,medicamentRepository,comandaRepository,medicamentComandatRepository);
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        LoginController controller = fxmlLoader.getController();
        controller.setServices(stage,service);
        controller.init(service);
        stage.setTitle("Login to app");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}