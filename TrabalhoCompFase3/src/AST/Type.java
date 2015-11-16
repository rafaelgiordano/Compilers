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

// type ::= stdtype | arraytype
abstract public class Type {
	abstract public String getType();
        abstract public boolean isArray();
	abstract public void genC();
}
