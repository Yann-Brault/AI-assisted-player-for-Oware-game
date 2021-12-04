package com.ai.game;

import com.ai.ai.Ai;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import static java.lang.Math.max;

public class Board {
    private final static int size = 6;
    private final static int initialGraine = 4;
    private final int[] caseJoueur = new int[]{initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine};
    private final int[] caseOrdi = new int[]{initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine};
    private boolean iaTurn;
    private int PionsPrisJoueur; //pions pris par le joueur
    private int PionsPrisOrdi; // pions pris par l'ordi
    private final Ai ai;

    public Board(boolean iaBegin) {
        this.ai = new Ai(0);
        this.iaTurn = iaBegin;
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

    public void addPionsPrisJoueur(int pions) {
        PionsPrisJoueur += pions;
    }

    public void addPionsPrisOrdi(int pions) {
        PionsPrisOrdi += pions;
    }


    public Position getactualPosition() {
        int[] copycaseJoueur = Arrays.copyOf(caseJoueur, size);
        int[] copyCaseOrdi = Arrays.copyOf(caseOrdi, size);
        return new Position(copycaseJoueur, copyCaseOrdi, isIaTurn(), getPionsPrisJoueur(), getPionsPrisOrdi());
    }

    public void play() {
        int seedTaken = 0;
        int holeToStartFrom = 0;
        int[] playerHoles;
        int[] opponentHoles;
        if (iaTurn) {
//            Random r = new Random();
//            holeToStartFrom = r.nextInt(6);
//            System.out.println(holeToStartFrom);
            playerHoles = getCaseOrdi();
            opponentHoles = getCaseJoueur();
            Position currentPos = this.getactualPosition();
            Position[] children = currentPos.getsNextPositions();
            int[] valuesNodes = new int[size];
            int p = 9;
            if(currentPos.nbcoupValide() <= 2){
                p +=1;
            }
            long time = System.currentTimeMillis();
            for (int i = 0; i < size; i++) {
                if (currentPos.coupValide(i)) {
                    valuesNodes[i] = max(ai.valeurMinMax(children[i], iaTurn, 0, p),ai.evaluate(children[i],true,0));
                } else {
                    valuesNodes[i] = -100;
                }
            }
            System.out.println("l'ia a recherché avec une profondeur de "+p+" coups parmis "+ai.nbnode+" noeuds.");
            ai.nbnode = 0;
            System.out.println("en t = " + (System.currentTimeMillis() - time) +"ms");
            System.out.println("VALUES NODES = " + Arrays.toString(valuesNodes));
            int max = -100;
            int idxmax = -1;
            for (int i = 0; i < size; i++) {
                if (valuesNodes[i] > max) {
                    max = valuesNodes[i];
                    idxmax = i;
                }
            }
            holeToStartFrom = idxmax;
            System.out.println("L'ia a choisis le trou n° "+holeToStartFrom);

        } else {
            Scanner sc = new Scanner(System.in);  // Create a Scanner object
            System.out.println("which hole do you want to play ?");
            holeToStartFrom = sc.nextInt();
            playerHoles = getCaseJoueur();
            opponentHoles = getCaseOrdi();
        }

        int seedToPlay = playerHoles[holeToStartFrom];
        playerHoles[holeToStartFrom] = 0;
        int indexToPlant = holeToStartFrom + 1;
        boolean capturing = false;
        int tour = 0 ;
        //tant que l'on peut jouer
        while (seedToPlay > 0) {
            //on joue dans nos trous
            if(tour > 0){indexToPlant =0;}
            capturing = false;
            for (int i = indexToPlant; i < size; i++) {
                if (seedToPlay > 0 && i != holeToStartFrom) {
                    playerHoles[i]++;
                    seedToPlay--;
                }
            }
            /*
             * si après avoir joué dans nos trous il nous reste des graines alors on les joue
             * chez l'adversaire
             */

            if (seedToPlay > 0) {
                indexToPlant = 0;
                for (int i = 0; i < size; i++) {
                    if (seedToPlay > 0) {
                        opponentHoles[i]++;
                        seedToPlay--;
                        if (i > 0) {
                            indexToPlant++;
                        }
                    } else {
                        break;
                    }
                }
                capturing = true;
            }
            tour ++;
        }
        if (capturing) {
            if (1 < opponentHoles[indexToPlant] && opponentHoles[indexToPlant] <= 3) {
                while (indexToPlant >= 0 && 1 < opponentHoles[indexToPlant] && opponentHoles[indexToPlant] <= 3) {
                    seedTaken += opponentHoles[indexToPlant];
                    opponentHoles[indexToPlant] = 0;
                    indexToPlant--;
                }
            }
        }
        updateGameStatus(iaTurn, playerHoles, opponentHoles, seedTaken);
        setIaTurn(!iaTurn);
    }

    private void updateGameStatus(boolean iaTurn, int[] playerHoles, int[] opponentHoles, int seedTaken) {
        if (iaTurn) {
            addPionsPrisOrdi(seedTaken);
        } else {
            addPionsPrisJoueur(seedTaken);
        }
        updateHoles(iaTurn, playerHoles, opponentHoles);
    }

    private void updateHoles(boolean iaTurn, int[] playerHoles, int[] opponentHoles) {
        if (iaTurn) {
            setArray(getCaseOrdi(), playerHoles);
            setArray(getCaseJoueur(), opponentHoles);
        } else {
            setArray(getCaseJoueur(), playerHoles);
            setArray(getCaseOrdi(), opponentHoles);
        }
    }

    private void setArray(int[] oldArray, int[] newArray) {
        System.arraycopy(newArray, 0, oldArray, 0, oldArray.length);
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
