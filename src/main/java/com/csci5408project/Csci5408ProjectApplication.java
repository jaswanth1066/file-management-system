package com.csci5408project;
//Author: @Smit_Thakkar
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;


import Backend.User;
import frontend.MainPrinter;
import frontend.Session;
import frontend.UserLoginView;
import frontend.UserRegistrationView;
import frontend.MainMenuView;

@SpringBootApplication
public class Csci5408ProjectApplication {

	private void folderStructureSetup(final MainPrinter printer) {
		final File outputFolder = new File("./src/main/java/output");
		final File dataDictionaryFolder = new File(outputFolder + "/data_dictionary");
		final File erdFilesFolder = new File(outputFolder + "/erd_files");
		final File logsFolder = new File(outputFolder + "/logs");
		final File metaDataFolder = new File(outputFolder + "/metadata");
		final File sqlDumpsFolder = new File(outputFolder + "/sql_dumps");
		final File databaseServerFolder = new File("./src/main/java/database/database_server");
		final File databaseInMemoryFolder = new File("./src/main/java/database/database_in_memory");
		if (!outputFolder.exists()) {
			if (outputFolder.mkdirs()) {
				printer.printContent(outputFolder.getName() + " directory created!");
			}
		}
		if (!dataDictionaryFolder.exists()) {
			if (dataDictionaryFolder.mkdirs()) {
				printer.printContent(dataDictionaryFolder.getName() + " directory created!");
			}
		}
		if (!erdFilesFolder.exists()) {
			if (erdFilesFolder.mkdirs()) {
				printer.printContent(erdFilesFolder.getName() + " directory created!");
			}
		}
		if (!logsFolder.exists()) {
			if (logsFolder.mkdirs()) {
				printer.printContent(logsFolder.getName() + " directory created!");
			}
		}
		if (!metaDataFolder.exists()) {
			if (metaDataFolder.mkdirs()) {
				printer.printContent(metaDataFolder.getName() + " directory created!");
			}
		}
		if (!sqlDumpsFolder.exists()) {
			if (sqlDumpsFolder.mkdirs()) {
				printer.printContent(sqlDumpsFolder.getName() + " directory created!");
			}
		}
		if (!databaseServerFolder.exists()) {
			if (databaseServerFolder.mkdirs()) {
				printer.printContent(sqlDumpsFolder.getName() + " directory created!");
			}
		}
		if (!databaseInMemoryFolder.exists()) {
			if (databaseInMemoryFolder.mkdirs()) {
				printer.printContent(databaseInMemoryFolder.getName() + " directory created!");
			}
		}
	}

	private void userRegistration(final MainPrinter printer,
								  final Scanner scanner) {
		final UserRegistrationView userRegistrationView =
				new UserRegistrationView(printer, scanner);
		userRegistrationView.performUserRegistration();
	}

	private User userLogin(final MainPrinter printer,
						   final Scanner scanner) {
		final UserLoginView userLoginView =
				new UserLoginView(printer, scanner);
		return userLoginView.performLogin();
	}

	public static void main(String[] args) throws IOException {
		final Csci5408ProjectApplication entry = new Csci5408ProjectApplication();
		final MainPrinter printer = MainPrinter.getInstance();
		final Scanner scanner = new Scanner(System.in);
		final Session userSession = Session.getInstance();
		entry.folderStructureSetup(printer);

		printer.printScreenTitle("Welcome to CSCI5408 Project");

		while (true) {
			printer.printContent("1. User registration.");
			printer.printContent("2. User login.");
			printer.printContent("3. Exit.");
			printer.printContent("Select an option:");
			final String input = scanner.nextLine();

			switch (input) {
				case "1":
					entry.userRegistration(printer, scanner);
					break;
				case "2":
					final User user = entry.userLogin(printer, scanner);
					if (user != null) {
						userSession.createUserSession(user);
						final MainMenuView mainMenuView = new MainMenuView(printer, scanner, userSession);
						mainMenuView.displayMainMenu();
					}
					break;
				case "3":
					userSession.destroyUserSession();
					System.exit(0);
				default:
					break;
			}
		}
	}
}