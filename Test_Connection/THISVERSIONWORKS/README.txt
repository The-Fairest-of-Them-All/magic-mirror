The files in this dir successfully create communication between a computer and an android device. I also included
the android server because we can do communication between 2 android devices too. Currently it doesn't work in
every instance. You can run the android project in ClientAndroid with no problems. You may need to change the
default IP address but the port number of 55555 is hard coded in the raspberry server program right now. It will
also break if you don't enter a string to send to raspberry server right now. Running the server side is the issue
right now. I don't know why but when I run the netbeans project in ServerRaspberry/raspberryPiServer I don't
get any output but the android app is connected. You can tell by the output on the android device. Running 
the java file in ServerRaspberry/ doesn't work at all and there is no output. I think these may be threading
issues but I'm not sure. However if you run java file in ServerRaspberry/ on something like jGrasp (jGrasp
definitely worked for me) then you see the output on both sides and it all works.


--------------------------------------------------------------------------------------------------------------
CONNECT ClientAndroid run on android studio to ServerRaspberry/raspberryServer.java run on jGrasp