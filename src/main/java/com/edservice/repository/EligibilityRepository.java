package com.edservice.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edservice.entity.EligibilityEntity;

public interface EligibilityRepository extends JpaRepository<EligibilityEntity, Serializable>{

}
