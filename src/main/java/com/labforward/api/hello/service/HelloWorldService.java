package com.labforward.api.hello.service;

import java.util.List;

import com.labforward.api.hello.domain.Greeting;

public interface HelloWorldService {
	
	List<Greeting> getGreetings();
	
	Greeting createGreeting(Greeting request);

	Greeting getGreeting(String id);

	Greeting updateGreeting(Greeting greeting);

	void deleteGreeting(String id);

}