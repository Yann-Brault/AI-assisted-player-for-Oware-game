package com.ai.ai;

import com.ai.Game2.Board2;
import com.ai.Game2.Position2;
import com.ai.game.Board;
import com.ai.game.Position;

public class Ai {
    private int score;
    public int nbnode = 0;
    public final int numPlayer;
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
    public int evaluate2(Position2 pos, boolean iaTurn,int Profondeur){
        return pos.getPionsPrisOrdi() - pos.getPionsPrisJoueur();
    }

    public int valeurMinMax(Position posCourante, boolean iaTurn, int profondeur, int profondeurMax) {
        nbnode++;
        int[] tabValeurs = new int[Board.getSize()];
        Position nextPos;

        if (posCourante.IsFinalPosition()) {
//            System.out.println("fin de jeu ");
            int valuation = evaluate(posCourante, iaTurn, profondeur);
            if (posCourante.getPionsPrisOrdi() > posCourante.getPionsPrisJoueur()) {
                return 48;
            } else {
                return -48;
            }
        }
        if (profondeur == profondeurMax) {
            return evaluate(posCourante, posCourante.isIaTurn(), profondeur);
            // dans un premier temps l'évaluation sera la
            // différence du nb de pions pris
        }
        for (int i = 0; i < Board.getSize(); i++) {

            if (posCourante.coupValide(i)) {
                nextPos = posCourante.getNextPos(i); // pos_next devient la position courante, et on change le joueur
                tabValeurs[i] = valeurMinMax(nextPos, nextPos.isIaTurn(), profondeur + 1, profondeurMax);
            } else {
                if (iaTurn) {
                    tabValeurs[i] = -100;
                } else {
                    tabValeurs[i] = 100;
                }
            }
        }

        int res;
//        System.out.println(" tour IA "+iaTurn+" "+Arrays.toString(tabValeurs));
        if (iaTurn) {
            res = max(tabValeurs);
//            System.out.println(res);
            // ecrire le code: res contient le MAX de
            // tab_valeurs
        } else {
            res = min(tabValeurs);
            // ecrire le code : res contient le MIN de
            // tab_valeurs
        }
//        if(res > 0){
//            System.out.println("res = " + res );
//        }
        return res;
    }


    public int valeurMinMax2(Position2 posCourante, boolean iaTurn, int profondeur, int profondeurMax) {
        nbnode++;
        int[] tabValeursRouge = new int[Board2.getSize()];
        int[] tabValeurBleu = new int[Board2.getSize()];
        Position2 nextPosBleu;
        Position2 nextPosRouge;

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
            return evaluate2(posCourante,iaTurn,profondeur);
            // dans un premier temps l'évaluation sera la
            // différence du nb de pions pris
        }

        for (int i = 0; i < Board2.getSize(); i++) { //

            if (posCourante.coupValideBleu(i, numPlayer)) {
                nextPosBleu = posCourante.getNextPosition(i, true); // pos_next devient la position courante, et on change le joueur
                tabValeurBleu[i] = valeurMinMax2(nextPosBleu, nextPosBleu.isIaTurn(), profondeur + 1, profondeurMax);
            } else {
                if (iaTurn) {
                    tabValeurBleu[i] = -100;
                } else {
                    tabValeurBleu[i] = 100;
                }

            }
            if (posCourante.coupValideRouge(i, numPlayer)) {
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
        }
        else {
            maxBleu = min(tabValeurBleu);
            maxRouge = min(tabValeursRouge);
            res = Math.min(maxBleu, maxRouge);
        }
        return res;
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
