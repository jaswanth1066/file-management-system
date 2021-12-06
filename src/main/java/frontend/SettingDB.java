package frontend;

import Backend.SelectedDB;
import Backend.User;


public final class SettingDB {

    private SelectedDB selectedDB = null;
    private static SettingDB instance = null;
    private SettingDB() {
        // Required empty constructor.
    }

    public static SettingDB getInstance() {
        if (instance == null) {
            instance = new SettingDB();
        }
        return instance;
    }

    public void setDatabaseForUse(final SelectedDB selectedDB) {
        this.selectedDB = selectedDB;
    }

    public SelectedDB getLoggedInUser() {
        return this.selectedDB;
    }


}
