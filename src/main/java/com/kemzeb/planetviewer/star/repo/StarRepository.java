package com.kemzeb.planetviewer.star.repo;

import com.kemzeb.planetviewer.star.entity.Star;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StarRepository extends JpaRepository<Star, String> {}
