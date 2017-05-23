#!/usr/bin/env python

from math import pi, sqrt

class FlexCircle(object):
	def __init__(self, radius):
		self.radius = radius

	def set_radius(self, r):
		if r < 0:
			print "An error occured, a circle can not have a negative radius"
			self.radius = 0
			return
		self._radius = r
		self._area = pi*self.radius*self.radius
		self._perimeter = 2*pi*self.radius
		
	def set_area(self, a):
		if a < 0:
			print "An error occured, a circle can not have a negative area"
			self.radius = 0
			return
		self._area = a
		self._radius = sqrt(self.area/pi)
		self._perimeter = 2*pi*self.radius

	def set_perimeter(self, p):
		if p < 0:
			print "An error occured, a circle can not have a negative perimeter"
			self.radius = 0
			return
		self._perimeter = p
		self._radius = self.perimeter/(2*pi)
		self._area = pi*self.radius*self.radius

	def get_radius(self):
		return self._radius

	def get_area(self):
		return self._area

	def get_perimeter(self):
		return self._perimeter

	radius = property(fget = get_radius, fset = set_radius)
	area = property(fget = get_area, fset = set_area)
	perimeter = property(fget = get_perimeter, fset = set_perimeter)