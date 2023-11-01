package com.example.tecleadtask.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(name="User")
public class UserDTO extends BaseDTO {
    @Schema(name = "name", example="Keßel")
    String name;
    @Schema(name = "vorname", example="Martin", required = true)
    @NotBlank
    @JsonProperty("vorname")
    String vorname;
    @Schema(name = "email", example="info@example.com")
    @Email
    @JsonProperty("email")
    String eMail;
}
