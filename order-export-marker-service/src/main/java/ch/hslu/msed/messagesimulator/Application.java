/*
* Copyright 2018-2022 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      https://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package ch.hslu.msed.messagesimulator;

import com.google.gson.Gson;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(Application.class);

    @Autowired
    private KafkaTemplate<Object, Object> kafkaTemplate;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args).close();
    }

    @Bean
    public NewTopic topic1() {
        return new NewTopic("orderForExport", 1, (short) 1);
    }
    @Bean
    public NewTopic topic2() {
        return new NewTopic("orderForProcessing", 1, (short) 1);
    }

    @Override
    public void run(String... args) throws Exception {
        Thread.currentThread().join();
    }

    @KafkaListener(id = "orders", topics = "orderCreated")
	public void listen(String msg) {
        maybeSendToExport(new Gson().fromJson(msg, Order.class));
	}

    private void maybeSendToExport(Order order) {
        if (order.getCustomer().getZip() > 9000) {
            System.out.printf("Export order %d", order.getNumber());
            kafkaTemplate.send("orderForExport", order);
        } else {
            System.out.printf("Process order %d", order.getNumber());
            kafkaTemplate.send("orderForProcessing", order);
        }
    }

}
