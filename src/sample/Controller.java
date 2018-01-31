package sample;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;

public class Controller {

    Main main;
    GraphicsContext gc;
    Game game;

    @FXML Button btn;
    @FXML Canvas canvas;

    public void initialize(){
        //initialize graphics context
        gc = canvas.getGraphicsContext2D();

        //Starting game
        game = new Game(gc);
    }

    //Function of button
    public void buttonOnAction(){
        game.run();
    }

    //Set main as main
    public void setMain(Main main){
        this.main = main;
    }
}
