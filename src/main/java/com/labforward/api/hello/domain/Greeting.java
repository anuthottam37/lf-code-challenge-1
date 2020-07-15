package com.labforward.api.hello.domain;

import com.labforward.api.core.validation.Entity;
import com.labforward.api.core.validation.EntityUpdateValidatorGroup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * Simple greeting message for dev purposes
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Greeting implements Entity {

	@NotEmpty(groups = {EntityUpdateValidatorGroup.class})
	private String id;

	@NotEmpty
	private String message;

}
