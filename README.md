Scala Anonymous Chat Bot For Telegram
=============================

## ABOUT
This bot allows two people to communicate with each other on any topic without revealing account

Main features:

- You can choose the criteria by which the interlocutors will look for each other. This could be politics, sports or other.
- You can connect people with one point of view on the topics discussed, or with different. It depends on the type of content you want to provide.
- Bot does not use third-party telegram libraries, it only implements the necessary functions.
- Bot provides statistics by `get` requests

## REQUIREMENTS
- Java 1.8+
- Scala 2.11.12
- PostgresDB (optional) - you can rewrite DAO object
- MongoDB / Cassandra (optional) - you can rewrite DAO object
- SSL/TLS - bot works on webhook [More info](https://core.telegram.org/bots/webhooks#ssl-tls-what-is-it-and-why-do-i-have-to-handle-this-for-a-webhoo)
- Domain name & static ip
- SBT
- Nginx (optional) - it's possible to inject ssl into jvm keystore
 
 ## QUICK START
 1) [Create new bot in telegram, and receive bot token](https://core.telegram.org/bots#3-how-do-i-create-a-bot)
 2) [Download Nginx](https://nginx.org/en/download.html)
 3) [Inject SSL cert to Nginx](https://helpdesk.ssls.com/hc/en-us/articles/203427642-How-to-install-an-SSL-certificate-on-a-NGINX-server)
  (listen 443 port)
 4) Download this project and add telegram.conf file in src/main/resources folder:
 
 telegram.conf
 
      bot {
        token = "YOUR TOKEN"
      }
      server {
        host = "0.0.0.0"
        port = YOUR PORT
        apiUrl = "https://api.telegram.org/bot/bot"
      }
      
5) **Работу с подключением и настройкой БД я еще не пишу, потому что еще не начинал с ней работать**
6) Start your app
7) Send `get` request on api.telegram.org/bot/bot{YOUR_TOKEN}/setWebhook?url=https://{YOUR_DOMAIN}/telegram
8) It works!

