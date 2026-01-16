package com.dospace.dosurvey.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;
import org.redisson.config.SubscriptionMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Value("${spring.data.redis.master.host:localhost}")
    private String redisMasterHost;

    @Value("${spring.data.redis.master.port:6379}")
    private int redisMasterPort;

    @Value("${spring.data.redis.password:dospace}")
    private String redisPassword;

    @Value("${spring.data.redis.replicas:localhost:6380,localhost:6381}")
    private String redisReplicas;

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        Config config = new Config();

        String masterAddress = "redis://" + redisMasterHost + ":" + redisMasterPort;

        // Parse replica addresses
        String[] replicaHosts = redisReplicas.split(",");
        String[] slaveAddresses = new String[replicaHosts.length];
        for (int i = 0; i < replicaHosts.length; i++) {
            slaveAddresses[i] = "redis://" + replicaHosts[i].trim();
        }

        config.setPassword(redisPassword.isEmpty() ? null : redisPassword)
                .useMasterSlaveServers()
                .setMasterAddress(masterAddress)
                .addSlaveAddress(slaveAddresses)
                .setReadMode(ReadMode.SLAVE)
                .setSubscriptionMode(SubscriptionMode.SLAVE)
                .setMasterConnectionMinimumIdleSize(5)
                .setMasterConnectionPoolSize(20)
                .setSlaveConnectionMinimumIdleSize(5)
                .setSlaveConnectionPoolSize(20);

        return Redisson.create(config);
    }
}

