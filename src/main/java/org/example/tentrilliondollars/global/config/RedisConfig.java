package org.example.tentrilliondollars.global.config;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;
    private static final Logger log = LoggerFactory.getLogger(RedisConfig.class);
    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
            .setAddress("redis://" + redisHost + ":" + redisPort);
        return Redisson.create(config);
    }
//    // Supplier<String> 인터페이스를 구현하는 람다 표현식
//    Supplier<String> helloSupplier = () -> "Hello";
//
//    // get() 메서드를 호출하여 값을 얻음
//    String hello = helloSupplier.get();
//
//    System.out.println(hello); // "Hello" 출력
public <T> T withLock(String lockKey, Supplier<T> supplier) {
    RLock lock = redissonClient().getLock(lockKey);
    boolean isLocked = false;
    try {
        isLocked = lock.tryLock(5, 60, TimeUnit.SECONDS);
        if (isLocked) {
            return supplier.get();
        } else {
            throw new IllegalStateException("Unable to acquire lock");
        }
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new IllegalStateException("Interrupted while acquiring the lock", e);
    } finally {
        if (lock.isHeldByCurrentThread()) {
            log.debug("Releasing lock for key: {}", lockKey);
            lock.unlock();
        }
    }
}


}
