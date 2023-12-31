package com.example.tecleadtask.controllers;

import com.example.tecleadtask.dto.UserDTO;
import com.example.tecleadtask.entities.UserEntity;
import com.example.tecleadtask.hateoas.UserModelAssembler;
import com.example.tecleadtask.services.UserService;
import com.example.tecleadtask.util.ApiConstants;
import com.example.tecleadtask.util.EntityConverter;
import com.example.tecleadtask.util.SortAlgorithm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.example.tecleadtask.util.ApiConstants.*;

@Tag(name = "Teclead Task Application", description = "An Application to find users.")
@RestController
@CrossOrigin(origins ="*")
@RequiredArgsConstructor
@RequestMapping(BASE_URL)
public class UserController {

    private final UserService userService;
    private final UserModelAssembler userModelAssembler;

    @Operation(
            summary = "Create a new user entity.",
            description = "Create a new user entity.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User to update.",
                    content = @Content(
                            schema = @Schema(ref = "#/components/schemas/UserRequest"))),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Created", content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(ref = "#/components/schemas/UserResponse")
                            )
                    }),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Server Error", content = @Content),
            })
    @PostMapping(SAVE_USER)
    public HttpEntity<UserDTO> saveUser(@Valid @RequestBody UserDTO userDTO) {
        UserEntity userEntity = userService.saveUser(EntityConverter.convertFromUserDTO(userDTO));
        return new ResponseEntity<>(userModelAssembler.toModel(userEntity), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get all users.",
            description = "Get all users.",
            responses = {
                    @ApiResponse(responseCode = "200", content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(ref = "#/components/schemas/Users")
                            )
                    }),
                    @ApiResponse(responseCode = "204", description = "No Content", content = @Content)
            })
    @GetMapping(FIND_USERS)
    public ResponseEntity<PagedModel<UserDTO>> findPaginatedUsers(@Parameter(description = "Number of the result set page.") @RequestParam(value = "page", defaultValue = "0") int page,
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
        PagedModel<UserDTO> collectionModel = userModelAssembler.toPagedModel(site);

        if (site.getTotalElements() >= 1) {
            return ResponseEntity.ok(collectionModel);
        } else {
            return new ResponseEntity<>(collectionModel, HttpStatus.NO_CONTENT);
        }
    }

    @Operation(
            summary = "Get a user account by identifier.",
            description = "Get a user account by identifier.",
            responses = {
                    @ApiResponse(responseCode = "200", content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(ref = "#/components/schemas/UserResponse")
                            )
                    }),
                    @ApiResponse(responseCode = "204", description = "No Content", content = @Content(
                            schema = @Schema()
                    )),
                    @ApiResponse(responseCode = "500", description = "Server Error", content = @Content),
            })
    @GetMapping(FIND_USER_BY_ID)
    public ResponseEntity<UserDTO> findUserById(@Parameter(description = "Id of user to be searched.") @PathVariable("id") Long id) {
        Optional<UserEntity> user = userService.findUserById(id);

        return user.map(value -> new ResponseEntity<>(userModelAssembler.toModel(value), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @Operation(
            summary = "Delete an user.",
            description = "Delete a user.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User to delete.",
                    content = @Content(
                            schema = @Schema(ref = "#/components/schemas/UserRequest"))),
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content", content = {@Content(schema = @Schema())}),
                    @ApiResponse(responseCode = "500", description = "Server Error", content = {@Content(schema = @Schema())})
            })
    @DeleteMapping(ApiConstants.DELETE_USER)
    public ResponseEntity<Void> deleteUser(@RequestBody UserDTO userDTO) {
        userService.deleteUser(EntityConverter.convertFromUserDTO(userDTO));
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Delete a user by identifier.",
            description = "Delete a user by identifier.",
            responses = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "500", description = "Server Error"),
            })
    @DeleteMapping(ApiConstants.DELETE_USER_BY_ID)
    public ResponseEntity<Void> deleteUserById(@Parameter(description = "Id of user to be searched.") @PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Update an user.",
            description = "Update an user.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User to update.",
                    content = @Content(
                            schema = @Schema(ref = "#/components/schemas/UserRequest"))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "ok", content = {@Content(schema = @Schema())}),
                    @ApiResponse(responseCode = "500", description = "Server Error", content = {@Content(schema = @Schema())}),
            })
    @PutMapping(ApiConstants.UPDATE_USER)
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO) {
        UserEntity userEntity = EntityConverter.convertFromUserDTO(userDTO);
        UserEntity updatedUserEntity = userService.updateUser(userEntity);
        return ResponseEntity.ok(userModelAssembler.toModel(updatedUserEntity));
    }

    @Operation(
            summary = "Get user entities related to the Vorname.",
            description = "Get user entities related to the Vorname.",
            responses = {
                    @ApiResponse(responseCode = "200", content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(ref = "#/components/schemas/Users")
                            )
                    }),
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
