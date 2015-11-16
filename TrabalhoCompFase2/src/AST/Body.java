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
public class Body{

	private DclPart dcl_;
	private CmpStmt compstmt_;
	
	
	public Body(DclPart d, CmpStmt c){
		this.dcl_ = d;
		this.compstmt_ = c;
	}
        /*
        body ::= [dclpart] compstmt
        dclpart ::= VAR dcls
        dcls ::= dcl {dcl}
        dcl ::= idlist ’:’ type ’;’
        compstmt ::= BEGIN stmts END
        stmts ::= stmt {’;’ stmt} ’;’
        stmt ::= ifstmt

        | whilestmt
        | assignstmt
        | compstmt
        | readstmt
        | writestmt
        | writelnstmt

        */
        public void genC(PW pw){
            
            if(dcl_ != null)   
                dcl_.genC(pw);
            
            compstmt_.genC(pw);
                
        }
           

}