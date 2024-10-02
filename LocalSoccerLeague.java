
/**
This program stores the statistics of different team.It also displays the statitics of different team and compare them to 
get the team's standing in the league. The teams get promoted and relegated based on their performance in the league.
**/

import java.io.*;
import java.util.*;

/*
 * This class implements a local soccer league. It is used to store the statistics of different teams and display the standings of the teams in the league.
 */
public class LocalSoccerLeague implements Comparable<LocalSoccerLeague> {

    // decalring the variables
    Map<String, String> teams = new HashMap<>();
    Map<String, Integer> teamPoints = new HashMap<>();
    Map<String, Integer> goalsFor = new HashMap<>();
    Map<String, Integer> goalsAgainst = new HashMap<>();
    Map<String, Integer> gamesPlayed = new HashMap<>();
    Map<String, Integer> gamesWon = new HashMap<>();
    Map<String, Integer> gamesLost = new HashMap<>();
    Map<String, Integer> gamesDrawn = new HashMap<>();
    int totalTeams;
    int uclTeams = 4; // Number of teams qualifying for UCL
    int uelTeams = 1; // Number of teams qualifying for UEL
    int promoteRelegateTeams;
    int points = 0;

    /*
     * This constructor reads the team names from a file and stores them in a map.
     * 
     * @param teamsFile The file containing the team names, total number of teams
     * and the teams that would get promoted and relegated.
     */
    public LocalSoccerLeague(String teamsFile) {
        File teamNameFile = new File(teamsFile);
    
        try {
            Scanner scanner = new Scanner(teamNameFile);
            String[] numbers = scanner.nextLine().split(" ");
    
            totalTeams = Integer.parseInt(numbers[0].trim());
            promoteRelegateTeams = Integer.parseInt(numbers[1].trim());
    
            // Initialize the stats for each team
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] info = line.split(",", 2); // Split only at the first comma
                
                if (info.length < 2) {
                    System.out.println("Invalid team data: " + Arrays.toString(info));
                    continue; // Skip this line and move to the next one
                }
                
                String teamAbbreviation = info[0].trim();
                String teamName = info[1].trim(); // The entire remainder is the team name
                
                teams.put(teamAbbreviation, teamName);
    
                // Initialize all stats to 0
                teamPoints.put(teamAbbreviation, 0);
                goalsFor.put(teamAbbreviation, 0);
                goalsAgainst.put(teamAbbreviation, 0);
                gamesPlayed.put(teamAbbreviation, 0);
                gamesWon.put(teamAbbreviation, 0);
                gamesLost.put(teamAbbreviation, 0);
                gamesDrawn.put(teamAbbreviation, 0);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Teams file not found.");
        }
    }
    /*
     * This method reads the game results from a file and updates the statistics of
     * the teams.
     * 
     * @param gameFile The file contains the game results.
     */
    public void gameResults(String gameFile) {
        File matchesFile = new File(gameFile);
    
        // Try block to handle the exception
        try {
            Scanner scanner = new Scanner(matchesFile);
    
            // Reads the file and assigns the teams and their goals to the map
            while (scanner.hasNextLine()) {
                String[] info = scanner.nextLine().split(" ");
                
                // Ensures that the line contains exactly 4 elements
                if (info.length < 4) {
                    System.out.println("Invalid game result format, skipping line.");
                    continue;  // Skip this invalid line
                }
    
                String homeTeam = info[0].trim();
                String awayTeam = info[2].trim();
                int homeGoals = Integer.parseInt(info[1].trim());
                int awayGoals = Integer.parseInt(info[3].trim());
    
                // Update the statistics of the teams
                updateStats(homeTeam, awayTeam, homeGoals, awayGoals);
                updateStats(awayTeam, homeTeam, awayGoals, homeGoals);
    
                // Update the points of the teams
                points(homeGoals, awayGoals, homeTeam);
                points(awayGoals, homeGoals, awayTeam);
            }
            // Closes the scanner
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Game file not found.");
        } catch (NumberFormatException e) {
            System.out.println("Error in parsing game result: " + e.getMessage());
        }
    }

    /*
     * This method updates the statistics of the teams.
     * 
     * @param team The team number.
     * 
     * @param opponent The opponent team number.
     * 
     * @param goalsFor The goals scored by the team.
     * 
     * @param goalsAgainst The goals scored by the opponent team.
     */
    private void updateStats(String team, String opponent, int goalsFor, int goalsAgainst) {

        // updates the goals for the team
        if (this.goalsFor.containsKey(team)) {
            this.goalsFor.put(team, this.goalsFor.get(team) + goalsFor);
        } else {
            this.goalsFor.put(team, goalsFor);
        }
        // updates the goals against the team
        if (this.goalsAgainst.containsKey(team)) {
            this.goalsAgainst.put(team, this.goalsAgainst.get(team) + goalsAgainst);
        } else {
            this.goalsAgainst.put(team, goalsAgainst);
        }
        gamesPlayed.put(team, gamesPlayed.get(team) + 1);
    }

    /*
     * The points method updates the points of the teams.
     * 
     * @param scored The goals scored by the team.
     * 
     * @param conceded The goals conceded by the team.
     * 
     * @param teamAbbreviation The team number.
     */
    private void points(int scored, int conceded, String teamAbbreviation) {
        // instance variable to store the points of the team
        int gamePoints;

        // assigns the points to the team based on the goals scored and conceded
        if (scored > conceded) {
            gamePoints = 3;
            gamesWon.put(teamAbbreviation, gamesWon.get(teamAbbreviation) + 1);
        }

        else if (scored == conceded) {
            gamePoints = 1; 
            gamesDrawn.put(teamAbbreviation, gamesDrawn.get(teamAbbreviation) + 1);
        }

        else {
            gamePoints = 0;
            gamesLost.put(teamAbbreviation, gamesLost.get(teamAbbreviation) + 1);
        }

        // updates the points of the team
        if (teamPoints.containsKey(teamAbbreviation)) {
            teamPoints.put(teamAbbreviation, teamPoints.get(teamAbbreviation) + gamePoints);
        }

        else {
            teamPoints.put(teamAbbreviation, gamePoints);
        }
        points += gamePoints;
    }

    /*
     * This method compares the teams based on their points, goal difference and
     * goals scored.
     * 
     * @param other The other team.
     * 
     * @return The comparison of the teams.
     */
    @Override
    public int compareTo(LocalSoccerLeague other) {
        return Integer.compare(this.points, other.points);
    }

    /*
     * This method sorts the teams based on their points, goal difference and goals
     * scored.
     * 
     * @param teamList The list of teams.
     * 
     * @param low The lower index of the list.
     * 
     * @param high The higher index of the list.
     */
    private void quicksortTeams(List<String> teamList, int low, int high) {
        if (low < high) {
            int partitionIndex = partition(teamList, low, high);
            quicksortTeams(teamList, low, partitionIndex - 1);
            quicksortTeams(teamList, partitionIndex + 1, high);
        }
    }

    /*
     * This method the list of teams.
     * 
     * @param teamList The list of teams.
     * 
     * @param low The lower index of the list.
     * 
     * @param high The higher index of the list.
     * 
     * @return the index of the partition
     */
    private int partition(List<String> teamList, int low, int high) {
        // instance variable to store the pivot
        String pivot = teamList.get(high);
        int i = low - 1;

        // compares the stats of the teams
        for (int j = low; j < high; j++) {
            if (compareStats(teamList.get(j), pivot) <= 0) {
                i++;
                swap(teamList, i, j);
            }
        }

        // swaps the teams
        swap(teamList, i + 1, high);
        return i + 1;
    }

    /*
     * This method compares the stats of the teams.
     * 
     * @param team1 The first team.
     * 
     * @param team2 The second team.
     */
    private int compareStats(String team1, String team2) {
        // Safely retrieve points, goal difference, and goals for, treating null as 0
        int team1Points = teamPoints.getOrDefault(team1, 0);
        int team2Points = teamPoints.getOrDefault(team2, 0);
        int team1GoalDifference = getGoalDifference(team1);
        int team2GoalDifference = getGoalDifference(team2);
        int team1GoalsFor = goalsFor.getOrDefault(team1, 0);
        int team2GoalsFor = goalsFor.getOrDefault(team2, 0);
    
        // Compare the points first
        int pointsComparison = Integer.compare(team2Points, team1Points);
        if (pointsComparison != 0) {
            return pointsComparison;
        }
    
        // If points are equal, compare goal difference
        int goalDifferenceComparison = Integer.compare(team2GoalDifference, team1GoalDifference);
        if (goalDifferenceComparison != 0) {
            return goalDifferenceComparison;
        }
    
        // If goal difference is equal, compare goals scored
        return Integer.compare(team2GoalsFor, team1GoalsFor);
    
    }

    /*
     * This method swaps the teams in the list.
     * 
     * @param teamList The list of teams.
     * 
     * @param firstTeam The first team.
     * 
     * @param secondTeam The second team.
     */
    private void swap(List<String> teamList, int firstTeam, int secondTeam) {
        // swaps the teams in the list
        String temp = teamList.get(firstTeam);
        teamList.set(firstTeam, teamList.get(secondTeam));
        teamList.set(secondTeam, temp);
    }

    /*
     * This method returns the goal difference of the teams.
     * 
     * @param teamAbbreviation The team number.
     * 
     * @return The goal difference of the team.
     */
    private int getGoalDifference(String teamAbbreviation) {
        return goalsFor.get(teamAbbreviation) - goalsAgainst.get(teamAbbreviation);
    }

    /*
     * This method sorts the teams based on their points, goal difference and goals
     * scored.
     * 
     * @return The list of sorted teams.
     */
    private List<String> sortTeams() {
        List<String> teamList = new ArrayList<>(teams.keySet());
        quicksortTeams(teamList, 0, teamList.size() - 1);
        return teamList;
    }

    /*
     * This method returns the sorted teams.
     * 
     * @return The list of sorted teams.
     */
    public List<String> getSortedTeams() {
        List<String> teamList = sortTeams();

        List<String> sortedTeams = new ArrayList<>();
        for (String teamAbbreviation : teamList) {
            sortedTeams.add(teams.get(teamAbbreviation));
        }
        return sortedTeams;
    }

    /*
     * This method handles the relegation and promotion of the teams.prints the
     * teams that get promoted and relegated.
     */
    public void handleRelegationPromotion() {
        List<String> sortedTeams = sortTeams();

        // Print teams qualifying for the UEFA Champions League (UCL)

        for (int i = 0; i < uclTeams; i++) {
            String uclTeam = sortedTeams.get(i);
            teams.get(uclTeam);
        }

        // Print teams qualifying for the UEFA Europa League (UEL)
        if (uclTeams < sortedTeams.size()) {
            String uelTeam = sortedTeams.get(uclTeams);
            teams.get(uelTeam);
        }

        // Print relegated teams
        for (int i = totalTeams - promoteRelegateTeams; i < totalTeams; i++) {
            String relegatedTeam = sortedTeams.get(i);
            teams.get(relegatedTeam);
        }
    }

    /*
     * This method prints the teams that are promoted and relegated with proper
     * formatting.
     * 
     */
    // Update the printStandings method to reflect league qualifications and
    // relegation
    public void printStandings() {
        List<String> sortedTeams = sortTeams(); // Get teams in sorted order based on points and stats.
    
        System.out.println("Full Points Table:");
        System.out.println(String.format("%-25s %5s %5s %5s %5s %10s %4s %5s %5s %5s", 
            "Team", "Played", "Won", "Drawn", "Lost", "Points", "GF", "GA", "GD", "Status"));
    
        for (int i = 0; i < totalTeams; i++) {
            String team = sortedTeams.get(i);
    
            // Initialize points, goals for, and goals against
            int points = 0;
            int gf = 0;
            int ga = 0;
            int played = gamesPlayed.get(team);
            int won = gamesWon.get(team);
            int drawn = gamesDrawn.get(team);
            int lost = gamesLost.get(team);
    
            // Retrieve points, goals for, and goals against if they exist
            if (teamPoints.containsKey(team)) {
                points = teamPoints.get(team);
            }
            
            if (goalsFor.containsKey(team)) {
                gf = goalsFor.get(team);
            }
    
            if (goalsAgainst.containsKey(team)) {
                ga = goalsAgainst.get(team);
            }
    
            // Calculate goal difference
            int gd = gf - ga;
    
            // Assign status based on league qualifications and relegation
            String status = "";
            if (i < uclTeams) {
                status = "UCL";
            } else if (i == uclTeams) {
                status = "UEL";
            } else if (i >= totalTeams - promoteRelegateTeams) {
                status = "Relegated";
            }
    
            // Print the full points table row for each team
            System.out.printf("%-25s %5d %5d %5d %5d %10d %5d %5d %5d %s\n", 
                teams.get(team), played, won, drawn, lost, points, gf, ga, gd, status);
        }
    }
}