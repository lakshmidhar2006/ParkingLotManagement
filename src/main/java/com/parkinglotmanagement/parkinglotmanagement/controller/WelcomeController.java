package com.parkinglotmanagement.parkinglotmanagement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return """
               <html>
                   <body style='font-family: sans-serif; text-align: center; padding-top: 50px;'>
                       <h1>Welcome to the Parking Lot Management API</h1>
                       <p>This is the backend service for the Valcare assignment.</p>
                       <p>You can access the API documentation here:</p>
                       <a href='/swagger-ui/index.html'>/swagger-ui.html</a>
                   </body>
               </html>
               """;
    }
}
