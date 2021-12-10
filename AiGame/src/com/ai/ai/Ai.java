package com.ai.ai;

import com.ai.Game2.Board2;
import com.ai.Game2.Position2;
import com.ai.game.Board;
import com.ai.game.Position;

public class Ai {
    private int score;
    public static int nbnode = 0;
    public static int nbturn = 0;
    public static int numPlayer = 0;
    private final static int[] case_J1 = new int[]{1, 3, 5, 7, 9, 11, 13, 15};
    private final static int[] case_J2 = new int[]{2, 4, 6, 8, 10, 12, 14, 16};
    private final static int sizePlayerCase = 8;

    public Ai(int nb) {
        this.numPlayer = nb;
    }

    public int evaluate(Position posCourante, boolean iaTurn, int Profondeur) {
//        System.out.println("hello " +(posCourante.getPionsPrisOrdi() - posCourante.getPionsPrisJoueur()) );
        return posCourante.getPionsPrisOrdi() - posCourante.getPionsPrisJoueur();
        // if(iaTurn){
        //   return posCourante.getPionsPrisOrdi();
        //}
        //else{
        //  return posCourante.getPionsPrisJoueur();
        //}
    }

    public static int evaluate2(Position2 pos, boolean iaTurn, int Profondeur) {
        if(pos.isFinalPosition() && pos.nbcoupValide(numPlayer) == 0){
            return -1000;
        }
        else if(pos.isFinalPosition() && pos.nbcoupValide(numPlayer == 1 ? 2 : 1 ) == 0){
            return 1000;}
        else if(pos.isFinalPosition() && iaTurn){
            if(pos.getPionsPrisOrdi() - pos.getPionsPrisJoueur() > 0){
                return 999;
            }
            else{
                return -999;
            }
        }
        return pos.getPionsPrisOrdi() - pos.getPionsPrisJoueur();
    }

    public static int evaluateEmptyHole(Position2 pos) {
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


    public static int valeurMinMax2(Position2 posCourante, boolean iaTurn, int profondeur, int profondeurMax) {
        nbnode++;
        int[] tabValeursRouge = new int[Board2.getSize()];
        int[] tabValeurBleu = new int[Board2.getSize()];
        Position2 nextPosBleu;
        Position2 nextPosRouge;
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

        for (int i = 0; i < Board2.getSize(); i++) { // changement de coup valide pour les joueurs fait ?

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

    public int alphaBetaValue(Position2 posCourante, int alpha , int beta ,boolean iaTurn, int profondeur, int profondeurMax){
        nbnode++;
        Position2 nextPosBleu;
        Position2 nextPosRouge;
        int blueAlpha = 0;
        int redAlpha = 0;
        int blueBeta = 0;
        int redBeta = 0;
        int num = iaTurn ? numPlayer : (numPlayer == 1 ? 2 : 1);
        if(posCourante.isWinning()){
            return 64;
        }
        if(posCourante.isLoosing()){
            return -64;
        }
        if(posCourante.isDraw()){
            return 0;
        }
        if(profondeur ==  profondeurMax){
            return evaluate2(posCourante,iaTurn,profondeur);
        }
        if(iaTurn) {
            for (int i = 0; i < Board2.getSize(); i++) {
                if (posCourante.coupValideBleu(i, num)) {
                    nextPosBleu = posCourante.getNextPosition(i, true); // pos_next devient la position courante, et on change le joueur
                    // il faut peut être utiliser un alphaBlue et un alphaRed
                    //blueAlpha = Math.max(blueAlpha, alphaBetaValue(nextPosBleu, blueAlpha, beta, nextPosBleu.isIaTurn(), profondeur + 1, profondeurMax));
                    alpha = Math.max(alpha, alphaBetaValue(nextPosBleu, alpha, beta, nextPosBleu.isIaTurn(), profondeur + 1, profondeurMax));
                    if (alpha >= beta) {
                        return alpha;
                    }
                } else {
                    if (iaTurn) {
                        alpha = -100;
                    } else {
                        alpha = 100;
                    }
                }
                if (posCourante.coupValideRouge(i, num)) {
                    nextPosRouge = posCourante.getNextPosition(i, false);
                    //redAlpha = Math.max(redAlpha, alphaBetaValue(nextPosRouge, redAlpha, beta, nextPosRouge.isIaTurn(), profondeur + 1, profondeurMax));
                    alpha = Math.max(alpha, alphaBetaValue(nextPosRouge, alpha, beta, nextPosRouge.isIaTurn(), profondeur + 1, profondeurMax));

                    if (alpha >= beta) {
                        return alpha;
                    }
                } else {
                    if (iaTurn) {
                        alpha = -100;
                    } else {
                        alpha = 100;
                    }
                }
            }
            return alpha;
            //return Math.max(blueAlpha, redAlpha);
        } else {
            for (int i = 0; i < Board2.getSize(); i++) {
                if (posCourante.coupValideBleu(i, num)) {
                    nextPosBleu = posCourante.getNextPosition(i, true); // pos_next devient la position courante, et on change le joueur
                    // même chose qu'avec les alpha, un par couleur
                    //blueBeta = Math.min(blueBeta, alphaBetaValue(nextPosBleu, alpha, blueBeta, nextPosBleu.isIaTurn(), profondeur + 1, profondeurMax));
                    beta = Math.min(beta, alphaBetaValue(nextPosBleu, alpha, beta, nextPosBleu.isIaTurn(), profondeur + 1, profondeurMax));
                    if (beta <= alpha) {
                        return beta;
                    }
                } else {
                    if (iaTurn) {
                        beta = -100;
                    } else {
                        beta = 100;
                    }
                }
                if (posCourante.coupValideRouge(i, num)) {
                    nextPosRouge = posCourante.getNextPosition(i, false);
                    //redBeta = Math.min(redBeta, alphaBetaValue(nextPosRouge, alpha, redBeta, nextPosRouge.isIaTurn(), profondeur + 1, profondeurMax));
                    beta = Math.min(beta, alphaBetaValue(nextPosRouge, alpha, beta, nextPosRouge.isIaTurn(), profondeur + 1, profondeurMax));

                    if (beta <= alpha) {
                        return beta;
                    }
                } else {
                    if (iaTurn) {
                        beta = -100;
                    } else {
                        beta = 100;
                    }
                }
            }
            return beta;
            //return Math.min(blueBeta, redBeta);
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
