package AST;

import java.io.*;

public class WhileStatement extends Statement {
    
    public WhileStatement( Expr expr, Statement statement ) {
        this.expr = expr;
        this.statement = statement;
    }
    
    public void genC( PW pw ) {
        
        pw.print("while ( ");
        expr.genC(pw, false);
        pw.out.println(" )");
        if ( statement instanceof CompositeStatement )
           statement.genC(pw);
        else {
           pw.add();
           statement.genC(pw);
           pw.sub();
        }
    }
    
    private Expr expr;
    private Statement statement;
}