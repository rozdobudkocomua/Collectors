package Collectors.Objects;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Person {
    //SimpleStringProperty SimpleDoubleProperty для ускоренного взаимодействия с TableView при редактировании. Т.е. при изменении данных моментально изменяются значения в таблице
    private SimpleStringProperty fio = new SimpleStringProperty(""); //начальная инициализация, для изюежания ошибок, которые могут возникнуть из-за null
    private SimpleStringProperty phone = new SimpleStringProperty("");
    private SimpleStringProperty address = new SimpleStringProperty("");
    private SimpleStringProperty app = new SimpleStringProperty("");
    private SimpleDoubleProperty debt = new SimpleDoubleProperty(0);

    public Person() {
    }

    public String getFio() {
        return fio.get();
    }

    public void setFio(String fio) {
        this.fio.set(fio);
    }

    public String getPhone() {
        return phone.get();
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public String getAddress() {
        return address.get();
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getApp() {
        return app.get();
    }

    public void setApp(String app) {
        this.app.set(app);
    }

    public double getDebt() {
        return debt.get();
    }

    public void setDebt(double debt) {
        this.debt.set(debt);
    }

    public SimpleStringProperty addressProperty() {
        return address;
    }

    public SimpleStringProperty appProperty() {
        return app;
    }

    public SimpleStringProperty fioProperty() {
        return fio;
    }

    public SimpleDoubleProperty debtProperty() {
        return debt;
    }

    public SimpleStringProperty phoneProperty() {
        return phone;
    }


}