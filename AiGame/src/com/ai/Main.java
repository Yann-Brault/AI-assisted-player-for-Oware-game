package com.ai;

import com.ai.Game2.Position2;
import com.ai.ai.Ai;
import com.ai.game.Board;
import com.ai.Game2.Board2;
import com.ai.game.Position;

public class Main {

    public static void main(String[] args) {
        Board b = new Board(false);
        Position depart = b.getactualPosition();
        //System.out.println(ai.valeurMinMax(depart, depart.isIaTurn(), 0,5));
//        Position p2 = depart.getNextPos(3);
//        System.out.println(p2.getPionsPrisOrdi());
        Board2 b2 = new Board2(false);
//        System.out.println(b2);
//        System.out.println();
//        Position2 p2 = b2.getActualPosition().getNextPosition(15,false);
//        System.out.println(p2);
//        System.out.println(p2.isFinalPosition());


        while ( !b2.getActualPosition().isFinalPosition()) {
            b2.play();
            System.out.println(b2);
            System.out.printf("ai score : %d\n", b2.getPionsPrisOrdi());
            System.out.printf("player score : %d\n", b2.getPionsPrisJoueur());
        }
//        System.out.println(b2.getAi().numPlayer);
//        Position2[] pos = b2.getActualPosition().getNextPositions(b2.getAi().numPlayer);
//        int acc = 0 ;
//        for(Position2 posi : pos){
//            System.out.println();
//            System.out.println();
//            System.out.println();
//            System.out.println();
//            System.out.println("position index " + acc);
//            System.out.println(posi);
//            acc ++ ;
//        }


    }
}
