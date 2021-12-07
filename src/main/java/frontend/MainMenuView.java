package frontend;

import java.io.IOException;
//Author: @Smit_Thakkar
import java.util.Scanner;

public final class MainMenuView {

	private final MainPrinter printer;
	private final Scanner scanner;
	private final Session userSession;

	public MainMenuView(final MainPrinter printer, final Scanner scanner, final Session userSession) {
		this.printer = printer;
		this.scanner = scanner;
		this.userSession = userSession;
	}

	public void displayMainMenu() throws IOException {
		printer.printScreenTitle("Main Menu");
		while (true) {
			printer.printContent("1. Execute SQL Query.");
			printer.printContent("2. Generate SQL Dump.");
			printer.printContent("3. Generate ERD.");
			printer.printContent("4. Generate Data Dictionary.");
			printer.printContent("5. View Meta Data.");
			printer.printContent("6. Import SQL Dump.");
			printer.printContent("7. Logout.");
			printer.printContent("8. Transaction.");
			printer.printContent("Select an option:");
			final String input = scanner.nextLine();

			switch (input) {
			case "1":
				

				break;
			case "2":

				break;
			case "3":

				break;
			case "4":

				break;
			case "5":

				break;
			case "6":

				break;
			case "7":
				userSession.destroyUserSession();
				return;
			case "8":
				transactions.ExecuteTransaction.main(null);
			default:
				break;
			}
		}
	}
}