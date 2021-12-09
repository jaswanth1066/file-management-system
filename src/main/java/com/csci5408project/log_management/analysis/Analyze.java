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

public class Analyze {

	private List<String> databases = new ArrayList<String>();
	private List<String> users = new ArrayList<String>();
	private List<String> queryTypes = new ArrayList<String>();

	public static final String FILE_PATH = "bin/Logs/Analysis.txt";

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
			} else if (subQueries[i].equalsIgnoreCase("users")) {
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
		return getAnalyzedLogs();
	}

	private String getAnalyzedLogs() {
		try {
			StringBuffer stringBuffer = new StringBuffer();
			String[] file = { EventLogWriter.filePath, EventLogWriter.filePath, EventLogWriter.filePath };
			for (String fileName : file) {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					for (String database : databases) {
						if (line.contains("<" + database + ">")) {
							stringBuffer.append(line).append(System.lineSeparator());
						}
					}
					for (String queryType : queryTypes) {
						if (line.contains("<" + queryType + ">")) {
							stringBuffer.append(line).append(System.lineSeparator());
						}
					}
					for (String user : users) {
						if (line.contains("<" + user + ">")) {
							stringBuffer.append(line).append(System.lineSeparator());
						}
					}
				}
				bufferedReader.close();
			}
			if (users.isEmpty() && databases.isEmpty() && queryTypes.isEmpty()) {
				return "The query should contain user, database or query_type as keyword";
			}
			writeToFile(stringBuffer.toString());
			return stringBuffer.toString();
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

}
