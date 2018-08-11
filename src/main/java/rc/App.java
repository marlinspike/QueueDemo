package rc;
import com.microsoft.azure.servicebus.*;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import picocli.CommandLine;
import picocli.CommandLine.Option;
import java.time.Duration;
/**
 * Hello world!
 *
 */
public class App implements Runnable{
    @Option(names = {"-c", "--connection"}, required = true, description = "Connection String for the Service Bus. Use either Primary or Secondary Connection String.")
    private String CONNECTIONSTRING = "";
    @Option(names = {"-q", "--queue"}, required = true, description = "Service Bus Queue Name.")
    private String QUEUE_NAME = "";
    @Option(names = { "-h", "--help" }, usageHelp = true,
            description = "Parameters required: -q [Queue Name], [-c] Connection String.")
    private boolean helpRequested = false;

    public void run() {
    
            QueueClient sendClient;
            IMessageReceiver receiver;
    
            try{
                sendClient = new QueueClient(new ConnectionStringBuilder(CONNECTIONSTRING, QUEUE_NAME), ReceiveMode.PEEKLOCK);
                receiver = ClientFactory.createMessageReceiverFromConnectionStringBuilder(new ConnectionStringBuilder(CONNECTIONSTRING, QUEUE_NAME), ReceiveMode.PEEKLOCK);
    
                sendMessage(sendClient);
                System.out.println("");
                sendClient.close();
                receiveMessage(receiver);
                System.out.println("");
                System.exit(0);
            }
            catch(Exception e){
                System.out.println(e);
            }
    }

    

    public static void main( String[] args ){
        CommandLine.run(new App(), args);
    }


    public static void sendMessage(QueueClient sendClient){
        Message message = new Message("Hello World!");
        message.setContentType("text");
            message.setLabel("Hello World!");
            message.setMessageId("1");
            //message.setTimeToLive(Duration.ofMinutes(20));
            try{
                System.out.println("Sending Message...");
                sendClient.send(message);
                System.out.println("Sent Message: " + message.getLabel());
                //endClient.send(message);
            }catch(Exception e){
                System.out.println(e);
            }
    }

    public static void receiveMessage(IMessageReceiver receiver){
        try{  
            IMessage message = receiver.receive(Duration.ofSeconds(2));
            
            if (message != null){
                System.out.println("Receiving Message...");
                System.out.println("Got Message: " + message.getLabel());
                receiver.complete(message.getLockToken());
            }else{
                System.out.println("No messages!");
            }
            
        }catch(Exception e){
            System.out.println(e);
        }
    }

}
