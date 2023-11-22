/*******************************************************************************
 * Copyright (c) 2009, 2023 Mountainminds GmbH & Co. KG and Contributors
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Marc R. Hoffmann - initial API and implementation
 *
 *******************************************************************************/
package org.jacoco.core.internal.analysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.jacoco.core.analysis.ICounter;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

/**
 * Unit tests for {@link LineImplTest}.
 */
public class LineImplTest {

	private LineImpl line;

	@Before
	public void setup() {
		line = LineImpl.EMPTY;
	}

	@Test
	public void testEMPTY() {
		assertEquals(CounterImpl.COUNTER_0_0, line.getInstructionCounter());
		assertEquals(CounterImpl.COUNTER_0_0, line.getBranchCounter());
		assertEquals(ICounter.EMPTY, line.getStatus());
	}

	@Test
	public void testIncrement1() {
		line = line.increment(CounterImpl.getInstance(1, 2),
				CounterImpl.getInstance(3, 4), new HashSet<String>());
		assertEquals(CounterImpl.getInstance(1, 2),
				line.getInstructionCounter());
		assertEquals(CounterImpl.getInstance(3, 4), line.getBranchCounter());
	}

	@Test
	public void testIncrement2() {
		line = line.increment(CounterImpl.getInstance(1, 2),
				CounterImpl.getInstance(3, 4000), new HashSet<String>());
		assertEquals(CounterImpl.getInstance(1, 2),
				line.getInstructionCounter());
		assertEquals(CounterImpl.getInstance(3, 4000), line.getBranchCounter());
	}

	@Test
	public void testIncrement3() {
		line = line.increment(CounterImpl.getInstance(1, 2),
				CounterImpl.getInstance(3000, 4000), new HashSet<String>());
		assertEquals(CounterImpl.getInstance(1, 2),
				line.getInstructionCounter());
		assertEquals(CounterImpl.getInstance(3000, 4000),
				line.getBranchCounter());
	}

	@Test
	public void testIncrement4() {
		line = line.increment(CounterImpl.getInstance(1, 2000),
				CounterImpl.getInstance(3000, 4000), new HashSet<String>());
		assertEquals(CounterImpl.getInstance(1, 2000),
				line.getInstructionCounter());
		assertEquals(CounterImpl.getInstance(3000, 4000),
				line.getBranchCounter());
	}

	@Test
	public void testIncrement5() {
		line = line.increment(CounterImpl.getInstance(1000, 2000),
				CounterImpl.getInstance(3000, 4000), new HashSet<String>());
		assertEquals(CounterImpl.getInstance(1000, 2000),
				line.getInstructionCounter());
		assertEquals(CounterImpl.getInstance(3000, 4000),
				line.getBranchCounter());
	}

	@Test
	public void testGetStatus1() {
		line = line.increment(CounterImpl.getInstance(1, 0),
				CounterImpl.getInstance(0, 0), new HashSet<String>());
		assertEquals(ICounter.NOT_COVERED, line.getStatus());
	}

	@Test
	public void testGetStatus2() {
		line = line.increment(CounterImpl.getInstance(0, 0),
				CounterImpl.getInstance(1, 0), new HashSet<String>());
		assertEquals(ICounter.NOT_COVERED, line.getStatus());
	}

	@Test
	public void testGetStatus3() {
		line = line.increment(CounterImpl.getInstance(0, 1),
				CounterImpl.getInstance(0, 0), new HashSet<String>());
		assertEquals(ICounter.FULLY_COVERED, line.getStatus());
	}

	@Test
	public void testGetStatus4() {
		line = line.increment(CounterImpl.getInstance(0, 0),
				CounterImpl.getInstance(0, 1), new HashSet<String>());
		assertEquals(ICounter.FULLY_COVERED, line.getStatus());
	}

	@Test
	public void testGetStatus5() {
		line = line.increment(CounterImpl.getInstance(1, 1),
				CounterImpl.getInstance(0, 0), new HashSet<String>());
		assertEquals(ICounter.PARTLY_COVERED, line.getStatus());
	}

	@Test
	public void testGetStatus6() {
		line = line.increment(CounterImpl.getInstance(0, 1),
				CounterImpl.getInstance(1, 1), new HashSet<String>());
		assertEquals(ICounter.PARTLY_COVERED, line.getStatus());
	}

	@Test
	public void testHashCode() {
		line = line.increment(CounterImpl.getInstance(111, 222),
				CounterImpl.getInstance(333, 444), new HashSet<String>());
		LineImpl line2 = LineImpl.EMPTY;
		line2 = line2.increment(CounterImpl.getInstance(111, 222),
				CounterImpl.getInstance(333, 444), new HashSet<String>());
		assertEquals(line.hashCode(), line2.hashCode());
	}

	@Test
	public void testEquals1() {
		line = line.increment(CounterImpl.getInstance(111, 222),
				CounterImpl.getInstance(333, 444), new HashSet<String>());
		LineImpl line2 = LineImpl.EMPTY;
		line2 = line2.increment(CounterImpl.getInstance(111, 222),
				CounterImpl.getInstance(333, 444), new HashSet<String>());
		assertEquals(line, line2);
	}

	@Test
	public void testEquals2() {
		line = line.increment(CounterImpl.getInstance(111, 222),
				CounterImpl.getInstance(333, 444), new HashSet<String>());
		assertFalse(line.equals(new Object()));
	}

	@Test
	public void testEquals3() {
		line = line.increment(CounterImpl.getInstance(111, 222),
				CounterImpl.getInstance(333, 444), new HashSet<String>());
		LineImpl line2 = LineImpl.EMPTY;
		line2 = line2.increment(CounterImpl.getInstance(111, 2220),
				CounterImpl.getInstance(333, 444), new HashSet<String>());
		assertFalse(line.equals(line2));
	}

	@Test
	public void testEquals4() {
		line = line.increment(CounterImpl.getInstance(111, 222),
				CounterImpl.getInstance(333, 4440), new HashSet<String>());
		LineImpl line2 = LineImpl.EMPTY;
		line2 = line2.increment(CounterImpl.getInstance(111, 222),
				CounterImpl.getInstance(333, 444), new HashSet<String>());
		assertFalse(line.equals(line2));
	}

}
