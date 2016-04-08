#tells system to run with bash
#! /bin/bash

#enable bluetooth
sudo systemctl start bluetooth

#wait for 1 second to ensure bluetooth is on
sleep 1

#run bluetoothctl
#turn on power, pairable, and discoverable to allow android device to connect
echo -e 'power on\npairable on\nshow\t\nquit' | bluetoothctl
echo -e 'discoverable yes\nshow\t\nquit' | bluetoothctl
echo complete
