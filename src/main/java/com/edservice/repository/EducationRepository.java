package com.edservice.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edservice.entity.EducationEntity;


public interface EducationRepository extends JpaRepository<EducationEntity, Serializable>{
	
	public EducationEntity findByCaseNum(Long caseNum);

}
