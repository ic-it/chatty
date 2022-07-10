import os
import requests
import threading

API_URL = f"http://{input('ServerIP: ')}:8080/api/v1"
API_KEY = ...

########################################################## DTBS
class User:
    id: int
    api_key: str
    username: str
    is_online: str

    def __init__(self, id: int, apiKey: str, username: str, isOnline: bool) -> None:
        self.id = id
        self.api_key = apiKey
        self.username = username
        self.is_online = isOnline
    
    def __str__(self) -> str:
        return f"User({self.id=}, {self.api_key=}, {self.username=}, {self.is_online=})"
    
    def __repr__(self) -> str:
        return str(self)

class Message:
    id: int
    chat_id: int
    user_id: int
    text: str
    time: int
    type: str

    def __init__(self, id: int, chatId: int, userId: int, text: str, time: int, type: str) -> None:
        self.id = id
        self.chat_id = chatId
        self.user_id = userId
        self.text = text
        self.time = time
        self.type = type
    
    def __str__(self) -> str:
        return f"User({self.id=}, {self.chat_id=}, {self.user_id=}, {self.text=}, {self.time=}, {self.type=})"
    
    def __repr__(self) -> str:
        return str(self)


class Chat:
    id: int
    name: str
    poll: bool

    def __init__(self, id: int, name: str) -> None:
        self.id = id
        self.name = name
        self.poll = False

    def __str__(self) -> str:
        return f"User({self.id=}, {self.name=})"
    
    def __repr__(self) -> str:
        return str(self)
        
    def send_message(self, message: str) -> Message:
        return Message(**requests.get(API_URL + "/sendMessage", params={"api_key": API_KEY, "cid": self.id, "text": message}).json())
    
    def get_messages(self) -> list[Message]:
        return [Message(**user) for user in requests.get(API_URL + "/getChatMessages", params={"api_key": API_KEY, "cid": self.id}).json()]
    
    def polling(self, callback: callable) -> threading.Thread:
        self.poll = True

        def target():
            while self.poll:
                update_dict = requests.get(API_URL + "/getUpdates", params={"api_key": API_KEY, "cid": self.id}).json()
                # print(rdata)
                update = Update(**update_dict)
                callback(update)

        return threading.Thread(target=target).start()

    
class Update:
    online_users: list[User]
    notices: list[User]
    chat_updates: list[Message]
    chat: Chat


    def __init__(self, onlineUsers: list[dict], notices: list[dict], chatUpdates: list[dict], chat: dict) -> None:
        self.online_users = [User(**u) for u in onlineUsers]
        self.notices = [User(**u) for u in notices]
        self.chat_updates = [Message(**m) for m in chatUpdates]
        self.chat = Chat(**chat)
    
    def __str__(self) -> str:
        return f"User({self.online_users=}, {self.notices=}, {self.chat_updates=}, {self.chat=})"
    
    def __repr__(self) -> str:
        return str(self)

########################################################## DTBS

def create_user(username: str) -> User:
    return User(**requests.get(API_URL + "/createUser", params={"username": username}).json())

def get_me() -> User:
    return User(**requests.get(API_URL + "/me", params={"api_key": API_KEY}).json())

def get_users() -> list[User]:
    return [User(**user) for user in requests.get(API_URL + "/getUsers", params={"api_key": API_KEY}).json()]

def get_user(uid: int) -> User:
    return User(**requests.get(API_URL + "/getUser", params={"api_key": API_KEY, "uid": uid}).json())

def join_chat_with(uid: int) -> Chat:
    return Chat(**requests.get(API_URL + "/joinChatWith", params={"api_key": API_KEY, "uid": uid}).json())

###############################################################
# cli

def repr_messages(messages: list[Message]):
    os.system("clear")
    for message in messages:
        if message.type == "join_chat":
            print(get_user(message.user_id).username, "joined")
        else:
            print(f"{message.id=}, {message.time=}, {get_user(message.user_id).username}: \n\t{message.text}")
    print()


username = input("username: ")
user = create_user(username)
API_KEY = user.api_key

messages: list[Message] = []

print(f"Me: {user.id}:{user.username}")

print("Users: ")
for user in get_users():
    print(f"{user.id}: {user.username}")

uid = input("Select user: ")
chat = join_chat_with(uid)
chat_with = get_user(uid)


messages = chat.get_messages()
repr_messages(messages)



chat.polling(lambda update: messages.__iadd__(update.chat_updates) and repr_messages(messages))

try:
    while True:
        chat.send_message(input(""))
except KeyboardInterrupt:
    pass

chat.poll = False
