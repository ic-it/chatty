# CHATTY

[![Project Status: Active – The project has reached a stable, usable state and is being actively developed.](https://www.repostatus.org/badges/latest/active.svg)](https://www.repostatus.org/#active)
## Short description:
At the moment the API only has creation of a user, sending a message, getting chat history and waiting for updates via long-polling. When you call the createUser method, you get a token with which you access the rest of the methods (sendMessage, getUpdates and more).
## Conceptual features:
### Join chat & chat:
I plan to create multiuser chats as well. And I came up with the idea to introduce a message like `join_chat` and not create an unnecessary table with user and chat relations. To join a chat you send a join message and your friend in the same chat sends this message.
### Long-polling:
In the server side, long-polling is implemented as an observer. When you make a request for getUpdates you subscribe to updates and every time someone sends a message, it goes into the notification mechanism and checks to see if someone is waiting for a message in that chat.

### Technical Features:
- DataBase: PostgreSQL
- Language: Java8
- Build Tool: Maven

### Requirements:
- docker
- docker-compose

### Building:
```sh
sudo docker-compose up --build
```

API documentation path:
```
{host}/api/v1/docs
```



## :warning: Important information. :warning:
Right now I don't have a WEB CLI for the API. But I made a lightweight pythonCLI which you can try out.  
https://github.com/ic-it/chatty/tree/pyCLI
