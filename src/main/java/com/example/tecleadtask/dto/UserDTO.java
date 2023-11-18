package com.example.tecleadtask.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.hateoas.server.core.Relation;

@Data
@Relation(collectionRelation = "Users")
public class UserDTO extends BaseDTO {
    String name;
    @NotBlank
    @JsonProperty("vorname")
    String vorname;
    @Email
    @JsonProperty("email")
    String eMail;
}
