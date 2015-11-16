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


public class MessageSendToVariable extends MessageSend { 

    public Type getType() { 
        return vb.getType();
    }
    
    public void genC( PW pw, boolean putParenthesis ) {
        
    }

    private Variable vb;
    private MethodDec methodDec;
    private ExprList exprlist;
    private KraClass kraclass;
    
    public MessageSendToVariable(Variable vb, MethodDec methodDec, ExprList exprlist, KraClass kraclass){
        this.vb = vb;
        this.methodDec = methodDec;
        this.exprlist = exprlist;
        this.kraclass = kraclass;
    }
    
    public void genKra(PW pw){
        pw.printIdent("");
        if(kraclass != null){
            pw.print(kraclass.getName());
            pw.print(".");
        }
        if(vb != null){
            pw.print(vb.getName());
            pw.print(".");
            pw.print(methodDec.getMethodName());
            pw.print("(");
            if(exprlist != null){
                exprlist.genKra(pw);
            }
            pw.print(")");
            
        }
    }
    
}    