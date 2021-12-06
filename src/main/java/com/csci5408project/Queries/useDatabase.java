package com.csci5408project.Queries;

import frontend.Session;
import frontend.SettingDB;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import Backend.BackendSelectedDatabase;
import Backend.SelectedDB;

public class useDatabase {

    final SettingDB settingDB = SettingDB.getInstance();

//    final Session userSession = Session.getInstance();
//    selectedDB.setSelectedDB(selectedDB);
    public static void main(String[] args) throws IOException {
        useDatabaseQuery();
    }

    public static void useDatabaseQuery() throws IOException {
        Scanner sc = new Scanner(System.in);
        int exitFlag = 0;
        while (exitFlag == 0) {
            System.out.println("Enter query");
            String query = sc.nextLine();
            if (parseUseDatabaseQuery(query) == true) {
                exitFlag = 1;
            }
        }
    }

    public static boolean parseUseDatabaseQuery(String query){

        String useDBQuery = query.trim().replaceAll(" +", " ");
        final Pattern useDBCompile = Pattern.compile("use (\\w+);");

        System.out.println("useDBQuery : " +useDBQuery);;
        final Matcher useMatcher = useDBCompile.matcher(useDBQuery);
        boolean databaseMatchFound = useMatcher.find();

        if(!databaseMatchFound){
            System.out.println("ERROR OCCURRED Query incorrect LINE 43");
            return false;
        }

        final Pattern getDatabase = Pattern.compile("(?<=\\buse\\s)(\\w+)");
        final Matcher getDatabaseMatcher = getDatabase.matcher(useDBQuery);
        boolean DatabaseNameBoolean = getDatabaseMatcher.find();

        System.out.println("DatabaseNameBoolean: " + DatabaseNameBoolean);

        String databaseName = "";
        if(DatabaseNameBoolean){
            databaseName = getDatabaseMatcher.group();
        }else{
            System.out.println("Syntax error");
            return false;
        }
        System.out.println("databaseName: " + databaseName);
        final SelectedDB selectedDB = new SelectedDB(databaseName);
        settingDB.setDatabaseForUse(selectedDB);
//        userSession.createUserSession(user)

        return true;
    }
}
