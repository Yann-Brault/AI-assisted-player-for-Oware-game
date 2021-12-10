package com.ai.game;

import com.ai.ai.Ai;

import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Board {
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

    public Board(boolean iaBegin) {

        this.iaTurn = iaBegin;
        this.iaJ1 = iaBegin;
        this.ai = new Ai(iaBegin ? 1 : 2);
    }

    public void play() throws InterruptedException {
        ExecutorService executor = null;
        Ai.nbturn++;
        int seedTaken = 0;
        int holeToStartFrom = 0;
        boolean colorToPlay = false; // false implique de jouer rouge

        if (iaTurn) { // choix du trou de l'ia
            Position currentPos = this.getActualPosition();
            int[] cases = Ai.numPlayer == 1 ? case_J1 : case_J2;
            if (currentPos.isFamine(Ai.numPlayer, cases)) {
                int result = 0;
                for (int i = 0; i < size; i++) {
                    result += tableauBleu[i] + tableauRouge[i];
                    tableauBleu[i] = 0;
                    tableauRouge[i] = 0;

                }
                addPionsPrisJoueur(result);


                return;
            }
            Position[] children = currentPos.getNextPositions(Ai.numPlayer);
            CountDownLatch latch = new CountDownLatch(currentPos.nbcoupValide(Ai.numPlayer));
            executor = Executors.newFixedThreadPool(currentPos.nbcoupValide(Ai.numPlayer));
            int[] valuesNodes = new int[size];
            int p = 8;
            if (currentPos.nbcoupValide(Ai.numPlayer) <= 10) {
                p = 10;
            }

            long time = System.currentTimeMillis();
            for (int i = 0; i < sizePlayerCase; i++) {
                if (currentPos.coupValideBleu(cases[i], Ai.numPlayer)) {
                    final int i2 = i;
                    final int p2 = p;
                    final Position currentchildrenB = children[i2];

                    executor.submit(new Runnable() {
                        @Override
                        public void run() {

                            valuesNodes[i2] = Ai.alphaBeta2(currentchildrenB, false, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, p2);
                            latch.countDown();
                        }

                    });

                } else {
                    valuesNodes[i] = Integer.MIN_VALUE;
                }
                if (currentPos.coupValideRouge(cases[i], Ai.numPlayer)) {
                    final int i2 = i + sizePlayerCase;
                    final int p2 = p;

                    final Position currentchildrenR = children[i2];

                    executor.submit(new Runnable() {
                        @Override
                        public void run() {
                            valuesNodes[i2] = Ai.alphaBeta2(currentchildrenR, false, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, p2);
                            latch.countDown();
                        }

                    });


                } else {
                    valuesNodes[i + sizePlayerCase] = Integer.MIN_VALUE;
                }


            }
            latch.await(); // attend la find es calculs 2000,TimeUnit.MILLISECONDS


            System.out.println("l'ia a recherché avec une profondeur de " + (p + 1) + " coups parmis " + Ai.nbnode + " noeuds." + " avec " + Ai.nbcut);
            System.out.println("en t = " + (System.currentTimeMillis() - time) + "ms");
            executor.shutdown();
            Ai.nbnode = 0;
            Ai.nbcut = 0;

            System.out.println("VALUES NODES = " + Arrays.toString(valuesNodes) + " first half is blue action and second red over his cases " + Arrays.toString(cases));
            int max = Integer.MIN_VALUE;
            int idxmax = -1;
            for (int i = 0; i < size; i++) {
                if (valuesNodes[i] >= max) {
                    max = valuesNodes[i];
                    idxmax = i;
                }
            }
            if (Ai.numPlayer == 1) {
                if (idxmax >= sizePlayerCase) {
                    holeToStartFrom = case_J1[idxmax - sizePlayerCase] - 1;
                    colorToPlay = false;
                }
                if (idxmax < sizePlayerCase) {
                    holeToStartFrom = case_J1[idxmax] - 1;
                    colorToPlay = true;
                }

            } else if (ai.numPlayer == 2) {
                if (idxmax >= sizePlayerCase) {
                    holeToStartFrom = case_J2[idxmax - sizePlayerCase] - 1;
                    colorToPlay = false;
                }
                if (idxmax < sizePlayerCase) {
                    holeToStartFrom = case_J2[idxmax] - 1;
                    colorToPlay = true;
                }

            }
            String s = colorToPlay ? "blue" : "red";
            System.out.println("l'ia joue " + (holeToStartFrom + 1) + " " + s);
        } else {// choix du trou de l'opposant
            Position currentPos = this.getActualPosition();
            int num = iaJ1 ? 2 : 1;
            int[] cases = num == 1 ? case_J1 : case_J2;
            if (currentPos.isFamine(num, cases)) {
                int result = 0;
                for (int i = 0; i < size; i++) {
                    result += tableauBleu[i] + tableauRouge[i];
                    tableauBleu[i] = 0;
                    tableauRouge[i] = 0;

                }
                addPionsPrisOrdi(result);


                return;

            }


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

    public Position getActualPosition() {
        return new Position(tableauBleu, tableauRouge, iaTurn, iaJ1, getPionsPrisJoueur(), getPionsPrisOrdi());
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
