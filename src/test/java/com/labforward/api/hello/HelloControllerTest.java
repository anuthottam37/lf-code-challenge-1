package com.labforward.api.hello;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.labforward.api.common.MVCIntegrationTest;
import com.labforward.api.core.GlobalControllerAdvice;
import com.labforward.api.hello.common.HelloWorldConstants;
import com.labforward.api.hello.domain.Greeting;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloControllerTest extends MVCIntegrationTest {

	private static final String HELLO_LUKE = "Hello Luke";
	private static final String TEST_MSG = "Test Message";

	
	@Test
	public void getHelloDefaultIsOKAndReturnsValidJSON() throws Exception {
		mockMvc.perform(get("/hello/default"))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$.id", is(HelloWorldConstants.DEFAULT_ID)))
		       .andExpect(jsonPath("$.message", is(HelloWorldConstants.DEFAULT_MESSAGE)));
	}
	
	@Test
	public void getHelloIsOKAndReturnsValidJSON() throws Exception {
		mockMvc.perform( MockMvcRequestBuilders
			      .get("/hello/{id}", HelloWorldConstants.DEFAULT_ID)
			      .accept(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk())
			      .andExpect(jsonPath("$.id", is(HelloWorldConstants.DEFAULT_ID)))
			      .andExpect(jsonPath("$.message", is(HelloWorldConstants.DEFAULT_MESSAGE)));
	}

	@Test
	public void returnsBadRequestWhenMessageMissing() throws Exception {
		String body = "{}";
		mockMvc.perform(post("/hello").content(body)
		                              .contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isUnprocessableEntity())
		       .andExpect(jsonPath("$.validationErrors", hasSize(1)))
		       .andExpect(jsonPath("$.validationErrors[*].field", contains("message")));
	}

	@Test
	public void returnsBadRequestWhenUnexpectedAttributeProvided() throws Exception {
		String body = "{ \"tacos\":\"value\" }";
		mockMvc.perform(post("/hello").content(body).contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isBadRequest())
		       .andExpect(jsonPath("$.message", containsString(GlobalControllerAdvice.MESSAGE_UNRECOGNIZED_PROPERTY)));
	}

	@Test
	public void returnsBadRequestWhenMessageEmptyString() throws Exception {
		Greeting emptyMessage = new Greeting(null, "");
		final String body = getGreetingBody(emptyMessage);

		mockMvc.perform(post("/hello").content(body)
		                              .contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isUnprocessableEntity())
		       .andExpect(jsonPath("$.validationErrors", hasSize(1)))
		       .andExpect(jsonPath("$.validationErrors[*].field", contains("message")));
	}

	@Test
	public void createOKWhenRequiredGreetingProvided() throws Exception {
		Greeting hello = new Greeting(null, HELLO_LUKE);
		final String body = getGreetingBody(hello);
		mockMvc.perform( MockMvcRequestBuilders
			      .post("/hello")
			      .content(body)
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
			      .andExpect(status().isCreated())
			      .andExpect(jsonPath("$.message", is(hello.getMessage())));
	}
	
	@Test
	public void updateOKWhenRequiredGreetingProvided() throws Exception {
		Greeting hello = new Greeting(HelloWorldConstants.DEFAULT_ID, TEST_MSG);
		String body = getGreetingBody(hello);
		mockMvc.perform( MockMvcRequestBuilders
			      .put("/hello/{id}", HelloWorldConstants.DEFAULT_ID)
			      .content(body)
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk())
			      .andExpect(jsonPath("$.message", is(TEST_MSG)));
		// Setting message back to original message
		body = getGreetingBody(new Greeting(HelloWorldConstants.DEFAULT_ID, HelloWorldConstants.DEFAULT_MESSAGE));
		mockMvc.perform( MockMvcRequestBuilders
			      .put("/hello/{id}", HelloWorldConstants.DEFAULT_ID)
			      .content(body)
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON));
	}

	@Test
	public void deleteOKWhenRequiredGreetingProvided() throws Exception {
		Greeting hello = new Greeting(null, TEST_MSG);
		String body = getGreetingBody(hello);
		//creating new greeting to perform delete
		MvcResult resultPost = mockMvc.perform( MockMvcRequestBuilders
						.post("/hello")
						.content(body)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
						.andExpect(status().isCreated())
						.andExpect(jsonPath("$.message", is(hello.getMessage())))
						.andReturn();
		Greeting newGreeting = parseResponse(resultPost, Greeting.class);
		String new_id = newGreeting.getId();
		
		mockMvc.perform( MockMvcRequestBuilders.delete("/hello/{id}", new_id) )
					.andExpect(status().isAccepted());
	}
	
	 public static <T> T parseResponse(MvcResult result, Class<T> responseClass) {
		    try {
		      String contentAsString = result.getResponse().getContentAsString();
		      return new ObjectMapper().readValue(contentAsString, responseClass);
		    } catch (IOException e) {
		      throw new RuntimeException(e);
		    }
		  }
	 
	private String getGreetingBody(Greeting greeting) throws JSONException {
		JSONObject json = new JSONObject().put("message", greeting.getMessage());

		if (greeting.getId() != null) {
			json.put("id", greeting.getId());
		}

		return json.toString();
	}

}
