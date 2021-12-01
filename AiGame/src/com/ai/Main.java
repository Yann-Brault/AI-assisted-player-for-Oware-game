package com.ai;

import com.ai.ai.Ai;
import com.ai.game.Board;
import com.ai.game.Position;

public class Main {

    public static void main(String[] args) {
        Board b = new Board(false);
        Position depart = b.getactualPosition();
        Ai ai = new Ai();
        //System.out.println(ai.valeurMinMax(depart, depart.isIaTurn(), 0,5));
//        Position p2 = depart.getNextPos(3);
//        System.out.println(p2.getPionsPrisOrdi());

        while ( !b.getactualPosition().IsFinalPosition()) {
            b.play();
            System.out.println(b);
            System.out.printf("ai score : %d\n", b.getPionsPrisOrdi());
            System.out.printf("player score : %d\n", b.getPionsPrisJoueur());
        }
    }
}
