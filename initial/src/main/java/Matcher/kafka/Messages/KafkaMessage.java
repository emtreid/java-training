package Matcher.kafka.Messages;

public interface KafkaMessage<E> {
    String getHeader();

    E getBody();
}
