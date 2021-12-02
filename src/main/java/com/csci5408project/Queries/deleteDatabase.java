package com.csci5408project.Queries;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class deleteDatabase {

    public static void main(String[] args) throws IOException {
        deleteDatabaseQuery();
    }

    public static void deleteDatabaseQuery() throws IOException {
        Scanner sc = new Scanner(System.in);
        int exitFlag = 0;
        while (exitFlag == 0) {
            System.out.println("Enter query");
            String query = sc.nextLine();
            if (parseDeleteDatabaseQuery(query) == true) {
                exitFlag = 1;
            }

        }
    }

    public static boolean parseDeleteDatabaseQuery(String query){

        String deleteDatabase = query.toLowerCase();
        String trimmedQuery = deleteDatabase.trim().replaceAll(" +", " ");

        final Pattern compile = Pattern.compile("drop database (\\w+);");
        final Matcher matcher = compile.matcher(trimmedQuery);
        boolean matchFound = matcher.find();

        if(!matchFound){
            System.out.println("ERROR OCCURED Query incorrect LINE 43");
            return false;
        }

        final Pattern getDatabase = Pattern.compile("(?<=\\bdrop database\\s)(\\w+)");
        final Matcher getDatabaseMatcher = getDatabase.matcher(trimmedQuery);
        boolean DBNameBoolean = getDatabaseMatcher.find();

        String databaseName = "";
        if(DBNameBoolean){
            databaseName = getDatabaseMatcher.group();
        }else{
            System.out.println("Syntax error Line 62");
            return false;
        }


        File file = new File("S:\\5408-project\\Temp-files");
        String[] databases = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        boolean flagFolderExist = false;
        for(int i = 0; i < databases.length; i++){
            if(databases[i].toLowerCase().equals(databaseName)){
                flagFolderExist = true;
                break;
            }
        }
        if(!flagFolderExist){
            System.out.println("Database does not exist. ");
            return false;
        }
        File databaseFolder = new File("S:\\5408-project\\Temp-files\\" +databaseName);
        try {
            dropDatabase(databaseFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void dropDatabase(File file) throws IOException {
        if (file.isDirectory()) {
            File[] entries = file.listFiles();
            if (entries != null) {
                for (File entry : entries) {
                    dropDatabase(entry);
                    System.out.println("Database deleted successfully");
                }
            }
        }
        if (!file.delete()) {
            throw new IOException("Failed to delete " + file);
        }
    }

}
