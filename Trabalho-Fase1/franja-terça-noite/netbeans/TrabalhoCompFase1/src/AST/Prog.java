
package AST;

/*
prog ::= P pid ’;’ body ’.’
body ::= [dclpart] compstmt
dclpart ::= V dcls
dcls ::= dcl {dcl}
dcl ::= idlist ’:’ type ’;’
idlist ::= id {’,’ id}
type ::= stdtype
stdtype ::= I
compstmt ::= B stmts E
stmts ::= stmt {’;’ stmt} ’;’
stmt ::= L ’(’ vblist ’)’
vblist ::= vbl {’,’ vbl}
vbl ::= id
id ::= letter {letter | digit}
pid ::= letter {letter | digit}

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
	
	public void genC() {
            System.out.println("#include <stdio.h>\n");
            System.out.println("int main() {");
            body_.genC();
            System.out.println("return 0;   \n}");
        }


}