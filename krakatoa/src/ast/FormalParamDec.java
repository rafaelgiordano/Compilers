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


public class FormalParamDec {
    private ParamList paramList;

    public FormalParamDec(ParamList paramList) {
        this.paramList = paramList;
    }

    public ParamList getParamList() {
        return paramList;
    }

    public void setParamList(ParamList paramList) {
        this.paramList = paramList;
    }
    
    //fazer gemKra
    public void genKra(PW pw){
        pw.print(" ");
        paramList.genKra(pw);
        pw.print(" ");
    }
}
