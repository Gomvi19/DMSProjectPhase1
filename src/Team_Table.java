

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class Team_Table extends DBHelper {
	private final String TABLE_NAME = "Team_Table";
	public static final String Team_Name = "Team Name";
	public static final String Wins = "Wins";
	public static final String Draws = "Draws";
	public static final String Losses = "Losses";
	public static final String GoalsFor = "GoalsFor";
	public static final String GoalsAgainst = "GoalsAgainst";
	public static final String Last_5_results = "Last 5 results";

    public Team_Table(String databasePath) {
        super(databasePath);
    }

	private String prepareSQL(String fields, String whatField, String whatValue, String sortField, String sort) {
		String query = "SELECT ";
		query += fields == null ? " * FROM " + TABLE_NAME : fields + " FROM " + TABLE_NAME;
		query += whatField != null && whatValue != null ? " WHERE " + whatField + " = \"" + whatValue + "\"" : "";
		query += sort != null && sortField != null ? " order by " + sortField + " " + sort : "";
		return query;
	}

	public void insert(String TeamName, Integer Wins, Integer Draws, Integer Losses, Integer GoalsFor, Integer GoalsAgainst, String Last5results) {
		TeamName = TeamName != null ? "\"" + TeamName + "\"" : null;
		Last5results = Last5results != null ? "\"" + Last5results + "\"" : null;
		
		Object[] values_ar = {TeamName, Wins, Draws, Losses, GoalsFor, GoalsAgainst, Last5results};
		String[] fields_ar = {Team_Table.Team_Name, Team_Table.Wins, Team_Table.Draws, Team_Table.Losses, Team_Table.GoalsFor, Team_Table.GoalsAgainst, Team_Table.Last_5_results};
		String values = "", fields = "";
		for (int i = 0; i < values_ar.length; i++) {
			if (values_ar[i] != null) {
				values += values_ar[i] + ", ";
				fields += fields_ar[i] + ", ";
			}
		}
		if (!values.isEmpty()) {
			values = values.substring(0, values.length() - 2);
			fields = fields.substring(0, fields.length() - 2);
			super.execute("INSERT INTO " + TABLE_NAME + "(" + fields + ") values(" + values + ");");
		}
	}

	public void delete(String whatField, String whatValue) {
		super.execute("DELETE from " + TABLE_NAME + " where " + whatField + " = " + whatValue + ";");
	}

	public void update(String whatField, String whatValue, String whereField, String whereValue) {
		super.execute("UPDATE " + TABLE_NAME + " set " + whatField + " = \"" + whatValue + "\" where " + whereField + " = \"" + whereValue + "\";");
	}

	public ArrayList<ArrayList<Object>> select(String fields, String whatField, String whatValue, String sortField, String sort) {
		return super.executeQuery(prepareSQL(fields, whatField, whatValue, sortField, sort));
	}

	public ArrayList<ArrayList<Object>> getExecuteResult(String query) {
		return super.executeQuery(query);
	}

	public void execute(String query) {
		super.execute(query);
	}

	public DefaultTableModel selectToTable(String fields, String whatField, String whatValue, String sortField, String sort) {
		return super.executeQueryToTable(prepareSQL(fields, whatField, whatValue, sortField, sort));
	}

}