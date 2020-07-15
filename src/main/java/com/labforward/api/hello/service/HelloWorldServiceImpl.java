package com.labforward.api.hello.service;

import com.labforward.api.core.exception.ResourceNotFoundException;
import com.labforward.api.core.validation.EntityValidator;
import com.labforward.api.hello.common.HelloWorldConstants;
import com.labforward.api.hello.domain.Greeting;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class HelloWorldServiceImpl implements HelloWorldService {

	private Map<String, Greeting> greetings;

	private EntityValidator entityValidator;

	public HelloWorldServiceImpl(EntityValidator entityValidator) {
		this.entityValidator = entityValidator;

		this.greetings = new HashMap<>(1);
		save(getDefault());
	}
	
	private static Greeting getDefault() {
		return new Greeting(HelloWorldConstants.DEFAULT_ID, HelloWorldConstants.DEFAULT_MESSAGE);
	}

	@Override
	public List<Greeting> getGreetings() {
		if (greetings.isEmpty()) {
			throw new ResourceNotFoundException(HelloWorldConstants.GREETING_NOT_FOUND);
		} 
		return new ArrayList<>(greetings.values());
	}
	
	@Override
	public Greeting createGreeting(Greeting request) {
		entityValidator.validateCreate(request);

		request.setId(UUID.randomUUID().toString());
		return save(request);
	}

	@Override
	public Greeting getGreeting(String id) {
		validate(id);
		return greetings.get(id);
	}

	@Override
	public Greeting updateGreeting(Greeting greeting) {
		if (greeting == null) {
			throw new ResourceNotFoundException(HelloWorldConstants.GREETING_NOT_FOUND);
		}
		Greeting updateGreeting = getGreeting(greeting.getId());
		updateGreeting.setMessage(greeting.getMessage());
		return save(greeting);
	}

	@Override
	public void deleteGreeting(String id) {
		validate(id);
		greetings.remove(id);
	}

	private void validate(String id) {
		if (id == null || !greetings.containsKey(id)) {
			throw new ResourceNotFoundException(HelloWorldConstants.GREETING_NOT_FOUND);
		}
	}
	
	private Greeting save(Greeting greeting) {
		this.greetings.put(greeting.getId(), greeting);
		return greeting;
	}

}
