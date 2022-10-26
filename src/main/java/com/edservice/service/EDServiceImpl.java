package com.edservice.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edservice.binding.EligibilityDetermination;
import com.edservice.entity.CitizenDetailsEntity;
import com.edservice.entity.DcCaseEntity;
import com.edservice.entity.EducationEntity;
import com.edservice.entity.EligibilityEntity;
import com.edservice.entity.IncomeEntity;
import com.edservice.entity.KidsEntity;
import com.edservice.entity.PlanEntity;
import com.edservice.repository.CitizenDetailsRepository;
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
	
	@Override
	public EligibilityDetermination determinEligilibility(Long caseNum) {
		
		Integer planCategoryId = 0;
		Integer appId = 0;
		Optional<DcCaseEntity> dcentity = dcrepo.findById(caseNum);
		if(dcentity.isPresent()) {
		    planCategoryId = dcentity.get().getPlanCategoryId();
		    appId = dcentity.get().getAppId();
		}
		switch (planCategoryId) {
		case 1:
			  EligibilityDetermination determineSnap = determineSnap(caseNum, appId, planCategoryId);
			  return determineSnap;
		case 2:
			  EligibilityDetermination determineCcap = determineCcap(caseNum, appId, planCategoryId);
              return determineCcap;
		case 3:
		         EligibilityDetermination determineMedicait = determineMedicait(caseNum, appId, planCategoryId);
		         return determineMedicait;		    
		case 4:
			     EligibilityDetermination determineMedicare = determineMedicare(caseNum, appId, planCategoryId);
			     return determineMedicare;
		case 5:
			    EligibilityDetermination determineNJW = determineNJW(caseNum, appId, planCategoryId);
			    return determineNJW;
		}
		return null;
	}
	
	private EligibilityDetermination determineSnap(Long caseNum, Integer appId, Integer planCategoryId) {
		
		EligibilityEntity eentity = new EligibilityEntity();
		EligibilityDetermination ed = new EligibilityDetermination();
		IncomeEntity ientity = irepo.findByCaseNum(caseNum);
		 Optional<CitizenDetailsEntity>  centity = crepo.findById(appId);
		 PlanEntity pentity = prepo.findByPlanCategoryId(planCategoryId);
		if(ientity != null && centity.isPresent() && pentity!=null) {
			   CitizenDetailsEntity centity1 = centity.get();
			if(ientity.getEmpIncome() <= 300) {
				eentity.setCaseNum(caseNum);
				eentity.setHolderName(centity1.getFullName());
				eentity.setCaseNum(centity1.getSsn());
				eentity.setPlanName(pentity.getPlanName());
				eentity.setPlanStartDate(pentity.getUpdatedDate());
				eentity.setPlanEndDate(pentity.getPlanEndDate());
				eentity.setPlanStatus("Approved");
				eentity.setBenefitAmnt(300.0);
				eentity.setDenialReason("NA");
				elrepo.save(eentity);
			}
			else {
				eentity.setPlanStatus("Denied");
				eentity.setDenialReason("Income condition failed");
				elrepo.save(eentity);
			}
			BeanUtils.copyProperties(eentity, ed);
			return ed;
		}
		return null;
	}
	
	private EligibilityDetermination determineCcap(Long caseNum, Integer appId, Integer planCategoryId) {
		EligibilityEntity eentity = new EligibilityEntity();
		EligibilityDetermination ed = new EligibilityDetermination();
		IncomeEntity ientity = irepo.findByCaseNum(caseNum);
		 Optional<CitizenDetailsEntity>  centity = crepo.findById(appId);
		 PlanEntity pentity = prepo.findByPlanCategoryId(planCategoryId);
		 List<KidsEntity> kidslst = krepo.findByCaseNum(caseNum);
		if(ientity != null && centity.isPresent() && pentity!=null && kidslst != null) {
			 CitizenDetailsEntity centity1 = centity.get();
			 if(ientity.getEmpIncome() <= 300) {
				 for(KidsEntity kentity : kidslst) {
					 if(kentity.getAge()<=16) {
						    eentity.setCaseNum(caseNum);
							eentity.setHolderName(centity1.getFullName());
							eentity.setCaseNum(centity1.getSsn());
							eentity.setPlanName(pentity.getPlanName());
							eentity.setPlanStartDate(pentity.getUpdatedDate());
							eentity.setPlanEndDate(pentity.getPlanEndDate());
							eentity.setPlanStatus("Approved");
							eentity.setBenefitAmnt(400.0);
							eentity.setDenialReason("NA");
							elrepo.save(eentity);
					 }//if
					 else {
						 eentity.setPlanStatus("Denied");
						 eentity.setDenialReason("Kids age condition failed");
						 elrepo.save(eentity);
					 }//else
				 }//for
				 
			 }//if
			 else {
				 eentity.setPlanStatus("Denied");
				 eentity.setDenialReason("Income condition failed");
				 elrepo.save(eentity);
			 }//else
			 BeanUtils.copyProperties(eentity, ed);
				return ed; 
		}
		 return null;
	}
	
private EligibilityDetermination determineMedicait(Long caseNum, Integer appId, Integer planCategoryId) {
		
		EligibilityEntity eentity = new EligibilityEntity();
		EligibilityDetermination ed = new EligibilityDetermination();
		IncomeEntity ientity = irepo.findByCaseNum(caseNum);
		 Optional<CitizenDetailsEntity>  centity = crepo.findById(appId);
		 PlanEntity pentity = prepo.findByPlanCategoryId(planCategoryId);
		if(ientity != null && centity.isPresent() && pentity!=null) {
			   CitizenDetailsEntity centity1 = centity.get();
			if(ientity.getEmpIncome() <= 300 && ientity.getPropertyIncome() == 0.0) {
				eentity.setCaseNum(caseNum);
				eentity.setHolderName(centity1.getFullName());
				eentity.setCaseNum(centity1.getSsn());
				eentity.setPlanName(pentity.getPlanName());
				eentity.setPlanStartDate(pentity.getUpdatedDate());
				eentity.setPlanEndDate(pentity.getPlanEndDate());
				eentity.setPlanStatus("Approved");
				eentity.setBenefitAmnt(300.0);
				eentity.setDenialReason("NA");
				elrepo.save(eentity);
			}
			else {
				eentity.setPlanStatus("Denied");
				eentity.setDenialReason("Income condition failed");
				elrepo.save(eentity);
			}
			BeanUtils.copyProperties(eentity, ed);
			return ed;
		}
		return null;
	}

private EligibilityDetermination determineMedicare(Long caseNum, Integer appId, Integer planCategoryId) {
	
	EligibilityEntity eentity = new EligibilityEntity();
	EligibilityDetermination ed = new EligibilityDetermination();
	IncomeEntity ientity = irepo.findByCaseNum(caseNum);
	 Optional<CitizenDetailsEntity>  centity = crepo.findById(appId);
	 PlanEntity pentity = prepo.findByPlanCategoryId(planCategoryId);
	if(ientity != null && centity.isPresent() && pentity!=null) {
		   CitizenDetailsEntity centity1 = centity.get();
		if(centity1.getAge()>= 65) {
			eentity.setCaseNum(caseNum);
			eentity.setHolderName(centity1.getFullName());
			eentity.setCaseNum(centity1.getSsn());
			eentity.setPlanName(pentity.getPlanName());
			eentity.setPlanStartDate(pentity.getUpdatedDate());
			eentity.setPlanEndDate(pentity.getPlanEndDate());
			eentity.setPlanStatus("Approved");
			eentity.setBenefitAmnt(300.0);
			eentity.setDenialReason("NA");
			elrepo.save(eentity);
		}
		else {
			eentity.setPlanStatus("Denied");
			eentity.setDenialReason("Age condition failed");
			elrepo.save(eentity);
		}
		BeanUtils.copyProperties(eentity, ed);
		return ed;
	}
	return null;
}

private EligibilityDetermination determineNJW(Long caseNum, Integer appId, Integer planCategoryId) {
	
	EligibilityEntity eentity = new EligibilityEntity();
	EligibilityDetermination ed = new EligibilityDetermination();
	IncomeEntity ientity = irepo.findByCaseNum(caseNum);
	 Optional<CitizenDetailsEntity>  centity = crepo.findById(appId);
	 PlanEntity pentity = prepo.findByPlanCategoryId(planCategoryId);
	 EducationEntity eduentity = erepo.findByCaseNum(caseNum);
	if(ientity != null && centity.isPresent() && pentity!=null &&  eduentity!= null) {
		   CitizenDetailsEntity centity1 = centity.get();
		if(ientity.getEmpIncome() == 0.0 && eduentity.getQualification().equalsIgnoreCase("Graduation")) {
			eentity.setCaseNum(caseNum);
			eentity.setHolderName(centity1.getFullName());
			eentity.setCaseNum(centity1.getSsn());
			eentity.setPlanName(pentity.getPlanName());
			eentity.setPlanStartDate(pentity.getUpdatedDate());
			eentity.setPlanEndDate(pentity.getPlanEndDate());
			eentity.setPlanStatus("Approved");
			eentity.setBenefitAmnt(300.0);
			eentity.setDenialReason("NA");
			elrepo.save(eentity);
		}
		else {
			eentity.setPlanStatus("Denied");
			eentity.setDenialReason("Income and education contition failed");
			elrepo.save(eentity);
		}
		BeanUtils.copyProperties(eentity, ed);
		return ed;
	}
	return null;
}
		
}
