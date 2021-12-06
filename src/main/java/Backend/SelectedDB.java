package Backend;

public class SelectedDB {

    private String databaseSelected = "";

    public SelectedDB(String databaseSelected){
        this.databaseSelected = databaseSelected;
    }

    public String getDatabaseSelected() {
        return databaseSelected;
    }

    public void setDatabaseSelected(String databaseSelected) {
        this.databaseSelected = databaseSelected;
    }
}
