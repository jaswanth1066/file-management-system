package frontend;

import Backend.SelectedDB;
import Backend.User;

public class SelectedDatabase {

    private SelectedDB selectedDB = null;
    private static SelectedDatabase instance = null;
    private SelectedDatabase() {
        // Required empty constructor.
    }

    public static SelectedDatabase getInstance() {
        if (instance == null) {
            instance = new SelectedDatabase();
        }
        return instance;
    }

    public void setSelectedDB(final SelectedDB selectedDB) {
        this.selectedDB = selectedDB;
    }

    public SelectedDB getLoggedInUser() {
        return this.selectedDB;
    }
}
