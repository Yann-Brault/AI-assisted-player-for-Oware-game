package com.ai;

import com.ai.game.Board;
import com.ai.game.Position;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        Board b = new Board();
        while (b.getPionsPrisOrdi() != 25 || b.getPionsPrisJoueur() != 25) {
            b.play();
            System.out.println(b);
            System.out.printf("ai score : %d\n", b.getPionsPrisOrdi());
            System.out.printf("player score : %d\n", b.getPionsPrisJoueur());
        }

    }
}
