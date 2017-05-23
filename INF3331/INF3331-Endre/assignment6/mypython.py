import sys, traceback, os, inspect
from feedline import feedline, get_namespace
from getchar import getchar

history = []
position = 0
cache = ""
call = 0

def ctrld(string):
    """Handle Ctrl+D input.

    Args:
        string (str): input, if any, already written into terminal.
    Returns:
        empty string

    Example usage:
    >>> print ctrld("any string")
    ""
    """
    if string:
        print "\nKeyboardusInterruptus"
        #reset history-position in case the input is from history
        position = len(history)
        #make an empty call to reset input-prompt
        sys.stdout.write(feedline(""))
        return ""
    else:
        print "\nkthxbye"
        exit()

def updown(direction, string):
    """Access the command history by using the up and down buttons

    Args:
        direction (str): the string that identifies a button as either up or down.
        string (str): input, if any, already written into terminal.
    Returns:
        command from history, as string.

    Example usage:
    >>> history = ["print 1"]
    >>> position = 1
    >>> print updown("[A", "")
    "print 1"
    """
    global position, history, cache
    #button up
    if direction == "[A":
        #cache current input for later retrieval
        if string not in history:
            cache = string
        #cycles history, if there is older commands to be accessed
        if position > 0:
            #fix position-index
            position -= 1
            #clear terminal line
            clear_line(string)
            #update to new string and print to terminal
            string = history[position]
            sys.stdout.write(history[position])
    #button down
    elif direction == "[B":
        #cycles history, if there is newer commands to be accessed
        if position < len(history)-1:
            #fix position index
            position += 1
            #clear terminal line
            clear_line(string)
            #update to new string and write to terminal
            string = history[position]
            sys.stdout.write(history[position])
        #no newer commands in history
        else:
            #same updates
            clear_line(string)
            position = len(history)
            #retrieves cache and prints to terminal
            string = cache
            sys.stdout.write(cache)
    #returns the selected command
    return string

def clear_line(string):
    """Overwrites the string with whitespace in terminal

    Args:
        string (str): input already in the terminal

    Example usage:
    >>> clear_line("print 2")
    "       "
    """
    for character in string:
        #backtrack-whitespace-backtrack
        sys.stdout.write("\b \b")

def magic_sys(string):
    """magic operator to pass commands to system

    Args:
        string (str): input from terminal
    Returns:
        empty string

    Example usage:
    >>> magic_sys("!echo 123")
    ""
    """
    os.system(string[1:])
    return ""

def magic_inspect(string):
    """magic operator for inspecting an object

    Args:
        string (str): input from terminal
    Raises:
        NameError
    Returns:
        empty string

    Example usage:
    >>> a = 12
    >>> magic_inspect(a)
    ""
    """
    try:
        namespace = get_namespace()
        print inspect.getdoc(eval(string[:-1], namespace))
    except:
        traceback.print_exc(file=sys.stdout)
    return ""

def magic_save(string):
    """magic operator to save input from session

    Args:
        string (str): input from terminal
    Returns:
        empty string
    Raises:
        IOError, UsageError
    Example usage:
    >>> magic_save("%save data.txt")
    ""
    """
    global history

    try:
        filename = string.split(" ")[1]
        file = open(filename, "w")
        for line in history:
            file.write(line+"\n")
        file.close()
    except IndexError:
        sys.stdout.write("UsageError: missing filename\n")
    except:
        traceback.print_exc(file=sys.stdout)
    return ""

def exit():
    """Exits the program
    """
    sys.exit(0)

if __name__ == "__main__":
    print "Welcome to mypython!"
    sys.stdout.write("in [{}]: ".format(call))
    string = ""

    while True:
        #captures and handles every character
        while True:
            char = getchar()

            #handles case of ctrl-d
            if char == "\x04":
                string = ctrld(string)
                sys.stdout.write("in [{}]: ".format(call))
                break

            #handles input of up and down arrows
            if char == "\x1b":
                string = updown(sys.stdin.read(2), string)
                break

            sys.stdout.write(char)
            
            if char in "\r\n":
                #force newline
                print ""

                #Empty cache in case history was involved
                cache = ""

                if string:
                    #magic commands passed to os
                    if string[0] == "!":
                        string = magic_sys(string)
                    #magic command to display docstring of object
                    elif string[-1] == "?":
                        string = magic_inspect(string)
                    #magic command to save all input lines for one interactive session
                    elif string[0:5] == "%save":
                        string = magic_save(string)
                    else:
                        result = feedline(string)
                        if result:
                            sys.stdout.write("out [{}]: ".format(call) + result)

                    call += 1

                #append to history
                if string and string not in history:
                    history.append(string)
                #update position for new length of history
                position = len(history)

                #empty buffer
                string = ""

                #new prompt for input
                sys.stdout.write("in [{}]: ".format(call))
                break

            string += char