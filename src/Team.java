import java.util.List;

//Victor Gomez, CEN3024C 10/01/2025
//Team class
//Has 7 attributes name, wins, draws, losses, goalsfor, goalsagainst,and the last 5 results and holds all the information of the teams it also overrides the toString method
public class Team {
    private String name;
    private int wins;
    private int draws;
    private int losses;
    private int goalsFor;
    private int goalsAgainst;
    private List<Character> last5;

    //constructor
    public Team(String name, int wins, int draws, int losses, int goalsFor, int goalsAgainst, List<Character> last5) {
        this.name = name;
        this.wins = wins;
        this.draws = draws;
        this.losses = losses;
        this.goalsFor = goalsFor;
        this.goalsAgainst = goalsAgainst;
        this.last5 = last5;
    }

    //setters and getters

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getWins() {
        return wins;
    }
    public void setWins(int wins) {
        this.wins = wins;
    }
    public int getDraws() {
        return draws;
    }
    public void setDraws(int draws) {
        this.draws = draws;
    }
    public int getLosses() {
        return losses;
    }
    public void setLosses(int losses) {
        this.losses = losses;
    }
    public int getGoalsFor() {
        return goalsFor;
    }
    public void setGoalsFor(int goalsFor) {
        this.goalsFor = goalsFor;
    }
    public int getGoalsAgainst() {
        return goalsAgainst;
    }
    public void setGoalsAgainst(int goalsAgainst) {
        this.goalsAgainst = goalsAgainst;
    }
    public List<Character> getLast5() {
        return last5;
    }
    public void setLast5(List<Character> last5) {
        this.last5 = last5;
    }

    @Override
    public String toString() {
        return "Team name: " + name + ", wins: " + wins + ", draws: " + draws + ", losses: " + losses + ", goalsFor: " + goalsFor + ", goalsAgainst: " + goalsAgainst + ", last5: " + last5;
    }
}