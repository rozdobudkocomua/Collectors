package Collectors.Controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import Collectors.Interfaces.impls.CollectionDebtBook ;
import Collectors.Objects.Person;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class Controller {
    // коллекция должников
    private CollectionDebtBook cdb = new CollectionDebtBook();
    //коллекция с номерами телефонов для обзвона
    private SortedSet<String> phonesToCall = new TreeSet<String>();

    public String fileName;   //имя файла из окна FileChooser

    //выносим загрузчики для доступа из методов
    private Stage mainStage;
    private Parent fxmlEdit;
    private FXMLLoader fxmlLoader = new FXMLLoader();
    private EditController editController;
    private Stage editStage;

    @FXML
    private TableView tableFirst;

    @FXML
    private TableColumn<Person, String> columnAddress;

    @FXML
    private TableColumn<Person, Integer> columnApp;

    @FXML
    private TableColumn<Person, String> columnFIO;

    @FXML
    private TableColumn<Person, Double> columnDebt;

    @FXML
    private TableColumn<Person, Integer> columnPhone;

    @FXML
    private Button BtnLoad;

    @FXML
    private Button BtnToCall;

    @FXML
    private Button BtnDeleteAll;

    @FXML
    private Button btnEdit;

    public TableView getTable() {
        return tableFirst;
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    // загружаем данные в TableView
    @FXML
    private void initialize() {
        columnAddress.setCellValueFactory(new PropertyValueFactory<Person, String>("address"));
        columnApp.setCellValueFactory(new PropertyValueFactory<Person, Integer>("app"));
        columnFIO.setCellValueFactory(new PropertyValueFactory<Person, String>("fio"));
        columnDebt.setCellValueFactory(new PropertyValueFactory<Person, Double>("debt"));
        columnPhone.setCellValueFactory(new PropertyValueFactory<Person, Integer>("phone"));
        initListeners();
        fillData();
        initLoader();
    }

    private void fillData() {
        tableFirst.setItems(cdb.getPersonsList());
    }

    // открытие окна для редактирования по двойному клику на записи в TableView
    private void initListeners() {
        tableFirst.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    editController.setPerson((Person) tableFirst.getSelectionModel().getSelectedItem());
                    showDialog();
                }
            }
        });
    }

    private void initLoader() {
        try {
            fxmlLoader.setLocation(getClass().getResource("/fxml/edit.fxml"));
            fxmlEdit = fxmlLoader.load();
            editController = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Загружаем внешний файл нужно будет засингелтонить
    public void loadFile(ActionEvent actionEvent) {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("MS Excel files", "*.xlsx"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                cdb.readFromExcel(file.getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void editPerson(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        //если не кнопка - закрываем метод... для примера
        if (!(source instanceof Button)) {
            return;
        }
        editController.setPerson((Person) tableFirst.getSelectionModel().getSelectedItem());
        showDialog();
    }

    private void showDialog() {
        if (editStage == null) {
            editStage = new Stage();
            editStage.setTitle("Редактирование записи");
            editStage.setMinHeight(185);
            editStage.setMinWidth(495);
            editStage.setResizable(false);
            editStage.setScene(new Scene(fxmlEdit));
            editStage.initModality(Modality.WINDOW_MODAL);
            editStage.initOwner(mainStage);
        }

        editStage.showAndWait(); //ожидаем закрытия окна для дальнейших манипуляций

    }

    // очистка таблицы
    public void clearAll(ActionEvent actionEvent) {
        cdb.getPersonsList().clear();
    }

    //удаление записи из TableView
    public void deletePerson(ActionEvent actionEvent) {
        cdb.delete((Person) tableFirst.getSelectionModel().getSelectedItem());
    }

    //создание коллекции из уникальных номеров телефона для записи в файл обзвона
    public SortedSet<String> phonesToCall(CollectionDebtBook cdb) {
        SortedSet<String> phonesTo = new TreeSet<String>();
        int i = 0;
        while (i < cdb.getPersonsList().size()) {
            phonesTo.add(cdb.getPersonsList().get(i).getPhone());
            i++;
        }
        return phonesTo;
    }

    //создание excel файла с уникальными номерами для передачи в АТС на автообзвон
    public void writeToExcel(String fileName, SortedSet<String> sortedSet) {
        XSSFWorkbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet();
        int end = sortedSet.size(); //заранее определяем кол-во строк, т.к. уменьшаем коллекцию в цикле

        for (int i = 0; i < end; i++) {
            Row row = sheet.createRow(i);
            org.apache.poi.ss.usermodel.Cell cell = row.createCell(0);
            cell.setCellValue(sortedSet.first()); //берем минимальный элемент коллекции
            sortedSet.remove(sortedSet.first()); // удаляем минимальный элемент
        }

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(fileName);
            wb.write(fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //сохранение в файл
    public void saveToCall(ActionEvent actionEvent) {
        //System.out.println(phonesToCall(cdb));  // смотрим в консоли список уникальных номеров
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранение списка на обзвон...");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("MS Excel files", "*.xlsx"));
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) writeToExcel(file.getPath(), phonesToCall(cdb));
    }
}



