# Data-Comm-Proj-2

To do:
Host:
-Create p2p hosts that have both client and server functionality and can download from eachother
-have it read the files in its directory, save the list to a file, and send the list of files to the central server
-Create a gui for the p2p hosts that allow you to enter in the information used by server and use the search
-send that information for use by the central server
-create a panel on our host GUI to display the table returned by the central server after searching
(object sockets seem to the be the easiest solution for sending and receiving tables) 

Central Server:
-After a host connects, collects information to be used in table from host
-Stores information in table that can be saved and reopened when the program closes
-returns sub-table of results after being sent a search term by host, returns table to host(use row filter for search)



