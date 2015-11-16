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
public class Factor {
	private Variable vbl;
	private String number;
	private Expr expr;
	private String string;
        private Stmt myProcFunc;
                
	public Factor(Variable vb, String n, Expr expr, String s, Stmt myProcFunc ) {
		this.vbl = vbl;
		this.number = n;
		this.expr = expr;
		this.string = s;
                this.myProcFunc = myProcFunc;
	}
	
        //factor ::= vbl | num | ’(’ expr ’)’ | ”’.”’
	public void genC(PW pw) {
            if(vbl!=null){
                vbl.genC(pw);
            }else if(number!=null){
                pw.print(number);
            }else if (myProcFunc != null) {
                myProcFunc.genC(pw);
            }else if(expr!=null){
                expr.genC(pw);
            }else if(string != null){
                pw.print(string);
            }
        } 
}
