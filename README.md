Scala Anonymous Chat Bot For Telegram
=============================

## ABOUT
This bot allows two people to communicate with each other on any topic without revealing account

Main features:
- Choosing the topic. This can be politics, sports or any other.
- Connecting people with different opinions on the topic.
- Bot does not use third-party telegram libraries, it implements only necessary functions.
- Bot provides statistics by `get` requests

## FRONTEND
[https://github.com/MalyginL/malygin.club](https://github.com/MalyginL/malygin.club)
[https:malygin.club](https:malygin.club)

## REQUIREMENTS
- Java 1.8+
- Scala 2.11.12
- PostgreSQL
- Cassandra 
- SSL/TLS - bot works on webhook _[more info](https://core.telegram.org/bots/webhooks#ssl-tls-what-is-it-and-why-do-i-have-to-handle-this-for-a-webhoo)_
- Domain name & static ip
- SBT
- Nginx (optional) - it's possible to inject ssl into jvm keystore
 
 ## QUICK START
 1) [Create new bot in telegram, and receive bot token](https://core.telegram.org/bots#3-how-do-i-create-a-bot)
 2) [Download Nginx](https://nginx.org/en/download.html)
 3) [Inject SSL cert to Nginx](https://helpdesk.ssls.com/hc/en-us/articles/203427642-How-to-install-an-SSL-certificate-on-a-NGINX-server)
  (listen 443 port)
 4) Download this project and add telegram.conf file in `src/main/resources` folder:
 
 telegram.conf
 
      bot {
        token = "YOUR TOKEN"
      }
      server {
        host = "0.0.0.0"
        port = YOUR PORT
        apiUrl = "https://api.telegram.org/bot/bot"
      }
      
5) Install Cassandra & PostgreSQL
6) Add application.conf file in `src/main/resources` folder

 application.conf
 
      allowed-origins = "*"
      akka.http.server.max-connections = 200
      
      db {
        postgres {
          connectionPool = "HikariCP"
          dataSourceClass = "slick.jdbc.DatabaseUrlDataSource"
          driver = "slick.driver.PostgresDriver$"
          properties {
            driver = "org.postgresql.Driver"
            url = "YOUR DATABASE"
          }
          numThreads = 3
        }
        cassandra {
          host = "YOUR DATABASE"
          keyspace = "chatlogs"
        }
        test{
          connectionPool = "HikariCP"
          dataSourceClass = "slick.jdbc.DatabaseUrlDataSource"
          driver = "slick.driver.PostgresDriver$"
          properties {
            driver = "org.postgresql.Driver"
            url = "YOUR DATABASE"
          }
          numThreads = 1
        }
      }
      
7) Add Postgres tables mapped in `/src/main/scala/club/malygin/data/dataBase/pg/Schema.scala`

- table `quiz_q` stores information about poll topics
- table `quiz_r` stores information about poll answers
- table `users` stores all users information (username,chatID, etc)

8) Start your app
9) Send `GET` request on `api.telegram.org/bot/bot{YOUR_TOKEN}/setWebhook?url=https://{YOUR_DOMAIN}/telegram`
10) It works!

## HOW IT WORKS
![](https://i.imgur.com/My7exCQ.png)

## BOT COMMANDS
- `/start` - shows greeting message and starts poll
- `/register` - starts poll. It can be helpful if user wants to change answer
- `/search` - bot finds a pair of users and connects them
- `/leave` - bot stops current chat

## API
GET `/statistic/app` - Provides main statistic of application
 <br/><br/>_returns entity with params_:
- javaVendor: String
- javaVersion: String
- pid: Long
- uptime: Long
- heapInit: Long
- heapUsed: Long
- heapMax: Long
- heapCommited: Long
- nonHeapUsed: Long
- nonHeapInit: Long
- nonHeapMax: Long
- nonHeapCommited: Long
- threadCount: Int
- daemons: Int

GET `/statistic/cache` - Provides scaffeine cache statistic
 <br/><br/>_returns entity with params_ :
- averageLoadPenalty: Double
- hitRate: Double
- evictionCount: Long
- estimatedSize: Long

GET `api/cache/current` - Returns Map[Long,Long] of current chatting pairs

GET `telegram/questions` - Returns current topics of poll
 <br/><br/>_returns entity with params_ :
- quizIdd: UUID               _topic id_
- text: String                _topic text_
- firstOption: String         _first possible answer_
- secondOption: String        _second possible answer_
- status: Boolean             _status of topic; true - active_

GET `api/logs/{id}` - PathVariable ! `id:Long` - returns message history of user with selected id
<br/><br/>_returns entity with params_ :
- id: UUID, _message id_
- user: BigInt, _author id_
- target: BigInt, _opponent id_
- message: String, _message text_
- time: org.joda.time.DateTime _message received time_

GET `"api/logs/clear"` - Truncates cassandra


