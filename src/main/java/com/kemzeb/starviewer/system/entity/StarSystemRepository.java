package com.kemzeb.starviewer.system.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StarSystemRepository extends JpaRepository<StarSystem, Integer> {}
