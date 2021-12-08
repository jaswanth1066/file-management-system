package com.csci5408project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class SqlDump {
    public static void generateSqlDump() throws IOException {

        Scanner sc= new Scanner(System.in);    //System.in is a standard input stream
        System.out.print("Enter Database Name : ");
        String dbName= sc.nextLine();
        System.out.print("\n");
        File file = new File("bin/Databases/" + dbName);
        try {
            Path path = Paths.get("bin/Dumpfile/" + dbName +"Dump.txt");
            StringBuilder sbFile = new StringBuilder();
            File[] allfiles = file.listFiles();
            if (allfiles != null) {

                for (File individualfile : allfiles) {

                    StringBuilder sbCreate = new StringBuilder();
                    StringBuilder sbInsert = new StringBuilder();

                    String[] colHeaders = {};
                    String[] colTypes = {};
                    String[] foreignKey = {};
                    String primaryKey = "";
                    String[] row = {};
                    List<String[]> foreignKeyList = new ArrayList<>();
                    List<String[]> rowList = new ArrayList<>();

                    //System.out.println(individualfile.getName());
                    FileReader fileReader = new FileReader(individualfile);
                    String filename = individualfile.getName().substring(0, individualfile.getName().length() - 4);
                    //System.out.println(filename);
                    sbCreate.append("CREATE TABLE ").append(filename).append(" ( ");
                    sbInsert.append("INSERT INTO ").append(filename).append(" ( ");
                    BufferedReader br = new BufferedReader(fileReader);
                    String strLine;
                    while ((strLine = br.readLine()) != null) {
                        if (strLine.contains("<~colheader~>")) {
                            colHeaders = strLine.split("<~colheader~>");
                        } else if (strLine.contains("<~colDataType~>")) {
                            colTypes = strLine.split("<~colDataType~>");
                        } else if (strLine.contains("primarykey")) {
                            primaryKey = strLine.split("=")[1];
                            //System.out.println("pk->>" + primaryKey);
                        } else if (strLine.contains("foreignkey")) {
                            foreignKey = strLine.split("=");
                            foreignKeyList.add(foreignKey);
                        } else if (strLine.contains("<~row~>")) {
                            row = strLine.split("<~row~>");
                            rowList.add(row);
                        }
                    }

                    String[] colHeaderDataType = new String[colHeaders.length];
                    for (int i = 0; i < colHeaders.length; i++) {
                        colHeaderDataType[i] = (String.format("%s %s", colHeaders[i], colTypes[i]));
                    }

                    String headerType = String.join(", ", Arrays.copyOfRange(colHeaderDataType, 1, colHeaderDataType.length));
                    String header = String.join(", ", Arrays.copyOfRange(colHeaders, 1, colHeaders.length));
                    sbInsert.append(header).append(" ) VALUES ");
                    sbCreate.append(headerType);
                    sbCreate.append(String.format(", PRIMARY KEY %s", primaryKey));

                    for (String[] str : foreignKeyList) {
                        //System.out.println(Arrays.toString(str));
                        String[] foreignKeyArray = new String[str.length];
                        for (int i = 0; i < str.length; i++) {
                            foreignKeyArray[i] = (String.format(",  FOREIGN Key %s ", str[i]));
                            //System.out.println(foreignKeyArray[i]);
                        }
                        String insertForeignKey = String.join(", ", Arrays.copyOfRange(foreignKeyArray, 1, foreignKeyArray.length));
                        sbCreate.append(insertForeignKey);
                    }
                    sbCreate.append(");\n");
                    List<String> rowListArray = new ArrayList<>();
                    for (String[] str : rowList) {
                        String strjoin = String.format("(%s)", String.join(", ", Arrays.copyOfRange(str, 1, str.length)));
                        rowListArray.add(strjoin);
                    }
                    sbInsert.append(String.join(", ", rowListArray)).append("\n");
                    System.out.println(sbCreate);
                    System.out.println(sbInsert);
                    sbFile.append(sbCreate).append("\n").append(sbInsert).append("\n");

                }
                Files.write(path, sbFile.toString().getBytes());
            }
            else{
                System.out.println("Please Create table First");
            }
            //Files.write(path, sbCreate.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        generateSqlDump();
    }

}
