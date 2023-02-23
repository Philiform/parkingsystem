package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.TimesDuration;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	private static final double DUREE_30_MINUTES_EN_HEURE = 0.5;

	private static final double TARIF_PLEIN = 1.0;
	private static final double TARIF_REDUIT = 0.95; // réduction 5%
	private double tarifCorrection;

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
      duration = roundValue( (outHour - inHour) / TimesDuration._1_HEURE);

		if (duration < DUREE_30_MINUTES_EN_HEURE) {
			duration = 0.0;
		} else if (ticket.isRecurringUser()) {
			tarifCorrection = TARIF_REDUIT;
		} else {
			tarifCorrection = TARIF_PLEIN;
		}
		

		switch (ticket.getParkingSpot().getParkingType()) {
			case CAR: {
				ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR * tarifCorrection);
				break;
			}
			case BIKE: {
				ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR * tarifCorrection);
				break;
			}
			default:
				throw new IllegalArgumentException("Unkown Parking Type");
		}
    }
	
	public double roundValue(double valeur) {
		return Math.round(valeur * 100.0) / 100.0;
	}
}