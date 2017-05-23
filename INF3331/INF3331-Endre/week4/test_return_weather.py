from week4 import return_weather, lookup

def test_return_weather():
	hannestad = return_weather(lookup('Hannestad'))['Hannestad']
	assert int(hannestad[0][5]) < 50 and int(hannestad[0][5]) > -50