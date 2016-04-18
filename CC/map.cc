#include <iostream>
#include <map>

using namespace std;

int main()
{
	map<int, int> m1;
	map<int, int>::iterator it;

	m1.insert(pair<int, int>(1, 20));
	m1.insert(pair<int, int>(4, 40));
	m1.insert(pair<int, int>(3, 60));
	m1.insert(pair<int, int>(2, 50));
	m1.insert(pair<int, int>(6, 40));
	m1.insert(pair<int, int>(7, 30));

	cout << "The original map is: " <<endl;
	for (it = m1.begin(); it != m1.end(); it++) {
		cout << it->first << " " << it->second << endl;
	}

	return 0;

}