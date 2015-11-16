/*
Primeiro Trabalho de Laboratório de Compiladores
 
Segundo Semestre de 2015.
Campus de Sorocaba da UFSCar
Prof. José de Oliveira Guimarães
 
Grupo:
Nome:Lucas Gabriel Mendes de Oliveira  RA: 380091
Nome:Rafael Paschoal Giordano       RA: 408298
 */
package ast;

public class MessageSendToSuper extends MessageSend { 

    public Type getType() { 
        return null;
    }

    public void genC( PW pw, boolean putParenthesis ) {
        
    }
    
    private MethodDec methodDec;
    private ExprList  exprlist;
    
    public MessageSendToSuper(MethodDec methodDec, ExprList exprlist){
        this.methodDec = methodDec;
        this.exprlist = exprlist;
    }
    
    public void genKra(PW pw){
        pw.printIdent("super.");
        pw.print(methodDec.getMethodName());
        pw.print("( ");
        if(exprlist != null){
            exprlist.genKra(pw);
        }
        pw.print(" )");
    }
}