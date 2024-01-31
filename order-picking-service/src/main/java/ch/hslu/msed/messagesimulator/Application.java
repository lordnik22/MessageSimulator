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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Random;


@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private KafkaTemplate<Object, Object> kafkaTemplate;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args).close();
    }

    @Bean
    public NewTopic topic() {
        return new NewTopic("orderForPicking", 1, (short) 1);
    }

    @Override
    public void run(String... args) throws Exception {
        Thread.currentThread().join();
    }

    @KafkaListener(id = "orders", topics = "orderForPicking")
    public void listen(String msg) {
        pickAvailablePositions(new Gson().fromJson(msg, Order.class));
    }

    private void pickAvailablePositions(Order order) {
        if (order.isPartialDeliveryEnabled()) {
            kafkaTemplate.send("orderForShipping", order);
            System.out.printf("Ship partial of order %d%n", order.getNumber());
        } else {
            if ((new Random()).nextInt(10) == 0) {
                order.setPartialDeliveryEnabled(true);
                System.out.printf("Not all positions on order %d are available%n", order.getNumber());
                kafkaTemplate.send("orderForPicking", order);
            } else {
                System.out.printf("Ship order %d%n", order.getNumber());
                kafkaTemplate.send("orderForShipping", order);
            }
        }
    }
}
