package AST;

import java.io.*;
  
public class BooleanType extends Type {
    
   public BooleanType() { super("boolean"); }
   
   public String getCname() {
      return "int";
   }
   
}
