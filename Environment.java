/*
 * Stores symbol-value pairs to be retrieved by the environment
 * Made a class so can be stackable
 * Created: 11/5
 */
 
public class Environment {
    //stores the matches
    Sexp.List names;
    Sexp.List values;
    Interpreter itpr;
    Environment enclosing;

    Environment(Interpreter itpr) {
        this(itpr, null);
    }

    Environment(Interpreter itpr, Environment env) {
        names = new Sexp.List();
        values = new Sexp.List();
        this.itpr = itpr;
        enclosing = env;
    }

    //adds name and value to beginning of lists
    Sexp set(Sexp name, Sexp value) {
        names = new Sexp.List(name, names);
        values = new Sexp.List(value, values);
        return new Sexp.Atom("T");
    }

    //Returns the value associated with a given sexp
    Sexp lookup(Sexp name) {
        Sexp curName = itpr.car(names);
        Sexp cdrNames = itpr.cdr(names);
        Sexp curValue = itpr.car(values);
        Sexp cdrValues = itpr.cdr(values);

        //go until match found
        while (true) {
            //break when hit nil, just return the symbol
            if (!itpr.isTruthy(curName))
                break;
            
            //return matching val when found
            if (curName.equals(name)) {
                return curValue;
            }
            
            //else increment
            curName = itpr.car(cdrNames);
            cdrNames = itpr.cdr(cdrNames);
            curValue = itpr.car(cdrValues);
            cdrValues = itpr.cdr(cdrValues);
        }

        if (this.enclosing != null) {
            return this.enclosing.lookup(name);
        }

        return name;
    }

    //returns whether given name is a callable function
    boolean isFunction(Sexp.Atom name) {
        Sexp curName = itpr.car(names);
        Sexp cdrNames = itpr.cdr(names);

        //go until match found
        while (true) {
            //break when hit nil, just return the symbol
            if (!itpr.isTruthy(curName))
                break;
            
            //return matching val when found
            if (curName.equals(name)) {
                return ((Sexp.Atom) curName).callable;
            }
            
            //else increment
            curName = itpr.car(cdrNames);
            cdrNames = itpr.cdr(cdrNames);
        }

        if (this.enclosing != null) {
            return this.enclosing.isFunction(name);
        }

        //else not found
        return false;
    }

}

