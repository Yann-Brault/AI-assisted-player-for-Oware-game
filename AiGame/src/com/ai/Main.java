package com.ai;

import com.ai.game.Board;
import com.ai.game.Position;

public class Main {

    public static void main(String[] args) {
        Board b = new Board();
        System.out.println(b);
        System.out.println();
        Position p = b.getactualPosition();
        System.out.println(p.toString());
        System.out.println();
        Position p2 = p.getNextPos(3);
        System.out.println(p2);


    }
}
