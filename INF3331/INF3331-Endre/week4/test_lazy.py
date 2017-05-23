from lazy import Lazy
from week4 import retrieve, lookup

def func(x):
	print "Actual execution"
	return x+1


def test_Lazy(capsys):
	#Test that lazy_func prints string only on first call
	lazy_func = Lazy(func)
	assert lazy_func(4) == 5
	out, err = capsys.readouterr()
	assert out == 'Actual execution\n'
	assert lazy_func(4) == 5
	out, err = capsys.readouterr()
	assert out == ''

	#Missing test for expired time stamp.
	#(it does work, though, try setting the computer-clock forward manually)