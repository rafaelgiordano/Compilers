/*
Marcos Cavalcante Barboza - 408336
Rafael Paschoal Giordano - 408298
*/


import AST.*;

public class Main {
    public static void main( String []args ) {
        char []input = "P exemplo02Correto; V a : I; B L(a); E.".toCharArray();
        
        Compiler compiler = new Compiler();
        
        Prog program  = compiler.compile(input);
        program.genC();
    }
}
        