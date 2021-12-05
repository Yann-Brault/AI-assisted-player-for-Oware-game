package com.ai.Game2;

import com.ai.ai.Ai;
import com.ai.game.Position;

import java.util.Arrays;
import java.util.Scanner;

import static java.lang.Math.max;

public class Board2 {
    private final static int size = 16;
    private final static int initialGraine = 2;
    private final int[] tableauBleu = new int[]{initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine};
    private final int[] tableauRouge = new int[]{initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine};
    private boolean iaTurn;
    private final boolean iaJ1;
    private int PionsPrisJoueur; //pions pris par le joueur
    private int PionsPrisOrdi; // pions pris par l'ordi
    private final static int[] case_J1 = new int[]{1, 3, 5, 7, 9, 11, 13, 15};
    private final static int[] case_J2 = new int[]{2, 4, 6, 8, 10, 12, 14, 16};
    private final static int sizePlayerCase = 8;
    private final Ai ai;

    public Board2(boolean iaBegin) {

        this.iaTurn = iaBegin;
        this.iaJ1 = iaBegin;
        this.ai = new Ai(iaBegin ? 1 : 2);
    }

    public static int getSize() {
        return size;
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

    public Position2 getActualPosition() {
        return new Position2(tableauBleu, tableauRouge, iaTurn, iaJ1, getPionsPrisJoueur(), getPionsPrisOrdi());
    }


    public void play() {
        int seedTaken = 0;
        int holeToStartFrom = 0;
        boolean colorToPlay = false; // false implique de joeur rouge
        int[] playerHoles;
        int[] opponentHoles;
        if (iaTurn) { // choix du trou de l'ia
            Position2 currentPos = this.getActualPosition();
            Position2[] children = currentPos.getNextPositions(ai.numPlayer);
            int[] valuesNodes = new int[size];
            int p =6;
            int[] cases = ai.numPlayer == 1 ? case_J1 : case_J2;
            long time = System.currentTimeMillis();
            for (int i = 0; i < sizePlayerCase; i++) {
                if (currentPos.coupValideBleu(cases[i], ai.numPlayer)) {
                    valuesNodes[i] = max(ai.valeurMinMax2(children[i], iaTurn, 0, p), ai.evaluate2(children[i], iaTurn, 0));
                } else {
                    valuesNodes[i] = -100;
                }
                if (currentPos.coupValideRouge(cases[i], ai.numPlayer)) {
                    valuesNodes[i + sizePlayerCase] = max(ai.valeurMinMax2(children[i], iaTurn, 0, p), ai.evaluate2(children[i], iaTurn, 0));

                } else {
                    valuesNodes[i + sizePlayerCase] = -100;
                }

            }
            System.out.println("l'ia a recherché avec une profondeur de " + (p+1) + " coups parmis " + ai.nbnode + " noeuds.");
            ai.nbnode = 0;
            System.out.println("en t = " + (System.currentTimeMillis() - time) + "ms");
            System.out.println("VALUES NODES = " + Arrays.toString(valuesNodes) + " first half is blue action and second red over his cases " + Arrays.toString(cases));
            int max = -100;
            int idxmax = -1;
            for (int i = 0; i < size; i++) {
                if (valuesNodes[i] > max) {
                    max = valuesNodes[i];
                    idxmax = i;
                }
            }
            if (ai.numPlayer == 1) {
                if (idxmax >= sizePlayerCase) {
                    holeToStartFrom = case_J1[idxmax - sizePlayerCase]-1;
                    colorToPlay = false;
                }
                if (idxmax < sizePlayerCase) {
                    holeToStartFrom = case_J1[idxmax] -1 ;
                    colorToPlay = true;
                }

            }
            else if (ai.numPlayer == 2) {
                if (idxmax >= sizePlayerCase) {
                    holeToStartFrom = case_J2[idxmax - sizePlayerCase]-1;
                    colorToPlay = false;
                }
                if (idxmax < sizePlayerCase) {
                    holeToStartFrom = case_J2[idxmax] -1 ;
                    colorToPlay = true;
                }

            }
            String s = colorToPlay ? "blue" : "red";
            System.out.println("l'ia joue "+ (holeToStartFrom +1)  + " " + s);
        } else {// choix du trou de l'opposant
            Scanner sc = new Scanner(System.in);
            boolean validInput = false;
            int i = -1;
            while (!validInput) {
                System.out.println("player choose a hole ?");
                String str = sc.nextLine();
                String[] entry = str.split(" ");
                if (entry.length == 0 || entry.length > 2) { // Si taille entrée non valide on boucle
                    continue;
                }
                try {
                    i = Integer.parseInt(entry[0]);
                } catch (Exception e) { // on boucle si premier param pas un chiffre
                    continue;
                }

                if ((i >= 1 && i <= 16) && (entry[1].equalsIgnoreCase("b") || entry[1].equalsIgnoreCase("r"))) {
                    colorToPlay = entry[1].equalsIgnoreCase("b"); // la couleur est un bool
                    int numPlayer;
                    boolean included = false;
                    if (!iaJ1) {
                        numPlayer = 1;
                        if (colorToPlay) {

                            included = this.getActualPosition().coupValideBleu(i, numPlayer);
                        } else {

                            included = this.getActualPosition().coupValideRouge(i, numPlayer);

                        }
                        if (!included) {
                            continue;
                        }
                    } else {
                        numPlayer = 2;
                        if (colorToPlay) {

                            included = this.getActualPosition().coupValideBleu(i, numPlayer);
                        } else {

                            included = this.getActualPosition().coupValideRouge(i, numPlayer);

                        }
                        if (!included) {
                            continue;
                        }
                    }
                    validInput = true;

                    holeToStartFrom = i - 1; // -1 pour respecter les index
                }
            }
            playerHoles = case_J2;
            opponentHoles = case_J1;
        }

        int index = holeToStartFrom + 1 >= size ? 0 : holeToStartFrom + 1;

        if (colorToPlay) {
            int nbGraine = tableauBleu[holeToStartFrom];
            tableauBleu[holeToStartFrom] = 0;
            while (nbGraine > 0) {
                if (index != holeToStartFrom) {
                    tableauBleu[index]++;
                    nbGraine--;
                }
                if (nbGraine > 0) {
                    if (iaJ1) {
                        if (iaTurn) {
                            index = index + 2 >= size ? 1 : index + 2;
                        } else {
                            index = index + 2 >= size ? 0 : index + 2;
                        }
                    } else {
                        if (iaTurn) {
                            index = index + 2 >= size ? 0 : index + 2;
                        } else {
                            index = index + 2 >= size ? 1 : index + 2;
                        }
                    }
                }
            }
        } else {
            int nbGraine = tableauRouge[holeToStartFrom];
            tableauRouge[holeToStartFrom] = 0;
            while (nbGraine > 0) {
                if (index != holeToStartFrom) {
                    tableauRouge[index]++;
                    nbGraine--;
                }
                if (nbGraine > 0) {
                    index = index + 1 >= size ? 0 : index + 1;

                }
            }
        }
        while ((tableauBleu[index] + tableauRouge[index]) == 2 || (tableauBleu[index] + tableauRouge[index]) == 3) {
            seedTaken += (tableauBleu[index] + tableauRouge[index]);
            tableauBleu[index] = 0;
            tableauRouge[index] = 0;
            index = index - 1 < 0 ? size - 1 : index - 1;
        }

        if (iaTurn) {
            addPionsPrisOrdi(seedTaken);
        } else {
            addPionsPrisJoueur(seedTaken);
        }
        iaTurn = !iaTurn;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" ".repeat(7));
        for (int i = 1; i < size / 2 + 1; i++) {
            sb.append(String.format("%d", i));
            sb.append(" ".repeat(12));
        }
        sb.append("\n");
        sb.append("| ");
        for (int i = 0; i < size / 2; i++) {
            sb.append(String.format("\033[34m b:%d", tableauBleu[i])).append(" ").append(String.format("\033[31m r:%d \033[0m", tableauRouge[i])).append(" | ");
        }

        sb.append("\n");
        sb.append("-".repeat(size * 6 + 9));
        sb.append("\n");
        sb.append("| ");
        for (int i = size - 1; i >= size / 2; i--) {
            sb.append(String.format("\033[34m b:%d", tableauBleu[i])).append(" ").append(String.format("\033[31m r:%d \033[0m", tableauRouge[i])).append(" | ");
        }
        sb.append("\n");
        sb.append("      16           15           14            13           12          11           10           9");
        sb.append("\n");
        return sb.toString();
    }


}
