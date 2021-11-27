#include "Position.h"
#include <iostream>
#include <sstream>
using namespace std;



Position Position::nexPos(int trou) {
	Position nextPos;
	bool ordi = !ia_turn;
	int tab_ordi[size];
	int tab_joueur[size];
	//memcpy(tab_ordi, case_ordi, size);
	//memcpy(tab_joueur, case_joueurs, size);
	for (int i = 0; i < size; i++) {
		tab_ordi[i] = case_ordi[i];
		tab_joueur[i] = case_joueurs[i];
	}

	if (ordi) {
		int nb_graine = tab_ordi[trou];
		tab_ordi[trou] = 0;
		int index = trou + 1;
		bool ops = false;
		while (nb_graine > 0) {
			ops = false;// nous dis si l'index se trouve chez l'opposant ou chez nous.
			for (int i = index; i < size; i++) {
				if (nb_graine > 0) {
					tab_ordi[i]++;
					nb_graine--;
				}
				else { break; }
			}
			if (nb_graine > 0) {
				index = 0;
				for (int i = index; i < size; i++) {
					if (nb_graine > 0) {
						tab_joueur[i]++;
						nb_graine--;
						index++;
					}
					else { break; }
				}
				ops = true; // nous dis si l'index se trouve chez l'opposant ou chez nous.
			}
		}
		if (ops) {
			if (tab_joueur[index] <= 3) {
				while (index >= 0 && tab_joueur[index] <= 3) {
					pions_pris_ordi += tab_joueur[index];
					tab_joueur[index] = 0;
					index--;
				}
			}
		}

	}
	else {
		int nb_graine = tab_joueur[trou];
		tab_joueur[trou] = 0;
		int index = trou + 1;
		bool ops = false;
		while (nb_graine > 0) {
			ops = false;

			for (int i = index; i < size; i++) {
				if (nb_graine > 0) {
					tab_joueur[i]++;
					nb_graine--;
				}
				else { break; }
			}
			if (nb_graine > 0) {
				index = 0;
				for (int i = index; i < size; i++) {
					if (nb_graine > 0) {
						tab_ordi[i]++;
						nb_graine--;
						index++;
					}
					else { break; }
				}
				ops = true;

			}
		}
		if (ops) {
			if (tab_ordi[index] <= 3) {
				while (index >= 0 && tab_ordi[index] <= 3) {
					pions_pris_ordi += tab_ordi[index];
					tab_ordi[index] = 0;
					index--;
				}
			}
		}

	}

	nextPos.case_joueurs = tab_joueur;
	nextPos.case_ordi = tab_ordi;
	nextPos.ia_turn = ordi;
	nextPos.pions_pris_joueur = pions_pris_joueur;
	nextPos.pions_pris_ordi = pions_pris_ordi;


	cout << "\n\n";


	cout << nextPos.toString();

	cout << "\n\n";

	return nextPos;
}


string Position::toString() {
	std::ostringstream res;

	res << "| ";
	for (int i = size - 1; i >= 0; i--) {
	
		res << case_joueurs[i];
		res << " | ";
	}
	res << "\n";
	for (int i = 0; i <= size * 4; i++) { res << "-"; }
	res << "\n";
	res << "| ";
	for (int i = 0; i < size; i++) {

		res << case_ordi[i];
		res << " | ";
	}
	return res.str();
}
