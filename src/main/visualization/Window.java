package main.visualization;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.Game;
import main.NumberChangeListener;
import main.NumberPositiveChangeListener;
import main.Vector2d;

import java.io.InputStream;
import java.util.Objects;


public class Window extends Application{

    private Stage window;
    private Scene menu, gameScene;
    private Timeline timeline;

    private Vector2d size;

    private GridPane grid;
    private TextField sizeInput;
    private TextField numberOfPlayersInput;
    private TextField numberOfEnemiesInput;
    private TextField numberOfObstaclesInput;
    private CheckBox borders;
    private Button startButton;
    private Renderer renderer;

    private Game game;

    private long startTime;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception {
        window = stage;
        size = new Vector2d(800, 800);

        initComponents();

        // MENU SCENE
        menu = new Scene(grid, size.x, size.y);

        // WINDOW
        window.setTitle("Color Squares");
        InputStream stream = getClass().getClassLoader().getResourceAsStream("icon.png");
        window.getIcons().add(new Image(Objects.requireNonNull(stream)));
        window.setScene(menu);
        window.show();
    }

    private void initComponents(){
        // GRID
        grid = new GridPane();
        grid.setPadding(new Insets(10,10,10,10));
        grid.setVgap(8);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);

        // size
        Label sizeLabel = new Label("Size: ");
        GridPane.setConstraints(sizeLabel, 0,0);

        sizeInput = new TextField("10");
        sizeInput.focusedProperty().addListener(new NumberPositiveChangeListener(sizeInput));
        GridPane.setConstraints(sizeInput, 1,0);

        // number of players
        Label numberOfPlayersLabel = new Label("Number of players: ");
        GridPane.setConstraints(numberOfPlayersLabel, 0,1);

        numberOfPlayersInput = new TextField("1");
        numberOfPlayersInput.focusedProperty().addListener(new NumberChangeListener(numberOfPlayersInput));
        GridPane.setConstraints(numberOfPlayersInput, 1,1);

        // number of enemies
        Label numberOfEnemiesLabel = new Label("Number of enemies: ");
        GridPane.setConstraints(numberOfEnemiesLabel, 0,2);

        numberOfEnemiesInput = new TextField("3");
        numberOfEnemiesInput.focusedProperty().addListener(new NumberChangeListener(numberOfEnemiesInput));
        GridPane.setConstraints(numberOfEnemiesInput, 1,2);

        // number of obstacles
        Label numberOfObstaclesLabel = new Label("Number of obstacles: ");
        GridPane.setConstraints(numberOfObstaclesLabel, 0,3);

        numberOfObstaclesInput = new TextField("12");
        numberOfObstaclesInput.focusedProperty().addListener(new NumberChangeListener(numberOfObstaclesInput));
        GridPane.setConstraints(numberOfObstaclesInput, 1,3);

        // borders checkbox
        borders = new CheckBox("Borders");
        borders.setSelected(true);
        GridPane.setConstraints(borders, 0,4);

        // start
        startButton = new Button("START");
        startButton.setOnAction(e -> startGame());
        GridPane.setConstraints(startButton, 0,5);

        grid.getChildren().addAll(
                sizeLabel, sizeInput,
                numberOfPlayersLabel, numberOfPlayersInput,
                numberOfEnemiesLabel, numberOfEnemiesInput,
                numberOfObstaclesLabel, numberOfObstaclesInput,
                borders,
                startButton);
    }


    private void startGame(){
        try {
            if(sizeInput.getText().equals("") ||
                numberOfPlayersInput.getText().equals("") ||
                numberOfEnemiesInput.getText().equals("") ||
                numberOfObstaclesInput.getText().equals(""))
                throw new IllegalArgumentException("Fill in all fields");

            // size
            int mapSize = Integer.parseInt(sizeInput.getText());
            Vector2d mapSizeVector = new Vector2d(mapSize, mapSize);

            // number of players
            int numberOfPlayers = Integer.parseInt(numberOfPlayersInput.getText());

            // number of enemies
            int numberOfEnemies = Integer.parseInt(numberOfEnemiesInput.getText());

            // number of obstacles
            int numberOfObstacles = Integer.parseInt(numberOfObstaclesInput.getText());

            if(numberOfPlayers > 8){
                throw new IllegalArgumentException("Too many players. There can be max 8 players");
            }
            if(numberOfEnemies > 8){
                throw new IllegalArgumentException("Too many enemies. There can be max 8 enemies");
            }
            if(numberOfPlayers + numberOfEnemies + numberOfObstacles > mapSize*mapSize) {
                throw new IllegalArgumentException("Too little space to fit all elements");
            }

            game = new Game(mapSizeVector, numberOfPlayers, numberOfEnemies, numberOfObstacles);

            // RENDERER
            renderer = new Renderer(size, game, borders.isSelected());
            gameScene = new Scene(renderer);

            window.setScene(gameScene);

            gameScene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                public void handle(KeyEvent ke) {
                    if (ke.getCode() == KeyCode.P) {
                        ke.consume();
                        if(!game.paused) {
                            game.paused = true;
                            renderer.repaint();
                            timeline.pause();
                        }
                        else {
                            game.paused = false;
                            timeline.play();
                        }
                    }
                    else if (ke.getCode() == KeyCode.R) {
                        game.restart();
                        renderer.repaint();
                    }
                    else if (ke.getCode() == KeyCode.ESCAPE){
                        timeline.pause();
                        window.setScene(menu);
                    }
                }
            });

            int duration = 33;
            startTime = System.currentTimeMillis();
            timeline = new Timeline(new KeyFrame(
                    Duration.millis(duration),
                    e -> {
                        if(game.isPlayersSet) {
                            long elapsedTime = System.currentTimeMillis() - startTime;
                            if(elapsedTime >= game.animationTime){
                                startTime = System.currentTimeMillis();
                                renderer.repaint();
                                game.run();
                            }
                        }
                        else{
                            renderer.repaint();
                            game.run();
                        }
                    }));
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
        }
        catch(IllegalArgumentException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
        }
        catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }

}
