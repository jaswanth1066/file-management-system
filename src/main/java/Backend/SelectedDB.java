package Backend;

public class SelectedDB {

    private String databaseSelected = "";

    public SelectedDB(final String databaseSelected){
        this.databaseSelected = databaseSelected;
    }

    public String getDatabaseSelected() {
        return databaseSelected;
    }
}
