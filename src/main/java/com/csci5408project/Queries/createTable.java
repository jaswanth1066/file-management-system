package com.csci5408project.Queries;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class createTable {
    public static void main(String[] args) throws IOException {
        createTableQuery();
    }

    public static void createTableQuery() throws IOException {
        Scanner sc = new Scanner(System.in);
        int exitFlag = 0;
        while (exitFlag == 0) {
            System.out.println("Enter query");
            String query = sc.nextLine();
            if (parseTableQuery(query) == true) {
                exitFlag = 1;
            }

        }
    }

    public static boolean parseTableQuery(String query) throws IOException {

//        String createTable = "  Create table Players_dd56 (student_id int,name string,column3 string,PRIMARY KEY student_id);".toLowerCase();
        String createTable = query.toLowerCase();

        String trimmedQuery = createTable.trim().replaceAll(" +", " ");

        final Pattern compile = Pattern.compile("create table \\w+( *)\\((\\w+\\s(string|int),( *))*(\\w+\\s(string|int))((,)( *)primary key \\w+|)\\);");

        final Matcher matcher = compile.matcher(trimmedQuery);
        boolean matchFound = matcher.find();
        System.out.println("matchFound: " +matchFound );
        System.out.println("matchFound: " +matchFound );
        if(!matchFound){
            System.out.println("ERROR OCCURRED Query incorrect LINE 49");
            return false;
        }

        final Pattern getTable = Pattern.compile("(?<=\\bcreate table\\s)(\\w+)");
        final Matcher getTableMatcher = getTable.matcher(trimmedQuery);
        boolean tableNameBoolean = getTableMatcher.find();
        String tableName = getTableMatcher.group();
        String getColumnsString = trimmedQuery.trim().replaceAll(",( *)", ",");
        final Pattern compileColumns = Pattern.compile("(\\w+\\s(string|int),)*(( *)\\w+\\s(string|int))");
        final Matcher getColumns = compileColumns.matcher(getColumnsString);
        boolean matchFound2 = getColumns.find();
        if(!matchFound2){
             System.out.println("An error occurred");
            return false;
        }

        String columns = getColumns.group();

        String[] columnStrings = columns.split(",");
        List<String> columnNames = new ArrayList<>();
        List<String> columnDataTypes = new ArrayList<>();

        for(int i = 0; i < columnStrings.length;i++){
            String temp = columnStrings[i].trim();
            String[] columnValues = temp.split(" ");
            columnNames.add(columnValues[0]);
            columnDataTypes.add(columnValues[1]);
        }

        boolean flagPrimaryKey = false;
        String parsedPrimaryKey = "student_id";

        //  to find primary key in string
        final Pattern primaryKeyPattern = Pattern.compile("(?<=\\bprimary key\\s)(\\w+)");
        final Matcher getPrimaryKey = primaryKeyPattern.matcher(trimmedQuery);
        boolean primaryKeyExist = getPrimaryKey.find();

        String primaryKey = "";

        File folder = new File("S:\\5408-project\\Temp-files\\DB1\\");
        File[] listOfFiles = folder.listFiles();
        boolean flagFileExist = false;
        for (File file : listOfFiles) {
            if (file.isFile()) {

                if(file.getName().equals(tableName+".txt")){
                    System.out.println("table: "+tableName);
                    System.out.println("Table Name exist: "+file.getName());
                    flagFileExist = true;
                    break;
                }
            }
        }

        if(flagFileExist){
            System.out.println("Table already exist");
            return false;
        }


        if(primaryKeyExist){
            primaryKey = getPrimaryKey.group();
        } else{
            writeToFile(tableName,false,"",columnNames,columnDataTypes);
            return true;
        }

        parsedPrimaryKey = primaryKey;
        for (int i = 0; i < columnNames.size(); i++) {
            if(parsedPrimaryKey.equals(columnNames.get(i))){
                flagPrimaryKey = true;
            }
        }
        if(!flagPrimaryKey){
            System.out.println("ERROR OCCURRED PLEASE ENTER CORRECT PRIMARY KEY");
            return false;
        }

        writeToFile(tableName,primaryKeyExist,parsedPrimaryKey,columnNames,columnDataTypes);
        return true;

    }

    public static void writeToFile(String tableName, boolean primaryKeyExist, String primaryKey, List columnNames, List columnDataTypes) {

        String colHeaders = "";
        String colHeadersDatatype = "";
        for (int i = 0; i < columnNames.size(); i++) {
            colHeaders+= "<~colheader~>" + columnNames.get(i);
            colHeadersDatatype += "<~coldatatype~>" + columnDataTypes.get(i);
        }
        String table = "<~tablename~>" + tableName;

        try {
            PrintWriter writer = new PrintWriter("bin/Databases/TestDatabase/"+ tableName+".txt", "UTF-8");
            writer.println(table);
            writer.println(colHeaders);
            writer.println(colHeadersDatatype);
            if(primaryKeyExist){
                String metaDataPK = "<~metadata~>primarykey=" + primaryKey;
                writer.println(metaDataPK);
            }
            System.out.println("Table successfully created");
            writer.close();
        } catch ( IOException e ) {
            e.printStackTrace();
        }

    }
}
