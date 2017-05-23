from week4 import retWeather, lookup

def test_retWeather():
	hannestad = retWeather(lookup('Hannestad'))['Hannestad']
	assert int(hannestad[0][5]) < 50 and int(hannestad[0][5]) > -50