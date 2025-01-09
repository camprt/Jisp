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


public class Test {
    static boolean hadError = false;
    static boolean hadRuntimeError = false;
    static Interpreter itpr = new Interpreter();

    public static void main(String[] args) throws IOException { 
        String test;
        //Sprint 2 Tests
        test = run("(nil? ())");
        if (test.equals("T\n")) System.out.println("Nil? Test:\t PASS");
        else {
            System.out.println("Nil? Test: \t FAIL");
            System.out.println("\t(isNil ()) returned " + test +", expected T.");
        }

        test = run("(symbol? symbol)");
        if (test.equals("T\n")) System.out.println("Symbol? Test:\t PASS");
        else {
            System.out.println("Symbol? Test: \t FAIL");
            System.out.println("\t(isSymbol symbol) returned " + test +", expected T.");
        }

        test = run("(number? 100)");
        if (test.equals("T\n")) System.out.println("Number? Test:\t PASS");
        else {
            System.out.println("Number? Test: \t FAIL");
            System.out.println("\t(isNumber 100) returned " + test +", expected T.");
        }

        test = run("(list? (a b c))");
        if (test.equals("T\n")) System.out.println("List? Test:\t PASS");
        else {
            System.out.println("List? Test: \t FAIL");
            System.out.println("\t(isList (a b c)) returned " + test +", expected T.");
        }

        test = run("(cons a (cons b (cons c ())))");
        if (test.equals("(a b c)\n")) System.out.println("Cons Test:\t PASS");
        else {
            System.out.println("Cons Test: \t FAIL");
            System.out.println("\t(cons a (cons b (cons c ()))) returned " + test +", expected (a b c).");
        }

        test = run("(car (1 2 3 4))");
        if (test.equals("1\n")) System.out.println("Car Test:\t PASS");
        else {
            System.out.println("Car Test: \t FAIL");
            System.out.println("\t(car (1 2 3 4)) returned " + test +", expected 1.");
        }

        test = run("(cdr (a b (c d)))");
        if (test.equals("(b (c d))\n")) System.out.println("Cdr Test:\t PASS");
        else {
            System.out.println("Cdr Test: \t FAIL");
            System.out.println("\t(cdr (a b (c d))) returned " + test +", expected (b (c d)).");
        }

        //Sprint 3 tests
        test = run("(add 10 5)");
        if (test.equals("15\n")) System.out.println("Add Test:\t PASS");
        else {
            System.out.println("Add Test: \t FAIL");
            System.out.println("\t(add 10 5) returned " + test +", expected 15.");
        }

        test = run("(sub 36 10)");
        if (test.equals("26\n")) System.out.println("Sub Test:\t PASS");
        else {
            System.out.println("Sub Test: \t FAIL");
            System.out.println("\t(sub 36 10) returned " + test +", expected 26.");
        }

        test = run("(div 25 5)");
        if (test.equals("5\n")) System.out.println("Div Test:\t PASS");
        else {
            System.out.println("Div Test: \t FAIL");
            System.out.println("\t(div 25 5) returned " + test +", expected 5.");
        }

        test = run("(mul 25 5)");
        if (test.equals("125\n")) System.out.println("Mul Test:\t PASS");
        else {
            System.out.println("Mul Test: \t FAIL");
            System.out.println("\t(mul 25 5) returned " + test +", expected 125.");
        }

        test = run("(eq 25 5)");
        if (test.equals("()\n")) System.out.println("Eq Fail Test:\t PASS");
        else {
            System.out.println("Eq fail Test: \t FAIL");
            System.out.println("\t(eq 25 5) returned " + test +", expected ().");
        }

        test = run("(eq 3 3)");
        if (test.equals("T\n")) System.out.println("Eq Test:\t PASS");
        else {
            System.out.println("Div Test: \t FAIL");
            System.out.println("\t(eq 3 3) returned " + test +", expected T.");
        }

        test = run("(eq (add 1 2) (div 9 3))");
        if (test.equals("T\n")) System.out.println("Nest Test:\t PASS");
        else {
            System.out.println("Nest Test: \t FAIL");
            System.out.println("\t(eq (add 1 2) (div 9 3)) returned " + test +", expected T.");
        }

        test = run("(lte 9 10)");
        if (test.equals("T\n")) System.out.println("LTE Test:\t PASS");
        else {
            System.out.println("LTE Test: \t FAIL");
            System.out.println("\t(lte 9 10) returned " + test +", expected T.");
        }

        test = run("(gt 9 10)");
        if (test.equals("()\n")) System.out.println("GT Test:\t PASS");
        else {
            System.out.println("GT Test: \t FAIL");
            System.out.println("\t(gt 9 10) returned " + test +", expected ().");
        }

        //Sprint 4 tests
        test = run("(set a 10)\na");
        if (test.equals("10\n10\n")) System.out.println("Set Test:\t PASS");
        else {
            System.out.println("Set Test: \t FAIL");
            System.out.println("\t(set a 10); a returned " + test +", expected 10/10.");
        }

        test = run("(quote (car (a b c)))");
        if (test.equals("(car (a b c))\n")) System.out.println("Quote Test:\t PASS");
        else {
            System.out.println("Quote Test: \t FAIL");
            System.out.println("\t(quote (car (a b c))); a returned " + test +", expected (car (a b c)).");
        }

        //Sprint 5 tests
        test = run("(and T ())");
        if (test.equals("()\n")) System.out.println("And Test:\t PASS");
        else {
            System.out.println("And Test: \t FAIL");
            System.out.println("\t(and T ()) returned " + test +", expected ().");
        }

        test = run("(or T ())");
        if (test.equals("T\n")) System.out.println("Or Test:\t PASS");
        else {
            System.out.println("Or Test: \t FAIL");
            System.out.println("\t(or T ()) returned " + test +", expected T.");
        }

        test = run("(if T 1)");
        if (test.equals("1\n")) System.out.println("IfTrue Test:\t PASS");
        else {
            System.out.println("IfTrue Test: \t FAIL");
            System.out.println("\t(if T 1) returned " + test +", expected 1.");
        }

        test = run("(if (eq 1 2) 2 3)");
        if (test.equals("3\n")) System.out.println("IfFalse Test:\t PASS");
        else {
            System.out.println("IfFalse Test: \t FAIL");
            System.out.println("\t(if (eq 1 2) 2 3) returned " + test +", expected 3.");
        }

        test = run("(cond ((eq 1 2) 2) ((eq 1 1) 1))");
        if (test.equals("1\n")) System.out.println("Cond Test:\t PASS");
        else {
            System.out.println("Cond Test: \t FAIL");
            System.out.println("\t(cond ((eq 1 2) 2) ((eq 1 1) 1)) returned " + test +", expected 1.");
        }

        //Sprint 6 tests
        test = runFile("tests/fizzbuzz.yisp");
        if (test.equals("T\nT\nT\n(\"fizzbuzz\" \"\" \"\" \"fizz\" \"\" \"buzz\" \"fizz\" \"\" \"\" \"fizz\")\n")) System.out.println("FizzBuzz:\t PASS");
        else {
            System.out.println("FizzBuzz: FAIL");
            System.out.println("fizzbuzz returned " + test + ", expected T\nT\nT\n(\"fizzbuzz\" \"\" \"\" \"fizz\" \"\" \"buzz\" \"fizz\" \"\" \"\" \"fizz\")");
        }

        test = runFile("tests/userfunction.yisp");
        if (test.equals("T\n\"hello!\"\nT\n(1 2 3 4)\nT\n49\n")) System.out.println("UserFunc Test:\t PASS");
        else {
            System.out.println("UserFunc Test: FAIL");
            System.out.println("userfunctions returned " + test + ", expect T\n\"hello!\"\nT\n(1 2 3 4)\nT\n49\n");
        }

        test = runFile("tests/fib.yisp");
        if (test.equals("T\n55\n13\n17711\n")) System.out.println("Fib Test:\t PASS");
        else {
            System.out.println("Fib Test: FAIL");
            System.out.println("fib(10, 7, 22) returned " + test + ", expect T\n55\n13\n17711\n");
        }

        test = runFile("tests/factorial.yisp");
        if (test.equals("T\n120\n")) System.out.println("Factorial Test:\t PASS");
        else {
            System.out.println("Factorial Test: FAIL");
            System.out.println("Fact(5) returned " + test + ", expect T\n120\n");
        }

        //Error tests
        test = run("(add 1 3");
        if (test.equals("ParseError")) System.out.println("ParseError Test:\t PASS");
        else {
            System.out.println("ParseError Test: FAIL");
            System.out.println("(add 1 3 return " + test + ", expected ParseError");
        }

        test = run("(define (a b) () (add 1 2))");
        if (test.equals("RuntimeError")) System.out.println("RuntimeError Test:\t PASS");
        else {
            System.out.println("RuntimeError Test: FAIL");
            System.out.println("(define (a b) () (add 1 2) returned " + test + ", expected RuntimeError");
        }
    }

    //runs from file input
    public static String runFile(String filepath) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(filepath));
        String output = run(new String(bytes, Charset.defaultCharset()));
        if (hadError) System.exit(65);
        if (hadRuntimeError) System.exit(70);
        return output;
    }

    //runs the repl
    public static void runRepl() throws IOException {
        //get the input
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
    public static String run(String src) {        
        Lexer lexer = new Lexer(src);               //read
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);         //read
        List<Sexp> exprs = parser.parse();
        
        if (parser.hadError) return "ParseError";

        //Sprint 5
        String output = itpr.interpret(exprs);

        if (itpr.hadRuntimeError) return "RuntimeError";
        
        return output;
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
