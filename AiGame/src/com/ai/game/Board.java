package com.ai.game;

import com.ai.ai.Ai;

import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Board {
    private final static int size = 16;
    private final static int sizePlayerCase = 8;
    private final static int initialGraine = 2;
    private final static int[] case_J1 = new int[]{1, 3, 5, 7, 9, 11, 13, 15}; // Player one got the odd holes
    private final static int[] case_J2 = new int[]{2, 4, 6, 8, 10, 12, 14, 16}; // PLayer two got the even holes
    private final int[] tableauBleu = new int[]{initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine};
    private final int[] tableauRouge = new int[]{initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine, initialGraine};
    private int PionsPrisJoueur; // Seeds taken by the player
    private int PionsPrisOrdi; // Seeds taken by the AI
    private final Ai ai;
    private boolean iaTurn;
    private final boolean iaJ1;

    public Board(boolean iaBegin) {
        this.iaTurn = iaBegin;
        this.iaJ1 = iaBegin;
        this.ai = new Ai(iaBegin ? 1 : 2);
    }

    public void play() throws InterruptedException {
        ExecutorService executor;
        Ai.nbTurn++;
        int seedTaken = 0;
        int holeToStartFrom = 0;
        boolean colorToPlay = false; // false means red

        //the AI player choose the hole he'll play
        if (iaTurn) {
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
            //children array contains all postions following the current one for the actual player
            Position[] children = currentPos.getNextPositions(Ai.numPlayer);

            //parallelism mechanism
            int processors = Runtime.getRuntime().availableProcessors();
            CountDownLatch latch = new CountDownLatch(currentPos.nbcoupValide(Ai.numPlayer));
            executor = Executors.newFixedThreadPool(processors);

            int[] valuesNodes = new int[size];
            //dynamic deep
            int p = 8;
            if (Ai.nbTurn <= 11) {
                p = 7;
            }
            if (Ai.nbTurn <= 1) {
                p = 6;
            }
            if (currentPos.nbcoupValide(Ai.numPlayer) < 10 && Ai.nbTurn > 11) {
                p = 10;
            }
            if (Ai.nbTurn >= 80 && this.getActualPosition().nbGrainePlayer(1) + this.getActualPosition().nbGrainePlayer(2) < 40) {
                p = 11;
            }
            Ai.nbNode.set(0); // number of nodes explored
            long time = System.currentTimeMillis();
            for (int i = 0; i < sizePlayerCase; i++) {
                //if the position is blue legal we explore it with minmax + alpha beta pruning and the value goes in valuesNode
                if (currentPos.coupValideBleu(cases[i], Ai.numPlayer)) {
                    final int i2 = i; // needed because the executor only take copies
                    final int p2 = p;
                    final Position currentchildrenB = children[i2];
                    executor.submit(() -> {
                        valuesNodes[i2] = Ai.alphaBeta(currentchildrenB, false, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, p2);
                        latch.countDown();
                    });
                    //else we assigne the minimal value possible
                } else {
                    valuesNodes[i] = Integer.MIN_VALUE;
                }
                //same thing with the red
                if (currentPos.coupValideRouge(cases[i], Ai.numPlayer)) {
                    final int i2 = i + sizePlayerCase;
                    final int p2 = p;
                    final Position currentchildrenR = children[i2];
                    executor.submit(() -> {
                        valuesNodes[i2] = Ai.alphaBeta(currentchildrenR, false, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, p2);
                        latch.countDown();
                    });
                } else {
                    valuesNodes[i + sizePlayerCase] = Integer.MIN_VALUE;
                }
            }
            latch.await(); //wait the end of calcul, 2500ms
            long t2 = System.currentTimeMillis();
            System.out.println("deep : " + (p + 1) + ", nodes : " + Ai.nbNode + ", cuts : " + Ai.nbCut + "\n");
            System.out.println("time : " + (t2 - time) + "ms\n");
            executor.shutdown();

            Ai.nbCut = 0;

            System.out.println("VALUES NODES = " + Arrays.toString(valuesNodes) + " first half is blue action and second red over his cases " + Arrays.toString(cases));
            /*
            selecting the best to play
            first half of values nodes are the blue move and second half are the red moves
            so if idxmax is lower than sizePlayerCase it's a blue mvoe else it's a red move
            */
            long max = (long) Integer.MIN_VALUE - 2;
            int idxmax = -1;
            for (int i = 0; i < size; i++) {
                if (i < sizePlayerCase) {
                    if (valuesNodes[i] > max && (currentPos.coupValideBleu(cases[i % sizePlayerCase], Ai.numPlayer))) {
                        max = valuesNodes[i];
                        idxmax = i;
                    }
                } else {
                    if (valuesNodes[i] > max && (currentPos.coupValideRouge(cases[i % sizePlayerCase], Ai.numPlayer))) {
                        max = valuesNodes[i];
                        idxmax = i;
                    }
                }
            }
            if (Ai.numPlayer == 1) {
                if (idxmax >= sizePlayerCase) {
                    holeToStartFrom = case_J1[idxmax - sizePlayerCase] - 1;
                }
                if (idxmax < sizePlayerCase) {
                    holeToStartFrom = case_J1[idxmax] - 1;
                    colorToPlay = true;
                }

            } else if (Ai.numPlayer == 2) {
                if (idxmax >= sizePlayerCase) {
                    holeToStartFrom = case_J2[idxmax - sizePlayerCase] - 1;
                }
                if (idxmax < sizePlayerCase) {
                    holeToStartFrom = case_J2[idxmax] - 1;
                    colorToPlay = true;
                }
            }
            String s = colorToPlay ? "blue" : "red";
            System.out.println("AI plays " + (holeToStartFrom + 1) + " " + s);

        } else { // Here the human player select the hole he wants to play
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
            int i;
            while (!validInput) {
                System.out.println("player choose a hole ?");
                String str = sc.nextLine();
                String[] entry = str.split(" ");
                if (entry.length < 2 || entry.length > 3) { // loop until size of the input is incorrect
                    continue;
                }
                try {
                    i = Integer.parseInt(entry[0]);
                } catch (Exception e) { // loop until first param isn't a digit
                    continue;
                }
                if ((i >= 1 && i <= 16) && (entry[1].equalsIgnoreCase("b") || entry[1].equalsIgnoreCase("r"))) {
                    colorToPlay = entry[1].equalsIgnoreCase("b"); //if equals b or B return true
                    int numPlayer;
                    boolean included;
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
                    holeToStartFrom = i - 1; // inputs are on [1;16] but index are on [0;15]
                }
            }
        }
        //same mechanism as in getNextPosition but here we play on the actual board not on copies
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
        sb.append("turn : ").append(Ai.nbTurn).append("\n");
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
