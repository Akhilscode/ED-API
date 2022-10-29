package com.edservice.binding;


import lombok.Data;

@Data
public class EligibilityDetermination {
	
	private String planName;
	private String planStatus;
	private String planStartDate;
	private String planEndDate;
	private String benefitAmnt;
	private String denialReason;

}
