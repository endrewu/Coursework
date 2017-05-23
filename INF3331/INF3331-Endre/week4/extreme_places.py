import sys, time
from week4 import lookup, return_weather

def extreme_places():
    dateToday = time.strftime("%Y-%m-%d", time.localtime(time.time())) #today's date, yyyy-mm-dd
    timeToday = dateToday+'T13:00:00'
    dateTomorrow = time.strftime("%Y-%m-%d", time.localtime(time.time()+86400)) #tomorrow's date, yyyy-mm-dd
    timeTomorrow = dateTomorrow+'T13:00:00'

    forecast_urls = lookup('')
    weather_data = return_weather(forecast_urls)

    hottest_name = None
    hottest_value = -sys.maxint-1
    coldest_name = None
    coldest_value = sys.maxint-1

    for key in weather_data:
        for line in weather_data[key]:
            if line[0] < timeToday and timeToday < line[1]:
                temp = line[5]
                if temp > hottest_value:
                	hottest_value = temp
                	hottest_name = key
                if temp < coldest_value:
                	coldest_value = temp
                	coldest_name = key
                break
            elif line[0] < timeTomorrow and timeTomorrow < line[1]:
            	temp = line[5]
            	if temp > hottest_value:
                	hottest_value = temp
                	hottest_name = key
                if temp < coldest_value:
                	coldest_value = temp
                	coldest_name = key
                break

    print 'Hottest place is {0} at {1} degrees celsius'.format(hottest_name, hottest_value)
    print 'Coldest place is {0} at {1} degrees celsius'.format(coldest_name, coldest_value)

if __name__ == '__main__':
	extreme_places()