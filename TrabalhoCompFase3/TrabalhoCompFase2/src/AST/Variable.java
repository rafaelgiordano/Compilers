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
public class Variable {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
    private String name;
    private Expr expr;
    private Type type;

    public Variable(String name, Expr expr, Type type) {
            this.name = name;
            this.expr = expr;
            this.type = type;
    }
    
    //id [’[’ expr ’]’]
    public void genC(PW pw){
        pw.print(name);

        if (expr != null) {
            pw.print("[");
            expr.genC(pw);
            pw.print("]");
        }

    }
}
