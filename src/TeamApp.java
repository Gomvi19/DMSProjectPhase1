//Victor Gomez, CEN3024C, 10/01/2025
//Main class
//Contains main and calls the menu function
//This program aims to create a system to input, load from a file, delete, and display teams for a DMS of teams
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TeamApp {
    private TeamManager teamManager;
    private Scanner input;

    // constructor
    public TeamApp() {
        this.teamManager = new TeamManager();
        this.input = new Scanner(System.in);
    }

    // Returns menu as String instead of printing
    public String displayMenu() {
        return "\nTeam App\n" +
                "Choose an option:\n" +
                "1. Add Team\n" +
                "2. Import Teams from File\n" +
                "3. Remove Team\n" +
                "4. Display Teams\n" +
                "5. Calculate Stats\n" +
                "6. Quit\n";
    }

    // Handles one loop iteration and returns true if user decides not to continue
    public boolean handleInput() {
        System.out.println(displayMenu());
        int choice = input.nextInt();
        input.nextLine(); // consume leftover newline

        switch (choice) {
            case 1: // Add Team
                System.out.print("Enter name: ");
                String name = input.nextLine();

                System.out.print("Enter wins: ");
                int wins = input.nextInt();

                System.out.print("Enter draws: ");
                int draws = input.nextInt();

                System.out.print("Enter losses: ");
                int losses = input.nextInt();
                System.out.print("Enter goalsfor: ");
                int goalsfor = input.nextInt();

                System.out.print("Enter goalsagainst: ");
                int goalsagainst = input.nextInt();

                System.out.print("Enter last5: ");
                List<Character> last5 = new ArrayList<Character>();
                for (int i = 0; i < 5; i++) {
                    System.out.println("Enter last5: [" +  (i + 1) + "]: ");
                    char result = input.next().charAt(0);
                    last5.add(result);
                }

                input.nextLine(); // consume leftover newline
                Team team = new Team(name,wins,draws,losses,goalsfor,goalsagainst,last5);
                teamManager.addTeam(team);
                System.out.println("Team added successfully.");
                break;

            case 2: // Import Teams
                System.out.print("Enter filename address: ");
                String filename = input.nextLine();
                teamManager.addTeamsFromFile(filename);
                break;

            case 3: // Remove Teams
                System.out.print("Enter index of team to remove: ");
                int index = input.nextInt();
                teamManager.removeTeam(index);
                break;

            case 4: // Display Teams
                teamManager.displayTeams();
                break;

            case 5: //custom method
                System.out.println("Enter index of team ");
                teamManager.displayTeams();
                int index2 = input.nextInt();
                StatsCalculator stats = new StatsCalculator();
                System.out.println(stats.displayTeamStats(teamManager.getTeam(index2)));
                break;

            case 6: // Quit
                System.out.println("Goodbye!");
                return false; // end loop

            default:
                System.out.println("Invalid option, try again.");
        }
        return true; // keep looping
    }

    // Run the program (non-void, returns exit message)
    public String run() {
        boolean running = true;
        while (running) {
            running = handleInput();
        }
        return "Program finished.";
    }

    public static void main(String[] args) {
        TeamApp app = new TeamApp();
        System.out.println(app.run());
    }
}
