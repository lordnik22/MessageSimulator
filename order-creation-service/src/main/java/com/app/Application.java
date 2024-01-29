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

package com.app;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;


@SpringBootApplication
public class Application {

    private final Logger logger = LoggerFactory.getLogger(Application.class);
    private static Boolean running = false;
    private int interval = 5000;

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
            int i = 0;
            while(true) {
                if (this.running) {
                    kafkaTemplate.send("orderCreated", "test_" + i);
                    System.out.println("Send message");
                } else {
                    System.out.println("Idle");
                }

                // Thread.sleep(Math.random() * 10000);
                Thread.sleep(this.interval);
                i++;
            }
		};
	}

    public void start() {
        System.out.println("START");
        this.running = true;
    }

    public void stop() {
        System.out.println("STOP");
        this.running = false;
    }

    public void interval(int interval) {
        System.out.println(String.format("SET Interval to %dms", interval));
        this.interval = interval;
    }

    public void toggle() {
        this.running = !this.running;
    }

    public Boolean isRunning() {
        return this.running;
    }

    public static Application getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Application();
        }

        return INSTANCE;
    }
}
