package com.cgg.callcenter.dispatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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

		int numThreads = 10;
		ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

		Call call = null;
		Employee operator, supervisor, director;
		List<CallAttendance> callAttendances = new ArrayList<CallAttendance>();

		int i = 0;

		while ((call = calls.poll()) != null) {

			// if (operators.size() > 0) {
			// Employee operator = operators.poll();
			// attendCall(executorService, operator, call);
			// operators.add(operator);
			// } else if (supervisors.size() > 0) {
			// Employee supervisor = supervisors.poll();
			// attendCall(executorService, supervisor, call);
			// supervisors.add(supervisor);
			// } else if (directors.size() > 0) {
			// Employee director = directors.poll();
			// attendCall(executorService, director, call);
			// directors.add(director);
			// }

			if ((operator = operators.poll()) != null) {
				callAttendances.add(new CallAttendance(call, operator));
			} else if ((supervisor = supervisors.poll()) != null) {
				callAttendances.add(new CallAttendance(call, supervisor));
			} else if ((director = directors.poll()) != null) {
				callAttendances.add(new CallAttendance(call, director));
			}
			i++;

			if (i == numThreads) {
				for (CallAttendance callAttendance : callAttendances) {
					attendCall(executorService, callAttendance, operators, supervisors, directors);
				}
				callAttendances.clear();
				i = 0;
			}

		}

		executorService.shutdownNow();

		try {
			if (!executorService.awaitTermination(100, TimeUnit.MICROSECONDS)) {
				System.out.println("Still waiting...");
				System.exit(0);
			}
		} catch (InterruptedException e) {
			System.out.println("Executor interrrumpido...");
		}
		System.out.println("Ejecucion finalizada.");
	}

	public class CallAttendance {
		private Call call;
		private Employee employee;

		public CallAttendance(Call call, Employee employee) {
			this.call = call;
			this.employee = employee;
		}

		public Call getCall() {
			return call;
		}

		public void setCall(Call call) {
			this.call = call;
		}

		public Employee getEmployee() {
			return employee;
		}

		public void setEmployee(Employee employee) {
			this.employee = employee;
		}

	}

	private void attendCall(ExecutorService executor, CallAttendance callAttendance, Queue<Employee> operators,
			Queue<Employee> supervisors, Queue<Employee> directors) {

		Random ran = new Random();
		int x = ran.nextInt(6) + 5;

		long endNanos = System.nanoTime() + (x * 1000000000);

		Call call = callAttendance.getCall();
		Employee employee = callAttendance.getEmployee();

		Future<?> f = executor.submit(new Runnable() {
			public void run() {
				try {
					Thread.sleep(x * 10000);
				} catch (Exception e) {
				}
				System.out.println("Llamada atendida de " + call.getNumber() + " por el "
						+ employee.getEmployeeType().toString().toLowerCase() + " " + employee.getName() + " "
						+ employee.getLastName() + ", con una duracion de : " + x + " segundos.");
			}
		});

		long timeLeft = endNanos - System.nanoTime();

		try {
			f.get(timeLeft, TimeUnit.NANOSECONDS);
		} catch (ExecutionException e) {
		} catch (InterruptedException e) {

		} catch (TimeoutException e) {
			f.cancel(true);
		}

		switch (employee.getEmployeeType()) {
		case OPERATOR:
			operators.add(employee);
			break;
		case SUPERVISOR:
			supervisors.add(employee);
			break;
		case DIRECTOR:
			directors.add(employee);
			break;
		default:
			break;
		}
	}

}
