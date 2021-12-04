package com.ai.Game2;

import com.ai.ai.Ai;

public class Board2 {
    private final static int size = 16;
    private final static int initialGraine = 2;
    private final int[] tableauBleu = new int[]{initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine,initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine,initialGraine, initialGraine, initialGraine,initialGraine};
    private final int[] tableauRouge = new int[]{initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine,initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine,initialGraine, initialGraine, initialGraine,initialGraine};
    private boolean iaTurn;
    private final boolean iaJ1;
    private int PionsPrisJoueur; //pions pris par le joueur
    private int PionsPrisOrdi; // pions pris par l'ordi
    private final static int[] case_J1 = new int[]{1,3,5,7,9,11,13,15};
    private final static int[] case_J2 = new int[]{2,4,6,8,10,12,14,16};
    private final static int sizePlayerCase = 8;
    private final Ai ai;

    public Board2(boolean iaBegin) {
        this.ai = new Ai();
        this.iaTurn = iaBegin;
        this.iaJ1 = iaBegin;
    }

    public int[] getTableauBleu() {
        return tableauBleu;
    }

    public int[] getTableauRouge() {
        return tableauRouge;
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
    public void addPionsPrisJoueur(int pions) {
        PionsPrisJoueur += pions;
    }

    public void addPionsPrisOrdi(int pions) {
        PionsPrisOrdi += pions;
    }


    public Ai getAi() {
        return ai;
    }
    public Position2 getActualPosition(){
        return new Position2(tableauBleu,tableauRouge,iaTurn,iaJ1,getPionsPrisJoueur(),getPionsPrisOrdi());
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("| ");
        for (int i = 0; i < size/2; i++) {
            sb.append(String.format("\033[34m b:%d",tableauBleu[i])).append(" ").append(String.format("\033[31m r:%d \033[0m", tableauRouge[i])).append(" | ");
        }

        sb.append("\n");
        sb.append("-".repeat(size * 6 +9));
        sb.append("\n");
        sb.append("| ");
        for (int i = size-1; i >= size/2; i--) {
            sb.append(String.format("\033[34m b:%d",tableauBleu[i])).append(" ").append(String.format("\033[31m r:%d \033[0m", tableauRouge[i])).append(" | ");
        }
        return sb.toString();
    }

}
