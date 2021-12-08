package transactions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.qos.logback.classic.db.names.ColumnName;



public class update {
	
// FLOW OF PROGRAM : 
// update table_name set column1 = value1, column2 = value2 where condition
// check table exists or not
// check if primary key column exists
// get primary keys
// check if any value duplicates the primary key data of primary key column
// Get the line which matches the constraint 
// Replace the line
// update StudentTable set StudentName = Kandarp,StudentID=123 where condition
	
// TYPES OF QUERIES TO TEST : 
// update StudentTable set StudentName=KandarpModified,StudentID=0982 where StudentName=parth
// update StudentTable set StudentName=SmitPatel,StudentID=0982 where StudentName=smit
// update StudentTable set StudentName=Smit,StudentID=789 where StudentName=smit - should throw error of primaryKey
	public static Map<Integer, String> updateTransaction(String query,Map<Integer, String> tempTransactionFile) throws IOException {

			System.out.println("Enter update query :");
			Scanner sc = new Scanner(System.in);
	        Pattern pattern = Pattern.compile("update\\s+(.*)\\s+set\\s+(.*)\\s+where\\s+(.*)");
	        Matcher matcher = pattern.matcher(query);
	        matcher.find();
			if(checkTableExistence(query) == false)
			{
				System.out.println("ERROR : Table does not exist");
			}

			if(primaryKeyColumnExistence(query,getPrimaryKey(query,tempTransactionFile)) == true)
			{
				if(dataTypeValidation(query, matcher.group(1),tempTransactionFile) == false)
				{
					System.out.println("Error : DataType validation failed");
				}
				if(checkPrimaryKeyDuplicate(getAllPrimaryKeysData(matcher.group(1),getIndexOfPrimaryKey(matcher.group(1),tempTransactionFile),tempTransactionFile)
						, query , getPrimaryKey(query,tempTransactionFile)) == true)
				{
					System.out.println("ERROR : Primary key value cant be duplicate");
				}
				else
				{
					System.out.println("No duplicate primary key");
					tempTransactionFile = updateRows(query, matcher.group(1),tempTransactionFile);
				}	
			}
			else
			{
				tempTransactionFile = updateRows(query, matcher.group(1),tempTransactionFile);
			}
			return tempTransactionFile;
		}

	
	public static boolean checkTableExistence(String query) {
        Pattern pattern = Pattern.compile("update\\s+(.*)\\s+set\\s+(.*)\\s+where\\s+(.*)");
        Matcher matcher = pattern.matcher(query);
        matcher.find();
		File tempFile = new File("bin/Databases/TestDatabase/"+ matcher.group(1)+".txt");
		boolean exists = tempFile.exists();
		return exists;
	}
	
	public static boolean primaryKeyColumnExistence(String query , String primaryKey) {
        Pattern pattern = Pattern.compile("update\\s+(.*)\\s+set\\s+(.*)\\s+where\\s+(.*)");
        Matcher matcher = pattern.matcher(query);
        matcher.find();
        List<String> columnList = new ArrayList<>();
        String queryColumnConstraints = matcher.group(2);
        String[] constraints = queryColumnConstraints.split(",");
        for(int i = 0 ; i<constraints.length;i++)
        {
        	columnList.add(constraints[i].replaceAll("\\s+", "").split("=")[0]);
        }
		return columnList.contains(primaryKey);
	}
	
	public static String getPrimaryKey(String query,Map<Integer, String> tempTransactionFile) throws IOException {
        Pattern pattern = Pattern.compile("update\\s+(.*)\\s+set\\s+(.*)\\s+where\\s+(.*)");
        Matcher matcher = pattern.matcher(query);
        matcher.find();
		String tableLocation = "bin/Databases/TestDatabase/"+matcher.group(1)+".txt";
		BufferedReader br = new BufferedReader(new FileReader(tableLocation));
		List<String> ColumnList = new ArrayList<>();
//	    String line;
	    String primaryKey="";
	    for (String line : tempTransactionFile.values())
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
	
	public static List<String> getAllPrimaryKeysData(String tableName,int indexOfPK,Map<Integer, String> tempTransactionFile) throws IOException
	{
		String tableLocation = "bin/Databases/TestDatabase/"+tableName+".txt";
		BufferedReader br = new BufferedReader(new FileReader(tableLocation));
		List<String> primaryKeyData = new ArrayList<>();
		//String line;
	    for (String line : tempTransactionFile.values())
//		    while ((line = br.readLine()) != null) 
	    {
	    	if(line.startsWith("<~row~>"))
	    	{
	    		primaryKeyData.add(line.split("<~row~>")[indexOfPK]);
	    	}
	    }
	    return primaryKeyData;
	}
	
	public static Integer getIndexOfPrimaryKey(String tableName ,Map<Integer, String> tempTransactionFile) throws IOException
	{
		String tableLocation = "bin/Databases/TestDatabase/"+tableName+".txt";
		BufferedReader br = new BufferedReader(new FileReader(tableLocation));
		List<String> ColumnList = new ArrayList<>();
	    //String line;
	    String primaryKey="";
	    for (String line : tempTransactionFile.values())
//		    while ((line = br.readLine()) != null)  
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
	
	public static boolean checkPrimaryKeyDuplicate(List<String> primaryKeyData, String query , String primaryKey)
	{
        Pattern pattern = Pattern.compile("update\\s+(.*)\\s+set\\s+(.*)\\s+where\\s+(.*)");
        Matcher matcher = pattern.matcher(query);
        matcher.find();
        List<String> columnList = new ArrayList<>();
        List<String> valueList = new ArrayList<>();
        String primaryKeyValue = "";
        String queryColumnConstraints = matcher.group(2);
        String[] constraints = queryColumnConstraints.split(",");
        for(int i = 0 ; i<constraints.length;i++)
        {
        	if(constraints[i].replaceAll("\\s+", "").split("=")[0].equals(primaryKey))
        			{
        				primaryKeyValue = constraints[i].replaceAll("\\s+", "").split("=")[1];
        				break;
        			}
        }
        if(primaryKeyData.contains(primaryKeyValue))
        {
        	return true;
        }
        else {
        	return false;
        }
	}
	
    public static void writeToFile (Map tableData , String tableName) throws IOException {
    	Writer fileWriter = new FileWriter("bin/Databases/TestDatabase/"+tableName+".txt", false);
    	String newLine = System.getProperty("line.separator");
	    for (Object value : tableData.values()) {
	    	fileWriter.write(value.toString() + newLine);
	    }
	    fileWriter.flush();
    }
    
    public static Map updateRows(String query , String tableName,Map<Integer, String> tempTransactionFile) throws IOException
    {
		String tableLocation = "bin/Databases/TestDatabase/"+tableName+".txt";
		BufferedReader br = new BufferedReader(new FileReader(tableLocation));
//		String line;
        Pattern pattern = Pattern.compile("update\\s+(.*)\\s+set\\s+(.*)\\s+where\\s+(.*)");
        Matcher matcher = pattern.matcher(query);
        matcher.find();
        String[] constraints = matcher.group(2).split(",");
        List<String> constraintColumns = new ArrayList<>();
        List<String> constraintValues = new ArrayList<>();
        String whereColumnName = matcher.group(3).split("=")[0].trim();
        String whereColumnValue = matcher.group(3).split("=")[1].trim();
        List<String> tableColumns = new ArrayList<>();
        String[] columnNames = {};
        
        for(int i=0;i<constraints.length;i++)
        {
        	constraintColumns.add(constraints[i].split("=")[0]);
        	constraintValues.add(constraints[i].split("=")[1]);
        }
		Map tableData = new HashMap();
		int lineNumber = 0;
		int rowsAffected = 0 ;
	    for (String line : tempTransactionFile.values())
//		    while ((line = br.readLine()) != null) 
	    {
	    	if(line.startsWith("<~row~>") == false)
	    	{
	    		tableData.put(lineNumber, line);
	    		lineNumber += 1;
	    	}
	    	if(line.startsWith("<~colheader~>"))
	    	{
	    		columnNames = line.split("<~colheader~>");
	    		tableColumns = Arrays.asList(columnNames);
	    	}
	    	else if (line.startsWith("<~row~>"))
	    	{
	    		String[] row = line.split("<~row~>");
	    		List<String> rowData = Arrays.asList(row);
	    		
	    		if(rowData.get(tableColumns.indexOf(whereColumnName)).equals(whereColumnValue))
	    		{
	    			String newRow = "";
	    			for(int i=1;i<columnNames.length;i++)
	    			{
	    				if(constraintColumns.contains(columnNames[i]))
	    				{
	    					newRow = newRow+"<~row~>"+constraintValues.get(constraintColumns.indexOf(columnNames[i]));
	    				}
	    				else
	    				{
	    					newRow = newRow+"<~row~>"+rowData.get(i);
	    				}
	    			}
	    			if(line.equals(newRow) == false)
	    			{
	    				rowsAffected += 1;
	    			}
	    			tableData.put(lineNumber, newRow);
	    			lineNumber += 1;
	    		}
	    		else
	    		{
	    			tableData.put(lineNumber, line);
	    			lineNumber += 1;
	    		}
	    	}
	    }
	    System.out.println("Rows affected : "+ rowsAffected);
	    return tableData;
    }
    
	public static boolean dataTypeValidation(String query , String tableName ,Map<Integer, String> tempTransactionFile) throws IOException
	{
		int validationFlag = 0;
		String tableLocation = "bin/Databases/TestDatabase/"+tableName+".txt";
		BufferedReader br = new BufferedReader(new FileReader(tableLocation));
		List<String> dataTypeList = new ArrayList<>();
		List<String> tableColumns = new ArrayList<>();
	    //String line;
        Pattern pattern = Pattern.compile("update\\s+(.*)\\s+set\\s+(.*)\\s+where\\s+(.*)");
        Matcher matcher = pattern.matcher(query);
        matcher.find();
        
        String[] constraints = matcher.group(2).split(",");
        List<String> constraintColumns = new ArrayList<>();
        List<String> constraintValues = new ArrayList<>();
        String whereColumnName = matcher.group(3).split("=")[0];
        String whereColumnValue = matcher.group(3).split("=")[1];

        String[] columnNames = {};
        
        for(int i=0;i<constraints.length;i++)
        {
        	constraintColumns.add(constraints[i].split("=")[0]);
        	constraintValues.add(constraints[i].split("=")[1]);
        }
        
	    for (String line : tempTransactionFile.values())
//		    while ((line = br.readLine()) != null)  
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
	    for(int i=0;i<constraintColumns.size();i++)
	    {
	    	String temp = dataTypeList.get(tableColumns.indexOf(constraintColumns.get(i)));
	    	System.out.println(constraintColumns.get(i) +" : "+temp+"  "+constraintValues.get(i).matches("\\d+"));
	    	if(temp.equals("int") && (constraintValues.get(i).matches("\\d+") == false))
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
