package com.edservice.service;


import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edservice.binding.EligibilityDetermination;
import com.edservice.entity.CitizenDetailsEntity;
import com.edservice.entity.CoTrigger;
import com.edservice.entity.DcCaseEntity;
import com.edservice.entity.EducationEntity;
import com.edservice.entity.EligibilityEntity;
import com.edservice.entity.IncomeEntity;
import com.edservice.entity.KidsEntity;
import com.edservice.entity.PlanEntity;
import com.edservice.repository.CitizenDetailsRepository;
import com.edservice.repository.CoTriggerRepository;
import com.edservice.repository.DCCasesRepository;
import com.edservice.repository.EducationRepository;
import com.edservice.repository.EligibilityRepository;
import com.edservice.repository.IncomeRepository;
import com.edservice.repository.KidsRepository;
import com.edservice.repository.PlanRepository;

@Service
public class EDServiceImpl implements EDService {
    
	@Autowired
	private DCCasesRepository dcrepo;
	
	@Autowired
	private IncomeRepository irepo;
	
	@Autowired
	private PlanRepository prepo;
	
	@Autowired
	private CitizenDetailsRepository crepo;
	
	@Autowired
	private KidsRepository krepo;
	
	@Autowired
	private EducationRepository erepo;
	
	@Autowired
	private EligibilityRepository elrepo;
	
	@Autowired
	private CoTriggerRepository corepo;
	
	@Override
	public EligibilityDetermination determinEligilibility(Long caseNum) {
		
		Integer planCategoryId = 0;
		Integer appId = 0;
		String planName = null;
		int age = 0;
		CitizenDetailsEntity citizenentity = null;
		
		Optional<DcCaseEntity> dcentity = dcrepo.findById(caseNum);
		if(dcentity.isPresent()) {
		    planCategoryId = dcentity.get().getPlanCategoryId();
		    appId = dcentity.get().getAppId();
		}
		
		PlanEntity pentity = prepo.findByPlanCategoryId(planCategoryId);
		if(pentity!=null) {
	    planName = pentity.getPlanName();
		}
		
		Optional<CitizenDetailsEntity> centity = crepo.findById(appId);
		if(centity.isPresent()) {
			 citizenentity = centity.get();
			LocalDate dob = citizenentity.getDob();
			LocalDate curr = LocalDate.now();
		     age = Period.between(dob, curr).getYears();
		}
		
		//EligibilityResponse
		 EligibilityDetermination edresponse = checkPlanCondition(caseNum, planName, age);
		 //Eligibility Entity
		 EligibilityEntity eentity = new EligibilityEntity();
		 BeanUtils.copyProperties(edresponse, eentity);
		 
		 eentity.setCaseNum(caseNum);
		 eentity.setHolderName(citizenentity.getFullName());
		 eentity.setHolderSsn(citizenentity.getSsn());
		 
		 //save the object 
		 elrepo.save(eentity);
		 
		 CoTrigger coentity = new CoTrigger();
		 coentity.setCaseNum(caseNum);
		 coentity.setTriggerStatus("pending");
		 
		 //save the object
		 corepo.save(coentity);
		
		return edresponse;
	}
	
	private EligibilityDetermination checkPlanCondition(Long caseNum, String planName, Integer age) {
		Double empIncome = 0.0;
		Double propertyIncome = 0.0;
		
		//create Eligibility binding class object
		EligibilityDetermination edresponse = new EligibilityDetermination();
		edresponse.setPlanName(planName);
		IncomeEntity ientity = irepo.findByCaseNum(caseNum);
		if(ientity != null) {
			 empIncome = ientity.getEmpIncome();
			 propertyIncome = ientity.getPropertyIncome();
		}
		
		if(planName.equals("SNAP")) {
			if(empIncome <= 300) {
				edresponse.setPlanStatus("Approved");
			}
			else {
				edresponse.setPlanStatus("Denied");
				edresponse.setDenialReason("High Income");
			}
		}
		else if(planName.equals("CCAP")) {
			
			Boolean kidsCountCondition = false;
			Boolean kidsAgeCondition = true;
			
			List<KidsEntity> kids = krepo.findByCaseNum(caseNum);
			if(!kids.isEmpty()) {
			    kidsCountCondition = true;
			    for(KidsEntity kid : kids) {
			    	if(kid.getAge() > 16) {
			    		kidsAgeCondition = false;
			    		break;
			    	}
			    }
			}
			//check the condition for CCAP
			if(empIncome <= 300 && kidsCountCondition && kidsAgeCondition) {
				edresponse.setPlanStatus("Approved");
			}
			else {
				edresponse.setPlanStatus("Denied");
				edresponse.setDenialReason("High Income, kidsCountCondition and kidsAgeConditon failed");
			}
			
		}
        else if(planName.equals("Medicaide")) {
			if(empIncome <= 300 && propertyIncome == 0) {
				edresponse.setPlanStatus("Approved");
			}
			else {
				edresponse.setPlanStatus("Denied");
				edresponse.setDenialReason("High Income");
			}
		}
       else if(planName.equals("Medicare")) {
			if(age > 65) {
				edresponse.setPlanStatus("Approved");
			}
			else {
				edresponse.setPlanStatus("Denied");
				edresponse.setDenialReason("Age condition failed");
			}
		}
       else if(planName.equals("NJW")) {
			EducationEntity eentity = erepo.findByCaseNum(caseNum);
			if(eentity != null) {
				Integer graduateYear = eentity.getYear();
				int currYear = LocalDate.now().getYear();
				if(empIncome == 0 && graduateYear < currYear) {
					edresponse.setPlanStatus("Approved");
				}
				else {
					edresponse.setPlanStatus("Denied");
					edresponse.setDenialReason("Income and Graduation condition failed");
				}
			}
		}
		
		if(edresponse.getPlanStatus().equals("Approved")) {
			edresponse.setPlanStartDate(LocalDate.now().toString());
			edresponse.setPlanEndDate(LocalDate.now().plusMonths(10).toString());
			edresponse.setBenefitAmnt("500.0");
			edresponse.setDenialReason("NA");
		}else {
			edresponse.setBenefitAmnt("NA");
			edresponse.setBenefitAmnt("NA");
			edresponse.setPlanStartDate("NA");			
			edresponse.setPlanEndDate("NA");
		}
		
	return edresponse;
}
		
}
