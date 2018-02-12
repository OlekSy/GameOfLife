package sample;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Controller {

    private Main main;
    private GraphicsContext gc;
    private Game game;
    private Stage stage;

    @FXML Button btn;
    @FXML Canvas canvas;

    public void initialize(){
        //initialize graphics context
        gc = canvas.getGraphicsContext2D();

        //Starting game
        game = new Game(gc);
        game.setDaemon(true);
    }

    //Function of button
    public void buttonOnAction(){
        game.start();
    }
}
