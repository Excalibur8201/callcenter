package com.cgg.callcenter.dispatcher;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.cgg.callcenter.model.Call;
import com.cgg.callcenter.model.Employee;

/**
 * 
 * @author claudio
 *
 */
public class Dispatcher {

	/**
	 * Dispatch calls concurrently
	 */
	public void dispatchCalls(BlockingQueue<Employee> operators, BlockingQueue<Employee> supervisors, BlockingQueue<Employee> directors,
			List<Call> calls) {

		
	}
}
