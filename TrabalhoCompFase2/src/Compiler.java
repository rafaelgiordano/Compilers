/*

Marcos Cavalcante Barboza - 408336
Rafael Paschoal Giordano - 408298

*/

/*
prog ::= PROGRAM pid ’;’ body ’.’
body ::= [dclpart] compstmt
dclpart ::= VAR dcls
dcls ::= dcl {dcl}
dcl ::= idlist ’:’ type ’;’
idlist ::= id {’,’ id}
type ::= stdtype | arraytype
stdtype ::= INTEGER | REAL | CHAR | STRING
arraytype ::= ARRAY ’[’ intnum ’..’ intnum ’]’ OF stdtype
compstmt ::= BEGIN stmts END
stmts ::= stmt {’;’ stmt} ’;’
stmt ::= ifstmt
| whilestmt
| assignstmt
| compstmt
| readstmt
| writestmt
| writelnstmt
ifstmt ::= IF expr THEN stmts [ELSE stmts] ENDIF
whilestmt ::= WHILE expr DO stmts ENDWHILE
assignstmt ::= vbl ’:=’ expr
readstmt ::= READ ’(’ vblist ’)’
writestmt ::= WRITE ’(’ exprlist ’)’
writelnstmt ::= WRITELN ’(’ [exprlist] ’)’
vblist ::= vbl {’,’ vbl}
vbl ::= id [’[’ expr ’]’]
exprlist ::= expr {’,’ expr}
expr ::= simexp [relop expr]
simexp ::= [unary] term {addop term}
term ::= factor {mulop factor}
factor ::= vbl
| num
| ’(’ expr ’)’
| ”’.”’
id ::= letter {letter | digit}
pid ::= letter {letter | digit}
num ::= [’+’ | ’-’] digit [’.’] {digit}
intnum ::= digit {digit}
relop ::= ’=’ | ’<’ | ’>’ | ’<=’ | ’>=’ | ’<>’
addop ::= ’+’ | ’-’ | OR
mulop ::= ’*’ | ’/’ | AND | MOD | DIV
unary ::= ’+’ | ’-’ | NOT

*/
import AST.*;
import java.util.ArrayList;
import lexer.*;

import java.util.*;


public class Compiler {
    
    
    private Lexer lexer;
    
    private Hashtable<String, Variable> symbolTable = new Hashtable<>();
    
    
    public Prog compile(char[] p_input) {
		// No construtor da classe Lexer ocorre a chamada da função
		// que converte toda a entrada para letras minúsculas
		this.lexer = new Lexer(p_input);
		this.symbolTable = new Hashtable();
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
    
    private Prog program(){
        String my_pid = null;
        Body my_body = null;
        
        if (lexer.token != Symbol.PROGRAM) {
                lexer.error("'program' esperado no início do arquivo.\n");
        }

	lexer.nextToken();
	my_pid = id();
        
        if (lexer.token != Symbol.SEMICOLON) {
			lexer.error("';' esperado.\n");
		}

        lexer.nextToken();
        my_body = body();
        
        if (lexer.token != Symbol.DOT) {
            lexer.error("'.' esperado.\n");
        }

        lexer.nextToken();

        if (lexer.token != Symbol.EOF) {    // or != '\0'
                lexer.error("Final do arquivo esperado.\n");
        }

        return new Prog(my_pid, my_body);
    }
     private Body body(){

        DclPart my_dcl = null;
        CmpStmt my_compStmt = null;

        if(lexer.token == Symbol.VAR){
            my_dcl = dclspart();
        }
        
        
        my_compStmt = cmpstmt();

        return new Body(my_dcl, my_compStmt);
    }
    private DclPart dclspart(){
        
        DclList dcls = null;
        
        if ( lexer.token == Symbol.VAR){
            lexer.nextToken();
            dcls = dcls();
        }
        
    return new DclPart(dcls);
        
    }
    private DclList dcls(){
        
        ArrayList<Dcl> dclList = null;
        dclList = new ArrayList<Dcl>();
        
        dclList.add(dcl());
        while (lexer.token == Symbol.IDENT) {
			dclList.add(dcl());
		}
        
        return new DclList(dclList);
    }
    
    private Dcl dcl(){
        
        ArrayList<String> idlist;
        idlist = null;
        Type type = null;
        
        idlist = idList();
        
        
        if(lexer.token == Symbol.COLON){
            lexer.nextToken();
            
            type = type();
            for (String v : idlist){
              
               if( symbolTable.get(v) == null){
               symbolTable.put(v, new Variable(v, null,type));
               }else{
                   lexer.error("variavel \"" + v + "\" ja declarada\n");
               }
           }
            if (lexer.token == Symbol.SEMICOLON) {
                    lexer.nextToken();
            }
            else {
                    lexer.error("';' esperado.\n");
            }
        }else{
                lexer.error("':' esperado \n");
            
        }
        
        return new Dcl ( idlist , type);
    }
    
    private ArrayList<String> idList(){
        ArrayList<String> idlist = new ArrayList<String>();
        System.out.println("IDLIST");
        System.out.println("token: " + lexer.token.toString());
        idlist.add(id());
        System.out.println("token: " + lexer.token.toString());
        
        while (lexer.token == Symbol.COMMA) {
            System.out.println("token: " + lexer.token.toString());
            lexer.nextToken();
            idlist.add(id());
        }
        
        System.out.println("token: " + lexer.token.toString());
        return idlist;
    }
    
    private Type type(){
        
        StdType stdtype = null;
        ArrayType arraytype = null;
        
        if (( lexer.token == Symbol.INTEGER) || ( lexer.token == Symbol.REAL) || ( lexer.token == Symbol.CHAR) || ( lexer.token == Symbol.STRING)){
        
        stdtype = stdtype();
        }else{
            if(lexer.token == Symbol.ARRAY){
                arraytype = arrayType();
                
        }else
                lexer.error("tipo nao identificado \n");
    }  
        if(stdtype == null){
            return arraytype;
        }
        return stdtype;
    }
    
    
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
                            lexer.error("missing of\n");
                        }
                    }else{
                        lexer.error("missing RIGHTSBRACKET\n");
                    }
                          
                }else{
                    lexer.error("missing DOUBLEDOT\n");
                }
            }else{
                lexer.error("missing LEFTSBRACKET");
            }
        }else{
            lexer.error("missing ARRAY");
        }
        return new ArrayType(type, inic, fim);
        
    }
    
    private StdType stdtype() {
        String val;

        if ((lexer.token == Symbol.INTEGER) || (lexer.token == Symbol.REAL) || (lexer.token == Symbol.CHAR) || (lexer.token == Symbol.STRING)) {
            val = lexer.getStringValue();
            lexer.nextToken();
            return new StdType(val);
        }else{
            lexer.error("missing type.\n");
            return null;
        }

    }
    
    private CmpStmt cmpstmt(){
        StmtList stmt = null;
        
        if(lexer.token == Symbol.BEGIN){
            lexer.nextToken();
            stmt = stmts();
            if(lexer.token == Symbol.END){
                lexer.nextToken();
            }else{
                lexer.error("missing END\n");
            }
        }else{
            System.out.println("GetStringValue["+lexer.getStringValue()+"]");
            lexer.error("missing BEGIN\n");
        }
        return new CmpStmt(stmt);
    }
    
    //stmts ::= stmt {’;’ stmt} ’;’
    private StmtList stmts(){
        ArrayList<Stmt> stmtlist;
        stmtlist = new ArrayList<>();
        
        
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
    
    private Stmt stmt(){
        
        switch(lexer.token){
            case IDENT:
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
            default:
                System.out.println("TOKEN STMT["+lexer.token.toString()+"]");
                lexer.error("missing statment\n");
        }
        
        return null;
    }
    
    private AssignStmt assignstmt(){
        Variable vb = null;
        
        Expr expr = null;
        
        vb = vb();
        
        if(lexer.token == Symbol.ASSIGN){
            lexer.nextToken();
            expr= (expr());
        }else
        {
            lexer.error("missing ASSIGN SYMBOl :=");
        }
        
        return new AssignStmt(vb, expr);
    }
    
    private IfStmt ifstmt(){
        Expr expr=null;
        Stmt thenstmt=null;
        Stmt elsestmt= null;
        
        if(lexer.token == Symbol.IF){
            lexer.nextToken();
            System.out.println("token: " + lexer.token.toString());
            expr= (expr());
            if(lexer.token == Symbol.THEN){
                lexer.nextToken();
                thenstmt = stmt();
                if(lexer.token == Symbol.ELSE){
                    lexer.nextToken();
                    elsestmt = stmt();
                }
                //System.out.println("TokenIFSTMT["+lexer.token.toString()+"]");
                lexer.nextToken();
                if(lexer.token == Symbol.ENDIF){
                    lexer.nextToken();
                }else
                    lexer.error("missing ENDIF");
            }else
                lexer.error("missing then");
        }else{
            lexer.error("missing IF");
        }
        
        return new IfStmt(expr, thenstmt, elsestmt);
    }
    
    private WhileStmt whilestmt(){
        
        Expr expr=null;
        Stmt stmt=null;
        
        if(lexer.token == Symbol.WHILE){
            lexer.nextToken();
            expr=expr();
            if(lexer.token == Symbol.DO){
                lexer.nextToken();
                stmt = stmt();
                
                if(lexer.token == Symbol.ENDWHILE){
                    lexer.nextToken();
                
                }else{
                    lexer.error("missing ENDWHILE\n");
                }
            }else{
                lexer.error("missing DO\n");
            }
        }else{
            lexer.error("missing WHILE");
        }
        
        return new WhileStmt(expr , stmt);
    }
    
    private WriteStmt writestmt(){
        
        
        ExprList exprlist=null;
        
        if(lexer.token == Symbol.WRITE){
            lexer.nextToken();
            if (lexer.token == Symbol.LEFTPAR) {
                lexer.nextToken();
                exprlist = exprlist();

                if (lexer.token == Symbol.RIGHTPAR) {
                        lexer.nextToken();
                }
                else {
                    lexer.error("missing RIGHTPAR\n");
                }
        }else{
                lexer.error("missing LEFTPAR\n");
            }
    }else{
            lexer.error("missing WRITE stmt\n");
        }
    
    
    return new WriteStmt (exprlist, false);
    
    }
    
    private WriteStmt writelnstmt(){
        
        ExprList exprlist = null;
        
        if(lexer.token == Symbol.WRITELN){
            lexer.nextToken();
            
            if(lexer.token == Symbol.LEFTPAR){
                
                lexer.nextToken();
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
                    lexer.error("missing RIGHTPAR\n");
                }
            }else{
                lexer.error("missing LEFTPAR\n");
            }
        }else{
            lexer.error("missing WRITELN\n");
        }
        
        return new WriteStmt (exprlist, true);
    }
    
    private ReadStmt readstmt(){
        VariableList vblist = null;
        
        if(lexer.token == Symbol.READ){
            lexer.nextToken();
            if(lexer.token == Symbol.LEFTPAR){
                lexer.nextToken();
                vblist = vblist();
                if(lexer.token == Symbol.RIGHTPAR){
                    lexer.nextToken();
                }else{
                    lexer.error("missing RIGHTPAR\n");
                }
            }else{
                lexer.error("missing LEFTPAR\n");
            }
        }else{
            lexer.error("MISSING READ STMT\n");
        }
        
        
        return new ReadStmt( vblist);
    }
    
    
    private Variable vb(){
        
        String id = null;
        Expr expr = null;
        Variable var = null;
        
        
        id = id();
        
        if(lexer.token == Symbol.LEFTSQBRACKET){
            lexer.nextToken();
            expr = expr();
            
            
            if(lexer.token == Symbol.RIGHTSQBRACKET){
                lexer.nextToken();
            }else{
                lexer.error("missing RIGHTSBRACKET \n");
            }
        }
        
        var = symbolTable.get(id);
        if( var == null){
            lexer.error("Variable["+id+"]not declared");
        }
        
        return new Variable (id, expr, var.getType());
           
        
    }
    
    private VariableList vblist(){
        
        
        ArrayList<Variable> vblist;
        vblist = new ArrayList<Variable>();
        
        vblist.add(vb());
        
        while(lexer.token == Symbol.COMMA){
            lexer.nextToken();
            vblist.add(vb());
        }
        return new VariableList(vblist);
        
    }
    
    private ExprList exprlist(){
        
        ArrayList<Expr> exprlist;
        exprlist = new ArrayList<Expr>();
        
        exprlist.add(expr());
        while(lexer.token == Symbol.COMMA){
            lexer.nextToken();
            exprlist.add(expr());
        }
        
        return new ExprList(exprlist);
    }
    
    private Expr expr(){
        
        SimExpr simexpr = null;
        
        Expr expr = null;
        
        String relop = null;
        
        simexpr = simexpr();

        if ((lexer.token == Symbol.EQUAL)
                        || (lexer.token == Symbol.GT) || (lexer.token == Symbol.LE)
                        || (lexer.token == Symbol.GE) || (lexer.token == Symbol.DIF) || (lexer.token == Symbol.LT)) {
                relop = relop();
                expr = expr();
    }
                
        return new Expr( simexpr, relop , expr);        
          
    }
    
    
    private SimExpr simexpr(){
        
        
        String unary = null;
        Term term = null;
        ArrayList<String> addopL = null;
        ArrayList<Term> termL=null;
        
        if ((lexer.token == Symbol.PLUS) || (lexer.token == Symbol.MINUS )|| (lexer.token == Symbol.NOT)){
            unary = unary();
        }
        term = term();
        
        while( (lexer.token == Symbol.PLUS) || (lexer.token == Symbol.MINUS )|| (lexer.token == Symbol.NOT)){
            if (addopL == null) {
                addopL = new ArrayList<String>();
                termL = new ArrayList<Term>();
            }
            addopL.add(addop());
            termL.add(term());
        }
        return new SimExpr ( unary, term , addopL, termL);
    }
    
    private String unary(){
        
        String var = null;
        
        switch(lexer.token){
            case PLUS:
                var = "+";
            case MINUS:
                var = "-";
            case NOT:
                var = "not";
            default:
                lexer.error("missing OPERADOR\n");
                     
        }
        lexer.nextToken();
        
        return var;
    }
    
    private Term term(){
        Factor factor = null;
        
        ArrayList<String> mulop = null;
        
        ArrayList<Factor> factorlist = null;
        
        factor = factor();
        
        
        while(lexer.token == Symbol.AND || lexer.token == Symbol.MINUS || lexer.token == Symbol.PLUS || lexer.token == Symbol.DIVIDER || lexer.token == Symbol.DIV
                || lexer.token == Symbol.MOD || lexer.token == Symbol.MULT){
            if(mulop == null){
                mulop = new ArrayList<String>();
                factorlist = new ArrayList<Factor>();
            }
            mulop.add(mulop());
            factorlist.add(factor());
        }
        
        return new Term( factor, mulop, factorlist);
    }
    
    private Factor factor(){
        
        Variable vb = null;
        String num = null;
        Expr expr = null;
        Stmt stmt = null;
        String nome = null;
        
        System.out.println("FACTOR - token: " + lexer.token.toString());
        
        switch (lexer.token){
            case IDENT:
                vb = vb();
                break;
            case NUMBER:
                System.out.println("token: " + lexer.token.toString());
                num = num();
                break;
            case LEFTPAR:
                lexer.nextToken();
                expr = expr();
                if(lexer.token == Symbol.RIGHTPAR){
                    lexer.nextToken();
                }else
                    lexer.error("missing RIGHTPAR\n");
                break;
            case APOSTROPHE:
                lexer.nextToken();
                nome = nome();
                if (lexer.token == Symbol.APOSTROPHE) {
                    lexer.nextToken();
                }
                else {
                    lexer.error("missing APOSTROPHE\n");
                }
                break;
            default:
                lexer.error("missing valid keyword\n");
        }
        return new Factor(vb, num, expr, nome);
    }
    
    private String id(){
        
        String var = null;
        if(lexer.token == Symbol.IDENT){
                var = lexer.getStringValue();
		lexer.nextToken();
        }else
            lexer.error("missing IDENT\n");
                    
	return var;
    }
    
    private String nome(){
        String var = null;
        
        var = lexer.getStringValue();
        lexer.nextToken();
        
        return var;
    }
    
    
    private String num(){
        StringBuffer number = new StringBuffer();

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
        }
        else {
                lexer.error("missing number\n");
        }

        return number.toString();
    }
    
    private String relop() {
        String var = null;

        switch (lexer.token) {
                case EQUAL:
                        var = Symbol.EQUAL.toString();
                        break;
                case LT:
                        var = Symbol.LT.toString();
                        break;
                case GT:
                    var = Symbol.GT.toString();
                        break;
                case LE:
                    var = Symbol.LE.toString();
                    break;
                case GE:
                    var = Symbol.GE.toString(); 
                case DIF:
                    var = Symbol.DIF.toString();
                    
                default:
                        lexer.error("missing OPERATOR\n");
                    break;
        }

        lexer.nextToken();

        return var;
    }
    private String addop() {
        String var = null;

        switch (lexer.token) {
                case PLUS:
                    var = Symbol.PLUS.toString();
                    break;
                case MINUS:
                    var = Symbol.MINUS.toString();
                    break;
                case OR:
                    var = Symbol.OR.toString();
                    break;
                default:
                    lexer.error("missing Operator\n");
                    break;
        }

        lexer.nextToken();

        return var;
    }
    private String mulop() {
    String var = null;

    switch (lexer.token) {
            case MULT:
                var = Symbol.MULT.toString();
                break;
            case DIVIDER:
                var = Symbol.DIVIDER.toString();
                break;
            case AND:
                var = Symbol.AND.toString();
                break;
            case MOD:
                var = Symbol.MOD.toString();
                break;
            case DIV:
                var = Symbol.DIV.toString();
                break;
            default:
                lexer.error("Missing OPERATOR.\n");
            break;
    }

    lexer.nextToken();

    return var;
    }
}//final do compiler
