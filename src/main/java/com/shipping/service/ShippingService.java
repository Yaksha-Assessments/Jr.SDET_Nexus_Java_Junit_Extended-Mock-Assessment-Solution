package com.shipping.service;

public class ShippingService {
	private double weight;
	private int distance;

	public ShippingService(double weight, int distance) throws IllegalArgumentException {
		this.weight = weight;
		this.distance = distance;
	}

	public double calculateShippingCost() {
		double cost = 0.0;
		if (weight <= 5 && weight > 0)
			cost = 10 * distance;
		else if (weight <= 20)
			cost = 15 * distance;
		else 
			cost = 20 * distance;
		if(weight < 0)
			cost = 0.0;
		if(distance <=0)
			cost = 0.0;
		
		return cost;
	}
}
