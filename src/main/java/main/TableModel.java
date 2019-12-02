package main;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.Date;
import java.time.LocalDate;

public class TableModel {

    private String firstName;
    private String lastName;
    private Button btnOpen;
    private Button btnEdit;
    private String category;
    private String revision;
    private final ObjectProperty<Date> realeased_on;
    //    private Date realeased_on;
    private String departments;
    private String language;



    public TableModel(String firstName, String lastName, Button openBtn, String link,
                      String category, String revision, Date realeased_on,
                      Button btnEdit, String departments, String language) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.btnOpen = openBtn;
        this.category = category;
        this.revision=revision;
        this.realeased_on =  new SimpleObjectProperty<Date>(realeased_on);
        this.btnEdit = btnEdit;
        this.departments = departments;
        this.language = language;

        openBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                fileOpen(link);
            }
        });

        btnEdit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                setValid(firstName,lastName,revision);
                System.out.println("Kreirana forma Edit dokument");


                try {
//                    Stage primaryStage = new Stage();
//                    Parent root = null;
//                    root = FXMLLoader.load(getClass().getClassLoader().getResource("editDocument.fxml"));
//                    primaryStage.setTitle("Edit document");
//                    primaryStage.setScene(new Scene(root, 600, 400));
//                    primaryStage.show();


                    Stage primaryStage = new Stage();
                    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("editWindow.fxml"));
                    primaryStage.setTitle("Edit document");
                    primaryStage.setScene(new Scene(root, 400, 300));
                    primaryStage.show();

                    Stage transStage = primaryStage;
                    TransferClass.setPointerToEditWindowStage(transStage);




                } catch (IOException ex) {
                    ex.printStackTrace();
                }


            }
        });

    }

    public void fileOpen(String link) {
        //Test otvaranje tek snimljenog file
        try {
            Desktop desktop = Desktop.getDesktop();
            File file = new File(link);
            desktop.open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public Button getBtnOpen() {
        return btnOpen;
    }

    public void setBtnOpen(Button btnOpen) {
        this.btnOpen = btnOpen;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }


    public void setBirthday(Date realeased_on) {
        this.realeased_on.set(realeased_on);
    }

    public ObjectProperty<Date> realeased_onProperty() {
        return realeased_on;
    }

//    public Date getReleasedOn() {
//        return realeased_on;
//    }
//
//    public void setReleasedOn(Date realeased_on) {
//        this.realeased_on = realeased_on;
//    }

    public Button getBtnEdit() {
        return btnEdit;
    }

    public void setBtnEdit(Button btnEdit) {
        this.btnEdit = btnEdit;
    }
    public void setValid(String docNum, String docName, String rev){

        TransferClass.setDocNum(docNum);
        TransferClass.setRevision(rev);
        TransferClass.setDocName(docName);
    }


    public String getDepartments() {
        return departments;
    }

    public void setDepartments(String departments) {
        this.departments = departments;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}
