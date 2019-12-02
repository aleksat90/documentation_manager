package main;

public class ListView {

    private String docNum;
    private String docName;

    public ListView(String docNum, String docName) {
        this.docNum = docNum;
        this.docName = docName;
    }
    public ListView(String docNum) {
        this.docNum = docNum;

    }

    public String getDocNum() {
        return docNum;
    }

    public void setDocNum(String docNum) {
        this.docNum = docNum;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }




}
