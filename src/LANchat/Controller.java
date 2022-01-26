package LANchat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller implements Initializable{

    @FXML
    private Label currentUser;

    @FXML
    private Label fileNameLabel;

    @FXML
    private Label recieverText1;

    @FXML
    private Label recieverText2;

    @FXML
    private Label recieverText3;

    @FXML
    private Label recieverText4;

    @FXML
    private TextField searchField;

    @FXML
    private Label senderText1;

    @FXML
    private Label senderText2;

    @FXML
    private Label senderText3;

    @FXML
    private Label senderText4;

    @FXML
    private TableView<UsersList> tableView;

    @FXML
    private TableColumn<UsersList, String> userTable;

    @FXML
    private TextArea writeMessage;

    Alert alrt = new Alert(Alert.AlertType.NONE);

    final FileChooser fc = new FileChooser();

    public static String uname;
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date date = new Date();

    ObservableList<UsersList> obList = FXCollections.observableArrayList();

    public void setCurrentUser(String name) {
        this.currentUser.setText(name);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            DBconnection conNow = new DBconnection();
            Connection conDB = conNow.getConnection();

            String qury = "select username from Users where username != '" + uname + "'";

            ResultSet rs = conDB.createStatement().executeQuery(qury);

            while (rs.next()) {
                obList.add(new UsersList(rs.getString("username")));
            }

        } catch (Exception e) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }

        userTable.setCellValueFactory(new PropertyValueFactory<UsersList, String>("username"));

        tableView.setItems(obList);

        currentUser.setText(uname);

    }

    @FXML
    void attachClicked(ActionEvent event) {
        Stage stage = new Stage();
        stage.setTitle("Select a File to attach");
        File file = fc.showOpenDialog(stage);
        if (file != null) {
            fileNameLabel.setText(file.getAbsolutePath());
        }
    }

    @FXML
    void logOutClicked(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("Login.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("LAN Chat");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void refreshClicked(ActionEvent event) {
        tableView.getItems().clear();
        try {
            DBconnection conNow = new DBconnection();
            Connection conDB = conNow.getConnection();

            String qury = "select username from Users where username != '" + uname + "'";

            ResultSet rs = conDB.createStatement().executeQuery(qury);

            while (rs.next()) {
                obList.add(new UsersList(rs.getString("username")));
            }

        } catch (Exception e) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }

        userTable.setCellValueFactory(new PropertyValueFactory<UsersList, String>("username"));
        tableView.setItems(obList);

    }

    @FXML
    void searchClicked(ActionEvent event) {
        tableView.getItems().clear();
        try {
            DBconnection conNow = new DBconnection();
            Connection conDB = conNow.getConnection();

            String qury = "select username from Users where username != '" + uname + "' and username like '%" + searchField.getText().toString() + "%'";

            ResultSet rs = conDB.createStatement().executeQuery(qury);

            while (rs.next()) {
                obList.add(new UsersList(rs.getString("username")));
            }
        } catch (Exception e) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }

        userTable.setCellValueFactory(new PropertyValueFactory<UsersList, String>("username"));
        tableView.setItems(obList);
    }

    @FXML
    void sendClicked(ActionEvent event) throws SQLException {
        if (senderText1.getText() != "") {
            if (senderText2.getText() != "") {
                if (senderText3.getText() != "") {
                    senderText4.setText(senderText3.getText());
                    senderText3.setText("");
                    recieverText4.setText("");
                } else if (recieverText3.getText() != "") {
                    recieverText4.setText(recieverText3.getText());
                    recieverText3.setText("");
                }
                senderText3.setText(senderText2.getText());
                senderText2.setText("");
            } else if (recieverText2.getText() != "") {
                recieverText3.setText(recieverText2.getText());
                recieverText2.setText("");
            }
            senderText2.setText(senderText1.getText());
            senderText1.setText("");
        } else if (recieverText1.getText() != "") {
            if (recieverText2.getText() != "") {
                if (recieverText3.getText() != "") {
                    recieverText4.setText(recieverText3.getText());
                    recieverText3.setText("");
                    senderText4.setText("");
                } else if (senderText4.getText() != "") {
                    senderText4.setText(senderText3.getText());
                    senderText3.setText("");
                }
                recieverText3.setText(recieverText2.getText());
                recieverText2.setText("");
            } else if (senderText2.getText() != "") {
                senderText3.setText(senderText2.getText());
                senderText2.setText("");
            }
            recieverText2.setText(recieverText1.getText());
            recieverText1.setText("");
        }

        senderText1.setText(writeMessage.getText());


        try {
            DBconnection conNow = new DBconnection();
            Connection conDB = conNow.getConnection();
            Statement stat = conDB.createStatement();
            String tableName = "message_" + uname;

            String qury = "INSERT INTO " + tableName + " (reciever, message, timeSent, status)" +
                    "VALUES ('" + tableView.getSelectionModel().getSelectedItem().toString() + "', '"
                    + writeMessage.getText() + "', '" + date.getTime() + "', 'Sent')";

            stat.executeUpdate(qury);

        } catch (SQLSyntaxErrorException ex) {
            DBconnection conNow = new DBconnection();
            Connection conDB = conNow.getConnection();
            Statement stat = conDB.createStatement();
            String tableName = "message_" + uname;

            String qury = "create table " + tableName + " (reciever varchar(100), message TEXT(64000), timeSent varchar(32), status varchar(16))";

            stat.executeUpdate(qury);

            qury = "INSERT INTO " + tableName + " (reciever, message, timeSent, status)" +
                    "VALUES ('" + tableView.getSelectionModel().getSelectedItem().toString() + "', '"
                    + writeMessage.getText() + "', '" + date.getTime() + "', 'Sent')";

            stat.executeUpdate(qury);
        }
        catch (Exception e) {
            UsersList row = tableView.getSelectionModel().getSelectedItem();
            System.out.println(row.getUsername());
            System.out.println(formatter.format(date));
//            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }

        writeMessage.setText("");
    }

    @FXML
    void settingsClicked(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("Setting.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("LAN Chat");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
