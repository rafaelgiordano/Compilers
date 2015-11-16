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
public class Expr {
    private SimExpr simexpr;
	private String relop;
	private Expr expr;

    public Type getType() {
        return type;
    }
	private Type type;
	
	public Expr(SimExpr simexpr, String relop, Expr expr) {
		this.simexpr = simexpr;
		this.relop = relop;
		this.expr = expr;
	}
	
        
        /*
        exprlist ::= expr {’,’ expr}
        expr ::= simexp [relop expr]
        simexp ::= [unary] term {addop term}
        term ::= factor {mulop factor}
        */
	public void genC(PW pw) {
            
            simexpr.genC(pw);
            if(relop != null){
                pw.print(relop);
                expr.genC(pw);
            }
        
        }
}
