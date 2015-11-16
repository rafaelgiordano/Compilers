/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import java.util.ArrayList;


public class Dcl{

	private ArrayList<String> id;
	private Type type;
        
	public Dcl(ArrayList<String> d, Type type){
		this.id = d;
                this.type = type;
	}
        
        /*
        dclpart ::= VAR dcls
        dcls ::= dcl {dcl}
        dcl ::= idlist ’:’ type ’;’

        */
        public void genC(PW pw){
           
            pw.println(type.getType());
            for(int k=0; k < id.size();k++){
                pw.print(id.get(k));
                if(k < id.size()-1)
                    pw.print(",");
            }
           
            
        }


}
