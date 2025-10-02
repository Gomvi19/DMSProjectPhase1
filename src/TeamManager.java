//Victor Gomez, CEN3024C, 10/01/2025
//TeamManager class
//holds a list of Team objects and performs methods to add teams, load from a file, remove teams, and display the list of teams
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TeamManager {
    private List<Team> teams;

    //constructor
    public TeamManager() {
        teams = new ArrayList<>();
    }
    //adding team from console manually
    public void addTeam(Team t) {
        teams.add(t);
    }

    //addTeamsFromFile
    //loads teams from a file
    // takes the file's address as input
    //void method doesn't return anything but displays a message to the user confirming the teams have been loaded
    public void addTeamsFromFile(String filename){
        int count =0; //count of teams added
        // Automatically append .txt if it’s not already there
        if (!filename.endsWith(".txt")) {
            filename += ".txt";
        }
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) { //reading until last line in the file
                String[] parts = line.split("-"); //separating fields by "-"
                if (parts.length == 7) { //if each line has 7 parts separated by "-" each part is assigned to a variable
                    String name = parts[0];
                    int wins = Integer.parseInt(parts[1]);
                    int draws = Integer.parseInt(parts[2]);
                    int losses = Integer.parseInt(parts[3]);
                    int goalsFor = Integer.parseInt(parts[4]);
                    int goalsAgainst = Integer.parseInt(parts[5]);
                    List<Character> last5 = new ArrayList<>();
                    for (char c : parts[6].toCharArray()) {
                        last5.add(c);
                    }


                    Team t = new Team(name,wins,draws,losses,goalsFor,goalsAgainst,last5); //creating the Team object with the input from the file
                    teams.add(t);
                    count ++; //increasing count by one to let the user know how many teams where added from the file
                } else {
                    System.out.println("Invalid line format: " + line);
                }
            }
            System.out.println(count + " team(s) successfully loaded from file.");
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
    //removeTeam
    //removes Teams from list
    //takes the index value to be looked at in the list to delete the team
    //void function doesn't return anything but displays a message to let the user know if the team was deleted
    public void removeTeam(int index){
        if (index >= teams.size()) {
            System.out.println("Team number out of range");
        }
        else {
            System.out.println(teams.get(index) +  " successfully removed from list.");
            teams.remove(index);
        }
    }

    //displayTeam
    //displays the list of teams
    //takes no arguments
    //void methods doesn't return anything, prints the list if is not empty
    public void displayTeams(){
        if (teams.isEmpty()) {
            System.out.println("No teams found.");
        } else {
            for (int i = 0; i < teams.size(); i++) {
                System.out.println("("+i+")" + teams.get(i));
            }
        }
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
