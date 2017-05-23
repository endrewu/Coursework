#!/usr/bin/env python

class SimpleString(object):
	"""
	This script takes a string-input and prints it in all caps.
	"""

	s = "No string defined by user"

	def getString(self):
		"""
		Prompts user for a string from terminal
		"""

		self.s = raw_input("Enter String value: ")

	def printString(self):
		"""
		Prints string in all caps
		"""

		print self.s.upper()