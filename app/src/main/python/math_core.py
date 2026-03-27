import sympy as sp
from sympy.logic.boolalg import truth_table

def calculate(subject, expr, param=""):
    try:
        if subject == "calculus":
            x = sp.Symbol('x')
            equation = sp.sympify(expr)
            if param == "derive":
                return str(sp.diff(equation, x))
            elif param == "integrate":
                return str(sp.integrate(equation, x))
                
        elif subject == "discrete":
            equation = sp.sympify(expr)
            table = truth_table(equation, equation.free_symbols)
            res = [f"{list(t[0])} -> {t[1]}" for t in table]
            return "\n".join(res)
            
        elif subject == "algebra":
            rows = expr.split(';')
            mat_data = [[sp.sympify(v) for v in r.split(',')] for r in rows]
            M = sp.Matrix(mat_data)
            if param == "det":
                return str(M.det())
            elif param == "inv":
                return str(M.inv())
                
        elif subject == "probability":
            n, k = map(int, expr.split(','))
            if param == "comb":
                return str(sp.binomial(n, k))
            elif param == "perm":
                return str(sp.factorial(n) / sp.factorial(n-k))
                
        return "Unknown Operation"
    except Exception as e:
        return f"Error: {str(e)}"
