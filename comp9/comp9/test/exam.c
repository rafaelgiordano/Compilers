#include <stdio.h>

void main() {
   int i;
   int j;
   int achou;
   char ch;

   i = 1;
   j = (((i * 3) - (4 % i)) + (((3 * 2) * i) / 2));
   if ( ((((i + 1) > (j - 3)) && (i <= (j + 5))) || (4 < i)) ) { 
      printf("%d\n", i );
      printf("%d\n", j );
   }
   ch = 'a';
   achou = 0;
   if ( ((ch >= 'b') && !achou) ) { 
      { char s[256]; gets(s); sscanf(s, "%c", &ch); }
   }
}
