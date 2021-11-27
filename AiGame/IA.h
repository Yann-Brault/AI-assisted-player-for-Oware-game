#pragma once
#include "Position.h"
class IA
{
	Position pos_courante;
	Position pos_next;

	int valeurMinMax(Position* pos_courante, int profondeur, int profondeurMax);


	int positionFinale(Position* pos_courante, bool tourOrdi, int profondeur);





};

