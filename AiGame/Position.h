#pragma once
#include <iostream>
#include <sstream>
using namespace std;
class Position
{
public:
	const static int size = 6;
	const static int initialGraine = 4;
	int* case_joueurs;
	int* case_ordi;
	bool ia_turn;
	int pions_pris_joueur; //pions pris par le joueur
	int pions_pris_ordi; // pions pris par l'ordi

	Position() {
		case_joueurs =NULL;
		case_ordi = NULL;
		ia_turn = NULL;
		pions_pris_joueur = 0;
		pions_pris_ordi = 0;
	};

	Position(int* joueurs, int* ordi, bool iaturn) {
		case_joueurs = joueurs;
		case_ordi = ordi;
		ia_turn = iaturn;
		pions_pris_joueur = 0;
		pions_pris_ordi = 0;
	}
	Position(int* joueurs, int* ordi, bool iaturn, int pionsJoueur, int pionsOrdi) {
		case_joueurs = joueurs;
		case_ordi = ordi;
		ia_turn = iaturn;
		pions_pris_joueur = pionsJoueur;
		pions_pris_ordi = pionsOrdi;

	}


	int getSize() { return size; }

	int* getCaseJoueur() { return case_joueurs; }

	int* getCaseOrdi() { return case_ordi; }


	bool iaTurn() { return ia_turn; }


	int getPionsPrisJoueur() { return pions_pris_joueur; }

	int getPionsPrisOrdi() { return pions_pris_ordi; }


	void addPionsPrisJoueur(int i) { pions_pris_joueur += i; }

	void addPionsPrisOrdi(int i) { pions_pris_ordi += i; }


	Position nexPos(int trou);


	string toString();


};

