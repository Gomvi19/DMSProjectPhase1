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
    private JButton btmLoad;
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
                "Name", "Wins", "Draws", "Losses", "Goals For", "Goals Against", "Last 5"
        };

        // Initialize table model with no rows but visible columns
        tableModel = new DefaultTableModel(columnNames, 0);

        // Wrap JTable inside a scroll pane (so headers show correctly)
        DisplayTable.setModel(tableModel);

        // Make frame visible at the end
        setVisible(true);
        //------Add Team Button-------
        //manually add a team or updates an existing team
        btmAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                    //if editingRowIndex equals -1 then we're not in update mode so we just add the new team into the list and table
                    if(editingRowIndex == -1) {
                        Team team = new Team(name, wins, draws, losses, goalsFor, goalsAgainst, last5);
                        teamManager.addTeam(team);
                        JOptionPane.showMessageDialog(null, "Team added successfully!");

                        // Add row to JTable and add last 5 characters into a string for better display
                        String last5String = "";
                        for (char c : last5)
                            last5String += c;
                        tableModel.addRow(new Object[]{
                                name, wins, draws, losses, goalsFor, goalsAgainst, last5String
                        });
                    }
                    //if editingRowIndex is not -1 then we are in update mode and use the value of that variable as the index to update the object in the list
                    else{
                        teamManager.updateTeam(editingRowIndex, name, wins, draws, losses, goalsFor, goalsAgainst, last5);

                        // Update table row
                        tableModel.setValueAt(name, editingRowIndex, 0);
                        tableModel.setValueAt(wins, editingRowIndex, 1);
                        tableModel.setValueAt(draws, editingRowIndex, 2);
                        tableModel.setValueAt(losses, editingRowIndex, 3);
                        tableModel.setValueAt(goalsFor, editingRowIndex, 4);
                        tableModel.setValueAt(goalsAgainst, editingRowIndex, 5);
                        tableModel.setValueAt(last5Str, editingRowIndex, 6);
                        //after update show confirmation message and set Add button back to normal and editingRowIndex back to -1
                        JOptionPane.showMessageDialog(null, "Team updated successfully!");
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

        // --- Load from file button ---
        //Loads team data from a text file and validates each line before adding to the list and table
        btmLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(MainFrame.this);

                if (result != JFileChooser.APPROVE_OPTION) {
                    JOptionPane.showMessageDialog(null, "File loading canceled.");
                    return;
                }
                //address of the file
                String filename = fileChooser.getSelectedFile().getAbsolutePath();

                try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
                    String line;
                    int count = 0;
                    int invalidCount = 0;
                    //read unit there are no more lines left
                    while ((line = br.readLine()) != null) {
                        String[] parts = line.split("-");

                        if (parts.length != 7) {
                            invalidCount++;
                            continue;
                        }

                        String name = parts[0];
                        if (name.isEmpty()) {
                            invalidCount++;
                            continue;
                        }

                        int wins, draws, losses, goalsFor, goalsAgainst;
                        try {
                            wins = Integer.parseInt(parts[1]);
                            draws = Integer.parseInt(parts[2]);
                            losses = Integer.parseInt(parts[3]);
                            goalsFor = Integer.parseInt(parts[4]);
                            goalsAgainst = Integer.parseInt(parts[5]);
                            if (wins < 0 || draws < 0 || losses < 0 || goalsFor < 0 || goalsAgainst < 0) {
                                invalidCount++;
                                continue;
                            }
                        } catch (NumberFormatException ex) {
                            invalidCount++;
                            continue;
                        }

                        String last5String = parts[6].trim().toUpperCase();
                        if (last5String.length() != 5 || !last5String.matches("[WDL]+")) {
                            invalidCount++;
                            continue;
                        }

                        List<Character> last5 = new ArrayList<>();
                        for (char c : last5String.toCharArray())
                            last5.add(c);

                        Team team = new Team(name, wins, draws, losses, goalsFor, goalsAgainst, last5);
                        teamManager.addTeam(team);

                        // Add valid team to table
                        tableModel.addRow(new Object[]{
                                name, wins, draws, losses, goalsFor, goalsAgainst, last5String
                        });
                        count++;
                    }

                    // Show summary results
                    String message = count + " team(s) successfully loaded.";
                    if (invalidCount > 0) {
                        message += "\n" + invalidCount + " line(s) were skipped due to invalid format or data.";
                    }
                    JOptionPane.showMessageDialog(null, message);

                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error reading file: " + ex.getMessage());
                }
            }
        });
        // --- Remove team button ---
        //Removes the selected team from the JTable and from the TeamManager list
        btmRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                    // Remove from TeamManager
                    teamManager.removeTeam(selectedRow);

                    // Remove from JTable
                    tableModel.removeRow(selectedRow);

                    JOptionPane.showMessageDialog(null, "Team removed successfully!");
                }
            }
        });
        // --- Update team button ---
        //Loads the selected team's details into the text fields for editing
        btmUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = DisplayTable.getSelectedRow();

                if (selectedRow == -1) { //no team selected
                    JOptionPane.showMessageDialog(null, "Please select a team to update.");
                    return;
                }

                // Get the selected team from TeamManager
                Team selectedTeam = teamManager.getTeam(selectedRow);

                // Populate text fields with current values
                tfTeamName.setText(selectedTeam.getName());
                tfWins.setText(String.valueOf(selectedTeam.getWins()));
                tfDraws.setText(String.valueOf(selectedTeam.getDraws()));
                tfLosses.setText(String.valueOf(selectedTeam.getLosses()));
                tfGoalsFor.setText(String.valueOf(selectedTeam.getGoalsFor()));
                tfGoalsAgainst.setText(String.valueOf(selectedTeam.getGoalsAgainst()));

                StringBuilder last5String = new StringBuilder();
                for (char c : selectedTeam.getLast5())
                    last5String.append(c);
                tfLast5.setText(last5String.toString());

                // Switch to update mode
                editingRowIndex = selectedRow;
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

                if (selectedRow == -1) { //no team selected
                    JOptionPane.showMessageDialog(null, "Please select a team to get stats for.");
                    return;
                }
                Team selectedTeam = teamManager.getTeam(selectedRow);

                JOptionPane.showMessageDialog(null, stats.displayTeamStats(selectedTeam),"Team Stats", JOptionPane.INFORMATION_MESSAGE);

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
                    Team_Table teamTable = new Team_Table(databasePath);
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




    public static   void main(String[] args)
    {
        MainFrame mainFrame = new MainFrame();
    }

}
