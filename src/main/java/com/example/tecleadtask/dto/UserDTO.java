package com.example.tecleadtask.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(name="User")
public record UserDTO(
    Long id,
    @Schema(name = "name", example="Keßel")
    String name,
    @Schema(name = "vorname", example="Martin", required = true)
    @NotBlank
    @JsonProperty("vorname")
    String vorName,
    @Schema(name = "email", example="info@example.com")
    @Email
    @JsonProperty("email")
    String eMail
){
}
