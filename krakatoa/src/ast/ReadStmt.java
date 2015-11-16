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


public class ReadStmt extends Statement {

    ArrayList<String> leftValueList;

    public ReadStmt() {
        this.leftValueList = new ArrayList<String>();
    }

    public void addElement(String leftValue) {
        leftValueList.add(leftValue);
    }

    public Iterator<String> elements() {
        return leftValueList.iterator();
    }

    public int getSize() {
        return leftValueList.size();
    }

    @Override
    public void genC(PW pw) {
        
    }

    @Override
    public void genKra(PW pw) {
        
        int size = leftValueList.size();
        
        pw.printIdent("read ( ");
        for( String s : leftValueList){
            if(s != null){
                pw.print(s);
            }
            if(--size > 0){
                pw.print(", ");
            }
        }
        //final read statement
        pw.print(" );\n");
    }
    
}
