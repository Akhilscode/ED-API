package com.edservice.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edservice.entity.CoTrigger;

public interface CoTriggerRepository extends JpaRepository<CoTrigger, Serializable> {

}
