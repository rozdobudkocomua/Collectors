package Collectors.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Collectors.Objects.Person;

public class EditController {

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtApp;

    @FXML
    private TextField txtFio;

    @FXML
    private TextField txtDebt;

    @FXML
    private TextField txtPhone;

    @FXML
    private Button btnOk;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnDelete;

    private Person person;

    public void actionClose(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.hide();
    }

    // кнопка Ок
    public void actionOk(ActionEvent actionEvent) {
        boolean bool = true;
        while (bool) {
            person.setAddress(txtAddress.getText());
            person.setApp(txtApp.getText());
            person.setFio(txtFio.getText());
            person.setPhone(txtPhone.getText());
            // Предупреждение при попытке ввести буквы вместо цифр
            try {
                person.setDebt(Double.parseDouble(txtDebt.getText()));
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText(null);
                alert.setContentText("Долг: введите числовое значение");
                alert.setResizable(false);
                alert.showAndWait();
                break;
            }
            bool = false;
            actionClose(actionEvent);
        }
    }

    //редактирование

    public void setPerson(Person person) {
        if (person == null) {
            return;
        }
        this.person = person;

        txtAddress.setText(person.getAddress());
        txtApp.setText(person.getApp());
        txtDebt.setText(String.valueOf(person.getDebt()));
        txtFio.setText(person.getFio());
        txtPhone.setText(person.getPhone());

    }

    public Person getPerson() {
        return person;
    }

}
