// package com.example.poc.config;

// import com.hazelcast.config.Config;
// import com.hazelcast.config.MapConfig;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// @Configuration
// public class HazelcastConfig {

//     @Bean
//     public Config hazelcastConfig() {
//         Config config = new Config();
//         config.setInstanceName("hazelcastInstance");

//         // Configure the messages cache
//         config.addMapConfig(new MapConfig()
//                 .setName("messagesCache")
//                 .setTimeToLiveSeconds(3600)  // Cache entries live for 1 hour
//                 .setMaxIdleSeconds(1800));  // Entries are removed if idle for 30 minutes

//         return config;
//     }
// }
