package gameClient;

import java.awt.*;

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
