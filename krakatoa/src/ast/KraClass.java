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
/*
 * Krakatoa Class
 */

import java.util.*;

/*
 * KraClass ::= "class" Id [ "extends" Id ] "{" MemberList "}"
 * MemberList ::= { Qualifier Member } 
 * Member ::= InstVarDec | MethodDec
 * InstVarDec ::= Type IdList ";" 
 * MethodDec ::= Qualifier Type Id "("[ FormalParamDec ] ")" "{" StatementList "}" 
 * Qualifier ::= ["Final"]["static"]  ( "private" | "public" )
 */
public class KraClass extends Type {

    private String name;
    private boolean isFinal = false;
    private KraClass superclass;
    private InstanceVariableList instanceVariableList;
    private MethodList publicMethodList, privateMethodList;

    public KraClass(String name) {
        super(name);
        this.superclass = null;
        this.instanceVariableList = new InstanceVariableList();
        this.privateMethodList = new MethodList();
        this.publicMethodList = new MethodList();
    }

    public String getCname() {
        return getName();
    }

    public KraClass getSuperclass() {
        return superclass;
    }

    public void setSuperclass(KraClass superclass) {
        this.superclass = superclass;
    }

    public InstanceVariableList getInstanceVariableList() {
        return instanceVariableList;
    }

    public void setInstanceVariableList(InstanceVariableList instanceVariableList) {
        this.instanceVariableList = instanceVariableList;
    }

    public MethodList getPublicMethodList() {
        return publicMethodList;
    }

    public void setPublicMethodList(MethodList publicMethodList) {
        this.publicMethodList = publicMethodList;
    }

    //adicionar variáveis de instância
    public void addInstanceVariable(InstanceVariable instanceVariable) {
        this.instanceVariableList.addElement(instanceVariable);
    }

    //adicionar métodos pub/priv
    public void addPublicMethod(MethodDec methodDec) {
        this.publicMethodList.addMethodDec(methodDec);
    }

    public void addPrivateMethod(MethodDec methodDec) {
        this.privateMethodList.addMethodDec(methodDec);
    }

    public boolean isIsFinal() {
        return isFinal;
    }

    public void setIsFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }

    public MethodList getPrivateMethodList() {
        return privateMethodList;
    }

    public void setPrivateMethodList(MethodList privateMethodList) {
        this.privateMethodList = privateMethodList;
    }

    //procurar variável
    public InstanceVariable findInstanceVariable(String name) {
        Iterator<InstanceVariable> ivList = this.instanceVariableList.elements();
        InstanceVariable instanceVariable;

        while (ivList.hasNext()) {
            instanceVariable = ivList.next();
            if (name.equals(instanceVariable.getName())) {
                return instanceVariable;
            }
        }
        return null;
    }

    //procurar método
    public MethodDec findMethodDec(String name, boolean inSuper) {
        Iterator<MethodDec> mdList = this.publicMethodList.elements();
        MethodDec methodDec;

        while (mdList.hasNext()) {
            methodDec = mdList.next();
            if (name.equals(methodDec.getMethodName())) {
                return methodDec;
            }
        }

        mdList = this.privateMethodList.elements();
        while (mdList.hasNext()) {
            methodDec = mdList.next();
            if (name.equals(methodDec.getMethodName())) {
                return methodDec;
            }
        }

        //se não existe na classe e tem superclasse, procura na superclasse
        //flag para não procurar sempre, pois podemos querer verificar se não há
        //replicação do método apenas na própria classe
        if (superclass != null && inSuper == true) {
            return superclass.findMethodDec(name, inSuper);
        }

        return null;
    }

    public void genKra(PW pw) {

        if (isFinal) {
            pw.print("final ");
        }

        pw.print("class ");
        pw.print(getCname());
        pw.print(" ");

        if (superclass != null) {
            pw.print("extends ");
            pw.print(superclass.getName());
        }

        pw.print("{\n\n");
        pw.add();

        if (instanceVariableList != null) {
            instanceVariableList.genKra(pw);
            pw.print("\n");
        }
        if (publicMethodList != null) {
            publicMethodList.genKra(pw);
        }
        if (privateMethodList != null) {
            privateMethodList.genKra(pw);
        }
        pw.sub();
        pw.print("}\n\n");
    }
}
