package com.cgg.callcenter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

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
		BufferedReader br = null;
		try {

			br = new BufferedReader(new FileReader("/src/test/resources/employess.csv"));
			String line = null;

			while ((line = br.readLine()) != null) {
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
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			try {
				br.close();
			} catch (Exception ex) {
				throw new Exception(ex);
			}
		}
	}

	private void fillCalls() throws Exception {
		BufferedReader br = null;
		try {

			br = new BufferedReader(new FileReader("resources/calls.csv"));
			String line = null;

			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				Call call = new Call();
				call.setNumber(values[0]);
				calls.add(call);
			}

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			try {
				br.close();
			} catch (Exception ex) {
				throw new Exception(ex);
			}
		}
	}
}
