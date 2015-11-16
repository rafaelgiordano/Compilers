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
import java.util.Iterator;


public class StatementList {
    
    private ArrayList<Statement> stmts;
    
    public StatementList(){
        this.stmts = new ArrayList<Statement>();
    }
    
    
    public void addElement(Statement s) {
       stmts.add(s);
    }

    public Iterator<Statement> elements() {
        return stmts.iterator();
    }

    public int getSize() {
        return stmts.size();
    }
    
    public void genKra(PW pw){
        for( Statement s : stmts){
            if(s != null){
                s.genKra(pw);
            }
        }
    }
}
