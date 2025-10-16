//Victor Gomez, CEN3024C, 10/01/2025
//TeamManager class
//holds a list of Team objects and performs methods to add teams, load from a file, remove teams, and display the list of teams
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TeamManager {
    private List<Team> teams;

    //constructor
    public TeamManager() {
        teams = new ArrayList<>();
    }
    //adding team from console manually
    public Team addTeam(Team t) {
        teams.add(t);
        return t;
    }

    //addTeamsFromFile
    //loads teams from a file
    // takes the file's address as input
    //void method doesn't return anything but displays a message to the user confirming the teams have been loaded
    public List<Team> addTeamsFromFile(String filename) {
        int count = 0; // count of teams added

        // Automatically append .txt if it’s not already there
        if (!filename.endsWith(".txt")) {
            filename += ".txt";
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("-");

                if (parts.length != 7) {
                    System.out.println("Invalid line format (wrong number of fields): " + line);
                    continue;
                }

                String name = parts[0].trim();
                if (name.isEmpty()) {
                    System.out.println("Invalid team name in line: " + line);
                    continue;
                }

                int wins, draws, losses, goalsFor, goalsAgainst;

                try {
                    wins = Integer.parseInt(parts[1]);
                    draws = Integer.parseInt(parts[2]);
                    losses = Integer.parseInt(parts[3]);
                    goalsFor = Integer.parseInt(parts[4]);
                    goalsAgainst = Integer.parseInt(parts[5]);
                    // Ensure integers are non-negative
                    if (wins < 0 || draws < 0 || losses < 0 || goalsFor < 0 || goalsAgainst < 0) {
                        System.out.println("Negative numbers are not allowed: " + line);
                        continue;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format in line: " + line);
                    continue;
                }

                String last5String = parts[6].trim().toUpperCase();
                if (last5String.length() != 5) {
                    System.out.println("Last5 must be exactly 5 characters (W/D/L): " + line);
                    continue;
                }

                List<Character> last5 = new ArrayList<>();
                boolean validLast5 = true;
                for (char c : last5String.toCharArray()) {
                    if (c != 'W' && c != 'D' && c != 'L') {
                        validLast5 = false;
                        break;
                    }
                    last5.add(c);
                }

                if (!validLast5) {
                    System.out.println("Invalid last5 characters in line: " + line);
                    continue;
                }

                // Create team and add to list
                Team t = new Team(name, wins, draws, losses, goalsFor, goalsAgainst, last5);
                teams.add(t);
                count++;
            }

            System.out.println(count + " team(s) successfully loaded from file.");
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return teams;
    }
    //removeTeam
    //removes Teams from list
    //takes the index value to be looked at in the list to delete the team
    //void function doesn't return anything but displays a message to let the user know if the team was deleted
    public Team removeTeam(int index){
        if (index < 0 || index >= teams.size()) {
            return null;
        }
        else {
            return teams.remove(index);
        }
    }

    //updateteam
    //updates a team's attributes
    //takes the index of the team as argument
    //returns updated team
    public Team updateTeam(int index, String name, int wins, int draws, int losses, int goalsFor, int goalsAgainst, List<Character> last5) {
        if (index < 0 || index >= teams.size())
            return null;

        Team team = teams.get(index);

        team.setName(name);
        team.setWins(wins);
        team.setDraws(draws);
        team.setLosses(losses);
        team.setGoalsFor(goalsFor);
        team.setGoalsAgainst(goalsAgainst);
        team.setLast5(last5);

        return team;
    }


    //displayTeam
    //displays the list of teams
    //takes no arguments
    //void methods doesn't return anything, prints the list if is not empty
    public List<Team> displayTeams(){
        if (teams.isEmpty()) {
            System.out.println("No teams found.");
        } else {
            for (int i = 0; i < teams.size(); i++) {
                System.out.println("("+i+")" + teams.get(i));
            }
        }
        return teams;
    }

    //getTeam
    //retrieves a Team object from the list
    //takes an integer that represents the index in the list
    //Team method returns the team object
    public Team getTeam(int index) {
        if (index >= 0 && index < teams.size()) {
            return teams.get(index);
        }
        return null;
    }

}
