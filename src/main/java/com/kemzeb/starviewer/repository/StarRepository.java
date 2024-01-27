package com.kemzeb.starviewer.repository;

import com.kemzeb.starviewer.entity.Star;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StarRepository extends JpaRepository<Star, String> {}
