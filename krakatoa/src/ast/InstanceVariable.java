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

//KraClass ::= "class" Id [ "extends" Id ] "{" MemberList "}"
// MemberList ::= { Qualifier Member } 
// Member ::= InstVarDec | MethodDec
// InstVarDec ::= Type IdList ";"  
// Qualifier ::= ["Final"]["static"]  ( "private" | "public" )
public class InstanceVariable extends Variable {

    private boolean isPrivate;
    private boolean isStatic;
    private boolean isFinal;

    public InstanceVariable(String name, Type type) {
        super(name, type);
    }

    public InstanceVariable(boolean isFinal, boolean isStatic, boolean isPrivate, String name, Type type) {
        super(name, type);
        this.isPrivate = isPrivate;
        this.isStatic = isStatic;
        this.isFinal = isFinal;
    }
    
    public boolean isFinal(){
        return this.isFinal;
    }
    
    public boolean isStatic(){
        return this.isStatic;
    }
}
