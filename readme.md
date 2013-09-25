<<README>>
0.3.1
author: Plamen Ignatov
e-mail: <plig@mail.bg>
[Last modification [27/05/2003]]

CONTENTS
1.Brief Description
    1.1 System requirements
    1.2 Installation
    1.3 Deinstallation
2.Message Server interface
    2.1 Command line options
    	2.1.a Initialization file
    2.2 Error messages
3.Client interface
    3.1 Command line options
    3.2 Log-In dialog & Authentication
    3.3 Main Client Window
        3.3.1 Status altering
    3.4 Sending messages
    3.5 Receiving messages
    3.6 Common Chat
    3.7 Message Board
    3.8 Settings
        3.8.1 Adjusting settings
4.Tech info
    4.1 Protocol specifications
    4.2 Authentication records
5.Troubleshooting

1. Brief Description
LIM stands for LAN Instant Messaging. Some of the key features are Client/Server based technology, personal
messages, per user message history, user info, common chat, plain text protocol, TCP/IP, settings and login data stored
in a local file (settings.dat and login.txt respectively). Voice chat, emoticons and flexible message board are 
available but in development state. 
User defined colors and offline messages are proposed for future development.

Programmed in Java (JDK 1.4.1) using IntelliJ Idea Ariadna with Java Swing GUI.
    
    1.1 System Requirements
    Windows/Linux/BeOS/OS/2/MacOS, virtually any OS with JRE installed on it and graphic environment.
    Java JRE 1.1+ for the Message server, 1.4+ for the Client
    Sound Card & Microphone supported by the OS and JavaSound for voice chat.
    
    1.2 Installation
    Starting with 0.3.1 rc2 LIM is deployed as executable install file for Windows OS. The setup.exe is based
    on Nullsoft awesome installation software. The installation itself is rather intuitive and creates startuo menu
    entries.
    For all other OS, the old depployment is valid - Simple copy of all files. Two startup files are provided:
    	server.bat/server.sh and client.bat/client.sh
    	
    1.3 Deinstallation 
    Windows version can be uninstalled with the uninstall.exe created during the installation process.
    UNIX/LINUX versions are uninstalled by simple delete of all files in the LIM installation directory.

2. Message Server interface
Default TCP/IP communication port 1001
    2.1 Command Line options
        No command line options for now
        2.1.a Initialization file
        At the present moment [27.05.2003] there is just one setting - TCP/IP port number specified in lim.ini
        (by default it is 1001)
    2.2 Error messages
        If you don't have root privilleges you cannot start server socket in the protected port range (1-1024).
        If another server socket is started on that port.

4. Tech Info
    4.1 Protocol specifications
    Client -> Server                                    Example
        UIN <user_name>                                 UIN Plamen
        Response: +OK or -ERR                           +OK
    Client -> Server
        LIST                                            LIST
        Response: list of users ending with a dot       PLAMEN
                                                        STEFAN
                                                        .
    Client->Server
        SEND SRC DEST
        Line1
        Line2
        .
        The previous fragment sends message from src to dest. Remark: src needs to be the same as the user client.
    Client->Server
        INFO user                                       INFO PLAMEN
    Client->Server
        CHAT SRC
        Chat Message
        .
    Client->Server
        STATUS User new_status                         STATUS Plamen DND
        new_status: Online, Away, DND, NA
    Server->Client
        +ADD user                                      +ADD IVAN
    Server->Client
        +REM user                                      +REM IVAN
    Server->Client
        +STATUS user new_status
        
	4.2 Authentication records
	All authentication records are stored in passwd file, residing in the main installation directory.
	Format of records follows this pattern
	name:<MD5 hash of the password>
	MD5 hash can be obtained either by third party software or by the tool md5sum 
	
    --TO-BE-CONTINUED---