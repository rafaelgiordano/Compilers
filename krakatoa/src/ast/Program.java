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

import java.util.*;
import comp.CompilationError;

public class Program {

	public Program(ArrayList<KraClass> classList, ArrayList<MetaobjectCall> metaobjectCallList, 
			       ArrayList<CompilationError> compilationErrorList) {
		this.classList = classList;
		this.metaobjectCallList = metaobjectCallList;
		this.compilationErrorList = compilationErrorList;
	}


	public void genKra(PW pw) {
            for ( KraClass k : classList){
                k.genKra(pw);
            }
            for( MetaobjectCall m : metaobjectCallList){
                m.genKra(pw);
            }
	}

	public void genC(PW pw) {
	}
	
	public ArrayList<KraClass> getClassList() {
		return classList;
	}


	public ArrayList<MetaobjectCall> getMetaobjectCallList() {
		return metaobjectCallList;
	}
	

	public boolean hasCompilationErrors() {
		return compilationErrorList != null && compilationErrorList.size() > 0 ;
	}

	public ArrayList<CompilationError> getCompilationErrorList() {
		return compilationErrorList;
	}

	
	private ArrayList<KraClass> classList;
	private ArrayList<MetaobjectCall> metaobjectCallList;
	
	ArrayList<CompilationError> compilationErrorList;

	
}