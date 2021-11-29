package com.ai;

import com.ai.ai.Ai;
import com.ai.game.Board;
import com.ai.game.Position;

public class Main {

    public static void main(String[] args) {
        Board b = new Board();
        System.out.println(b);
        System.out.println();
        Position p = b.getactualPosition();
        Ai ai = new Ai();
        System.out.println(ai.valeurMinMax(p,p.isIaTurn(),0,2));
        Position newpos =p.getNextPos(3);
        System.out.println(ai.evatualte(newpos, newpos.isIaTurn(), 0));


//        System.out.println(ai.valeurMinMax(p,p.isIaTurn(),0,10));


    }
}
