package com.ai;

import com.ai.Game2.Position2;
import com.ai.ai.Ai;
import com.ai.game.Board;
import com.ai.Game2.Board2;
import com.ai.game.Position;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        Board2 b2 = new Board2(true);


        while ( !b2.getActualPosition().isFinalPosition()) {
            b2.play();
            System.out.println(b2);
            //System.out.println(b2.getActualPosition().nbcoupValide(1));
            System.out.printf("ai score : %d\n", b2.getPionsPrisOrdi());
            System.out.printf("player score : %d\n", b2.getPionsPrisJoueur());
        }


    }
}
