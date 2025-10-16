public class StatsCalculator {
    private int goal_difference = 0;
    private double win_rate = 0;
    private int points = 0;

    // Calculates goal difference (Goals For - Goals Against)
    public int calculateGoalDifference(Team team) {
        this.goal_difference = team.getGoalsFor() - team.getGoalsAgainst();
        return goal_difference;
    }

    // Calculates win rate as a percentage (Wins / Total Games * 100)
    public double calculateWinRate(Team team) {
        int totalGames = team.getWins() + team.getDraws() + team.getLosses();
        if (totalGames == 0) {
            return 0.0; // Avoid division by zero
        }
        this.win_rate = (team.getWins() * 100.0) / totalGames;
        return win_rate;
    }

    public int points(Team team) {
        this.points = (team.getWins() * 3) + team.getDraws();
        return points;
    }

    // Custom action: Display a formatted summary of the team’s stats
    public String displayTeamStats(Team team) {
        return "Team: " + team.getName() +
                "\nGoal Difference: " + calculateGoalDifference(team) +
                "\nWin Rate: " + String.format("%.2f", calculateWinRate(team)) + "%" +
                "\nTotal points: " + points(team);
    }
}