package LANchat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class loginController {

    Alert alrt = new Alert(Alert.AlertType.NONE);

    @FXML
    private PasswordField loginPassword;

    @FXML
    private TextField loginUsername;

    @FXML
    void forgotClicked(ActionEvent event) {

    }

    @FXML
    void loginClicked(ActionEvent event) {
        DBconnection conNow = new DBconnection();
        Connection conDB = conNow.getConnection();

        String qury = "SELECT * from Users where username = '" + loginUsername.getText() + "'";

        try {
            Statement stat = conDB.createStatement();
            ResultSet rs = stat.executeQuery(qury);

            if (rs.next() == false) {
                alrt.setAlertType(Alert.AlertType.ERROR);
                alrt.setContentText("You have entered a wrong username. \n" +
                        "Please enter your username correctly or Sign Up.\n");
            } else {
                if (rs.getString("password").equals(loginPassword.getText())) {
                    Controller obj = new Controller();
                    Controller.uname = loginUsername.getText().toString();
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(getClass().getResource("LANchat.fxml"));
                        Scene scene = new Scene(fxmlLoader.load());
                        Stage stage = new Stage();
                        stage.setTitle("LAN Chat");
                        stage.setScene(scene);
                        stage.setResizable(false);
                        stage.show();
                        ((Node) (event.getSource())).getScene().getWindow().hide();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    alrt.setAlertType(Alert.AlertType.ERROR);
                    alrt.setContentText("You have entered a wrong password. \n" +
                            "Please enter your password correctly \n" +
                            "or click forgot password.\n");
                }
            }
            conDB.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void signupClicked(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("Signup.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("LAN Chat");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
