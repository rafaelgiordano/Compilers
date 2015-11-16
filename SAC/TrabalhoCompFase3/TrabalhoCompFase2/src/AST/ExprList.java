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
public class ExprList {
    private ArrayList<Expr> exprlist;
    
    public ExprList(ArrayList<Expr> exprlist){
        this.exprlist = exprlist;
    }
    public ArrayList<Type> getTypeList(){
        ArrayList<Type> type= new ArrayList<Type>();
        for(Expr e: exprlist){
            type.add(e.getType());
        }
        return type;
    }
    //exprlist ::= expr {’,’ expr}
    public void genC(PW pw){
        for (Expr exprlist1 : exprlist) 
            exprlist1.genC(pw);
        
    }
}
