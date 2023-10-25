package com.example.tecleadtask.controllers;

import com.example.tecleadtask.dto.UserDTO;
import com.example.tecleadtask.entities.User;
import com.example.tecleadtask.services.UserService;
import com.example.tecleadtask.util.ApiConstants;
import com.example.tecleadtask.util.EntityConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityExistsException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Tag(name = "Teclead Task Application", description = "An Application to find users.")
@RestController
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @Operation(
            summary = "Create a new user entity.",
            description = "Create a new user entity.",
            responses = {
                    @ApiResponse(responseCode = "400", description = "Die Anfrage ist ungültig."),
                    @ApiResponse(responseCode = "201", description = "Eine neue Resource wurde gemäß Anfrage erstellt.",
                            content = {@Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json")}),
            })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(ApiConstants.SAVE_USER)
    public UserDTO saveUser(@Valid @RequestBody UserDTO userDTO) {
            User user = userService.saveUser(EntityConverter.convertFromUserDTO(userDTO));
            return EntityConverter.convertFromUserEntity(user);
    }

    @Operation(
            summary = "Get all users.",
            description = "Get all users."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(
                    schema = @Schema(implementation = UserDTO.class)), mediaType = "application/json")}),
            @ApiResponse(responseCode = "204", content = {@Content(schema = @Schema())})
    })
    @GetMapping(ApiConstants.GET_USERS)
    public ResponseEntity<List<UserDTO>> findAllUsers() {
        List<UserDTO> userDTOS = userService.findAllUsers()
                .stream().map(EntityConverter::convertFromUserEntity)
                .toList();
        if (!userDTOS.isEmpty()) {
            return new ResponseEntity<>(userDTOS, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(userDTOS, HttpStatus.NO_CONTENT);
        }
    }

    @Operation(
            summary = "Get a bank account by identifier.",
            description = "Get a bank account by identifier."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "204", content = {@Content(schema = @Schema())})
    })
    @GetMapping(ApiConstants.FIND_USER_BY_ID)
    public ResponseEntity<UserDTO> findUserById(@PathVariable Long id) {
        Optional<User> user = userService.findUserById(id);

        if (user.isPresent()) {
            return new ResponseEntity<>(EntityConverter.convertFromUserEntity(user.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @Operation(
            summary = "Delete an user.",
            description = "Delete a user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json")})
    })
    @DeleteMapping(ApiConstants.DELETE_USER)
    public void deleteBank(@RequestBody UserDTO userDTO) {
        userService.deleteUser(EntityConverter.convertFromUserDTO(userDTO));
    }

    @Operation(
            summary = "Delete a user by identifier.",
            description = "Delete a user by identifier."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json")})
    })
    @DeleteMapping(ApiConstants.DELETE_USER_BY_ID)
    public void deleteBankById(@PathVariable Long id) {
        userService.deleteUserById(id);
    }

    @Operation(
            summary = "Update an user.",
            description = "Update an user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema =
            @Schema(implementation = UserDTO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "204", content = {@Content(schema = @Schema())})
    })
    @PutMapping(ApiConstants.UPDATE_USER)
    public ResponseEntity<UserDTO> updateBank(@RequestBody UserDTO userDTO) {
        Optional<User> userOptional = userService.findUserById(userDTO.id());
        if (userOptional.isPresent()) {
            var oldUser = User.builder()
                    .id(userDTO.id())
                    .name(userDTO.name())
                    .vorname(userDTO.vorName())
                    .eMail(userDTO.eMail())
                    .build();
            User newUser = userService.saveUser(oldUser);
            UserDTO newUserDTO = EntityConverter.convertFromUserEntity(newUser);
            return new ResponseEntity<>(newUserDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @Operation(
            summary = "Get user entities related to the vorName.",
            description = "Get user entities related to the vorName."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(
                    schema = @Schema(implementation = UserDTO.class)), mediaType = "application/json")}),
            @ApiResponse(responseCode = "204", content = {@Content(schema = @Schema())})
    })
    @GetMapping(ApiConstants.FIND_USER_BY_VORNAME)
    public ResponseEntity<List<UserDTO>> getAllKonten(@RequestParam("vorname") String vorname) {
        List<User> users = userService.findByVorname(vorname);
        if (!users.isEmpty()) {
            List<UserDTO> kontenDAO = users.stream().map(EntityConverter::convertFromUserEntity)
                    .toList();
            return new ResponseEntity<>(kontenDAO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, EntityExistsException.class})
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
