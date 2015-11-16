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

abstract public class Expr {

    abstract public void genC(PW pw, boolean putParenthesis);

    // new method: the type of the expression

    abstract public void genKra(PW pw);

    abstract public Type getType();
}
