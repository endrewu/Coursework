#!/usr/bin/env python

f = open('data.log', 'r')
results = {}

for line in f:
	if "CPU" in line:
		l = line.strip().split(' ')
		k = l[1]
		v = float(l[-1])
		if k in results:
			results[k].append(v)
		else:
			results[k] = [v]

for key in results:
	print "Test name:", key
	print "CPU time: {0:6.1f}s (min)".format(min(results[key]))
	print "{0:16.1f}s (avg)".format(sum(results[key])/len(results[key]))
	print "{0:16.1f}s (max) \n".format(max(results[key]))

f.close()