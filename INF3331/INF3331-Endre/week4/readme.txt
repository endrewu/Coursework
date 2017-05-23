Starting the program with as "">python week4.py" should give a forecast for Idd
and Idd Kirke at 13:00 today or tomorrow. This is just included as a quick
sample of a working test. Feel free to play add tests in the same place, or run
through ipython to do more rigorous tests.

The process return_weather uses a dictionary to return data from the xmls. This
screws up the ordering of the places found in noreg.txt. I had planned to 
implement an ordered dictionary here, but did not get the time. 

There are a few things missing from this assignment:
I did not complete the test for 4.5 and the second test for 4.4, the rest 
should be there and work.

There are some small issues with the Lazy class, I didn't get the optional 
timestamp to work as intended for testing. But it works fine in general and
deletes outdated files (try manually setting the computers clock ahead to test)

I did not get 4.7 to work properly.

I have not implemented an easy way to run all the tests at once, sorry. Run
tests individually by calling them with py.test.