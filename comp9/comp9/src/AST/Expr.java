package AST;

abstract public class Expr {
    abstract public void genC( PW pw );
      // new method: the type of the expression
    abstract public Type getType();
}