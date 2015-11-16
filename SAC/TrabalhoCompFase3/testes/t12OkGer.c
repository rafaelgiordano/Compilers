#include <stdio.h>
#include <string.h>

float a;
float p;
float raio;

float area( float r ) {
	return (3.14 * r) * r;
}

float perimetro( float r ) {
	return (3.14 * 2) * r;
}

int main() {
	printf("%s\n", "Informe o raio: ");
	scanf("%f", raio);
	a = area(raio);
	p = perimetro(raio);
	printf("%f\n", a);
	printf("%f\n", p);
}
