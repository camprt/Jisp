/*
*   Stores all the tokens for jysp
*   Created: 10/25
*/

//Token data structure, based off of Crafting Interpreters
class Token {
    public TokenType type;
    String lexeme;
    Object literal;
    int line;

    //constructor
    Token(TokenType ty, String le, Object lit, int lin) {
        this.type = ty;
        this.lexeme = le;
        this.literal = lit;
        this.line = lin;
    }

    public String toString() {
        return lexeme;
    }
}