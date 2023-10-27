package com.example.tecleadtask.controllers;

import com.example.tecleadtask.dto.UserDTO;
import com.example.tecleadtask.entities.User;
import com.example.tecleadtask.services.UserService;
import com.example.tecleadtask.util.ApiConstants;
import com.example.tecleadtask.util.EntityConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
                    @ApiResponse(responseCode = "201", description = "Created",
                            content = {@Content(schema = @Schema(implementation = UserDTO.class),
                                    mediaType = "application/json")}),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Server Error", content = @Content),
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
            @ApiResponse(responseCode = "204", description = "No Content", content = @Content)
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
            summary = "Get a user account by identifier.",
            description = "Get a user account by identifier."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "204", description = "No Content", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server Error", content = @Content),
    })
    @GetMapping(ApiConstants.FIND_USER_BY_ID)
    public ResponseEntity<UserDTO> findUserById(@Parameter(description = "Id of user to be searched.") @PathVariable Long id) {
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
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Server Error", content = @Content),
    })
    @DeleteMapping(ApiConstants.DELETE_USER)
    public void deleteUser(@RequestBody UserDTO userDTO) {
        userService.deleteUser(EntityConverter.convertFromUserDTO(userDTO));
    }

    @Operation(
            summary = "Delete a user by identifier.",
            description = "Delete a user by identifier."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Server Error", content = @Content),
    })
    @DeleteMapping(ApiConstants.DELETE_USER_BY_ID)
    public void deleteUserById(@Parameter(description = "Id of user to be searched.") @PathVariable Long id) {
        userService.deleteUserById(id);
    }

    @Operation(
            summary = "Update an user.",
            description = "Update an user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema =
            @Schema(implementation = UserDTO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Server Error", content = @Content),
    })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(ApiConstants.UPDATE_USER)
    public UserDTO updateUser(@RequestBody UserDTO userDTO) {
        User user = EntityConverter.convertFromUserDTO(userDTO);
        User updatedUser = userService.updateUser(user);
        return EntityConverter.convertFromUserEntity(updatedUser);
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
    public ResponseEntity<List<UserDTO>> findByVornamen(@Parameter(description = "Id of user to be searched.") @RequestParam("vorname") String vorname) {
        List<User> users = userService.findByVorname(vorname);
        if (!users.isEmpty()) {
            List<UserDTO> kontenDAO = users.stream().map(EntityConverter::convertFromUserEntity)
                    .toList();
            return new ResponseEntity<>(kontenDAO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
