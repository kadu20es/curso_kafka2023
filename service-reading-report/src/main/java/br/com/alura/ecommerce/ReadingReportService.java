package br.com.alura.ecommerce;

import br.com.alura.ecommerce.services.ConsumerService;
import br.com.alura.ecommerce.services.ServiceRunner;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;


public class ReadingReportService implements ConsumerService<User> {

    private static final Path SOURCE = new File("src/main/resources/report.txt").toPath();

    public static void main(String[] args) {
        new ServiceRunner(ReadingReportService::new).start(3);
    }

    @Override
    public String getConsumerGroup() {
        return ReadingReportService.class.getSimpleName();
    }

    @Override
    public String getTopic() {
        return "ECOMMERCE_USER_GENERATE_READING_REPORT";
    }

    public void parse(ConsumerRecord<String, Message<User>> record) throws IOException {

        System.out.println("---------------------------------------------------------\n" +
                " Processing report for " + record.value());

        var message = record.value();
        var user = message.getPayload();
        var target = new File(user.getReportPath());
        IO.copyTo(SOURCE, target);
        IO.append(target, "Created for " + user.getUUID());

        System.out.println("File created: " + target.getAbsolutePath());
    }



}
