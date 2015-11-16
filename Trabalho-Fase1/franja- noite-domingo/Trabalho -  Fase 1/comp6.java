comp6

6 Compiler 6
This language supports variable2 declarations preceding the expression. A typical program would be
a = 1 b = 3 : (- (+ a 2) b)
The new grammar is:
Program ::= VarDecList ’:’ Expr
VarDecList ::= | VarDec VarDecList
VarDec ::= Letter ’=’ Number
Expr::= ’(’ oper Expr Expr ’)’ | Number | Letter
Oper ::= ’+’ | ’-’ | ’*’ | ’/’
Number ::= ’0’| ’1’ | ... | ’9’
Letter ::= ’A’ | ’B’| ... | ’Z’| ’a’| ’b’ | ... | ’z’
The first rule for VarDecList uses empty as the right-hand side. The first rule is that before |.
In this grammar, there is a clear need for a new AST class: Variable. A variable in this language
not only has a name but it also keeps a value. Therefore, the AST class for Variable should have
two instance variables:
public class Variable {
public Variable( char name, int value ) {
this.name = name;
this.value = value;
}
public void genC() {
System.out.println( "int " + name + " = " + value + ";" );
}
private char name;
private int value;
}
Method genC generates the code for the declaration of the variable.
The new class Compiler has methods for rules Program, VarDecList, VarDec, Expr, Oper, Number,
and Letter. VarDecList is a list of zero or more VarDec’s, which starts with a letter. Then
VarDecList should derive using the second rule,
VarDecList ::= VarDec VarDecList
while the next token is a letter. This is reflected in method varDecList:
private Vector varDecList() {
/* See how the repetition in the grammar reflects in the code. Since VarDec
always begin with a letter, if token is NOT a letter, then VarDecList is
empty and null is returned */
if ( ! Character.isLetter(token) )
return null;
else {
Vector arrayVariable = new Vector();
2Since the values of the variables cannot be modified, they are in fact constants.
37
while ( Character.isLetter(token) )
arrayVariable.addElement( varDec() );
return arrayVariable;
}
}
Objects of Vector are arrays with an undetermined number of elements. Objects of any class are
sequentially added to the array by method addElement. Method isLetter is a static method of class
Character and tests if its parameter is a letter.
Method program gets from VarDecList the vector with the variables and returns an object of
Program with the vector and the expression:
private Program program() {
Vector arrayVariable = varDecList();
if ( token != ’:’ ) {
error();
return null;
}
else {
nextToken();
Expr e = expr();
return new Program( arrayVariable, e );
}
}
Method varDec returns a Variable object containing the name and value of the variable:
private Variable varDec() {
char name = letter();
if ( token != ’=’ ) {
error();
return null;
}
else {
nextToken();
NumberExpr n = number();
return new Variable( name, n.getValue() );
}
}
Method number checks if the current token is a number:
private NumberExpr number() {
NumberExpr e = null;
if ( token >= ’0’ && token <= ’9’ ) {
e = new NumberExpr(token);
nextToken();
38
}
else
error();
return e;
}
Method letter tests if token is a letter:
private char letter() {
if ( ! Character.isLetter(token) ) {
error();
return ’\0’;
}
else {
char ch = token;
nextToken();
return ch;
}
}
Method expr now has to test if token is ’(’, a number, or a letter:
private Expr expr() {
if ( token == ’(’ ) {
nextToken();
char op = oper();
Expr e1 = expr();
Expr e2 = expr();
CompositeExpr ce = new CompositeExpr(e1, op, e2);
if ( token == ’)’ )
nextToken();
else
error();
return ce;
}
else
// note we test the token to decide which production to use
if ( Character.isDigit(token) )
return number();
else
return new VariableExpr(letter());
}
Note that if the expression is a variable, as in
a = 1 : a
method expr (second line from bottom to top) returns an object of VariableExpr, not Variable.
Class Variable represents declarations of variables, not the use of a variable in an expression. See the
code of classes Variable and VariableExpr (that follows). Method genC of VariableExpr generates
39
code for a variable in an expression, which is just the variable itself. Method genC of Variable
generates code for the declaration of the variable in language C, something like “int a = 1;”. The
return type of method expr is Expr and therefore should return, in expr, an object of a subclass of
Expr, which is abstract. Then we could not return an object of Variable. Class Variable does not
inherit from Expr. Even if it did, there would be the problem of method genC just exposed.
public class VariableExpr extends Expr {
public VariableExpr( char name ) {
this.name = name;
}
public void genC() {
System.out.print(name);
}
private char name;
}
Method Program::genC3 generates code for the main program in C, which will contain the variable
declarations and the expression:
package AST;
import java.util.*;
public class Program {
public Program( Vector arrayVariable, Expr expr ) {
this.arrayVariable = arrayVariable;
this.expr = expr;
}
public void genC() {
System.out.println("#include <stdio.h>\n");
System.out.println("void main() {");
if ( arrayVariable != null ) {
// generate code for the declaration of variables
Enumeration e = arrayVariable.elements();
while ( e.hasMoreElements() )
((Variable ) e.nextElement()).genC();
}
// generate code for the expression
System.out.print("printf(\"%d\\n\", ");
expr.genC();
System.out.println(" );\n}");
}
3Method genC of class Program.
40
private Vector arrayVariable;
private Expr expr;
}
In Program::genC, first e.nextElement() is cast to Variable. Then method Variable::genC is
called. This code is equivalent to
Enumeration e = arrayVariable.elements();
while ( e.hasMoreElements() ) {
Variable v = (Variable ) e.nextElement();
v.genC();
}
The generated code for the program
a = 1 b = 3 : (- (+ a 2) b)
would be
#include <stdio.h>
void main() {
int a = 1;
int b = 3;
printf("%d\n", ((a + 2) - b));
}
The complete compiler code is in Appendix A.