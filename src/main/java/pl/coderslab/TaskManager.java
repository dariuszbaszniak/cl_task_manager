package pl.coderslab;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TaskManager {
    public static void main(String[] args) {

        System.out.println(ConsoleColors.BLUE + "Hello - Start");

        String fileName = "tasks.csv";
        checkIfFileExists(fileName);

        if (checkIfFileExists(fileName) == true) {

            int rows = countRowsInFile(fileName);
            int cols = countColumnsInFile(fileName);
            String[][] tasksTable = createTable(rows, cols);
            tasksTable = insertTasksToTable(fileName, tasksTable);
            viewTask(tasksTable);

        } else {
            System.out.println("File " + fileName + " not exists");
        }

    }

    public static boolean checkIfFileExists(String fileName) {
        Path fileNamePath = Paths.get(fileName);
        return Files.exists(fileNamePath) ? true : false;
    }

    public static int countRowsInFile(String fileName) {
        int rows = 0;
        Path fileNamePath = Paths.get(fileName);
        try {
            for (String line : Files.readAllLines(fileNamePath)) {
                rows++;
            }
        } catch (IOException e) {
            System.out.println("File " + e.getMessage() + " not exists");
        }

        return rows;
    }

    public static int countColumnsInFile(String fileName) {
        int cols = 0;
        Path fileNamePath = Paths.get(fileName);
        try {
            for (String line : Files.readAllLines(fileNamePath)) {
                String[] columns = line.split(",");
                if (columns.length > cols) {
                    cols = columns.length;
                }
            }
        } catch (IOException e) {
            System.out.println("File " + e.getMessage() + " not exists");
        }

        return cols;
    }

    public static String[][] createTable(int rows, int cols) {
        String[][] table = new String[rows][cols];
        return table;
    }

    public static String[][] insertTasksToTable(String fileName, String[][] table) {

        String[][] returnTable = table;
        String line = "";
        int i = 0;

        try {
            //CSV file into BufferedReader
            BufferedReader br = new BufferedReader(new FileReader(fileName));

            while ((line = br.readLine()) != null)   //returns a Boolean value
            {
                String[] tempTable = line.split(",");

                for (int j = 0; j < tempTable.length; j++) {
                    returnTable[i][j] = tempTable[j];
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return returnTable;
    }

    public static void viewTask(String[][] tableName) {

        for (int i = 0; i < tableName.length; i++) {
            for (int j = 0; j < tableName[i].length; j++) {
                System.out.print(tableName[i][j]+"\t");
            }
            System.out.println();
        }
    }
}
