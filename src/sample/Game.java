package sample;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;


public class Game extends Thread {

    private GraphicsContext gc;
    private Canvas canvas;
    private double height;
    private double width;
    private double heightOfCell;
    private double widthOfCell;
    private int[][] board;

    Game(GraphicsContext gc){
        this.gc = gc;
        canvas = gc.getCanvas();
        height = canvas.getHeight();
        width = canvas.getWidth();
        heightOfCell = 10.0;
        widthOfCell = 10.0;
        System.out.println("Initialized\n" + "Width: " + width + "\nHeight: " + height);

        board = new int[(int)(width/widthOfCell)][(int)(height/heightOfCell)];
        for(int i = 0; i < height / heightOfCell; i++){
            for(int j = 0; j < width / widthOfCell; j++){
                board[j][i] = 0;
//                System.out.println(i + " " + j);
            }
        }
        initializeGame();
    }

    private void initializeGame(){
        gc.setLineWidth(1);
        gc.setStroke(Color.GREY);
        for(int i = 0; i <= width; i += widthOfCell){
            gc.strokeLine(i, 0, i, height);
        }
        for(int i = 0; i <= height; i += heightOfCell){
            gc.strokeLine(0, i, width, i);
        }
        gc.setFill(Color.RED);
        for(int i = 0; i < board[0].length; i++){
            for(int j = 0; j < board.length; j++) {
                int randomNum = ThreadLocalRandom.current().nextInt(0, 2);
                if(randomNum == 1) {
//                    System.out.println(board[0].length);
                    board[j][i] = 1;
                    gc.fillRect(j * widthOfCell + 1, i * heightOfCell + 1, heightOfCell - 2, widthOfCell - 2);
                }
            }
//            System.out.println(randomNum);
        }
    }

    private void refreshBoard(){
        for(int i = 0; i < board[0].length; i++){
            for(int j = 0; j < board.length; j++){
                if(board[j][i] == 0){
                    gc.clearRect(j * widthOfCell + 1, i * heightOfCell + 1, heightOfCell - 2, widthOfCell - 2);
                } else {
                    gc.fillRect(j * widthOfCell + 1, i * heightOfCell + 1, heightOfCell - 2, widthOfCell - 2);
                }
//                try {
//                    Thread.sleep(10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        }
    }

    private boolean boardIsChanged(){
        boolean isChanged = false;
        for (int i = 0; i < board[0].length; i++) {
            for (int j = 0; j < board.length; j++) {
                int counter = 0;
                if (j > 0) {
                    if (i > 0) {
                        counter += board[j - 1][i - 1];
                    }
                    counter += board[j - 1][i];
                    if (i < board[0].length - 1) {
                        counter += board[j - 1][i + 1];
                    }
                }
                if (i > 0) {
                    counter += board[j][i - 1];
                }
                if (i < board[0].length - 1) {
                    counter += board[j][i + 1];
                }
                if (j < board.length - 1) {
                    if (i > 0) {
                        counter += board[j + 1][i - 1];
                    }
                    counter += board[j + 1][i];
                    if (i < board[0].length - 1) {
                        counter += board[j + 1][i + 1];
                    }
                }
                if (counter <= 1 || counter >= 4) {
                    if(board[j][i] == 1) {
                        board[j][i] = 0;
                        isChanged = true;
                    }
                }
                //Check if it's okay
                else {
                    boolean found = true;
                    int randomNum = 0;
                    while(found){
                        randomNum = ThreadLocalRandom.current().nextInt(1, 9);
                        if(!((i == 0 && (randomNum == 6 || randomNum == 7 || randomNum == 8))
                                || (j == 0 && (randomNum == 3 || randomNum == 5 || randomNum == 8))
                                || (i == board[0].length - 1 && (randomNum == 1 || randomNum == 2 || randomNum == 3))
                                || (j == board.length - 1 && (randomNum == 1 || randomNum == 4 || randomNum == 6)))){
                            found = false;
                        }
                    }
                    switch (randomNum) {
                        case 1: {
                            board[j + 1][i + 1] = 1;
                            break;
                        }
                        case 2: {
                            board[j][i + 1] = 1;
                            break;
                        }
                        case 3: {
                            board[j - 1][i + 1] = 1;
                            break;
                        }
                        case 4: {
                            board[j + 1][i] = 1;
                            break;
                        }
                        case 5: {
                            board[j - 1][i] = 1;
                            break;
                        }
                        case 6: {
                            board[j + 1][i - 1] = 1;
                            break;
                        }
                        case 7: {
                            board[j][i - 1] = 1;
                            break;
                        }
                        case 8: {
                            board[j - 1][i - 1] = 1;
                            break;
                        }
                    }
                }
            }
        }
        return isChanged;
    }

    @Override
    public void run(){
        boolean isChanged = true;
        while(isChanged) {
            isChanged = boardIsChanged();
            refreshBoard();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
