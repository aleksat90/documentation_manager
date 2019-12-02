package main;

import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import jdk.nashorn.internal.codegen.CompilerConstants;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    @FXML
    private TableView<TableModel> table;
    @FXML
    private TableColumn<TableModel,String> col_firstName;
    @FXML
    private TableColumn<TableModel,String> col_lastName;
    @FXML
    private TableColumn<TableModel,String> colCategory;
    @FXML
    private TableColumn<TableModel, String> colRevision;
    @FXML
    Button btnUpdateFilter;
    @FXML
    private TableColumn<TableModel, String> colOpen;
    @FXML
    private TableColumn<TableModel, String> colEditBtn;
    @FXML
    private TableColumn<TableModel, Date> colReleasedOn;
    @FXML
    private TableColumn<TableModel, String> colDepartments;
    @FXML
    private TableColumn<TableModel, String> colLanguage;
    @FXML
    private CheckBox cbPB, cbAA, cbFB, cbSelectAll, cbSRB, cbEN, cbDE;

    @FXML private DatePicker dpFrom, dpTo;

    @FXML
    private TextField searchField;
    @FXML
    Button btnSearch;


    Button btnOpenFile, btnEditFile;
    LocalDate date;
    ObservableList<TableModel> oblist = FXCollections.observableArrayList();



    public void initialize(URL location, ResourceBundle resources) {
        String SQL_statement="SELECT * FROM documents";
        viewAll(SQL_statement);

//        System.out.println(takeMaxMinDate().get(0));

        dpFrom.setValue((LocalDate) takeMaxMinDate().get(0));
        dpTo.setValue((LocalDate) takeMaxMinDate().get(1));
        dpFrom.setEditable(false);
        dpTo.setEditable(false);
    }


    public void newDocument(ActionEvent event) throws Exception{
        System.out.println("Kreirana forma");

        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("newDocumentForm.fxml"));
        primaryStage.setTitle("New document");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
        TransferClass.setPointerToNewWindowStage(primaryStage);

    }

    public void closeApp(ActionEvent event){
        Platform.exit();
        System.exit(0);
    }

    public void viewAll(String sql_statement){

        try {
            Connection con = DBConnect.getConnection();
            ResultSet rs = con.createStatement().executeQuery(sql_statement);
            String link;
            while (rs.next()) {
                oblist.add(new TableModel(rs.getString("document_number"),
                        rs.getString("document_name"),
                        btnOpenFile = new Button("Open"),
                        link = rs.getString("location_link"),
                        rs.getString("category"),
                        rs.getString("revision"),
                        rs.getDate("realeased_on"),
                        btnEditFile = new Button("Edit"),
                        rs.getString("departments"),
                        rs.getString("language")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        table.setItems(oblist);

        col_firstName.setCellValueFactory(new PropertyValueFactory<TableModel, String>("firstName"));
        col_lastName.setCellValueFactory(new PropertyValueFactory<TableModel, String>("lastName"));
        colOpen.setCellValueFactory(new PropertyValueFactory<TableModel, String>("btnOpen"));
        colCategory.setCellValueFactory(new PropertyValueFactory<TableModel, String>("category"));
        colRevision.setCellValueFactory(new PropertyValueFactory<TableModel, String>("revision"));
        colReleasedOn.setCellValueFactory(cellData -> cellData.getValue().realeased_onProperty());
        colEditBtn.setCellValueFactory(new PropertyValueFactory<TableModel, String>("btnEdit"));
        colDepartments.setCellValueFactory(new PropertyValueFactory<TableModel, String>("departments"));
        colLanguage.setCellValueFactory(new PropertyValueFactory<TableModel, String>("language"));
//        colEditBtn.setVisible(false);
        SimpleDateFormat dateFromatter = new SimpleDateFormat("dd.MM.yyyy");

// Custom rendering of the table cell.
        colReleasedOn.setCellFactory(column -> {
            return new TableCell<TableModel, Date>() {
                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        // Format date.
                        setText(dateFromatter.format(item));
                      }
                }
            };
        });

//        table.setRowFactory(row -> new TableRow<TableModel>(){
//            @Override
//            public void updateItem(TableModel item, boolean empty){
//                super.updateItem(item, empty);
//
//                if (item == null || empty) {
//                    setStyle("");
//                } else {
//                    //Now 'item' has all the info of the Person in this row
//                    if (item.getCategory().equals("PB")) {
//                        setStyle("-fx-background-color: #ffd7d1");
//                        //We apply now the changes in all the cells of the row
////                        for(int i=0; i<getChildren().size();i++){
////                            ((Labeled) getChildren().get(i)).setTextFill(Color.RED);
////                        }
//                    }
//                }
//            }
//        });

        FilteredList<TableModel> filterData = new FilteredList(oblist,b->true);

        searchField.textProperty().addListener((observable,oldValue,newValue) -> {
            filterData.setPredicate(tableModel -> {
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }
                String lowerCaseFilter=newValue.toLowerCase();

                if(tableModel.getFirstName().toLowerCase().indexOf(lowerCaseFilter)!=-1){
                    return true;
                } else if(tableModel.getLastName().toLowerCase().indexOf(lowerCaseFilter)!=-1){
                    return true;
                } else{
                    return false;
                }

            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<TableModel> sortedData = new SortedList<>(filterData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
       sortedData.comparatorProperty().bind(table.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        table.setItems(sortedData);

    }


    @FXML
    private void handleButtonAction(ActionEvent event){
        System.out.println("Button pressed");
    }

    public void tableRefresh(ActionEvent event) throws IOException, SQLException {

        String SQL_date_range = "WHERE realeased_on>="+ "'"+dpFrom.getValue().toString() +"'"+" AND realeased_on<="+"'"+dpTo.getValue().toString()+"'";

        String SQL_statement="SELECT * FROM documents "+ SQL_date_range;
        System.out.println(SQL_statement);

        boolean alreadySelectedCB=false;

        if(cbSelectAll.isSelected()){
            cbPB.setIndeterminate(false);
            cbAA.setIndeterminate(false);
            cbFB.setIndeterminate(false);
        }

        List <CheckBox> cbListCategory = new ArrayList<>();
        cbListCategory.add(cbAA);
        cbListCategory.add(cbPB);
        cbListCategory.add(cbFB);

        List <CheckBox> cbListLang = new ArrayList<>();
        cbListLang.add(cbSRB);
        cbListLang.add(cbEN);
        cbListLang.add(cbDE);


        String andST = " AND ";
        boolean andSTboolCat = false;
        for (CheckBox item:cbListCategory){
            if(item.isSelected()){
                if(!andSTboolCat){
                    SQL_statement=SQL_statement+andST+"category='"+item.getText()+"'";

                } else if(andSTboolCat){
                    SQL_statement = SQL_statement +" OR category='"+item.getText()+"'";
                }

                andSTboolCat=true;
            }


        }
        boolean andSTboolLang = false;
        for (CheckBox item:cbListLang){
            if(item.isSelected()){
                if(!andSTboolLang){
                    SQL_statement=SQL_statement+andST+"language='"+item.getText()+"'";

                } else if(andSTboolLang){
                    SQL_statement = SQL_statement +" OR language='"+item.getText()+"'";
                }

                andSTboolLang=true;
            }
        }


        table.setItems(oblist);
        table.getItems().clear();
        System.out.println("Get items: "+ table.getItems());
        table.refresh();
        System.out.println("Filter stmt: " + SQL_statement);
//        table.getSortOrder().add(col_firstName);
        viewAll(SQL_statement);
        searchField.clear();


    }


    public List takeMaxMinDate(){

        List<LocalDate> list= new ArrayList<LocalDate>();

        Connection con = null;

        try {
            String getMIN = "SELECT MIN(realeased_on)FROM documents";
            String getMAX = "SELECT MAX(realeased_on)FROM documents";
            con = DBConnect.getConnection();
            Statement stmt = null;
            stmt = con.createStatement();
            ResultSet rsMIN = stmt.executeQuery(getMIN);


            while(rsMIN.next()){
                LocalDate minDate= rsMIN.getDate(1).toLocalDate();
                list.add(minDate);
            }
            ResultSet rsMAX = stmt.executeQuery(getMAX);
            while(rsMAX.next()){
                LocalDate maxDate= rsMAX.getDate(1).toLocalDate();
                list.add(maxDate);
            }


        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        System.out.println(list);
        return list;

    }

    public void editDocument(ActionEvent event) throws Exception{
        System.out.println("Kreirana forma Edit dokument");

        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("editDocument.fxml"));
        primaryStage.setTitle("Edit document");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();

    }

    public void selectAllCat(ActionEvent event){
        if(cbSelectAll.isSelected()){
            cbAA.setSelected(false);
            cbPB.setSelected(false);
            cbFB.setSelected(false);

            }

        }
    }



