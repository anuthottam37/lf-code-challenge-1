package com.labforward.api.hello.controller;

import com.labforward.api.hello.domain.Greeting;
import com.labforward.api.hello.service.HelloWorldService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/hello")
public class HelloController {

	private HelloWorldService helloWorldService;

	public HelloController(HelloWorldService helloWorldService) {
		this.helloWorldService = helloWorldService;
	}
	
	@GetMapping
	@ResponseBody
	public ResponseEntity<List<Greeting>> getAllGreetings() {
		return ResponseEntity.ok(helloWorldService.getGreetings());
	}
	
	@GetMapping("/{id}")
	@ResponseBody
	public ResponseEntity<Greeting> getGreeting(@PathVariable String id) {
		return ResponseEntity.ok(helloWorldService.getGreeting(id));
	}

	@PostMapping
	public ResponseEntity<Greeting> createGreeting(@RequestBody Greeting request) {
		return new ResponseEntity<>(helloWorldService.createGreeting(request), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Greeting> updateGreeting(@PathVariable String id, @RequestBody Greeting request) {
		request.setId(id);
		return ResponseEntity.ok(helloWorldService.updateGreeting(request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteGreeting(@PathVariable String id) {
		helloWorldService.deleteGreeting(id);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
}
