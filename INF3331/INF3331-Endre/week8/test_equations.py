from numpy import *
from math import pi
from heat_equation_numpy import heat_equation_numpy

n = 100
m = 50
nu = 1.0
analytic_u = zeros((n*m)).reshape(n,m)

f=[[0 for x in range(m)] for x in range(n)]

for i in range(n):
	for j in range(m):
		f[i][j] = nu*((2*pi/(m-1))**2 + (2*pi/(n-1))**2) *sin(2*pi/(n-1)*i)*sin(2*pi/(m-1)*j)
		analytic_u[i][j] = sin(2*pi/(n-1)*i)*sin(2*pi/(m-1)*j)

u=zeros((n*m)).reshape(n,m)
numpy_u = heat_equation_numpy(0, 1000, 0.1, n, m, u, asarray(f), nu)
err = (abs(numpy_u - analytic_u)).max()

assert err < 0.0012 