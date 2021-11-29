package com.csci5408project.Queries;
import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class createDatabase {
    public static void main(String[] args){
        System.out.println("LINE 14");


        File file = new File("S:\\5408-project\\Temp-files");

        String[] databases = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });


        // System.out.println(Arrays.toString(databases));


        String createDatabase = "  Create database players2;".toLowerCase();


        // System.out.println("Please enter query");


        String trimmedQuery = createDatabase.trim().replaceAll("\\S+", " ");
        final Pattern compile = Pattern.compile("create database (\\w+);");


        // System.out.println("cT:" +createDatabase.trim().replaceAll(" +", " "));

        final Matcher matcher = compile.matcher(trimmedQuery);
        boolean matchFound = matcher.find();

        System.out.println("matchFound: " +matchFound );
        if(!matchFound){
            // return "error";
            System.out.println("ERROR OCCURED Query incorrect LINE 49");
        }

        // TO get database NAME
        final Pattern getDatabase = Pattern.compile("(?<=\\bcreate database\\s)(\\w+)");
        final Matcher getDatabaseMatcher = getDatabase.matcher(trimmedQuery);
        boolean DatabaseNameBoolean = getDatabaseMatcher.find();


        System.out.println("DatabaseNameBoolean: " + DatabaseNameBoolean);
        // System.out.println("DatabaseNameBoolean Group: " + getDatabaseMatcher.group());


        String databaseName = "";
        if(DatabaseNameBoolean){
            databaseName = getDatabaseMatcher.group();
        }else{
            // return "error";
            System.out.println("Syntax error");
        }


        for(int i = 0; i < databases.length; i++){
            if(databases[i].toLowerCase().equals(databaseName)){
                // return "error";
                System.out.println("ERROR Database exist: LINE 56");
            }
        }

        // create folder with database name

        boolean file2 = new File("S:\\5408-project\\Temp-files\\"+databaseName).mkdirs();


        System.out.println("file2: "+file2);
    }

}
