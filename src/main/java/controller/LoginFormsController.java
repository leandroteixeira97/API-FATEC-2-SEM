
package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.*;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.ConnectionFactory;
import model.User;
import view.Main;

import java.net.URL;
import java.util.ResourceBundle;


public class LoginFormsController implements Initializable {
    @FXML
    private TextField emailInputField;
    @FXML
    private PasswordField passwordInputField;
    @FXML
    private Label saveMessageButton;
    @FXML
    private CheckBox checkBox;
    @FXML
    private TextField passwordField;
    @FXML
    void changeVisibility(ActionEvent event) {
        if (checkBox.isSelected()){
            passwordField.setText(passwordInputField.getText());
            passwordField.setVisible(true);
            passwordInputField.setVisible(false);
            return;
        }
        passwordInputField.setText(passwordField.getText());
        passwordInputField.setVisible(true);
        passwordField.setVisible(false);
    }
    @FXML
    private Button saveButton;

    public void loginButton() throws IOException {
        String email = emailInputField.getText();
        String password = passwordInputField.getText();

        if (email.isBlank() || password.isBlank()) {
            saveMessageButton.setText("Os campos devem ser preenchidos!");
        } else {
            PreparedStatement stmt;
            ResultSet resultSet;
            Connection conn;
            conn = ConnectionFactory.getConnection();
            try {
                stmt = conn.prepareStatement("SELECT * FROM users WHERE email=(?)  AND password=(?)");
                stmt.setString(1, email);
                stmt.setString(2, password);
                resultSet = stmt.executeQuery();

                if (resultSet.next()) {
                    User usuarioLogado = User.getInstance();
                    usuarioLogado.setId(resultSet.getInt("user_id"));
                    usuarioLogado.setName(resultSet.getString("name"));
                    usuarioLogado.setEmail(resultSet.getString("email"));
                    usuarioLogado.setPassword(resultSet.getString("password"));
                    usuarioLogado.setPhone(resultSet.getString("phone"));
                    usuarioLogado.setDocument(resultSet.getString("document"));
                    usuarioLogado.setType(resultSet.getInt("type_adm"));

                    if (email.equals("admin") && password.equals("admin")) {
                        emailInputField.setText("");
                        passwordInputField.setText("");
                        Main.changeScene("profileHandler");
                    } else {
                        emailInputField.setText("");
                        passwordInputField.setText("");
                        Main.changeScene("userActiveConfig");
                    }
                } else {
                    saveMessageButton.setText("Essa combinação de e-mail e senha está incorreta.");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        saveButton.setDefaultButton(true);
        saveButton.setOnAction(actionEvent -> {
            try {
                loginButton();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void forgotPasswordOnAction(ActionEvent actionEvent) throws IOException {
        Main.changeScene("forgotPassword");
    }

    public void createAccountOnAction(ActionEvent actionEvent) throws IOException {
        Main.changeScene("userRegisterUser");
    }
}



