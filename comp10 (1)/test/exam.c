#include <stdio.h>

void print(int x, int y, char ch) {
   int i;
   printf("%c\n", ch );
   i = 1;
   while ( i <= 5 )
   {
      printf("%d\n", x + y );
      i = i + 1;
   }
   while ( i <= 50 )
      i = i + 1;
   for ( i = 1; i <= 10; i++ )
      x = x + 1;
   for ( i = 1; i <= 10; i++ )
   {
      x = x + 1;
      y = y + 1;
   }
}


int fatorial(int n) {

   if ( n <= 1 ) { 
      return 1;
   }
   else {
      return n * fatorial(n - 1);
   }
}


void main() {
   int n;
   printf("%d\n", fatorial(5) );
   print(0, 1, 'A');
   for ( n = 1; n <= 10; n++ )
      printf("%d\n", n );
}


