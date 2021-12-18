package com.ai;


import com.ai.game.Board;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        boolean J1ia = false;
        while (true) {
            System.out.println("Ia starts ? yes or no");
            String str = sc.nextLine();
            if (str.equalsIgnoreCase("yes")) {
                J1ia = true;
                break;
            }
            if (str.equalsIgnoreCase("no")) {
                break;
            }
        }
        Board b2 = new Board(J1ia);
        while (!b2.getActualPosition().isFinalPosition()) {
            b2.play();
            System.out.println(b2);
            System.out.printf("ai score : %d\n", b2.getPionsPrisOrdi());
            System.out.printf("player score : %d\n", b2.getPionsPrisJoueur());
        }
    }
}
