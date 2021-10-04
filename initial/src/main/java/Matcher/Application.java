package Matcher;

import Matcher.components.Account.Account;
import Matcher.components.Account.AccountService;
import Matcher.kafka.Messages.AccountRequest;
import Matcher.kafka.Messages.AccountResponse;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;


@SpringBootApplication
public class Application {

//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurerAdapter() {
//            @Override
//            public void addCorsMappings(@NotNull CorsRegistry registry) {
//                registry.addMapping("/**");
//            }
//        };
//    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);// new SpringApplicationBuilder(Application.class).run(args);

        MessageListener listener = context.getBean(MessageListener.class);
        MessageProducer producer = context.getBean(MessageProducer.class);

        //SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            System.out.println("Let's inspect the beans provided by Spring Boot:");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }

        };
    }

    //    @Value("${rt.server.host}")
    //private String host = "http://localhost";

    //    @Value("${rt.server.port}")
    //private Integer port = 3010;

    @Bean
    public SocketIOServer socketIOServer() {
        Configuration config = new Configuration();
        //config.setHostname(host);
        config.setPort(4000);
        return new SocketIOServer(config);
    }

    @Bean
    public MessageListener messageListener() {
        return new MessageListener();
    }

    @Bean
    public MessageProducer messageProducer() {
        return new MessageProducer();
    }

    public static class MessageProducer {

        @Autowired
        private KafkaTemplate<String, String> kafkaTemplate;

        @Autowired
        private KafkaTemplate<String, AccountResponse> accountResponseKafkaTemplate;

        @Value(value = "${message.topic.name}")
        private String topicName;

        @Value(value = "${accountResponse.topic.name}")
        private String accountResponseTopicName;

        @Value(value = "${partitioned.topic.name}")
        private String partitionedTopicName;

        @Value(value = "${filtered.topic.name}")
        private String filteredTopicName;

        public void sendAccountResponseMessage(AccountResponse accountResponse) {
            ListenableFuture<SendResult<String, AccountResponse>> future = accountResponseKafkaTemplate.send(accountResponseTopicName, accountResponse);
            future.addCallback(new ListenableFutureCallback<SendResult<String, AccountResponse>>() {
                @Override
                public void onSuccess(SendResult<String, AccountResponse> result) {
                    System.out.println("Sent request for account info for username=[" + accountResponse.getUsername() + "] with offset=[" + result.getRecordMetadata()
                            .offset() + "]");
                }

                @Override
                public void onFailure(Throwable ex) {
                    System.out.println("Unable to send request for username=[" + accountResponse.getUsername() + "] due to : " + ex.getMessage());
                }
            });
        }
    }

    public static class MessageListener {
        @Autowired
        Application.MessageProducer producer;

        @Autowired
        AccountService accountService;

        private CountDownLatch latch = new CountDownLatch(3);

        private CountDownLatch partitionLatch = new CountDownLatch(2);

        private CountDownLatch filterLatch = new CountDownLatch(2);

        private CountDownLatch accountRequestLatch = new CountDownLatch(1);

        @KafkaListener(topics = "${accountRequest.topic.name}", containerFactory = "accountRequestConcurrentKafkaListenerContainerFactory")
        public void accountRequestListener(AccountRequest accountRequest) {
            System.out.println("Received account request for username: " + accountRequest.getBody());
            List<Account> accountList = accountService.getAccountByUsername(accountRequest.getBody());
            if (accountList.size() == 0) System.out.println("Not found");
            else {
                Account account = accountList.get(0);
                AccountResponse response = new AccountResponse(account.getUsername(), account.getGbp(), account.getBtc());
                System.out.println(response.toString());
                producer.sendAccountResponseMessage(response);
            }
            this.accountRequestLatch.countDown();
        }
    }

//    public static void main(String[] args) {
//        new SpringApplicationBuilder(Application.class).run(args);
//    }

}
