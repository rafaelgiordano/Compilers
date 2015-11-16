package AST;

public class NumberExpr extends Expr {
    
    public NumberExpr( int value ) { 
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    public void genC( PW pw ) {
        pw.out.print(value);
    }
    
    public Type getType() {
        return Type.integerType;
    }
    
    private int value;
}
