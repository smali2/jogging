/**
 * Bismillah Hirrahman Nirrahim
 */
package com.toptal.controller;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DateController {

	@PostMapping("/date")
    public void date(@RequestParam("date") 
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
        // ...
    }
	
}
