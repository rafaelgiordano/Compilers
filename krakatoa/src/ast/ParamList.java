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

import java.util.*;

public class ParamList {

    public ParamList() {
       paramList = new ArrayList<Parameter>();
    }

    public void addElement(Parameter p) {
       paramList.add(p);
    }

    public Iterator<Parameter> elements() {
        return paramList.iterator();
    }

    public int getSize() {
        return paramList.size();
    }

    private ArrayList<Parameter> paramList;

    void genKra(PW pw) {
        for ( Parameter p : paramList){
            //pw.print(p.getType().getName());
            pw.print(p.getType().toString());
            pw.print(" ");
            pw.print(p.getName());
            //se tiver mais de um parametro e este nao for o ultimo, print ", " 
            if(p != paramList.get(getSize() -1)){
                pw.print(", ");
            }
        }
    }

    

}
