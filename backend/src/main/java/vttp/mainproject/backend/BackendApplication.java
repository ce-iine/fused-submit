package vttp.mainproject.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import vttp.mainproject.backend.service.ApiService;

@SpringBootApplication
public class BackendApplication implements CommandLineRunner{

	@Autowired
	ApiService apiService;

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// apiService.readApi();
	}

}
