package com.csci5408project.Queries;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class createDatabase {
    public static void main(String[] args) throws IOException {
        createDatabaseQuery();
    }
    public static void createDatabaseQuery() throws IOException {
        Scanner sc = new Scanner(System.in);
        int exitFlag = 0;
        while (exitFlag == 0) {
            System.out.println("Enter query");
            String query = sc.nextLine();
            if (parseDatabaseQuery(query) == true) {
                exitFlag = 1;
            }

        }
    }

    public static boolean parseDatabaseQuery(String query) throws IOException {
        String createDatabase = query.toLowerCase();
        String trimmedQuery = createDatabase.trim().replaceAll(" +", " ");
        final Pattern compile = Pattern.compile("create database (\\w+);");

        final Matcher matcher = compile.matcher(trimmedQuery);
        boolean matchFound = matcher.find();

        if(!matchFound){
            System.out.println("Error occurred query incorrect");
            return false;
        }

        // TO get database NAME
        final Pattern getDatabase = Pattern.compile("(?<=\\bcreate database\\s)(\\w+)");
        final Matcher getDatabaseMatcher = getDatabase.matcher(trimmedQuery);
        boolean DatabaseNameBoolean = getDatabaseMatcher.find();

        String databaseName = "";
        if(DatabaseNameBoolean){
            databaseName = getDatabaseMatcher.group();
        }else{
            System.out.println("Error occurred query incorrect");
            return false;
        }


        File file = new File("bin/Databases");

        String[] databases = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });

        for(int i = 0; i < databases.length; i++){
            if(databases[i].toLowerCase().equals(databaseName)){
                System.out.println("Error occurred Database exist");
                return false;
            }
        }

        boolean file2 = new File("bin/Databases/"+databaseName).mkdirs();

        if(file2){
            System.out.println("Database created");
            return true;
        }else{
            System.out.println("Problem creating Database");
            return false;
        }

    }
}
