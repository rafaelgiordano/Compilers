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


public class ReturnStmt extends Statement{

    private Expr expr;

    public ReturnStmt(Expr expr) {
        this.expr = expr;
    }

    public void genKra(PW pw) {
        
        pw.print("return ");
        expr.genKra(pw);
        pw.print(";\n");
    }

    @Override
    public void genC(PW pw) {
        
    }
}
