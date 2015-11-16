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
public class AssignStmt extends Stmt {
	private Variable vbl;
	private Expr expr;
	
	public AssignStmt(Variable vbl, Expr expr) {
		this.vbl = vbl;
		this.expr = expr;
	}
        public void genC(PW pw){
            vbl.genC(pw);
            pw.print(" = ");
            expr.genC(pw);
        }
}
