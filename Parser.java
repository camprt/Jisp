/*
*   Parses given tokens into the grammar structure
*   Created: 10/25
*   Modified: 11/11
*/

import java.util.ArrayList;
import java.util.List;

class Parser {
    private static class ParseError extends RuntimeException {
        Token token;

        ParseError(Token t, String msg) {
            super(msg);
            token = t;
        }

        String print() {
            String errorLoc;
            if (token.type == TokenType.EOF) {
                errorLoc = " at end";
            }
            else {
                errorLoc = " at '" + token.lexeme + "'";
            }
            return "[line: " + token.line +"] Error" + errorLoc + ": " + super.getMessage();
        }
    }

    public boolean hadError = false;
    private List<Token> tokens;
    private int curr = 0;

    //Constructor
    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<Sexp> parse() {
        try {
            List<Sexp> exprs = new ArrayList<Sexp>();
            while (!isAtEnd()) {
                exprs.add(sexpression());
            }
            return exprs;
        } catch (ParseError error) {
            error.print();
            return null;
        }

    }

    /* LISP GRAMMAR */
    //sexp -> atom | '(' list
    private Sexp sexpression() {
        if (match(TokenType.LEFT_PAREN)) {
            Sexp list = list();
            consume(TokenType.RIGHT_PAREN, "Missing closing ')'.");
            return list;
        }
        else return atom();
    }

    //atom -> NUMBER | STRING | SYMBOL
    private Sexp atom() {
        advance();
        return new Sexp.Atom(tokens.get(curr - 1));
    }

    //list -> ')' | sexp list
    private Sexp list() {
        if (lookahead().type == TokenType.EOF) {
            error(lookahead(), "Expect a closing ')' for lists.");
        }
        //get all the values
        List<Sexp> values = new ArrayList<>();
        if (lookahead().type != TokenType.RIGHT_PAREN) {
            values.add(sexpression()); //car
            values.add(list()); //cdr, auto adds nil at the end
        }
        
        return new Sexp.List(values);
    }


    /* HELPERS */
    //Checks if curr token matches any of the given types
    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            //and consume it
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return lookahead().type == type;
    }

    private Token advance() {
        if (!isAtEnd()) this.curr++;
        return previous();
    }

    private boolean isAtEnd() {
        return lookahead().type == TokenType.EOF;
    }

    private Token lookahead() {
        return tokens.get(curr);
    }

    private Token previous() {
        return tokens.get(curr - 1);
    }

    //error catching
    private ParseError error(Token token, String msg) {
        hadError = true;
        Jysp.error(token, msg);
        throw new ParseError(token, msg);
    }

    private Token consume(TokenType type, String msg) {
        if (check(type)) return advance();
        throw error(lookahead(), msg);
    }

}