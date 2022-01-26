package LANchat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class signupController {

    @FXML
    private PasswordField confirmPassword;

    @FXML
    private TextField email;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private PasswordField password;

    @FXML
    private TextField userName;

    @FXML
    void loginClicked(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("Login.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("LAN Chat");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void signupClicked(ActionEvent event) {
        DBconnection conNow = new DBconnection();
        Connection conDB = conNow.getConnection();

        String qury = "INSERT INTO `Users` ( `username`, `firstname`, `lastname`, `email`, `password`, `active`) " +
                "VALUES ('" + userName.getText() + "', '" + firstName.getText() + "', '" + lastName.getText() +
                "', '" + email.getText() + "', '" + password.getText() + "', '0')";

        try {

            Statement stat = conDB.createStatement();
            stat.executeUpdate(qury);

            conDB.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
