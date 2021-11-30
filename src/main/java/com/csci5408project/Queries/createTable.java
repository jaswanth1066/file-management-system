package com.csci5408project.Queries;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class createTable {

    public static void main(String[] args){
        createTable cT =  new createTable();
        File file = new File("S:\\5408-project\\Temp-files");

        String useDatabase = "  use players2s;".toLowerCase();


        // System.out.println("Please enter query");


//        String useDBQuery = useDatabase.trim().replaceAll("\\S+", " ");
        String useDBQuery = useDatabase.trim().replaceAll(" +", " ");
        final Pattern useDBCompile = Pattern.compile("use (\\w+);");


         System.out.println("useDBQuery : " +useDBQuery);
         System.out.println("useDatabase : " +useDatabase);

        final Matcher useMatcher = useDBCompile.matcher(useDBQuery);
        boolean databaseMatchFound = useMatcher.find();

        System.out.println("matchFound: " +databaseMatchFound );
        if(!databaseMatchFound){
            // return "error";
            System.out.println("ERROR OCCURED Query incorrect LINE 43");
            System.exit(0);
        }


        final Pattern getDatabase = Pattern.compile("(?<=\\buse\\s)(\\w+)");
        final Matcher getDatabaseMatcher = getDatabase.matcher(useDBQuery);
        boolean DatabaseNameBoolean = getDatabaseMatcher.find();


        System.out.println("DatabaseNameBoolean: " + DatabaseNameBoolean);
        // System.out.println("DatabaseNameBoolean Group: " + getDatabaseMatcher.group());


        String databaseName = "";
        if(DatabaseNameBoolean){
            databaseName = getDatabaseMatcher.group();
        }else{
            // return "error";
            System.out.println("Syntax error Line 62");
            System.exit(0);
        }
        System.out.println("databaseName: " + databaseName);
//        System.exit(0);



        String[] databases = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        System.out.println(Arrays.toString(databases));
        String useDatabaseName = "";
        boolean foundDatabase = false;
        for (int i = 0; i < databases.length; i++) {
            // Get all databases i.e. folders
             System.out.println("databaseName.equals(databases[i]): "+databaseName.equals(databases[i]));
            if(databaseName.equals(databases[i])){
                useDatabaseName = databaseName;
                foundDatabase = true;
            }
        }
        if(!foundDatabase){
            System.out.println("NO Database found with this name");
            System.exit(0);
        }
        System.exit(0);
        // String createTable = "  Create table Players (student_id int,name string,column3 string);".toLowerCase();
        String createTable = "  Create table Players_dd3 (student_id int,name string,column3 string,PRIMARY KEY student_id);".toLowerCase();

        // System.out.println("Please enter query");
        String trimmedQuery = createTable.trim().replaceAll(" +", " ");
        System.out.println("cT:" +createTable.trim().replaceAll(" +", " "));


        // final Pattern compile = Pattern.compile("create table .* \\((\\w+\s(string|int),)*(\\w+\s(string|int))\\);");
        // final Pattern compile = Pattern.compile("create table .* \\((\\w+\s(string|int),)*(\\w+\s(string|int))((,)PRIMARY KEY \\w+|)\\);");
        final Pattern compile = Pattern.compile("create table \\w+(|\\s)\\((\\w+\\s(string|int),)*(\\w+\\s(string|int))((,)primary key \\w+|)\\);");

        final Matcher matcher = compile.matcher(trimmedQuery);
        boolean matchFound = matcher.find();
        System.out.println("matchFound: " +matchFound );
        System.out.println("matchFound: " +matchFound );
        if(!matchFound){
            // return "error";
            System.out.println("ERROR OCCURED Query incorrect LINE 49");
            System.exit(0);
        }

        final Pattern getTable = Pattern.compile("(?<=\\bcreate table\\s)(\\w+)");
        final Matcher getTableMatcher = getTable.matcher(trimmedQuery);
        boolean tableNameBoolean = getTableMatcher.find();
//         System.out.println("tableNameBoolean: " + tableNameBoolean);
        // System.out.println("tableNameBoolean Group: " + getTableMatcher.group());
        String tableName = getTableMatcher.group();


        File folder = new File("S:\\5408-project\\Temp-files\\" + "DB1");
        File[] listOfFiles = folder.listFiles();

        for (File selectedDatabase : listOfFiles) {
            if (selectedDatabase.isFile()) {
                System.out.println("fileName: "+selectedDatabase.getName());
                if(selectedDatabase.getName().equals(tableName+".txt")){
                    System.out.println("Error occurred table already exists.");
                    System.exit(0);
                }
            }
        }


        // regex for columns
        final Pattern compileColumns = Pattern.compile("(\\w+\\s(string|int),)*(\\w+\\s(string|int))");
        final Matcher getColumns = compileColumns.matcher(trimmedQuery);
        boolean matchFound2 = getColumns.find();
        if(!matchFound2){
             System.out.println("ERROR OCCURED Query incorrect LINE 64");
            System.exit(0);
//            return "error";
        }

        String columns = getColumns.group();

        // get all columns and their datatypes
        String[] columnStrings = columns.split(",");
        List<String> columnNames = new ArrayList<>();
        List<String> columnDataTypes = new ArrayList<>();
        for(int i = 0; i < columnStrings.length;i++){
            String temp = columnStrings[i];
            String[] columnValues = temp.split(" ");
            columnNames.add(columnValues[0]);
            columnDataTypes.add(columnValues[1]);
            System.out.println("columnName: " + columnValues[0]);
            System.out.println("columnDatatype: " + columnValues[1]);
        }
        boolean flagPrimaryKey = false;
        String parsedPrimaryKey = "student_id";

        //  to find primary key in string
        final Pattern primaryKeyPattern = Pattern.compile("(?<=\\bprimary key\\s)(\\w+)");
        final Matcher getPrimaryKey = primaryKeyPattern.matcher(trimmedQuery);
        boolean primaryKeyBoolean = getPrimaryKey.find();

        String primaryKey = getPrimaryKey.group();
        System.out.println("primaryKeyBoolean: " + primaryKeyBoolean);
        System.out.println("primaryKey Group: " + primaryKey);
        String metaDataPK = "<~metadata~> primarykey=" + primaryKey;
        parsedPrimaryKey = primaryKey;
        for (int i = 0; i < columnNames.size(); i++) {
            if(parsedPrimaryKey.equals(columnNames.get(i))){
                System.out.println("PRIMARY KEY FOUND");
                flagPrimaryKey = true;
            }
        }
        if(!flagPrimaryKey){
            // return "error";
            System.out.println("ERROR OCCURED PLEASE ENTER CORRECT PRIMARY KEY");
            System.exit(0);
        }

        // Create string to write to file
        String colHeaders = "";
        String colHeadersDatatype = "";
        for (int i = 0; i < columnNames.size(); i++) {
            colHeaders+= "<~" + columnNames.get(i) + "~>";
            colHeadersDatatype += "<~" + columnDataTypes.get(i) + "~>";
        }
        System.out.println("colHeaders: " + colHeaders);
        System.out.println("colHeadersDatatype: " + colHeadersDatatype);
        System.out.println("metaDataPK: " + metaDataPK);

        try {
            PrintWriter writer = new PrintWriter("S:\\5408-project\\Temp-files\\DB1\\"+tableName+".txt", "UTF-8");
            writer.println(colHeaders);
            writer.println(colHeadersDatatype);
            writer.println(metaDataPK);
            writer.close();
        } catch ( IOException e ) {
            e.printStackTrace();
        }


    }

}
