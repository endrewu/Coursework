import matplotlib.pyplot as plt
import matplotlib.colors as colors

def heat_equation(t0, t1, dt, n, m, u, f, nu, verbose=None):
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
		print "Starting pure python implementation..."

	v = [[0 for x in range(m)] for x in range(n)] #v=copy(u)?
	
	for time in range(t0, int(t1/dt)):
		if verbose > 1:
			print "Calculating time-step for time...", time/10.0
		for i in range(1,n-1):
			for j in range(1,m-1):
				v[i][j] = u[i][j] + dt*(nu*u[i-1][j] + nu*u[i][j-1] - 4*nu*u[i][j] + nu*u[i][j+1] + nu*u[i+1][j] + f[i][j])		
		u, v = v, u

	if verbose:
		print "Returning results..."
	return u

if __name__ == '__main__':
	#Test-run
	n = 100
	m = 50
	f = [[1 for x in range(m)] for x in range(n)]
	u = [[0 for x in range(m)] for x in range(n)]
	results = heat_equation(0, 1000, 0.1, n, m, u, f, 1.0)
	
	image = plt.imshow(results, cmap=plt.get_cmap('gray'))
	plt.colorbar()
	plt.show()


	#Is working now