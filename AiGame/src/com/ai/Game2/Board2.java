package com.ai.Game2;

import com.ai.ai.Ai;

import java.util.Arrays;
import java.util.Scanner;

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

    public Position2 getActualPosition() {
        return new Position2(tableauBleu, tableauRouge, iaTurn, iaJ1, getPionsPrisJoueur(), getPionsPrisOrdi());
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

    public void play() {
        int seedTaken = 0;
        int holeToStartFrom = 0;
        boolean colorToPlay = false; // false implique de joeur rouge
        int[] playerHoles;
        int[] opponentHoles;
        if (iaTurn) {
            Scanner sc = new Scanner(System.in);
            boolean validInput = false;
            int i = -1;
            while (!validInput) {
                System.out.println("ia choose a hole ?");
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
                    if (iaJ1) {
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
            playerHoles = case_J1;
            opponentHoles = case_J2;
        } else {
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
}
