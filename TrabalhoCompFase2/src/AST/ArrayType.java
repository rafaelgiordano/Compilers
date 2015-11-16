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
public class ArrayType extends Type{
    

	private int inic;
	private int fim;
	private StdType type;
	
	public ArrayType(StdType s, int i, int f) {
		this.inic = i;
		this.fim = f;
		this.type = s;
	}
	
	@Override
	public String getType() {
		return type.getType();
	}
	
	public int getBeginNum() {
		return this.inic;
	}
	
	public int getEndNum() {
		return this.fim;
	}


	public boolean isArray() {
		return true;
	}
	
	@Override
	public void genC() {
		System.out.print("[" + (getEndNum() - getBeginNum()) + "]");
	}
}
