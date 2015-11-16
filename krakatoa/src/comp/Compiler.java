/*
 Primeiro Trabalho de Laboratório de Compiladores
 
 Segundo Semestre de 2015.
 Campus de Sorocaba da UFSCar
 Prof. José de Oliveira Guimarães
 
 Grupo:
 Nome:Lucas Gabriel Mendes de Oliveira  RA: 380091
 Nome:Rafael Paschoal Giordano          RA: 408298
 */
package comp;

import ast.*;
import lexer.*;
import java.io.*;
import java.util.*;

public class Compiler {

    // compile must receive an input with an character less than
    // p_input.lenght
    public Program compile(char[] input, PrintWriter outError) {

        ArrayList<CompilationError> compilationErrorList = new ArrayList<>();
        signalError = new SignalError(outError, compilationErrorList);
        symbolTable = new SymbolTable();
        lexer = new Lexer(input, signalError);
        signalError.setLexer(lexer);

        Program program = null;
        lexer.nextToken();
        //verificar arquivo vazio
        program = program(compilationErrorList);
        //verificar se o arquivo termina EOF
        return program;
    }

    private Program program(ArrayList<CompilationError> compilationErrorList) {

        // Program ::= KraClass { KraClass }
        ArrayList<MetaobjectCall> metaobjectCallList = new ArrayList<>();
        ArrayList<KraClass> kraClassList = new ArrayList<>();

        try {
            while (lexer.token == Symbol.MOCall) {
                metaobjectCallList.add(metaobjectCall());
            }
            kraClassList.add(classDec());
            while (lexer.token == Symbol.CLASS || lexer.token == Symbol.FINAL) {
                //adicionar a classe a lista
                kraClassList.add(classDec());
            }
            if (lexer.token != Symbol.EOF) {
                signalError.show("End of file expected");
            }

            //verificar se existe uma classe Program
            if (symbolTable.getInGlobal("Program") == null) {
                signalError.show("O código deve ter uma classe 'Program'!");
            }
        } catch (RuntimeException e) {
            // if there was an exception, there is a compilation signalError
        }

        return new Program(kraClassList, metaobjectCallList, compilationErrorList);
    }

    /**
     * parses a metaobject call as <code>{@literal @}ce(...)</code> in <br>
     * <code>
     *
     * @ce(5, "'class' expected") <br>
     * clas Program <br>
     * public void run() { } <br>
     * end <br>
     * </code>
     *
     *
     */
    @SuppressWarnings("incomplete-switch")
    private MetaobjectCall metaobjectCall() {
        String name = lexer.getMetaobjectName();
        lexer.nextToken();
        ArrayList<Object> metaobjectParamList = new ArrayList<>();
        if (lexer.token == Symbol.LEFTPAR) {
            // metaobject call with parameters
            lexer.nextToken();
            while (lexer.token == Symbol.LITERALINT || lexer.token == Symbol.LITERALSTRING
                    || lexer.token == Symbol.IDENT) {
                switch (lexer.token) {
                    case LITERALINT:
                        metaobjectParamList.add(lexer.getNumberValue());
                        break;
                    case LITERALSTRING:
                        metaobjectParamList.add(lexer.getLiteralStringValue());
                        break;
                    case IDENT:
                        metaobjectParamList.add(lexer.getStringValue());
                }
                lexer.nextToken();
                if (lexer.token == Symbol.COMMA) {
                    lexer.nextToken();
                } else {
                    break;
                }
            }
            if (lexer.token != Symbol.RIGHTPAR) {
                signalError.show("')' expected after metaobject call with parameters");
            } else {
                lexer.nextToken();
            }
        }
        if (name.equals("nce")) {
            if (metaobjectParamList.size() != 0) {
                signalError.show("Metaobject 'nce' does not take parameters");
            }
        } else if (name.equals("ce")) {
            if (metaobjectParamList.size() != 3 && metaobjectParamList.size() != 4) {
                signalError.show("Metaobject 'ce' take three or four parameters");
            }
            if (!(metaobjectParamList.get(0) instanceof Integer)) {
                signalError.show("The first parameter of metaobject 'ce' should be an integer number");
            }
            if (!(metaobjectParamList.get(1) instanceof String) || !(metaobjectParamList.get(2) instanceof String)) {
                signalError.show("The second and third parameters of metaobject 'ce' should be literal strings");
            }
            if (metaobjectParamList.size() >= 4 && !(metaobjectParamList.get(3) instanceof String)) {
                signalError.show("The fourth parameter of metaobject 'ce' should be a literal string");
            }

        }

        return new MetaobjectCall(name, metaobjectParamList);
    }

    private KraClass classDec() {
        // Note que os métodos desta classe não correspondem exatamente às
        // regras
        // da gramática. Este método classDec, por exemplo, implementa
        // a produção KraClass (veja abaixo) e partes de outras produções.

        /*
         * KraClass ::= "class" Id [ "extends" Id ] "{" MemberList "}"
         * MemberList ::= { Qualifier Member } 
         * Member ::= InstVarDec | MethodDec
         * InstVarDec ::= Type IdList ";" 
         * MethodDec ::= Qualifier Type Id "("[ FormalParamDec ] ")" "{" StatementList "}" 
         * Qualifier ::= ["Final"]["static"]  ( "private" | "public" )
         */
        KraClass kclass;
        boolean isFinal = false;

        if (lexer.token == Symbol.FINAL) {
            isFinal = true;
            lexer.nextToken();
        }

        if (lexer.token != Symbol.CLASS) {
            signalError.show("'class' expected");
        }
        lexer.nextToken();
        if (lexer.token != Symbol.IDENT) {
            signalError.show(SignalError.ident_expected);
        }

        String className = lexer.getStringValue();

        //verificar se a classe já existe
        if (symbolTable.getInGlobal(className) != null) {
            signalError.show("Classe " + className + " está sendo redeclarada!");
        }

        kclass = new KraClass(className);
        currentClass = kclass;
        kclass.setIsFinal(isFinal);

        symbolTable.putInGlobal(className, kclass);

        lexer.nextToken();
        if (lexer.token == Symbol.EXTENDS) {
            lexer.nextToken();
            if (lexer.token != Symbol.IDENT) {
                signalError.show(SignalError.ident_expected);
            }
            String superclassName = lexer.getStringValue();

            //verificar se a super classe já existe
            if (symbolTable.getInGlobal(className) == null) {
                signalError.show("Classe " + superclassName + " não existe!");
            }

            //verificar se não herda dela mesma 
            if (className.equals(superclassName)) {
                signalError.show("Classe " + superclassName + " está herdado de si mesma!");
            }

            //define a super classe dentro da classe
            kclass.setSuperclass(symbolTable.getInGlobal(superclassName));

            lexer.nextToken();
        }
        if (lexer.token != Symbol.LEFTCURBRACKET) {
            signalError.show("{ expected", true);
        }
        lexer.nextToken();

        //MemberList ::= { Qualifier Member } 
        //Member ::= InstVarDec | MethodDec
        while (lexer.token == Symbol.PRIVATE || lexer.token == Symbol.PUBLIC
                || lexer.token == Symbol.FINAL || lexer.token == Symbol.STATIC) {

            boolean qualifier_private,
                    qualifier_final = false,
                    qualifier_static = false;

            switch (lexer.token) {
                case FINAL:
                    lexer.nextToken();
                    qualifier_final = true;
                    if (lexer.token == Symbol.STATIC) {
                        lexer.nextToken();
                        qualifier_static = true;
                    }
                    break;
                case STATIC:
                    lexer.nextToken();
                    qualifier_static = true;
                    break;
            }

            switch (lexer.token) {
                case PRIVATE:
                    lexer.nextToken();
                    qualifier_private = true;
                    break;
                case PUBLIC:
                    lexer.nextToken();
                    qualifier_private = false;
                    break;
                default:
                    signalError.show("private, or public expected");
                    qualifier_private = false;
            }

            Type t = type();
            //InstVarDec ::= Type IdList ";" 
            //MethodDec ::= Type Id "("[ FormalParamDec ] ")" "{" StatementList "}" 

            if (lexer.token != Symbol.IDENT) {
                signalError.show("Identifier expected");
            }

            String memberName = lexer.getStringValue();
            lexer.nextToken();

            if (lexer.token == Symbol.LEFTPAR) {
                MethodDec methodDec = methodDec(t, memberName, qualifier_private, qualifier_final, qualifier_static);

//                //verificar se já existe este método com os mesmos parâmetros para esta classe
//                MethodDec methodReturned = kclass.findMethodDec(memberName, false);
//                if (methodReturned != null && methodReturned.getFormalParamDec() == methodDec.getFormalParamDec()) {
//                    signalError.show("O método " + memberName + " da classe " + className + " está já existe com estes parâmetros!");
//                }
                if (qualifier_private) {
                    kclass.addPrivateMethod(methodDec);
                } else {
                    kclass.addPublicMethod(methodDec);
                }
            } else if (!qualifier_private) {
                //se não for método, verifica se não está declarando variável pública
                signalError.show("Attempt to declare a public instance variable");
            } else {
                instanceVarDec(t, memberName, qualifier_final, qualifier_static, qualifier_private);

                //estava fazendo a verificação aqui, mas foi melhor verificar 
                //dentro do método instanceVarDec por poder declarar várias 
                //variáveis de mesmo tipo juntas
//                InstanceVariable instVarReturned = kclass.findInstanceVariable(memberName);
//                if (instVarReturned != null) {
//                    signalError.show("A variável " + memberName + " da classe " + className + " está já existe!");
//                }
//
//                kclass.addInstanceVariable(instanceVariable);
            }
        }

        if (className.equals("Program") && currentClass.findMethodDec("run", false) == null) {
            signalError.show("Método 'run' não foi encontrado na classe 'Program'!");
        }

        if (lexer.token != Symbol.RIGHTCURBRACKET) {
            signalError.show("public/private or \"}\" expected");
        }
        lexer.nextToken();

        return kclass;
    }

    private void instanceVarDec(Type type, String name, boolean qualifier_final, boolean qualifier_static, boolean qualifier_private) {
        // InstVarDec ::= [ "static" ] "private" Type IdList ";"

        InstanceVariable instVarReturned = currentClass.findInstanceVariable(name);
        if (instVarReturned != null) {
            signalError.show("A variável " + name + " da classe " + currentClass.getName() + " está já existe!");
        }

        InstanceVariable instanceVariable = new InstanceVariable(qualifier_final, qualifier_static, qualifier_private, name, type);
        currentClass.addInstanceVariable(instanceVariable);

        while (lexer.token == Symbol.COMMA) {
            lexer.nextToken();
            if (lexer.token != Symbol.IDENT) {
                signalError.show("Identifier expected");
            }
            String variableName = lexer.getStringValue();

            instVarReturned = currentClass.findInstanceVariable(variableName);
            if (instVarReturned != null) {
                signalError.show("A variável " + variableName + " da classe " + currentClass.getName() + " está já existe!");
            }

            instanceVariable = new InstanceVariable(qualifier_final, qualifier_static, qualifier_private, variableName, type);
            currentClass.addInstanceVariable(instanceVariable);

            lexer.nextToken();
        }
        if (lexer.token != Symbol.SEMICOLON) {
            signalError.show(SignalError.semicolon_expected);
        }
        lexer.nextToken();

    }

    private MethodDec methodDec(Type type, String name, boolean qualifier_private, boolean qualifier_final, boolean qualifier_static) {
        /*
         * MethodDec ::= Qualifier Return Id "("[ FormalParamDec ] ")" "{"
         *                StatementList "}"
         */

        MethodDec methodDec = new MethodDec(qualifier_final, qualifier_static, qualifier_private, type, name);

        currentMethod = methodDec;

        //verificar se tem o mesmo nome de alguma variável da classe corrente
        if (currentClass.findInstanceVariable(name) != null) {
            signalError.show("A classe " + currentClass.getName() + " tem uma variável declarada com o nome mesmo nome do método!");
        }

        lexer.nextToken();
        if (lexer.token != Symbol.RIGHTPAR) {
            //se for run não pode ter parâmetros, verificar
            methodDec.setFormalParamDec(formalParamDec());
        }

        //verificar se já existe este método com os mesmos parâmetros para esta classe
        MethodDec methodReturned = currentClass.findMethodDec(name, false);
        if (methodReturned != null && methodReturned.getFormalParamDec() == methodDec.getFormalParamDec()) {
            signalError.show("O método " + name + " da classe " + currentClass.getName() + " já existe com estes parâmetros!");
        }
        if (lexer.token != Symbol.RIGHTPAR) {
            signalError.show(") expected");
        }

        lexer.nextToken();
        if (lexer.token != Symbol.LEFTCURBRACKET) {
            signalError.show("{ expected");
        }

        if (currentClass.getName().equals("Program") && name.equals("run")) {
            if (qualifier_static) {
                signalError.show("Método 'run' não pode ser estático!");
            }
            if (qualifier_private) {
                signalError.show("Método 'run' não pode ser privado!");
            }
            if (currentMethod.getFormalParamDec() != null) {
                signalError.show("Método 'run' não pode receber parâmetros!");
            }
            if (!type.getName().equals("void")) {
                signalError.show("Método 'run' deve ser do tipo 'void'!");
            }
        } else if (currentClass.findMethodDec("run", false) != null) {
            signalError.show("Método 'run' fora de uma classe 'Program'!");
        }

        lexer.nextToken();

        //verificações para classe program método run, se tem retorno, se é publico, não estático
        methodDec.setStatementList(statementList());

        //verificar se o statement tem return caso o método tenha retorno
        //talvez seja necessário criar um flag na classe MethodDec para o retorno
//        if (currentMethod.isHaveReturn() && type.getName().equals("void")) {
//            signalError.show("O método " + name + " tem tipo 'void', portanto não pode ter retorno!");
//        }
        if (!currentMethod.isHaveReturn() && !type.getName().equals("void")) {
            signalError.show("Statement 'return' não encontrado no método " + name + "!");
        }

        if (lexer.token != Symbol.RIGHTCURBRACKET) {
            signalError.show("} expected");
        }

        symbolTable.removeLocalIdent();

        lexer.nextToken();

        return methodDec;
    }

    private LocalVariableList localDec() {
        // LocalDec ::= Type IdList ";"

        LocalVariableList localVariableList = new LocalVariableList();

        Type type = type();
        if (lexer.token != Symbol.IDENT) {
            signalError.show("Identifier expected");
        }

        String varriableName = lexer.getStringValue();

        //verificar se ela já existe
        if (symbolTable.getInLocal(varriableName) != null) {
            signalError.show("A variável" + varriableName + " está sendo redeclarada!");
        }

        Variable variable = new Variable(varriableName, type);
        symbolTable.putInLocal(varriableName, variable);

        localVariableList.addElement(variable);

        lexer.nextToken();
        if (lexer.token == Symbol.COMMA) {
            while (lexer.token == Symbol.COMMA) {
                lexer.nextToken();
                if (lexer.token != Symbol.IDENT) {
                    signalError.show("Identifier expected");
                }
                varriableName = lexer.getStringValue();

                //verificar se ela já existe
                if (symbolTable.getInLocal(varriableName) != null) {
                    signalError.show("A variável" + varriableName + " está sendo redeclarada!");
                }

                variable = new Variable(varriableName, type);
                symbolTable.putInLocal(varriableName, variable);

                localVariableList.addElement(variable);

                lexer.nextToken();
            }
        } else if (lexer.token != Symbol.SEMICOLON) {
            signalError.show("';' expected");
        }
        lexer.nextToken();

        return localVariableList;
    }

    private FormalParamDec formalParamDec() {
        // FormalParamDec ::= ParamDec { "," ParamDec }

        ParamList paramList = new ParamList();

        paramList.addElement(paramDec());
        while (lexer.token == Symbol.COMMA) {
            lexer.nextToken();
            paramList.addElement(paramDec());
        }

        return new FormalParamDec(paramList);
    }

    private Parameter paramDec() {
        // ParamDec ::= Type Id

        Type paramType = type();
        if (lexer.token != Symbol.IDENT) {
            signalError.show("Identifier expected");
        }
        String paramName = lexer.getStringValue();

        if (symbolTable.getInLocal(paramName) != null) {
            signalError.show("O parâmetro " + paramName + " já foi declarado anteriormente!");
        }

        Parameter parameter = new Parameter(paramName, paramType);
        symbolTable.putInLocal(paramName, parameter);

        lexer.nextToken();

        return parameter;
    }

    private Type type() {
        // Type ::= BasicType | Id
        Type result;

        switch (lexer.token) {
            case VOID:
                result = Type.voidType;
                break;
            case INT:
                result = Type.intType;
                break;
            case BOOLEAN:
                result = Type.booleanType;
                break;
            case STRING:
                result = Type.stringType;
                break;
            case IDENT:
                // # corrija: faça uma busca na TS para buscar a classe
                // IDENT deve ser uma classe.
                String className = lexer.getStringValue();
                result = symbolTable.getInGlobal(className);

                if (result == null) {
                    signalError.show("Classe não existente");
                    result = Type.undefinedType;
                }
                break;

            default:
                signalError.show("Type expected");
                result = Type.undefinedType;
        }
        lexer.nextToken();
        return result;
    }

    private CompositeStmt compositeStatement() {

        lexer.nextToken();

        CompositeStmt compositeStm = new CompositeStmt(statementList());
        if (lexer.token != Symbol.RIGHTCURBRACKET) {
            signalError.show("} expected");
        } else {
            lexer.nextToken();
        }
        return compositeStm;
    }

    private StatementList statementList() {
        // CompStatement ::= "{" { Statement } "}"

        StatementList statementList = new StatementList();
        Symbol tk;
        // statements always begin with an identifier, if, read, write, ...
        while ((tk = lexer.token) != Symbol.RIGHTCURBRACKET
                && tk != Symbol.ELSE) {
            statementList.addElement(statement());
        }
        return statementList;
    }

    private Statement statement() {
        /*
         * Statement ::= Assignment ";" | IfStat |WhileStat | MessageSend
         *                ";" | ReturnStat ";" | ReadStat ";" | WriteStat ";" |
         *               "break" ";" | ";" | CompStatement | LocalDec
         */

        switch (lexer.token) {
            case THIS:
            case IDENT:
            case SUPER:
            case INT:
            case BOOLEAN:
            case STRING:
                return assignExprLocalDec();
            case RETURN:
                return returnStatement();
            case READ:
                return readStatement();
            case WRITE:
                return writeStatement();
            case WRITELN:
                return writelnStatement();
            case IF:
                return ifStatement();
            case BREAK:
                return breakStatement();
            case WHILE:
                return whileStatement();
            case SEMICOLON:
                return nullStatement();
            case LEFTCURBRACKET:
                return compositeStatement();
            default:
                signalError.show("Statement expected");
        }
        return null;
    }

    /*
     * retorne true se 'name' é uma classe declarada anteriormente. É necessário
     * fazer uma busca na tabela de símbolos para isto.
     */
    private boolean isType(String name) {
        return this.symbolTable.getInGlobal(name) != null;
    }

    /*
     * AssignExprLocalDec ::= Expression [ "=" Expression ] | LocalDec
     * Expression ::= SimpleExpression [ Relation SimpleExpression ]
     * Relation ::= "==" | "<" | ">" | "<=" | ">=" | "!="
     * SimpleExpression ::= Term { LowOperator Term }
     * Term ::= SignalFactor { HighOperator SignalFactor }
     * SignalFactor ::= [ Signal ] Factor
     * Signal ::= "+" | "-"
     * LocalDec ::= Type IdList ";"
     */
    private Statement assignExprLocalDec() {
        // não entendi como fazer herdar de expr e não de statement =/
        // como colocar um expr em uma statementList?

        Expr rightExpr = null;
        Expr leftExpr = null;
        if (lexer.token == Symbol.INT || lexer.token == Symbol.BOOLEAN
                || lexer.token == Symbol.STRING
                || // token é uma classe declarada textualmente antes desta
                // instrução
                (lexer.token == Symbol.IDENT && isType(lexer.getStringValue()))) {
            /*
             * uma declaração de variável. 'lexer.token' é o tipo da variável
             * 
             * AssignExprLocalDec ::= Expression [ "=" Expression ] | LocalDec
             * LocalDec ::= Type IdList ";"
             */

            LocalVariableList localVariableList = localDec();
            return new AssignExprLocalDec(localVariableList);
            //Aqui ta dando bosta

        } else {
            /*
             * AssignExprLocalDec ::= Expression [ "=" Expression ]
             */
            leftExpr = expr();

            if (lexer.token == Symbol.ASSIGN) {
                lexer.nextToken();
                rightExpr = expr();

                if (leftExpr == null) {
                    signalError.show("Variavel nao existe para a Atribuicao ", true);
                }
                /*
                 Type t_left, t_right;
                 t_left = leftExpr.getType();
                 t_right = rightExpr.getType();

                 if (t_left.getName().equals(Symbol.INT) && t_right.getName().equals(Symbol.BOOLEAN)
                 || t_left.getName().equals(Symbol.BOOLEAN) && t_right.getName().equals(Symbol.INT)) {
                 signalError.show("Type error", true);
                 }
                 */
                //verificar se os tipos dos expr são os mesmos
                if (lexer.token != Symbol.SEMICOLON) {
                    signalError.show("';' expected", true);
                } else {
                    lexer.nextToken();
                }
            }
            //verificar se o right não é null
            if (rightExpr != null) {
                return new AssignExprLocalDec(leftExpr, rightExpr);
            }
        }
        return new MessageSendStatement((MessageSend) leftExpr);
    }

    private ExprList realParameters() {
        ExprList anExprList = null;

        if (lexer.token != Symbol.LEFTPAR) {
            signalError.show("( expected");
        }
        lexer.nextToken();
        if (startExpr(lexer.token)) {
            anExprList = exprList();
        }
        if (lexer.token != Symbol.RIGHTPAR) {
            signalError.show(") expected");
        }
        lexer.nextToken();
        return anExprList;
    }

    private WhileStmt whileStatement() {

        quantidadeWhile++;

        lexer.nextToken();
        if (lexer.token != Symbol.LEFTPAR) {
            signalError.show("( expected");
        }
        lexer.nextToken();
        Expr e = expr();

        //verificar se é uma expressão booleana
        if (lexer.token != Symbol.RIGHTPAR) {
            signalError.show(") expected");
        }
        lexer.nextToken();
        Statement s = statement();

        quantidadeWhile--;
        return new WhileStmt(e, s);
    }

    private IfStmt ifStatement() {

        lexer.nextToken();
        if (lexer.token != Symbol.LEFTPAR) {
            signalError.show("( expected");
        }
        lexer.nextToken();
        Expr e = expr();
        if (lexer.token != Symbol.RIGHTPAR) {
            signalError.show(") expected");
        }
        lexer.nextToken();
        Statement leftStatement = statement();
        Statement rightStatement = null;
        if (lexer.token == Symbol.ELSE) {
            lexer.nextToken();
            rightStatement = statement();
        }

        return new IfStmt(e, leftStatement, rightStatement);
    }

    private ReturnStmt returnStatement() {

        //verificar método corrent
        //verificar se tem tipo de retorno void
        currentMethod.setHaveReturn(true);

        if (currentMethod.getTipoRetorno().getName().equals("void")) {
            signalError.show("O método " + currentMethod.getMethodName() + " tem tipo 'void', portanto não pode ter retorno!");
        }

        lexer.nextToken();
        Expr returnExpr = expr();

        //verificar se o tipo da expressão é o mesmo tipo do retorno do método
//        if (returnExpr.getType() != currentMethod.getTipoRetorno()) {
//            signalError.show("Tipo de retorno incompatível com o do método " + currentMethod.getMethodName() + "!");
//        }
        if (lexer.token != Symbol.SEMICOLON) {
            signalError.show(SignalError.semicolon_expected);
        }
        lexer.nextToken();

        return new ReturnStmt(returnExpr);
    }

    private ReadStmt readStatement() {
        //"read" "(" LeftValue { "," LeftValue } ")"

        ReadStmt readStmt = new ReadStmt();

        lexer.nextToken();
        if (lexer.token != Symbol.LEFTPAR) {
            signalError.show("( expected");
        }
        lexer.nextToken();
        while (true) {
            if (lexer.token == Symbol.THIS) {
                lexer.nextToken();
                if (lexer.token != Symbol.DOT) {
                    signalError.show(". expected");
                }
                lexer.nextToken();
            }
            if (lexer.token != Symbol.IDENT) {
                signalError.show(SignalError.ident_expected);
            }

            String name = lexer.getStringValue();
            lexer.nextToken();
            if (lexer.token == Symbol.COMMA) {
                lexer.nextToken();
            } else {
                break;
            }
        }

        if (lexer.token != Symbol.RIGHTPAR) {
            signalError.show(") expected");
        }
        lexer.nextToken();
        if (lexer.token != Symbol.SEMICOLON) {
            signalError.show(SignalError.semicolon_expected);
        }
        lexer.nextToken();

        return readStmt;
    }

    private WriteStmt writeStatement() {

        lexer.nextToken();
        if (lexer.token != Symbol.LEFTPAR) {
            signalError.show("( expected");
        }
        lexer.nextToken();
        ExprList exprList = exprList();

        //verificar se não está imprimeindo objetos ou expressões booleanas
        if (lexer.token != Symbol.RIGHTPAR) {
            signalError.show(") expected");
        }
        lexer.nextToken();
        if (lexer.token != Symbol.SEMICOLON) {
            signalError.show(SignalError.semicolon_expected);
        }
        lexer.nextToken();

        return new WriteStmt(exprList, false);
    }

    private WriteStmt writelnStatement() {

        lexer.nextToken();
        if (lexer.token != Symbol.LEFTPAR) {
            signalError.show("( expected");
        }
        lexer.nextToken();
        ExprList exprList = exprList();

        //verificar se não está imprimeindo objetos ou expressões booleanas
        if (lexer.token != Symbol.RIGHTPAR) {
            signalError.show(") expected");
        }
        lexer.nextToken();
        if (lexer.token != Symbol.SEMICOLON) {
            signalError.show(SignalError.semicolon_expected);
        }
        lexer.nextToken();

        return new WriteStmt(exprList, true);
    }

    private BreakStmt breakStatement() {

        //verificar break fora de while
        if (quantidadeWhile == 0) {
            signalError.show("'break' statement found outside a 'while' statement");
        }
        lexer.nextToken();
        if (lexer.token != Symbol.SEMICOLON) {
            signalError.show(SignalError.semicolon_expected);
        }
        lexer.nextToken();

        return new BreakStmt();
    }

    private NullStmt nullStatement() {
        lexer.nextToken();
        return new NullStmt();
    }

    private ExprList exprList() {
        // ExpressionList ::= Expression { "," Expression }

        ExprList anExprList = new ExprList();
        anExprList.addElement(expr());
        while (lexer.token == Symbol.COMMA) {
            lexer.nextToken();
            anExprList.addElement(expr());
        }
        return anExprList;
    }

    private Expr expr() {

        Expr left = simpleExpr();
        Symbol op = lexer.token;
        if (op == Symbol.EQ || op == Symbol.NEQ || op == Symbol.LE
                || op == Symbol.LT || op == Symbol.GE || op == Symbol.GT) {
            lexer.nextToken();
            Expr right = simpleExpr();
            left = new CompositeExpr(left, op, right);
        }
        return left;
    }

    private Expr simpleExpr() {
        Symbol op;

        Expr left = term();
        while ((op = lexer.token) == Symbol.MINUS || op == Symbol.PLUS
                || op == Symbol.OR) {
            lexer.nextToken();
            Expr right = term();
            left = new CompositeExpr(left, op, right);
        }
        return left;
    }

    private Expr term() {
        Symbol op;

        Expr left = signalFactor();
        while ((op = lexer.token) == Symbol.DIV || op == Symbol.MULT
                || op == Symbol.AND) {
            lexer.nextToken();
            Expr right = signalFactor();
            left = new CompositeExpr(left, op, right);
        }
        return left;
    }

    private Expr signalFactor() {
        Symbol op;
        if ((op = lexer.token) == Symbol.PLUS || op == Symbol.MINUS) {
            lexer.nextToken();
            return new SignalExpr(op, factor());
        } else {
            return factor();
        }
    }

    /*
     * Factor ::= BasicValue | "(" Expression ")" | "!" Factor | "null" |
     *      ObjectCreation | PrimaryExpr
     * 
     * BasicValue ::= IntValue | BooleanValue | StringValue 
     * BooleanValue ::=  "true" | "false" 
     * ObjectCreation ::= "new" Id "(" ")" 
     * PrimaryExpr ::= "super" "." Id "(" [ ExpressionList ] ")"  | 
     *                 Id  |
     *                 Id "." Id | 
     *                 Id "." Id "(" [ ExpressionList ] ")" |
     *                 Id "." Id "." Id "(" [ ExpressionList ] ")" |
     *                 "this" | 
     *                 "this" "." Id | 
     *                 "this" "." Id "(" [ ExpressionList ] ")"  | 
     *                 "this" "." Id "." Id "(" [ ExpressionList ] ")"
     */
    private Expr factor() {

        Expr e;
        ExprList exprList;
        String messageName, ident;
        KraClass classe;
        MethodDec methodDec;

        switch (lexer.token) {
            // IntValue
            case LITERALINT:
                return literalInt();
            // BooleanValue
            case FALSE:
                lexer.nextToken();
                return LiteralBoolean.False;
            // BooleanValue
            case TRUE:
                lexer.nextToken();
                return LiteralBoolean.True;
            // StringValue
            case LITERALSTRING:
                String literalString = lexer.getLiteralStringValue();
                lexer.nextToken();
                return new LiteralString(literalString);
            // "(" Expression ")" |
            case LEFTPAR:
                lexer.nextToken();
                e = expr();
                if (lexer.token != Symbol.RIGHTPAR) {
                    signalError.show(") expected");
                }
                lexer.nextToken();
                return new ParenthesisExpr(e);

            // "null"
            case NULL:
                lexer.nextToken();
                return new NullExpr();
            // "!" Factor
            case NOT:
                lexer.nextToken();
                e = expr();
                return new UnaryExpr(e, Symbol.NOT);
            // ObjectCreation ::= "new" Id "(" ")"
            case NEW:
                lexer.nextToken();
                if (lexer.token != Symbol.IDENT) {
                    signalError.show("Identifier expected");
                }

                String className = lexer.getStringValue();
                /*
                 * // encontre a classe className in symbol table KraClass 
                 *      aClass = symbolTable.getInGlobal(className); 
                 *      if ( aClass == null ) ...
                 */
                classe = symbolTable.getInGlobal(className);
                if (classe == null) {
                    signalError.show("Classe não existente");
                }

                //se não for nulo precisa colocar na local?
                lexer.nextToken();
                if (lexer.token != Symbol.LEFTPAR) {
                    signalError.show("( expected");
                }
                lexer.nextToken();
                if (lexer.token != Symbol.RIGHTPAR) {
                    signalError.show(") expected");
                }
                lexer.nextToken();
                /*
                 * return an object representing the creation of an object
                 */
                return new ObjectCreation(classe);
            /*
             * PrimaryExpr ::= "super" "." Id "(" [ ExpressionList ] ")"  | 
             *                 Id  |
             *                 Id "." Id | 
             *                 Id "." Id "(" [ ExpressionList ] ")" |
             *                 Id "." Id "." Id "(" [ ExpressionList ] ")" |
             *                 "this" | 
             *                 "this" "." Id | 
             *                 "this" "." Id "(" [ ExpressionList ] ")"  | 
             *                 "this" "." Id "." Id "(" [ ExpressionList ] ")"
             */
            case SUPER:
                // "super" "." Id "(" [ ExpressionList ] ")"
                lexer.nextToken();
                if (lexer.token != Symbol.DOT) {
                    signalError.show("'.' expected");
                } else {
                    lexer.nextToken();
                }
                if (lexer.token != Symbol.IDENT) {
                    signalError.show("Identifier expected");
                }
                messageName = lexer.getStringValue();
                /*
                 * para fazer as conferências semânticas, procure por 'messageName'
                 * na superclasse/superclasse da superclasse etc
                 */
                KraClass superClass = currentClass.getSuperclass();

                if (superClass == null) {
                    signalError.show("A classe " + currentClass.getName() + " não tem superclasse!");
                }

                methodDec = superClass.findMethodDec(messageName, true);

                if (methodDec == null) {
                    signalError.show("O método " + messageName + " não existe na superclasse!");
                } else if (methodDec.isIsPrivate()) {
                    signalError.show("O método " + messageName + " é privado na superclasse!");
                }

                lexer.nextToken();
                exprList = realParameters();

                return new MessageSendToSuper(methodDec, exprList);

            case IDENT:
                /*
                 * PrimaryExpr ::=  
                 *                 Id  |
                 *                 Id "." Id | 
                 *                 Id "." Id "(" [ ExpressionList ] ")" |
                 *                 Id "." Id "." Id "(" [ ExpressionList ] ")" |
                 */

                String firstId = lexer.getStringValue();
                lexer.nextToken();
                if (lexer.token != Symbol.DOT) {
                    // Id
                    Variable variable = symbolTable.getInLocal(firstId);

                    if (variable == null) {
                        signalError.show("A variável " + firstId + " ainda não foi declarada!");
                    }

                    // retorne um objeto da ASA que representa um identificador
                    return new VariableExpr(variable);
//---------------------------------------------------------------------------------------------------------------------
                } else { // Id "."
                    lexer.nextToken(); // coma o "."
                    if (lexer.token != Symbol.IDENT) {
                        signalError.show("Identifier expected");
                    } else {
                        // Id "." Id
                        lexer.nextToken();
                        ident = lexer.getStringValue();  //seria a variável da classe firstId no caso
                        if (lexer.token == Symbol.DOT) {
                            // Id "." Id "." Id "(" [ ExpressionList ] ")"
						/*
                             * se o compilador permite variáveis estáticas, é possível
                             * ter esta opção, como
                             *     Clock.currentDay.setDay(12);
                             * Contudo, se variáveis estáticas não estiver nas especificações,
                             * sinalize um erro neste ponto.
                             */
                            lexer.nextToken();
                            if (lexer.token != Symbol.IDENT) {
                                signalError.show("Identifier expected");
                            }
                            messageName = lexer.getStringValue();
                            lexer.nextToken();

                            classe = symbolTable.getInGlobal(firstId);
                            //verificar se a classe existe
                            if (classe == null) {
                                signalError.show("A classe " + firstId + " não existe!");
                            }

                            InstanceVariable instanceVariable = classe.findInstanceVariable(ident);
                            if (instanceVariable == null) {
                                signalError.show("A variável " + ident + " não foi declarada na classe" + firstId + " !");
                            }
//                            if(!instanceVariable.isStatic()){
//                                signalError.show("A variável " + ident + " deve ser estática para ser usada desta forma!");
//                            }

                            methodDec = classe.findMethodDec(messageName, false);
                            if (methodDec == null) {
                                signalError.show("O método " + messageName + " não existe na classe " + firstId + "!");
                            }

                            exprList = this.realParameters();

                            return new MessageSendToVariable(instanceVariable, methodDec, exprList, classe);
                        } else if (lexer.token == Symbol.LEFTPAR) {
                            // Id "." Id "(" [ ExpressionList ] ")"
                            exprList = this.realParameters();
                            /*
                             * para fazer as conferências semânticas, procure por
                             * método 'ident' na classe de 'firstId'
                             */

                            //talvez tenha q fazer distinção de local e global
                            Variable localVariable = symbolTable.getInLocal(firstId);
                            if (localVariable != null) {

                                if (!(localVariable.getType() instanceof KraClass)) {
                                    signalError.show("A variável " + firstId + " não é uma classe para poder chamar métodos!");
                                }

                                KraClass variableTypeClass = (KraClass) localVariable.getType();

                                methodDec = variableTypeClass.findMethodDec(ident, true);
                                if (methodDec == null) {
                                    signalError.show("O método " + ident + " não existe na classe " + variableTypeClass.getName() + "!");
                                } else if (methodDec.isIsPrivate()) {
                                    signalError.show("O método " + ident + " não pode ser acessado pois é privado!");
                                }

                                //falta fazer a verificação na lista de parâmetros
                                return new MessageSendToVariable(localVariable, methodDec, exprList, null);

                            }

                            classe = symbolTable.getInGlobal(firstId);

                            //verificar se a classe existe
                            if (classe == null) {
                                signalError.show("A classe " + firstId + " não existe!");
                            }

                            methodDec = classe.findMethodDec(ident, false);
                            if (methodDec == null) {
                                signalError.show("O método " + ident + " não existe na classe " + firstId + "!");
                            } else if (methodDec.isIsPrivate()) {
                                signalError.show("O método " + ident + " não pode ser acessado pois é privado!");
                            }

                            return new MessageSendToVariable(null, methodDec, exprList, classe);

                        } else {
                            // retorne o objeto da ASA que representa Id "." Id
                            classe = symbolTable.getInGlobal(firstId);
                            if (classe == null) {
                                signalError.show("A classe " + firstId + " não existe!");
                            }

                            //talvez tenha q verificar pela currentClass
                            InstanceVariable instanceVariable = classe.findInstanceVariable(ident);
                            if (instanceVariable == null) {
                                signalError.show("A variável " + ident + " não foi declarada na classe" + firstId + " !");
                            }

                            return new VariableExpr(instanceVariable);
                        }
                    }
                }
                break;

            case THIS:
                /*
                 * Este 'case THIS:' trata os seguintes casos: 
                 * PrimaryExpr ::= 
                 *                 "this" | 
                 *                 "this" "." Id | 
                 *                 "this" "." Id "(" [ ExpressionList ] ")"  | 
                 *                 "this" "." Id "." Id "(" [ ExpressionList ] ")"
                 */
                lexer.nextToken();
                if (lexer.token != Symbol.DOT) {
                    // only 'this'
                    // retorne um objeto da ASA que representa 'this'
                    // confira se não estamos em um método estático
                    if (currentMethod.isIsStatic()) {
                        signalError.show("Não é possível utilizar 'this' em um método estático!");
                    }

                    return new MessageSendToSelf(null, currentClass, null, null);

                } else {
                    lexer.nextToken();
                    if (lexer.token != Symbol.IDENT) {
                        signalError.show("Identifier expected");
                    }

                    ident = lexer.getStringValue();

                    lexer.nextToken();
                    // já analisou "this" "." Id
                    if (lexer.token == Symbol.LEFTPAR) {
                        // "this" "." Id "(" [ ExpressionList ] ")"
			/*
                         * Confira se a classe corrente possui um método cujo nome é
                         * 'ident' e que pode tomar os parâmetros de ExpressionList
                         */

                        //talvez possa ser false, para não procurar na superclasse
                        methodDec = currentClass.findMethodDec(ident, true);
                        if (methodDec == null) {
                            signalError.show("O método " + ident + " não existe na classe " + currentClass.getName() + "!");
                        }

                        exprList = this.realParameters();
                        if ((exprList == null && methodDec.getFormalParamDec() != null)
                                || (exprList != null && methodDec.getFormalParamDec() == null)) {
                            signalError.show("O método " + ident + " não é compatível com os parâmetros enviados!");
                        } else {
                            //verificar se o tipo/ordem dos parametros passados corresponde ao esperado pelo método
                            //methodDec.getFormalParamDec();
                        }

                        return new MessageSendToSelf(null, currentClass, methodDec, exprList);
                    } else if (lexer.token == Symbol.DOT) {
                        // "this" "." Id "." Id "(" [ ExpressionList ] ")"
                        lexer.nextToken();
                        if (lexer.token != Symbol.IDENT) {
                            signalError.show("Identifier expected");
                        }

                        messageName = lexer.getStringValue();
                        lexer.nextToken();

                        InstanceVariable instanceVariable = currentClass.findInstanceVariable(ident);
                        if (instanceVariable == null) {
                            signalError.show("A variável " + ident + " não foi declarada na classe" + currentClass.getName() + " !");
                        }

                        methodDec = currentClass.findMethodDec(messageName, true);
                        if (methodDec == null) {
                            signalError.show("O método " + messageName + " não existe na classe " + currentClass.getName() + "!");
                        }

                        exprList = this.realParameters();
                        if ((exprList == null && methodDec.getFormalParamDec() != null)
                                || (exprList != null && methodDec.getFormalParamDec() == null)) {
                            signalError.show("O método " + messageName + " não é compatível com os parâmetros enviados!");
                        } else {
                            //verificar se o tipo/ordem dos parametros passados corresponde ao esperado pelo método
                            //methodDec.getFormalParamDec();
                        }

                        return new MessageSendToSelf(null, currentClass, methodDec, exprList);
                    } else {
                        // retorne o objeto da ASA que representa "this" "." Id
			/*
                         * confira se a classe corrente realmente possui uma
                         * variável de instância 'ident'
                         */
                        InstanceVariable instanceVariable = currentClass.findInstanceVariable(ident);
                        if (instanceVariable == null) {
                            signalError.show("A variável " + ident + " não foi declarada na classe" + currentClass.getName() + " !");
                        }

                        return new MessageSendToSelf(instanceVariable, currentClass, null, null);
                    }
                }
            default:
                signalError.show("Expression expected");
        }
        return null;
    }

    private LiteralInt literalInt() {

        LiteralInt e = null;

        // the number value is stored in lexer.getToken().value as an object of
        // Integer.
        // Method intValue returns that value as an value of type int.
        int value = lexer.getNumberValue();
        lexer.nextToken();
        return new LiteralInt(value);
    }

    private static boolean startExpr(Symbol token) {

        return token == Symbol.FALSE || token == Symbol.TRUE
                || token == Symbol.NOT || token == Symbol.THIS
                || token == Symbol.LITERALINT || token == Symbol.SUPER
                || token == Symbol.LEFTPAR || token == Symbol.NULL
                || token == Symbol.IDENT || token == Symbol.LITERALSTRING;

    }

    private SymbolTable symbolTable;
    private Lexer lexer;
    private SignalError signalError;
    private KraClass currentClass; //ponteiro para a classe corrente
    private MethodDec currentMethod; //ponteiro para o método corrente
    private int quantidadeWhile = 0;
}
