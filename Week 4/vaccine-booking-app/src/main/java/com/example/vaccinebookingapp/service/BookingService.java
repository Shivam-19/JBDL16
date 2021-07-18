package com.example.vaccinebookingapp.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.vaccinebookingapp.models.Booking;
import com.example.vaccinebookingapp.models.CompletionStatus;
import com.example.vaccinebookingapp.models.Slot;
import com.example.vaccinebookingapp.models.User;
import com.example.vaccinebookingapp.repository.BookingRepository;

@Service
public class BookingService {

    @Autowired
    BookingRepository bookingRepository;

   @Autowired
   SlotService slotService;
   
   @Autowired
   UserService userService;
    
    public String bookSlotForUser(int userId, int slotId) {

        // user and slot exist
        // slot with given id is old
        // age limit
        // check for previous appointments 
        // - whether user is already vaccinated
        // - PENDING appointment
        // - check for previous vaccine and book the same one
        // - check for minimum number of days
        // see whether vaccines are available in the slot, whether slot is filled or have some vaccines

    	
    	  // user and slot exist
    	User user = userService.getUserById(userId);
    	Slot slot = slotService.getSlotById(slotId);
    	
    	 // slot with given id is old
    	if(slot.getDate().before(new Date())) {
    		//throw exception
    	}
    	
    	//age limit
    	if(!( user.getAge() < slot.getAgeLimit().getMaxAge() && user.getAge() > slot.getAgeLimit().getMinAge())) {
    		//throw excpeiton
    	}
    	
    	
    	 // see whether vaccines are available in the slot, whether slot is filled or have some vaccines
    	if(!isVaccineAvaliableForCurrentSlot(slot)) {
    		
    	}
    	
    	 // - whether user is already vaccinated
    	if(isVaccinated(user)) {
    		
    	}
    	
    	  // - PENDING appointment
    	if(hasPendingAppointments(user)) {
    		
    	}
    	
    	 // - check for previous vaccine and book the same one
    	if(!isCurrentBookingVaccineSameAsPrevious(user, slot)) {
    		
    	}
    	
    	// - check for minimum number of days
    	if(!hasDaysDifferenceForSecondDose(user, slot)) {
    		
    	}
    	
    	return  bookingRepository.save(Booking.builder()  //referenceId ?
    							 .user(user)
    							 .slot(slot)
    							 .completionStatus(CompletionStatus.PENDING)
    							 .build()
    			).getReferenceId();
    	
    }

    public void cancelSlotForUser(int userId, int slotId) {

        // check whether user has really booked the slot, PENDING status
    	User user = userService.getUserById(userId);
    	Slot slot = slotService.getSlotById(slotId);
    	
    	isSlotBookedByUser(user, slot);
    	
    	bookingRepository.deleteById(slotId);
    }
    
    private boolean hasPendingAppointments(User user) {
    	
    	Long pendingCount = user.getBookings().stream()
    					  					  .filter(b -> b.getCompletionStatus().equals(CompletionStatus.PENDING))
    					  					  .count();
    	if(pendingCount > 0) {
    		return true;
    	}
    	
    	return false;
    }
    
    private boolean isVaccinated(User user) {
    	if(user.getBookings().size() >= 2 ) {
    		long count = user.getBookings().stream()	
    									  .filter(b -> b.getCompletionStatus().equals(CompletionStatus.COMPLETED))
    									  .count();
    									  
    		if(count == 2) {
    			return true;
    		}
    		
    	}
    	
    	return false;
    }
    
    private boolean isCurrentBookingVaccineSameAsPrevious(User user, Slot slot) {
    	boolean isSame = false;
    	List<Booking> listOfBookings = user.getBookings().stream()
    					  								 .filter(b -> b.getCompletionStatus().equals(CompletionStatus.COMPLETED))
    					  								 .collect(Collectors.toList());
    	
    	isSame = listOfBookings.stream()
    						 .allMatch(b -> b.getSlot().getVaccine().getName().equals(slot.getVaccine().getName()));
    				 
    	
    	return isSame;
    }
    
    private boolean hasDaysDifferenceForSecondDose(User user, Slot slot) {
    	
    	List<Booking> listOfBookings = user.getBookings().stream()
    					  								 .filter(b -> b.getCompletionStatus().equals(CompletionStatus.COMPLETED))
    					  								 .collect(Collectors.toList());
    	
    	if(listOfBookings.size() == 1) {
    		Integer diff = Period.between(listOfBookings.get(0).getSlot().getDate().toLocalDate(), LocalDate.now()).getDays();
    		if(diff < slot.getVaccine().getMinDaysOfDifference()) {
    			return false;
    		}
    		return true;
    	}
    	else {
    		return false;
    		//thow exception
    	}
		
    }
    
    private boolean isVaccineAvaliableForCurrentSlot(Slot slot) {
    	
    	if(slot.getCount() > 0) {
    		return true;
    	}
    	return false;
    }
    
    private boolean isSlotBookedByUser(User user, Slot slot) {
    	
    	return user.getBookings().stream()
    							 .filter(b -> b.getCompletionStatus().equals(CompletionStatus.PENDING))
    							 .anyMatch(b -> b.getSlot().getSlotId().equals(slot.getSlotId()));
    					  
    }
}
