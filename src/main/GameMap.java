package main;

import javafx.scene.paint.Color;
import javafx.util.Pair;
import main.utils.MyColors;

import java.util.*;

public class GameMap {

    public Vector2d size;
    private ArrayList<Character> characters;
    private ArrayList<Player> players;
    private ArrayList<Enemy> enemies;
    private HashMap<Vector2d, Obstacle> obstacles;
    private ArrayList<Integer> order;

    private int numberOfPlayers;
    private int numberOfEnemies;

    public GameMap(Vector2d size, int numberOfPlayers, int numberOfEnemies, int numberOfObstacles){
        this.size = size;
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfEnemies = numberOfEnemies;

        characters = new ArrayList<>();
        players = new ArrayList<>();
        enemies = new ArrayList<>();
        obstacles = new HashMap<>();

        order = new ArrayList<>();
        for(int i=0; i<numberOfPlayers + numberOfEnemies; i++)
            order.add(i);


        //for(int i=0; i<numberOfPlayers; i++)
        //    players.add(new Player(this));

        for(int i=0; i<numberOfEnemies; i++){
            addEnemy(getRandomEmptyPosition());
        }


        for(int i=0; i<numberOfObstacles; i++) {
            Vector2d pos = getRandomEmptyPosition();
            obstacles.put(pos, new Obstacle(pos));
        }
    }

    // todo: list of ready maps to load from file/mock
    public GameMap(int mapId){

    }

    public boolean addPlayer(Vector2d position){
        if(!isOccupied(position)) {
            Color color = MyColors.getEvenlyDistributedColorHSB(numberOfEnemies+players.size(), numberOfPlayers+numberOfEnemies);
            Player newPlayer = new Player(position, color, this);
            players.add(newPlayer);
            characters.add(newPlayer);
            return true;
        }
        return false;
    }
    public boolean addEnemy(Vector2d position){
        if(!isOccupied(position)) {
            Color color = MyColors.getEvenlyDistributedColorHSB(enemies.size(), numberOfPlayers+numberOfEnemies);
            Enemy newEnemy = new Enemy(position, color, this);
            enemies.add(newEnemy);
            characters.add(newEnemy);
            return true;
        }
        return false;
    }

    public boolean run(){
        boolean end = true;

        Collections.shuffle(order);

        for(int i=0; i<characters.size(); i++){
            boolean moved = characters.get(order.get(i)).expand();
            end = end && !moved;
        }
        return end;
    }


    public Vector2d getRandomPosition(){
        Random rand = new Random();
        int x = rand.nextInt(size.x);
        int y = rand.nextInt(size.y);
        return new Vector2d(x,y);
    }

    public Vector2d getRandomEmptyPosition(){
        if(isEmptyPosExists()) {
            Vector2d randPos;
            do {
                randPos = getRandomPosition();
            } while (isOccupied(randPos));
            return randPos;
        }
        return null;
    }
    public ArrayList<Vector2d> getEmptyPositionsAround(Vector2d position){
        ArrayList<Vector2d> positions = new ArrayList<>();
        Vector2d newPosition;

        // top
        newPosition = new Vector2d(position.x, position.y - 1);
        if(position.y > 0 && !isOccupied(newPosition))
            positions.add(newPosition);

        // right
        newPosition = new Vector2d(position.x + 1, position.y);
        if(position.x + 1 < size.x && !isOccupied(newPosition))
            positions.add(newPosition);

        // bottom
        newPosition = new Vector2d(position.x, position.y + 1);
        if(position.y + 1 < size.y && !isOccupied(newPosition))
            positions.add(newPosition);

        // left
        newPosition = new Vector2d(position.x - 1, position.y);
        if(position.x > 0 && !isOccupied(newPosition))
            positions.add(newPosition);

        return positions;
    }

    public boolean isOccupied(Vector2d position){
        for (Character character : characters) {
            if(character.isOccupied(position))
                return true;
        }
        return obstacles.containsKey(position);
    }

    public boolean isEmptyPosExists(){
        int occupiedSize = 0;

        for (Character character : characters)
            occupiedSize += character.getTerritorySize();
        occupiedSize += obstacles.size();

        return (occupiedSize < size.x * size.y);
    }

    public ArrayList<Pair<Character, String>> getWinners(){
        int max = 0;
        ArrayList<Pair<Character, String>> winners = new ArrayList<>();

        int i=0;
        for (Player player : players) {
            if (player.getTerritorySize() == max) {
                winners.add(new Pair<>(player, "Player " + (i+1)));
            } else if (player.getTerritorySize() > max) {
                max = player.getTerritorySize();
                winners.clear();
                winners.add(new Pair<>(player, "Player " + (i+1)));
            }
            i++;
        }
        i=0;
        for (Enemy enemy : enemies){
            if (enemy.getTerritorySize() == max) {
                winners.add(new Pair<>(enemy, "Enemy " + (i+1)));
            } else if (enemy.getTerritorySize() > max) {
                max = enemy.getTerritorySize();
                winners.clear();
                winners.add(new Pair<>(enemy, "Enemy " + (i+1)));
            }
            i++;
        }
//        winners.add(new Pair<>(enemies.get(0), "dasdsad"));
//        winners.add(new Pair<>(enemies.get(1), "dasdsad"));

        return winners;
    }


    public ArrayList<Player> getPlayers(){
        return players;
    }
    public ArrayList<Enemy> getEnemies(){
        return enemies;
    }
    public ArrayList<Character> getCharacters() {
        return characters;
    }
    public HashMap<Vector2d, Obstacle> getObstacles(){
        return obstacles;
    }


}
