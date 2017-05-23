from scipy import weave
from numpy import *
import matplotlib.pyplot as plt
import matplotlib.colors as colors

def heat_equation_weave(t0, t1, dt, n, m, u, f, nu, verbose=None):
	"""
	t0: starting time
	t1: ending time
	dt: time step
	n, m: dimensions of rectangle
	u: space- and time-dependent temperature within the material
	f: heat source
	nu: material-specific thermal diffusivity
	"""
	v = zeros((n*m)).reshape(n,m)
	
	code = r"""
		int i, j;
		double t;

		for(t = t0; t < t1; t+=dt) {
			for (i = 1; i < n-1; i++) {
				for (j = 1; j < m-1; j++) {
					v(i,j) = u(i,j) + dt*(nu*u(i-1,j) + nu*u(i,j-1) - 4*nu*u(i,j) + nu*u(i,j+1) + nu*u(i+1,j) + f(i,j));
				}
			}
				
			for (i = 0; i < n; i++) {
				for (j = 0; j < m; j++) {
					u(i,j) = v(i,j);
				}
			}
		}
	"""
	#TODO: 'u, v = v, u' isn't working, need to find some other way of switching the pointers
	if verbose:
		print "Calling weave.inline"
	weave.inline(code, ['t0', 't1', 'dt', 'n', 'm', 'u', 'f', 'nu', 'v'], type_converters=weave.converters.blitz, compiler='gcc')

	if verbose:
		print "Returning results..."
	return u

if __name__ == '__main__':
	#Test-run
	n = 100
	m = 50
	f = ones((n*m))
	u = zeros((n*m))
	f.shape = u.shape = (n,m)
	results = heat_equation_weave(0, 1000, 0.1, n, m, u, f, 1.0)
	
	image = plt.imshow(results, cmap=plt.get_cmap('gray'))
	plt.colorbar()
	plt.show()