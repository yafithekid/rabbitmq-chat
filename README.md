# RabbitMQ Chat
Simple RPC chat with RabbitMQ

### Members

- 13512014 Muhammad Yafi
- 13512066 Calvin Sadewa


### Concepts

You should familiarize yourself with the concepts of queue, exchange, and binding in RabbitMQ first.

The structure of queue and exchange in the RabbitMQ of this chat server is
```
  V1   V2     V3
     E1    E2

 EX    EX     QU
      RPLS - Calvin
     /     - Yafi
 Yafi
     \
      PAT - Kevin
          - Yafi

EX : exchange
QU : queue
V  : node
E  : binding
```
- There are queues named with nickname. When a user is connected to the server, it will consume messages from this queue
- There are exchanges named with channel. When a nickname subscribes to a channel, it will bind queue with related nick to exchange with related channel.
When a nickname unsubscribe, it will unbind the queue from the exchange.
- When a user send a message to a channel, it will send the message to the channel. The exchange channel will broadcast to the queues using fanout.
- To make broadcast message, we create another exchange named with nickname. The exchange will bind another exchange related to user channel subscriptions.
So, if `Yafi` joins `RPLS` and `PAT` channel, then if he broadcast a message, the `Yafi` exchange will fanout message to `PAT` and `RPLS` exchange.
The `RPLS` and `PAT` exchange will then forward the message to the queues.
- To handle name collision of the exchanges (example when there is a channel named `X` and nickname named `X`), we can add prefix to the
names. So example broadcast_X and channel_X

### Available commands

1. `/NICK <nickname>`: change your nickname to **nickname**. Default nickname is `paijo`
2. `/JOIN <channelname>`: join current nickname to **channelname**
3. `/LEAVE <channel>`: leave current nickname from **channel**
4. `/EXIT`: terminate the application
5. `@<channelname> <any_text>` send **any_text** to **channelname**
6. `<any_text>` send **any_text** to all channels joined by nickname.

Note: you must type a command to get the chat from the server. This is intended for the UX (when you type a message, and
there are incoming messages, it will buffered first and will be displayed after you send a command.)
### Prerequisites

1. Install Java JDK 1.8
2. Install Gradle, add it to your PATH environment variables.
3. Ensure gradle can works by typing `gradle` in your command prompt.


### Run the jar files

1. Run `gradle clientJar` on root project dir
2. In `build/libs` folder, run `java -jar chat-client-1.0.jar`