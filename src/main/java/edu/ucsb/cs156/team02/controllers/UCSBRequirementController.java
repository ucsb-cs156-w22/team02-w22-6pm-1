package edu.ucsb.cs156.team02.controllers;

import edu.ucsb.cs156.team02.entities.UCSBRequirement;
import edu.ucsb.cs156.team02.repositories.UCSBRequirementRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@Api(description = "UCSBRequirementController")
@RequestMapping("/api/UCSBRequirements")
@RestController
public class UCSBRequirementController extends ApiController {
    @Autowired
    UCSBRequirementRepository UCSBRequirementRepository;

    @Autowired
    ObjectMapper mapper;

    public class RequirementOrError {
        Long id;
        UCSBRequirement requirement;
        ResponseEntity<String> error;

        public RequirementOrError(Long id) {
            this.id = id;
        }
    }

    public RequirementOrError doesRequirementExist(RequirementOrError roe) {

        Optional<UCSBRequirement> optionalRequirement = UCSBRequirementRepository.findById(roe.id);

        if (optionalRequirement.isEmpty()) {
            roe.error = ResponseEntity
                    .badRequest()
                    .body(String.format("id %d not found", roe.id));
        } else {
            roe.requirement = optionalRequirement.get();
        }
        return roe;
    }

    @ApiOperation(value = "Get a requirement entry with a specific id from the table")
    @GetMapping("")
    public ResponseEntity<String> getRequirementById(
            @ApiParam("id") @RequestParam Long id) throws JsonProcessingException {
        loggingService.logMethod();
        RequirementOrError roe = new RequirementOrError(id);

        roe = doesRequirementExist(roe);
        if (roe.error != null) {
            return roe.error;
        }

        String body = mapper.writeValueAsString(roe.requirement);
        return ResponseEntity.ok().body(body);
    }

    @ApiOperation(value = "List all requirements in the database")
    @GetMapping("/all")
    public Iterable<UCSBRequirement> index() {
        loggingService.logMethod();
        Iterable<UCSBRequirement> subreddits = UCSBRequirementRepository.findAll();
        return subreddits;
    }

    @ApiOperation(value = "Create a new requirement entry in the table")
    @PostMapping("/post")
    public UCSBRequirement createEntry(
            @ApiParam("requirementCode")        @RequestParam String requirementCode,
            @ApiParam("requirementTranslation") @RequestParam String requirementTranslation,
            @ApiParam("collegeCode")            @RequestParam String collegeCode,
            @ApiParam("objCode")                @RequestParam String objCode,
            @ApiParam("courseCount")            @RequestParam int courseCount,
            @ApiParam("units")                  @RequestParam int units,
            @ApiParam("inactive")               @RequestParam boolean inactive) {
        loggingService.logMethod();
        UCSBRequirement req = new UCSBRequirement();
        req.setRequirementCode(requirementCode);
        req.setRequirementTranslation(requirementTranslation);
        req.setCollegeCode(collegeCode);
        req.setObjCode(objCode);
        req.setCourseCount(courseCount);
        req.setUnits(units);
        req.setInactive(inactive);
        UCSBRequirement savedSub = UCSBRequirementRepository.save(req);
        return savedSub;
    }
}