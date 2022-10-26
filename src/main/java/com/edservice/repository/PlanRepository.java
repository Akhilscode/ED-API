package com.edservice.repository;


import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edservice.entity.PlanEntity;



public interface PlanRepository extends JpaRepository<PlanEntity, Serializable> {
	
	public PlanEntity findByPlanCategoryId(Integer planCategoryId);

}
