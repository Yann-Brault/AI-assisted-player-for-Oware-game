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
        numPlayer = nb;
        numOponent = nb == 1 ? 2 : 1;
    }

    public static int evaluate2(Position pos, boolean iaTurn, int Profondeur) { // todo mega evalutaion
        if (pos.getPionsPrisOrdi() >= 33) {
            return Integer.MAX_VALUE;
        }
        if (pos.getPionsPrisJoueur() >= 33) {
            return Integer.MIN_VALUE;
        }
        if (pos.isFinalPosition() && pos.nbcoupValide(numOponent) == 0) {
            if (pos.nbGrainesTotal() < 8) {
                if (pos.getPionsPrisOrdi() - pos.getPionsPrisJoueur() < 0) {
                    return Integer.MIN_VALUE;
                } else {
                    return Integer.MAX_VALUE;
                }
            } else {
                return Integer.MAX_VALUE;
            }
        }
        if (pos.isFinalPosition() && pos.nbcoupValide(numPlayer) == 0) {
            return Integer.MIN_VALUE;
        } else if (pos.isFinalPosition()) {
            if (pos.getPionsPrisOrdi() >= 33) {
                return Integer.MAX_VALUE;
            }
            if (pos.getPionsPrisOrdi() - pos.getPionsPrisJoueur() > 0) {
                return Integer.MAX_VALUE;
            } else {
                return Integer.MIN_VALUE;
            }
        }
        int val = 0;
        if (pos.nbGrainesPlayerRouge(numPlayer) + pos.nbGrainesPlayerRouge(numOponent) <= 4) {
            val -= 100;
        }
        val += (pos.getPionsPrisOrdi()) - (pos.getPionsPrisJoueur() + pos.nbgrainePlayer(numOponent)) * 2 + pos.nbgrainePlayer(numPlayer) - pos.nbGrainesPlayerBleu(numOponent) + pos.nbGrainesPlayerBleu(numPlayer);
        if (pos.isIaTurn()) {
            if (numPlayer == 2) {
                return val - pos.nbCasePrenable() * 4 - pos.nbComboCasePrenable() * 4;
            }
            return val -  pos.nbCasePrenable() * 2 - pos.nbComboCasePrenable() * 4;
        } else {
            return val - pos.nbCasePrenable() * 2 + pos.nbComboCasePrenable() * 4;
        }

        /**   if(iaTurn){
         return (pos.getPionsPrisOrdi()) - (pos.getPionsPrisJoueur() + pos.nbgrainePlayer(numOponent));
         }
         else if (pos.getPionsPrisJoueur() > pos.getPionsPrisOrdi()){
         return (pos.getPionsPrisOrdi()) - (pos.getPionsPrisJoueur() + pos.nbgrainePlayer(numOponent))*2;
         }
         else{
         return (pos.getPionsPrisOrdi() + pos.nbgrainePlayer(numPlayer)) - (pos.getPionsPrisJoueur() + pos.nbgrainePlayer(numOponent));
         }*/
//        return pos.getPionsPrisOrdi() - pos.getPionsPrisJoueur();  pos.nbgrainePlayer(numPlayer) +
        // return (pos.getPionsPrisOrdi()) - (pos.nbgrainePlayer(numOponent) + pos.getPionsPrisJoueur() + pos.nbCasePrenablePlayer(numPlayer));
        //return pos.getPionsPrisOrdi()*2 - (pos.nbCasePrenable() + pos.getPionsPrisJoueur());
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

                        if (posCourante.coupValideBleu(i + 1, num)) {// +1 parce que i varie de 1 à 16
                            Position nextPosBleu = posCourante.getNextPosition(i, true);
                            int evalB = alphaBeta(nextPosBleu, false, alpha, beta, profondeur + 1, profondeurMax);
                            value = Math.max(value, evalB);
                        }

                    } else {

                        if (posCourante.coupValideRouge(i + 1, num)) {// +1 parce que i varie de 1 à 16
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
        } else {
            int value = Integer.MAX_VALUE;
            for (int couleur = 0; couleur < 2; couleur++) {
                for (int i = 0; i < Board.getSize(); i++) {
                    if (couleur == 0) {
                        if (posCourante.coupValideBleu(i + 1, numOponent)) {
                            Position nextPosBleu = posCourante.getNextPosition(i, true); // +1 parce que i varie de 1 à 16
                            int evalB = alphaBeta(nextPosBleu, true, alpha, beta, profondeur + 1, profondeurMax);
                            value = Math.min(value, evalB);
                        }

                    } else {

                        if (posCourante.coupValideRouge(i + 1, numOponent)) {// +1 parce que i varie de 1 à 16
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
}
