# MARK II

## Experiment 1 Explanation

### Why
Experiment 1 is about using the MARK II API for creating a large
Neocortex and Retina object. Connecting them and running the algorithms on it.
This takes more than 4GB of RAM memory but because unused objects are 
written to disk when not used the MARK II API can create models that are only
limited by the computer's hard disk space and not the max Java heap size.

### How to run code

1. The following instructions are for a Windows computer. For Linux or Max 
run instructions refer to initial outline on issue [here](https://github.com/WalnutiQ/WalnutiQ/issues/169)

2. First we need to compile the file. Navigate to "walnutiq" folder.
3. Type `gradlew.bat build` to create WalnutiQ.jar file
4. Navigate with `cd src\test\java\model\experiments\vision\MARK_II\` to MARK_II folder
5. Type: `javac -cp "c:\users\huanqing\workspaceJava\walnutiq\referencedlibraries\gson-2.2.4.jar;c:\users\huanqing\workspaceJava\walnutiq\build\libs\WalnutiQ.jar;." Experiment_1.java`
6. Now we need to run the file. Type `cd ..\..\..\..` or navigate to folder immediately above "model/"
7. Make sure to insert your own unique path from "C:\". For example mine will be:
   `java -cp "c:\users\huanqing\workspaceJava\walnutiq\referencedlibraries\gson-2.2.4.jar;c:\users\huanqing\workspaceJava\walnutiq\build\libs\WalnutiQ.jar;." Experiment_1`

8. To recompile and run everything together copy and paste the following command
   when you are in the `walnutiq\src\test\java` folder:
   ```
   cd ..\..\..&
   gradlew.bat build&
   cd src\test\java\model\experiments\vision\MARK_II\&
   javac -cp "c:\users\huanqing\workspaceJava\walnutiq\referencedlibraries\gson-2.2.4.jar;c:\users\huanqing\workspaceJava\walnutiq\build\libs\WalnutiQ.jar;." Experiment_1.java&
   cd ..\..\..\..&
   java -cp "c:\users\huanqing\workspaceJava\walnutiq\referencedlibraries\gson-2.2.4.jar;c:\users\huanqing\workspaceJava\walnutiq\build\libs\WalnutiQ.jar;." Experiment_1
   ```
   