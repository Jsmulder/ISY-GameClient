package nl.isygameclient;

import nl.isygameclient.util.DataSaver;

public class MergeFiles {
    public static void main(String[] args) {
        DataSaver dataSaver = new DataSaver("./data/", "results.csv");
        dataSaver.mergeFiles(DataSaver.TEMP_PATH);
    }
}
