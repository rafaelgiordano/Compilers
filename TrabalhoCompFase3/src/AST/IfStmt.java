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
public class IfStmt extends Stmt{
	private Expr expr;
	private Stmt thenStmt;
	private Stmt elseStmt;
	
	public IfStmt(Expr expr, Stmt thenStmt, Stmt elseStmt) {
		this.expr = expr;
		this.thenStmt = thenStmt;
		this.elseStmt = elseStmt;
	}
        
    /*
      ifstmt ::= IF expr THEN stmts [ELSE stmts] ENDIF
  
    */
    public void genC(PW pw){
        pw.println("if(");
        expr.genC(pw); 
        pw.print("){");
        thenStmt.genC(pw);
        pw.println("}");
        if(elseStmt!=null){
            pw.print("else{");
            elseStmt.genC(pw);
            pw.println("}");
        }
    }
}
