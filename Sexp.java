/*
 *  Holds data strcutre for Sexpressions
 *  Either Lists or Atoms
 *  Created: 10/28
 *  Modified: 11/11
 */


//Sexp -> ATOM | '(' LIST
abstract class Sexp {
    //Visitor pattern, may not use?
    interface Visitor<R> {
        R visitAtom(Atom atom);
        R visitList(List list);
    }
    abstract <R> R accept(Visitor<R> visitor);
    @Override
    public
    abstract String toString();
    abstract String pretty();

    public static class Atom extends Sexp {
        //Basic structure
        String value;
        TokenType type;
        int line = 1;
        boolean callable = false;
        
        Atom(Token value) {
            this.value = value.lexeme;
            type = value.type;
        }

        public Atom(String value) {
            this(value, 1);
        }

        public Atom(String value, int line) {
            this.line = line;
            if (value.charAt(0) >= '0' && value.charAt(0) <= '9') {
                //make a double
                this.value = value;
                type = TokenType.NUMBER;
            }
            //else make a symbol
            else {
                this.value = value;
                type = TokenType.SYMBOL;
                if (value.equals("T"));
                    type = TokenType.TRUE;
            }
        }

        //visitor
        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitAtom(this);
        }

        @Override
        public String toString() {
            return value;
        }

        public String pretty() {
            return value;
        }  
    }

    public static class List extends Sexp {
        //Holds array of values,
        java.util.List<Sexp> values;

        //nil is an empty list
        List() {
            this.values = new java.util.ArrayList<Sexp>();
        }

        //conscell
        List(Sexp e1, Sexp e2) {
            this.values = new java.util.ArrayList<Sexp>();
            this.values.add(e1);
            this.values.add(e2);
        }

        List(java.util.List<Sexp> values) {
            this.values = values;
        }

        //Visitor
        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitList(this);
        }

        @Override
        public String toString() {
            String list = "";
            list += "(";

            for (Sexp s : values) {
                //if an empty list, print the null
                if (s instanceof List) {
                    List li = (List) s;
                    if (li.values.isEmpty()) {
                        list += "()";
                        continue;
                    }
                }
                //else
                list += s.toString();
            }
            return list + ")";
        }

        public String pretty() {

            String line = "(";
            Sexp.List curList = this;

            //print all atoms or start new list for real lists
            while (curList.values.size() != 0) {
                if (curList.values.get(0) instanceof Atom) {
                    line += curList.values.get(0).toString();
                }
                else {
                    line += ((Sexp.List)curList.values.get(0)).pretty();
                }
                curList = (Sexp.List) curList.values.get(1);
                //add spaces between but not at ends
                if (curList.values.size() != 0) line += " ";
            }

            line += ")";
            return line;
            
        }
    }

    //Equality function
    public boolean equals(Sexp expr) {
        //check same type
        if (this.getClass() != expr.getClass())
            return false;

        //if atoms, check 
        if (this instanceof Sexp.Atom) {
            return ((Sexp.Atom)this).value.equals(((Sexp.Atom)expr).value);
        }

        if (this instanceof Sexp.List) {
            boolean eq = true;
            int i = 0;
            for (Sexp value : ((Sexp.List)this).values) {
                if (!value.equals(((Sexp.List)expr).values.get(i))) {
                    eq = false;
                }
                i++;
            }
            return eq;

        }

        //not sure how to deal with lists yet, come back
        return false;
    }
}
