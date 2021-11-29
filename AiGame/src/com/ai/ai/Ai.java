package com.ai.ai;

import com.ai.game.Board;
import com.ai.game.Position;

import java.util.Arrays;

public class Ai {
    private int score;


    public Ai(){
    }

    public int evatualte(Position posCourante, boolean iaTurn, int Profondeur) {
        return posCourante.getPionsPrisOrdi() - posCourante.getPionsPrisJoueur();

    }

    public int valeurMinMax(Position posCourante, boolean iaTurn, int profondeur, int profondeurMax) {
        int[] tabValeurs = new int[Board.getSize()];
        Position nextPos;

        if (posCourante.IsFinalPosition()) {
            int valuation = evatualte(posCourante, iaTurn, profondeur);
            if (valuation > 0) {
                return 48;
            } else {
                return -48;
            }
        }
        if (profondeur == profondeurMax) {
            return evatualte(posCourante, iaTurn, profondeur);
            // dans un premier temps l'évaluation sera la
            // différence du nb de pions pris
        }
        for (int i = 0; i < Board.getSize(); i++) {

            if (posCourante.coupValide(i)) {
                nextPos = posCourante.getNextPos(i); // pos_next devient la position courante, et on change le joueur
                tabValeurs[i] = valeurMinMax(nextPos, nextPos.isIaTurn(), profondeur + 1, profondeurMax);
            }
            else {
                //condition bizzare

            }
        }
        int res = evatualte(posCourante, posCourante.isIaTurn(),profondeur);// pour le moment
        //System.out.println(res);
        if(iaTurn){
            // ecrire le code: res contient le MAX de
            // tab_valeurs
        } else {
            // ecrire le code : res contient le MIN de
            // tab_valeurs
        }
       // System.out.println(Arrays.toString(tabValeurs));
        return res;
    }


}
