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


public class LocalDec extends Statement{
    
    
    private Type type;
    private VariableList vblist;
    
    public LocalDec(Type type, VariableList vblist){
        this.type = type;
        this.vblist = vblist;
    }
    
    
    public void genKra(PW pw){
        vblist.genKra(pw);
    }

    @Override
    public void genC(PW pw) {
        
    }
}
