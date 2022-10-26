package com.edservice.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Entity
@Table(name="PLAN_MASTER")
@Data
public class PlanEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer planId;
	private Integer appId;
	private String planName;
	private LocalDate planStartDate;		
	private LocalDate planEndDate;
	private Integer planCategoryId;
	private String activeSw;
	
	@CreationTimestamp
	@Column(name="CREATED_DATE", updatable = false )
	private LocalDate createdDate;
	@UpdateTimestamp
	@Column(name="UPDATED_DATE",  insertable = false )
	private LocalDate updatedDate;
	private String createdBy;
	private String updatedBy; 



}
