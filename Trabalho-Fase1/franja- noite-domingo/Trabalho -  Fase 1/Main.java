import AST.*;

public class Main {
    public static void main( String []args ) {
        char []input = "a = 1  b = 3 : (- (+ a 2) b)".toCharArray();
        
        Compiler compiler = new Compiler();
        
        Program program  = compiler.compile(input);
        program.genC();
    }
}
        