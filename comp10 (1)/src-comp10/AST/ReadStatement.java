package AST;

import java.io.*;

public class ReadStatement extends Statement {
    public ReadStatement( Variable v ) {
        this.v = v;
    }
 
    public void genC( PW pw ) {
        if ( v.getType() == Type.charType ) 
          pw.print("{ char s[256]; gets(s); sscanf(s, \"%c\", &"  );
        else // should only be an integer
          pw.println("{ char s[256]; gets(s); sscanf(s, \"%d\", &" +
             v.getName() + "); }" ); 
    }
    private Variable v;
}