package main;

import javafx.stage.Stage;

public class TransferClass {


    static Stage stage;
    public static String docNum;
    public static String docName;
    public static String revision;



    public static String getDocNum() {
        return docNum;
    }

    public static void setDocNum(String docNum) {
        TransferClass.docNum = docNum;
    }

    public static String getDocName() {
        return docName;
    }

    public static void setDocName(String docName) {
        TransferClass.docName = docName;
    }

    public static String getRevision() {
        return revision;
    }

    public static void setRevision(String revision) {
        TransferClass.revision = revision;
    }





    public static void setPointerToEditWindowStage(Stage stage_){
         stage=stage_;
    }

    public static Stage getPointerToEditWindowStage(){
        return stage;
    }

    public static void setPointerToNewWindowStage(Stage stage_){
        stage=stage_;
    }

    public static Stage getPointerToNewWindowStage(){
        return stage;
    }






}
