//Victor Gomez, CEN3024C, 10/26/2025
//MainFrame class
//Handles the graphical user interface for the Team Management System.
//Allows the user to add, load, remove, update, and get stats of Team objects through interactive components

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class MainFrame extends JFrame {
    // GUI components
    private JTextField tfTeamName;
    private JTextField tfWins;
    private JTextField tfDraws;
    private JTextField tfLosses;
    private JTextField tfGoalsFor;
    private JTextField tfGoalsAgainst;
    private JTextField tfLast5;
    private JButton btmAdd;
    private JPanel mainPanel;
    private JTable DisplayTable;
    private JScrollPane DisplayTbale;
    private JButton btmRemove;
    private JButton btmUpdate;
    private JButton btmStats;
    private JButton btmDB;

    // Logic and data handling
    private TeamManager teamManager= new TeamManager();
    private DefaultTableModel tableModel;
    private int editingRowIndex = -1;
    private StatsCalculator stats = new StatsCalculator();
    private String databasePath = null;
    private Team_Table teamTable;
    ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();
    private int selectedID;

    //Constructor
    //Initializes the GUI components, sets up button listeners, and configures table columns
    public  MainFrame()
    {
        setTitle("Team Manager");
        setContentPane(mainPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800,600);
        setVisible(true);

        // Define table columns
        String[] columnNames = {
                "ID","Name", "Wins", "Draws", "Losses", "Goals For", "Goals Against", "Last 5"
        };

        // Initialize table model with no rows but visible columns
        tableModel = new DefaultTableModel(columnNames, 0);

        // Wrap JTable inside a scroll pane (so headers show correctly)
        DisplayTable.setModel(tableModel);
        DisplayTable.removeColumn(DisplayTable.getColumn("ID"));
        // Make frame visible at the end
        setVisible(true);

        //------Add Team Button-------
        //manually add a team or updates an existing team
        btmAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (teamTable == null) {
                    JOptionPane.showMessageDialog(
                            MainFrame.this,
                            "Please select a database before performing this action.",
                            "Database Not Connected",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }
                //get data from text fields and validate data
                String name = tfTeamName.getText();
                    if (name.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Team name cannot be empty!");
                        return;
                    }
                    int wins = getPositiveIntFromField(tfWins, "Wins");
                    int draws = getPositiveIntFromField(tfDraws, "Draws");
                    int losses = getPositiveIntFromField(tfLosses, "Losses");
                    int goalsFor = getPositiveIntFromField(tfGoalsFor, "Goals For");
                    int goalsAgainst = getPositiveIntFromField(tfGoalsAgainst, "Goals Against");
                    String last5Str = tfLast5.getText().trim().toUpperCase();
                    //verify only 5 results are entered
                    if (last5Str.length() != 5) {
                        JOptionPane.showMessageDialog(null, "Last 5 results must be exactly 5 characters (W/D/L).");
                        tfLast5.setText("");
                        return;
                    }
                    //Convert string of results into a character array
                    List<Character> last5 = new ArrayList<>();
                    for (char c : last5Str.toCharArray()) {
                        if (c != 'W' && c != 'D' && c != 'L') {
                            JOptionPane.showMessageDialog(null, "Invalid result in Last 5 (use only W, D, or L).");
                            tfLast5.setText("");
                            return;
                        }
                        last5.add(c);
                    }
                    last5Str = "";
                    for (char c : last5)
                        last5Str += c;
                    //if editingRowIndex equals -1 then we're not in update mode so we just add the new team into the list and table
                    if(editingRowIndex == -1) {
                        teamTable.insert(name,wins,draws,losses,goalsFor,goalsAgainst,last5Str);
                        JOptionPane.showMessageDialog(null, "Team added successfully!");
                        refreshTable();
                    }
                    //if editingRowIndex is not -1 then we are in update mode and use the value of that variable as the index to update the object in the list
                    else{

                        // Update table row
                        teamTable.update("Team_Name", name, "ID", String.valueOf(selectedID));
                        teamTable.update("Wins", String.valueOf(wins), "ID", String.valueOf(selectedID));
                        teamTable.update("Draws", String.valueOf(draws), "ID", String.valueOf(selectedID));
                        teamTable.update("Losses", String.valueOf(losses), "ID", String.valueOf(selectedID));
                        teamTable.update("GoalsFor", String.valueOf(goalsFor), "ID", String.valueOf(selectedID));
                        teamTable.update("GoalsAgainst", String.valueOf(goalsAgainst), "ID", String.valueOf(selectedID));
                        teamTable.update("Last_5_Results", last5Str, "ID", String.valueOf(selectedID));
                        //after update show confirmation message and set Add button back to normal and editingRowIndex back to -1
                        JOptionPane.showMessageDialog(null, "Team updated successfully!");
                        refreshTable();
                        btmAdd.setText("Add Team"); // switch back to add mode
                        editingRowIndex = -1;
                    }
                    // Clear fields
                    tfTeamName.setText("");
                    tfWins.setText("");
                    tfDraws.setText("");
                    tfLosses.setText("");
                    tfGoalsFor.setText("");
                    tfGoalsAgainst.setText("");
                    tfLast5.setText("");

            }
        });


        // --- Remove team button ---
        //Removes the selected team from the JTable and from the TeamManager list
        btmRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (teamTable == null) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Please connect to a database before deleting a record.",
                            "Database Not Connected",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }
                int selectedRow = DisplayTable.getSelectedRow(); // Get the selected row in JTable

                if (selectedRow == -1) { // Nothing selected
                    JOptionPane.showMessageDialog(null, "Please select a team to remove.");
                    return;
                }

                // Confirm deletion
                int confirm = JOptionPane.showConfirmDialog(
                        null,
                        "Are you sure you want to remove this team?",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    int id = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
                    teamTable.delete("ID", String.valueOf(id));
                    refreshTable();

                    JOptionPane.showMessageDialog(null, "Team removed successfully!");
                }
            }
        });

        // --- Update team button ---
        //Loads the selected team's details into the text fields for editing
        btmUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (teamTable == null) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Please connect to a database before deleting a record.",
                            "Database Not Connected",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }
                int selectedRow = DisplayTable.getSelectedRow();

                if (selectedRow == -1) { //no team selected
                    JOptionPane.showMessageDialog(null, "Please select a team to update.");
                    return;
                }

                int modelRow = DisplayTable.convertRowIndexToModel(selectedRow);

                // Populate text fields with current values
                tfTeamName.setText(tableModel.getValueAt(modelRow, 1).toString());
                tfWins.setText(tableModel.getValueAt(modelRow, 2).toString());
                tfDraws.setText(tableModel.getValueAt(modelRow, 3).toString());
                tfLosses.setText(tableModel.getValueAt(modelRow, 4).toString());
                tfGoalsFor.setText(tableModel.getValueAt(modelRow, 5).toString());
                tfGoalsAgainst.setText(tableModel.getValueAt(modelRow, 6).toString());
                tfLast5.setText(tableModel.getValueAt(modelRow, 7).toString());


                // Switch to update mode
                editingRowIndex = selectedRow;
                selectedID = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
                btmAdd.setText("Save Update");

                JOptionPane.showMessageDialog(null, "You can now edit the team fields and click 'Save Update' to confirm.");
            }
        });

        // --- Get Stats Button ---
        //gets the selected team to calculate stats for and displays the calculations in a pop-up message
        btmStats.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = DisplayTable.getSelectedRow();
                int points,goalDifference,totalGames;
                double winRate;
                if (selectedRow == -1) { //no team selected
                    JOptionPane.showMessageDialog(null, "Please select a team to get stats for.");
                    return;
                }
                int modelRow = DisplayTable.convertRowIndexToModel(selectedRow);
                int wins = Integer.parseInt(tableModel.getValueAt(modelRow, 2).toString());
                int draws = Integer.parseInt(tableModel.getValueAt(modelRow, 3).toString());
                int losses = Integer.parseInt(tableModel.getValueAt(modelRow, 4).toString());
                int goalsFor = Integer.parseInt(tableModel.getValueAt(modelRow, 5).toString());
                int goalsAgainst = Integer.parseInt(tableModel.getValueAt(modelRow, 6).toString());

                points = (wins * 3) + draws;
                goalDifference = goalsFor - goalsAgainst;
                totalGames = wins + draws + losses;

                winRate = (totalGames == 0) ? 0.0 : ((double) wins * 100 / totalGames);
                String message = "Team: " + tableModel.getValueAt(modelRow, 1).toString() + "\nWin Rate: " + String.format("%.2f", winRate) + "\nPoints: " + points + "\nGoal Difference: " + goalDifference;
                JOptionPane.showMessageDialog(null, message,"Team Stats", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // --- Connect to database button---
        //connects to a database of teams to make all changes made in the GUI persist after closing the program
        btmDB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Select SQLite Database File");

                int result = fileChooser.showOpenDialog(MainFrame.this);

                if (result == JFileChooser.APPROVE_OPTION) {
                    databasePath = fileChooser.getSelectedFile().getAbsolutePath();
                    JOptionPane.showMessageDialog(
                            MainFrame.this,
                            "Database selected:\n" + databasePath,
                            "Database Loaded",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    // Initialize DBHelper with the selected path
                    teamTable = new Team_Table(databasePath);
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(
                            MainFrame.this,
                            "No database selected.",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            }
        });
    }

    //getPositiveIntFromField
    //Validates that user entered input is a positive integer
    //takes a JTextField and a field label as arguments
    //returns the validated integer value or -1 if invalid
    public int getPositiveIntFromField(JTextField field, String fieldName) {
        try {
            int value = Integer.parseInt(field.getText().trim());
            if (value < 0) {
                JOptionPane.showMessageDialog(this, fieldName + " must be a positive integer.");
                field.setText("");
                return -1;
            }
            return value;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input for " + fieldName + ". Please enter a positive integer.");
            field.setText("");
            return -1;
        }
    }

    public void refreshTable() {
        data = teamTable.getExecuteResult("select * from Team_Table");
        if (data.isEmpty()) {
            JOptionPane.showMessageDialog(
                    MainFrame.this,
                    "No records found in the selected database.",
                    "Database Empty",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            // Clear the table before adding new rows (optional)
            tableModel.setRowCount(0);

            // Loop through each row of database data
            for (ArrayList<Object> row : data) {
                tableModel.addRow(row.toArray());
            }

        }
    }

    public static   void main(String[] args)
    {
        MainFrame mainFrame = new MainFrame();
    }

}
