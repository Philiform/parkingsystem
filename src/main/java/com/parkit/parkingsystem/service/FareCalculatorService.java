package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.TimesDuration;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	private double inHour;
	private double outHour;
	private double duration;

	public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

      inHour = (double)ticket.getInTime().getTime();
      outHour = (double)ticket.getOutTime().getTime();

      //TODO: Some tests are failing here. Need to check if this logic is correct
      duration = ( (outHour - inHour) / TimesDuration._1_HEURE);

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}