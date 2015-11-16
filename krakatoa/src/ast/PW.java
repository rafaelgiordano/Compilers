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

import java.io.*;


public class PW {
   
   public void add() {
      currentIndent += step;
   }
   public void sub() {
      currentIndent -= step;
   }
   
   public void set( PrintWriter out ) {
      this.out = out;
      currentIndent = 0;
   }
   
   public void set( int indent ) {
      currentIndent = indent;
   }
   
   public void printIdent( String s ) {
      out.print( space.substring(0, currentIndent) );
      out.print(s);
   }
   
   public void printlnIdent( String s ) {
      out.print( space.substring(0, currentIndent) );
      out.println(s);
   }

   public void print( String s ) {
      out.print(s);
   }
   
   public void println( String s ) {
      out.println(s);
   }
   
   int currentIndent = 0;
   public int step = 3;
   private PrintWriter out;
         
   
   static final private String space = "                                                                                                        ";

}
      
       
