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
public class Term {
	private Factor factor;
	private ArrayList<String> mulopList;
	private ArrayList<Factor> factorList;
	
	public Term(Factor factor, ArrayList<String> mulopList, ArrayList<Factor> factorList) {
		this.factor = factor;
		this.mulopList = mulopList;
		this.factorList = factorList;
	}
	// term ::= factor {mulop factor}
	public void genC(PW pw) {
            
            factor.genC(pw);
            for(int i=0, k=0; i < mulopList.size() && k < factorList.size(); i++, k++){
               pw.print(mulopList.get(i));
               factorList.get(k).genC(pw);
            }
            
        }
}
