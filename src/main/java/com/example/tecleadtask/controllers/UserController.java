package com.example.tecleadtask.controllers;

import com.example.tecleadtask.dto.UserDTO;
import com.example.tecleadtask.entities.UserEntity;
import com.example.tecleadtask.services.UserService;
import com.example.tecleadtask.util.ApiConstants;
import com.example.tecleadtask.util.EntityConverter;
import com.example.tecleadtask.util.SortAlgorithm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.example.tecleadtask.util.ApiConstants.BASE_URL;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Tag(name = "Teclead Task Application", description = "An Application to find users.")
@RestController
@RequiredArgsConstructor
@RequestMapping(BASE_URL)
public class UserController {

    private final UserService userService;

    private final String host = "http://localhost:8080/api/v1/";

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
    @PostMapping(ApiConstants.SAVE_USER)
    public HttpEntity<UserDTO> saveUser(@Valid @RequestBody UserDTO userDTO) {
        UserEntity userEntity = userService.saveUser(EntityConverter.convertFromUserDTO(userDTO));
        UserDTO userDto = EntityConverter.convertFromUserEntity(userEntity);
        userDto.add(linkTo(methodOn(UserController.class).saveUser(userDTO)).withSelfRel());
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get all users.",
            description = "Get all users.",
            responses = {
                    @ApiResponse(responseCode = "200", content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(ref = "#/components/schemas/Collection")
                            )
                    }),
                    @ApiResponse(responseCode = "204", description = "No Content", content = @Content)
            })
    @GetMapping(ApiConstants.GET_USERS)
    public ResponseEntity<CollectionModel<UserDTO>> findPaginatedUsers(@Parameter(description = "Number of the result set page.") @RequestParam(value = "page", defaultValue = "0") int page,
                                                                       @Parameter(description = "Number of results on the page.") @RequestParam(value = "size", defaultValue = "10") int size,
                                                                       @Parameter(description = "Field sort criteria.") @RequestParam(value = "field", defaultValue = "vorname") String field,
                                                                       @Parameter(description = "Sort algorithm.") @RequestParam(value = "sortAlg", defaultValue = "ASC") SortAlgorithm sortAlg) {

        Sort sort = Sort.by(field);
        if (sortAlg.equals(SortAlgorithm.ASC)) {
            sort.ascending();
        } else {
            sort.descending();
        }

        Page<UserEntity> site = userService.findAllUsersWithPagination(page, size, sort);
        int pageSizes = site.getTotalPages();
        int preSite = page > 0 && page <= pageSizes ? page - 1 : page;
        int nextSite = page < pageSizes - 1 ? page + 1 : page;

        List<UserDTO> users = site
                .stream()
                .map(EntityConverter::convertFromUserEntity)
                .toList()
                .stream()
                .map(userDto -> (UserDTO) userDto.add(linkTo(methodOn(UserController.class).findUserById(userDto.getId())).withRel("findUser"))).toList();

        var collectionModel = CollectionModel.of(users);
        collectionModel.add(linkTo(methodOn(UserController.class).findPaginatedUsers(preSite, size, field, sortAlg)).withRel("before"));
        collectionModel.add(linkTo(methodOn(UserController.class).findPaginatedUsers(nextSite, size, field, sortAlg)).withRel("next"));

        if (!users.isEmpty()) {
            return ResponseEntity.ok(collectionModel);
        } else {
            return new ResponseEntity<>(collectionModel, HttpStatus.NO_CONTENT);
        }
    }

    @Operation(
            summary = "Get a user account by identifier.",
            description = "Get a user account by identifier.",
            responses = {
                    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json")}),
                    @ApiResponse(responseCode = "204", description = "No Content", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Server Error", content = @Content),
            })
    @GetMapping(ApiConstants.FIND_USER_BY_ID)
    public ResponseEntity<UserDTO> findUserById(@Parameter(description = "Id of user to be searched.") @PathVariable Long id) {
        Optional<UserEntity> user = userService.findUserById(id);

        return user.map(value -> new ResponseEntity<>(EntityConverter.convertFromUserEntity(value), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @Operation(
            summary = "Delete an user.",
            description = "Delete a user.",
            responses = {
                    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json")}),
                    @ApiResponse(responseCode = "500", description = "Server Error", content = @Content)
            })
    @DeleteMapping(ApiConstants.DELETE_USER)
    public void deleteUser(@RequestBody UserDTO userDTO) {
        userService.deleteUser(EntityConverter.convertFromUserDTO(userDTO));
    }

    @Operation(
            summary = "Delete a user by identifier.",
            description = "Delete a user by identifier.",
            responses = {
                    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json")}),
                    @ApiResponse(responseCode = "500", description = "Server Error", content = @Content),
            })
    @DeleteMapping(ApiConstants.DELETE_USER_BY_ID)
    public void deleteUserById(@Parameter(description = "Id of user to be searched.") @PathVariable Long id) {
        userService.deleteUserById(id);
    }

    @Operation(
            summary = "Update an user.",
            description = "Update an user.",
            responses = {
                    @ApiResponse(responseCode = "200", content = {@Content(schema =
                    @Schema(implementation = UserDTO.class), mediaType = "application/json")}),
                    @ApiResponse(responseCode = "500", description = "Server Error", content = @Content),
            })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(ApiConstants.UPDATE_USER)
    public UserDTO updateUser(@RequestBody UserDTO userDTO) {
        UserEntity userEntity = EntityConverter.convertFromUserDTO(userDTO);
        UserEntity updatedUserEntity = userService.updateUser(userEntity);
        return EntityConverter.convertFromUserEntity(updatedUserEntity);
    }

    @Operation(
            summary = "Get user entities related to the vorName.",
            description = "Get user entities related to the vorName.",
            responses = {
                    @ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(
                            schema = @Schema(implementation = UserDTO.class)), mediaType = "application/json")}),
                    @ApiResponse(responseCode = "204", content = {@Content(schema = @Schema())})
            })
    @GetMapping(ApiConstants.FIND_USER_BY_VORNAME)
    public ResponseEntity<List<UserDTO>> findByVornamen(@Parameter(description = "Id of user to be searched.") @RequestParam("vorname") String vorname) {
        List<UserEntity> userEntities = userService.findByVorname(vorname);
        if (!userEntities.isEmpty()) {
            List<UserDTO> kontenDAO = userEntities.stream().map(EntityConverter::convertFromUserEntity)
                    .toList();
            return new ResponseEntity<>(kontenDAO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
