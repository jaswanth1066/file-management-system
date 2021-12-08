package transactions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// delete from StudentTable where StudentName = Parth
// delete from StudentTable where StudentID=2002
// delete from table
public class delete {

	public static Map<Integer, String> deleteTransaction(String query,Map<Integer, String> tempTransactionFile) throws IOException {

			Scanner sc = new Scanner(System.in);
	        Pattern wherePattern = Pattern.compile("delete from\\s+(.*)\\s+where\\s+(.*)");
	        Matcher matcher = wherePattern.matcher(query);
	        
	        Pattern pattern = Pattern.compile("delete from\\s+(.*)");
	        Matcher matcherWithoutWhere = pattern.matcher(query);
	        
	        matcher.find();
	        matcherWithoutWhere.find();
	        
	        if(query.split(" ").length<4)
	        {
	        	if(checkTableExistence(matcherWithoutWhere.group(1)) == false)
				{
					System.out.println("ERROR : Table does not exist");
				}
	        	else
	        	{
	        		tempTransactionFile = deleteAllContents(matcherWithoutWhere.group(1),tempTransactionFile);
	        	}
	        }
	        else
	        {
				if(checkTableExistence(matcher.group(1)) == false)
				{
					System.out.println("ERROR : Table does not exist");
				}
				tempTransactionFile = deleteRows(query, matcher.group(1),tempTransactionFile);
	        }
	        return tempTransactionFile;
		}
	
	public static boolean checkTableExistence(String tableName) {
		File tempFile = new File("bin/Databases/TestDatabase/"+ tableName+".txt");
		boolean exists = tempFile.exists();
		return exists;
	}
	
    public static Map deleteRows(String query , String tableName , Map<Integer, String> tempTransactionFile) throws IOException
    {
		String tableLocation = "bin/Databases/TestDatabase/"+tableName+".txt";
		BufferedReader br = new BufferedReader(new FileReader(tableLocation));
//		String line;
        Pattern pattern = Pattern.compile("delete from\\s+(.*)\\s+where\\s+(.*)");
        Matcher matcher = pattern.matcher(query);
        matcher.find();

        String whereColumnName = matcher.group(2).split("=")[0].trim().replace(" ", "");
        String whereColumnValue = matcher.group(2).split("=")[1].trim().replace(" ", "");
        List<String> tableColumns = new ArrayList<>();
        String[] columnNames = {};
        
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
	    				rowsAffected += 1;
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

    public static void writeToFile (Map tableData , String tableName) throws IOException {
    	Writer fileWriter = new FileWriter("bin/Databases/TestDatabase/"+tableName+".txt", false);
    	String newLine = System.getProperty("line.separator");
	    for (Object value : tableData.values()) {
	    	fileWriter.write(value.toString() + newLine);
	    }
	    fileWriter.flush();
    }
    
    public static Map<Integer, String> deleteAllContents(String tableName , Map<Integer, String> tempTransactionFile) throws FileNotFoundException
    {
    	Map<Integer, String> cleanedTable = new HashMap<>();
    	int linenumber =0;
	    for (String line : tempTransactionFile.values())
//		    while ((line = br.readLine()) != null) 
	    {
	    	if(line.startsWith("<~row~>") == false)
	    	{
	    		cleanedTable.put(linenumber, line);
	    		linenumber += 1;
	    	}
	    }
	    tempTransactionFile = cleanedTable;
    	return tempTransactionFile;
    }
}
