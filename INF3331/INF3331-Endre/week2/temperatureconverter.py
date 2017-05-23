#!/usr/bin/env python

import sys

f = float(sys.argv[1])
c = (f-32)/1.8

print "{0:.1f} Fahrenheit is equal to {1:.1f} Celsius.".format(f, c)