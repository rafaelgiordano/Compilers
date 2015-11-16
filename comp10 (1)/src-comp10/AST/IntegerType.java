package AST;

import java.io.PrintWriter;

public class IntegerType extends Type {
    
    public IntegerType() {
        super("integer");
    }
    
   public String getCname() {
      return "int";
   }

}