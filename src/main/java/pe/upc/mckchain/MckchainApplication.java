package pe.upc.mckchain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MckchainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MckchainApplication.class, args);
    }

}
