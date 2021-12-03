package com.csci5408project.log_management;

import java.util.Map;

public class LogWriterService {

	public static final String QUERY_EXECUTION_TIME_KEY = "QUERY_EXECUTION_TIME";
	public static final String DATABASE_STATE_KEY = "DATABASE_KEY";

	public static final String EXECUTED_QUERY_KEY = "EXECUTED_QUERY";

	public static final String DATABASE_CHANGES_KEY = "DATABASE_CHANGES";
	public static final String TRANSACTIONS_KEY = "TRANSACTIONS";
	public static final String DATABASE_CRASH_KEY = "DATABASE_CRASH";

	private static LogWriterService instance = null;

	public static LogWriterService getInstance() {
		if (instance == null) {
			instance = new LogWriterService();
		}
		return instance;
	}

	private LogWriterService() {
		start();
	}
	
	public void start() {
		GeneralLogWriter.getInstance().start();
		EventLogWriter.getInstance().start();
		QueryLogWriter.getInstance().start();
	}

	public void write(Map<String, String> informationMap) {
		if (informationMap.containsKey(QUERY_EXECUTION_TIME_KEY) && informationMap.containsKey(DATABASE_STATE_KEY)) {
			GeneralLogWriter.getInstance().writeGeneralLog(informationMap);
		}
		if (informationMap.containsKey(EXECUTED_QUERY_KEY)) {
			QueryLogWriter.getInstance().writeQueryLog(informationMap);
		}
		if (informationMap.containsKey(DATABASE_CHANGES_KEY) || informationMap.containsKey(TRANSACTIONS_KEY)
				|| informationMap.containsKey(DATABASE_CRASH_KEY)) {
			EventLogWriter.getInstance().writeEventLog(informationMap);
		}

	}
	
	public void stop() {
		GeneralLogWriter.getInstance().stop();
		EventLogWriter.getInstance().stop();
		QueryLogWriter.getInstance().stop();
	}
}
