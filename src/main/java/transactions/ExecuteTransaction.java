package transactions;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

// insert into StudentTable (StudentName,StudentID,Course) values (Harsh,0001,data)
// insert into StudentTable (StudentName,StudentID,Course) values (Het,0002,Software)
// insert into StudentTable (StudentName,StudentID,Course) values (daks,0003,SDC)
// update StudentTable set StudentName=HetChanged where StudentID = 0002
// update StudentTable set StudentName=HarshChanged where StudentID = 0001
// delete from StudentTable where StudentID=0002
public class ExecuteTransaction {
public static void main(String[] args) throws IOException {
	int commitFlag = 0;
	while(commitFlag == 0)
	{
	System.out.println("Transaction starts");
	Map transactionFile = new HashMap<>();
	
	System.out.println("Select Database : use <Database name>");
	Scanner sc = new Scanner(System.in);
	String databaseName = sc.nextLine().split(" ")[1];
	
	System.out.println("Select Database : use <Table name>");
	String tableName = sc.nextLine().split(" ")[1];

	if(checkLock(tableName)==false)
	{
		String newLine = System.getProperty("line.separator");
		applyLock(tableName);
		transactionFile = getTableData(databaseName,tableName);
	    for (Object value : transactionFile.values()) {
	    	System.out.println(value.toString() + newLine);
	    }
	    if(queryProcessor(transactionFile,tableName)==false)
	    {
	    	//commitFlag = 1;
	    }
	}
	else
	{
		System.out.println("ERROR : Table is locked");
	}
	}
}

public static boolean checkLock(String tableName) throws IOException
{
	String LockManagerLocation = "bin/Databases/LockManager.txt";
	BufferedReader br = new BufferedReader(new FileReader(LockManagerLocation));
    String line;
	    while ((line = br.readLine()) != null) 
	    {
	    	if(line.equals(tableName))
	    	{
	    		return true;
	    	}
	    }
	    return false;
}

public static void applyLock(String tableName)
{
    String newLine = System.getProperty("line.separator");
    String LockManagerLocation = "bin/Databases/LockManager.txt";
	try(PrintWriter output = new PrintWriter(new FileWriter(LockManagerLocation,true))) 
    {
    	output.write(tableName+newLine);
    } 
    catch (Exception e) {}
}

public static void releaseLock(String tableName) throws IOException
{
	Map<Integer, String> locks = new HashMap<>();
	String LockManagerLocation = "bin/Databases/LockManager.txt";
	BufferedReader br = new BufferedReader(new FileReader(LockManagerLocation));
    String line;
    int lineNumber=0;
	    while ((line = br.readLine()) != null) 
	    {
	    	if(line.equals(tableName) == false)
	    	{
	    		locks.put(lineNumber, line);
	    		lineNumber += 1;
	    	}
	    }
    	Writer fileWriter = new FileWriter(LockManagerLocation, false);
    	String newLine = System.getProperty("line.separator");
	    for (Object value : locks.values()) {
	    	fileWriter.write(value.toString() + newLine);
	    }
	    fileWriter.flush();
}

public static Map<Integer, String> getTableData(String databaseName, String tableName) throws IOException
{
	Map<Integer, String> tableData = new HashMap<>();
	String tableLocation = "bin/Databases/"+databaseName+"/"+tableName+".txt";
	BufferedReader br = new BufferedReader(new FileReader(tableLocation));
    String line;
    int lineNumber=0;
    while ((line = br.readLine()) != null) 
    {
		tableData.put(lineNumber, line);
		lineNumber += 1;
    }
    return tableData;
}

public static boolean queryProcessor(Map<Integer, String> transactionFile , String tableName) throws IOException
{
	int commitFlag = 0;
	while(commitFlag == 0)
	{
		System.out.println("Enter query : or enter commit");
		Scanner sc = new Scanner(System.in);
		String query = sc.nextLine();
		String typeOfQuery = query.split(" ")[0];
		
		if(typeOfQuery.equals("insert"))
		{
			insert i = new insert();
			i.insertTransaction(query,transactionFile);
		}
		
		else if(typeOfQuery.equals("update"))
		{
			update u = new update();
			transactionFile = u.updateTransaction(query,transactionFile);
			for(String s : transactionFile.values())
			{
				System.out.println(s);
			}
		}
		
		else if(typeOfQuery.equals("delete"))
		{
			delete u = new delete();
			transactionFile = u.deleteTransaction(query,transactionFile);
		}
		
		else if(typeOfQuery.equals("commit"))
		{
	    	Writer fileWriter = new FileWriter("bin/Databases/TestDatabase/"+tableName+".txt", false);
	    	String newLine = System.getProperty("line.separator");
		    for (Object value : transactionFile.values()) {
		    	fileWriter.write(value.toString() + newLine);
		    }
		    fileWriter.flush();
		    releaseLock(tableName);
		    break;
	    }
		
		else if(typeOfQuery.equals("rollback"))
		{
		    break;
	    }
		
		}
	return false;
	}
}

