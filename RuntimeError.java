/*
 * Class to implement runtime errors & pass to the main
 * Based off Crafting Interpreters
 * Created: 11/11
 */

 class RuntimeError extends RuntimeException {
    Sexp expr;

    RuntimeError(Sexp expr, String msg) {
        super(msg);
        this.expr = expr;
    }

    public String print() {
        return "Runtime error: " + super.getMessage() + ", on expression " + expr.pretty() + ".";
    }
 }