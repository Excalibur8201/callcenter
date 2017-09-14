package com.cgg.callcenter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import com.cgg.callcenter.dispatcher.Dispatcher;
import com.cgg.callcenter.model.Call;
import com.cgg.callcenter.model.Employee;
import com.cgg.callcenter.model.EmployeeType;

import junit.framework.TestCase;

/**
 * 
 * Test class for <code>Dispatcher</code> class.
 *
 */
public class DispatcherTest extends TestCase {

	private Dispatcher dispatcher;

	private Queue<Employee> operators;

	private Queue<Employee> supervisors;

	private Queue<Employee> directors;

	private Queue<Call> calls;

	@Before
	public void setUp() {
		dispatcher = new Dispatcher();
		operators = new LinkedBlockingQueue<Employee>();
		supervisors = new LinkedBlockingQueue<Employee>();
		directors = new LinkedBlockingQueue<Employee>();
		calls = new LinkedBlockingQueue<Call>();
		try {
			fillEmployees();
			fillCalls();
		} catch (Exception e) {
			System.out.println("Error loading data files.");
			e.printStackTrace();
		}
	}

	@Test
	public void testCalls() {

		dispatcher.dispatchCalls(operators, supervisors, directors, calls);

	}

	private void fillEmployees() throws Exception {
		try {

			List<String> contents = IOUtils.readLines(
					this.getClass().getClassLoader().getResourceAsStream("employees.csv"), Charset.defaultCharset());

			for (String line : contents) {
				String[] values = line.split(",");
				Employee employee = new Employee();
				employee.setName(values[0]);
				employee.setLastName(values[1]);
				EmployeeType type = EmployeeType.valueOf(values[2]);
				employee.setEmployeeType(type);
				switch (type) {
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
		} catch (IOException e) {
			throw new Exception(e);
		}
	}

	private void fillCalls() throws Exception {
		try {

			List<String> contents = IOUtils.readLines(this.getClass().getClassLoader().getResourceAsStream("calls.csv"),
					Charset.defaultCharset());

			for (String line : contents) {
				String[] values = line.split(",");
				Call call = new Call();
				call.setNumber(values[0]);
				calls.add(call);
			}

		} catch (IOException e) {
			throw new Exception(e);
		}
	}
}
