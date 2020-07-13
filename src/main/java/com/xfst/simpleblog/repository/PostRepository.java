package com.xfst.simpleblog.repository;

import com.xfst.simpleblog.repository.data.PostDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<PostDAO, Long> {
    Optional<PostDAO> findByTitle(final String title);

    Optional<List<PostDAO>> findAllByDeletedTimeIsNullOrDeletedTimeAfter(Date now);
}
