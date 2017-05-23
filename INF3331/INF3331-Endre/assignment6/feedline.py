import sys, traceback
from StringIO import StringIO

namespace = vars().copy()

def get_namespace():
    return namespace

def feedline(string):
    """Handles the evaluation and execution of code, and magic functions.

    Args:
        string (str): the input from user
    Returns:
        evaluation of input and the next prompt for input
    Raises:
        IndexError, ArithmeticError, NameError, SyntaxError, TypeError (and pretty much any type of error that can be generated)

    Example usage:
    >>> print feedline("print 1")
    1
    """
    if string == "exit":
        sys.exit(0)

    # Swap stdout with a StringIO instance
    oldio, sys.stdout = sys.stdout, StringIO()

    if string:
        #eval input, if it does not work try exec
        try:
            sys.stdout.write(str(eval(string, namespace)) +"\n")
        except:
            try:
                exec(string, namespace)
            except:
                traceback.print_exc(file=sys.stdout)

    # Get stdout buffer
    out = sys.stdout.getvalue()    
    # Reset stdout
    sys.stdout = oldio

    # Print out captured stdout
    return out