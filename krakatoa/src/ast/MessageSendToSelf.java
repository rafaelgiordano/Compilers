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


public class MessageSendToSelf extends MessageSend {
    
    public Type getType() { 
        return this.type;
    }
    
    public void genC( PW pw, boolean putParenthesis ) {
    }
    
    private Variable vb;
    private Type type;
    private MethodDec methodDec;
    private ExprList exprlist;
    
    public MessageSendToSelf(Variable vb, Type type, MethodDec methodDec, ExprList exprlist){
        this.vb = vb;
        this.type = type;
        this.methodDec = methodDec;
        this.exprlist = exprlist;
    }
    
    
    public void genKra(PW pw){
        pw.printIdent("this.");
        if(vb != null){
            pw.print(vb.getName());
        }
        if(methodDec != null){
            pw.print(methodDec.getMethodName());
            pw.print("( ");
            if(exprlist != null){
                exprlist.genKra(pw);
            }
            pw.print(" )");
        }
    }
}