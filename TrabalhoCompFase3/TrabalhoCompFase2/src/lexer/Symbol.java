/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexer;

public enum Symbol {
      PROGRAM("program"),
      EOF("eof"),
      VAR("var"),
      NUMBER("Number"),
      IDENT("ident"),
      OF("of"),
      ASSIGNSTRING("assignstring"),
      
      // some operators
      PLUS("+"),
      MINUS("-"),
      MULT("*"),
      DIVIDER("/"),
      OR("or"),
      AND("and"),
      MOD("mod"),
      DIV("div"),
      NOT("not"),
      
      // some comparisons
      LT("<"),
      LE("<="),
      GT(">"),
      GE(">="),
      DIF("<>"),
      ASSIGN(":="),
      EQUAL("="),

      // some statements
      BEGIN("begin"),
      END("end"),
      IF("if"),
      THEN("then"),
      ELSE("else"),
      ENDIF("endif"),
      WHILE("while"),
      DO("do"),
      ENDWHILE("endwhile"),
      PROCEDURE("procedure"),
      FUNCTION("function"),
      RETURN("return"),
      
      // some symbols
      DOT("."),
      DOUBLEDOT(".."),
      COMMA(","),
      COLON(":"),
      SEMICOLON(";"),
      LEFTPAR("("),
      RIGHTPAR(")"),
      CURLYLEFTBRACE("{"),
      CURLYRIGHTBRACE("}"),
      LEFTSQBRACKET("["),
      RIGHTSQBRACKET("]"),
      APOSTROPHE("'"),
      
      // some commands
      READ("read"),
      WRITE("write"),
      WRITELN("writeln"),
      
      // variable types
      INTEGER("integer"),
      REAL("real"),
      CHAR("char"),
      ARRAY("array"),
      STRING("string");

      Symbol(String name) {
          this.name = name;
      }

      public String toString() {
          return name;
      }

      private String name;
}