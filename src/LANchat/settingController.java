package LANchat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class settingController {

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

    @FXML
    void saveClicked(ActionEvent event) {

    }

}
