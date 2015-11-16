#include <stdio.h>
#include <string.h>

int apple, pear, orange;
string text;
float number;
char a, b;

void writentimes( string text, int n ) {
	int i;
	i = 1;

	while ( i <= n ) {
		printf("%s", text);
	}
}

int factorial( int n ) {
	int i;
	int product;

	product = 1;
	i = 2;
	
	while ( i <= n ) {
		product = product * i;
	}
}

int main() {
	apple = 5;
	a = 'A';
	pear = factorial(apple);
	orange = 10;
	number = 4.2;
	text = "Hello, world!";
	printf("%s", text);
	gets(text);
	printf("%s\n", text);
}
