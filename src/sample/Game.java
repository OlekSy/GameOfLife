package sample;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.geometry.Point2D;

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
    private int numberOfCellsInARow;
    private int numberOfCellsInAColomn;
    private List<Cell> board;

    Game(GraphicsContext gc){
        this.gc = gc;
        canvas = gc.getCanvas();
        height = canvas.getHeight();
        width = canvas.getWidth();
        heightOfCell = 10.0;
        widthOfCell = 10.0;
        System.out.println("Initialized\n" + "Width: " + width + "\nHeight: " + height);

        board = new ArrayList<>();
        numberOfCellsInARow = (int)(width / heightOfCell);
        numberOfCellsInAColomn = (int)(height / heightOfCell);
        for(int i = 0; i < numberOfCellsInARow; i++){
            for(int j = 0; j < height / widthOfCell; j++){
                board.add(new Cell(i, j, false));
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
        for(int i = 0; i < numberOfCellsInARow; i++){
            for(int j = 0; j < numberOfCellsInAColomn; j++) {
                int randomNum = ThreadLocalRandom.current().nextInt(0, 2);
                if(randomNum == 1) {
//                    System.out.println(board[0].length);
                    findCell(i, j).setAlive(true);
                    gc.fillRect(i * widthOfCell + 1, j * heightOfCell + 1, heightOfCell - 2, widthOfCell - 2);
                }
            }
//            System.out.println(randomNum);
        }
    }

    private void refreshBoard(){
        for(int i = 0; i < numberOfCellsInARow; i++){
            for(int j = 0; j < numberOfCellsInAColomn; j++){
                if(!findCell(i, j).isAlive()){
                    gc.clearRect(i * widthOfCell + 1, j * heightOfCell + 1, heightOfCell - 2, widthOfCell - 2);
                } else {
                    gc.fillRect(i * widthOfCell + 1, j * heightOfCell + 1, heightOfCell - 2, widthOfCell - 2);
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
        for (int i = 0; i < numberOfCellsInARow; i++) {
            for (int j = 0; j < numberOfCellsInAColomn; j++) {
                int counter = 0;
                if (j > 0) {
                    if (i > 0) {
                        if(findCell(i - 1, j - 1).isAlive())
                            counter++;
                    }
                    if(findCell(i, j - 1).isAlive())
                        counter++;
                    if (i < numberOfCellsInARow - 1) {
                        if(findCell(i + 1, j - 1).isAlive())
                            counter++;
                    }
                }
                if (i > 0) {
                    if(findCell(i - 1, j).isAlive())
                        counter++;
                }
                if (i < numberOfCellsInARow - 1) {
                    if(findCell(i + 1, j).isAlive())
                        counter++;
                }
                if (j < numberOfCellsInAColomn - 1) {
                    if (i > 0) {
                        if(findCell(i - 1, j + 1).isAlive())
                            counter++;
                    }
                    if(findCell(i, j + 1).isAlive())
                        counter++;
                    if (i < numberOfCellsInARow - 1) {
                        if(findCell(i + 1, j + 1).isAlive())
                            counter++;
                    }
                }
                if (counter <= 1 || counter >= 4) {
                    Cell currentCell = findCell(i, j);
                    if(currentCell.isAlive()) {
                        currentCell.setAlive(false);
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
                                || (i == numberOfCellsInARow - 1 && (randomNum == 1 || randomNum == 2 || randomNum == 3))
                                || (j == numberOfCellsInAColomn - 1 && (randomNum == 1 || randomNum == 4 || randomNum == 6)))){
                            found = false;
                        }
                    }
                    switch (randomNum) {
                        case 1: {
                            findCell(i + 1, j + 1).setAlive(true);
                            break;
                        }
                        case 2: {
                            findCell(i + 1, j).setAlive(true);
                            break;
                        }
                        case 3: {
                            findCell(i + 1, j - 1).setAlive(true);
                            break;
                        }
                        case 4: {
                            findCell(i, j + 1).setAlive(true);
                            break;
                        }
                        case 5: {
                            findCell(i, j - 1).setAlive(true);
                            break;
                        }
                        case 6: {
                            findCell(i - 1, j + 1).setAlive(true);
                            break;
                        }
                        case 7: {
                            findCell(i - 1, j).setAlive(true);
                            break;
                        }
                        case 8: {
                            findCell(i - 1, j - 1).setAlive(true);
                            break;
                        }
                    }
                }
            }
        }
        return isChanged;
    }

    private Cell findCell(int x, int y){
        int count = 0;
        for(int i = 0; i < numberOfCellsInARow; i++){
            for(int j = 0; j < numberOfCellsInAColomn; j++){
                Cell tempCell = board.get(count);
                if(j == 0) {
                    if (tempCell.getCoordinates().getX() != x) {
                        count += numberOfCellsInAColomn;
                        break;
                    }
                }
                if(tempCell.getCoordinates().getY() == y){
                    return tempCell;
                }
                count++;
            }
        }
        return null;
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
