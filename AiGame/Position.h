#pragma once
#include <iostream>
class Position
{

	const static int size = 6;
	const static int initialGraine = 4;
	int* case_joueurs;
	int* case_ordi;
	bool ia_turn;
	int pions_pris_joueur; //pions pris par le joueur
	int pions_pris_ordi; // pions pris par l'ordi
	Position();

	int getSize() { return size; }

	int* getCaseJoueur() {return case_joueurs;}

	int* getCaseOrdi() { return case_ordi; }


	bool iaTurn() { return ia_turn; }


	int getPionsPrisJoueur() { return pions_pris_joueur; }

	int getPionsPrisOrdi(){ return pions_pris_ordi; }


	void addPionsPrisJoueur(int i ){ pions_pris_joueur += i ; }

	void addPionsPrisOrdi(int i) { pions_pris_ordi += i; }




};

