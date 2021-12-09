package com.csci5408project.log_management;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Map;

import Backend.SetDatabase;
import frontend.Session;

public class GeneralLogWriter {

//	private String filePath = "/Users/jaswanth106/Desktop/GeneralLogs.txt";
	public static final String filePath = "/Users/jaswanth106/git/csci5408-project-g8/GeneralLogs.txt";

	private File file;
	private java.io.FileWriter fileWriter;
	private BufferedWriter bufferedWriter;

	private static GeneralLogWriter instance = null;

	public static GeneralLogWriter getInstance() {
		if (instance == null) {
			instance = new GeneralLogWriter();
		}
		return instance;
	}

	private GeneralLogWriter() {
	}

	public void start() {
		try {
			file = new File(GeneralLogWriter.filePath);
			file.createNewFile();
			this.fileWriter = new java.io.FileWriter(this.file, true);
			this.bufferedWriter = new BufferedWriter(this.fileWriter);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean writeGeneralLog(Map<String, String> information) {
		try {
			Calendar calendar = Calendar.getInstance();
			StringBuffer sb = new StringBuffer();
			sb.append("<").append(calendar.getTime()).append(">-");
			sb.append("<").append(Session.getInstance().getLoggedInUser().getUserName()).append(">-");
			sb.append("<").append(SetDatabase.getInstance().getDb()).append(">-");
			// sb.append("<").append("Jaswanth").append(">-");
			sb.append("<").append("Time taken for query to extecute is : ")
					.append(information.get(LogWriterService.GENRAL_LOG_QUERY_EXECUTION_TIME_KEY)).append("ms. Database state is: ")
					.append(information.get(LogWriterService.GENRAL_LOG_DATABASE_STATE_KEY)).append(">");
			this.bufferedWriter.newLine();
			this.bufferedWriter.append(sb.toString());
			this.bufferedWriter.flush();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void stop() {
		try {
			this.bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
