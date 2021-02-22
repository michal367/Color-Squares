package main;

import javafx.util.Pair;

import java.util.ArrayList;

public class Game {
    private GameMap map;

    private Vector2d size;
    public int numberOfPlayers;
    public int numberOfEnemies;
    public int numberOfObstacles;

    public boolean paused;
    public boolean end;
    public int animationTime;

    public boolean isPlayersSet;
    public int playersSet;

    public Game(Vector2d size, int numberOfPlayers, int numberOfEnemies, int numberOfObstacles){
        this.size = size;
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfEnemies = numberOfEnemies;
        this.numberOfObstacles = numberOfObstacles;

        paused = false;
        end = false;
        animationTime = 400;
        isPlayersSet = (numberOfPlayers == 0);
        playersSet = 0;

        map = new GameMap(size, numberOfPlayers, numberOfEnemies, numberOfObstacles);
    }

    public void run(){
        if(isPlayersSet)
            if(map.run() == true){
                end = true;
            }
    }
    public void addPlayer(Vector2d position){
        if(!isPlayersSet) {
            if (map.addPlayer(position)) {
                playersSet++;
                if (playersSet == numberOfPlayers)
                    isPlayersSet = true;
            }
        }
    }
    public ArrayList<Pair<Character, String>> getWinners(){
        return map.getWinners();
    }

    public void restart(){
        isPlayersSet = (numberOfPlayers == 0);
        playersSet = 0;
        end = false;
        map = new GameMap(size, numberOfPlayers, numberOfEnemies, numberOfObstacles);
    }

    public GameMap getMap(){
        return map;
    }
}
