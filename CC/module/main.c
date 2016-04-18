#include <stdio.h>
#include "first.h"
#include "second.h"

int main() 
{
	char * str = "call print func!";
	print(str);

	int a = 3, b = 4;
	printf("max: %d, ", max(a, b));

	return 0;
}