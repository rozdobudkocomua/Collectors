package Collectors.Interfaces.impls;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import Collectors.Controllers.Controller;
import Collectors.Interfaces.DebtBook;
import Collectors.Objects.Person;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

public class CollectionDebtBook implements DebtBook {

    private ObservableList<Person> personsList = FXCollections.observableArrayList();
    protected Controller controller;

    @Override
    public void add(Person person) {
        personsList.add(person);
    }

    @Override
    public void update(Person person) {
        //пусть побудет
    }

    @Override
    public void delete(Person person) {
        personsList.remove(person);
    }

    public ObservableList<Person> getPersonsList() {
        return personsList;
    }

    public void setPersonsList(ObservableList<Person> personsList) {
        this.personsList = personsList;
    }

    public void readFromExcel(String fileName) throws IOException {  // тестируем выгрузку из экселя в коллекцию класса Персон
        int ADDRESS_CLMN = 0; //номера строк в экселе
        int APP_CLMN = 1;
        int FIO_CLMN = 2;
        int DEBT_CLMN = 3;
        int PHONE_CLMN = 4;

        FileInputStream fis = new FileInputStream(fileName);
        XSSFWorkbook wb = new XSSFWorkbook(fis);  //  пока используем на *.xlsx
        XSSFSheet wb_sheet = wb.getSheetAt(0);
        Iterator<Row> rows = wb_sheet.rowIterator();
        if (rows.hasNext()) //пропускаем заголовок таблицы
        {
            rows.next();
        }
        while (rows.hasNext()) {
            XSSFRow row = (XSSFRow) rows.next();
            Person person = new Person();
            person.setAddress(row.getCell(0).getStringCellValue());
            if (row.getCell(1).getCellType() == XSSFCell.CELL_TYPE_STRING) {
                person.setApp(row.getCell(1).getStringCellValue());
            }
            if (row.getCell(1).getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
//                person.setApp(Double.toString(row.getCell(1).getNumericCellValue()));
                person.setApp(String.valueOf(Integer.valueOf(row.getCell(1).toString().replace(".0",""))));
            }
            person.setFio(row.getCell(2).getStringCellValue());
            person.setDebt(row.getCell(3).getNumericCellValue());
            person.setPhone(Integer.toString((int) row.getCell(4).getNumericCellValue()));
            personsList.add(person);
        }
        fis.close();
    }
}