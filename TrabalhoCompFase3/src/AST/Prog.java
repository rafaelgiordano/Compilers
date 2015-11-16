/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

/*

Marcos Cavalcante Barboza - 408336
Rafael Paschoal Giordano - 408298


*/
public class Prog{

	private String pid_;
	private Body body_;
	
	public Prog(String p, Body b){
		
		this.pid_ = p;
		this.body_ = b;
	}
	
	public void genC(PW pw) {
            pw.println("#include <stdio.h>\n");
            pw.println("int main() {");
            body_.genC(pw);
            pw.println("return 0;   \n}");
        }


}