package main;

public class SettingsClass {

    private static String mainDir = "C:\\testJavaFile";


    public static String getMainDir() {
        return mainDir;
    }

    public static void setMainDir(String mainDir) {
        SettingsClass.mainDir = mainDir;
    }
}
