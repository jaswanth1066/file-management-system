package com.csci5408project.Queries;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//Author
// Kandarp Parikh
// B00873863
public class insert {
	//Check the existence of table
	// Get metadata
	//Get primary key column
	//Get all the primary keys
	//check if the data contains duplicate primary key
	
	//INSERT INTO table_name (column1, column2, column3, ...)
	//VALUES (value1, value2, value3, ...);
	
	//insert into StudentTable (StudentName,StudentID,Course) values (Kandarp,B00873863,data)
	public static void main(String[] args) throws IOException {
		int flag = 0;
		while(flag == 0)
		{
		System.out.println("Enter Query : ");
		Scanner sc = new Scanner(System.in);
		
		//query validation
		
		//query parsing
		String query = sc.nextLine();
		String[] queryArray = query.split(" ");
		String tableName = queryArray[2];
		if(checkFileExistence(tableName) == true) 
			{
				int indexOfPK=getIndexOfPrimaryKey(tableName);
				String primaryKey = getPrimaryKey(tableName);
				List<String> primaryKeysData = getAllPrimaryKeysData(tableName,indexOfPK);
				if(checkDuplicatePrimaryKey(primaryKeysData,query,primaryKey) == true) {
					
				}
				else {
					System.out.println("No duplicate primary key");
					insertData(tableName,query);
					
				}
			}
		else {
			System.out.println("Table does not exists");
		}
		}
		
	}
	
	public static boolean checkFileExistence(String tableName) {
		File tempFile = new File("bin/Databases/TestDatabase/"+tableName+".txt");
		boolean exists = tempFile.exists();
		return exists;
	}
	
	public static String getPrimaryKey(String tableName) throws IOException {
		String tableLocation = "bin/Databases/TestDatabase/"+tableName+".txt";
		BufferedReader br = new BufferedReader(new FileReader(tableLocation));
		List<String> ColumnList = new ArrayList<>();
	    String line;
	    String primaryKey="";
		    while ((line = br.readLine()) != null) 
		    {
		    	if(line.startsWith("<~metadata~>primarykey"))
		    	{
		    		primaryKey = line.split("=")[1];
		    		System.out.println("Primary Key of table : "+primaryKey);
		    	}
		    }
		    return primaryKey;
	}
	public static Integer getIndexOfPrimaryKey(String tableName) throws IOException
	{
		String tableLocation = "bin/Databases/TestDatabase/"+tableName+".txt";
		BufferedReader br = new BufferedReader(new FileReader(tableLocation));
		List<String> ColumnList = new ArrayList<>();
	    String line;
	    String primaryKey="";
		    while ((line = br.readLine()) != null) 
		    {
		    	if(line.startsWith("<~metadata~>primarykey"))
		    	{
		    		primaryKey = line.split("=")[1];
		    	}
		    	if(line.startsWith("<~colheader~>"))
		    	{
		    		String[] columnArray = line.split("<~colheader~>");
		    		ColumnList = Arrays.asList(columnArray);
		    	}
		    }
		return ColumnList.indexOf(primaryKey);
	}

	public static List<String> getAllPrimaryKeysData(String tableName,int indexOfPK) throws IOException
	{
		String tableLocation = "bin/Databases/TestDatabase/"+tableName+".txt";
		BufferedReader br = new BufferedReader(new FileReader(tableLocation));
		List<String> primaryKeyData = new ArrayList<>();
		String line;
	    while ((line = br.readLine()) != null) 
	    {
	    	if(line.startsWith("<~row~>"))
	    	{
	    		primaryKeyData.add(line.split("<~row~>")[indexOfPK]);
	    	}
	    }
	    return primaryKeyData;
	}
	
	public static boolean checkDuplicatePrimaryKey(List<String> primaryKeysData , String query , String primaryKey)
	{
        Pattern pattern = Pattern.compile("insert into\\s+(.*?)\\s+\\((.*?)\\)\\s+values\\s+\\((.*?)\\)");
        Matcher matcher = pattern.matcher(query);
        matcher.find();

        String tableName = matcher.group(1);
        String[] columnName = matcher.group(2)
                .replaceAll("\\s+", "")
                .split(",");
        List<String> collist = Arrays.asList(columnName);
        
        String[] values = matcher.group(3)
               // .replaceAll("\\s+", "")
                .split(",");
        int primaryKeyIndex = collist.indexOf(primaryKey);
        if(primaryKeyIndex == -1)
        {
        	System.out.println("Primary Key Column cant be empty");
        	return true;
        }
        if(columnName.length != values.length)
        {
        	System.out.println("ERROR : Number of column names specified should match the number of values in the query");
        	return true;
        }
        List<String> insertValues = Arrays.asList(values);
        if(primaryKeysData.contains(insertValues.get(primaryKeyIndex)))
        {
        	System.out.println("Duplicate Primary Key");
        	return true;
        }
        else {
        	return false;
        }
        
	}
	
	public static void insertData(String tableName, String query) throws IOException
	{
        Pattern pattern = Pattern.compile("insert into\\s+(.*?)\\s+\\((.*?)\\)\\s+values\\s+\\((.*?)\\)");
        Matcher matcher = pattern.matcher(query);
        matcher.find();
     
        String[] columnName = matcher.group(2)
                .replaceAll("\\s+", "")
                .split(",");
        List<String> collist = Arrays.asList(columnName);
        
        String[] values = matcher.group(3)
                //.replaceAll("\\s+", "")
                .split(",");
        
        Map<String, String> columnRowsMap = new HashMap();
        for(int i = 0 ; i<collist.size() ; i++)
        {
        	columnRowsMap.put(columnName[i],values[i]);
        }
        
		String tableLocation = "bin/Databases/TestDatabase/"+tableName+".txt";
		BufferedReader br = new BufferedReader(new FileReader(tableLocation));
		List<String> ColumnList = new ArrayList<>();
	    String line;
	    while ((line = br.readLine()) != null) 
	    {
	    	if(line.startsWith("<~colheader~>"))
	    	{
	    		String[] columnArray = line.split("<~colheader~>");
	    		ColumnList = Arrays.asList(columnArray);
	    	}
	    }
	    
	    String insertString = "";
	    String newLine = System.getProperty("line.separator");
	    for(int i=1;i<ColumnList.size();i++)
	    {
	    	insertString = insertString + "<~row~>"+columnRowsMap.get(ColumnList.get(i));
	    }
	    try(PrintWriter output = new PrintWriter(new FileWriter(tableLocation,true))) 
	    {
	    	output.write(newLine + insertString);
	    } 
	    catch (Exception e) {}
	    
	}
}
