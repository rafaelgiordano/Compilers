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
public class WriteStmt extends Stmt {
    private ExprList exprList;
    private boolean newLine;

	public WriteStmt(ExprList exprList, boolean newLine) {
            this.exprList = exprList;
            this.newLine = newLine;
	}
        
   //WRITE ’(’ exprlist ’)’
   // WRITELN ’(’ [exprlist] ’)’
   public void genC(PW pw){
       
       ArrayList<Type> type;
       type = exprList.getTypeList();
       pw.print("printf(\"");
       for(Type a: type){
            String t = a.getType();
            
            if( t.equals("int"))
                pw.print("%d ");
            else
                if(t.equals("float"))
                    pw.print("%f ");
                else
                    pw.print("%s ");
                
            
       }
       pw.print("\",");
       exprList.genC(pw);
       pw.print(")");
   }
    
}
