/**
 This program is a driver class for the LocalSoccerLeague class. It reads the team names from team.txt and game results from games.txt and prints the standings of the teams and the teams that are promoted and relegated.
**/
public class LeagueDriver {
    public static void main(String[] args) {
        //create a new local league which reads the team names from team.txt
        LocalSoccerLeague localLeague = new LocalSoccerLeague("team.txt");

        //read the game results from games.txt
        localLeague.gameResults("games.txt");

        //print the standings
        localLeague.printStandings();
    }
}
