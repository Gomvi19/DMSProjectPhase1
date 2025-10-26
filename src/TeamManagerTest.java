import org.junit.jupiter.api.DisplayName;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class TeamManagerTest {

    //create objects to test
    Team team;
    TeamManager teamManager;
    StatsCalculator stats;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        //supply data to objects
        team = new Team("Real Madrid",10,2,1,54,12, Arrays.asList('W', 'W', 'W', 'D', 'L'));
        teamManager = new TeamManager();
        stats = new StatsCalculator();
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Add team")
    void addTeam() {
        teamManager.addTeam(team);
        assertEquals("Real Madrid",team.getName(),"Team was not created");
        assertEquals(1,teamManager.displayTeams().size(),"Team was not added to the list");

        assertEquals("Barcelona", team.getName(), "Team was not created");
        assertEquals(2,teamManager.displayTeams().size(),"Team was not added to the list");
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Add team from file")
    void addTeamsFromFile() throws IOException {
        //create a dummy file and give it some data
        File tempFile = File.createTempFile("teams_test", ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write("Valencia-10-2-1-45-12-WWDLW\n");
        }

        // Call the method — should open and read the file without errors
        assertDoesNotThrow(() -> {
            List<Team> result = teamManager.addTeamsFromFile(tempFile.getAbsolutePath());
        }, "File should open and be read without throwing an exception");


        String filepath = "sometext";
        assertThrows(IOException.class, () -> {
            teamManager.addTeamsFromFile(filepath);
        }, "Expected IOException when file does not exist");
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Remove team")
    void removeTeam() {
        teamManager.addTeam(team);
        teamManager.removeTeam(0);
        assertTrue(teamManager.displayTeams().isEmpty(), "Team list should be empty after removal.");

        teamManager.removeTeam(5);
        assertFalse(teamManager.displayTeams().isEmpty(), "Removing with invalid index should do nothing");

    }

    @org.junit.jupiter.api.Test
    @DisplayName("Update team")
    void updateTeam() {
        teamManager.addTeam(team);
        teamManager.updateTeam(0,"Barcelona",2,13,24,11,57, Arrays.asList('L', 'L', 'L', 'D', 'W'));

        assertNotEquals("Real Madrid", teamManager.getTeam(0).getName(), "Team name should update correctly.");
        assertNotEquals(10, teamManager.getTeam(0).getWins(), "Team wins should update correctly.");
        assertNotEquals(2, teamManager.getTeam(0).getDraws(), "Team draws should update correctly.");
        assertNotEquals(1, teamManager.getTeam(0).getLosses(), "Team losses should update correctly.");
        assertNotEquals(54, teamManager.getTeam(0).getGoalsFor(), "Team goals for should update correctly.");
        assertNotEquals(12, teamManager.getTeam(0).getGoalsAgainst(), "Team goals against should update correctly.");
        assertNotEquals(Arrays.asList('W', 'W', 'W', 'D', 'L'), teamManager.getTeam(0).getLast5(), "Team last 5 should update correctly.");
        assertEquals("Real Madrid", teamManager.getTeam(0).getName(), "Name should not be Real Madrid.");
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Stat calculation")
    void testCustomAction_DisplayStats() {
        int goal_diff = stats.calculateGoalDifference(team);
        double win_rate = stats.calculateWinRate(team);
        int points = stats.points(team);

        double expectedWinRate = (team.getWins() * 100.0) / (team.getWins() + team.getDraws() + team.getLosses());
        int expectedPoints = (team.getWins() * 3) + team.getDraws();
        int expectedGoalDiff = team.getGoalsFor()-team.getGoalsAgainst();

        assertEquals(expectedGoalDiff, goal_diff, "Team goal difference should calculate correctly.");
        assertEquals(expectedWinRate,win_rate,"win rate should calculate correctly");
        assertEquals(expectedPoints,points,"points should calculate correctly.");
        assertEquals(21.7,win_rate,"win rate should calculate correctly");
    }
}