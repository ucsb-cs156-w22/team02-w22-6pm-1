package edu.ucsb.cs156.team02.controllers;

import edu.ucsb.cs156.team02.entities.CollegiateSubreddit;
import edu.ucsb.cs156.team02.repositories.CollegiateSubredditRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "CollegiateSubredditController")
@RequestMapping("/api/collegiateSubreddits")
@RestController
@Slf4j
public class CollegiateSubredditController extends ApiController {

    @Autowired
    CollegiateSubredditRepository collegiateSubredditRepository;

    @Autowired
    ObjectMapper mapper;

    @ApiOperation(value = "List all collegiate subreddits in the database")
    @GetMapping("/all")
    public Iterable<CollegiateSubreddit> index() {
        loggingService.logMethod();
        Iterable<CollegiateSubreddit> subreddits = collegiateSubredditRepository.findAll();
        return subreddits;
    }

    @ApiOperation(value = "Create a new entry in the table")
    @PostMapping("/post")
    public CollegiateSubreddit createEntry(
            @ApiParam("name")       @RequestParam String name,
            @ApiParam("location")   @RequestParam String location,
            @ApiParam("subreddit")  @RequestParam String subreddit) {
        loggingService.logMethod();
        CollegiateSubreddit sub = new CollegiateSubreddit();
        sub.setName(name);
        sub.setLocation(location);
        sub.setSubreddit(subreddit);
        CollegiateSubreddit savedSub = collegiateSubredditRepository.save(sub);
        return savedSub;
    }

}