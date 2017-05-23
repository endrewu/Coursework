from numpy import *
import matplotlib.pyplot as plt
import matplotlib.colors as colors
from scitools.numpyutils import seq

def heat_equation_numpy(t0, t1, dt, n, m, u, f, nu, verbose=None):
	"""
	t0: starting time
	t1: ending time
	dt: time step
	n, m: dimensions of rectangle
	u: space- and time-dependent temperature within the material
	f: heat source
	nu: material-specific thermal diffusivity
	"""
	if verbose:
		print "Starting numpy implementation..."

	v = zeros((n*m)).reshape(n,m)

	for time in seq(t0,t1,dt):
		if verbose > 1:
			print "Calculating time-step for time...", time
		v[1:-1,1:-1] = u[1:-1,1:-1] + dt*(nu*u[:-2,1:-1] + nu*u[1:-1,:-2] - 4*nu*u[1:-1,1:-1] + nu*u[1:-1,2:] + nu*u[2:,1:-1] + f[1:-1,1:-1])
		u, v = v, u

	if verbose:
		print "Returning results..."
	return u

if __name__ == '__main__':
	#Test-run
	n = 100
	m = 50
	f = ones((n*m)).reshape(n,m)
	u = zeros((n*m)).reshape(n,m)
	results = heat_equation_numpy(0, 1000, 0.1, n, m, u, f, 1.0)
	
	image = plt.imshow(results, cmap=plt.get_cmap('gray'))
	plt.colorbar()
	plt.show()

	#Virker na