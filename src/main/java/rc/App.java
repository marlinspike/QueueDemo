package rc;
import com.microsoft.azure.servicebus.*;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import java.time.Duration;
/**
 * Hello world!
 *
 */
public class App {
    
    static final String SAMPLE_CONNECTIONSTRING = "<SAMPLE_CONNECTIONSTRING>";
    static final String QUEUE_NAME = "<QUEUE_NAME>";

    public static void main( String[] args ){
        QueueClient sendClient;
        IMessageReceiver receiver;
        String connectionString;

        System.out.println( "Hello World!" );

        try{
            sendClient = new QueueClient(new ConnectionStringBuilder(SAMPLE_CONNECTIONSTRING, QUEUE_NAME), ReceiveMode.PEEKLOCK);
            receiver = ClientFactory.createMessageReceiverFromConnectionStringBuilder(new ConnectionStringBuilder(SAMPLE_CONNECTIONSTRING, "myq"), ReceiveMode.PEEKLOCK);

            sendMessage(sendClient);
            sendClient.close();
            receiveMessage(receiver);
            System.exit(0);
        }
        catch(Exception e){
            System.out.println(e);
        }
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
                //endClient.send(message);
            }catch(Exception e){
                System.out.println(e);
            }
    }

    public static void receiveMessage(IMessageReceiver receiver){
        try{  
            IMessage message = receiver.receive(Duration.ofSeconds(2));
            
            if (message != null){
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
