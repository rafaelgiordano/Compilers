package AST;

import java.io.*;

abstract public class Type {
    
    public Type( String name ) {
        this.name = name;
    }
    
    public static Type booleanType = new BooleanType();
    public static Type integerType = new IntegerType();
    public static Type charType    = new CharType();
    public static Type undefinedType = new UndefinedType();
    
    public String getName() {
        return name;
    }
    
    abstract public String getCname();
    
    private String name;
}
