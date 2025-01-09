/*
*   main runner file
*   Created: 10/25
*   Modified: 11/11
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class Jysp {
    static boolean hadError = false;
    static boolean hadRuntimeError = false;
    static Interpreter itpr = new Interpreter();

    public static void main(String[] args) throws IOException { 
        if (args.length > 1) {
            System.out.println("Usage: jysp [script].");
            System.exit(64);
        }
        else if (args.length == 1) {
            runFile(args[0]);
        }
        else {
            runRepl();
        }
        
    }

    //runs from file input
    public static void runFile(String filepath) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(filepath));
        run(new String(bytes, Charset.defaultCharset()));
        if (hadError) System.exit(65);
        if (hadRuntimeError) System.exit(70);
    }

    //runs the repl
    public static void runRepl() throws IOException {
        //get the input
        System.out.println("Running repl...");
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        while (true) {
            System.out.print("> ");
            String line = reader.readLine();
            if (line == null) break;
            run(line);
            hadError = false;
            hadRuntimeError = false;
        }
    }

    //runs the string
    public static void run(String src) {
        Lexer lexer = new Lexer(src);               //read
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);         //read
        List<Sexp> exprs = parser.parse();
        
        if (hadError) return;

        //Sprint 5
        System.out.print(itpr.interpret(exprs));

        if (hadRuntimeError) return;
    }

    //error handling
    static void error(Token token, String msg) {
        hadError = true;
        String errorLoc;
        if (token.type == TokenType.EOF) {
            errorLoc = " at end";
        }
        else {
            errorLoc = " at '" + token.lexeme + "'";
        }
        System.out.println("[line: " + token.line +"] Error" + errorLoc + ": " + msg);
    }
    
    static void runtimeerror(RuntimeError error) {
        hadRuntimeError = true;
        System.out.println("Runtime error: " + error.getMessage() + " on expr '" + error.expr.toString() + "'");
    }
    
}
