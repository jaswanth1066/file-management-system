package com.csci5408project.Queries;

import frontend.SettingDB;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class createTable {
    public static SettingDB settingDB;
    public createTable(SettingDB settingDB){
        this.settingDB = settingDB;
    }
    public static void main(String[] args) throws IOException {
        System.out.println("selectedDatabase: "+ settingDB);
//        selectedDatabase.setSelectedDB(selectedDatabase.setSelectedDB());
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

//        query = "  Create table 2Playerssa_dd56 (student_id int,name string,column3 string,PRIMARY KEY student_id);".toLowerCase();
//        query = "create table 3Playerssa_dd56(asfs int,adsfas int);";
//        query = "create table 4Playerssa_dd56(asfs int,adsfas int,foreign key asfs references playersq2(asad));";
//        query = "create table 5Playerssa_dd56(asf int,safdd int,primary key asf,foreign key asfsd references asf(as));";
//        query = "create table 6Playerssa_dd56(asad int,fasgr string,primary key asad,foreign key fasgr references playersq2(asad));";
        String createTable = query.toLowerCase();

        String trimmedQuery = createTable.trim().replaceAll(" +", " ");

        final Pattern compile = Pattern.compile("create table \\w+( *)\\((\\w+\\s(string|int),( *))*(\\w+\\s(string|int))((,)( *)primary key \\w+|)((,)foreign key \\w+ references \\w+\\(\\w+\\)|)\\);");

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
        String parsedPrimaryKey = "";

        //  to find primary key in string
        final Pattern primaryKeyPattern = Pattern.compile("(?<=\\bprimary key\\s)(\\w+)");
        final Matcher getPrimaryKey = primaryKeyPattern.matcher(trimmedQuery);
        boolean primaryKeyExist = getPrimaryKey.find();

        String primaryKey = "";

        File folder = new File("bin/Databases/TestDatabase/");
        File[] listOfFiles = folder.listFiles();

        boolean flagCheckTable = checkTableExist(tableName,folder);
        if(flagCheckTable){
            System.out.println("Table Name exists");
            return false;
        }
        boolean flagFileExist = false;
        boolean referenceTableExist = false;

        final Pattern foreignKeyPattern = Pattern.compile("(?<=\\bforeign key\\s)(\\w+)");
        final Matcher getForeignKey = foreignKeyPattern.matcher(trimmedQuery);
        boolean foreignKeyExist = getForeignKey.find();

        String referenceTableName = "";
        String referenceColumn = "";
        if(foreignKeyExist){
            String foreignKey = "";
            if(foreignKeyExist){
                foreignKey = getForeignKey.group();
            }
            System.out.println("foreignKey: "+foreignKey);

//        (?<=\breferences\s)(\w+\(\w+\))
            final Pattern referencesPattern = Pattern.compile("(?<=\\breferences\\s)(\\w+\\(\\w+\\))");
            final Matcher getReferences = referencesPattern.matcher(trimmedQuery);
            boolean referenceExist = getReferences.find();
            String referencesString = "";
            if(referenceExist){
                referencesString = getReferences.group();
            }
            System.out.println("referencesString: "+referencesString);

            String[] references = referencesString.split("\\(");
            if(foreignKeyExist) {
                for (int i = 0; i < references.length; i++) {
                    referenceTableName = references[0].trim();
                    referenceColumn = references[1].trim();
                    referenceColumn = referenceColumn.replaceAll("\\)", "");
                    System.out.println("Hello World: " + columnStrings[i]);
                }
            }
            System.out.println("referenceTableName: "+referenceTableName);
            System.out.println("referenceColumn: "+referenceColumn);
        }

        for (File file : listOfFiles) {
            if (file.isFile()) {

                if(file.getName().equals(tableName+".txt")){
                    System.out.println("table: "+tableName);
                    System.out.println("Table Name exist: "+file.getName());
                    flagFileExist = true;
                    break;
                }

                if(file.getName().equals(referenceTableName+".txt")){
                    referenceTableExist = true;
                }
            }
        }

        if(flagFileExist){
            System.out.println("Table already exist");
            return false;
        }

        if(foreignKeyExist) {
            if (!referenceTableExist) {
                System.out.println("Referenced table does not exist");
                return false;
            }
        }
        if(foreignKeyExist) {
            boolean checkForeignKey = checkForeignKeyExist(referenceTableName, referenceColumn);
            if(!checkForeignKey){
                System.out.println("Foreign key reference incorrect");
                return false;
            }
        }

        String foreignKey = "";
        if(foreignKeyExist){
            foreignKey = getForeignKey.group();
        }

        if(primaryKeyExist){
            primaryKey = getPrimaryKey.group();
        } else{
            if(foreignKeyExist){
                writeToFile(tableName,false,"",columnNames,columnDataTypes,true,foreignKey);
            }else{
                writeToFile(tableName,false,"",columnNames,columnDataTypes,false,"");
            }
            return true;
        }
        parsedPrimaryKey = primaryKey;
        boolean flagForeignKey = false;
        for (int i = 0; i < columnNames.size(); i++) {
            if(parsedPrimaryKey.equals(columnNames.get(i))){
                flagPrimaryKey = true;
            }
        }
        for (int i = 0; i < columnNames.size(); i++) {
            if(foreignKey.equals(columnNames.get(i))){
                flagForeignKey = true;
            }
        }
        if(!flagPrimaryKey){
            System.out.println("ERROR OCCURRED PLEASE ENTER CORRECT PRIMARY KEY");
            return false;
        }
        if(foreignKeyExist) {
            if (!flagForeignKey) {
                System.out.println("ERROR OCCURRED PLEASE ENTER CORRECT FOREIGN KEY");
                return false;
            }
        }

        writeToFile(tableName,primaryKeyExist,parsedPrimaryKey,columnNames,columnDataTypes,flagForeignKey,foreignKey);
        return true;

    }

    public static void writeToFile(String tableName, boolean primaryKeyExist, String primaryKey, List columnNames, List columnDataTypes,boolean foreignKeyExist,String foreignKey) {

        String colHeaders = "";
        System.out.println("foreignKey: "+foreignKey);
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
            if(foreignKeyExist){
                String metaDataFK = "<~metadata~>foreignkey=" + foreignKey;
                writer.println(metaDataFK);
            }
            System.out.println("Table successfully created");
            writer.close();
        } catch ( IOException e ) {
            e.printStackTrace();
        }

    }


    public static boolean checkForeignKeyExist(String tableName,String foreignKeyColumn) throws IOException
    {
        String tableLocation = "bin/Databases/TestDatabase/"+tableName+".txt";
        BufferedReader br = new BufferedReader(new FileReader(tableLocation));
        List<String> ColumnList = new ArrayList<>();
        String line;
        String primaryKey="";
        while ((line = br.readLine()) != null)
        {
            if(line.startsWith("<~colheader~>"))
            {
                String[] columnArray = line.split("<~colheader~>");
                System.out.println("");
                ColumnList = Arrays.asList(columnArray);
            }
        }
        System.out.println("ColumnList.contains(foreignKeyColumn): "+ColumnList.contains(foreignKeyColumn));
        if(ColumnList.contains(foreignKeyColumn)){
            return true;
        }else{
            return false;
        }


    }


    public static boolean checkTableExist(String tableName,File filePath) throws IOException{
        File[] listOfFiles = filePath.listFiles();
        boolean flagFileExist = false;
        boolean referenceTableExist = false;
        for (File file : listOfFiles) {
            if (file.isFile()) {

                if(file.getName().equals(tableName+".txt")){
                    System.out.println("table: "+tableName);
                    System.out.println("Table Name exist: "+file.getName());
                    flagFileExist = true;
                    return true;
                }

//                if(file.getName().equals(referenceTableName+".txt")){
//                    referenceTableExist = true;
//                }
            }
        }
        return false;
    }


    public static void foreignKeyInformation(String query,File filePath){
//        Line 98 to 130
        final Pattern foreignKeyPattern = Pattern.compile("(?<=\\bforeign key\\s)(\\w+)");
        final Matcher getForeignKey = foreignKeyPattern.matcher(query);
        boolean foreignKeyExist = getForeignKey.find();

        String foreignKey = "";
        if(foreignKeyExist){
            foreignKey = getForeignKey.group();
        }
        System.out.println("foreignKey: "+foreignKey);

//        (?<=\breferences\s)(\w+\(\w+\))
        final Pattern referencesPattern = Pattern.compile("(?<=\\breferences\\s)(\\w+\\(\\w+\\))");
        final Matcher getReferences = referencesPattern.matcher(query);
        boolean referenceExist = getReferences.find();
        String referencesString = "";
        if(referenceExist){
            referencesString = getReferences.group();
        }
        System.out.println("referencesString: "+referencesString);

        String[] references = referencesString.split("\\(");
        String referenceColumn = "";
        String referenceTableName = "";
        if(foreignKeyExist) {
            for (int i = 0; i < references.length; i++) {
                referenceTableName = references[0].trim();
                referenceColumn = references[1].trim();
                referenceColumn = referenceColumn.replaceAll("\\)", "");
//                System.out.println("Hello World: " + columnStrings[i]);
            }
        }
        System.out.println("referenceTableName: "+referenceTableName);
        System.out.println("referenceColumn: "+referenceColumn);
    }
}
