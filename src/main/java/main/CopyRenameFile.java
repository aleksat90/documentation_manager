package main;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class CopyRenameFile {

    File source;
    File dest;
    File selectedFile;
    String sourcePathAbs;
    String categoryLoc;
    String fileLocationLink;
    String destDir;




    String mainDir="C:\\testJavaFile";

    public CopyRenameFile(File selectedFile, String categoryLoc, String fileLocationLink) {
        this.selectedFile = selectedFile;
        this.sourcePathAbs = selectedFile.getAbsolutePath();
        this.categoryLoc = categoryLoc;
        this.fileLocationLink = fileLocationLink;
        this.destDir=mainDir+"\\"+categoryLoc;
    }



    public void setMainDir(String mainDir) {
        this.mainDir = mainDir;
    }

    public String getMainDir() {
        return mainDir;
    }




    public void copyFile(){
        //Copy of source file on client side
        File source = new File(sourcePathAbs);
        //Copy to destination directory
        File dest = new File(destDir);


        try {
            System.out.println("Kopiraj "+source+" na "+dest);
            FileUtils.copyFileToDirectory(source, dest);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void renameFile(){
        File destCopiedNotRenamed = new File(destDir+"\\"+selectedFile.getName());
        File finalFileLoc = new File(fileLocationLink);
        try {
            System.out.println("Rename from "+destCopiedNotRenamed+" na "+finalFileLoc);
            destCopiedNotRenamed.renameTo(finalFileLoc);
        } catch (Exception e) {
            e.printStackTrace();
        }
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




