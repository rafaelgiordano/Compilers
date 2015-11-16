/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

/**
 *
 * @author home
 */
public class WhileStmt extends Stmt{
    private Expr expr;
    private Stmt doStmt;
	
    public WhileStmt(Expr expr, Stmt dostmt) {
		this.expr = expr;
		this.doStmt = dostmt;
	}
    
    //WHILE expr DO stmts ENDWHILE
    public void genC(PW pw){
        
        pw.println("while(");
        expr.genC(pw);
        pw.print("){");
        doStmt.genC(pw);
        pw.println("}");
        
    }
}
