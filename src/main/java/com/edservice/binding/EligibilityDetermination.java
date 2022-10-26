package com.edservice.binding;

import java.time.LocalDate;

import lombok.Data;

@Data
public class EligibilityDetermination {
	
	private String planName;
	private String planStatus;
	private LocalDate planStartDate;
	private LocalDate planEndDate;
	private Double benefitAmnt;
	private String denialReason;

}
