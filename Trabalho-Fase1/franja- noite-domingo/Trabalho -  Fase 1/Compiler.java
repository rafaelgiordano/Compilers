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
*/
import AST.*;
import java.util.ArrayList;

public class Compiler {

	public Prog compile( char []p_input ) {
        input = p_input;
        tokenPos = 0;
        nextToken();
        
        Prog e = program();

        if (tokenPos != input.length)
          error();
          
        return e;
    }

    //prog ::= P pid ’;’ body ’.’
    private Prog program(){
    
    	Pid my_pid = null;
		Body my_body = null;

    	if(token == 'P'){
    		nextToken();
    		my_pid = pid();
    	}
    	else{
    		error("Missing Variable Declaration");
    	}

    	if(token == ';'){
    		nextToken();
    		my_body = body();
    	}else{
    		error("Missing semi-colon");
    	}

    	if(token != '.')
    		error("Missing dot");
    	

    	return new Prog(my_pid, my_body);
    }

    //body ::= [dclpart] compstmt
    private Body body(){

        DCL my_dcl = null;
        CompStmt my_compStmt = null;

        if(token == 'V'){
            nextToken();
            my_dcl = dcl();
        }

        my_compStmt = compStmt();

        return new Body(my_dcl,my_compStmt);
    }


    //dcls ::= dcl {dcl}
    //idlist ::= id {’,’ id}
    //id ::= letter {letter | digit}
    private Dcl dcl(){
        Dcl my_dcl = null;
        ArrayList<Dcl> myListDcl = null;

        my_dcl = dclExpr();
        myListDcl.add(my_dcl);

        while(Character.isLetter(token)){
            nextToken();
            my_dcl = dclExpr();
            myListDcl.add(my_dcl);
        }


    }

    //dcl ::= idlist ’:’ type ’;’
    //type ::= stdtype
    //stdtype ::= I
    private Dcl dclExpr(){


        if (token == ':')
            nextToken();
        else
            error("Missing colon");


        if(token == 'I')
            nextToken();
        else
            error("Missing type value");

        if(token == ';')
            nextToken();
        else
            error("Missing semi-colon");

    }


    /*
    compstmt ::= B stmts E
    stmts ::= stmt {’;’ stmt} ’;’
    stmt ::= L ’(’ vblist ’)’
    vblist ::= vbl {’,’ vbl}
    vbl ::= id
    */

    private Stmt compstmt(){




    }






    private void nextToken() {

        //Checagem dos comentarios
        if(input[tokenPos] == '{'){
            
            tokenPos++;
            
            while(input[tokenPos] != '}')
                tokenPos++;

        }


    	while (tokenPos < input.length && input[tokenPos] == ' ')  
            tokenPos++;
		
		
		if (tokenPos < input.length) {
			token = input[tokenPos];
			tokenPos++;
		}
    }

    private void error(String str) {
        if ( tokenPos == 0 ) 
          tokenPos = 1; 
        else 
          if ( tokenPos >= input.length )
            tokenPos = input.length;
        
        String strInput = new String( input, tokenPos - 1, input.length - tokenPos + 1 );
        String strError = "Error at \"" + strInput + "\"";
        System.out.println( strError );
        throw new RuntimeException(strError);
    }
    
    private char token;
    private int  tokenPos;
    private char []input;
    private ArrayList<Id> my_id;



}