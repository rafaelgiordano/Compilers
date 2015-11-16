package AST;

import java.io.PrintWriter;

abstract public class Statement {
    abstract public void genC( PW pw );
}
