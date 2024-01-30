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

import java.sql.Timestamp;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;


@SpringBootApplication
public class Application {

    private final Logger logger = LoggerFactory.getLogger(Application.class);
    private static Boolean running = false;
    private static int interval = 5000;

    private static Application INSTANCE;

    public void Application() {
        INSTANCE = this;
    }

    @Autowired
    private KafkaTemplate<Object, Object> kafkaTemplate;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args).close();
    }

    @Bean
    public NewTopic topic() {
        return new NewTopic("orderCreated", 1, (short) 1);
    }

    @Bean
	@Profile("default") // Don't run from test(s)
	public ApplicationRunner runner() {
		return args -> {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            OrderGenerator og = new OrderGenerator();
            while(true) {
                if (running) {
                    kafkaTemplate.send("orderCreated", og.createRandomOrder());
                    System.out.println("Send message");
                } else {
                    System.out.println("Idle");
                }

                Thread.sleep(interval);
            }
		};
	}

    public void start() {
        System.out.println("START");
        running = true;
    }

    public void stop() {
        System.out.println("STOP");
        running = false;
    }

    public void interval(int interval) {
        System.out.printf("SET Interval to %dms%n", interval);
        Application.interval = interval;
    }

    public void toggle() {
        running = !running;
    }

    public Boolean isRunning() {
        return running;
    }

    public static Application getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Application();
        }

        return INSTANCE;
    }
}
