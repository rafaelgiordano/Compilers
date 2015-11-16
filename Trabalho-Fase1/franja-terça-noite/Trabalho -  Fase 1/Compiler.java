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
    //pid ::= letter {letter | digit}
    private Prog program(){
    
    	Char my_pid = null;
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
            nextToken();
    	}else{
    		error("Missing semi-colon");
    	}

    	if(token != '.')
    		error("Missing dot");
    	

    	return new Prog(my_pid, my_body);
    }

    //body ::= [dclpart] compstmt
    //dclpart ::= V dcls
    //compstmt ::= B stmts E
    private Body body(){

        ArrayList<Dcl> my_dcl = new ArrayList<Dcl>();
        CompStmt my_compStmt = null;

        if(token == 'V'){
            nextToken();
            my_dcl = dcl();
            nextToken();
        }

        my_compStmt = compStmt();

        return new Body(my_dcl, my_compStmt);
    }


    //dcls ::= dcl {dcl}
    //dcl ::= idlist ’:’ type ’;’
    //idlist ::= id {’,’ id}
    //id ::= letter {letter | digit}
    private Dcl dcls(){
        ArrayList<Dcl> myListDcl = new ArrayList<Dcl>();

        myListDcl.add(dclExpr());

        while(Character.isLetter(token))
            myListDcl.add(dcl());
        

        return myListDcl;

    }

    //idlist ::= id {’,’ id}
    //id ::= letter {letter | digit}

    private List<String> idlist(){
        List<String> sArray = new List<String>();        
        
        sArray.add(id());

        while(token == ','){
            nextToken();
            sArray.add(id());
        }

        return sArray;
    }

    //id ::= letter {letter | digit}
    //pid ::= letter {letter | digit}
    private String id(){

        String str = null;

        if(Character.isLetter(token)){
            str += Character.toString(token);
            nextToken();
        }else{
            error("letter was expected");
        }

        while(Character.isLetter(token) || Character.isDigit(token)){
            str += Character.toString(token);
            nextToken();
        }

        return str;
    }



    //dcl ::= idlist ’:’ type ’;’
    //idlist ::= id {’,’ id}
    //id ::= letter {letter | digit}
    //type ::= stdtype
    //stdtype ::= I
    private Dcl dcl(){

        List<String> arrCh = new List<String>();
        
        arrCh = idlist();


        if (token == ':')
            nextToken();
        else
            error("Missing colon");


        if(token == 'I'){
            nextToken();

        }else
            error("Missing type value");


        if(token == ';')
            nextToken();
        else
            error("Missing semi-colon");

        return new Dcl(arrCh);

    }


    /*
    compstmt ::= B stmts E
    stmts ::= stmt {’;’ stmt} ’;’
    stmt ::= L ’(’ vblist ’)’
    vblist ::= vbl {’,’ vbl}
    vbl ::= id
    */

    private Stmt compstmt(){

        ArrayList<Stmt> s_ = new ArrayList<Stmt>();

        if (token == 'B') {
            nextToken();
            s_ = stmts();
            nextToken();
            if (token != 'E') 
                error("Missing E declaration");
            
        }else{
            error("Missing B declaration");
        }

        return s_;

    }


    //stmts ::= stmt {’;’ stmt} ’;’
    private ArrayList<Stmt> stmts(){
      ArrayList<Stmt> arrStmt = new ArrayList<Stmt>();

      do{
            arrStmt.add(Stmt());
            nextToken();
            if (token == ';') 
                nextToken();
            

        }while(token == 'L');     

        return arrStmt;

    }



    //stmt ::= L ’(’ vblist ’)’
    private Stmt stmt(){
        Stmt st_ = null;
        if(token == 'L'){
            nextToken();
            if (token == '('){
                vblist();
                nextToken();

                if(token == ')')
                    error("Missing closing brackets");

            }else{
                error("Missing opening brackets");
            }
                
            
        }
    }

    //vblist ::= vbl {’,’ vbl}
    //vbl ::= id

    private List<String> vblist(){

        List<String> myListArr = new List<String>();

        myListArr.add(id());

        while(token == ','){
            nextToken();
            myListArr(id());
        }

        return myListArr;

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


}