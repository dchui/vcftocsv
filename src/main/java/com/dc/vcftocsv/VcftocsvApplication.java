package com.dc.vcftocsv;

import com.dc.vcftocsv.cli.VcfToCsv;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.IOException;

@SpringBootApplication
public class VcftocsvApplication implements CommandLineRunner {

    private final VcfToCsv vcfToCsv;

    public VcftocsvApplication(VcfToCsv vcfToCsv) {
        this.vcfToCsv = vcfToCsv;
    }

    @Override
    public void run(String... args) throws IOException {
        vcfToCsv.run(args);
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(VcftocsvApplication.class).web(WebApplicationType.NONE).run(args);
	}
}

