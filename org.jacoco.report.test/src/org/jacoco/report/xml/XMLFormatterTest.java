/*******************************************************************************
 * Copyright (c) 2009, 2010 Mountainminds GmbH & Co. KG and Contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Marc R. Hoffmann - initial API and implementation
 *    
 * $Id: $
 *******************************************************************************/
package org.jacoco.report.xml;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jacoco.core.analysis.CoverageNodeImpl;
import org.jacoco.core.analysis.ICoverageNode;
import org.jacoco.core.analysis.ICoverageNode.ElementType;
import org.jacoco.core.data.ExecutionData;
import org.jacoco.core.data.SessionInfo;
import org.jacoco.report.MemorySingleReportOutput;
import org.jacoco.report.ReportStructureTestDriver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

/**
 * Unit tests for {@link XMLFormatter}.
 * 
 * @author Marc R. Hoffmann
 * @version $Revision: $
 */
public class XMLFormatterTest {

	private ReportStructureTestDriver driver;

	private XMLFormatter formatter;

	private MemorySingleReportOutput output;

	@Before
	public void setup() {
		driver = new ReportStructureTestDriver();
		formatter = new XMLFormatter();
		output = new MemorySingleReportOutput();
		formatter.setReportOutput(output);
	}

	@After
	public void teardown() {
		output.assertClosed();
	}

	@Test
	public void testSessionInfo() throws Exception {
		final List<SessionInfo> infos = new ArrayList<SessionInfo>();
		infos.add(new SessionInfo("session-1", 12345, 67890));
		infos.add(new SessionInfo("session-2", 1, 2));
		infos.add(new SessionInfo("session-3", 1, 2));
		ICoverageNode node = new CoverageNodeImpl(ElementType.GROUP, "Sample",
				false);
		final Collection<ExecutionData> data = Collections.emptyList();
		formatter.createReportVisitor(node, infos, data).visitEnd(null);
		assertPathMatches("session-1", "/report/sessioninfo[1]/@id");
		assertPathMatches("12345", "/report/sessioninfo[1]/@start");
		assertPathMatches("67890", "/report/sessioninfo[1]/@dump");
		assertPathMatches("session-2", "/report/sessioninfo[2]/@id");
		assertPathMatches("session-3", "/report/sessioninfo[3]/@id");
	}

	@Test
	public void testStructureWithGroup() throws Exception {
		driver.sendGroup(formatter);
		assertPathMatches("group", "/report/@name");
		assertPathMatches("bundle", "/report/group/@name");
		assertPathMatches("org/jacoco/example", "/report/group/package/@name");
		assertPathMatches("org/jacoco/example/FooClass",
				"/report/group/package/class/@name");
		assertPathMatches("fooMethod",
				"/report/group/package/class/method/@name");
	}

	@Test
	public void testStructureWithBundleOnly() throws Exception {
		driver.sendBundle(formatter);
		assertPathMatches("bundle", "/report/@name");
		assertPathMatches("org/jacoco/example", "/report/package/@name");
		assertPathMatches("org/jacoco/example/FooClass",
				"/report/package/class/@name");
		assertPathMatches("fooMethod", "/report/package/class/method/@name");
	}

	private void assertPathMatches(String expected, String path)
			throws Exception {
		XMLSupport support = new XMLSupport(XMLFormatter.class);
		Document document = support.parse(output.getFile());
		assertEquals(expected, support.findStr(document, path));
	}

}