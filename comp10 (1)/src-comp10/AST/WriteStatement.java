package AST;

import java.io.*;

public class WriteStatement extends Statement {
    
    public WriteStatement( Expr expr ) {
        this.expr = expr;
    }
 
    public void genC( PW pw ) {
        
        if ( expr.getType() == Type.booleanType ) {
          pw.print("printf(\"%s\\n\", ");
          expr.genC(pw, false);
          pw.out.print(" ? \"True\" : \"False\"");
        }
        else {
            if ( expr.getType() == Type.charType ) 
              pw.print("printf(\"%c\\n\", ");
            else
              pw.print("printf(\"%d\\n\", ");
            expr.genC(pw, false);
        }
        pw.out.println(" );");
    }
    
    
    private Expr expr;
}