package com.ai.ai;

import com.ai.game.Board;
import com.ai.game.Position;

import java.util.concurrent.atomic.AtomicInteger;

public class Ai {
    private int score;
    public static AtomicInteger nbnode = new AtomicInteger(0);
    public static int nbcut = 0;
    public static int nbturn = 0;
    public static int numPlayer = 0;
    public static int numOponent = 0;

    public Ai(int nb) {
        this.numPlayer = nb;
        numOponent = nb == 1 ? 2 : 1;
    }



    public static int evaluate2(Position pos, boolean iaTurn, int Profondeur) { // todo mega evalutaion
        if (pos.isFinalPosition() && pos.nbcoupValide(numPlayer) == 0) {
            return Integer.MIN_VALUE;
        } else if (pos.isFinalPosition() && pos.nbcoupValide(numPlayer == 1 ? 2 : 1) == 0) {
            return Integer.MAX_VALUE;
        }
        else if (pos.isFinalPosition() && iaTurn) {
            if (pos.getPionsPrisOrdi() - pos.getPionsPrisJoueur() > 0) {
                return 999;
            } else {
                return -999;
            }
        }
//        return pos.getPionsPrisOrdi() - pos.getPionsPrisJoueur();
        return (pos.nbgrainePlayer(numPlayer) + pos.getPionsPrisOrdi() )-(pos.nbgrainePlayer(numOponent) + pos.getPionsPrisJoueur());
    }


    public static int evaluateEmptyHole(Position pos) {
        int nbHolesEmpty = 0;
        if (numPlayer == 1) {
            for (int i = 0; i < pos.getTableauBleu().length; i++) {
                if (i % 2 == 0 && pos.getTableauBleu()[i] == 0 && pos.getTableauRouge()[i] == 0) {
                    nbHolesEmpty++;
                }
            }
            return nbHolesEmpty;
        } else {
            for (int i = 0; i < pos.getTableauBleu().length; i++) {
                if (i % 2 == 1 && pos.getTableauBleu()[i] == 0 && pos.getTableauRouge()[i] == 0) {
                    nbHolesEmpty++;
                }
            }
            return -nbHolesEmpty;
        }
    }
    public static int valeurMinMax2(Position posCourante, boolean iaTurn, int profondeur, int profondeurMax) {
//        nbnode++;
        int[] tabValeursRouge = new int[Board.getSize()];
        int[] tabValeurBleu = new int[Board.getSize()];
        Position nextPosBleu;
        Position nextPosRouge;
        int num = iaTurn ? numPlayer : (numPlayer == 1 ? 2 : 1);
        if (posCourante.isFinalPosition()) {
//            System.out.println("fin de jeu ");
            int valuation = 0; // TODO evaluate
            if (posCourante.getPionsPrisOrdi() > posCourante.getPionsPrisJoueur()) { //TODO GERER LES PIONS APRès FAMINE
                return 64;
            } else {
                return -64;
            }

        }
        if (profondeur == profondeurMax) {
            return evaluate2(posCourante, iaTurn, profondeur);
            //return  evaluateEmptyHole(posCourante);
            // dans un premier temps l'évaluation sera la
            // différence du nb de pions pris
        }

        for (int i = 0; i < Board.getSize(); i++) { // changement de coup valide pour les joueurs fait ?

            if (posCourante.coupValideBleu(i, num)) {
                nextPosBleu = posCourante.getNextPosition(i, true); // pos_next devient la position courante, et on change le joueur
                tabValeurBleu[i] = valeurMinMax2(nextPosBleu, nextPosBleu.isIaTurn(), profondeur + 1, profondeurMax);
            } else {
                if (iaTurn) {
                    tabValeurBleu[i] = -100;
                } else {
                    tabValeurBleu[i] = 100;
                }

            }
            if (posCourante.coupValideRouge(i, num)) {
                nextPosRouge = posCourante.getNextPosition(i, false);
                tabValeursRouge[i] = valeurMinMax2(nextPosRouge, nextPosRouge.isIaTurn(), profondeur + 1, profondeurMax);
            } else {
                if (iaTurn) {
                    tabValeursRouge[i] = -100;
                } else {
                    tabValeursRouge[i] = 100;
                }
            }

        }
        int maxRouge;
        int maxBleu;
        int res;
        if (iaTurn) {
            maxBleu = max(tabValeurBleu);
            maxRouge = max(tabValeursRouge);
            res = Math.max(maxBleu, maxRouge);
        } else {
            maxBleu = min(tabValeurBleu);
            maxRouge = min(tabValeursRouge);
            res = Math.min(maxBleu, maxRouge);
        }
        return res;
    }


    public static int alphaBeta(Position posCourante, boolean iaTurn, int alpha, int beta, int profondeur, int profondeurMax) {
        nbnode.addAndGet(1);

        int num = numPlayer;
        int numOponent = num == 1 ? 2 : 1;

        if (posCourante.isFinalPosition() || profondeur == profondeurMax) {

            return evaluate2(posCourante, iaTurn, profondeur);
        }
        if (iaTurn) {
            int value = Integer.MIN_VALUE;
            for (int couleur = 0; couleur < 2; couleur++) {
                for (int i = 0; i < Board.getSize(); i++) {
                    if (couleur == 0) {

                        if (posCourante.coupValideBleu(i+1, num)) {// +1 parce que i varie de 1 à 16
                            Position nextPosBleu = posCourante.getNextPosition(i, true);
                            int evalB = alphaBeta(nextPosBleu, false, alpha, beta, profondeur + 1, profondeurMax);
                            value = Math.max(value, evalB);
                        }

                    } else {

                        if (posCourante.coupValideRouge(i+1, num)) {// +1 parce que i varie de 1 à 16
                            Position nextPosRouge = posCourante.getNextPosition(i, false);
                            int evalR = alphaBeta(nextPosRouge, false, alpha, beta, profondeur + 1, profondeurMax);
                            value = Math.max(value, evalR);
                        }
                    }
                    if (beta <= alpha) {
                        nbcut++;
                        return value;
                    }

                    alpha = Math.max(alpha, value);



                }
            }

            return value;
        }
        else {
            int value = Integer.MAX_VALUE;
            for (int couleur = 0; couleur < 2; couleur++) {
                for (int i = 0; i < Board.getSize(); i++) {
                    if (couleur == 0) {
                        if (posCourante.coupValideBleu(i+1, numOponent)) {
                            Position nextPosBleu = posCourante.getNextPosition(i, true); // +1 parce que i varie de 1 à 16
                            int evalB = alphaBeta(nextPosBleu, true, alpha, beta, profondeur + 1, profondeurMax);
                            value = Math.min(value, evalB);
                        }

                    } else {

                        if (posCourante.coupValideRouge(i+1, numOponent)) {// +1 parce que i varie de 1 à 16
                            Position nextPosRouge = posCourante.getNextPosition(i, false);
                            int evalR = alphaBeta(nextPosRouge, true, alpha, beta, profondeur + 1, profondeurMax);
                            value = Math.min(value, evalR);
                        }
                    }

                    if (beta <= alpha) {
                        nbcut++;
                        return value;
                    }
                    beta = Math.min(beta, value);

                }
            }
            return value;
        }


    }


    public static int max(int[] array) {
        int max = 0;
        for (int i = 0; i < Board.getSize(); i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    public static int min(int[] array) {
        int min = 0;

        for (int i = 0; i < Board.getSize(); i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }


}
