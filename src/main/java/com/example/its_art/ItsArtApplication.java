package com.example.its_art;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.Country;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

@SpringBootApplication
public class ItsArtApplication {

	public static void main(String[] args) throws IOException {



		SpringApplication.run(ItsArtApplication.class, args);
	}

}

