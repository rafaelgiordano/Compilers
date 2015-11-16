
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


public class Body{

	private ArrayList<Dcl> dcl_;
	private ArrayList<Stmt> compstmt_;
	
	
	public Body(ArrayList<Dcl> d, ArrayList<Stmt> c){
		this.dcl_ = d;
		this.compstmt_ = c;
	}

        public void genC(){
            
            if(dcl_ != null){
                for (Dcl dcl_1 : dcl_) {
                    System.out.print("int ");
                    dcl_1.genC();
                    System.out.println(" ;");
                }
            }
            for(Stmt stmt_1 : compstmt_){
                System.out.println(" scanf(\"%d\", &");
                stmt_1.genC();
                System.out.println(");");
            }
    
        }

}