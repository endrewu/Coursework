#!usr/bin/env python
#encoding: utf-8

import cPickle as pickler
import os.path, time

class Lazy:
    """
    Simple class for memoization, supports timestamping of retrieved data to ensure outdated
    memoizations get refreshed
    """
    def __init__(self, func, timestamp=None):
        self.func = func
        self.buffer = {}
        self.timestamp = timestamp
        if os.path.isfile("data.p"):
            self.buffer = pickler.load(open("data.p", "rb"))
            

    def __call__(self, arg):
        if arg in self.buffer:
            if self.buffer[arg][0] < time.time()-21600: #21600 being the amount of seconds in 6 hours
                print 'Updating buffer'
                del self.buffer[arg]
            else:
                return self.buffer[arg][1]
        retval = self.func(arg)
        if retval:                      #Only saves to buffer if it did indeed find anything
            if self.timestamp == None:
                self.buffer[arg] = (time.time(), retval)
            else:
                self.buffer[arg] = (self.timestamp, retval) #This didn't work some reason, I was unable to create a static timestamp for the second test for 4.4
        return retval

    def pickle(self):
        pickler.dump(self.buffer, open("data.p", "wb"), 2)