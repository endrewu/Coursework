#encoding: utf-8

from week4 import lookup

def test_lookup():
	assert lookup('Hannestad') == ['http://www.yr.no/place/Norway/Østfold/Sarpsborg/Hannestad/forecast.xml']