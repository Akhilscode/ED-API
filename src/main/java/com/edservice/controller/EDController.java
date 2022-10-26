package com.edservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.edservice.binding.EligibilityDetermination;
import com.edservice.service.EDService;

@RestController
public class EDController {
	
	@Autowired
	private EDService eservice;
  @GetMapping("/determine/{caseNum}")
	public ResponseEntity<EligibilityDetermination> determine(@PathVariable Long caseNum){
		EligibilityDetermination determinEligilibility = eservice.determinEligilibility(caseNum);
		return new ResponseEntity<EligibilityDetermination>(determinEligilibility, HttpStatus.OK);
	}
}
