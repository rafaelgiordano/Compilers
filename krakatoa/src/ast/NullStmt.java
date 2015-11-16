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


public class NullStmt extends Statement{
    
    @Override
    public void genC(PW pw) {
        
    }

    @Override
    public void genKra(PW pw) {
        //pw.print(";\n");
    }
    
}
