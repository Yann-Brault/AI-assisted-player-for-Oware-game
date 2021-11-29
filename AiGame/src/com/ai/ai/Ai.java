package com.ai.ai;

import com.ai.game.Board;
import com.ai.game.Position;

import java.util.Arrays;
import java.util.Random;

public class Ai {
    private int score;


    public Ai(){
    }

    public int evatualte(Position posCourante, boolean iaTurn, int Profondeur) {
//        System.out.println("hello " +(posCourante.getPionsPrisOrdi() - posCourante.getPionsPrisJoueur()) );
        return posCourante.getPionsPrisOrdi() - posCourante.getPionsPrisJoueur();

    }

    public int valeurMinMax(Position posCourante, boolean iaTurn, int profondeur, int profondeurMax) {
        int[] tabValeurs = new int[Board.getSize()];
        Position nextPos;

        if (posCourante.IsFinalPosition()) {
//            System.out.println("fin de jeu ");
            int valuation = evatualte(posCourante, iaTurn, profondeur);
            if (valuation >= 0) {
                return 48;
            } else {
                return -48;
            }
        }
        if (profondeur == profondeurMax) {
            return evatualte(posCourante, posCourante.isIaTurn(), profondeur);
            // dans un premier temps l'évaluation sera la
            // différence du nb de pions pris
        }
        for (int i = 0; i < Board.getSize(); i++) {

            if (posCourante.coupValide(i)) {
                nextPos = posCourante.getNextPos(i); // pos_next devient la position courante, et on change le joueur
                tabValeurs[i] = valeurMinMax(nextPos, nextPos.isIaTurn(), profondeur + 1, profondeurMax);
            }
            else {
                if(iaTurn){
                tabValeurs[i] = -100;
                }
                else{
                    tabValeurs[i] = 100;
                }

            }
        }
        int res;
//        System.out.println(" tour IA "+iaTurn+" "+Arrays.toString(tabValeurs));
        if(iaTurn){
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


    public static int max(int[] array){
        int max = 0;
        for(int i = 0 ; i < Board.getSize(); i++){
            if(array[i] > max){
                max = array[i];
            }
        }
        return max;
    }
    public static int min(int[] array){
        int min = 0;

        for(int i = 0 ; i < Board.getSize(); i++){
            if(array[i] < min){
                min = array[i];
            }
        }
        return min;
    }


}
