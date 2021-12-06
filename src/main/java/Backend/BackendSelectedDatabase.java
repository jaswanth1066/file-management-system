package Backend;

import frontend.SettingDB;


public final class BackendSelectedDatabase {

    private static SelectedDB dbInUse = null;

    private BackendSelectedDatabase() {

    }

    public static SelectedDB getLoggedInUser() {
        return BackendSelectedDatabase.dbInUse;
    }

    public static void setLoggedInUser(final User user) {
        BackendSelectedDatabase.dbInUse = dbInUse;
    }
}