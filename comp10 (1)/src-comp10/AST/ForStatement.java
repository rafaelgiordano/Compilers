package AST;

import java.io.*;

public class ForStatement extends Statement {
    
    public ForStatement( Variable forVariable,
                         Expr exprStart, 
                         Expr exprEnd,
                         Statement statement ) {
        this.forVariable = forVariable;
        this.exprStart = exprStart;
        this.exprEnd = exprEnd;
        this.statement= statement;
    }
    
    public void genC( PW pw ) {
        pw.print("for ( " + forVariable.getName() + " = ");
        exprStart.genC(pw, false);
        pw.out.print("; " + forVariable.getName() + " <= ");
        exprEnd.genC(pw, false);
        pw.out.println("; " + forVariable.getName() + "++ )");
        if ( statement != null ) { 
           if ( statement instanceof CompositeStatement )
              statement.genC(pw);
           else {
              pw.add();
              statement.genC(pw);
              pw.sub();
           }
        }
    }

    private Variable forVariable;
    private Expr exprStart, exprEnd;
    private Statement statement;
}