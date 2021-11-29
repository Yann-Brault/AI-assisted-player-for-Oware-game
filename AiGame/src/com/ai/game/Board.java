package com.ai.game;

import java.util.Arrays;

public class Board {
    private final static int size = 6;
    private final static int initialGraine = 4;
    private final int[] caseJoueur = new int[]{1, 1, 1, initialGraine, initialGraine, initialGraine};
    private final int[] caseOrdi = new int[]{initialGraine, initialGraine, initialGraine, 5, initialGraine, initialGraine};
    private boolean iaTurn = true;
    private int PionsPrisJoueur; //pions pris par le joueur
    private int PionsPrisOrdi; // pions pris par l'ordi

    public Board() {
    }


    public int[] getCaseJoueur() {
        return caseJoueur;
    }

    public int[] getCaseOrdi() {
        return caseOrdi;
    }

    public static int getSize() {
        return size;
    }

    public static int getInitialGraine() {
        return initialGraine;
    }


    public int getCaseJoueurAt(int i) {
        return caseJoueur[i];
    }

    public int getCaseOrdiAt(int i) {
        return caseOrdi[i];
    }

    public boolean isIaTurn() {
        return iaTurn;
    }

    public void setIaTurn(boolean iaTurn) {
        this.iaTurn = iaTurn;
    }

    public int getPionsPrisJoueur() {
        return PionsPrisJoueur;
    }

    public void setPionsPrisJoueur(int pionsPrisJoueur) {
        PionsPrisJoueur = pionsPrisJoueur;
    }

    public int getPionsPrisOrdi() {
        return PionsPrisOrdi;
    }

    public void setPionsPrisOrdi(int pionsPrisOrdi) {
        PionsPrisOrdi = pionsPrisOrdi;
    }


    public Position getactualPosition(){
        int[] copycaseJoueur = Arrays.copyOf(caseJoueur,size);
        int[] copyCaseOrdi = Arrays.copyOf(caseOrdi,size);
        return new Position(copycaseJoueur,copyCaseOrdi,isIaTurn(),getPionsPrisJoueur(),getPionsPrisOrdi());
    }





    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("| ");
        for (int i = size - 1; i >= 0; i--) {
            sb.append(caseOrdi[i]).append(" | ");
        }
        sb.append("\n");
        sb.append("-".repeat(size * 4 + 1));
        sb.append("\n");
        sb.append("| ");
        for (int i = 0; i < size; i++) {
            sb.append(caseJoueur[i]).append(" | ");
        }
        return sb.toString();
    }
}
