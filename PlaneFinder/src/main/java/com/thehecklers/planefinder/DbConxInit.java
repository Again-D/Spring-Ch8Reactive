package com.thehecklers.planefinder;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

// 데이터베이스 연결 초기화 클래스
@Configuration
public class DbConxInit {

    // ConnectionFactoryInitializer 처리를위한 빈 등록
    @Bean
    public ConnectionFactoryInitializer initializer(
            @Qualifier("connectionFactory") ConnectionFactory connectionFacotry) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFacotry);
        initializer.setDatabasePopulator(
                new ResourceDatabasePopulator(new ClassPathResource("schema.sql"))
        );
        return initializer;
    }

    // 테스트를 위한 Bean 등록 -
//    @Bean
    public CommandLineRunner init(PlaneRepository repo) {
        return args -> {
            repo.save(new Aircraft("SAL01", "N12345", "SAL01", "LJ",
                    30000, 30, 300,
                    38.7209228, -90.4107416))
                    .thenMany(repo.findAll())
                    .subscribe(System.out::println);
        };
    }
}
