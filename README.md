# Data-Comm-Proj-2

Looking at the project requirements again, there were a  few things I interpretated incorrectly. I updated the to-do list with how I currently plan to implement the features. -Edward

To do:

Host:
-Create p2p hosts that have both client and server functionality and can download from eachother

-Create a gui for the p2p hosts that allow you to enter in the information used by server and use the search

-send that information for use by the central server

-have it read a text file containing the names of the files in the directory and their descriptions and send them to the central server

-create a panel on our host GUI table to display the entries returned by the central server after a keyword search
(possibly sending the entries back in the form of an object arraylist, with each object containg the information for each row of the table) 

Central Server:

-After a host connects, collects information sent by host to be stored in user table 

-takes list of files and their descriptions, and stores them (also possibly as an object arraylist, with each object containing the file name and description as string values)

-searches through file descriptions for keywords, and returns relevant entries back to the host along with hostname and username of uploader



Current state of project:
HostClient collects username, hostname, and speed, and list of files (in string form) from its directory and sends them to CentralServer - Edward
