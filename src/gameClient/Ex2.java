package gameClient;

import java.awt.*;

/**
 * Running class of the game
 * Option 1 - Run through file .jar
 * Option 2 - Run through the CMD
 */
public class Ex2 {
    public static void main(String[] args){
        if(args.length == 0){
            Frame login = new LoginGameFrame();
        }else{
            Thread client = new Thread(new MainGame(Long.parseLong(args[0]),Integer.parseInt(args[1])));
            client.start();
        }
    }
}
