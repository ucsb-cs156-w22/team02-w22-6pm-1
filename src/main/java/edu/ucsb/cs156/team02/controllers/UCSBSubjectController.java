package edu.ucsb.cs156.team02.controllers;

import edu.ucsb.cs156.team02.entities.UCSBSubject;
import edu.ucsb.cs156.team02.entities.User;
import edu.ucsb.cs156.team02.models.CurrentUser;
import edu.ucsb.cs156.team02.repositories.UCSBSubjectRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@Api(description = "UCSB Subject")
@RequestMapping("/api/UCSBSubject")
@RestController
@Slf4j
public class UCSBSubjectController extends ApiController{
    @Autowired
    UCSBSubjectRepository ucsbSubjectRepository;

    @Autowired
    ObjectMapper mapper;

    @ApiOperation(value = "Lists all UCSB Subject information in the database")
    @GetMapping("/all")
    public Iterable<UCSBSubject> subjectInfo() {
        loggingService.logMethod();
        Iterable<UCSBSubject> subjects = ucsbSubjectRepository.findAll();
        return subjects;
    }

    @ApiOperation(value = "Create a new UCSB Subject entry")
    @PostMapping("/post")
    public UCSBSubject postUCSBSubject(
            @ApiParam("id") @RequestParam long id,
            @ApiParam("subject code") @RequestParam String subjectCode,
            @ApiParam("subject translation") @RequestParam String subjectTranslation,
            @ApiParam("dept code") @RequestParam String deptCode,
            @ApiParam("college code") @RequestParam String collegeCode,
            @ApiParam("related dept code") @RequestParam String relatedDeptCode,
            @ApiParam("inactive") @RequestParam boolean inactive) {
        loggingService.logMethod();
        
        UCSBSubject newSubject = new UCSBSubject();
        newSubject.setId(id);
        newSubject.setSubjectCode(subjectCode);
        newSubject.setSubjectTranslation(subjectTranslation);
        newSubject.setDeptCode(deptCode);
        newSubject.setCollegeCode(collegeCode);
        newSubject.setRelatedDeptCode(relatedDeptCode);
        newSubject.setInactive(inactive);

        UCSBSubject savedSubject = ucsbSubjectRepository.save(newSubject);
        return savedSubject;
    }


}
