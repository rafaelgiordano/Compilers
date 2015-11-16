/*
Primeiro Trabalho de Laboratório de Compiladores
 
Segundo Semestre de 2015.
Campus de Sorocaba da UFSCar
Prof. José de Oliveira Guimarães
 
Grupo:
Nome:Lucas Gabriel Mendes de Oliveira  RA: 380091
Nome:Rafael Paschoal Giordano       RA: 408298
 */
package ast;


public class WhileStmt extends Statement {

    private Expr expr;
    private Statement stmt;
    
    public WhileStmt(Expr expr, Statement stmt){
        this.expr = expr;
        this.stmt = stmt;
    }
    
    public void genKra(PW pw){
        
        pw.printIdent("while ( ");
        expr.genKra(pw);
        pw.print(" ) ");
        
        if ( stmt != null){
            pw.print("{");
        }else{
            pw.print("\n");
        }
        
        //+1 ident
        pw.add();
        if ( stmt != null){
            stmt.genKra(pw);
        }else{
            pw.print(";\n");
        }
        
        //-1 ident
        pw.sub();
        
        if(stmt != null){
            pw.printIdent("}\n");
        }else{
            pw.print("\n");
        }
    }

    @Override
    public void genC(PW pw) {
        
    }
}
