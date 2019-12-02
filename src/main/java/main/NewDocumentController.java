package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.awt.Desktop;


public class NewDocumentController implements Initializable {


    //Deklarisanje promenljivih iz FXML-a
    @FXML
    private TextField txtDocumentNumber;
    @FXML
    private TextField txtDocumentName;
    @FXML
    private TextField txtDocumentLocation;
    @FXML
    private Button btnCreate,btnCancel;
    @FXML
    private Button btnBrowse,btnCreateRev,btnBrowseNewRev;

    @FXML
    private DatePicker datePicker;
    @FXML
    ChoiceBox choiceBox,langChoiceBox;

    @FXML
    CheckBox cbPRM, cbKU, cbEX, cbQS, cbQMS;

    @FXML
    private javafx.scene.control.ListView listView;

    String sourcePath, destFileName;
    String destDir = "C:\\testJavaFile";
    LocalDate dateCreatedOn = LocalDate.now();

    String newFileName, oldFileNameLoc;
    File selectedFile;
    String fN, lN;

    ObservableList<String> itemsListView = FXCollections.observableArrayList();


    public void initialize(URL location, ResourceBundle resources) {
        populateListView();
    }

    public void createDocument(ActionEvent event) throws SQLException {
        createNewDocument();
    }

    public void createAction(ActionEvent event){

//        Selected document from list view -> create new revision
//        Copy from

        System.out.println(listView.getSelectionModel().getSelectedItem().toString());
        createNewRevision();
    }



    public void browse(ActionEvent event){
        FileChooser fc = new FileChooser();
        this.selectedFile = fc.showOpenDialog(null);

        if(selectedFile!=null){
            txtDocumentLocation.setText(selectedFile.getAbsolutePath());
            this.sourcePath = selectedFile.getAbsolutePath();

        } else{
            System.out.println("File not selected valid...");
        }
    }

    public  String getExtension(String str){
        String extension ="";

        int i = str.lastIndexOf('.');
        if (i > 0) {
            extension = str.substring(i+1);
        }

        return extension;
    }


    public void pickRealeasdDate(ActionEvent event){
        this.dateCreatedOn=datePicker.getValue();
//        System.out.println(this.dateCreatedOn);
//        System.out.println(createChoiseBox());
    }

    public String createChoiseBox(){
        return choiceBox.getSelectionModel().getSelectedItem().toString();
    }
    public String langChoiseBox(){
        return langChoiceBox.getSelectionModel().getSelectedItem().toString();
    }


    public Integer getCountPreviousRevisions(String docNumber){
        Connection con= null;

        String SQLcheckDocNum = "SELECT COUNT(document_number) AS countRev FROM documents WHERE document_number='"+ docNumber +"'";
        System.out.println(SQLcheckDocNum);
        Integer countPrevRevs = 0;

        int revision=1;

        try {
            con = DBConnect.getConnection();
            Statement stmt = null;
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQLcheckDocNum);

            while(rs.next()){
                countPrevRevs=rs.getInt("countRev");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        revision=countPrevRevs+1;


        System.out.println("Revizija je: "+ revision);

        return revision;
    }



    public String getSelectedDepartments(){
        java.util.List<CheckBox> listCB = Arrays.asList(cbPRM, cbKU, cbEX, cbQS, cbQMS);
        String checkboxs="";

        for (CheckBox item:listCB){
            if(item.isSelected()){
                checkboxs=checkboxs+";"+item.getText();
            }
        }
        checkboxs=checkboxs.substring(1);
        System.out.println(checkboxs);
        return checkboxs;
    }


    public Integer getDocumentNumber(){
        //Prebroj koliko dokumenta tog tipa postoji u bazi u datoj godini
        String startDate = LocalDate.now().getYear()+"-"+"01-01";

        String SQL_count_category = "SELECT COUNT(document_number) AS docSerial FROM documents WHERE category='"+
                choiceBox.getSelectionModel().getSelectedItem().toString()+"' AND realeased_on BETWEEN '"+
                startDate+"' AND now()";
        Connection con;
        Integer serialDocNum=0;

        try {
            con = DBConnect.getConnection();
            Statement stmt = null;
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQL_count_category);

            while(rs.next()){
                serialDocNum=rs.getInt("docSerial");

            }
            serialDocNum=serialDocNum+1;


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return serialDocNum;
    }


    public void populateListView(){


        String sqlListView = "SELECT DISTINCT document_number, document_name\n" +
                "FROM documents ORDER BY document_number";

        try {
            Connection con = DBConnect.getConnection();
            ResultSet rs = con.createStatement().executeQuery(sqlListView);

            while (rs.next()) {
                itemsListView.add(rs.getString("document_number"));

//               listView= new javafx.scene.control.ListView<>(itemsListView);
                listView.setItems(itemsListView);
            }



        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void createNewDocument(){

//        this.lN = txtDocumentName.getText();
//        this.fN = choiceBox.getValue().toString()+"_"
//                +Integer.toString(LocalDate.now().getYear()).substring(2)+ "_"
//                +String.format("%03d",getDocumentNumber());
        Integer revision;


        Integer document_NUM = getDocumentNumber();
        String document_NAME = choiceBox.getValue().toString()+"_"+
                Integer.toString(LocalDate.now().getYear()).substring(2)+ "_"+
                String.format("%03d", document_NUM);



        String fileLocationLink = generateFileName(document_NUM).get(0);
        CopyRenameFile objekat = new CopyRenameFile(selectedFile,
                                                    choiceBox.getValue().toString(),
                                                    fileLocationLink);

        try {
            //Povezivanje na bazu
            Connection con=DBConnect.getConnection();
            revision = getCountPreviousRevisions(fN);
//            Snimiti sve parametre iz forme kao novi red u bazi
            String SQLstatement = "INSERT INTO documents(document_number, document_name,location_link, realeased_on,category,revision,language,departments)" +
                    "VALUES('"+document_NAME+"','"+txtDocumentName.getText()+"', '"+fileLocationLink+"','"+this.dateCreatedOn+"','"+createChoiseBox()+"'," +
                    "'"+revision+"','"+langChoiseBox()+"','"+getSelectedDepartments()+"')";
            System.out.println(SQLstatement);

            PreparedStatement st = con.prepareStatement(SQLstatement);
            st.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        destDir=destDir+"\\"+choiceBox.getValue().toString();


        //Kopiraj dokument na odredjenu lokaciju u filesystem i rename
        objekat.copyFile();
        objekat.renameFile();

        //Zatvaranje forme nakon kreiranja
        TransferClass.getPointerToNewWindowStage().close();



    }

    public void createNewRevision(){

        //Take which DocumentNumber to copy from
        String documentNumber = listView.getSelectionModel().getSelectedItem().toString();
        getCountPreviousRevisions(documentNumber);
        String fullNewRevFileLoc =  documentNumber+"_"+
                                    String.format("%02d", getCountPreviousRevisions(documentNumber))+"."+
                                    getExtension(selectedFile.getName());


        //Create SQL statement to copy this file
        String SQL_createNewRevisionDoc = "INSERT INTO documents(document_number,document_name,location_link, realeased_on,category,revision,language,departments)\n" +
                "SELECT document_number,document_name,'"+fullNewRevFileLoc+"', realeased_on,category,'"+getCountPreviousRevisions(documentNumber)+"',language,departments\n" +
                "FROM documents\n" +
                "WHERE document_number='"+documentNumber+"' AND revision='1'";


        CopyRenameFile objekat = new CopyRenameFile(selectedFile,documentNumber.substring(0,2),fullNewRevFileLoc);

        objekat.copyFile();
        objekat.renameFile();

        try {

            Connection con=DBConnect.getConnection();

            System.out.println(SQL_createNewRevisionDoc);

            PreparedStatement st = con.prepareStatement(SQL_createNewRevisionDoc);
            st.execute();
            //Read the needed information from newly created instance (Cateogor

        } catch (SQLException e) {
            e.printStackTrace();
        }


        //Zatvaranje forme nakon kreiranja
        TransferClass.getPointerToNewWindowStage().close();

    }


    public List<String> generateFileName(Integer docNumber){
        List<String> fileNames = new ArrayList<String>();

        String onlyNewFilename;
        //File name of the final with extension
        this.destFileName = destDir+"\\"+choiceBox.getValue().toString()+"\\"
                +choiceBox.getValue().toString()+"_"
                +Integer.toString(LocalDate.now().getYear()).substring(2)+"_"+
                docNumber.toString()+"_"+String.format("%03d", getCountPreviousRevisions(docNumber.toString()))+"."+getExtension(selectedFile.getName());

        //Name of the file in storage folder before renaming
        this.oldFileNameLoc = destDir+"\\"+choiceBox.getValue().toString()+"\\"+
                selectedFile.getName();

        //New filename with extension
        onlyNewFilename=choiceBox.getValue().toString()+"_"
                +Integer.toString(LocalDate.now().getYear()).substring(2)+"_"+
                docNumber.toString()+"_"+String.format("%02d", getCountPreviousRevisions(docNumber.toString()))+"."+getExtension(selectedFile.getName());

        fileNames.add(destFileName);
        fileNames.add(oldFileNameLoc);
        fileNames.add(onlyNewFilename);

        return fileNames;
    }





}
