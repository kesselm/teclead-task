package com.example.tecleadtask.entities;

import com.example.tecleadtask.model.User;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class UserEntity extends User {

    private String name;
    private String vorname;
    private String eMail;

}
