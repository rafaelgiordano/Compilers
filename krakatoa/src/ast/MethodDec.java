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

//KraClass ::= "class" Id [ "extends" Id ] "{" MemberList "}"
import lexer.Symbol;

// MemberList ::= { Qualifier Member } 
// Member ::= InstVarDec | MethodDec
// MethodDec ::= Type Id "("[ FormalParamDec ] ")" "{" StatementList "}" 
// Qualifier ::= ["Final"]["static"]  ( "private" | "public" )
public class MethodDec {

    private boolean isFinal = false;
    private boolean isStatic = false;
    private boolean isPrivate = false;
    private boolean haveReturn = false;
    private Type tipoRetorno;
    private String methodName;
    private FormalParamDec formalParamDec; //fazer métodos em construtores dessa classe
    private StatementList statementList;

    public MethodDec(boolean isFinal, boolean isStatic, boolean isPrivate, Type tipoRetorno, String methodName) {

        this.isFinal = isFinal;
        this.isStatic = isStatic;
        this.isPrivate = isPrivate;
        this.tipoRetorno = tipoRetorno;
        this.methodName = methodName;
        this.formalParamDec = null;
        this.statementList = null;
    }

    public FormalParamDec getFormalParamDec() {
        return formalParamDec;
    }

    public void setFormalParamDec(FormalParamDec formalParanDec) {
        this.formalParamDec = formalParanDec;
    }

    public StatementList getStatementList() {
        return statementList;
    }

    public void setStatementList(StatementList statementList) {
        this.statementList = statementList;
    }

    public boolean isIsFinal() {
        return isFinal;
    }

    public boolean isIsStatic() {
        return isStatic;
    }

    public boolean isIsPrivate() {
        return isPrivate;
    }

    public Type getTipoRetorno() {
        return tipoRetorno;
    }

    public String getMethodName() {
        return methodName;
    }

    public boolean isHaveReturn() {
        return haveReturn;
    }

    public void setHaveReturn(boolean haveReturn) {
        this.haveReturn = haveReturn;
    }

    public void genKra(PW pw) {
        if (isFinal) {
            pw.print("final ");
        }
        if (isStatic) {
            pw.print("static ");
        }
        if (isPrivate) {
            pw.print("private ");
        } else {
            pw.print("public ");
        }
        pw.print(tipoRetorno.getName());
        pw.print(" ");
        pw.print(methodName);
        pw.print("(");
        //metodo sem parametros
        if (formalParamDec == null) {
            pw.print(") {\n");
            pw.add();
        } else {//com parametros
            formalParamDec.genKra(pw);
            pw.print(") {");
            pw.add();
        }
        statementList.genKra(pw);
        pw.sub();
        pw.printIdent("}\n");
    }
}
