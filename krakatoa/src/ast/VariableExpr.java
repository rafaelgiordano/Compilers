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

public class VariableExpr extends Expr {
    
    public VariableExpr( Variable v ) {
        this.v = v;
    }
    
    public void genC( PW pw, boolean putParenthesis ) {
        pw.print( v.getName() );
    }
    
    public Type getType() {
        return v.getType();
    }
    
    private Variable v;

    @Override
    public void genKra(PW pw) {
        pw.print(v.getName());
    }
}