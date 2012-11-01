CrashAutoRestart
================

RemoteToolkit module to automatically restart the server and optionally execute configurable commands when it encounters an unexpected exception.


To install, simply download the module jar to the modules directory in the toolkit folder.

By default, CrashAutoRestart simply reschedules the restart to 1 second when the server crashes. 
If you want to decide which commands are done upon crashing, create a file named "crash_lines.txt" in the toolkit folder. 
Each line in the file will be executed as a command.
