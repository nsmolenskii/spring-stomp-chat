# Chat example

Simple chat application, works with STOMP over WebSoocket protocol

## Running


### Active MQ
- Run activemq 
    ```bash
    docker run -d --name='activemq' -it -P \
    -p 8161:8161 \
    -p 61616:61616 \
    -p 61613:61613 \
    webcenter/activemq
    ```
- Check web console http://localhost:8161/ with credentials admin/admin

### Run application
- Run application
    ```bash
      ./gradlew bootRun
    ``` 
- Open application http://localhost:8080/ with credentials john/john or jane/jane 


