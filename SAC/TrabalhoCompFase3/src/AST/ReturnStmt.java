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


public class ReturnStmt extends Stmt {
	private Expr myExpr;
	
	public ReturnStmt(Expr expr) {
		this.myExpr = expr;
	}
	
	@Override
	public void genC(PW pw) {
		System.out.print("return ");
		if (myExpr != null) {
			myExpr.genC(pw);
		}
		pw.println(";\n}\n");
	}
}
