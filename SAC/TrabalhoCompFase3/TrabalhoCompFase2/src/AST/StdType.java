/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

/**
 *
 * @author home
 */
public class StdType extends Type{

	private String type;
	
	public StdType(String type) {
		if (type.equals("integer"))
                    this.type = "int";
		else if (type.equals("real")) 
                    this.type = "float";
		else 
                    this.type = type;
		
	}

	@Override
	public String getType() {
            return this.type;
	}


	public boolean isArray() {
            return false;
	}

	@Override
	public void genC() {}
}
