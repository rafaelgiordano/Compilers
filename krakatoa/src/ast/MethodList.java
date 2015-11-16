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

public class MethodList {

    private ArrayList<MethodDec> methodList;

    public MethodList() {
        methodList = new ArrayList<MethodDec>();
    }

    public ArrayList<MethodDec> getMethodList() {
        return methodList;
    }

    public void addMethodDec(MethodDec meth) {
        methodList.add(meth);
    }

    public Iterator<MethodDec> elements() {
        return this.methodList.iterator();
    }

    public int getSize() {
        return methodList.size();
    }

    public void genKra(PW pw){
        for( MethodDec m : methodList){
            m.genKra(pw);
        }
    }
}
