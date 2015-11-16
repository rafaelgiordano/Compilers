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


public class WriteStmt extends Statement{
    
    
    private ExprList exprList;
    private boolean haveLn;
    
    public WriteStmt(ExprList exprList, boolean haveLn){
        this.exprList = exprList;
        this.haveLn = haveLn;
    }
    
    public void genKra(PW pw){
        if(haveLn){
            pw.printIdent("writeln ( ");
        }else{
            pw.printIdent("writeln ( ");
        }
        exprList.genKra(pw);
        pw.print(" );\n");
    }

    @Override
    public void genC(PW pw) {
        
    }
}
