/*
    comp10
    
    Main changes from the previous compiler:
    
      - user can supply only the file to be compiled. The output file is made
        to a ".c" file with the same name as the input;
      - identifiers should start with a letter but can be composed by letters and
        digits;
      - there is a while and a for statement;
      - some error messages do not cause the compiler to terminate the
        compilation. Then the compiler can signal more than one error;
      - previous compilers put parenthesis around any composite expression. Now they are
        put only in some situations in which they are probably necessary;
      - the program has now procedures and functions that resemble Pascal:

             procedure print( x, y : integer; ch : char )
                  var i : integer;
                begin
                write(ch);
                i = 1;
                while i <= 5 do
                  begin
                  write( x + y );
                  i = i + 1;
                  end;
                end
                
              function factorial( n : integer ) : integer
                begin
                if ( n <= 1 ) 
                  return 1;
                else
                  return n*factorial(n-1);
                endif;
                end
                
      - there must be a procedure called main without parameters.
    
    Grammar:
       Program ::= ProcFunc { ProcFunc }
       ProcFunc ::= Procedure | Function
       Procedure ::= "procedure" Ident '(' ParamList ')' 
           [ LocalVarDec ] CompositeStatement
       Function ::= "function" Ident '(' ParamList ')' ':' Type 
           [ LocalVarDec ] CompositeStatement
       ParamList ::= | ParamDec { ';' ParamDec } 
       ParamDec ::= Ident { ',' Ident } ':' Type 
       LocalVarDec ::= "var" VarDecList 
       CompositeStatement ::= "begin" StatementList "end"
       StatementList ::= | Statement ";" StatementList
       Statement ::= AssignmentStatement | IfStatement | ReadStatement |
          WriteStatement | ProcedureCall | ForStatement | WhileStatement |
          CompositeStatement | ReturnStatement
       AssignmentStatement ::= Ident "="  OrExpr  
       IfStatement ::= "if" OrExpr "then" StatementList [ "else" StatementList ] "endif" 
       ReadStatement ::= "read" "(" Ident ")"
       WriteStatement ::= "write" "(" OrExpr ")"
       ProcedureCall ::= Ident '(' ExprList ')'
       ExprList ::=  | OrExpr { ',' OrExpr } 
       ForStatement ::= "for" Ident '=' OrExpr "to" OrExpr "do" Statement
       WhileStatement ::= "while" OrExpr "do" Statement
       ReturnStatement ::= "return" OrExpr
       
       VarDecList ::= VarDecList2  { VarDecList2  }
       VarDecList2 ::= Ident { ',' Ident } ':' Type ';'
       Ident ::= Letter { Letter } 
       Type ::= "integer" | "boolean" | "char"
       OrExpr ::= AndExpr [ "or" AndExpr ]
       AndExpr ::= RelExpr [ "and" RelExpr ]
       RelExpr ::= AddExpr [ RelOp AddExpr ]
       AddExpr ::= MultExpr { AddOp MultExpr }
       MultExpr ::= SimpleExpr { MultOp SimpleExpr }
       SimpleExpr ::= Number | "true" | "false" | Character 
           | '(' OrExpr ')' | "not" SimpleExpr | AddOp SimpleExpr
           | Ident [ '(' ExprList ')' ]
       RelOp ::= '<' | '<=' | '>' | '>='| '==' | '<>'
       AddOp ::= '+'| '-'
       MultOp ::= '*' | '/' | '%'
       Number ::= ['+'|'-'] Digit { Digit }
       Digit ::= '0'| '1' | ... | '9'
       Letter ::= 'A' | 'B'| ... | 'Z'| 'a'| 'b' | ... | 'z'
       
   Character is a Letter enclosed between ' and ', like 'A', 'e' as in Java, C++, etc. 
   Anything between [] is optional. Anything between { e } can be 
   repeated zero or more times. 
*/

import AuxComp.StatementException;
import AuxComp.CompilerError;
import AuxComp.SymbolTable;
import AST.*;
import java.util.*;
import Lexer.*;
import java.io.*;


public class Compiler {
    
      // compile must receive an input with an character less than 
      // p_input.lenght
    public Program compile( char []input, PrintWriter outError ) {
        
        symbolTable = new SymbolTable();
        error = new CompilerError( lexer, new PrintWriter(outError) );
        lexer = new Lexer(input, error);
        error.setLexer(lexer);
        
        lexer.nextToken();
        
        Program p = null;
        try {
          p = program();
        } catch ( Exception e ) {
              // the below statement prints the stack of called methods.
              // of course, it should be removed if the compiler were 
              // a production compiler.
            e.printStackTrace();
        }
        if ( error.wasAnErrorSignalled() )
          return null;
        else
          return p;
        }
    
    private Program program() {
       // Program ::= ProcFunc { ProcFunc }
     
       ArrayList<Subroutine> procfuncList = new ArrayList<Subroutine>();
       
       while ( lexer.token == Symbol.PROCEDURE ||
               lexer.token == Symbol.FUNCTION ) 
           procfuncList.add( procFunc() );
           
       Program program = new Program( procfuncList );
       if ( lexer.token != Symbol.EOF ) 
         error.signal("EOF expected");
         // semantics analysis
         // there must be a procedure called main
       Subroutine mainProc;
       if ( (mainProc = (Subroutine ) symbolTable.getInGlobal("main")) == null )
          error.show("Source code must have a procedure called main");
       return program;
    }
    
    private Subroutine procFunc() {
       // Procedure ::= "procedure" Ident '(' ParamList ')' 
       //     [ LocalVarDec ] CompositeStatement
       // Function ::= "function" Ident '(' ParamList ')' ':' Type 
       //     [ LocalVarDec ] CompositeStatement
       
        boolean isFunction;
        
        
        if ( lexer.token == Symbol.PROCEDURE )
          isFunction = false;
        else if ( lexer.token == Symbol.FUNCTION )
          isFunction = true;
        else {
          // should never occur
          error.signal("Internal compiler error");
          return null;
        }
       
       lexer.nextToken();
       if ( lexer.token != Symbol.IDENT )
         error.signal("Identifier expected");
       String name = (String ) lexer.getStringValue();
         // Symbol table now searches for an identifier in the scope order. First
         // the local variables and parameters and then the procedures and functions.
         // at this point, there should not be any local variables/parameters in the
         // symbol table.
       Subroutine s = (Subroutine ) symbolTable.getInGlobal(name);
         // semantic analysis
         // identifier is in the symbol table
       if ( s != null ) 
         error.show("Subroutine " + name + " has already been declared");
       lexer.nextToken();
       
       if ( isFunction ) {
           // currentFunction is used to store the function being compiled or null if it is a procedure
         s = currentFunction = new Function(name);
       }
       else {
         s = new Procedure(name);
         currentFunction = null;
       }
         
         // insert s in the symbol table
       symbolTable.putInGlobal( name, s );
       
       if ( lexer.token != Symbol.LEFTPAR ) {
         error.show("( expected");
         lexer.skipBraces();
       }
       else
         lexer.nextToken();
       
         // semantic analysis
         // if the subroutine is "main", it must be a parameterless procedure
       if ( name.compareTo("main") == 0 && ( lexer.token != Symbol.RIGHTPAR
            || isFunction ) ) 
            error.show("main must be a parameterless procedure");
            
       s.setParamList( paramList() );
       if ( lexer.token != Symbol.RIGHTPAR ) {
         error.show(") expected");
         lexer.skipBraces();
       }
       else
         lexer.nextToken();
       
       if ( isFunction ) {
         if ( lexer.token != Symbol.COLON ) {
           error.show(": expected");
           lexer.skipPunctuation();
         }
         else
           lexer.nextToken();
         ((Function ) s).setReturnType( type() );
       }
       
       if ( lexer.token == Symbol.VAR ) 
         s.setLocalVarList( localVarDec() );
       
       s.setCompositeStatement( compositeStatement() );
       
       symbolTable.removeLocalIdent();
       return s;
    }
         
        
    private LocalVarList localVarDec() {
        // LocalVarDec ::= "var" VarDecList 
        // VarDecList ::= VarDecList2  { VarDecList2  }
        
        LocalVarList localVarList = new LocalVarList();
          // eat token "var"
        lexer.nextToken();
        
        varDecList2(localVarList);
        while ( lexer.token == Symbol.IDENT )
          varDecList2(localVarList);
        return localVarList;
    }

    private void varDecList2( LocalVarList localVarList ) {
        //  VarDecList2 ::= Ident { ',' Ident } ':' Type ';'

        ArrayList<Variable> lastVarList = new ArrayList<Variable>();
        
        while ( true ) {
          if ( lexer.token != Symbol.IDENT )
            error.signal("Identifier expected");
            // name of the identifier
          String name = (String ) lexer.getStringValue();
          lexer.nextToken();
            // semantic analysis
            // if the name is in the symbol table and the scope of the name is local, 
            // the variable is been declared twice. 
          if ( symbolTable.getInLocal(name) != null ) 
             error.show("Variable " + name + " has already been declared");

            // variable does not have a type yet
          Variable v = new Variable(name);
            // inserts the variable in the symbol table. The name is the key and an
            // object of class Variable is the value. Hash tables store a pair (key, value)
            // retrieved by the key.
          symbolTable.putInLocal( name, v );
            // list of the last variables declared. They don't have types yet
          lastVarList.add(v);
          
          if ( lexer.token == Symbol.COMMA ) 
            lexer.nextToken();
          else 
            break;
        }

        if ( lexer.token != Symbol.COLON ) { 
          error.show(": expected");
          lexer.skipPunctuation();
        }
        else
          lexer.nextToken();
          // get the type
        Type typeVar = type();
        
        for ( Variable v : lastVarList ) {
          v.setType(typeVar);
            // add variable to the list of variable declarations
          localVarList.addElement( v );
        }
        
        if ( lexer.token != Symbol.SEMICOLON ) {
          error.show("; expected");
          lexer.skipPunctuation();
        }
        else
          lexer.nextToken();  
    }

          
    private ParamList paramList() {
       // ParamList ::= | ParamDec { ';' ParamDec } 
        
        ParamList paramList = null;
        if ( lexer.token == Symbol.IDENT ) {
            paramList = new ParamList();
            paramDec(paramList);
            while ( lexer.token == Symbol.SEMICOLON ) {
                lexer.nextToken();
                paramDec(paramList);
            }
        }
        return paramList;
    }

    private void paramDec( ParamList paramList ) {
       // ParamDec ::= Ident { ',' Ident } ':' Type 

        ArrayList<Parameter> lastVarList = new ArrayList<Parameter>();
        
        while ( true ) {
          if ( lexer.token != Symbol.IDENT )
            error.signal("Identifier expected");
            // name of the identifier
          String name = (String ) lexer.getStringValue();
          lexer.nextToken();
            // semantic analysis
            // if the name is in the symbol table and the scope of the name is local, 
            // the variable is been declared twice. 
          if ( symbolTable.getInLocal(name) != null ) 
             error.show("Parameter " + name + " has already been declared");

            // variable does not have a type yet
          Parameter v = new Parameter(name);
            // inserts the variable in the symbol table. The name is the key and an
            // object of class Variable is the value. Hash tables store a pair (key, value)
            // retrieved by the key.
          symbolTable.putInLocal( name, v );
            // list of the last variables declared. They don't have types yet
          lastVarList.add(v);
          
          if ( lexer.token == Symbol.COMMA ) 
            lexer.nextToken();
          else 
            break;
        }

        if ( lexer.token != Symbol.COLON ) { //  :
          error.show(": expected");
          lexer.skipPunctuation();
        }
        else
          lexer.nextToken();
          // get the type
        Type typeVar = type();
        
        for ( Parameter v : lastVarList )  {
            // add type to the variable
          v.setType(typeVar);
            // add v to the list of parameter declarations
          paramList.addElement(v);

        }

    }
          
        

    private Type type() {
        Type result;
        
        switch ( lexer.token ) {
            case INTEGER :
              result = Type.integerType;
              break;
            case BOOLEAN :
              result = Type.booleanType;
              break;
            case CHAR :
              result = Type.charType;
              break;
            default :
              error.show("Type expected");
              result = Type.integerType;
        }
        lexer.nextToken();
        return result;
    }
    
        
        

    private CompositeStatement compositeStatement() {
          // CompositeStatement ::= "begin" StatementList "end"
          //  StatementList ::= | Statement ";" StatementList
        
        if ( lexer.token != Symbol.BEGIN )  
            error.show("begin expected");
        else
          lexer.nextToken();
        StatementList sl = statementList();
        if ( lexer.token != Symbol.END )
          error.show("end expected");
        else
          lexer.nextToken();
        return new CompositeStatement(sl);
    }



    private StatementList statementList() {
        
        Symbol tk;
        Statement astatement;
        ArrayList<Statement> v = new ArrayList<Statement>();
        
          // statements always begin with an identifier, if, read or write, ...
        while ( (tk = lexer.token) != Symbol.END && 
                tk != Symbol.ELSE &&
                tk != Symbol.ENDIF ) {
            astatement = null;
            try {
                // statement() should return null in a serious error
              astatement = statement();
            } catch (  StatementException e ) {
                lexer.skipToNextStatement();
            }
            if ( astatement != null ) {
              v.add(astatement);
              if ( lexer.token != Symbol.SEMICOLON ) {
                error.show("; expected", true);
                lexer.skipPunctuation();
              }
              else
                lexer.nextToken();
            }
        }
        return new StatementList(v);
    }
            
    private Statement statement() throws StatementException {
        /*  Statement ::= AssignmentStatement | IfStatement | ReadStatement |
               WriteStatement | ProcedureCall | ForStatement | WhileStatement |
               CompositeStatement | ReturnStatement
                          
        */
        
        switch (lexer.token) {
            case IDENT :
                // if the identifier is in the symbol table, "symbolTable.get(...)"
                // will return the corresponding object. If it is a procedure,
                // we should call procedureCall(). Otherwise we have an assignment
              if ( symbolTable.get(lexer.getStringValue()) instanceof Procedure )  
                  // Is the identifier a procedure ?
                return procedureCall();
              else
                return assignmentStatement();
            case IF :
              return ifStatement();
            case READ :
              return readStatement();
            case WRITE :
              return writeStatement();
            case FOR :
              return forStatement();
            case WHILE :
              return whileStatement();
            case BEGIN :
              return compositeStatement();
            case RETURN :
              return returnStatement();
            default :
              error.show("Statement expected");
              throw new StatementException();

        }
    }
    
    private AssignmentStatement assignmentStatement() {
        
        Variable v = checkVariable();
        String name = v.getName();
          
        if ( lexer.token != Symbol.ASSIGN ) {
          error.show("= expected");
          lexer.skipToNextStatement();
          return null;
        }
        else
          lexer.nextToken();
        Expr right = orExpr();
          // semantic analysis
          // check if expression has the same type as variable
        if ( ! checkAssignment( v.getType(), right.getType() ) )
          error.show("Type error in assignment");
          
        return new AssignmentStatement( v, right );
    }
    
    private boolean checkAssignment( Type varType, Type exprType ) {
        if ( varType == Type.undefinedType || exprType == Type.undefinedType )
          return true;
        else 
          return varType == exprType;
    }
    
    private IfStatement ifStatement() {
        
        lexer.nextToken();
        Expr e = orExpr();
          // semantic analysis
          // check if expression has type boolean
        if ( e.getType() != Type.booleanType ) 
          error.show("Boolean type expected in if expression");
        
        if ( lexer.token != Symbol.THEN ) 
          error.show("then expected");
        else
          lexer.nextToken();
        StatementList thenPart = statementList();
        StatementList elsePart = null;
        if ( lexer.token == Symbol.ELSE ) {
            lexer.nextToken();
            elsePart = statementList();
        }
        if ( lexer.token != Symbol.ENDIF )
          error.signal("endif expected");
        lexer.nextToken();
        return new IfStatement( e, thenPart, elsePart );
    }
            
          
            
    private ReadStatement readStatement() {
        lexer.nextToken();
        if ( lexer.token != Symbol.LEFTPAR ) {
          error.show("( expected");
          lexer.skipBraces();
        }
        else
          lexer.nextToken();
        if ( lexer.token != Symbol.IDENT )
          error.signal("Identifier expected");
          // semantic analysis
          // check if the variable was declared
        String name = (String ) lexer.getStringValue();
        Variable v = (Variable ) symbolTable.getInLocal(name);
        if ( v == null ) {
          error.show("Variable " + name + " was not declared");
          symbolTable.putInLocal( name, new Variable(name, Type.undefinedType) );
        }
          // semantic analysis
          // check if variable has type char or integer or undefined
        if ( v.getType() != Type.charType && v.getType() != Type.integerType &&
             v.getType() != Type.undefinedType ) 
          error.show("Variable should have type char or integer");
          
        lexer.nextToken();
        if ( lexer.token != Symbol.RIGHTPAR ) {
          error.show(") expected");
          lexer.skipBraces();
          lexer.skipToNextStatement();
          return null;
        }
        else {
          lexer.nextToken();
          return new ReadStatement( v );
        }
    }
    
    
    private WriteStatement writeStatement() {
        
        lexer.nextToken();
        if ( lexer.token != Symbol.LEFTPAR ) {
          error.show("( expected");
          lexer.skipBraces();
        }
        else
          lexer.nextToken();
          // expression may be of any type
        Expr e = orExpr();
        if ( lexer.token != Symbol.RIGHTPAR ) {
          error.show(") expected");
          lexer.skipBraces();
          lexer.skipToNextStatement();
          return null;
        }
        else {
          lexer.nextToken();
          return new WriteStatement( e );
        }
    }
        
        

    private ProcedureCall procedureCall() {
          // we already know the identifier is a procedure. So we need not to check it
          // again.
        ExprList anExprList = null;
        
        String name = (String ) lexer.getStringValue();
        lexer.nextToken();
        Procedure p = (Procedure ) symbolTable.getInGlobal(name);
        if ( lexer.token != Symbol.LEFTPAR ) {
          error.show("( expected");
          lexer.skipBraces();
        }
        else
          lexer.nextToken();
        
        if ( lexer.token != Symbol.RIGHTPAR ) {
            // The parameter list is used to check if the arguments to the
            // procedure have the correct types 
          anExprList = exprList( p.getParamList() );
          if ( lexer.token != Symbol.RIGHTPAR )
            error.show("Error in expression");
          else
            lexer.nextToken();
        }
        else {
            // semantic analysis
            // does the procedure has no parameter ?
          if ( p.getParamList() != null && p.getParamList().getSize() != 0 )
            error.signal("Parameter expected");
          lexer.nextToken();
        }
          
        return new ProcedureCall(p,anExprList);
    }
    
    
    
    ExprList exprList( ParamList paramList )
    {
       ExprList anExprList;
       boolean firstErrorMessage = true;
       
       if ( lexer.token == Symbol.RIGHTPAR ) 
          return null;
       else {
          Parameter parameter;
          int sizeParamList = paramList.getSize();
          Iterator e = paramList.getParamList().iterator();
          anExprList = new ExprList();
          while ( true ) {
            parameter = (Parameter ) e.next();
              // semantic analysis
              // does the procedure has one more parameter ?
            if ( sizeParamList < 1 && firstErrorMessage ) {
              error.show("Wrong number of parameters in call");
              firstErrorMessage = false;
            }
            sizeParamList--;
            Expr anExpr = orExpr();
            if ( ! checkAssignment(parameter.getType(), anExpr.getType()) )
              error.show("Type error in parameter passing");
            anExprList.addElement( anExpr );
            if ( lexer.token == Symbol.COMMA ) 
              lexer.nextToken();
            else
              break;
          }
            // semantic analysis
            // the procedure may need more parameters
          if ( sizeParamList > 0 && firstErrorMessage ) 
            error.show("Wrong number of parameters");
          return anExprList;
       }
    }

    private Statement forStatement() throws StatementException {
        
        Variable v = null;
        
        lexer.nextToken();
        if ( lexer.token != Symbol.IDENT ) 
            error.signal("Variable expected");
        v = checkVariable();
          // variable can be of any type
        if ( lexer.token != Symbol.ASSIGN )
          error.show("= expected");
        else
          lexer.nextToken();
        Expr exprLower = orExpr();
        if ( lexer.token != Symbol.TO )
          error.show("to expected");
        else
          lexer.nextToken();
        Expr exprUpper = orExpr();
          // semantic analysis
        if ( ! checkForExpr(exprLower.getType(), exprUpper.getType()) ) 
          error.show("both for expressions should have the same type");
        if ( lexer.token != Symbol.DO )
          error.show("do expected");
        else
          lexer.nextToken();
        return new ForStatement(v, exprLower, exprUpper, statement() );
    }


    private Statement whileStatement() throws StatementException {

        lexer.nextToken();
        Expr expr = orExpr();
        if ( !  checkWhileExpr(expr.getType()) )
          error.show("Boolean expression expected");
        if ( lexer.token != Symbol.DO )
          error.show("do expected");
        else
          lexer.nextToken();
        return new WhileStatement(expr, statement() );
    }
    
    
    private ReturnStatement returnStatement() {
        
        lexer.nextToken();
        Expr e = orExpr();
          // semantic analysis
          // Are we inside a function ?
        if ( currentFunction == null ) 
          error.show("return statement inside a procedure");
        else if ( ! checkAssignment( currentFunction.getReturnType(), 
                                e.getType() ) )
            error.show("Return type does not match function type");
        return new ReturnStatement(e);
    }
    
    
    private boolean checkWhileExpr( Type exprType ) {
        if ( exprType == Type.undefinedType || exprType == Type.booleanType )
          return true;
        else
          return false;
    }
          
    private boolean checkForExpr( Type lowerExprType, Type upperExprType ) {
        if ( lowerExprType == Type.undefinedType || upperExprType == Type.integerType )
          return true;
        else
          return lowerExprType == upperExprType;
    }

          
    private Variable checkVariable() {
        // tests if the current identifier is a declared variable. If not,
        // declares it with the type Type.undefinedType.
        // assume lexer.token == Symbol.IDENT
        
        Variable v = null;
        
        String name = (String ) lexer.getStringValue();
        try { 
          v = (Variable ) symbolTable.getInLocal( name );
        } catch ( Exception e ) {
        }
               // semantic analysis
               // was the variable declared ? 
        if ( v == null ) {
            error.show("Variable " + name + " was not declared");
            v = new Variable(name, Type.undefinedType);
            symbolTable.putInLocal( name, v );
        }
        lexer.nextToken();
        return v;
    }


    private Expr orExpr() {
      /*  
        OrExpr ::= AndExpr [ "or" AndExpr ]
      */
      
      Expr left, right;
      left = andExpr();
      if ( lexer.token == Symbol.OR ) {
        lexer.nextToken();
        right = andExpr();
          // semantic analysis
        if ( ! checkBooleanExpr( left.getType(), right.getType() ) )
           error.show("Expression of boolean type expected");
        left = new CompositeExpr(left, Symbol.OR, right);
      }
      return left;
    }

    private boolean checkBooleanExpr( Type left, Type right ) {
        
        if ( left == Type.undefinedType || right == Type.undefinedType ) 
          return true;
        else
          return left == Type.booleanType && right == Type.booleanType;
    }
    
    private Expr andExpr() {
      /*
        AndExpr ::= RelExpr [ "and" RelExpr ]
      */
      Expr left, right;
      left = relExpr();
      if ( lexer.token == Symbol.AND ) {
        lexer.nextToken();
        right = relExpr();
          // semantic analysis
        if ( ! checkBooleanExpr( left.getType(), right.getType() )  )
           error.show("Expression of boolean type expected");
        left = new CompositeExpr( left, Symbol.AND, right );
      }
      return left;
    }

    private Expr relExpr() {
      /*
        RelExpr ::= AddExpr [ RelOp AddExpr ]
      */
      Expr left, right;
      left = addExpr();
      Symbol op = lexer.token;
      if ( op == Symbol.EQ || op == Symbol.NEQ || op == Symbol.LE || op == Symbol.LT ||
           op == Symbol.GE || op == Symbol.GT  ) {
         lexer.nextToken();
         right = addExpr();
           // semantic analysis
         if ( ! checkRelExpr(left.getType(), right.getType() ) ) 
           error.show("Type error in expression");
         left = new CompositeExpr( left, op, right );
       }
       return left;
    }
    
    private boolean checkRelExpr( Type left, Type right ) {
        
        if ( left == Type.undefinedType || right == Type.undefinedType )
          return true;
        else
          return left == right;
    }
    
    
    private Expr addExpr() {
      /*
        AddExpr ::= MultExpr { AddOp MultExpr }
        
      */
      Symbol op;
      Expr left, right;
      left = multExpr();
      while ( (op = lexer.token) == Symbol.PLUS ||
              op == Symbol.MINUS ) {
        lexer.nextToken();
        right = multExpr();
          // semantic analysis
        if ( ! checkMathExpr( left.getType(), right.getType() ) )
           error.show("Expression of type integer expected");
        left = new CompositeExpr( left, op, right );
      }
      return left;
    }
       
    private boolean checkMathExpr( Type left, Type right ) {
        boolean orLeft  = left  == Type.integerType ||
                          left  == Type.undefinedType;
        boolean orRight = right == Type.integerType ||
                          right == Type.undefinedType;
        return orLeft && orRight;
    }
       
    private Expr multExpr() {
     /*
        MultExpr ::= SimpleExpr { MultOp SimpleExpr }
     */    
       Expr left, right;
       left = simpleExpr();
       Symbol op;
       while ( (op = lexer.token) == Symbol.MULT ||
               op == Symbol.DIV || op == Symbol.REMAINDER ) {
          lexer.nextToken();
          right = simpleExpr();
            // semantic analysis
          if ( ! checkMathExpr(left.getType(), right.getType())  )
            error.show("Expression of type integer expected");
          left = new CompositeExpr( left, op, right );
       }
       return left;
    }
           
    private Expr simpleExpr() {
      /*
        SimpleExpr ::= Number |  "true" | "false" | Character 
           | '(' OrExpr ')' | "not" SimpleExpr | Variable 
      */
      
      Expr e;
      
        // note we test the lexer.getToken() to decide which production to use
      switch ( lexer.token ) {
         case NUMBER :
            return number();
         case TRUE :
           lexer.nextToken();
           return BooleanExpr.True;
         case FALSE :
           lexer.nextToken();
           return BooleanExpr.False;
         case CHARACTER :
             // get the token with getToken.
             // then get the value of it, with has the type Object
             // convert the object to type Character using a cast
             // call method charValue to get the character inside the object
           char ch = lexer.getCharValue();
           lexer.nextToken();
           return new CharExpr(ch);
         case LEFTPAR :
           lexer.nextToken();
           e = orExpr();
           if ( lexer.token != Symbol.RIGHTPAR ) {
             error.show(") expected");
             lexer.skipBraces();
           }
           else
             lexer.nextToken();
           return new ParenthesisExpr(e);
         case NOT :
           lexer.nextToken();
           e = orExpr();
             // semantic analysis
           if ( e.getType() != Type.booleanType )
             error.show("Expression of type boolean expected");
           return new UnaryExpr( e, Symbol.NOT );
         case PLUS :
           lexer.nextToken();
           e = orExpr();
             // semantic analysis
           if ( e.getType() != Type.integerType )
             error.show("Expression of type integer expected");
           return new UnaryExpr( e, Symbol.PLUS );
         case MINUS :
           lexer.nextToken();
           e = orExpr();
             // semantic analysis
           if ( e.getType() != Type.integerType )
             error.show("Expression of type integer expected");
           return new UnaryExpr( e, Symbol.MINUS );
         default :
             // an identifier
           if ( lexer.token != Symbol.IDENT ) {
             error.show("Identifier expected");
             lexer.nextToken();
             return new VariableExpr(new Variable("nameless", Type.undefinedType ));
           }
           else {
                // this part needs to be improved. If the compiler finds
                // a call to a function that was not declared, it will sign an
                // error  "Identifier was not declared" and signal errors because of the
                // parentheses following the function name. This can be corrected.
             String name = (String ) lexer.getStringValue();
               // is it a function ?
             Object objIdent = symbolTable.get(name);
             if ( objIdent == null ) {
               error.show("Identifier was not declared");
               lexer.nextToken();
               if ( lexer.token != Symbol.LEFTPAR ) {
                  Variable newVariable = new Variable(name, Type.undefinedType);
                  symbolTable.putInLocal( name, newVariable );
                  return new VariableExpr(newVariable);
               }
               else {
                  Function falseFunction = new Function(name);
                  falseFunction.setReturnType(Type.undefinedType);
                  falseFunction.setCompositeStatement(null);
                  symbolTable.putInGlobal(name, falseFunction);
                  objIdent = falseFunction;
               }
             }
                
             if ( objIdent instanceof Subroutine ) {
                if ( objIdent instanceof Function ) 
                   return functionCall();
                else {
                  error.show("Attempt to call a procedure in an expression");
                  procedureCall();
                  return new VariableExpr(new Variable("nameless", Type.undefinedType));
                }
             }
             else {
                // it is a variable
               Variable v = (Variable ) objIdent;
               lexer.nextToken();
               return new VariableExpr(v);
             }
           }
      }
            
    }
    

    private FunctionCall functionCall() {
          // we already know the identifier is a function. So we 
          // need not to check it again.
        ExprList anExprList = null;
        
        String name = (String ) lexer.getStringValue();
        lexer.nextToken();
        Function p = (Function ) symbolTable.getInGlobal(name);
        if ( lexer.token != Symbol.LEFTPAR ) {
          error.show("( expected");
          lexer.skipBraces();
        }
        else
          lexer.nextToken();
        
        if ( lexer.token != Symbol.RIGHTPAR ) {
            // The parameter list is used to check if the arguments to the
            // procedure have the correct types 
          anExprList = exprList( p.getParamList() );
          if ( lexer.token != Symbol.RIGHTPAR )
            error.show("Error in expression");
          else
            lexer.nextToken();
        }
        else {
            // semantic analysis
            // does the procedure has no parameter ?
          if ( p.getParamList() != null && p.getParamList().getSize() != 0 )
            error.show("Parameter expected");
          lexer.nextToken();
        }
          
        return new FunctionCall(p,anExprList);
    }
    
    
    
    private NumberExpr number() {
        
        NumberExpr e = null;
        
          // the number value is stored in lexer.getToken().value as an object of Integer.
          // Method intValue returns that value as an value of type int.
        int value = lexer.getNumberValue();
        lexer.nextToken();
        return new NumberExpr( value );
    }
    
    
    private boolean checkTypes( Type left, Symbol op, Type right ) {
        // return true if  left and right can be the types of a composite
        // expression with operator op 

       if ( op == Symbol.EQ || op == Symbol.NEQ || op == Symbol.LE || op == Symbol.LT ||
            op == Symbol.GE || op == Symbol.GT )
          return left == right;
       else if ( op == Symbol.PLUS || op == Symbol.MINUS || op == Symbol.DIV ||
                 op == Symbol.MULT || op == Symbol.REMAINDER ) {
          if ( left != right || left != Type.integerType )
             return false;
          else 
             return true;
       }
       else if ( op == Symbol.AND || op == Symbol.OR ) {
          if ( left != Type.booleanType || 
               right != Type.booleanType )
             return false;
          else 
             return true;
       }
       else {
          error.signal("Compiler internal error: unknown operator");
          return true;
       }
    }
    
    

    
    
    private SymbolTable symbolTable;
    private Lexer lexer;
    private CompilerError error;
    
      // keeps a pointer to the current function being compiled
    private Function currentFunction;
    
}
