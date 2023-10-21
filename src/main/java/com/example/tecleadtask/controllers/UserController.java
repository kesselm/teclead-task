package com.example.tecleadtask.controllers;

import com.example.tecleadtask.dao.UserDAO;
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
            description = "Create a new user entity."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {@Content(schema = @Schema(implementation = UserDAO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())})
    })
    @PostMapping(ApiConstants.SAVE_USER)
    @ResponseStatus(HttpStatus.CREATED)
    public UserDAO saveUser(@Valid @RequestBody UserDAO userDAO) {
        User user = userService.saveUser(EntityConverter.convertFromUserDAO(userDAO));
        return EntityConverter.convertFromUserEntity(user);
    }

    @Operation(
            summary = "Get all users.",
            description = "Get all users."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(
                    schema = @Schema(implementation = UserDAO.class)), mediaType = "application/json")}),
            @ApiResponse(responseCode = "204", content = {@Content(schema = @Schema())})
    })
    @GetMapping(ApiConstants.GET_USERS)
    public ResponseEntity<List<UserDAO>> findAllUsers() {
        List<UserDAO> userDAOS = userService.findAllUsers()
                .stream().map(EntityConverter::convertFromUserEntity)
                .toList();
        if (!userDAOS.isEmpty()) {
            return new ResponseEntity<>(userDAOS, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(userDAOS, HttpStatus.NO_CONTENT);
        }
    }

    @Operation(
            summary = "Get a bank account by identifier.",
            description = "Get a bank account by identifier."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = UserDAO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "204", content = {@Content(schema = @Schema())})
    })
    @GetMapping(ApiConstants.FIND_USER_BY_ID)
    public ResponseEntity<UserDAO> findUserById(@PathVariable Long id) {
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
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = UserDAO.class), mediaType = "application/json")})
    })
    @DeleteMapping(ApiConstants.DELETE_USER)
    public void deleteBank(@RequestBody UserDAO userDAO) {
        userService.deleteUser(EntityConverter.convertFromUserDAO(userDAO));
    }

    @Operation(
            summary = "Delete a user by identifier.",
            description = "Delete a user by identifier."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = UserDAO.class), mediaType = "application/json")})
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
            @Schema(implementation = UserDAO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "204", content = {@Content(schema = @Schema())})
    })
    @PutMapping(ApiConstants.UPDATE_USER)
    public ResponseEntity<UserDAO> updateBank(@RequestBody UserDAO userDAO) {
        Optional<User> userOptional = userService.findUserById(userDAO.id());
        if (userOptional.isPresent()) {
            User oldUser = new User();
            oldUser.setId(userDAO.id());
            oldUser.setName(userDAO.name());
            oldUser.setVorname(userDAO.vorName());
            User newUser = userService.saveUser(oldUser);
            UserDAO newUserDAO = EntityConverter.convertFromUserEntity(newUser);
            return new ResponseEntity<>(newUserDAO, HttpStatus.OK);
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
                    schema = @Schema(implementation = UserDAO.class)), mediaType = "application/json")}),
            @ApiResponse(responseCode = "204", content = {@Content(schema = @Schema())})
    })
    @GetMapping(ApiConstants.FIND_USER_BY_VORNAME)
    public ResponseEntity<List<UserDAO>> getAllKonten(@RequestParam("vorname") String vorname) {
        List<User> users = userService.findByVorname(vorname);
        if (!users.isEmpty()) {
            List<UserDAO> kontenDAO = users.stream().map(EntityConverter::convertFromUserEntity)
                    .toList();
            return new ResponseEntity<>(kontenDAO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
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
