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


public class IfStmt extends Statement{

    private Expr expr;
    private Statement stmt;
    private Statement elsestmt;
    
    public IfStmt(Expr expr, Statement stmt, Statement elsestmt) {
        this.expr = expr;
        this.stmt = stmt;
        this.elsestmt = elsestmt;
    }
    
    public void genKra(PW pw){
        pw.printIdent("if ( ");
        expr.genKra(pw);
        pw.print(" ) {\n");
        //ident+1
        pw.add();
        if(stmt != null){
            stmt.genKra(pw);
        }else{
            pw.print("\n");
        }
        pw.printIdent("}\n");
        //ident -1
        pw.sub();
        if(elsestmt != null){
            pw.printIdent(" else {\n");
            //ident +1 no else
            pw.add();
            elsestmt.genKra(pw);
            pw.printIdent("}\n");
            pw.sub();
        }
    }

    @Override
    public void genC(PW pw) {
      
    }
}
