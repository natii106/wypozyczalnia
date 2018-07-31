package pl.walasik.wypozyczalnia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("pl.walasik.wypozyczalnia.*")
public class WypozyczalniaApplication {

	public static void main(String[] args) {
		SpringApplication.run(WypozyczalniaApplication.class, args);
	}


}

