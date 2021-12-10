package com.ai.game;

public class Position {
    private final static int size = 16;
    private final int[] tableauBleu = new int[size];
    private final int[] tableauRouge = new int[size];
    private boolean iaTurn;
    private final boolean iaJ1;
    private int PionsPrisJoueur; //pions pris par le joueur
    private int PionsPrisOrdi; // pions pris par l'ordi
    private final static int[] case_J1 = new int[]{1, 3, 5, 7, 9, 11, 13, 15};
    private final static int[] case_J2 = new int[]{2, 4, 6, 8, 10, 12, 14, 16};
    private final static int sizePlayerCase = 8;

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

    public boolean isWinning() {
        if (this.isFinalPosition()) {
            int numops;
            if (iaJ1) {
                numops = 2;
            } else {
                numops = 1;
            }
            if ((this.nbcoupValide(numops) == 0) && PionsPrisOrdi > PionsPrisJoueur) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isLoosing() {
        if (this.isFinalPosition()) {
            int num;
            if (iaJ1) {
                num = 1;
            } else {
                num = 2;
            }
            if ((this.nbcoupValide(num) == 0) && PionsPrisOrdi < PionsPrisJoueur) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isDraw() {
        return this.isFinalPosition() && getPionsPrisJoueur() == 33 && getPionsPrisOrdi() == 33;
    }

    public boolean coupValide(int i, int numPlayer) {// pas sûr non plus à vérifier si le coup valide est bien défini
        if (numPlayer == 1) {
            for (int j = 0; i < sizePlayerCase; i++) {
                if (i == case_J1[j] && (tableauBleu[i] != 0 || tableauRouge[i] != 0)) {
                    return true;
                }
            }
            return false;
        }
        if (numPlayer == 2) {
            for (int j = 0; i < sizePlayerCase; i++) {
                if (i == case_J2[j] && (tableauBleu[i] != 0 || tableauRouge[i] != 0)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

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


    public int nbcoupValide(int numPlayer) {
        int acc = 0;
        int[] casejoueur = numPlayer == 1 ? case_J1 : case_J2;
        for (int i = 0; i < sizePlayerCase; i++) {
            if (coupValideRouge(casejoueur[i], numPlayer)) {
                acc++;
            }
            if (coupValideBleu(casejoueur[i], numPlayer)) {
                acc++;
            }
        }
        return acc;
    }


    public boolean isFinalPosition() { // ne regarde que la fin par les points pour le moment
        int empty_J1 = 0;
        int empty_J2 = 0;
        for (int i = 0; i < sizePlayerCase; i++) {
            empty_J2 += tableauRouge[case_J2[i] - 1] + tableauBleu[case_J2[i] - 1];
            empty_J1 += tableauRouge[case_J1[i] - 1] + tableauBleu[case_J1[i] - 1];
        }
        int nbGraineRestante = empty_J2 + empty_J1;
        return getPionsPrisOrdi() >= 33 || getPionsPrisJoueur() >= 33 || (getPionsPrisOrdi() == 32 && getPionsPrisJoueur() == 32) || nbGraineRestante < 8; //empty_J1 == 0 || empty_J2 == 0
    }

    public Position getNextPosition(int trou, boolean bleu) {// sera toujours une position valide l'input varie de 0 à 15 attentions les indices ont 1 de moins que ceux joué par l'input
        int pions = 0;
        int[] copyBoardRed = new int[]{tableauRouge[0], tableauRouge[1], tableauRouge[2], tableauRouge[3], tableauRouge[4], tableauRouge[5], tableauRouge[6], tableauRouge[7], tableauRouge[8], tableauRouge[9], tableauRouge[10], tableauRouge[11], tableauRouge[12], tableauRouge[13], tableauRouge[14], tableauRouge[15]};
        int[] copyBoardBlue = new int[]{tableauBleu[0], tableauBleu[1], tableauBleu[2], tableauBleu[3], tableauBleu[4], tableauBleu[5], tableauBleu[6], tableauBleu[7], tableauBleu[8], tableauBleu[9], tableauBleu[10], tableauBleu[11], tableauBleu[12], tableauBleu[13], tableauBleu[14], tableauBleu[15]};
        int index = trou + 1 >= size ? 0 : trou + 1; // nos index varie de 0 à 15


        if (bleu) {// elle joue du bleu
            int nb_graine = copyBoardBlue[trou];
            copyBoardBlue[trou] = 0;
            while (nb_graine > 0) {
                if (index != trou) {
                    copyBoardBlue[index]++;
                    nb_graine--;
                }
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

        } else { // elle joue du rouge
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

    public boolean isFamine(int num, int[] cases) {


        int empty = 0;
        for (int i = 0; i < sizePlayerCase; i++) {
            empty += tableauRouge[cases[i] - 1] + tableauBleu[cases[i] - 1];
        }
        return empty == 0;


    }
}
