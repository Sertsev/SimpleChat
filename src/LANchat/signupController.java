package LANchat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;

import static LANchat.Controller.uname;

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

    Alert alrt = new Alert(Alert.AlertType.INFORMATION);

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

        if (!userName.getText().isEmpty() &&
                !firstName.getText().isEmpty() &&
                !lastName.getText().isEmpty() &&
                !email.getText().isEmpty() &&
                !password.getText().isEmpty()) {

            // check email validation
            if (email.getText().matches("^(.+)@(.+)\\.(.+)$")) {

                // check password strength
                if (password.getText().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$")) {
                    try {
                        String qury = "INSERT INTO `Users` ( `username`, `firstname`, `lastname`, `email`, `password`, `active`) " +
                                "VALUES ('" + userName.getText() + "', '" + firstName.getText() + "', '" + lastName.getText() +
                                "', '" + email.getText() + "', '" + password.getText() + "', '0')";

                        Statement stat = conDB.createStatement();
                        stat.executeUpdate(qury);

                        String tableName = "message_" + userName.getText();

                        qury = "create table " + tableName + " " +
                                "(withUser varchar(100), message TEXT(64000), timeSent varchar(32), status varchar(16))";

                        stat.executeUpdate(qury);
                        conDB.close();
                        alrt.setContentText("Your have registered Successfully \n");
                        alrt.show();
                        loginClicked(event);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    alrt.setContentText("Your password strength doesn't match the standard! " +
                            "Your new password must: \n" +
                            "  1. include numbers,\n" +
                            "  2. contain capital & small letters, and\n" +
                            "  3. should be longer than 8 characters. \n");
                    alrt.show();
                }
            } else {
                alrt.setContentText("Your email is not a valid email! " +
                        "Please enter a valid email! \n");
                alrt.show();
            }
        } else {
            alrt.setContentText("Please fill all the fields! \n");
            alrt.show();
        }
    }
}
