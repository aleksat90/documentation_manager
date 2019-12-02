package main;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NewDocRev {

    String categoryType;
    String fileNewRev;
    File selectedFile;
    String mainDir;

    public NewDocRev(File selectedFile, String mainDir) {
        this.categoryType = categoryType;
        this.selectedFile = selectedFile;
        this.mainDir = mainDir;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }


    public String getFileNewRev() {
        return fileNewRev;
    }

    public void setFileNewRev(String fileNewRev) {
        this.fileNewRev = fileNewRev;
    }

    public List<String> getNewDocumentName(){

        List<String> newDocument = new ArrayList<>();

        //Get free doc serial number
        getDocumentNumber();

        String newDocumentNumber;
        newDocumentNumber=categoryType+"_"+
                            Integer.toString(LocalDate.now().getYear()).substring(2)+ "_"+
                            String.format("%03d", getDocumentNumber());


        //Adding name_
//        [0] - only filename without revision and extension
//        [1] - filename with revision and extension
//        [2] - New full filename path in new folder
//        [3] - old File name path to be used for renaming
        newDocument.add(newDocumentNumber);
        newDocument.add(newDocumentNumber+"_01"+getExtension());
        newDocument.add(mainDir+"//"+categoryType+"//"+newDocument.get(1));
        newDocument.add(mainDir+"//"+categoryType+"//"+selectedFile.getName());
        return newDocument;
    }

    public String getNewDocFileName(){
        return getNewDocumentName()+"."+getExtension();
    }




    public Integer getDocumentNumber(){
        //Prebroj koliko dokumenta tog tipa postoji u bazi u datoj godini
        String startDate = LocalDate.now().getYear()+"-"+"01-01";

        String SQL_count_category = "SELECT COUNT(document_number) AS docSerial FROM documents WHERE category='"+
                categoryType+"' AND realeased_on BETWEEN '"+
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

    public String getExtension(){
        String extension ="";

        int i = selectedFile.getName().lastIndexOf('.');
        if (i > 0) {
            extension = selectedFile.getName().substring(i+1);
        }

        return extension;
    }


}
