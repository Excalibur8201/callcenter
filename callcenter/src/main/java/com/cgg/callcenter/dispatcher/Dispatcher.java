package com.cgg.callcenter.dispatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.cgg.callcenter.model.Call;
import com.cgg.callcenter.model.Employee;

/**
 * 
 * Class for calls dispatching.
 *
 */
public class Dispatcher {

	private Queue<Employee> operators;

	private Queue<Employee> supervisors;

	private Queue<Employee> directors;

	public Dispatcher(Queue<Employee> operators, Queue<Employee> supervisors, Queue<Employee> directors) {
		this.operators = operators;
		this.supervisors = supervisors;
		this.directors = directors;
	}

	/**
	 * Dispatch calls concurrently
	 */
	public void dispatchCalls(List<Call> calls) {

		int numThreads = 10;
		ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

		Employee operator, supervisor, director;
		List<CallAttendance> callAttendances = new ArrayList<CallAttendance>();

		int i = 0;

		for (Call call : calls) {

			if ((operator = operators.poll()) != null) {
				callAttendances.add(new CallAttendance(call, operator));
			} else if ((supervisor = supervisors.poll()) != null) {
				callAttendances.add(new CallAttendance(call, supervisor));
			} else if ((director = directors.poll()) != null) {
				callAttendances.add(new CallAttendance(call, director));
			}

			i++;

			if ((operators.size() == 0 && supervisors.size() == 0 && directors.size() == 0) || (i == numThreads)) {
				for (CallAttendance callAttendance : callAttendances) {
					attendCall(executorService, callAttendance);
				}
				callAttendances.clear();
				i = 0;
			}

		}

		// Esperar q procesar el volumen de llamdas
		try {

			Thread.sleep((calls.size() * 10000 / numThreads) + 10000);
		} catch (InterruptedException e) {
		}

		executorService.shutdownNow();

		try {
			if (!executorService.awaitTermination(100, TimeUnit.MICROSECONDS)) {
				System.out.println("Esperando a que finalicen todas las tareas ...");
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

	private void attendCall(ExecutorService executor, CallAttendance callAttendance) {

		Random ran = new Random();
		int x = ran.nextInt(6) + 5;

		Call call = callAttendance.getCall();
		Employee employee = callAttendance.getEmployee();

		executor.submit(new Runnable() {
			public void run() {

				long endTime = System.currentTimeMillis() + (x * 1000);
				while (System.currentTimeMillis() < endTime) {
				}
				System.out.println("Llamada atendida de " + call.getNumber() + " por el "
						+ employee.getEmployeeType().toString().toLowerCase() + " " + employee.getName() + " "
						+ employee.getLastName() + ", con una duracion de : " + x + " segundos.");
			}
		});

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
