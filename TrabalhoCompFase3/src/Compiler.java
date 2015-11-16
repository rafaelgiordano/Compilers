/*

Marcos Cavalcante Barboza - 408336
Rafael Paschoal Giordano - 408298

*/

/*
prog ::= PROGRAM pid ’;’ body ’.’
body ::= [dclpart] compstmt
dclpart ::= VAR dcls [subdcls] | subdcls
dcls ::= dcl {dcl}
dcl ::= idlist ’:’ type ’;’
idlist ::= id {’,’ id}
type ::= stdtype | arraytype
stdtype ::= INTEGER | REAL | CHAR | STRING
arraytype ::= ARRAY ’[’ intnum ’..’ intnum ’]’ OF stdtype
subdcls ::= subdcl {subdcl}
subdcl ::= subhead ’;’ body ’;’
subhead ::= FUNCTION pid args ’:’ stdtype | PROCEDURE pid args
args ::= ’(’ [dcls] ’)’
compstmt ::= BEGIN stmts END
stmts ::= stmt {’;’ stmt} ’;’
stmt ::= ifstmt
| whilestmt
| assignstmt
| compstmt
| readstmt
| writestmt
| writelnstmt
| returnstmt
| procfuncstmt
ifstmt ::= IF expr THEN stmts [ELSE stmts] ENDIF
whilestmt ::= WHILE expr DO stmts ENDWHILE
assignstmt ::= vbl ’:=’ expr
readstmt ::= READ ’(’ vblist ’)’
writestmt ::= WRITE ’(’ exprlist ’)’
writelnstmt ::= WRITELN ’(’ [exprlist] ’)’
returnstmt ::= RETURN [expr]
procfuncstmt ::= pid ’(’ [exprlist] ’)’
vblist ::= vbl {’,’ vbl}
vbl ::= id [’[’ expr ’]’]
exprlist ::= expr {’,’ expr}
expr ::= simexp [relop expr]
simexp ::= [unary] term {addop term}
term ::= factor {mulop factor}
factor ::= vbl
| num
| ’(’ expr ’)’
| ’"’.’"’
| procfuncstmt
id ::= letter {letter | digit}
pid ::= letter {letter | digit}
num ::= intnum [’.’ intnum]
intnum ::= digit {digit}
relop ::= ’=’ | ’<’ | ’>’ | ’<=’ | ’>=’ | ’<>’
addop ::= ’+’ | ’-’ | OR
mulop ::= ’*’ | ’/’ | AND | MOD | DIV
unary ::= ’+’ | ’-’ | NOT

*/
import AST.*;
import java.util.ArrayList;
import lexer.*;
import AuxComp.*;
import java.util.*;


public class Compiler {
    
    
    private Lexer lexer;
    
    private boolean isFunction;
    private boolean isGlobal;
    private SymbolTable symbolTable;
    
    
    public Prog compile(char[] p_input) {
		// No construtor da classe Lexer ocorre a chamada da função
		// que converte toda a entrada para letras minúsculas
		this.lexer = new Lexer(p_input);
		this.symbolTable = new SymbolTable();
                Prog e = null;
		lexer.nextToken();
                try{
                    e = program();
                }catch(Exception ef){
                    ef.printStackTrace();
                }
		if (lexer.getTokenPos() > p_input.length) {
                    lexer.error("Final do arquivo não definido corretamente.\n");
		}

		return e;
	}
    
    /*
    prog ::= PROGRAM pid ’;’ body ’.’
    body ::= [dclpart] compstmt
    */
    private Prog program(){
        String my_pid;
        Body my_body;
        
        isGlobal = true;
        
        if (lexer.token != Symbol.PROGRAM) 
            lexer.error("'program' esperado no início do arquivo.\n");
        

	lexer.nextToken();
	my_pid = id();
        
        if (lexer.token != Symbol.SEMICOLON)
            lexer.error("';' esperado.\n");
        

        lexer.nextToken();
        my_body = body();
        
        if (lexer.token != Symbol.DOT) 
            lexer.error("'.' esperado.\n");
        

        lexer.nextToken();

        if (lexer.token != Symbol.EOF) 
            lexer.error("Final do arquivo esperado.\n");
        

        return new Prog(my_pid, my_body);
    }
    
    /*
    body ::= [dclpart] compstmt
    dclpart ::= VAR dcls
    dcls ::= dcl {dcl}
    dcl ::= idlist ’:’ type ’;’
    compstmt ::= BEGIN stmts END
    stmts ::= stmt {’;’ stmt} ’;’
    */
    private Body body(){
        DclPart my_dcl = null;
        CmpStmt my_compStmt;

        if( (lexer.token == Symbol.VAR) 
           || (lexer.token == Symbol.FUNCTION)
	   || (lexer.token == Symbol.PROCEDURE) )
           my_dcl = dclspart();
        
        my_compStmt = cmpstmt();
        return new Body(my_dcl, my_compStmt);
    }
    
    /*
    *dclpart ::= VAR dcls
    *dcls ::= dcl {dcl}
    
    dclpart ::= VAR dcls [subdcls] | subdcls
    subdcls ::= subdcl {subdcl}
    subdcl ::= subhead ’;’ body ’;’
    subhead ::= FUNCTION pid args ’:’ stdtype | PROCEDURE pid args
    args ::= ’(’ [dcls] ’)’
    */
    private DclPart dclspart(){
        DclList dcls = null;
        SubDclList subDec = null;
       
        
        if ( lexer.token == Symbol.VAR){
            lexer.nextToken();
            dcls = dcls();
            isGlobal = false;
            
            //dclpart ::= VAR dcls [subdcls]
            if ((lexer.token == Symbol.FUNCTION) || (lexer.token == Symbol.PROCEDURE)){
                isGlobal = false;
                subDec = subdcls();
            }
        }
        else{
        //dclpart ::= subdcls
            isGlobal = false;
            subDec = subdcls();
        }
        
        return new DclPart(dcls);
    }
    
    
    /*
        *subdcls ::= subdcl {subdcl}
        *subdcl ::= subhead ’;’ body ’;’
        *subhead ::= FUNCTION pid args ’:’ stdtype | PROCEDURE pid args
        *args ::= ’(’ [dcls] ’)’
    */
    private SubDclList subdcls (){
        ArrayList<SubDcl> subdclArray = new ArrayList<SubDcl>();
        
        subdclArray.add(subdcl());
        while((lexer.token == Symbol.FUNCTION) || (lexer.token == Symbol.PROCEDURE))
            subdclArray.add(subdcl());
        
        return new SubDclList(subdclArray);
    }
    
    // subdcl ::= subhead ';' body ';'
    private SubDcl subdcl(){
        Subhead mySubHead = null;
        Body myBody = null;
        
        mySubHead = subhead();
        
        if(lexer.token == Symbol.SEMICOLON){
         
            lexer.nextToken();    
            myBody = body();
            
            if(lexer.token == Symbol.SEMICOLON)
                lexer.nextToken();
            else
                lexer.error("; exepcted");
        
        }else
            lexer.error("; esperado");
        
        
        
        return new SubDcl(mySubHead, myBody);
    }
    
    /*
        subhead ::= FUNCTION pid args ’:’ stdtype | PROCEDURE pid args
        args ::= ’(’ [dcls] ’)’
    */
    private Subhead subhead(){
        String myRoutine = null;
        Args myArgs = null;
        StdType myStdType = null;
        
        if(lexer.token == Symbol.FUNCTION){
            isFunction = true;
            lexer.nextToken();
            
            myRoutine = id();
            myArgs = args();
            
            if(lexer.token == Symbol.COLON){
                lexer.nextToken();
                myStdType = stdtype();
            }else
                lexer.error("Colon was expected");
            
           
            if (symbolTable.putInGlobal(myRoutine, new Function(myRoutine, myArgs, myStdType)) != null)
                    lexer.error(myRoutine + " has been already declared \n");
            

            return new Function(myRoutine, myArgs, myStdType);
            
        }else if(lexer.token == Symbol.PROCEDURE){
            isFunction = false;
            lexer.nextToken();
            myRoutine = id();
            myArgs = args();
            
            if (symbolTable.putInGlobal(myRoutine, new Procedure(myRoutine, myArgs, myStdType)) != null)
                    lexer.error(myRoutine + " has been already declared \n");
            
            return new Procedure(myRoutine, myArgs, myStdType);
        }else{
            lexer.error("Function or Procedure were expected");
        }
    
        return null;
    }
    
    //  args ::= ’(’ [dcls] ’)’
    private Args args(){
        DclList dclArray = null;
        
        if(lexer.token == Symbol.LEFTPAR){
            
            lexer.nextToken();
            
            if(lexer.token == Symbol.IDENT)
                dclArray = dcls();
         
            
            if(lexer.token == Symbol.RIGHTPAR)
                lexer.nextToken();
            else
                lexer.error(" ) was expected ");
            
        }else
            lexer.error(" ( was expected ");
        
        
        return new Args(dclArray);
    }
    
    
    
    /*
    dcls ::= dcl {dcl}
    dcl ::= idlist ’:’ type ’;’
    */
    private DclList dcls(){
        ArrayList<Dcl> dclList = new ArrayList<>();
        
        dclList.add(dcl());
        
        while (lexer.token == Symbol.IDENT) 
            dclList.add(dcl());
		
        return new DclList(dclList);
    }
    
    
    /*
    dcl ::= idlist ’:’ type ’;’
    idlist ::= id {’,’ id}
    type ::= stdtype | arraytype
    */

    private Dcl dcl(){
        
        ArrayList<String> idlist;
        Type type = null;
        
        idlist = idList();
        
        
        if(lexer.token == Symbol.COLON){
            lexer.nextToken();
            type = type();
            
            for (String v : idlist){
                
              if(isGlobal){
                if( symbolTable.putInGlobal(v,type) != null)
                  lexer.error(v + "has already been declared ");
                    
              }else{
               if (symbolTable.putInLocal(v, type) != null) {
                    lexer.error(v + "has already been declared ");
                }
              }
            }
                  
            if (lexer.token == Symbol.SEMICOLON)
                lexer.nextToken();
            else 
                lexer.error("SEMI-COLON expected.\n");
            
        }else{
            lexer.error("COLON expected \n"); 
        }
        
        return new Dcl ( idlist , type);
    }
    
    
    /*
    idlist ::= id {’,’ id}
    id ::= letter {letter | digit}
    */
    private ArrayList<String> idList(){
        ArrayList<String> idlist = new ArrayList<>();
        
        idlist.add(id());
        
        while (lexer.token == Symbol.COMMA) {
            lexer.nextToken();
            idlist.add(id());
        }
        
        return idlist;
    }
    
    /*
    type ::= stdtype | arraytype
    stdtype ::= INTEGER | REAL | CHAR | STRING
    arraytype ::= ARRAY ’[’ intnum ’..’ intnum ’]’ OF stdtype
    */
    private Type type(){
        
        StdType stdtype = null;
        ArrayType arraytype = null;
        
        if (( lexer.token == Symbol.INTEGER) || ( lexer.token == Symbol.REAL) || 
            ( lexer.token == Symbol.CHAR) || ( lexer.token == Symbol.STRING))
            stdtype = stdtype();
        else
            if(lexer.token == Symbol.ARRAY)
                arraytype = arrayType();
            else
                lexer.error("tipo nao identificado \n");
    
        if(stdtype == null)
            return arraytype;
        
        return stdtype;
    }
    
    /*
    arraytype ::= ARRAY ’[’ intnum ’..’ intnum ’]’ OF stdtype
    */
    private ArrayType arrayType(){
        int inic = 0;
        int fim = 0;
        StdType type = null;
        
        if(lexer.token == Symbol.ARRAY){
            lexer.nextToken();
            if( lexer.token == Symbol.LEFTSQBRACKET){
                lexer.nextToken();
                inic = Integer.parseInt(num());
                if(lexer.token == Symbol.DOUBLEDOT){
                    lexer.nextToken();
                    fim = Integer.parseInt(num());
                    if(lexer.token == Symbol.RIGHTSQBRACKET){
                        lexer.nextToken();
                        if(lexer.token == Symbol.OF){
                            lexer.nextToken();
                            type = stdtype();
                        }else{
                            lexer.error("Missing \n");
                        }
                    }else{
                        lexer.error("Missing RIGHTBRACKET\n");
                    }
                          
                }else{
                    lexer.error("Missing DOUBLEDOT\n");
                }
            }else{
                lexer.error("Missing LEFTBRACKET");
            }
        }else{
            lexer.error("Missing ARRAY");
        }
        return new ArrayType(type, inic, fim);    
    }
    
    /*
    stdtype ::= INTEGER | REAL | CHAR | STRING
    */
    private StdType stdtype() {
        String val;

        if ((lexer.token == Symbol.INTEGER) || (lexer.token == Symbol.REAL) || 
            (lexer.token == Symbol.CHAR) || (lexer.token == Symbol.STRING)) {
            val = lexer.getStringValue();
            lexer.nextToken();
            return new StdType(val);
        }else{
            lexer.error("Missing Type\n");
            return null;
        }
    }
    
    /*
    compstmt ::= BEGIN stmts END
    stmts ::= stmt {’;’ stmt} ’;’
    */
    private CmpStmt cmpstmt(){
        StmtList stmt = null;
        
        if(lexer.token == Symbol.BEGIN){
            lexer.nextToken();
            stmt = stmts();
            if(lexer.token == Symbol.END)
                lexer.nextToken();
            else
                lexer.error("Missing END\n");
        }else{
            lexer.error("missing BEGIN\n");
        }
        symbolTable.removeLocalIdent();
        return new CmpStmt(stmt);
    }
    
    //stmts ::= stmt {’;’ stmt} ’;’
    private StmtList stmts(){
        ArrayList<Stmt> stmtlist = new ArrayList<>();
        
       stmtlist.add(stmt());

         do{
            stmtlist.add(stmt());
            if(lexer.token == Symbol.SEMICOLON)
                lexer.nextToken();
            else
                lexer.error("Missing Semi-Colon\n");
            
        }while(lexer.token == Symbol.IF || lexer.token == Symbol.WRITE || lexer.token == Symbol.WRITELN ||
               lexer.token == Symbol.IDENT || lexer.token == Symbol.WHILE || lexer.token == Symbol.READ || lexer.token == Symbol.BEGIN);
            

            
        return new StmtList(stmtlist);
    }
    
    /*
    stmt ::= ifstmt | whilestmt | assignstmt | compstmt | readstmt | writestmt | writelnstmt | returnstmt | procfuncstmt

    */
    private Stmt stmt(){
        
        switch(lexer.token){
            case IDENT:
              
                if (symbolTable.get(lexer.getStringValue()) instanceof Procedure) 
                    return procedureCall();
                
                //can´t be a function
                else if (symbolTable.get(lexer.getStringValue()) instanceof Function)
                    lexer.error("Function was expected \n");
                
                //assignstmt
                else 
                    return assignstmt();
                
            case BEGIN:
                return cmpstmt();
            case IF:
                return ifstmt();
            case WHILE:
                return whilestmt();
            case WRITE:
                return writestmt();
            case WRITELN:
                return writelnstmt();
            case READ:
                return readstmt();
            case RETURN:
                return returnStmt();
            default:
                lexer.error("missing statment\n");
        }
        
        return null;
    }
    
    //procfuncstmt ::= pid ’(’ [exprlist] ’)’
    private ProcedureCall procedureCall() {
        String routineProc = null;
        ExprList exprArray = null;
        Procedure myProc;
        
            routineProc = id();
            
            if ((myProc = (Procedure) symbolTable.getInGlobal( routineProc )) == null)
                lexer.error( routineProc + " was not declared \n");
            

            if (lexer.token == Symbol.LEFTPAR) {
                    lexer.nextToken();

                    if ((lexer.token == Symbol.IDENT) 
                        || (lexer.token == Symbol.NUMBER)
                        || (lexer.token == Symbol.LEFTPAR)
                        || (lexer.token == Symbol.PLUS)
                        || (lexer.token == Symbol.MINUS)
                        || (lexer.token == Symbol.NOT)) {
                            exprArray = exprlist();}
                    

                    if (lexer.token == Symbol.RIGHTPAR){
                        lexer.nextToken();}
                    
                    else    lexer.error(" ) was expected \n");
                    
            } else 
                lexer.error(" ( was expected  \n");
            

            return new ProcedureCall(myProc, exprArray);
    }
    
    //returnstmt ::= RETURN [expr]
    private ReturnStmt returnStmt() {
        Expr myExpr = null;
      
        lexer.nextToken();
        
        if (isFunction)     myExpr = expr();
       
        return new ReturnStmt(myExpr);    
    }
    
    
    
    
    
    /*
    assignstmt ::= vbl ’:=’ expr
    */
    private AssignStmt assignstmt(){
        Variable vb;
        Expr expr = null;
        
        vb = vb();
        
        if(lexer.token == Symbol.ASSIGN){
            lexer.nextToken();
            expr = expr();
        }else{
            lexer.error("missing ASSIGN SYMBOl :=");
        }
        
        return new AssignStmt(vb, expr);
    }
    
    /*
    ifstmt ::= IF expr THEN stmts [ELSE stmts] ENDIF
    */
    private IfStmt ifstmt(){
        Expr expr=null;
        Stmt thenstmt=null;
        Stmt elsestmt= null;
        
        if(lexer.token == Symbol.IF){
            lexer.nextToken();
            expr = expr();

            if(lexer.token == Symbol.THEN){
                lexer.nextToken();
                thenstmt = stmt();
                
                if(lexer.token == Symbol.ELSE){
                    lexer.nextToken();
                    elsestmt = stmt();
                   
                }

                
                if(lexer.token == Symbol.ENDIF){
                    lexer.nextToken();
                }else{
                    lexer.error("Missing ENDIF");
                }
            }else{
                lexer.error("Missing THEN");
            }
        }else{
             lexer.error("Missing IF");
        }
        
        return new IfStmt(expr, thenstmt, elsestmt);
    }
    
    
    /*
    whilestmt ::= WHILE expr DO stmts ENDWHILE
    */
    private WhileStmt whilestmt(){
        
        Expr expr=null;
        Stmt stmt=null;
        
        if(lexer.token == Symbol.WHILE){
            lexer.nextToken();
            expr = expr();
            if(lexer.token == Symbol.DO){
                lexer.nextToken();
                stmt = stmt();
                if(lexer.token == Symbol.ENDWHILE){
                    lexer.nextToken();
                }else{
                    lexer.error("Missing ENDWHILE\n");
                }
            }else{
                lexer.error("Missing DO\n");
            }
        }else{
            lexer.error("Missing WHILE");
        }
        
        return new WhileStmt(expr , stmt);
    }
    
    
    /*
    writestmt ::= WRITE ’(’ exprlist ’)’
    */
    private WriteStmt writestmt(){
        ExprList exprlist = null;
        
        if(lexer.token == Symbol.WRITE){
            lexer.nextToken();
            if (lexer.token == Symbol.LEFTPAR) {
                lexer.nextToken();
                exprlist = exprlist();

                if (lexer.token == Symbol.RIGHTPAR) 
                    lexer.nextToken();
                else
                    lexer.error("Missing RIGHTPAR\n");
                
            }else{
                lexer.error("Missing LEFTPAR\n");
            }
        }else{
            lexer.error("Missing WRITE STMT\n");
        }
        return new WriteStmt (exprlist, false);
    
    }
    
    
    /*
    writelnstmt ::= WRITELN ’(’ [exprlist] ’)’
    */
    private WriteStmt writelnstmt(){
        ExprList exprlist = null;
        
        if(lexer.token == Symbol.WRITELN){
            lexer.nextToken();
            
            if(lexer.token == Symbol.LEFTPAR){
               // System.out.println("Valor do NEXTOKEN:" + lexer.token);
                lexer.nextToken();
                //System.out.println("Valor23 do NEXTOKEN23:" + lexer.token);

                if((lexer.token == Symbol.IDENT)||
                        (lexer.token == Symbol.LEFTPAR)||
                        (lexer.token == Symbol.NUMBER) ||
                        (lexer.token == Symbol.PLUS) ||
                        (lexer.token == Symbol.MINUS) ||
                        (lexer.token == Symbol.NOT)){
                    lexer.nextToken();
                    exprlist = exprlist();
                }
                if(lexer.token == Symbol.RIGHTPAR){
                    lexer.nextToken();
                }else{
                    lexer.error("Missing RIGHTPAR\n");
                }
            }else{
                lexer.error("Missing LEFTPAR\n");
            }
        }else{
            lexer.error("Missing WRITELN\n");
        }
        
        return new WriteStmt (exprlist, true);
    }
    
    /*
    readstmt ::= READ ’(’ vblist ’)’
    */
    private ReadStmt readstmt(){
        VariableList vblist = null;
        
        if(lexer.token == Symbol.READ){
            lexer.nextToken();
            if(lexer.token == Symbol.LEFTPAR){
                lexer.nextToken();
                vblist = vblist();
                
                if(lexer.token == Symbol.RIGHTPAR)
                    lexer.nextToken();
                else
                    lexer.error("Missing RIGHTPAR\n");
                
            }else{
                lexer.error("Missing LEFTPAR\n");
            }
        }else{
            lexer.error("Missing READ STMT\n");
        }
        
        
        return new ReadStmt( vblist);
    }
    
    /*
    vbl ::= id [’[’ expr ’]’]
    */
    private Variable vb(){
        String id = null;
        Expr expr = null;
        Variable var = null;
        Type myType = null;
        
        id = id();
        
        if(lexer.token == Symbol.LEFTSQBRACKET){
            lexer.nextToken();
            expr = expr();
            
            if(lexer.token == Symbol.RIGHTSQBRACKET)
                lexer.nextToken();
            else
                lexer.error("Missing RIGHTSBRACKET \n");
            
        }

        if((myType = (Type) symbolTable.getInLocal(id)) == null) 
           if ((myType = (Type) symbolTable.getInGlobal(id)) == null) 
                 lexer.error("Variável " + id + " não foi declarada.\n"); 
               
                    
        return new Variable (id, expr, myType);
    }
    
    /*
    vblist ::= vbl {’,’ vbl}
    */
    private VariableList vblist(){    
        ArrayList<Variable> vblist = new ArrayList<>();
        
        vblist.add(vb());
        
        while(lexer.token == Symbol.COMMA){
            lexer.nextToken();
            vblist.add(vb());
        }
        return new VariableList(vblist);   
    }
    
    
    /*
    exprlist ::= expr {’,’ expr}
    expr ::= simexp [relop expr]
    */
    private ExprList exprlist(){
        ArrayList<Expr> exprlist = new ArrayList<>();
        
        exprlist.add(expr());
        
        while(lexer.token == Symbol.COMMA){
            lexer.nextToken();
            exprlist.add(expr());
        }
        
        return new ExprList(exprlist);
    }
    
    
    /*
    expr ::= simexp [relop expr]
    simexp ::= [unary] term {addop term}
    */
    private Expr expr(){
        SimExpr simexpr;
        Expr expr = null;
        String relop = null;
        
        simexpr = simexpr();

        if ((lexer.token == Symbol.EQUAL) || (lexer.token == Symbol.GT) || 
            (lexer.token == Symbol.LE) || (lexer.token == Symbol.GE) || 
            (lexer.token == Symbol.DIF) || (lexer.token == Symbol.LT)) {
                relop = relop();
                expr = expr();
        }
                
        return new Expr( simexpr, relop , expr);        
          
    }
    
    /*
    simexp ::= [unary] term {addop term}
    term ::= factor {mulop factor}
    factor ::= vbl | num | ’(’ expr ’)’ | ”’.”’
    */
    private SimExpr simexpr(){
        String unary = null;
        Term term = null;
        ArrayList<String> addopL = null;
        ArrayList<Term> termL = null;
        
        if ((lexer.token == Symbol.PLUS) || (lexer.token == Symbol.MINUS )|| (lexer.token == Symbol.NOT))
            unary = unary();
        
        term = term();
        
        while( (lexer.token == Symbol.PLUS) || (lexer.token == Symbol.MINUS )|| (lexer.token == Symbol.NOT)){
            if (addopL == null) {
                addopL = new ArrayList<>();
                termL = new ArrayList<>();
            }
            addopL.add(addop());
            termL.add(term());
        }
        
        return new SimExpr ( unary, term , addopL, termL);
    }
    
    
    /*
    unary ::= ’+’ | ’-’ | NOT
    */
    private String unary(){
        String var = null;
        
        switch(lexer.token){
            case PLUS:
                var = "+";  break;
            case MINUS:
                var = "-";  break;
            case NOT:
                var = "not";break;
            default:
                lexer.error("Missing OPERADOR\n");            
        }
        lexer.nextToken();
        
        return var;
    }
    
    /*
    term ::= factor {mulop factor}
    factor ::= vbl | num | ’(’ expr ’)’ | ”’.”’
    */
    private Term term(){
        Factor factor = null;
        ArrayList<String> mulop = null;
        ArrayList<Factor> factorlist = null;
        
        factor = factor();
    
        while(lexer.token == Symbol.AND || lexer.token == Symbol.MINUS || lexer.token == Symbol.PLUS || 
              lexer.token == Symbol.DIVIDER || lexer.token == Symbol.DIV || lexer.token == Symbol.MOD || 
              lexer.token == Symbol.MULT){
            if(mulop == null){
                mulop = new ArrayList<>();
                factorlist = new ArrayList<>();
            }
            mulop.add(mulop());
            factorlist.add(factor());
        }
        
        return new Term( factor, mulop, factorlist);
    }
    
    /*
    factor ::= vbl | num | ’(’ expr ’)’ | ”’.”’ | procfuncstmt
    */
    private Factor factor(){
        Variable vb = null;
        String num = null;
        Expr expr = null;
        Stmt stmt = null;
        Stmt procfunc = null;
        String nome = null;

        
        
        switch (lexer.token){
            case IDENT:
                
                if (symbolTable.get(lexer.getStringValue()) instanceof Function) 
                    procfunc = (ProcFunction) functionCall();
                else if (symbolTable.get(lexer.getStringValue()) instanceof Procedure) 
                    lexer.error("There is no return in procedures.\n");
                else 
                    vb = vb();
                
                break;
            case NUMBER:
                num = num();    break;
            case LEFTPAR:
                lexer.nextToken();
                expr = expr();
                if(lexer.token == Symbol.RIGHTPAR)
                    lexer.nextToken();
                else
                    lexer.error("missing RIGHTPAR\n");
                
                break;
            case APOSTROPHE:
                lexer.nextToken();
                nome = nome();
                
                if (lexer.token == Symbol.APOSTROPHE)
                    lexer.nextToken();
                else
                    lexer.error("missing APOSTROPHE\n");
               
                break;
            default:
                lexer.error("missing valid keyword\n");
        }
        return new Factor(vb, num, expr, nome, procfunc);
    }
    
    
    private FunctionCall functionCall() {
        String routineFunc = null;
        ExprList exprArray = null;
        Function myFunc = null;
        routineFunc = id();
        myFunc = (Function) symbolTable.getInGlobal( routineFunc );
        
        
        if (myFunc == null)
            lexer.error(routineFunc + " already declared .\n");
        
        
        if (lexer.token == Symbol.LEFTPAR) {
                lexer.nextToken();

                if ((lexer.token == Symbol.IDENT) || (lexer.token == Symbol.NUMBER)
                        || (lexer.token == Symbol.LEFTPAR)
                        || (lexer.token == Symbol.PLUS)
                        || (lexer.token == Symbol.MINUS)
                        || (lexer.token == Symbol.NOT))
                    exprArray = exprlist();
                

                if (lexer.token == Symbol.RIGHTPAR) 
                    lexer.nextToken();
                else 
                    lexer.error("')' esperado.\n");

        }
        else 
            lexer.error("'(' esperado.\n");

        return new FunctionCall(myFunc,exprArray);
        
    }
    
    /*
    id ::= letter {letter | digit}
    pid ::= letter {letter | digit}
    */
    private String id(){
        String var = null;
        //System.out.println("Valor do NextToken" + lexer.token );
        if(lexer.token == Symbol.IDENT){
                var = lexer.getStringValue();
		lexer.nextToken();
        }else{
            lexer.error("Missing IDENT\n");
        }
        
	return var;
    }
    
    /*
    pid ::= letter {letter | digit}
    */
    private String nome(){
        String var;
        
        var = lexer.getStringValue();
        lexer.nextToken();
        
        return var;
    }
    
    /*
    num ::= [’+’ | ’-’] digit [’.’] {digit}
    intnum ::= digit {digit}
    */
    private String num(){
        StringBuilder number = new StringBuilder();

        if ((lexer.token == Symbol.PLUS) || (lexer.token == Symbol.MINUS)) {
            number.append(lexer.token.toString());
            lexer.nextToken();
        }

        if (lexer.token == Symbol.NUMBER) {
            number.append(lexer.getNumberValue());
            lexer.nextToken();
            if (lexer.token == Symbol.DOT) {
                    number.append(lexer.token.toString());
                    lexer.nextToken();
            }
            if (lexer.token == Symbol.NUMBER) {
                    number.append(lexer.getNumberValue());
                    lexer.nextToken();
            }
        }else {
            lexer.error("Missing Number\n");
        }

        return number.toString();
    }
    /*
    relop ::= ’=’ | ’<’ | ’>’ | ’<=’ | ’>=’ | ’<>’
    */
    private String relop() {
        String var = null;

        switch (lexer.token) {
            case EQUAL:
                var = Symbol.EQUAL.toString();  break;
            case LT:
                var = Symbol.LT.toString();     break;
            case GT:
                var = Symbol.GT.toString();     break;
            case LE:
                var = Symbol.LE.toString();     break;
            case GE:
                var = Symbol.GE.toString();     break;
            case DIF:
                var = Symbol.DIF.toString();    break;
            default:
                    lexer.error("missing OPERATOR\n");
        }

        lexer.nextToken();

        return var;
    }
    /*
    addop ::= ’+’ | ’-’ | OR
    */
    private String addop() {
        String var = null;

        switch (lexer.token) {
            case PLUS:
                var = Symbol.PLUS.toString();   break;
            case MINUS:
                var = Symbol.MINUS.toString();  break;
            case OR:
                var = Symbol.OR.toString();     break;
            default:
                lexer.error("missing Operator\n");
        }

        lexer.nextToken();

        return var;
    }
    
    /*
    mulop ::= ’*’ | ’/’ | AND | MOD | DIV
    */
    private String mulop() {
    String var = null;

    switch (lexer.token) {
        case MULT:
            var = Symbol.MULT.toString();   break;
        case DIVIDER:
            var = Symbol.DIVIDER.toString();break;
        case AND:
            var = Symbol.AND.toString();    break;
        case MOD:
            var = Symbol.MOD.toString();    break;
        case DIV:
            var = Symbol.DIV.toString();    break;
        default:
            lexer.error("Missing OPERATOR.\n");
       
    }

    lexer.nextToken();

    return var;
    }

    

    
}//final do compiler