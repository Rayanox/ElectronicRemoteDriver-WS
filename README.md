# Presentation

The project is a Web Service that drives a set of electronic device (remotely) using a command configuration file on the server-side. The final goal is to be able to drive any electronic device (and change the algorithm) without recompiling any component (Arduino or WS).

# WS Method

The main WS Method to reach is "GetCommands". It takes 2 parameters:
 1) The Access-Token, to increase the security.
 2) **The Program-ID**: it's a String ID (name) that relates to a specific device.
 
Depending on the time the method is called, the commands returned by the WS can be different, all the logic is located in the WS, instead of in the remote electronic device.


# Configuration File 

There must be one configuration file per **Program-ID**. This file give the algoritmh of the commands to send to the device, depending on the time it is called.

The synthax is explained here:
```
#Command Mapping#{1=X; 2=Haut ; 3=O;4=PS;5=BAS}
#Commands Sequence#{X-X-PS(2)-BAS-BAS-X-Sleep(5)-O-O}
[15:00-20:00]{X-X-PS(10)-O}
```

with:
- The first line is dedicated to command mapping
- All the lines > 1 are dedicated to the sequence algorithm
- in command mapping: the key is the **pin number of the microcontroller**
- the value is the command name that will be written in the command sequence
- A command with parenthesis is pushed during the number of seconds written between the two parenthesis. The parameter is a double.
- There are special commands that are not declared in the command mapping:
   -> Sleep(X): With X = time to sleep in seconds
- Comments are written between '#' and are only declared at starting of the line. If there is only one '#', all the line is commented
- A command line can start with a time interval check: it means that if the current time (when the service is called) is inside the time interval, then this command line sequence is returned.

# Sending protocol

The communication protocol between the WS and the electronic device is explained here. After the electronic device requests the WS to get his commands, then the WS responds using the following syntax:
```
(TD=0.4){1;1.2-5-S;120-4-2-3}
```
With:
- First, between parenthesis are declared usefully parameters. Currently, only the parameter TD exists, and means the time to keep the PIN activated by default when no time is specified. So TD=0.4 means that a PIN will be activated during 400 ms when no time is specified. If this parameter is not set, then the default time is set to 500ms.
- Then, between "{}" is written the commands sequence

# Project example

A project using this Remote Driver is explained here: https://github.com/RayanoxPersonalProjects/VideoAlarmClock . But the project is not used (deprecated), instead it uses this current repository in a version more generic.
