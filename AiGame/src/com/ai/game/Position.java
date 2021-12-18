package com.ai.game;

import java.util.Arrays;

public class Position {
    private final static int size = 16;
    private final static int sizePlayerCase = 8;
    private final static int[] case_J1 = new int[]{1, 3, 5, 7, 9, 11, 13, 15}; // Player one got the odd holes
    private final static int[] case_J2 = new int[]{2, 4, 6, 8, 10, 12, 14, 16}; // PLayer two got the even holes
    private final int[] tableauBleu = new int[size]; // Blue seeds in each holes
    private final int[] tableauRouge = new int[size]; // Red seeds in each holes
    private int PionsPrisJoueur; // Seeds taken by the player
    private int PionsPrisOrdi; // Seeds taken by the AI
    private boolean iaTurn;
    private final boolean iaJ1;

    /**
     * A position represents the state of the game after each move
     * @param tableauBleu
     * @param tableauRouge
     * @param iaTurn
     * @param iaJ1
     * @param pionsPrisJoueur
     * @param pionsPrisOrdi
     */
    public Position(int[] tableauBleu, int[] tableauRouge, boolean iaTurn, boolean iaJ1, int pionsPrisJoueur, int pionsPrisOrdi) {
        for (int i = 0; i < size; i++) {
            this.tableauBleu[i] = tableauBleu[i];
            this.tableauRouge[i] = tableauRouge[i];
        }
        this.iaTurn = iaTurn;
        this.iaJ1 = iaJ1;
        PionsPrisJoueur = pionsPrisJoueur;
        PionsPrisOrdi = pionsPrisOrdi;
    }

    /**
     * @param numPlayer
     * @return the global number of seeds left in a player's holes
     */
    public int nbGrainePlayer(int numPlayer) {
        int[] cases = numPlayer == 1 ? case_J1 : case_J2;
        int num = 0;
        for (int i = 0; i < sizePlayerCase; i++) {
            num += tableauRouge[cases[i] - 1] + tableauBleu[cases[i] - 1];
        }
        return num;
    }

    /**
     * @param numPlayer
     * @return the number of red seeds left in a player's holes
     */
    public int nbGrainesPlayerRouge(int numPlayer) {
        int[] cases = numPlayer == 1 ? case_J1 : case_J2;
        int num = 0;
        for (int i = 0; i < sizePlayerCase; i++) {
            num += tableauRouge[cases[i] - 1];
        }
        return num;
    }

    /**
     * @param numPlayer
     * @return the number of red seeds left in a player's holes
     */
    public int nbGrainesPlayerBleu(int numPlayer) {
        int[] cases = numPlayer == 1 ? case_J1 : case_J2;
        int num = 0;
        for (int i = 0; i < sizePlayerCase; i++) {
            num += tableauBleu[cases[i] - 1];
        }
        return num;
    }

    /**
     * @return the number of holes that can be captured
     */
    public int nbCasePrenable() {
        int num = 0;
        for (int i = 0; i < size; i++) {
            if (tableauBleu[i] + tableauRouge[i] == 2 || tableauBleu[i] + tableauRouge[i] == 1) {
                num++;
            }
        }
        return num;
    }

    /**
     * @return the maximum number of holes that can be captured in one move
     */
    public int nbComboCasePrenable() {
        int maxPrenable = 0;
        int num = 0;
        for (int i = 0; i < size; i++) {
            if (tableauBleu[i] + tableauRouge[i] == 2 || tableauBleu[i] + tableauRouge[i] == 1) {
                num++;
            } else {
                if (num > maxPrenable) {
                    maxPrenable = num;
                }
                num = 0;
            }
        }
        return maxPrenable;
    }

    /**
     * @return the total number of seeds one the board
     */
    public int nbGrainesTotal() {
        return Arrays.stream(tableauBleu).sum() + Arrays.stream(tableauRouge).sum();
    }

    /**
     * @param numPlayer
     * @return an array of all the positions that follow the actual one for a given player
     */
    public Position[] getNextPositions(int numPlayer) {
        Position[] array = new Position[size];
        if (numPlayer == 1) {
            for (int i = 0; i < sizePlayerCase; i++) {
                array[i] = this.getNextPosition(case_J1[i] - 1, true);
                array[i + sizePlayerCase] = this.getNextPosition(case_J1[i] - 1, false);
            }
        } else {
            for (int i = 0; i < sizePlayerCase; i++) {
                array[i] = this.getNextPosition(case_J2[i] - 1, true);
                array[i + sizePlayerCase] = this.getNextPosition(case_J2[i] - 1, false);
            }

        }
        return array;
    }

    /**
     * @param i
     * @param numPlayer
     * @return a bool if a blue move can be done or not
     */
    public boolean coupValideBleu(int i, int numPlayer) {
        if (numPlayer == 1) {
            for (int j = 0; j < sizePlayerCase; j++) {
                if (i == case_J1[j] && tableauBleu[i - 1] != 0) { // -1 parce que i varie de 1 à 16
                    return true;
                }
            }
            return false;
        }
        if (numPlayer == 2) {
            for (int j = 0; j < sizePlayerCase; j++) {
                if (i == case_J2[j] && tableauBleu[i - 1] != 0) {// -1 parce que i varie de 1 à 16
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    /**
     * @param i
     * @param numPlayer
     * @return a bool if a red move can be done or not
     */
    public boolean coupValideRouge(int i, int numPlayer) {
        if (numPlayer == 1) {
            for (int j = 0; j < sizePlayerCase; j++) {
                if (i == case_J1[j] && tableauRouge[i - 1] != 0) { // -1 parce que i varie de 1 à 16
                    return true;
                }
            }
            return false;
        }
        if (numPlayer == 2) {
            for (int j = 0; j < sizePlayerCase; j++) {
                if (i == case_J2[j] && tableauRouge[i - 1] != 0) { // -1 parce que i varie de 1 à 16
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    /**
     * @param numPlayer
     * @return the amount of legal moves that a player can do
     */
    public int nbcoupValide(int numPlayer) {
        int acc = 0;
        int[] caseJoueur = numPlayer == 1 ? case_J1 : case_J2;
        for (int i = 0; i < sizePlayerCase; i++) {
            if (coupValideRouge(caseJoueur[i], numPlayer)) {
                acc++;
            }
            if (coupValideBleu(caseJoueur[i], numPlayer)) {
                acc++;
            }
        }
        return acc;
    }

    /**
     * @param num
     * @param cases
     * @return true if a player is starving
     */
    public boolean isFamine(int num, int[] cases) {
        int empty = 0;
        for (int i = 0; i < sizePlayerCase; i++) {
            empty += tableauRouge[cases[i] - 1] + tableauBleu[cases[i] - 1];
        }
        return empty == 0;
    }

    /**
     * @return true if the actual is a final state of the game.
     */
    public boolean isFinalPosition() {
        int empty_J1 = 0;
        int empty_J2 = 0;
        for (int i = 0; i < sizePlayerCase; i++) {
            empty_J2 += tableauRouge[case_J2[i] - 1] + tableauBleu[case_J2[i] - 1];
            empty_J1 += tableauRouge[case_J1[i] - 1] + tableauBleu[case_J1[i] - 1];
        }
        int nbGraineRestante = empty_J2 + empty_J1;
        return getPionsPrisOrdi() >= 33 || getPionsPrisJoueur() >= 33 || (getPionsPrisOrdi() == 32 && getPionsPrisJoueur() == 32) || nbGraineRestante < 8;
    }

    /**
     * the inputs are on a 1 to 16 range
     * but the internal mechanism works on a 0 to 15 range
     * @param trou
     * @param bleu
     * @return a simulation of the position from a given hole and a given seed color, the returned position is always a legal one
     */
    public Position getNextPosition(int trou, boolean bleu) {
        int pions = 0;
        int[] copyBoardRed = new int[]{tableauRouge[0], tableauRouge[1], tableauRouge[2], tableauRouge[3], tableauRouge[4], tableauRouge[5], tableauRouge[6], tableauRouge[7], tableauRouge[8], tableauRouge[9], tableauRouge[10], tableauRouge[11], tableauRouge[12], tableauRouge[13], tableauRouge[14], tableauRouge[15]};
        int[] copyBoardBlue = new int[]{tableauBleu[0], tableauBleu[1], tableauBleu[2], tableauBleu[3], tableauBleu[4], tableauBleu[5], tableauBleu[6], tableauBleu[7], tableauBleu[8], tableauBleu[9], tableauBleu[10], tableauBleu[11], tableauBleu[12], tableauBleu[13], tableauBleu[14], tableauBleu[15]};
        int index = trou + 1 >= size ? 0 : trou + 1;

        //if the move is a blue one
        if (bleu) {
            int nb_graine = copyBoardBlue[trou];
            copyBoardBlue[trou] = 0;
            //sowing
            while (nb_graine > 0) {
                if (index != trou) {
                    copyBoardBlue[index]++;
                    nb_graine--;
                }
                // in case the number of seeds in the holes is great enough to sow on both sides of the board
                if (nb_graine > 0) {
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

        } else { // same thing here for a red move
            int nb_graine = copyBoardRed[trou];
            copyBoardRed[trou] = 0;
            while (nb_graine > 0) {
                if (index != trou) {
                    copyBoardRed[index]++;
                    nb_graine--;
                }
                if (nb_graine > 0) {
                    index = index + 1 >= size ? 0 : index + 1;

                }
            }
        }
        // here is the capture
        while ((copyBoardBlue[index] + copyBoardRed[index]) == 2 || (copyBoardBlue[index] + copyBoardRed[index]) == 3) {
            pions += (copyBoardBlue[index] + copyBoardRed[index]);
            copyBoardBlue[index] = 0;
            copyBoardRed[index] = 0;
            index = index - 1 < 0 ? size - 1 : index - 1;
        }
        if (iaTurn) {
            return new Position(copyBoardBlue, copyBoardRed, false, iaJ1, getPionsPrisJoueur(), pions);
        } else {
            return new Position(copyBoardBlue, copyBoardRed, true, iaJ1, pions, getPionsPrisOrdi());
        }
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
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
        return sb.toString();
    }
}
