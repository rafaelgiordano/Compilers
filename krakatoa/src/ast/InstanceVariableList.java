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

public class InstanceVariableList {
    
    private ArrayList<InstanceVariable> instanceVariableList;
    
    public InstanceVariableList() {
       instanceVariableList = new ArrayList<InstanceVariable>();
    }

    public void addElement(InstanceVariable instanceVariable) {
       instanceVariableList.add( instanceVariable );
    }

    public Iterator<InstanceVariable> elements() {
    	return this.instanceVariableList.iterator();
    }
    
    public boolean isFinal(){
        return instanceVariableList.get(0).isFinal();
    }
    public int getSize() {
        return instanceVariableList.size();
    }
    
    public boolean isStatic(){
        return instanceVariableList.get(0).isStatic();
    }
    public void genKra(PW pw){
        pw.printIdent("");
        /*
        if(isFinal()){
            pw.print("final ");
        }
        if(isStatic()){
            pw.print("static ");
        }
        */
        //como eh variavel de instancia tem que ser declarada como private
        //if(instanceVariableList != null)
        //pw.print("private ");
        for ( InstanceVariable iv : instanceVariableList){
            if(iv != null){
                pw.print(" ");
                pw.print(iv.getType().getName());
                pw.print(" ");
                pw.print(iv.getName());
                pw.print(";\n");
            }
        }
    }

}
