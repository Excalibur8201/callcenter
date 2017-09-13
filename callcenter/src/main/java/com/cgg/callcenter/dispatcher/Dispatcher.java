package com.cgg.callcenter.dispatcher;

import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
	public void dispatchCalls(Queue<Employee> operators, Queue<Employee> supervisors, Queue<Employee> directors,
			Queue<Call> calls) {

		// Para 1-10 threads
		// Para cada llamada
		// Ver si hay operadores
		// Sino ver si hay supervisores
		// Sino ver si hay directores
		// Sino

		ExecutorService executorService = Executors.newFixedThreadPool(10);

		Call call = null;

		while ((call = calls.poll()) != null) {

			if (operators.size() > 0) {
				Employee operator = operators.poll();
				executorService.execute(new Runnable() {
					public void run() {

					}
				});
				operators.add(operator);
			} else if (supervisors.size() > 0) {
				Employee supervisor = supervisors.poll();
				executorService.execute(new Runnable() {
					public void run() {
						
					}
				});
				supervisors.add(supervisor);
			} else if (directors.size() > 0) {
				Employee director = directors.poll();
				executorService.execute(new Runnable() {
					public void run() {

					}
				});
				directors.add(director);
			}

		}
	}

	private void attendCall(ExecutorService executor, Employee employee, Call call) {

	}

}
