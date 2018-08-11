# QueueDemo
To Run: (using Maven) mvn exec:java -Dexec.mainClass="rc.App" -Dexec.args="-q <QUEUE_NAME> -c <CONNECTION_STRING>"  
  
The command line parameters map as such:  
-q: The name of the Service Bus Queue  
-c: The Primary or Secondary Connection String URL  

For Help: mvn exec:java -Dexec.mainClass="rc.App" -Dexec.args="-h"  
  
  
What it does: Demonstrates adding a message on an Azure Service Bus Queue, and then reading it.  


