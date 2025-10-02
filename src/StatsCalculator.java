public class StatsCalculator {

    // Calculates goal difference (Goals For - Goals Against)
    public int calculateGoalDifference(Team team) {
        return team.getGoalsFor() - team.getGoalsAgainst();
    }

    // Calculates win rate as a percentage (Wins / Total Games * 100)
    public double calculateWinRate(Team team) {
        int totalGames = team.getWins() + team.getDraws() + team.getLosses();
        if (totalGames == 0) {
            return 0.0; // Avoid division by zero
        }
        return (team.getWins() * 100.0) / totalGames;
    }

    // Custom action: Display a formatted summary of the team’s stats
    public String displayTeamStats(Team team) {
        return "Team: " + team.getName() +
                "\nGoal Difference: " + calculateGoalDifference(team) +
                "\nWin Rate: " + String.format("%.2f", calculateWinRate(team)) + "%";
    }
}