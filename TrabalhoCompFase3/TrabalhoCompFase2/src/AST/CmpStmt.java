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
public class CmpStmt extends Stmt {
    
    private StmtList stmtlist;
    
    public CmpStmt(StmtList stmts){
        this.stmtlist = stmts;
    }
    
    /*
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
        
        stmtlist.genC(pw);
        
    }
}
