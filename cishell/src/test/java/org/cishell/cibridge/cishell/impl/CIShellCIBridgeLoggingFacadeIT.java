package org.cishell.cibridge.cishell.impl;

import org.cishell.cibridge.cishell.IntegrationTestCase;
import org.cishell.cibridge.core.model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class CIShellCIBridgeLoggingFacadeIT extends IntegrationTestCase {

	private CIShellCIBridgeLoggingFacade ciShellCIBridgeLoggingFacade = getCIShellCIBridge().cishellLogging;

	@Before
	public void waitForAllAlgorithmDefinitionsToBeCached() {

	}

	@Test
	public void validateResultsWithNullFilter() {

	}

	@Test
	public void validateResultsWithEmptyFilter() {
	}

	@Test
	public void validateResultsWithLoggingFilter() {
	}

	@Test
	public void validateResultsWithLogsSinceFilter() {
	}

	@Test
	public void validateResultsWithLogsBeforeFiler() {
	}

	@Test
	public void validateResultsWithMutlipleFilters() {
	}

	@Test
	public void validateResultWithInvalidDateFormat() {

	}

	@After
	public void tearDown() {

	}
}
