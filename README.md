# Data-Comm-Proj-2


Current state of project:
HostClient now sends user info which is collected and added to a textfile by the CentralServer. The CentralServer uses this textfile to store user info like a table. The textfile is updated each time a new unique hostname connects to the CentralServer. CentralServer reads the textfile into an array list of User objects, essentially making a table of user information. 
HostClient now also sends the information for a textfile containing a list of files the user wants to share and a series of keywords describing the file. CentralServer is receiving this information, but doesn't store it like the user information yet. This is in progress. See recent commits for more details and info on compiling -Edward

To do:

-Integrate HostClient into GUI, so the user can use the GUI to give the HostClient the information to send to the CentralServer

-make CentralServer save file information (with the hostname of the file's owner) into a textfile

-use this to populate an arraylist of SharedFile objects, each object containing relevant file information

-search function that searches through the list of files, and pulls out relevant results based on the keyword sent by the user and looking for matches in the file descriptions

-CentralServer will then return these relevant matches and as well as the hostname and port information of the user who they belong to's FTP server, and send them back to be displayed on the GUI's table

-GUI will display table of relevant results and information so the HostClient can use the FTP commands to retrieve the file

-GUI/Main will have both a HostClient and HostServer, a server that allows other users using our program to retrieve files from the user running it. 

