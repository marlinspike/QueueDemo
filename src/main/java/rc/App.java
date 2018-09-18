package rc;
import com.microsoft.azure.servicebus.*;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import picocli.CommandLine;
import picocli.CommandLine.Option;
import java.time.Duration;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Random;

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
    @Option(names = {"-m", "--message"}, required = false, description = "Message to send.")
    private String messageToSend = "Hello World!";

    public void run() {
    
            QueueClient sendClient;
            IMessageReceiver receiver;
    
            try{
                sendClient = new QueueClient(new ConnectionStringBuilder(CONNECTIONSTRING, QUEUE_NAME), ReceiveMode.PEEKLOCK);
                receiver = ClientFactory.createMessageReceiverFromConnectionStringBuilder(new ConnectionStringBuilder(CONNECTIONSTRING, QUEUE_NAME), ReceiveMode.PEEKLOCK);
    
                sendMessage(sendClient, messageToSend);
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


    public static void sendMessage(QueueClient sendClient, String messageToSend){
        Message message = new Message();

        message.setContentType("text");
            message.setBody(messageToSend.getBytes());
            message.setMessageId("1");
            //message.setTimeToLive(Duration.ofMinutes(20));
            try{
                System.out.println("Sending Message...");
                sendClient.send(message);

                System.out.println("Sent Message: " + new String(message.getBody(), "UTF-8"));
            }catch(Exception e){
                System.out.println(e);
            }
    }

    public static void receiveMessage(IMessageReceiver receiver){
        try{  
            IMessage  message = receiver.receive(Duration.ofSeconds(2));
            byte[] body = message.getBody();
            String msg = new String(body, "UTF-8");

            if (message != null){
                System.out.println("Receiving Message... [" + body.length + "] bytes");
                System.out.println("Translating Message: " + new String(body, "UTF-8"));
                receiver.complete(message.getLockToken());
            }else{
                System.out.println("No messages!");
            }
            
        }catch(Exception e){
            System.out.println(e);
        }
    }

}
