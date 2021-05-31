package com.jb.ElvinaFinalSpringProject;

import com.jb.ElvinaFinalSpringProject.utils.art;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/***
 * Main entry point for our application
 */
@Slf4j
@SpringBootApplication
public class ElvinaFinalSpringProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(ElvinaFinalSpringProjectApplication.class, args);
        log.info(art.myServer);
    }
}
