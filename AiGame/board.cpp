
#include "board.h"
#include <sstream>
using namespace std;


int board::getT1(int i) {
	return trous1[i];
}
int board::getT2(int i) {
	return trous2[i];
}
void board::setT1(int i, int val) {
	trous1[i] = val;
}
void board::setT2(int i, int val) {
	trous2[i] = val;
}




board::board() {
	for (int i = 0; i < size; i++) {
		setT1(i, initialGraine);
		setT2(i, initialGraine);
	}

}

string board::toString() {
	std::ostringstream res;
	
	res << "| ";
	for (int i = 0; i < size; i++) {
		res << trous1[i];
		res << " | ";
	}
	res << "\n";
	for (int i = 0; i <= size*4; i++) { res << "-"; }
	res << "\n";
	res << "| ";
	for (int i = 0; i < size; i++) {
		
		res << trous2[i];
		res << " | ";
	}
	return res.str();
}






