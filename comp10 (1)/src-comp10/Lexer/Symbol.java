package Lexer;

public enum Symbol {
      EOF("eof"),
      IDENT("Ident"),
      NUMBER("Number"),
      PLUS("+"),
      MINUS("-"),
      MULT("*"),
      DIV("/"),
      LT("<"),
      LE("<="),
      GT(">"),
      GE(">="),
      NEQ("!="),
      EQ("=="),
      ASSIGN("="),
      LEFTPAR("("),
      RIGHTPAR(")"),
      SEMICOLON(";"),
      VAR("var"),
      BEGIN("begin"),
      END("end"),
      IF("if"),
      THEN("then"),
      ELSE("else"),
      ENDIF("endif"),
      COMMA(","),
      READ("read"),
      WRITE("write"),
      COLON(":"),
      INTEGER("integer"),
      BOOLEAN("boolean"),
      CHAR("char"),
      CHARACTER("character"),
      TRUE("true"),
      FALSE("false"),
      OR   ("||"),
      AND  ("&&"),
      REMAINDER("%"),
      NOT("!"),
      PROCEDURE("procedure"),
      FUNCTION("function"),
        // the following symbols are used only at error treatment
      CURLYLEFTBRACE("{"),
      CURLYRIGHTBRACE("}"),
      LEFTSQBRACKET("["),
      RIGHTSQBRACKET("]"),
        // other symbols
      FOR("for"),
      WHILE("while"),
      TO("to"),
      DO("do"),
      RETURN("return");


      Symbol(String name) {
          this.name = name;
      }

      public String toString() {
          return name;
      }

      private String name;

}