from subprocess import call
from week4 import retrieve

def test_retrieve():
	call(["wget", "http://www.islostarepeat.com"])

	assert retrieve("http://www.islostarepeat.com") == open("index.html", "r").read()