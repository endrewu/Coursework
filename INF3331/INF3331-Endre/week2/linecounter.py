#!/usr/bin/env python

import sys, os.path

for arg in sys.argv[1:]:
    if os.path.isfile(arg):
        f = open(arg, 'r')
        i = 0
        for line in f:
            i += 1
        p = arg+":"
        print p, i
        f.close()