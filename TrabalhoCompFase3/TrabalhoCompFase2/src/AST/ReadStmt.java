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
public class ReadStmt extends Stmt{
    private VariableList vblist;
    
    public ReadStmt(VariableList vb){
        this.vblist = vb;
    }
    
    
    //readstmt ::= READ ’(’ vblist ’)’

    public void genC(PW pw){
        pw.println("scanf(\"%");
        vblist.genC(pw);
        pw.print(")");
    }
    
    
    
}
