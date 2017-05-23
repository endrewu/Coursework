from numpy import *
import matplotlib.pyplot as plt
import matplotlib.colors as colors
import argparse, timeit, os.path
import cPickle as pickler

from heat_equation import heat_equation
from heat_equation_numpy import heat_equation_numpy
from heat_equation_weave import heat_equation_weave

#Define some standard values in case user gives no input
#2D list/array u will be created later depending on what solver is chosen
#2D list/array f will later be created based on heat_source-variable
t0 = 0
t1 = 1000
dt = 0.1
n = 100
m = 50
nu = 1.0
heat_source = 1.0
verbose = None

parser = argparse.ArgumentParser()
parser.add_argument("-t0", "--start_time", help="Define a start-time for the simulation, default value = 0", type=int)
parser.add_argument("-t1", "--end_time", help="Define an end-time for the simulation, default value = 1000", type=int)
parser.add_argument("-dt", "--timestep", help="Define a timestep for the simulation, default value = 0.1", type=float)
parser.add_argument("-m", "--horizontal_dim", help="Define horizontal dimension for the simulation, default value = 100", type=int)
parser.add_argument("-n", "--vertical_dim", help="Define vertical dimension for the simulation, default value = 50", type=int)
parser.add_argument("-nu", "--diffusivity", help="Define thermal diffusivity for the simulation, default value = 1.0", type=float)
parser.add_argument("-f", "--heat_source", help="Define constant heat source for the simulation, default value = 1.0", type=float)
parser.add_argument("-o", "--output", help="The result of the simulation will be pickled to the file-name specified")
parser.add_argument("-i", "--input", help="Will try to unpickle from the specified pickle, and use this for as initial temperature")
parser.add_argument("-img", help="Will save the completed simulation as an image", action="store_true")
parser.add_argument("-v", "--verbose", help="Toggle verbosity. 1 means some verbosity, 2 means full verbosity." \
											"The main difference between the two is that verbosity 2 will print every time-stepped loop", type=int, choices=[1,2])
parser.add_argument("-ti", "--timeit", help="Turns on timed mode, when the program is running in this mode no output will be generated", action="store_true")
parser.add_argument("--solver", help="Choose what implementation to use for the simulation", choices=["python", "numpy", "C"])

args = parser.parse_args()

if args.verbose:
	verbose = args.verbose

#Imports input file to u, and derives dimensions of array 
if args.input:
	if verbose:
		print "Loading input-file..."
	in_file = args.input
	if os.path.isfile(in_file):
		u = pickler.load(open(in_file, "rb"))
		if type(u) == list:
			n=len(u)
			m=len(u[0])
		else: #assumes type(u) == np.ndarray
			n,m = u.shape
#if input file is not given, check values for m and n
else:
	if args.vertical_dim:
		n = args.vertical_dim
	if args.horizontal_dim:
		m = args.horizontal_dim

if args.start_time:
	t0 = args.start_time
if args.end_time:
	t1 = args.end_time
if args.timestep:
	dt = args.timestep
if args.diffusivity:
	nu = args.diffusivity
if args.heat_source:
	heat_source = args.heat_source

f = full((m*n), heat_source).reshape(n,m)
if not args.input:
	u = zeros((m*n)).reshape(n,m)

#Call the solver, defaults to weave since it is fastest
if args.solver == "python": #python solver if specified
	if args.timeit:
		timer = timeit.repeat(lambda: heat_equation(t0, t1, dt, n, m, u.tolist(), f.tolist(), nu, verbose), repeat=3, number=1)
		print min(timer)/2
	else:
		results = heat_equation(t0, t1, dt, n, m, u, f, nu, verbose)
elif args.solver == "numpy":
	if args.timeit:
		timer = timeit.repeat(lambda: heat_equation_numpy(t0, t1, dt, n, m, u, f, nu, verbose), repeat=9, number=1)
		print min(timer)/5
	else:
		results = heat_equation_numpy(t0, t1, dt, n, m, u, f, nu, verbose)
else:
	if args.timeit:
		timer = timeit.repeat(lambda: heat_equation_weave(t0, t1, dt, n, m, u, f, nu, verbose), repeat=9, number=1)
		print min(timer)
	else:
		results = heat_equation_weave(t0, t1, dt, n, m, u, f, nu, verbose)

#Checks if output should be generated before finishing up
if args.output:
	if verbose:
		print "Saving file..."
	out_file = args.output
	pickler.dump(results, open(out_file, "wb"), 2)

#Generate image and colorbar
if not args.timeit:
	image = plt.imshow(results, cmap=plt.get_cmap('gray'))
	plt.colorbar()
	#Saves image if defined, if not it defaults to renderin image now
	if args.img:
		if verbose:
			print "Saving image..."
		plt.savefig("image.PNG")
	else:
		plt.show()