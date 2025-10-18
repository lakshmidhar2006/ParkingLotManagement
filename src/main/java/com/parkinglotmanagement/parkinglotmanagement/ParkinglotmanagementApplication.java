package com.parkinglotmanagement.parkinglotmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.parkinglotmanagement.parkinglotmanagement.model.Role;
import com.parkinglotmanagement.parkinglotmanagement.repository.RoleRepository;

@SpringBootApplication
public class ParkinglotmanagementApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ParkinglotmanagementApplication.class, args);

        // This code will run once on startup to create the roles
        RoleRepository roleRepository = context.getBean(RoleRepository.class);
        if (roleRepository.findByName("USER").isEmpty()) {
            Role userRole = new Role();
            userRole.setName("USER");
            roleRepository.save(userRole);
        }
        if (roleRepository.findByName("PARKING_LOT_MANAGER").isEmpty()) {
            Role adminRole = new Role();
            adminRole.setName("PARKING_LOT_MANAGER");
            roleRepository.save(adminRole);
        }
    }
}