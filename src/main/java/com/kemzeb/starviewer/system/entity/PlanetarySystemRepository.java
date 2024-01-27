package com.kemzeb.starviewer.system.entity;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanetarySystemRepository extends JpaRepository<PlanetarySystem, UUID> {}
