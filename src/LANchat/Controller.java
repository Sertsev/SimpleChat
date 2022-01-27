package LANchat;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
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
    private Button onlineButton;

    @FXML
    private TextField searchField;

    @FXML
    private ScrollPane messageScreen;

    @FXML
    private VBox messageVbox;

    @FXML
    private TableView<UsersList> tableView;

    @FXML
    private TableColumn<UsersList, String> userTable;

    @FXML
    private TextArea writeMessage;

    FXMLLoader fxmlLoader = new FXMLLoader();

    Alert alrt = new Alert(Alert.AlertType.NONE);

    DBconnection conNow = new DBconnection();

    final FileChooser fc = new FileChooser();

    public static String uname;

    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date date = new Date();

    ObservableList<UsersList> obList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Connection conDB = conNow.getConnection();

            String qury = "select username from Users where username != '" + uname + "' and active = 1";

            ResultSet rs = conDB.createStatement().executeQuery(qury);

            while (rs.next()) {
                obList.add(new UsersList(rs.getString("username")));
            }
//
//            String quryMsg = "select username from Users where username != '" + uname + "'";
//
//            ResultSet rsMsg = conDB.createStatement().executeQuery(quryMsg);

            conDB.close();
        } catch (Exception e) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }

        userTable.setCellValueFactory(new PropertyValueFactory<UsersList, String>("username"));
        tableView.setItems(obList);
        currentUser.setText(uname);
        messageScreen.setFitToWidth(true);

        messageVbox.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                messageScreen.setVvalue((Double) newValue);
            }
        });



        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<UsersList>() {
            @Override
            public void changed(ObservableValue<? extends UsersList> observableValue, UsersList usersList, UsersList t1) {
                Connection conDB = conNow.getConnection();
                UsersList row = tableView.getSelectionModel().getSelectedItem();
                messageVbox.getChildren().clear();
                try {
                    String withUser = row.getUsername();
                    String tableName = "message_" + uname;
                    String qury = "select * from " + tableName + " where withUser = '" + withUser + "' ";

                    ResultSet rs = conDB.createStatement().executeQuery(qury);
                    while (rs.next() != false) {
                        HBox hbox = new HBox();
                        Text txtMsg = new Text(rs.getString("message"));
                        Text txtTime = new Text(rs.getString("timeSent"));
                        TextFlow txtTimFlw = new TextFlow(txtTime);
                        TextFlow txtFlow = new TextFlow(txtMsg);

                        if (rs.getString("status").equals("Received")) {
                            hbox.setAlignment(Pos.CENTER_LEFT);
                            hbox.setPadding(new Insets(5, 5, 5, 10));

                            txtFlow.setStyle("-fx-background-color: rgb(0,10,0); " +
                                    "-fx-background-radius: 15px;");

                            txtFlow.setPadding(new Insets(5, 10, 5, 10));
                            txtMsg.setFill(Color.color(0.934, 0.945, 0.996));

//                            txtTimFlw.setStyle("-fx-background-color: rgb(0,10,0); " +
//                                    "-fx-background-radius: 20px;");

                            txtTimFlw.setPadding(new Insets(2, 4, 2, 4));
                            txtTime.setStyle("-fx-font-size: 8");
                            txtTime.setFill(Color.color(0.934, 0.945, 0.996));

                            hbox.getChildren().add(txtFlow);
                            messageVbox.getChildren().add(hbox);
                            messageVbox.getChildren().add(txtTimFlw);
                        } else if (rs.getString("status").equals("Sent")) {
                            hbox.setAlignment(Pos.CENTER_RIGHT);
                            hbox.setPadding(new Insets(5, 5, 5, 10));;

                            txtFlow.setStyle("-fx-color: rgb(239,242,255); " +
                                    "-fx-background-color: rgb(15,125,242); " +
                                    "-fx-background-radius: 20px;");

                            txtFlow.setPadding(new Insets(5, 10, 5, 10));
                            txtMsg.setFill(Color.color(0.934, 0.945, 0.996));

                            hbox.getChildren().add(txtFlow);
                            messageVbox.getChildren().add(hbox);
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    @FXML
    void online_clicked(ActionEvent event) {
        tableView.getItems().clear();
            try {
                Connection conDB = conNow.getConnection();
                String qury;

                if (onlineButton.getText().equals("Online Users")) {
                    onlineButton.setText("Offline Users");
                    qury = "select username from Users where username != '" + uname + "' and active = 0";
                } else {
                    onlineButton.setText("Online Users");
                    qury = "select username from Users where username != '" + uname + "' and active = 1";
                }
                ResultSet rs = conDB.createStatement().executeQuery(qury);

                while (rs.next()) {
                    obList.add(new UsersList(rs.getString("username")));
                }

                conDB.close();
            } catch (Exception e) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, e);
                e.printStackTrace();
            }

            userTable.setCellValueFactory(new PropertyValueFactory<UsersList, String>("username"));
            tableView.setItems(obList);
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
            Connection conDB = conNow.getConnection();

            String qury = "select username from Users where username != '" + uname + "' and active = 1";

            ResultSet rs = conDB.createStatement().executeQuery(qury);

            while (rs.next()) {
                obList.add(new UsersList(rs.getString("username")));
            }
            conDB.close();
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
            Connection conDB = conNow.getConnection();

            String qury = "select username from Users where username != '" + uname + "' and username like '%" + searchField.getText().toString() + "%'";

            ResultSet rs = conDB.createStatement().executeQuery(qury);

            while (rs.next()) {
                obList.add(new UsersList(rs.getString("username")));
            }
            conDB.close();
        } catch (Exception e) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }

        userTable.setCellValueFactory(new PropertyValueFactory<UsersList, String>("username"));
        tableView.setItems(obList);
    }

    @FXML
    void sendClicked(ActionEvent event) throws SQLException {

        if (writeMessage.getText().isEmpty()) {
            return;
        }

        HBox hbox = new HBox();

        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox.setPadding(new Insets(5, 5, 5, 10));

        Text txtMsg = new Text(writeMessage.getText());
        TextFlow txtFlow = new TextFlow(txtMsg);

        txtFlow.setStyle("-fx-color: rgb(239,242,255); " +
                "-fx-background-color: rgb(15,125,242); " +
                "-fx-background-radius: 20px;");

        txtFlow.setPadding(new Insets(5,10,5,10));
        txtMsg.setFill(Color.color(0.934, 0.945, 0.996));

        hbox.getChildren().add(txtFlow);
        messageVbox.getChildren().add(hbox);

        try {
            UsersList row = tableView.getSelectionModel().getSelectedItem();
            Connection conDB = conNow.getConnection();
            Statement stat = conDB.createStatement();
            String tableName = "message_" + uname;
            String receiverTable = "message_" + row.getUsername();

            String qury = "INSERT INTO " + tableName + " (withUser, message, timeSent, status)" +
                    "VALUES ('" + row.getUsername() + "', '"
                    + writeMessage.getText() + "', '" + formatter.format(date) + "', 'Sent')";

            String qury2 = "INSERT INTO " + receiverTable + " (withUser, message, timeSent, status)" +
                    "VALUES ('" + uname + "', '"
                    + writeMessage.getText() + "', '" + formatter.format(date) + "', 'Received')";

            stat.executeUpdate(qury);
            stat.executeUpdate(qury2);
            conDB.close();
        } catch (Exception e) { // any exception catcher and printer to the log viewer
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }
        writeMessage.setText("");
    }

    @FXML
    void settingsClicked(ActionEvent event) {
        // settings window opener
        try {
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
