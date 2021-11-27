
#pragma once
#include <iostream>
class board{


public:
	const static int size = 6;
	const static int initialGraine = 4;
	int trous1[size];
	int trous2[size];

	int getT1(int i);
	int getT2(int i);
	void setT1(int i, int val);
	void setT2(int i, int val);

	int* getTrous1() { return trous1; };
	int* getTrous2() { return trous2; };
	board();
	std::string toString();
	

};





