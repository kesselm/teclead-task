package com.example.tecleadtask.dto;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class BaseDTO extends RepresentationModel<BaseDTO> {
    private Long id;
}
