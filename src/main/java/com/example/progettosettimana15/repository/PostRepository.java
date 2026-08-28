package com.example.progettosettimana15.repository;

import com.example.progettosettimana15.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {


    List<Post> findByUtente_Id(UUID utenteid);
}
