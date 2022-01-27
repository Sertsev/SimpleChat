package LANchat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class settingController implements Initializable {

    @FXML
    private PasswordField confirmPassword;

    @FXML
    private PasswordField currentPassword;

    @FXML
    private TextField email;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private PasswordField password;

    @FXML
    private Text userName;

    Alert alrt = new Alert(Alert.AlertType.ERROR);

    @FXML
    void saveClicked(ActionEvent event) {
        try {
            DBconnection conNow = new DBconnection();
            Connection conDB = conNow.getConnection();
            Statement stat = conDB.createStatement();
            String qury;

            // checking if first name is not empty to save
            if (!firstName.getText().isEmpty()) {
                qury = "UPDATE Users SET firstname = '" + firstName.getText() + "' where username = '" + Controller.uname + "'";
                stat.executeUpdate(qury);
            }

            // checking if last name is not empty to save
            if (!lastName.getText().isEmpty()) {
                qury = "UPDATE Users SET lastname = '" + lastName.getText() + "' where username = '" + Controller.uname + "'";
                stat.executeUpdate(qury);
            }

            // checking if email is not empty to save
            if (!email.getText().isEmpty()) {
                if (email.getText().matches("^(.+)@(.+)\\.(.+)$")) {
                    qury = "UPDATE Users SET lastname = '" + email.getText() + "' where username = '" + Controller.uname + "'";
                    stat.executeUpdate(qury);
                } else {
                    alrt.setContentText("Your email is not a valid email! " +
                            "Please enter a valid email! \n");
                    alrt.show();
                }
            }

            // checking if password is not empty to save
            if (!currentPassword.getText().isEmpty()) {
                qury = "select password from Users where username = '" + Controller.uname + "'";
                ResultSet rs = stat.executeQuery(qury);

                if (rs.next() == false) {
                    alrt.setAlertType(Alert.AlertType.ERROR);
                    alrt.setContentText("You have entered a wrong username. \n" +
                            "Please enter your username correctly or Sign Up.\n");
                } else
                // checking if the current password is correct
                if (rs.getString("password").equals(currentPassword.getText())) {

                    // checking if first name is not empty to save
                    if (password.getText().equals(confirmPassword.getText())) {
                        if (password.getText().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$")) {
                            qury = "UPDATE Users SET password = '" + password.getText() + "' WHERE username = '" + Controller.uname + "'";
                            stat.executeUpdate(qury);
                        } else {
                            alrt.setContentText("Your password strength doesn't match the standard! " +
                                    "Your new password must: \n" +
                                    "  1. include numbers,\n" +
                                    "  2. contain capital & small letters, and\n" +
                                    "  3. should be longer than 8 characters. \n");
                            alrt.show();
                        }

                    } else {
                        alrt.setContentText("The new password doesn't match!\n");
                        alrt.show();
                    }
                } else {
                    alrt.setContentText("You entered a wrong password!\n");
                    alrt.show();
                }
            }
            conDB.close();
            alrt.setAlertType(Alert.AlertType.CONFIRMATION);
            alrt.setContentText("You have updated your info Successfully. \n\n");
            alrt.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userName.setText(Controller.uname);
    }
}
