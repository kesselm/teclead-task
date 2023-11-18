package com.example.tecleadtask.hateoas;

import com.example.tecleadtask.controllers.UserController;
import com.example.tecleadtask.dto.UserDTO;
import com.example.tecleadtask.entities.UserEntity;
import com.example.tecleadtask.util.EntityConverter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class UserModelAssembler implements RepresentationModelAssembler<UserEntity, UserDTO> {

    final PagedResourcesAssembler<UserEntity> pagedResourcesAssembler;

    @Override
    public UserDTO toModel(UserEntity userEntity) {
        UserDTO userModel  = EntityConverter.convertFromUserEntity(userEntity);
        userModel.add(linkTo(methodOn(UserController.class).findUserById(userModel.getId())).withRel("findUser").withType("GET"));
        userModel.add(linkTo(methodOn(UserController.class).deleteUserById(userModel.getId())).withRel("deleteUser").withType("DELETE"));
        userModel.add(linkTo(methodOn(UserController.class).updateUser(userModel)).withRel("updateUser").withType("PUT"));
        userModel.add(linkTo(methodOn(UserController.class).findByVornamen(userEntity.getVorname())).withRel("findUserByVorname").withType("GET"));
        return userModel;
    }

    public CollectionModel<UserDTO> toCollectionModel(CollectionModel<UserDTO> collectionModel){
        UserDTO userDTO = new UserDTO();
        collectionModel.add(linkTo(methodOn(UserController.class).saveUser(userDTO)).withRel("saveUser").withType("POST"));
        return collectionModel;
    }

    public PagedModel<UserDTO> toPagedModel(Page<UserEntity> site) {
        PagedModel<UserDTO> resources = pagedResourcesAssembler.toModel(site, this);
        toCollectionModel(resources);
        return resources;
    }
}
