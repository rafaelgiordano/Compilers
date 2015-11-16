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
public class StmtList {
    private ArrayList<Stmt> stmts;
    
    public StmtList(ArrayList<Stmt> stmts){
        this.stmts = stmts;
    }
    /*
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
        for (Stmt s:stmts) {
            s.genC(pw);
            pw.print(";");
        }
    }
}
