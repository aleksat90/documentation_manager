package main;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EditWindowController implements Initializable {

    //PROMENLJIVE DEKLARACIJA
    @FXML
    private CheckBox cbValidity;
    @FXML
    private TextField txtDocName;

    private void applyAction(ActionEvent event){
        updateDB();
    }


    public void updateDB(){

        String docNum = TransferClass.getDocNum();
        String rev = TransferClass.getRevision();
        String docName = TransferClass.getDocName();

        try {
            Connection con=DBConnect.getConnection();

            //Update all DocumentNumbers to false
            if (cbValidity.isSelected()) {
                String sqlUpdateAllValidity = "UPDATE documents " +
                        "SET validity = 'false' " +
                        "WHERE document_number='" +
                        docNum+"'";
                System.out.println(sqlUpdateAllValidity);

                PreparedStatement st = con.prepareStatement(sqlUpdateAllValidity);
                st.execute();
                //Update of selected version to Valid version
                String sqlUpdateSelectedDocValidity = "UPDATE documents " +
                        "SET validity ='true' " +
                        "WHERE document_number='"+ docNum +"' AND revision="+rev;
//            System.out.println(sqlUpdateSelectedDocValidity);
                PreparedStatement stUpd = con.prepareStatement(sqlUpdateSelectedDocValidity);
                stUpd.execute();
            }


            //Update Document name
            String sqlUpdateDocName = "UPDATE documents SET document_name='"+
                                        txtDocName.getText()+"' WHERE document_number='"+
                                        TransferClass.getDocNum()+"'";

            PreparedStatement setDocName = con.prepareStatement(sqlUpdateDocName);
            setDocName.execute();



        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeApp();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Odlazak u bazu da proveris da li je za taj selektovana vec
//        cbValidity.setSelected();
    }

    public void closeApp(){
        TransferClass.getPointerToEditWindowStage().close();

//        Platform.exit();
//        System.exit(0);
    }



}
