package com.csci5408project.Queries;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class deleteTable {
    public static void main(String[] args) throws IOException {
        deleteTableQuery();
    }

    public static void deleteTableQuery() throws IOException {
        Scanner sc = new Scanner(System.in);
        int exitFlag = 0;
        while (exitFlag == 0) {
            System.out.println("Enter query");
            String query = sc.nextLine();
            if (parseDeleteTableQuery(query) == true) {
                exitFlag = 1;
            }

        }
    }

    public static boolean parseDeleteTableQuery(String query){

        String deleteTable = query.toLowerCase();
        String trimmedQuery = deleteTable.trim().replaceAll(" +", " ");

        final Pattern compile = Pattern.compile("drop table (\\w+);");
        final Matcher matcher = compile.matcher(trimmedQuery);
        boolean matchFound = matcher.find();

        System.out.println("matchFound: " +matchFound );
        if(!matchFound){
            System.out.println("ERROR OCCURED Query incorrect LINE 43");
            return false;
        }

        final Pattern getTable = Pattern.compile("(?<=\\bdrop table\\s)(\\w+)");
        final Matcher getTableMatcher = getTable.matcher(trimmedQuery);
        boolean TableNameBoolean = getTableMatcher.find();

        String tableName = "";
        if(TableNameBoolean){
            tableName = getTableMatcher.group();
        }else{
            System.out.println("Syntax error Line 62");
            return false;
        }

        System.out.println("tableName: "+tableName);

        File folder = new File("S:\\5408-project\\Temp-files\\DB1\\");
        File[] listOfFiles = folder.listFiles();
        boolean flagFileExist = false;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                if(file.getName().equals(tableName+".txt")){
                    flagFileExist = true;
                    break;
                }
            }
        }
        if(!flagFileExist){
            System.out.println("Table does not exist. ");
            return false;
        }

        try {
            File file= new File("S:\\5408-project\\Temp-files\\DB1\\"+tableName+".txt");
            if(file.delete()){
                System.out.println("File deleted");
                return true;
            }else {
                System.out.println("could not delete table");
                return false;
            }
        } catch (Exception e){
            System.out.println("Exception occurred ");

        }
        return true;
    }

}
