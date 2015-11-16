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
public class VariableList {
    private ArrayList<Variable> vblist;
    
    
    public VariableList (ArrayList<Variable> vblist){
        this.vblist = vblist;
    }
    
    public void genC(PW pw){
        for(Variable v: vblist){
            String t = v.getType().toString();
            if ( t.equals("int"))
                pw.print("%d ");
            else
                if(t.equals("float"))
                    pw.print("%f ");
                else
                    pw.print("%s ");
        }
        
        pw.print("\",");
        
        for(Variable a: vblist){
            pw.print(a.getName());
            if(a.equals(vblist.get(vblist.size()-1)))
                pw.print(a.getName());
            else
                pw.print(a.getName() + ",");
            
        }
        pw.print(")");
    }   
}
