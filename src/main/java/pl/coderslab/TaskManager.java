package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Scanner;

public class TaskManager {
    public static void main(String[] args) {

        String fileName = "tasks.csv";
        checkIfFileExists(fileName);

        if (checkIfFileExists(fileName) == true) {

            int rows = countRowsInFile(fileName);
            int cols = countColumnsInFile(fileName);
            String[][] tasksTable = createTable(rows, cols);
            tasksTable = insertTasksToTableFromFile(fileName, tasksTable);
            listMenu();
            Scanner scanner = new Scanner(System.in);
            String operation = scanner.next();

            while (!operation.equals("")) {
                switch (operation) {
                    case "add":
                        tasksTable = addTask(tasksTable, cols);
                        listMenu();
                        break;
                    case "remove":
                        tasksTable = removeTask(tasksTable);
                        listMenu();
                        break;
                    case "list":
                        viewTask(tasksTable);
                        listMenu();
                        break;
                    case "exit":
                        exit(fileName, tasksTable);
                        return;
                    default:
                        System.out.println("Please select a correct option.");
                }
                operation = scanner.next();
            }

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

    public static String[][] insertTasksToTableFromFile(String fileName, String[][] table) {

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
            System.out.print(ConsoleColors.WHITE + i + " : ");
            for (int j = 0; j < tableName[i].length; j++) {
                System.out.print(ConsoleColors.WHITE + tableName[i][j] + "\t");
            }
            System.out.println();
        }
    }

    public static void listMenu() {
        System.out.println();
        System.out.println(ConsoleColors.BLUE + "Pleas select an option");
        System.out.println(ConsoleColors.WHITE + "add");
        System.out.println(ConsoleColors.WHITE + "remove");
        System.out.println(ConsoleColors.WHITE + "list");
        System.out.println(ConsoleColors.WHITE + "exit");
    }

    public static void exit(String fileName, String[][] tasksTable) {

        //clear file
        try (PrintWriter writer = new PrintWriter(fileName)) {
            writer.write("");
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        Path newFile = Paths.get(fileName);
        String stringLine = "";

        for (int i = 0; i < tasksTable.length; i++) {
            for (int j = 0; j < tasksTable[i].length; j++) {
                if (j == tasksTable[i].length - 1) {
                    stringLine = stringLine + tasksTable[i][j];
                } else {
                    stringLine = stringLine + tasksTable[i][j] + ", ";
                }
            }
            try {
                // write stringLine to file
                Files.writeString(newFile, stringLine + "\n", StandardOpenOption.APPEND);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            stringLine = "";
        }

        System.out.println(ConsoleColors.RED + "Bye, bye.");
    }

    public static String[][] addTask(String[][] tasksTable, int cols) {

        Scanner scanner = new Scanner(System.in);
        System.out.println(ConsoleColors.WHITE + "Please add task description");
        String description = scanner.nextLine();

        System.out.println(ConsoleColors.WHITE + "Please add task due date [format YYYY-MM-DD]");
        String date = scanner.nextLine();

        boolean checkDate = validDate(date);
        while (checkDate != true) {
            System.out.println(ConsoleColors.RED + "Incorrect argument passed. Please give format YYYY-MM-DD");
            date = scanner.nextLine();
            checkDate = validDate(date);

        }

        System.out.println(ConsoleColors.WHITE + "Is your task is important: true/false");
        String important = scanner.nextLine();
        important = important.toLowerCase();
        //check important condition
        while (!important.toLowerCase().equals("true") && !important.toLowerCase().equals("false")) {
            System.out.println(ConsoleColors.RED + "Incorrect argument passed. Please give true or false");
            important = scanner.next();
            important = important.toLowerCase();
        }

        tasksTable = Arrays.copyOf(tasksTable, tasksTable.length + 1);
        tasksTable[tasksTable.length - 1] = new String[cols];

        tasksTable[tasksTable.length - 1][0] = description;
        tasksTable[tasksTable.length - 1][1] = date;
        tasksTable[tasksTable.length - 1][2] = important;

        return tasksTable;
    }

    public static String[][] removeTask(String[][] tasksTable) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please select number to remove");

        //check if digt is int
        while (!scanner.hasNextInt()) {
            System.out.println(ConsoleColors.RED + "Incorrect argument passed. Please give number greater or equal 0");
            scanner.next();
        }

        int removeLine = scanner.nextInt();

        //check if digt is in table range
        while (removeLine > tasksTable.length - 1 || removeLine < 0) {
            System.out.println(ConsoleColors.RED + "Incorrect argument passed. Please give number greater or equal 0");
            removeLine = scanner.nextInt();
        }

        System.out.println(removeLine);

        String[][] tasksTableCopy = new String[tasksTable.length - 1][tasksTable[0].length];

        int count = 0;

        // if removeLine is last rows
        if (removeLine == tasksTable.length - 1) {
            for (int i = 0; i < tasksTable.length; i++) {
                for (int j = 0; j < tasksTable[0].length; j++) {
                    if (i != removeLine)
                        tasksTableCopy[i][j] = tasksTable[i][j];
                }
            }
            // if removeLine isn't last rows
        } else {
            for (int i = 0; i < tasksTable.length; i++) {
                for (int j = 0; j < tasksTable[0].length; j++) {
                    if (removeLine == i)
                        i++;
                    tasksTableCopy[count][j] = tasksTable[i][j];
                }
                count++;
            }
        }
        System.out.println(ConsoleColors.RED + "Value was successfully deleted");

        return tasksTableCopy;
    }

    public static boolean validDate(String dateValid) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setLenient(false);
        String date = dateValid;

        try {
            format.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
