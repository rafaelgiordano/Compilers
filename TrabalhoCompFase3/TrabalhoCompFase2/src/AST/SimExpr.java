/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import java.util.ArrayList;

/**
 *
 * @author home
 */
public class SimExpr {
    
	private String unary;
        private Term term;
        private	ArrayList<String> addopList;
         private ArrayList<Term> termList;
    
    
    public SimExpr( String unary,Term term, ArrayList<String> addopList, ArrayList<Term> termList) {
		
        this.unary = unary;
        this.term = term;
		
	this.addopList = addopList;
	this.termList = termList;
	}
	/*
        simexp ::= [unary] term {addop term}
        unary ::= ’+’ | ’-’ | NOT
        term ::= factor {mulop factor}
        addop ::= ’+’ | ’-’ | OR
        */
	public void genC(PW pw) {
            
            if(unary!=null)
                pw.println(unary);
            term.genC(pw);
            
            if(addopList!=null){
                for(int i=0; i < addopList.size();i++){
                    pw.print(addopList.get(i));
                    termList.get(i).genC(pw);
                }
            }
            
        }
}
