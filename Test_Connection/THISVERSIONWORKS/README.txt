The files in this dir successfully create communication between a computer and an android device. I also included
the android server because we can do communication between 2 android devices too. Currently it doesn't work in
every instance. You can run the android project in ClientAndroid with no problems. You may need to change the
default IP address but the port number of 55555 is hard coded in the raspberry server program right now. It will
also break if you don't enter a string to send to raspberry server right now. Running the server side is the issue
right now. It works in netbeans but running the server through cmd does not work.

--------------------------------------------------------------------------------------------------------------
CONNECT ClientAndroid run on android studio to ServerRaspberry/raspberryPiServer run on netbeans