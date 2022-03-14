package com.rakesh.securityjwt.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRoleDTO {

	private long roleId;
	@ApiModelProperty(notes = "roleName",name="roleName",required=true,value="test name")
	private String roleName;
}
/*
@JsonInclude(JsonInclude.Include.NON_NULL)
You can ignore null fields at the class level by using @JsonInclude(Include. NON_NULL) 
to only include non-null fields, thus excluding any attribute whose value is null. 
You can also use the same annotation at the field level to instruct Jackson to ignore 
that field while converting Java object to json if it's null.

@JsonIgnoreProperties(ignoreUnknown = true)
If you are creating a Model class to represent the JSON in Java, then you can annotate 
the class with @JsonIgnoreProperties(ignoreUnknown = true) to ignore any unknown field. 
This means if there is a new field is added tomorrow on JSON which represents your Model 
then Jackson will not throw UnrecognizedPropertyException while parsing JSON in Java. 

 */

