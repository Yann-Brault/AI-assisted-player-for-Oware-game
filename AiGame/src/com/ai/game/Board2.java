package com.ai.game;

import com.ai.ai.Ai;

public class Board2 {
    private final static int size = 16;
    private final static int initialGraine = 4;
    private final int[] tableauBleu = new int[]{initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine,initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine,initialGraine, initialGraine, initialGraine,initialGraine};
    private final int[] getTableauRouge = new int[]{initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine,initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine,initialGraine, initialGraine, initialGraine,initialGraine};
    private boolean iaTurn;
    private int PionsPrisJoueur; //pions pris par le joueur
    private int PionsPrisOrdi; // pions pris par l'ordi
    private final Ai ai;

    public Board2(boolean iaBegin) {
        this.ai = new Ai();
        this.iaTurn = iaBegin;
    }




}
