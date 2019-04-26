package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;


@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

	@Autowired
	MeetingService meetingService;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getMeetings() {
		Collection<Meeting> meetings = meetingService.getAll();
		return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getMeeting(@PathVariable("title") String title) {
	     Meeting meeting = meetingService.findByTitle(title);
	     if (meeting == null) {
	         return new ResponseEntity(HttpStatus.NOT_FOUND);
	     }
	     return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	 }
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	 public ResponseEntity<?> removeMeeting(@PathVariable("title") String title){
		Meeting removedMeeting = meetingService.findByTitle(title);
	     if (removedMeeting == null) {
	    	 return new ResponseEntity(HttpStatus.NOT_FOUND);
	     }
		meetingService.remove(removedMeeting);
		return new ResponseEntity<Meeting>(removedMeeting, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	 public ResponseEntity<?> addMeeting(@RequestBody Meeting meeting){
		Meeting foundMeeting = meetingService.findByTitle(meeting.getTitle());
	     if (foundMeeting != null) {
	         return new ResponseEntity("Unable to create. A meeting with title " + meeting.getTitle() + " already exist.", HttpStatus.CONFLICT);
	     }
		meetingService.add(meeting);
		return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
		
	}
	
	@RequestMapping(value = "", method = RequestMethod.PUT)
	 public ResponseEntity<?> addParticipantToMeeting(@PathVariable ("id") String title, @RequestBody Participant participant){
		
		Meeting foundMeeting = meetingService.findByTitle(title);
	     if (foundMeeting == null) {
	         return new ResponseEntity("A meeting with title " + title + " doesn't exist. Couldn't add participant to it.", HttpStatus.CONFLICT);
	     }
	     foundMeeting.addParticipant(participant);
		meetingService.add(foundMeeting);
		return new ResponseEntity<Meeting>(foundMeeting, HttpStatus.CREATED);
		
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	 public ResponseEntity<?> updateMeeting(@PathVariable("id") long id, @RequestBody Meeting updatedMeeting){
		Meeting meeting = meetingService.findById(id);
	     if (meeting == null) {
	    	 return new ResponseEntity(HttpStatus.NOT_FOUND);
	     }
	    meeting.setTitle(updatedMeeting.getTitle());
	    meeting.setDescription(updatedMeeting.getDescription());
		meetingService.update(meeting);
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
		
	}

	
	@RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
	public ResponseEntity<?> getParicipantsFromMeeting(@PathVariable("id") String title) {
	     Meeting meeting = meetingService.findByTitle(title);
	     if (meeting == null) {
	         return new ResponseEntity(HttpStatus.NOT_FOUND);
	     }
	     Collection<Participant> participants = meeting.getParticipants();
		return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
	 }
	

//	

}
