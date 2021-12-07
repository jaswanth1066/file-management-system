package transactions;

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
// Author
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
	public static Map<Integer, String> insertTransaction(String query , Map<Integer, String> tempTransactionFile) throws IOException {

		String[] queryArray = query.split(" ");
		String tableName = queryArray[2];
		if(checkFileExistence(tableName) == true) 
			{
				if(dataTypeValidation(query, tableName , tempTransactionFile) == false)
					{
						System.out.println("Error : DataType validation failed");
					}
				else
					{
					int indexOfPK=getIndexOfPrimaryKey(tableName  , tempTransactionFile);
					String primaryKey = getPrimaryKey(tableName , tempTransactionFile);
					List<String> primaryKeysData = getAllPrimaryKeysData(tableName,indexOfPK , tempTransactionFile);
					if(checkDuplicatePrimaryKey(primaryKeysData,query,primaryKey) == true)
					{
						
					}
					else
					{
						System.out.println("No duplicate primary key");
						tempTransactionFile = insertData(tableName,query,tempTransactionFile);
					}
					}
			}
		else
		{
			System.out.println("ERROR : Table does not exists");
		}
		return tempTransactionFile;
	}
		
	
	public static boolean checkFileExistence(String tableName) {
		File tempFile = new File("bin/Databases/TestDatabase/"+tableName+".txt");
		boolean exists = tempFile.exists();
		return exists;
	}
	
	public static String getPrimaryKey(String tableName  , Map<Integer, String> transactionFile) throws IOException {
		String tableLocation = "bin/Databases/TestDatabase/"+tableName+".txt";
		BufferedReader br = new BufferedReader(new FileReader(tableLocation));
		List<String> ColumnList = new ArrayList<>();
//	    String line;
	    String primaryKey="";
	    for (String line : transactionFile.values())
//		    while ((line = br.readLine()) != null) 
		    {
		    	if(line.startsWith("<~metadata~>primarykey"))
		    	{
		    		primaryKey = line.split("=")[1];
		    		System.out.println("Primary Key of table : "+primaryKey);
		    	}
		    }
		    return primaryKey;
	}
	public static Integer getIndexOfPrimaryKey(String tableName  , Map<Integer, String> transactionFile) throws IOException
	{
		String tableLocation = "bin/Databases/TestDatabase/"+tableName+".txt";
		BufferedReader br = new BufferedReader(new FileReader(tableLocation));
		List<String> ColumnList = new ArrayList<>();
	    //String line;
	    String primaryKey="";
	    for (String line : transactionFile.values())
	//	    while ((line = br.readLine()) != null) 
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

	public static List<String> getAllPrimaryKeysData(String tableName,int indexOfPK, Map<Integer, String> transactionFile) throws IOException
	{
		String tableLocation = "bin/Databases/TestDatabase/"+tableName+".txt";
		BufferedReader br = new BufferedReader(new FileReader(tableLocation));
		List<String> primaryKeyData = new ArrayList<>();
		//String line;
	    for (String line : transactionFile.values())
	//	    while ((line = br.readLine()) != null)
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
        	System.out.println("ERROR : Primary Key Column cant be empty");
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
        	System.out.println("ERROR : Duplicate Primary Key");
        	return true;
        }
        else {
        	return false;
        }
        
	}
	
	public static Map<Integer, String> insertData(String tableName, String query, Map<Integer, String> transactionFile) throws IOException
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
	    //String line;
	    for (String line : transactionFile.values())
	//	    while ((line = br.readLine()) != null)
	    {
	    	if(line.startsWith("<~colheader~>"))
	    	{
	    		String[] columnArray = line.split("<~colheader~>");
	    		ColumnList = Arrays.asList(columnArray);
	    	}
	    }
	    
	    String insertString = "";
	    String newLine = System.getProperty("line.separator");
	    int linenumber = 0;
	    for (int line : transactionFile.keySet())
	    {
	    	linenumber = line;
	    }
	    for(int i=1;i<ColumnList.size();i++)
	    {
	    	insertString = insertString + "<~row~>"+columnRowsMap.get(ColumnList.get(i));
	    	transactionFile.put(linenumber+1, insertString);
	    }
//	    try(PrintWriter output = new PrintWriter(new FileWriter(tableLocation,true))) 
//	    {
//	    	output.write(newLine + insertString);
//	    } 
//	    catch (Exception e) {}
	    return transactionFile;
	}
	
	public static boolean dataTypeValidation(String query , String tableName , Map<Integer, String> transactionFile) throws IOException
	{
		int validationFlag = 0;
		String tableLocation = "bin/Databases/TestDatabase/"+tableName+".txt";
		BufferedReader br = new BufferedReader(new FileReader(tableLocation));
		List<String> dataTypeList = new ArrayList<>();
		List<String> tableColumns = new ArrayList<>();
	   // String line;
        Pattern pattern = Pattern.compile("insert into\\s+(.*?)\\s+\\((.*?)\\)\\s+values\\s+\\((.*?)\\)");
        Matcher matcher = pattern.matcher(query);
        matcher.find();

        String[] queryColumnName = matcher.group(2).replaceAll("\\s+", "").split(",");
        List<String> querycolumnlist = Arrays.asList(queryColumnName);
        
        String[] values = matcher.group(3).split(",");
        for (String line : transactionFile.values())
//	    while ((line = br.readLine()) != null) 
	    {
	    	if(line.startsWith("<~coldatatype~>"))
	    	{
	    		String[] dataTypes = line.split("<~coldatatype~>");
	    		dataTypeList = Arrays.asList(dataTypes);
	    	}
	    	
	    	if(line.startsWith("<~colheader~>"))
	    	{
	    		String[] columnHeader = line.split("<~colheader~>");
	    		tableColumns = Arrays.asList(columnHeader);
	    	}
	    }
	    for(int i=0;i<querycolumnlist.size();i++)
	    {
	    	String temp = dataTypeList.get(tableColumns.indexOf(querycolumnlist.get(i)));
//	    	System.out.println(querycolumnlist.get(i) +" : "+temp);
	    	if(temp.equals("int") && (values[i].matches("\\d+") == false))
	    	{
	    		validationFlag = 1;
	    	}
	    }
	    if(validationFlag == 1)
	    {
	    	return false;
	    }
	    else
	    {
	    	return true;
	    }
	}
}
