package com.gomoku.project04gomoku.mvc.ViewModel;

import com.gomoku.project04gomoku.app.logic.Game;
import com.gomoku.project04gomoku.app.models.Board;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.shape.Rectangle;

import java.util.Objects;
import java.util.Optional;


public class LocalMultiplayerController {
    @FXML
    public Button start;
    @FXML
    public GridPane gridPane;

    @FXML
    public Pane[] cell;

    private Game game;

    @FXML
    public void initialize() {
        //gridPane=new GridPane();
        game = new Game();
        setupBoard();
    }

    private void updateBoard() {
        // Update the board UI to reflect the current game state
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                Pane pane = (Pane) getNodeFromGridPane(gridPane, j, i);
                assert pane != null;
                pane.getChildren().clear(); // Clear previous UI components
                int player = game.getBoard().getCell(i, j);
                if (player != 0) {
                    Circle circle = new Circle(10); // Just an example, adjust the size as needed
                    circle.setFill(player == 1 ? Color.BLACK : Color.WHITE);
                    pane.getChildren().add(circle);
                }
            }
        }
    }

    @FXML
    private void setupBoard() {
        // Set up the board with clickable panes
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                Pane pane = new Pane();
                pane.setPrefSize(30, 30); // Set Pane size
                pane.setBorder(new Border(new BorderStroke(Color.BLACK,
                        BorderStrokeStyle.SOLID, null, new BorderWidths(1)))); // setBoarder

                // Create final variables for the lambda expression
                final int finalI = i ;
                final int finalJ = j;

                pane.setOnMouseClicked(event -> handleCellClick(finalI, finalJ));
                gridPane.add(pane, j, i);
            }
        }
        //testNode(gridPane);
    }

    @FXML
    private void handleCellClick(int x, int y) {
        // Check if the game is already over, do nothing if it is
        game.handleCellClick(x, y);
        // Add this line for debugging
        System.out.println("Clicked on cell: " + x + ", " + y);
        updateBoard();

        if (game.checkWin(x, y)) {
            // Handle win
            displayEndGameMessage("Player " + (game.getCurrentPlayer() == 1 ? "Black" : "White") + " wins!");
            game.setGameOver(true);
        } else if (game.getBoard().isFull()) {
            // Handle draw
            displayEndGameMessage("It's a draw!");
            game.setGameOver(true);
        }
    }

    private void displayEndGameMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

        ButtonType restartButton = new ButtonType("Restart");
        alert.getButtonTypes().add(restartButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == restartButton) {
            restartGame();
        }
    }

    @FXML
    public void startGame() {
        initialize();
        game.startGame();
        setupBoard();
        System.out.println("Start Game!");

    }

    @FXML
    public void restartGame() {
        game.restartGame();
        updateBoard();
        System.out.println("Restart Game!");
    }

    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        // This method finds a node within the GridPane by its row and column index

        for (Node node : gridPane.getChildren()) {
            Integer IntegerCol = col;
            Integer IntegerRow = row;
            if (Objects.equals(GridPane.getColumnIndex(node), IntegerCol) && Objects.equals(GridPane.getRowIndex(node), IntegerRow)) {
                return node;
            }

//            System.out.println(GridPane.getColumnIndex(node));
//            System.out.println(GridPane.getRowIndex(node));
        }

        return null;
    }

    private void testNode(GridPane gridPane) {
        System.out.println("TestNode的");
        for (Node node : gridPane.getChildren()) {

            System.out.println(GridPane.getColumnIndex(node));
            System.out.println(GridPane.getRowIndex(node));
        }
    }

}
