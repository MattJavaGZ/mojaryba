package matt.pass.mojaryba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MojaRybkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MojaRybkaApplication.class, args);
    }

}
