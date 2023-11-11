package com.example.tecleadtask.repositories;

import com.example.tecleadtask.entities.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPageableRepository extends PagingAndSortingRepository<UserEntity, Long> {
}
