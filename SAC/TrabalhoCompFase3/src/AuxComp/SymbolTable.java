/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AuxComp;

/**
 *
 * @author home
 */
import java.util.*;

public class SymbolTable {
	private Hashtable<String, Object> globalTable;
	private Hashtable<String, Object> localTable;

	public SymbolTable() {
		globalTable = new Hashtable<String, Object>();
		localTable = new Hashtable<String, Object>();
	}

	public Object putInGlobal(String key, Object value) {
		return getGlobalTable().put(key, value);
	}

	public Object putInLocal(String key, Object value) {
		return localTable.put(key, value);
	}

	public Object getInLocal(Object key) {
		return localTable.get(key);
	}

	public Object getInGlobal(Object key) {
		return getGlobalTable().get(key);
	}

	public Object get(String key) {
		// returns the object corresponding to the key.
		Object result;
		if ((result = localTable.get(key)) != null) {
			// found local identifier
			return result;
		}
		else {
			// global identifier, if it is in globalTable
			return getGlobalTable().get(key);
		}
	}

	public void removeLocalIdent() {
		// remove all local identifiers from the table
		localTable.clear();
	}

	public Hashtable<String, Object> getGlobalTable() {
		return globalTable;
	}

	public Hashtable<String, Object> getLocalTable() {
		return localTable;
	}
}