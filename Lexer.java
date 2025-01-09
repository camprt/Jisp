/*
*   Takes string input and creates tokens
*   Based on Crafting Interpreters
*   Creater: 10/25
*   Modified: 11/5
*/

import java.util.ArrayList;
import java.util.List;

class Lexer {
    //fields
    private String source;
    private List<Token> tokens = new ArrayList<>();
    
    private int start = 0;
    private int curr = 0;
    private int line = 1;

    //Constructor
    Lexer(String src) {
        this.source = src;
    }

    //scan the string
    public List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = curr;
            scanToken();
        }

        tokens.add(new Token(TokenType.EOF, "", null, line));
        return tokens;
    }

    //determine the token type
    private void scanToken() {
        char c = advance();
        switch (c) {
            case '(': 
                addToken(TokenType.LEFT_PAREN); break;
            case ')': addToken(TokenType.RIGHT_PAREN); break;
            
            //Truth
            case 'T': addToken(TokenType.TRUE); break;

            //Whitespace
            case ' ':
            case '\r':
            case '\t':
                break;
            case '\n':
                line++;
                break;

            //Strings
            case '"': string(); break;

            //Comments ;;
            case ';':
                if (match(';')) {
                    while (lookahead() != '\n' && !isAtEnd()) {
                        advance();
                    }
                }
                else {
                    symbol();
                }

            default:
                //Number?
                if (isDigit(c)) {
                    number();
                }
                else if (isAlpha(c)) {
                    symbol();
                }
                else {
                    //error(line, "unexpected char.");
                }
                break;
        }
    }

    //Creates a string token
    private void string() {
        //keep going until closing quote
        while (lookahead() != '"' && !isAtEnd()) {
            if (lookahead() == '\n') line++;
            advance();
        }

        if (isAtEnd()) {
            //error(line, "Unterminated string.")
            return;
        }

        //consume closing quote
        advance();

        String value = source.substring(start + 1, curr - 1);
        addToken(TokenType.STRING, value);
    }

    //Creates a number token
    private void number() {
        //consume all digits
        while (isDigit(lookahead())) advance();

        //consume decimal if there
        if (lookahead() == '.' && isDigit(lookTwoAhead())) {
            advance();

            //get digits after decimal
            while (isDigit(lookahead())) advance();
        }

        addToken(TokenType.NUMBER, Double.parseDouble(source.substring(start, curr)));
        
    }

    //Creates a symbol token
    private void symbol() {
        while(isAlpha(lookahead()) || isDigit(lookahead())) advance();

        //room to add reserved words
        //create hash map, see scanner chapter crafting interpreters

        addToken(TokenType.SYMBOL);
    }

    //Add unitialized token
    private void addToken(TokenType type) {
        addToken(type, null);
    }

    //Create new token, add to list
    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, curr);
        tokens.add(new Token(type, text, literal, line));
    }

    private boolean isAtEnd() {
        return curr >= source.length();
    }

    //Move to next token
    private char advance() {
        return source.charAt(curr++);
    }

    //Check next char type
    private boolean match(char want) {
        if (isAtEnd()) return false;
        if (source.charAt(curr) != want) return false;

        curr++;
        return true;
    }

    //Check next char
    private char lookahead() {
        if (isAtEnd()) return '\0';
        return source.charAt(curr);
    }

    //Check 2 ahead
    private char lookTwoAhead() {
        if (curr + 1 >= source.length()) return '\0';
        return source.charAt(curr + 1);
    }

    //Checks if a number char
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    //Checks if a letter char
    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_' || c == '?';
    }
}