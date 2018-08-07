# QueueDemo
To Run: rc.App

What it does: Demonstrates adding a message on an Azure Service Bus Queue, and then reading it.

Configuration Items: Within the App.java class, configure the two items below:

  - SAMPLE_CONNECTIONSTRING: Connection string (either primary or secondary) from your Azure SB. Please safeguard your connection strings by not checking them in anywhere publicly.
  - QUEUE_NAME: The name of the queue in your Azure SB
