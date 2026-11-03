package com.pictet.adventureservice.infrastructure.persistence.repository;

import com.pictet.adventureservice.infrastructure.persistence.entity.AdventureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdventureRepository extends JpaRepository<AdventureEntity, Long> {

    Long deleteByBookId(Long bookId);
}