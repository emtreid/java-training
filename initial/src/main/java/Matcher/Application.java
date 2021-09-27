package Matcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


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
        SpringApplication.run(Application.class, args);
    }

//    @Bean
//    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
//        return args -> {
//
//            System.out.println("Let's inspect the beans provided by Spring Boot:");
//
//            String[] beanNames = ctx.getBeanDefinitionNames();
//            Arrays.sort(beanNames);
//            for (String beanName : beanNames) {
//                System.out.println(beanName);
//            }
//
//        };
//    }

    //    @Value("${rt.server.host}")
    //private String host = "http://localhost";

    //    @Value("${rt.server.port}")
    //private Integer port = 3010;

//    @Bean
//    public SocketIOServer socketIOServer() {
//        Configuration config = new Configuration();
//        //config.setHostname(host);
//        config.setPort(4000);
//        return new SocketIOServer(config);
//    }

//    public static void main(String[] args) {
//        new SpringApplicationBuilder(Application.class).run(args);
//    }

}
