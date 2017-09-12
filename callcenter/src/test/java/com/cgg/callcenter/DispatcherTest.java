package com.cgg.callcenter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

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

	private List<Employee> operators;

	private List<Employee> supervisors;

	private List<Employee> directors;

	private List<Call> calls;

	@Before
	public void setUp() {
		dispatcher = new Dispatcher();
		operators = new ArrayList<Employee>();
		supervisors = new ArrayList<Employee>();
		directors = new ArrayList<Employee>();
		calls = new ArrayList<Call>();
		try {
			fillEmployees();
			fillCalls();
		} catch (Exception e) {
			System.out.println("Error loading data files: ");
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

			br = new BufferedReader(new FileReader("employess.csv"));
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

			br = new BufferedReader(new FileReader("calls.csv"));
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
