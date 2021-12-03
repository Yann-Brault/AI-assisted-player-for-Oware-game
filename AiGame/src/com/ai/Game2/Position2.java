package com.ai.Game2;

import java.util.Arrays;

public class Position2 {
    private final static int size = 16;
    private int[] tableauBleu;
    private int[] tableauRouge;
    private boolean iaTurn;
    private int PionsPrisJoueur; //pions pris par le joueur
    private int PionsPrisOrdi; // pions pris par l'ordi
    private final static int[] case_J1 = new int[]{1,3,5,7,9,11,13,15};
    private final static int[] case_J2 = new int[]{2,4,6,8,10,12,14,16};
    private final static int sizePlayerCase = 8;

    public Position2(int[] tableauBleu, int[] getTableauRouge, boolean iaTurn, int pionsPrisJoueur, int pionsPrisOrdi) {
        this.tableauBleu = Arrays.copyOf(tableauBleu, size);
        this.tableauRouge = Arrays.copyOf(getTableauRouge, size);
        this.iaTurn = iaTurn;
        PionsPrisJoueur = pionsPrisJoueur;
        PionsPrisOrdi = pionsPrisOrdi;
    }

    public boolean coupValide(int i ,int numPlayer){
        //pour le moment on ne prend pas en compte le fait que J1 ne peux utiliser certaiens cases
            return  tableauBleu[i] != 0 && tableauRouge[i] != 0;

    }
    public boolean IsFinalPosition(){ // ne regarde que la fin par les points pour le moment
        int empty_J1 = 0;
        int empty_J2 = 0;
        for(int i = 0; i < sizePlayerCase; i ++){
            empty_J2 += tableauRouge[case_J2[i]] + tableauBleu[case_J2[i]];
            empty_J1 += tableauRouge[case_J1[i]] + tableauBleu[case_J1[i]];
        }
        return getPionsPrisOrdi() >= 33 || getPionsPrisJoueur() >= 33 || (getPionsPrisOrdi() == 32 && getPionsPrisJoueur() == 32) ||empty_J1 == 0 || empty_J2 == 0;
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

    public int getPionsPrisJoueur() {
        return PionsPrisJoueur;
    }

    public int getPionsPrisOrdi() {
        return PionsPrisOrdi;
    }
}
