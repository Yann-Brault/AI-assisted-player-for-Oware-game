package com.ai.ai;

import com.ai.game.Board;
import com.ai.game.Position;

import java.util.concurrent.atomic.AtomicInteger;

public class Ai {
    private int score;
    public static AtomicInteger nbNode = new AtomicInteger(0); //Atomic to avoid concurrent access from the different threads
    public static int nbCut = 0;
    public static int nbTurn = 0;
    public static int numPlayer = 0;
    public static int numOpponent = 0;

    public Ai(int nb) {
        numPlayer = nb;
        numOpponent = nb == 1 ? 2 : 1;
    }

    /**
     * @param pos
     * @return a int
     * the higher the value the better the move is for the AI
     * the lower is the value the better the move is for the opponent of the AI
     */
    public static int evaluate2(Position pos) {
        if (pos.getPionsPrisOrdi() >= 33) {
            return Integer.MAX_VALUE;
        }
        if (pos.getPionsPrisJoueur() >= 33) {
            return Integer.MIN_VALUE;
        }
        if (pos.isFinalPosition() && pos.nbcoupValide(numOpponent) == 0) {
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
        if (pos.nbGrainesPlayerRouge(numPlayer) + pos.nbGrainesPlayerRouge(numOpponent) <= 4) {
            val -= 100;
        }
        val += (pos.getPionsPrisOrdi()) - (pos.getPionsPrisJoueur() + pos.nbGrainePlayer(numOpponent)) * 2 + pos.nbGrainePlayer(numPlayer) - pos.nbGrainesPlayerBleu(numOpponent) + pos.nbGrainesPlayerBleu(numPlayer);
        if (pos.isIaTurn()) {
            if (numPlayer == 2) {
                return val - pos.nbCasePrenable() * 4 - pos.nbComboCasePrenable() * 4;
            }
            return val - pos.nbCasePrenable() * 2 - pos.nbComboCasePrenable() * 4;
        } else {
            return val - pos.nbCasePrenable() * 2 + pos.nbComboCasePrenable() * 4;
        }
    }

    /**
     * MiniMax with Alpha-Beta Pruning
     * @param posCourante
     * @param iaTurn
     * @param alpha
     * @param beta
     * @param profondeur
     * @param profondeurMax
     * @return an int which is the value of a game state
     */
    public static int alphaBeta(Position posCourante, boolean iaTurn, int alpha, int beta, int profondeur, int profondeurMax) {
        nbNode.addAndGet(1);
        int num = numPlayer;
        int numOpponent = num == 1 ? 2 : 1;
        if (posCourante.isFinalPosition() || profondeur == profondeurMax) {
            return evaluate2(posCourante);
        }
        if (iaTurn) {
            int value = Integer.MIN_VALUE;
            for (int couleur = 0; couleur < 2; couleur++) {
                for (int i = 0; i < Board.getSize(); i++) {
                    if (couleur == 0) {
                        if (posCourante.coupValideBleu(i + 1, num)) {
                            Position nextPosBleu = posCourante.getNextPosition(i, true);
                            int evalB = alphaBeta(nextPosBleu, false, alpha, beta, profondeur + 1, profondeurMax);
                            value = Math.max(value, evalB);
                        }
                    } else {
                        if (posCourante.coupValideRouge(i + 1, num)) {
                            Position nextPosRouge = posCourante.getNextPosition(i, false);
                            int evalR = alphaBeta(nextPosRouge, false, alpha, beta, profondeur + 1, profondeurMax);
                            value = Math.max(value, evalR);
                        }
                    }
                    if (beta <= alpha) {
                        nbCut++;
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
                        if (posCourante.coupValideBleu(i + 1, numOpponent)) {
                            Position nextPosBleu = posCourante.getNextPosition(i, true);
                            int evalB = alphaBeta(nextPosBleu, true, alpha, beta, profondeur + 1, profondeurMax);
                            value = Math.min(value, evalB);
                        }
                    } else {
                        if (posCourante.coupValideRouge(i + 1, numOpponent)) {
                            Position nextPosRouge = posCourante.getNextPosition(i, false);
                            int evalR = alphaBeta(nextPosRouge, true, alpha, beta, profondeur + 1, profondeurMax);
                            value = Math.min(value, evalR);
                        }
                    }
                    if (beta <= alpha) {
                        nbCut++;
                        return value;
                    }
                    beta = Math.min(beta, value);
                }
            }
            return value;
        }
    }
}
