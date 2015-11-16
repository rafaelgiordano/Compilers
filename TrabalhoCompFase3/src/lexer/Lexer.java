/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexer;

import lexer.Symbol;
import java.util.*;

public class Lexer {

	// current token
	public Symbol token;
	private String stringValue;
	private int numberValue;
	private char charValue;
	private Symbol lastToken;
        private int beforeLastTokenPos;
	private int tokenPos;
        // input[lastTokenPos] is the last character of the last token found
	private int lastTokenPos;
	
	
	// program given as input - source code
	private char[] input;

	// number of current line. Starts with 1
	private int lineNumber;

	public Lexer(char[] input) {
		this.input = input;
		// add an end-of-file label to make it easy to do the lexer
		input[input.length - 1] = '\0';
		// number of the current line
		lineNumber = 1;
		tokenPos = 0;
                beforeLastTokenPos = 0;
		inputToLowerCase();
	}

	// contains the keywords
	static private Hashtable<String, Symbol> keywordsTable;

	// this code will be executed only once for each program execution
	static {
		keywordsTable = new Hashtable<String, Symbol>();
		keywordsTable.put("program", Symbol.PROGRAM);
		keywordsTable.put("var", Symbol.VAR);
		keywordsTable.put("of", Symbol.OF);
		keywordsTable.put("or", Symbol.OR);
		keywordsTable.put("and", Symbol.AND);
		keywordsTable.put("mod", Symbol.MOD);
		keywordsTable.put("div", Symbol.DIV);
		keywordsTable.put("begin", Symbol.BEGIN);
		keywordsTable.put("end", Symbol.END);
		keywordsTable.put("if", Symbol.IF);
		keywordsTable.put("then", Symbol.THEN);
		keywordsTable.put("else", Symbol.ELSE);
		keywordsTable.put("endif", Symbol.ENDIF);
		keywordsTable.put("while", Symbol.WHILE);
		keywordsTable.put("do", Symbol.DO);
		keywordsTable.put("endwhile", Symbol.ENDWHILE);
		keywordsTable.put("procedure", Symbol.PROCEDURE);
		keywordsTable.put("function", Symbol.FUNCTION);
		keywordsTable.put("return", Symbol.RETURN);
		keywordsTable.put("read", Symbol.READ);
		keywordsTable.put("write", Symbol.WRITE);
		keywordsTable.put("writeln", Symbol.WRITELN);
		keywordsTable.put("integer", Symbol.INTEGER);
		keywordsTable.put("real", Symbol.REAL);
		keywordsTable.put("char", Symbol.CHAR);
		keywordsTable.put("array", Symbol.ARRAY);
		keywordsTable.put("string", Symbol.STRING);
		keywordsTable.put("not", Symbol.NOT);
	}

	public void nextToken() {
		char ch;
//                System.out.println("\n\nNEXT TOKEN");

		while ((ch = input[tokenPos]) == ' ' || ch == '\r' || ch == '\t'
				|| ch == '\n') {
			// count the number of lines
			if (ch == '\n')
				lineNumber++;
			tokenPos++;
//                        System.out.println("\twhile... " + ch);
		}
//                System.out.println("inicializou... " + ch);
		if (ch == '\0')
			token = Symbol.EOF;
		else if (input[tokenPos] == '{') {
//                    System.out.println("\tcomment... " + ch);
			// comment found
			while (input[tokenPos] != '\0' && input[tokenPos] != '\n'
					&& input[tokenPos] != '}')
				tokenPos++;

			if (input[tokenPos] != '}') {
				error("'}' esperado.\n");
			}
			tokenPos++;
			nextToken();
		}
		else {
                        if (lastToken == Symbol.APOSTROPHE) {
				// get a string
				StringBuffer string = new StringBuffer();
				while (input[tokenPos] != '\'') {
					string.append(input[tokenPos]);
					tokenPos++;

					if (input[tokenPos] == '\0') {
						error("''' esperado.\n");
					}
				}
				token = Symbol.APOSTROPHE;
                                lastToken = Symbol.ASSIGNSTRING;
				stringValue = string.toString();
			}
                    
                        else if (Character.isLetter(ch)) {
//                                System.out.println("\tletter... " + ch);
				// get a keyword or identifier
				StringBuffer ident = new StringBuffer();
				while (Character.isLetter(input[tokenPos])
						|| Character.isDigit(input[tokenPos])) {
					ident.append(input[tokenPos]);
					tokenPos++;
				}
				stringValue = ident.toString();
				// if ident is in the list of keywords, it is a keyword !
				Symbol value = keywordsTable.get(stringValue);

				if (value == null) {
					token = Symbol.IDENT; // identifier
//                                        System.out.println("\t eh ident");
				}
				else {
					token = value; // keyword
//                                        System.out.println("\t eh keyword");
				}
			}
			else if (Character.isDigit(ch)) {
//                                System.out.println("\tdigit... " + ch);
				// get a number
				StringBuffer number = new StringBuffer();
				while (Character.isDigit(input[tokenPos])) {
					number.append(input[tokenPos]);
					tokenPos++;
				}
				token = Symbol.NUMBER;
				try {
					numberValue = Integer.valueOf(number.toString()).intValue();
				}
				catch (NumberFormatException e) {
					error("Number out of limits");
				}
			}
			else {
//                                System.out.println("\tswitch... " + ch);
				tokenPos++;
				switch (ch) {
					case '+':
						token = Symbol.PLUS;
						break;
					case '-':
						token = Symbol.MINUS;
						break;
					case '*':
						token = Symbol.MULT;
						break;
					case '/':
						token = Symbol.DIVIDER;
						break;
					case '<':
						if (input[tokenPos] == '=') {
							tokenPos++;
							token = Symbol.LE;
						}
						else if (input[tokenPos] == '>') {
							tokenPos++;
							token = Symbol.DIF;
						}
						else
							token = Symbol.LT;
						break;
					case '>':
						if (input[tokenPos] == '=') {
							tokenPos++;
							token = Symbol.GE;
						}
						else
							token = Symbol.GT;
						break;
					case ':':
						if (input[tokenPos] == '=') {
							tokenPos++;
							token = Symbol.ASSIGN;
						}
						else
							token = Symbol.COLON;
						break;
					case '(':
						token = Symbol.LEFTPAR;
						break;
					case ')':
						token = Symbol.RIGHTPAR;
						break;
					case ',':
						token = Symbol.COMMA;
						break;
					case ';':
						token = Symbol.SEMICOLON;
						break;
					case '.':
						if (input[tokenPos] == '.') {
							tokenPos++;
							token = Symbol.DOUBLEDOT;
						}
						else {
							token = Symbol.DOT;
						}
						break;
					case '\'':
						token = Symbol.APOSTROPHE;
						break;
					case '{':
						token = Symbol.CURLYLEFTBRACE;
						break;
					case '}':
						token = Symbol.CURLYRIGHTBRACE;
						break;
					case '[':
						token = Symbol.LEFTSQBRACKET;
						break;
					case ']':
						token = Symbol.RIGHTSQBRACKET;
						break;
					case '=':
						token = Symbol.EQUAL;
						break;
					default:
						error("Invalid Character: '" + ch + "'");
				}
			}
		}
                beforeLastTokenPos = lastTokenPos;
		lastTokenPos = tokenPos - 1;
                
                
		if (lastToken != Symbol.ASSIGNSTRING) {
			lastToken = token;
		}
		else if (token != Symbol.APOSTROPHE) {
			lastToken = token;
		}

		
	}

	// return the line number of the last token got with getToken()
	public int getLineNumber() {
		return lineNumber;
	}
        
        public int getLineNumberBeforeLastToken() {
            return getLineNumber( beforeLastTokenPos );
        }
        private int getLineNumber(int index) {
            // return the line number in which the character input[index] is
            int i, n, size;
            n = 1;
            i = 0;
            size = input.length;
            while (i < size && i < index) {
                    if (input[i] == '\n')
                            n++;
                    i++;
            }
            return n;
	}


	public String getCurrentLine() {
            int i = lastTokenPos;
                if ( i == 0 )
                    i = 1;
                else
                if ( i >= input.length )
                    i = input.length;
                StringBuffer line = new StringBuffer();
                // go to the beginning of the line
                while ( i >= 1 && input[i] != '\n' ){
                    i--;
                }
                if ( input[i] == '\n' )
                    i++;
                // go to the end of the line putting it in variable line
                while ( input[i] != '\0' && input[i] != '\n' && input[i] != '\r' ) {
                    line.append( input[i] );
                    i++;
                }
                return line.toString();
            }

             public String getLineBeforeLastToken() {
            return getLine(beforeLastTokenPos);
        }

        private String getLine( int index ) {
            // get the line that contains input[index]. Assume input[index] is at a token, not
            // a white space or newline

            int i = index;
            if ( i == 0 ) 
              i = 1; 
            else 
              if ( i >= input.length )
                i = input.length;

            StringBuffer line = new StringBuffer();
              // go to the beginning of the line
            while ( i >= 1 && input[i] != '\n' )
              i--;
            if ( input[i] == '\n' )
              i++;
              // go to the end of the line putting it in variable line
            while ( input[i] != '\0' && input[i] != '\n' && input[i] != '\r' ) {
                line.append( input[i] );
                i++;
            }
            return line.toString();
        }


	

	public String getStringValue() {
		return stringValue;
	}

	public int getNumberValue() {
		return numberValue;
	}

	public char getCharValue() {
		return charValue;
	}

	public void error(String errorCause) {
		if (tokenPos == 0) {
			tokenPos = 1;
		}
		else if (tokenPos >= input.length) {
			tokenPos = input.length;
		}

		String strError = "\nErro na linha " + getLineNumber() + ":\n\t"
				+ getCurrentLine() + "\n";
		strError += errorCause;

		throw new RuntimeException(strError);
	}

	public int getTokenPos() {
		return tokenPos;
	}

	// conversão de toda a entrada para letras minúsculas
	private void inputToLowerCase() {
		int i;

		for (i = 0; i < input.length; i++) {
			if ((Character.isUpperCase(input[i]))
					&& (Character.isLetter(input[i]))) {
				input[i] = Character.toLowerCase(input[i]);
			}
		}
	}
}
