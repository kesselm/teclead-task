package com.example.tecleadtask.repositories;

import com.example.tecleadtask.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findByVorname(String vorname);

//    @Query(value = "SELECT u FROM UserEntity u")
//    Page<UserEntity> findUsers(final Pageable pageable);
}
