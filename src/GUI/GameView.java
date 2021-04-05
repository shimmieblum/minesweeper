package GUI;

import Game.GameEngine;
import GameSetup.Board;
import GameSetup.GameType;
import GameSetup.Location;
import javafx.geometry.Dimension2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.animation.*;
// import javafx.util.Pair;

import java.awt.Toolkit;
import java.util.*;

public class GameView {

    private StackPane pane;
    private BorderPane outerPane;
    private BorderPane centerPane;
    private GridPane gridView;

    private Image mineImage;
    private Image flagImage;

    private GameEngine game;
    private String styleString;
    private GameType difficulty;

    private List<Location> locations;
    private Map<Location, Label> locationsLabels;

    // list of mines to be revealed.
    private List<ImageView> mines;
    private MainWindowView mainWindowView;
    private long startTime;
    private double sqrWidth;
    private double sqrHeight;

    public GameView(GameType difficulty, String styleString,  double sqrWidth, double sqrHeight, MainWindowView mainWindowView){

        this.difficulty = difficulty;
        this.mainWindowView = mainWindowView;
        this.sqrWidth = sqrWidth;
        this.sqrHeight = sqrHeight;

        startTime = System.currentTimeMillis();

        game = new GameEngine(difficulty);
        this.styleString = styleString;
        pane = new StackPane();
        outerPane = new BorderPane();
        pane.getChildren().add(outerPane);
        centerPane = new BorderPane();
        outerPane.setCenter(centerPane);
        BorderPane.setAlignment(centerPane, Pos.CENTER);

        mines = new LinkedList<>();
        mineImage = new Image(getClass().getResourceAsStream("Components/mine.png"));
        flagImage = new Image(getClass().getResourceAsStream("components/red_flag.png"));

        locations = new ArrayList<>();
        locationsLabels = new HashMap<>();

        createGridView();

    }

    public Pane getPane() {
        return pane;
    }

    public void createGridView() {
        gridView = new GridPane();
        Board board = game.getBoard();
        // create the requested grid and relate labels to their locations
        for(int x = 0; x < board.getNoColumns(); x++) {
            for (int y = 0; y < board.getNoRows(); y++) {
                Location location = board.getLocation(x,y);
                Label label = new Label();
                label.setOnMouseClicked(this::mouseClick);
                label.setId("unrevealedLocationLabel");
                label.getStylesheets().add(styleString);
                
                label.setPrefWidth(sqrWidth);
                label.setPrefHeight(sqrHeight);
                if(location.isMine()) {
                    ImageView mineView = new ImageView(mineImage);
                    mineView.setFitWidth(sqrWidth);
                    mineView.setFitHeight(sqrHeight);
                    mineView.setVisible(false);
                    label.setGraphic(mineView);
                    mines.add(mineView);
                }
                gridView.add(label, x, y);
                locations.add(location);
                locationsLabels.put(location,label);

            }
        }
        gridView.setGridLinesVisible(true);

        ScrollPane sp = new ScrollPane();
        BorderPane.setAlignment(sp, Pos.CENTER);

        sp.setContent(gridView);
        
        centerPane.setTop(timerLabel());
        centerPane.setCenter(sp);
        // calculate the dimensions of the monitor so the size of the window is created appropriately

        Rectangle2D monitor = Screen.getPrimary().getBounds();
        double screenWidth = Math.min(monitor.getWidth() - 100, (sqrWidth*difficulty.getColumns()) + 100);
        double screenHeight = Math.min(monitor.getHeight() - 100, (sqrHeight*difficulty.getRows()) + 100);
        System.out.println(difficulty.getColumns() +", "+ difficulty.getRows());

        // set the size of the window to show the whole board
        mainWindowView.setSize(screenWidth, screenHeight);
        optionsBar();
    }

    private Label timerLabel(){
        Label timerLabel = new Label();
        timerLabel.setId("timeLabel");
        timerLabel.getStylesheets().add(styleString);
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                long elapsedMillis = System.currentTimeMillis() - startTime ;
                timerLabel.setText("time elapsed: " + Long.toString(elapsedMillis / 1000));
            }
        }.start();

        return timerLabel;
        
    }


    private void optionsBar() {
        HBox options = new HBox();

        Button quitButton = new Button("Quit");
        Button restartButton = new Button("Restart");

        quitButton.setOnAction(e -> endGame("Bye Bye"));
        restartButton.setOnAction(e -> resetGame());

        options.getChildren().addAll(quitButton,restartButton);
        outerPane.setTop(options);
    }

    private void updateView() {

        Set<Location> locations = locationsLabels.keySet();
        for (Location location: locations) {
            if(location.isChanged()) {
                Label label = locationsLabels.get(location);
                if (location.isRevealed()) {
                    label.setText("" + location.getAdjacentMines());
                    label.setId("revealedLabel");
                }
                else if (location.isFlagged()) {
                    ImageView flagView = new ImageView(flagImage);
                    flagView.setFitWidth(sqrWidth);
                    flagView.setFitHeight(sqrHeight);
                    label.setGraphic(flagView);
                }
                else if (!location.isFlagged()) {
                    label.setGraphic(null);
                }
            }
            location.setChanged(false);
        }
    }

    private void mouseClick(MouseEvent event) {

        int x = GridPane.getColumnIndex((Node)event.getSource());
        int y = GridPane.getRowIndex((Node) event.getSource());

        if(event.getButton() == MouseButton.PRIMARY ) {
            game.leftClick(x,y);
            if (game.gameOver()) {
                gameOver();
                return;
            }
        }
        if (event.getButton() == MouseButton.SECONDARY) {
            game.rightClick(x,y);
        }
        if(victoryConditionsMet()) {
            gameWon();
        }
        else {
            updateView();
        }
    }

    public void resetGame() {
        game = new GameEngine(difficulty);
        outerPane.setBottom(null);
        startTime = System.currentTimeMillis();
        createGridView();
    }

    public void gameOver() {
        Label label = new Label("Game over");
        label.setFont(new Font("Ariel", 50));
        outerPane.setBottom(label);
        showMines();

    }

    public void endGame(String endMessage) {

        Alert endGameDialog = new Alert(Alert.AlertType.CONFIRMATION);

        endGameDialog.setHeaderText(endMessage);
        endGameDialog.initModality(Modality.WINDOW_MODAL);
        endGameDialog.setContentText("");

        ButtonType restartButtonType = new ButtonType("RESTART");
        ButtonType mainWindowButtonType = new ButtonType("MAIN-WINDOW");

        endGameDialog.getButtonTypes().setAll(restartButtonType,mainWindowButtonType);

        Optional<ButtonType> result = endGameDialog.showAndWait();

        if(result.get() == restartButtonType) {
            resetGame();
        }
        else if(result.get() == mainWindowButtonType) {
            mainWindowView.setMainWindow();
        }

    }

    public void showMines () {
        mines.forEach(e -> e.setVisible(true));
    }

    private boolean victoryConditionsMet() {
        boolean minesNotFound = false;
        Board board = game.getBoard();
        Iterator<Location> it = board.getLocationList().iterator();
        while(it.hasNext() && !minesNotFound) {
            Location location = it.next();
            if(!location.isMine() && !location.isRevealed()) {
                minesNotFound = true;
            }
        }
        return !minesNotFound;
    }

    private void gameWon (){
        String string = "Well done, you win";
        string += "\n time: " + (((System.currentTimeMillis() - startTime )/ 1000) + "s");
        endGame(string);
    }




}
