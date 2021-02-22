package main.visualization;

import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Pair;
import main.*;
import main.Character;

import java.util.ArrayList;
import java.util.HashMap;

public class Renderer extends Parent {

    private final Canvas canvas;
    private GraphicsContext gc;

    private Game game;

    private Vector2d size;
    private Vector2d mapSize;
    private Vector2d mapTilesAmount;
    private Vector2dDouble tileSize;

    private boolean borders;

    private Vector2d mousePos;

    public Renderer(Vector2d size, Game game, boolean borders){
        this.game = game;

        this.size = new Vector2d(size.x + 200, size.y);
        this.mapSize = size;
        this.mapTilesAmount = game.getMap().size;

        this.borders = borders;

        canvas = new Canvas(this.size.x, this.size.y);
        getChildren().add(canvas);
        gc = canvas.getGraphicsContext2D();

        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if(!game.paused) {
                int x = (int) (e.getX() / tileSize.x);
                int y = (int) (e.getY() / tileSize.y);
                if(x < mapTilesAmount.x && y < mapTilesAmount.y) {
                    Vector2d position = new Vector2d(x, y);
                    game.addPlayer(position);
                }
            }
        });
        canvas.addEventHandler(MouseEvent.MOUSE_MOVED, e -> {
            if(!game.paused && !game.isPlayersSet) {
                int x = (int) (e.getX() / tileSize.x);
                int y = (int) (e.getY() / tileSize.y);
                if(x < mapTilesAmount.x && y < mapTilesAmount.y)
                    mousePos = new Vector2d(x, y);
            }
        });

        tileSize = new Vector2dDouble((double)size.x / mapTilesAmount.x, (double)size.y / mapTilesAmount.y);

        paint();
    }


    private void paint(){
        GameMap map = game.getMap();

        drawMapElements();

        // MOUSE POS
        if(mousePos != null && !game.paused && !game.isPlayersSet) {
            if(!map.isOccupied(mousePos)){
                gc.setFill(Color.DARKGRAY);
                double posX = mousePos.x * tileSize.x;
                double posY = mousePos.y * tileSize.y;
                gc.fillRect(posX, posY, tileSize.x, tileSize.y);
            }
        }

        drawBorders();
        drawTextInterface();
    }

    public void drawMapElements(){
        GameMap map = game.getMap();

        // OBSTACLES
        gc.setFill(Color.BLACK);
        for (HashMap.Entry<Vector2d, Obstacle> obstacles : map.getObstacles().entrySet()) {
            Vector2d position = obstacles.getKey();
            double posX = position.x * tileSize.x;
            double posY = position.y * tileSize.y;

            gc.fillRect(posX, posY, tileSize.x, tileSize.y);
        }

        // CHARACTERS (PLAYERS + ENEMIES)
        for(Character character : map.getCharacters()){
            gc.setFill(character.getColor());

            for(Vector2d position : character.getTerritory()){
                double posX = position.x * tileSize.x;
                double posY = position.y * tileSize.y;

                gc.fillRect(posX, posY, tileSize.x, tileSize.y);
            }
        }
    }

    public void drawBorders(){
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        if(borders) {
            for (int i = 0; i <= mapTilesAmount.x; i++)
                gc.strokeLine(i * tileSize.x, 0, i * tileSize.x, mapSize.y);
            for (int i = 0; i <= mapTilesAmount.y; i++)
                gc.strokeLine(0, i * tileSize.y, mapSize.x, i * tileSize.y);
        }
        else{
            gc.strokeLine(0, 0, mapSize.x, 0);
            gc.strokeLine(0, mapSize.y, mapSize.x, mapSize.y);
            gc.strokeLine(0, 0, 0, mapSize.y);
            gc.strokeLine(mapSize.x, 0, mapSize.x, mapSize.y);
        }
    }

    public void drawTextInterface(){
        GameMap map = game.getMap();

        gc.setFont(new Font("System", 16));

        int startX = mapSize.x + 20;

        gc.strokeText("Players: " + game.numberOfPlayers, startX, 20);
        gc.strokeText("Enemies: " + game.numberOfEnemies, startX, 40);
        if(!game.isPlayersSet){
            gc.strokeText("Choose your position, \nplayer " + (game.playersSet+1), startX, 80);
        }

        int i=0;
        for(Player player : map.getPlayers()) {
            int posX = startX;
            int posY = 100 + 40 + i*20;

            gc.setFill(player.getColor());
            gc.fillRect(posX, posY-13, 14, 14);

            gc.strokeText("Player " + (i+1) + ": " + player.getTerritorySize(), posX + 18, posY);

            i++;
        }
        i=0;
        for(Enemy enemy : map.getEnemies()) {
            int posX = startX;
            int posY = 300 + i*20;

            gc.setFill(enemy.getColor());
            gc.fillRect(posX, posY-13, 14, 14);

            gc.strokeText("Enemy " + (i+1) + ": " + enemy.getTerritorySize(), posX + 18, posY);

            i++;
        }

        //
        gc.strokeText("R - Restart", startX, 480);
        gc.strokeText("P - Pause/Play", startX, 500);
        gc.strokeText("Esc - Back to menu", startX, 520);

        gc.setFont(new Font("System Regular", 26));
        if(game.paused)
            gc.strokeText("PAUSED", startX, 560);
        if(game.end) {
            gc.strokeText("THE END", startX, 600);

            ArrayList<Pair<Character, String>> winners = game.getWinners();

            int wonPosY = 650;
            gc.setFont(new Font("System Regular", 20));
            gc.strokeText("WON: ", startX, wonPosY);

            i=0;
            for(Pair<Character, String> pair : winners){
                gc.setFill(pair.getKey().getColor());
                gc.fillRect(startX, wonPosY + 8 + i*25, 20,20);
                gc.strokeText(pair.getValue() + ": " + pair.getKey().getTerritorySize(), startX + 25, wonPosY + (i+1)*25);
                ++i;
            }
        }
    }

    public void repaint(){
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        paint();
    }
}
