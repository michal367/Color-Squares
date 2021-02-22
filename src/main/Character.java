package main;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashSet;

public class Character {
    private HashSet<Vector2d> territory;
    private Color color;
    private GameMap map;

    public Character(Vector2d startPosition, Color color, GameMap map){
        territory = new HashSet<>();
        territory.add(startPosition);
        this.color = color;

        this.map = map;
    }


    public boolean expand(){
        HashSet<Vector2d> newPositionsTotal = new HashSet<>();
        for(Vector2d position : territory){
            ArrayList<Vector2d> newPositions = map.getEmptyPositionsAround(position);
            if(newPositions != null)
                newPositionsTotal.addAll(newPositions);
        }
        if(newPositionsTotal.size() != 0) {
            territory.addAll(newPositionsTotal);
            return true;
        }
        else
            return false;
    }


    public boolean isOccupied(Vector2d position){
        return territory.contains(position);
    }

    public int getTerritorySize(){
        return territory.size();
    }

    public HashSet<Vector2d> getTerritory(){
        return territory;
    }
    public Color getColor(){
        return color;
    }



}
