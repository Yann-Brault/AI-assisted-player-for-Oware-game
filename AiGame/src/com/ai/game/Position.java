package com.ai.game;

import java.util.Arrays;

public class Position {
    private final static int size = 6;
    private final int[] caseJoueur;
    private final int[] caseOrdi;
    private boolean iaTurn;
    private int PionsPrisJoueur; //pions pris par le joueur
    private int PionsPrisOrdi; // pions pris par l'ordi

    public Position(int[] caseJoueur, int[] caseOrdi, boolean iaTurn, int pionsPrisJoueur, int pionsPrisOrdi) {
        this.caseJoueur = caseJoueur;
        this.caseOrdi = caseOrdi;
        this.iaTurn = iaTurn;
        PionsPrisJoueur = pionsPrisJoueur;
        PionsPrisOrdi = pionsPrisOrdi;
    }


    public Position getNextPos(int trou) {
        int pions = 0;
        int[] copyplaying;
        int[] copyplayed;
        if (iaTurn) {
            copyplaying = Arrays.copyOf(caseOrdi, size);
            copyplayed = Arrays.copyOf(caseJoueur, size);

        } else {
            copyplaying = Arrays.copyOf(caseJoueur, size);
            copyplayed = Arrays.copyOf(caseOrdi, size);
        }

        int nb_graine = copyplaying[trou];
        copyplaying[trou] = 0;
        int index = trou + 1;
        boolean ops = false;

        while (nb_graine > 0) {
            ops = false;// nous dis si l'index se trouve chez l'opposant ou chez nous.
            for (int i = index; i < size; i++) {
                if (nb_graine > 0 && i != trou) {
                    copyplaying[i]++;
                    nb_graine--;
                }
            }

            if (nb_graine > 0) {
                index = 0;

                System.out.println(nb_graine);
                for (int i = 0; i < size; i++) {

                    if (nb_graine > 0) {
                        copyplayed[i]++;
                        nb_graine--;
                        if (i > 0) {
                            index++;// nous ne devons aps incrementer l'index pour coller aux index de tableaux
                        }

                    } else {
                        break;
                    }
                }
                ops = true; // nous dis si l'index se trouve chez l'opposant ou chez nous.
            }

        }
        if (ops) {
            if (copyplayed[index] > 1 && copyplayed[index] <= 3) {

                while (index >= 0 && copyplayed[index] > 1 && copyplayed[index] <= 3) {
                    pions += copyplayed[index];
                    copyplayed[index] = 0;
                    index--;
                }
            }
        }
        if (iaTurn) {
            return new Position(copyplayed, copyplaying, false, getPionsPrisJoueur(), pions);
        } else {
            return new Position(copyplaying, copyplayed, true, pions, getPionsPrisOrdi());
        }


        //  return new Position(caseJoueur,caseOrdi,iaTurn,getPionsPrisJoueur(),getPionsPrisOrdi());


    }


    public int[] getCaseJoueur() {
        return caseJoueur;
    }

    public int[] getCaseOrdi() {
        return caseOrdi;
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

    public void setIaTurn(boolean iaTurn) {
        this.iaTurn = iaTurn;
    }

    public void setPionsPrisJoueur(int pionsPrisJoueur) {
        PionsPrisJoueur = pionsPrisJoueur;
    }

    public void addPionsPrisJoueur(int pions) {
        PionsPrisJoueur += pions;
    }

    public void addPionsPrisOrdi(int pions) {
        PionsPrisOrdi += pions;
    }

    public void setPionsPrisOrdi(int pionsPrisOrdi) {
        PionsPrisOrdi = pionsPrisOrdi;
    }

    public int getCaseJoueurAt(int i) {
        return caseJoueur[i];
    }

    public int getCaseOrdiAt(int i) {
        return caseOrdi[i];
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
