#!/usr/bin/env python

import sys, os.path

for arg in sys.argv[1:]:
    if os.path.isfile(arg):
        f = open(arg, 'r')
        i = 0
        for line in f:
            for word in line.strip().split(' '):
            	if word:
            		i += 1
        p = arg+":"
        print p, i
        f.close()