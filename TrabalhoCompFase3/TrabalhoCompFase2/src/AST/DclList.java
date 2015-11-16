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
public class DclList {
    private ArrayList<Dcl> dclList;
    
    public DclList(ArrayList <Dcl> dclList){
        this.dclList = dclList;
    }
    
    public void genC(PW pw){
        
        for (Dcl dclList1 : dclList) 
            dclList1.genC(pw);
        
    }
}
