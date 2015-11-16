
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
import java.util.ArrayList;


public class Dcl{

	private ArrayList<String> id;
	
	public Dcl(ArrayList<String> d){
		this.id = d;
	}
        
        
        public void genC(){
            
            for (String id1 : id) 
                System.out.print(id1);
            
        }


}