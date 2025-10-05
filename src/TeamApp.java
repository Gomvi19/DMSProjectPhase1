//Victor Gomez, CEN3024C, 10/01/2025
//Main class
//Contains main and calls the menu function
//This program aims to create a system to input, load from a file, delete, and display teams for a DMS of teams
import java.util.ArrayList;
import java.util.InputMismatchException;
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

    //displayMenu
    //takes no attributes
    // Returns menu as String instead of printing
    public String displayMenu() {
        return "\nTeam App\n" +
                "1. Add Team\n" +
                "2. Import Teams from File\n" +
                "3. Remove Team\n" +
                "4. Display Teams\n" +
                "5. Calculate Stats\n" +
                "6. Quit\n";
    }

    //getPositiveInt
    //handles integer input exceptions
    //takes a prompt to the user as input
    //returns the valid input to create the object in the handleInput method
    public int getPositiveInt(String prompt) {
        int value = -1;
        while (value < 0) {
            try {
                System.out.println(prompt);
                value = input.nextInt();
                if (value < 0) {
                    System.out.println("Please enter a positive integer.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a positive integer.");
                input.nextLine(); // clear bad input
            }
        }
        input.nextLine(); // consume newline
        return value;
    }

    //handleInput
    //takes no attributes
    // Handles one loop iteration
    // returns true if user decides not to continue
    public boolean handleInput() {
        System.out.println(displayMenu());
        int choice = getPositiveInt("Enter an option:");

            switch (choice) {
                case 1: // Add Team
                    System.out.print("Enter name: ");
                    String name = input.nextLine();
                    while (name.isEmpty()) {
                        System.out.println("Invalid input must enter a name, please try again");
                        name = input.nextLine();
                    }
                    int wins = getPositiveInt("Enter wins: ");
                    int draws = getPositiveInt("Enter draws: ");
                    int losses = getPositiveInt("Enter losses: ");
                    int goalsFor = getPositiveInt("Enter goals for: ");
                    int goalsAgainst = getPositiveInt("Enter goals against: ");

                    System.out.print("Enter last5: ");
                    List<Character> last5 = new ArrayList<Character>();
                    for (int i = 0; i < 5; i++) {
                        System.out.println("Enter last5 (W or D or L): [" + (i + 1) + "]: ");
                        char result = input.next().charAt(0);
                        result = Character.toUpperCase(result);
                        while ((result != 'W') && (result != 'D') && (result != 'L')) {
                            System.out.println("Incorrect input please enter W/D/L");
                            result = input.next().charAt(0);
                            result = Character.toUpperCase(result);
                        }
                        last5.add(result);
                    }

                    input.nextLine(); // consume leftover newline
                    Team team = new Team(name, wins, draws, losses, goalsFor, goalsAgainst, last5);
                    teamManager.addTeam(team);
                    System.out.println("Team added successfully.");
                    break;

                case 2: // Import Teams
                    System.out.print("Enter filename address, ");
                    System.out.println("example: C:\\Users\\your username\\OneDrive\\Desktop\\teams.txt");
                    String filename = input.nextLine();
                    teamManager.addTeamsFromFile(filename);
                    break;

                case 3: // Remove Teams
                    int index = getPositiveInt("Enter index of team to remove: ");
                    teamManager.removeTeam(index);
                    break;

                case 4: // Display Teams
                    teamManager.displayTeams();
                    break;

                case 5: //custom method
                    teamManager.displayTeams();
                    int index2 = getPositiveInt("Enter index of team to display stats for: ");
                    StatsCalculator stats = new StatsCalculator();
                    if(teamManager.getTeam(index2) != null)
                        System.out.println(stats.displayTeamStats(teamManager.getTeam(index2)));
                    else
                        System.out.println("No team with that index exists.");
                    break;

                case 6: // Quit
                    System.out.println("Goodbye!");
                    return false; // end loop

                default:
                    System.out.println("Invalid option, try again.");
            }
            return true;// keep looping

    }

    //run
    //runs the program
    // returns exit message
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
