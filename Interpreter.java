/*
 * Evaluates the given sexpression
 * Created: 11/5
 * Modified: 11/11
 */

import java.util.List;
import java.util.ArrayList;

public class Interpreter {

    static final Sexp T = new Sexp.Atom("T");
    static final Sexp NIL = new Sexp.List();

    public boolean hadRuntimeError = false;

    List<Environment> environments;
    Environment curEnv;

    Interpreter() {
        environments = new ArrayList<Environment>();
        environments.add(new Environment(this));
        curEnv = environments.get(0);
    }

    String interpret(List<Sexp> exprs) {
        try {
            String output = "";
            for (Sexp expr : exprs) {
                output += evaluate(expr).pretty();
                output += "\n";
            }
            return output;
        } catch (RuntimeError error) {
            hadRuntimeError = true;
            return error.print();
        }
    }

    Sexp evaluate(Sexp expr) {
        //Atom evaluations
        if (expr instanceof Sexp.Atom) {
            Sexp.Atom atom = (Sexp.Atom) expr;

            //if a number, just retern the value as new atom
            if (atom.type == TokenType.NUMBER || atom.type == TokenType.STRING)
                return expr;

            //if a symbol, lookup
            if (atom.type == TokenType.SYMBOL) {
                Sexp value = curEnv.lookup(atom);
                return value;            
            }
        }
        
        //List evaluations
        else if (expr instanceof Sexp.List) {
            //cast to make things easier
            Sexp.List list = (Sexp.List) expr;

            //check if nil
            if (!isTruthy(list))
                return expr;
            
            //else known list, check for function call
            Sexp.Atom name = (Sexp.Atom) car(list);

            //check if is a user defined function or a symbol
            if (curEnv.isFunction(name)) {
                try {
                    return call(expr);
                }
                catch (RuntimeError error) {
                    error.print();
                }
            } 

            switch (name.value) {
                //all built ins
                case "quote": 
                    return car(cdr(expr));
                case "set":
                    return set(second(expr), evaluate(third(expr)));
                case "cons":
                    return cons(evaluate(second(expr)), evaluate(third(expr)));
                case "car":
                    return car(evaluate(second(expr)));
                case "cdr":
                    return cdr(evaluate(second(expr)));
                case "second":
                    return second(second(expr));
                case "third":
                    return third(second(expr));
                case "nil?":
                    return isNil(second(expr));
                case "symbol?":
                    return isSymbol(second(expr));
                case "number?":
                    return isNumber(second(expr));
                case "list?":
                    return isList(expr);
                case "add":
                    return add(evaluate(second(expr)), evaluate(third(expr)));
                case "sub":
                    return sub(evaluate(second(expr)), evaluate(third(expr)));
                case "mul":
                    return mul(evaluate(second(expr)), evaluate(third(expr)));
                case "div":
                    return div(evaluate(second(expr)), evaluate(third(expr)));
                case "mod":
                    return mod(evaluate(second(expr)), evaluate(third(expr)));
                case "lt":
                    return lt(evaluate(second(expr)), evaluate(third(expr)));
                case "lte":
                    return lte(evaluate(second(expr)), evaluate(third(expr)));
                case "gt":
                    return gt(evaluate(second(expr)), evaluate(third(expr)));
                case "gte":
                    return gte(evaluate(second(expr)), evaluate(third(expr)));
                case "eq":
                    return eq(evaluate(second(expr)), evaluate(third(expr)));
                case "not":
                    return not(evaluate(second(expr)));
                case "and":
                    return and(evaluate(second(expr)), evaluate(third(expr)));
                case "or":
                    return or(evaluate(second(expr)), evaluate(third(expr)));
                case "if":
                    return ifStmt(second(expr), third(expr), fourth(expr));
                case "cond":
                    try{
                        return cond((Sexp.List) cdr(expr));
                    }
                    catch (RuntimeError error) {
                        Jysp.runtimeerror(error);
                    }
                case "define":  //fun name         args         body
                    return define(second(expr), third(expr), fourth(expr));
                default:
                    break;
            }
        }
        return expr;
    }

    public void print(Sexp expr) {
        System.out.println(expr.pretty());
    } 

    /*PREDICATES*/
    //Checks if Sexp is nil, returns the lisp value
    Sexp isNil(Sexp expr) {
        //if atom -> not nil, return false(nil)
        if (expr instanceof Sexp.Atom) 
            return NIL;

        //else is list, check value size
        //constain vals? -> not nil
        if (((Sexp.List)expr).values.size() != 0) 
            return NIL; 
        
        //else is nil, return true
        return T;
    }

    //Is expr a symbol?
    Sexp isSymbol(Sexp expr) {
        //if a list, not a symbol
        if (expr instanceof Sexp.List) 
            return NIL;
    
        //check type
        if (((Sexp.Atom)expr).type == TokenType.SYMBOL) 
            return T;

        //else return nil;
        return NIL;
    }

    //Number? predicate
    Sexp isNumber(Sexp expr) {
        //if a list, not a number
        if (expr instanceof Sexp.List) 
            return NIL;
    
        //check type
        if (((Sexp.Atom)expr).type == TokenType.NUMBER) 
            return T;

        //else return nil;
        return NIL;
    }

    //List? predicate
    Sexp isList(Sexp expr) {
        //return Truth atom if a list
        if (expr instanceof Sexp.List)
            return T;
        
        //else is not a list, return nil
        return NIL;
    }

    boolean isTruthy(Sexp expr) {
        if (expr instanceof Sexp.List && ((Sexp.List) expr).values.size() == 0) 
            return false; //nil
        return true;
    }


    /*ARITHMETIC & LOGICAL OPERATIONS*/
    //returnssnumberic value of the atom
    int valueOf(Sexp.Atom expr) {
        //assumes its a number, we'll see what happens w/ strings?
        return Integer.parseInt(expr.value);
    }

    //Add values of 2 sexps;
    Sexp.Atom add(Sexp e1, Sexp e2) {
        int val1 = valueOf((Sexp.Atom)e1);
        int val2 = valueOf((Sexp.Atom)e2);

        return new Sexp.Atom(Integer.toString(val1 + val2));
    }

    //Subtraction
    Sexp.Atom sub(Sexp e1, Sexp e2) {
        int val1 = valueOf((Sexp.Atom)e1);
        int val2 = valueOf((Sexp.Atom)e2);

        return new Sexp.Atom(Integer.toString(val1 - val2));
    }

    //Multiplication
    Sexp.Atom mul(Sexp e1, Sexp e2) {
        int val1 = valueOf((Sexp.Atom)e1);
        int val2 = valueOf((Sexp.Atom)e2);

        return new Sexp.Atom(Integer.toString(val1 * val2));
    }

    //Multiplication
    Sexp.Atom div(Sexp e1, Sexp e2) {
        int val1 = valueOf((Sexp.Atom)e1);
        int val2 = valueOf((Sexp.Atom)e2);

        return new Sexp.Atom(Integer.toString(val1 / val2));
    }

    //Modulus
    Sexp.Atom mod(Sexp e1, Sexp e2) {
        int val1 = valueOf((Sexp.Atom)e1);
        int val2 = valueOf((Sexp.Atom)e2);

        return new Sexp.Atom(Integer.toString(val1 % val2));
    }

    //Left < Right
    Sexp lt(Sexp left, Sexp right) {
        int l = valueOf((Sexp.Atom)left);
        int r = valueOf((Sexp.Atom)right);

        //True
        if (l < r) 
            return T;
        //false
        return NIL;
    }

    //Left <= Right
    Sexp lte(Sexp left, Sexp right) {
        int l = valueOf((Sexp.Atom)left);
        int r = valueOf((Sexp.Atom)right);

        //True
        if (l <= r) 
            return T;
        //false
        return NIL;
    }

    //Left > Right
    Sexp gt(Sexp left, Sexp right) {
        int l = valueOf((Sexp.Atom)left);
        int r = valueOf((Sexp.Atom)right);

        //True
        if (l > r) 
            return T;
        //false
        return NIL;
    }

    //Left >= Right
    Sexp gte(Sexp left, Sexp right) {
        int l = valueOf((Sexp.Atom)left);
        int r = valueOf((Sexp.Atom)right);

        //True
        if (l >= r) 
            return T;
        //false
        return NIL;
    }

    //==
    Sexp eq(Sexp e1, Sexp e2) {
        int val1 = valueOf((Sexp.Atom)e1);
        int val2 = valueOf((Sexp.Atom)e2);

        //True
        if (val1 == val2) 
            return T;
        //false
        return NIL;
    }

    //! (not)
    Sexp not(Sexp expr) {

        //if true, return false
        if (expr instanceof Sexp.List && ((Sexp.List)expr).values.size() == 0) {
            return T;
        }

        //all other cases are truthy, so return false;
        return NIL;
    }

    //Logical and
    Sexp and(Sexp e1, Sexp e2) {
        if (!isTruthy(e1)) return NIL;
        if (!isTruthy(e2)) return NIL;
        //both need to pass to return true
        return T;
    }

    //Logical or
    Sexp or(Sexp e1, Sexp e2) {
        if (isTruthy(e1)) return T;
        if (isTruthy(e2)) return T;
        //neither true -> false;
        return NIL;
    }


    /** Other built ins **/
    Sexp set(Sexp name, Sexp value) {
        //add name = 2nd, value = 3rd, val evaluated first
        if (name instanceof Sexp.Atom && ((Sexp.Atom)name).type == TokenType.SYMBOL) {
            curEnv.set(name, value);
            return curEnv.lookup(name);
        }
        throw new RuntimeError(name, "Not a symbol");
    }

    //Cons 2 elements into a new list
    Sexp.List cons(Sexp e1, Sexp e2) {
        return new Sexp.List(e1, e2);
    }

    //retrieve first element
    Sexp car(Sexp expr) {
        //check its a real list
        if (expr instanceof Sexp.List && !((Sexp.List)expr).values.isEmpty()) {
            return ((Sexp.List) expr).values.get(0);
        }
        //return nil for atoms
        return NIL;
    }

    //Get rest of list
    Sexp cdr(Sexp expr) {

        //check its a real list
        if (expr instanceof Sexp.List && !((Sexp.List)expr).values.isEmpty()) {
            //if following conscell structure, should retrieve all values to end
            return ((Sexp.List) expr).values.get(1);
        }

        //else return nil
        return NIL;
    }

    //Cadr
    Sexp second(Sexp expr) {
        return car(cdr(expr));
    }

    //Caddr
    Sexp third(Sexp expr) {
        return car(cdr(cdr(expr)));
    }

    //Cadddr
    Sexp fourth(Sexp expr) {
        return car(cdr(cdr(cdr(expr))));
    }

    //If e1 (e2) else (e3)
    Sexp ifStmt(Sexp condition, Sexp block, Sexp otherwise) {
        if (isTruthy(evaluate(condition))) {
            return evaluate(block);
        }
        else {
            return evaluate(otherwise);
        }
    }

    //Cond (e1 e2) (e3 e4)
    Sexp cond(Sexp.List args) {
        //base case if none reached
        if (args.values.size() == 0) {
            throw new RuntimeError(args, "Cond structure: (cond (e1 e2) ...(T e3))");
        }
        
        //extract pair
        Sexp condition = car(car(args));
        Sexp block = second(car(args));

        if (isTruthy(evaluate(condition))) {
            return evaluate(block);
        }
        //else move to next pair
        return cond((Sexp.List)cdr(args));
    }

    //Function definition
    Sexp define(Sexp name, Sexp args, Sexp body) {
        //check name is an atom
        if (!(name instanceof Sexp.Atom) || ((Sexp.Atom) name).type != TokenType.SYMBOL) {
            throw new RuntimeError(name, "Function names must be symbols");
        }
        Sexp.Atom funcName = (Sexp.Atom) name;
        funcName.callable = true;

        //uses Lisp 1 - all funcs stored with regular variables
        set(funcName, new Sexp.List(args, body));
        return T;
    }

    //code to execute a function call
    Sexp call(Sexp function) {
        //check the function exists
        Sexp.Atom funcName = (Sexp.Atom)car(function);
        if (curEnv.lookup(funcName) == funcName) {
            throw new RuntimeError(funcName, "Unrecognized function name");
        }
        //get stored info
        Sexp params = car(curEnv.lookup(funcName));
        Sexp body = cdr(curEnv.lookup(funcName));

        //add args to environment
        Environment argsValues = new Environment(this, curEnv);
        environments.add(argsValues);
        curEnv = argsValues;

        //Set values
        Sexp args = cdr(function);
        while (isTruthy(params)) {
            set(car(params), (evaluate(car(args))));
            params = cdr(params);
            args = cdr(args);
        }
        //make sure no args lost
        if (isTruthy(args)) {
            throw new RuntimeError(args, "Mismatching parameter and argument counts.");
        }

        //call eval, all varibles should be in stack or globals
        Sexp solution = evaluate(body);

        //pop env
        environments.remove(environments.size() - 1);
        curEnv = curEnv.enclosing;

        return solution;
    }
}
