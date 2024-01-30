package com.kemzeb.planetviewer.repository;

import com.kemzeb.planetviewer.entity.Star;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StarRepository extends JpaRepository<Star, String> {}
