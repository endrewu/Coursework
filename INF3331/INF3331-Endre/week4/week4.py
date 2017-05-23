#!/usr/bin/env python
#encoding: utf-8

import urllib2, re, IPython, time
from lazy import Lazy

def retrieve(url): 
    """Downloads and returns content of url as string"""
    try:
        page = urllib2.urlopen(url)
        retval = page.read()
        page.close()
        return retval
    except urllib2.HTTPError:
        #exception
        return
    except urllib2.URLError:
        #exception
        return

def lookup(location):
    """
    Given an empty string will return list of URLs to all XML files.
    Given a string with valid location name will:
        -Check if input contains any wildcard characters (*) and convert these into a regex-compatible wildcard
        -Look for XML files for that location, if found returned as list, if not will try to match kommune and fylke
        -If none of the above, return an empty list
    """

    noreg = lazyRetrieve('http://fil.nrk.no/yr/viktigestader/noreg.txt')

    if not noreg:
        print 'Could not download file noreg.txt'
        return
    
    if not location: #If no string given, returns all links til xml
        return dupe_handler(re.findall(r".*(http.*?forecast.xml)", noreg))

    location = re.sub(r'\*', '\D*', location) #replace all asterix in location with regex .* to match wildcards

    stadnamn = r"\d+\t"+location+r"\t\d+\t.*(http.*?forecast.xml)" #location for stadnamn
    res = re.findall(stadnamn, noreg, re.I)

    if not res:
        kommune = r"\t"+location+r"\t.*\t\d+\.\d+\t\d+\.\d+.*(http.*?forecast.xml)" #location for kommune
        res = re.findall(kommune, noreg, re.I)
    if not res:
        fylke = r"\t"+location+r"\t\d+\.\d+\t\d+\.\d+.*(http.*?forecast.xml)" #location for fylke
        res = re.findall(fylke, noreg, re.I)

    return dupe_handler(res)

def dupe_handler(unhandled):
    """
    Removes dupes from a list (check Øvre Neiden), but doesn't mess with ordering of list (check 'Trondheim')
    """
    seen = set()
    seen_add = seen.add
    return [x for x in unhandled if not (x in seen or seen_add(x))]

def return_weather(forecast_urls):
    """
    Given a list of forecast.xml-links, will download them one by one. Regex will get all the necessary data 
    and return it all as a dictionary with the name of the place as the keys
    """
    data = {}

    for forecast in forecast_urls[:100]: #Make sure only to download 100 xmls at a time
        localdata = lazyRetrieve(forecast)

        if localdata:
            location = re.findall(r"<location>\r?\n\s*<name>(.+)<", localdata)[0]

            weather_re = re.compile(r"""
                (?x)                     #set verbose
                <time\ from=\"(.{19})\"  #time_from, matches 19 characters between a set of ". Will not trigger on shorter timestamps
                \ to=\"(.{19})           #time_to, matches ending timestamp
                (?:.|\n|\r)*?            #non-greedy discards characters and new-lines
                name=\" ([^\"]*)         #summary, matches between pair of "
                (?:.|\n|\r)*?            #non-greedy discards characters and new-lines
                value=\" ([^\"]*)        #precipitation
                (?:.|\r|\n)*?            #non-greedy discards characters and new-lines
                mps=\" ([^\"]*)          #wind_speed
                (?:.|\r|\n)*?            #non-greedy discards characters and new-lines
                value=\" ([-]?[^\"]*)    #potentially negative temperatures
                """)

            retval = re.findall(weather_re, localdata)
            if retval:
                data[location] = retval

    return data 

def weather_update(location, hour, minute):
    """
    Given the name of a location (or empty string) and the hour and minute, will retrieve the weather data and
    print a simple forecast for all locations found.
    """
    dateToday = time.strftime("%Y-%m-%d", time.localtime(time.time())) #today's date, yyyy-mm-dd
    timeToday = dateToday+'T'+str(hour)+':'+str(minute)+':00'
    dateTomorrow = time.strftime("%Y-%m-%d", time.localtime(time.time()+86400)) #tomorrow's date, yyyy-mm-dd
    timeTomorrow = dateTomorrow+'T'+str(hour)+':'+str(minute)+':00'

    forecast_urls = lookup(location)
    weather_data = return_weather(forecast_urls)

    first = True

    for key in weather_data: #key = name of place, value = list of all the weather data
        for line in weather_data[key]:
            if line[0] < timeToday and timeToday < line[1]: #index 0 and 1 being the timestamps
                if first:
                    print '{0}, {1}:{2:02d}'.format(dateToday, hour, minute)
                    first = False
                print '{0}: {1}, rain: {2}, mm, wind: {3} mps, temp: {4} deg C'.format(key, line[2], line[3], line[4], line[5])
                break
            if line[0] < timeTomorrow and timeTomorrow < line[1]:
                if first:
                    print '{0}, {1}:{2:02d}'.format(dateTomorrow, hour, minute)
                    first = False
                print '{0}: {1}, rain: {2}, mm, wind: {3} mps, temp: {4} deg C'.format(key, line[2], line[3], line[4], line[5])
                break

    lazyRetrieve.pickle() #when everything is printed, pickles to results found

lazyRetrieve = Lazy(retrieve)

if __name__ == '__main__':
    #Example of working script
    weather_update('Idd*', 13, 00)