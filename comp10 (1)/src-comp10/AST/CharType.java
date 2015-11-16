package AST;

import java.io.*;

public class CharType extends Type {
    
    public CharType() {
        super("char");
    }
    
   public String getCname() {
      return "char";
   }
   
}