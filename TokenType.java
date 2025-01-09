/*
*   Holds data structure containing possible token types
*   Created 10/28
*/

//Data Structure
public enum TokenType {
    LEFT_PAREN, RIGHT_PAREN, 
    
    //Atom
    SYMBOL, NUMBER, STRING,

    //T/F
    TRUE,

    //built in funcs
    EOF
}