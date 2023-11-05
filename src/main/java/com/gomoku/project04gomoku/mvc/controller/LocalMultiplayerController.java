package com.gomoku.project04gomoku.mvc.controller;

import com.gomoku.project04gomoku.app.logic.Game;
import com.gomoku.project04gomoku.app.models.Board;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;



public class LocalMultiplayerController {

    @FXML
    private GridPane gridPane;

    private Game game;

    public void initialize() {
        game = new Game();
        setupBoard();
    }

    private void setupBoard() {
        // Set up the board with clickable panes
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                Pane pane = new Pane();

                // Create final variables for the lambda expression
                final int finalI = i;
                final int finalJ = j;

                pane.setOnMouseClicked(event -> handleCellClick(finalI, finalJ));
                gridPane.add(pane, j, i);
            }
        }
    }


    private void handleCellClick(int x, int y) {
        // Check if the game is already over, do nothing if it is
        if (game.isGameOver()) {
            return;
        }

        game.handleCellClick(x, y);
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

    private void updateBoard() {
        // Update the board UI to reflect the current game state
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                Pane pane = (Pane) getNodeFromGridPane(gridPane, j, i);
                pane.getChildren().clear(); // Clear previous UI components
                int player = game.getBoard().getCell(i, j);
                if (player != 0) {
                    Circle circle = new Circle(20); // Just an example, adjust the size as needed
                    circle.setFill(player == 1 ? Color.BLACK : Color.WHITE);
                    pane.getChildren().add(circle);
                }
            }
        }
    }

    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        // This method finds a node within the GridPane by its row and column index
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }
}
