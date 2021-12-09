package com.csci5408project.log_management.analysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.csci5408project.log_management.EventLogWriter;
import com.csci5408project.log_management.GeneralLogWriter;
import com.csci5408project.log_management.QueryLogWriter;

public class Analyze {

	private List<String> databases = new ArrayList<String>();
	private List<String> users = new ArrayList<String>();
	private List<String> queryTypes = new ArrayList<String>();

	public static final String FILE_PATH = "/Users/jaswanth106/git/csci5408-project-g8/Analysis.txt";

	private void detectDatabase(String subQuery) {
		databases.addAll(Arrays.asList(subQuery.split(",")));
	}

	private void detectUsers(String subQuery) {
		users.addAll(Arrays.asList(subQuery.split(",")));
	}

	private void detectQueryType(String subQuery) {
		queryTypes.addAll(Arrays.asList(subQuery.split(",")));
	}

	public String analyze(String query) {
		String[] subQueries = query.split(" ");
		int i = 0;
		while (i < subQueries.length) {
			if (subQueries[i].equalsIgnoreCase("database")) {
				detectDatabase(subQueries[i + 1]);
				i = i + 2;
				continue;
			} else if (subQueries[i].equalsIgnoreCase("user")) {
				detectUsers(subQueries[i + 1]);
				i = i + 2;
				continue;
			} else if (subQueries[i].equalsIgnoreCase("query")) {
				detectQueryType(subQueries[i + 1]);
				i = i + 2;
				continue;
			}
			i++;
		}
		return getAnalyzedLogs(query);
	}

	private String getAnalyzedLogs(String query) {
		try {
			StringBuffer stringBuffer = new StringBuffer();
			String[] file = { QueryLogWriter.filePath };
			int noOfLines = 0;
			for (String fileName : file) {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					if (line.isBlank()) {
						continue;
					}
					String[] partsOfLog = line.substring(1, line.length() - 2).split(">-<");
					boolean hasDatabaseInQuery = databases.isEmpty();
					boolean hasUserInQuery = users.isEmpty();
					boolean hasQueryTypeInQuery = queryTypes.isEmpty();
					for (String database : databases) {
						if (partsOfLog[2].contains(database)) {
							hasDatabaseInQuery = true;
						}
					}
					for (String queryType : queryTypes) {
						if (partsOfLog[3].contains(queryType)) {
							hasQueryTypeInQuery = true;
						}
					}
					for (String user : users) {
						if (partsOfLog[1].contains(user)) {
							hasUserInQuery = true;
						}
					}
					if (hasDatabaseInQuery && hasUserInQuery && hasQueryTypeInQuery) {
						stringBuffer.append(line).append(System.lineSeparator());
						noOfLines++;
					}
				}
				bufferedReader.close();
			}
			if (users.isEmpty() && databases.isEmpty() && queryTypes.isEmpty()) {
				return "The query should contain user, database or query as keyword";
			}
			writeToFile("Found : " + noOfLines + " results for your analysis with input : " + query
					+ System.lineSeparator() + stringBuffer.toString());
			return "Found : " + noOfLines + " results for your analysis with input : " + query + System.lineSeparator()
					+ "The details and precise info can be found in the file :" + FILE_PATH;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private void writeToFile(String data) {
		try {
			File file = new File(FILE_PATH);
			file.createNewFile();
			FileWriter fileWriter = new java.io.FileWriter(file, true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.append(data);
			bufferedWriter.flush();
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		Analyze analyze = new Analyze();
		System.out.println(analyze.analyze("users Jaswanth database NEW_DATABASE"));
	}

}
