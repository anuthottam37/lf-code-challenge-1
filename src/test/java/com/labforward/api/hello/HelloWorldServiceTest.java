package com.labforward.api.hello;

import com.labforward.api.core.exception.EntityValidationException;
import com.labforward.api.hello.common.HelloWorldConstants;
import com.labforward.api.hello.domain.Greeting;
import com.labforward.api.hello.service.HelloWorldService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloWorldServiceTest {

	@Autowired
	private HelloWorldService helloService;

	@Test
	public void getDefaultGreetingIsOK() {
		Greeting greeting = helloService.getGreeting(HelloWorldConstants.DEFAULT_ID);
		Assert.assertNotNull(greeting);
		Assert.assertEquals(HelloWorldConstants.DEFAULT_ID, greeting.getId());
		Assert.assertEquals(HelloWorldConstants.DEFAULT_MESSAGE, greeting.getMessage());
	}

	@Test(expected = EntityValidationException.class)
	public void createGreetingWithEmptyMessageThrowsException() {
		final String EMPTY_MESSAGE = "";
		helloService.createGreeting(new Greeting(null, EMPTY_MESSAGE));
	}

	@Test(expected = EntityValidationException.class)
	public void createGreetingWithNullMessageThrowsException() {
		helloService.createGreeting(new Greeting(null, null));
	}

	@Test
	public void createGreetingOKWhenValidRequest() {
		final String HELLO_LUKE = "Hello Luke";
		Greeting request = new Greeting(null, HELLO_LUKE);

		Greeting created = helloService.createGreeting(request);
		Assert.assertEquals(HELLO_LUKE, created.getMessage());
	}
}
