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

import java.util.ArrayList;


public class VariableList {

    private ArrayList<Variable> vblist;

    public VariableList(ArrayList<Variable> vblist) {
        this.vblist = vblist;
    }

    public void genKra(PW pw) {
        for( Variable v : vblist){
            v.genKra(pw);
        }
    }

}
