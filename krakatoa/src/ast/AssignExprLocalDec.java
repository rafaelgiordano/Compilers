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


public class AssignExprLocalDec extends Statement{

    private Expr expr1;
    private Expr expr2;

    private LocalVariableList localdec;

    public AssignExprLocalDec(Expr expr1, Expr expr2) {
        this.expr1 = expr1;
        this.expr2 = expr2;
        this.localdec = null;
    }

    public AssignExprLocalDec(LocalVariableList localdec) {
        this.localdec = localdec;
        this.expr1 = null;
        this.expr2 = null;
    }
    public void genKra(PW pw){
        if(localdec == null){
            expr1.genKra(pw);
            pw.print(" = ");
            //if(expr2 != null)
            expr2.genKra(pw);
            pw.print(";\n");
        }else{
            localdec.genKra(pw);
        }
    }

    @Override
    public void genC(PW pw) {
       
    }
}
