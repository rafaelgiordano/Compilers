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

import java.util.ArrayList;

/** This class represents a metaobject call as <code>{@literal @}ce(...)</code> in <br>
 * <code>
 * @ce(5, "'class' expected") <br>
 * clas Program <br>
 *     public void run() { } <br>
 * end <br>
 * </code>
 * 
   @author José
   
 */
public class MetaobjectCall {

	public MetaobjectCall(String name, ArrayList<Object> paramList) {
		this.name = name;
		this.paramList = paramList;
	}
	
	public ArrayList<Object> getParamList() {
		return paramList;
	}
	public String getName() {
		return name;
	}


	private String name;
	private ArrayList<Object> paramList;

        public void genKra(PW pw){
            
        }
}
