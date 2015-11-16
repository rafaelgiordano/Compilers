/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

/**
 *
 * @author home
 */
public class FunctionCall extends ProcFunction{
    
    private Function function;
    private ExprList exprList;

    public FunctionCall(Function function, ExprList exprList) {
            this.function = function;
            this.exprList = exprList;
    }

    public void genC(PW pw) {
        System.out.print(function.getName() + "(");

        if (exprList != null) {
            exprList.genC(pw);
        }
        System.out.print(")");
    }
    
}
