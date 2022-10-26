package com.edservice.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name  = "ELIGIBILITY_MASTER")
@Data
public class EligibilityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
 	private  Integer eligibilityId;
	private Long caseNum;
	private String planName;
	private String holderName;
	private Long holderSsn;
	private LocalDate planStartDate;
	private LocalDate planEndDate;
	private String planStatus;
	private Double benefitAmnt;
	private String denialReason;
}
