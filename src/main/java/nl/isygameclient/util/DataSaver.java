package nl.isygameclient.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataSaver {

    public static final String TEMP_PATH = "./data/temp/";
    private final String path;
    private final String fileName;
    private final String filePath;

    public DataSaver() {
        this(TEMP_PATH, randomString(7) + ".csv" );
    }

    public DataSaver(String path, String fileName) {
        this.path = path;
        this.fileName = fileName;
        filePath = path + fileName;
        this.createDirectories();
    }

    /**
     * Using the path provided in the constructor this function created the necessary directories
     * in case they are not present.
     */
    public void createDirectories() {
        File file = new File(path);
        if (file.exists()) {
            return;
        }
        if (file.isFile()) {
            System.err.println("Path provided is not a directory: \n" + path);
            System.exit(-1);
        }

        boolean result = file.mkdirs();
        if (result) {
            System.out.println("Created directories for path:\n " + path);
        } else {
            System.err.println("Failed to create directories for path:\n" + path);
        }

    }

    /**
     * Given a String containing data. This function will store the data inside a csv file
     * of which the filepath has been provided in the constructor.
     * @param data to be stored inside a csv file.
     */
    public void saveData(String data) {
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(filePath, true));
            output.write(data);
            output.newLine();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Given a directory containing csv files. This function will merge the contents into a singular csv file
     * of which the filepath has been provided in the constructor.
     * @param directoryPath to the directory containing csv files that need merging.
     */
    public void mergeFiles(String directoryPath) {
        Set<String> files = getTempFiles(directoryPath);

        assert files != null;
        for (String file : files) {
            System.out.println(directoryPath + file);
            try {
                BufferedReader bf = new BufferedReader(new FileReader(filePath));
                bf.readLine();
                String line = bf.readLine();

                while (line != null) {
                    saveData(line);
                    line = bf.readLine();
                }
                bf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Given a path to a directory containing files.
     * @param directoryPath containing files.
     * @return A Set containing the file paths of all the files in the given directory.
     */
    private static Set<String> getTempFiles(String directoryPath) {
        try (Stream<Path> stream = Files.list(Paths.get(directoryPath))) {
            return stream
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Given a length this function returns a random string of said length consisting of capital letters, and numbers.
     * @param length of the random string to be returned.
     * @return String consisting of random capital letters A-Z, and numbers 0-9;
     */
    public static  String randomString(int length) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(alphabet.length());
            char randomChar = alphabet.charAt(index);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    public String getPath() {
        return path;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }
}
