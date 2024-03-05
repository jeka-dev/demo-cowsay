package com.github.ricksbrown.cowsay;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;

/**
 * Test that Cowloader works as expected.
 * @author Rick Brown
 */
public class CowloaderTest {

	private static final List<String> COWS_OFF = Arrays.asList("bong", "head-in", "sodomized");

	@After
	public void afterEach() {
		Cowloader.setCowEnvironment(CowEnvironment.getInstance());
	}

	/**
	 * Test of listAllCowfiles method, of class Cowloader.
	 */
	@Test
	public void testList() {
		String[] expected = TestUtils.getCowjarCowNames(new String[]{"cowjar"});
		String[] result = Cowloader.listAllCowfiles();
		Assert.assertArrayEquals(expected, result);
	}

	/**
	 * Test of listAllCowfiles method with cowfiles on the COWPATH.
	 */
	@Test
	public void testListWithCowpath() {
		String cowjarName = "cowjar-extra";
		String[] expected = TestUtils.getCowjarCowNames(new String[]{"cowjar", cowjarName});
		addCowjarCowsToCowpath(cowjarName);
		String[] result = Cowloader.listAllCowfiles();
		Assert.assertArrayEquals(expected, result);
	}

	/**
	 * Test that "offensive" cowfiles do not ship by default.
	 */
	@Test
	public void testDefaultWithoutOffensiveCows() {
		List<String> cowlist = Arrays.asList(Cowloader.listAllCowfiles());
		for (String offcow : COWS_OFF) {
			Assert.assertFalse("Default bundle should not ship with " + offcow, cowlist.contains(offcow));
		}
	}

	/**
	 * Test that "offensive" cowfiles can be added with cowjars-off.
	 */
	@Test
	public void testWithOffensiveCows() {
		addCowjarCowsToCowpath("cowjar-off");
		List<String> cowlist = Arrays.asList(Cowloader.listAllCowfiles());
		for (String offcow : COWS_OFF) {
			Assert.assertTrue("cowjar-off should provide " + offcow, cowlist.contains(offcow));
		}
	}

	/**
	 * Test loading a cowfile from the core cowjar.
	 * This tests loading a cowfile by name, e.g. when the CLI `-f` flag is used.
	 */
	@Test
	public void testLoadCoreCowfile() {
		String cowName = "cheese.cow";  // Can be with or without ".cow"
		String expected = loadRawCowfile(cowName, "cowjar");
		String actual = Cowloader.load(cowName);
		Assert.assertThat(expected, CoreMatchers.is(actual));
	}

	/**
	 * Test loading empty cowfilespec.
	 */
	@Test
	public void testLoadWithEmptyPath() {
		String cowName = "";
		String actual = Cowloader.load(cowName);
		Assert.assertNull(actual);
	}

	/**
	 * Test loading non-existent cowfilespec.
	 */
	@Test
	public void testLoadWithBogusPath() {
		String expected = loadRawCowfile("default.cow", "cowjar");
		String cowName = "src/chicken.cow";
		String actual = Cowloader.load(cowName);
		Assert.assertThat(expected, CoreMatchers.is(actual));
	}

	/**
	 * Test loading null cowfilespec.
	 */
	@Test
	public void testLoadWithNullPath() {
		String expected = loadRawCowfile("default.cow", "cowjar");
		String cowName = null;
		String actual = Cowloader.load(cowName);
		Assert.assertThat(expected, CoreMatchers.is(actual));
	}

	/**
	 * Test loading relative cowfilespec.
	 */
	@Test
	public void testLoadWithRelativePath() {
		String cowfileSpec = String.join(File.separator, "src", "test", "resources", "cattle", "cheese.cow");
		Assume.assumeTrue("file required for this test " + cowfileSpec, new File(cowfileSpec).exists());
		String actual = Cowloader.load(cowfileSpec);
		Assert.assertNotNull(actual);
	}

	/**
	 * Test loading absolute cowfilespec.
	 */
	@Test
	public void testLoadWithAbsolutePath() {
		String cowfileSpec = String.join(File.separator, "src", "test", "resources", "cattle", "cheese.cow");
		File cowFile = new File(cowfileSpec);
		Assume.assumeTrue("file required for this test " + cowfileSpec, cowFile.exists());
		String actual = Cowloader.load(cowFile.getAbsolutePath());
		Assert.assertNotNull(actual);
	}

	/**
	 * Test core cowjar cowfiles are trumped by cowfiles  with the same name on the COWPATH.
	 */
	@Test
	public void testLoadCoreCowfileTrumpedByCowpath() {
		MockCowEnvironment env = new MockCowEnvironment();
		try {
			String cowpath = String.join(File.separator, "src", "test", "resources", "cattle");
			env.setOverride("COWPATH", new File(cowpath).getAbsolutePath());
			Cowloader.setCowEnvironment(env);
			String cowName = "cheese.cow";
			String expected = loadRawCowfile(cowName, "cowjar");
			String actual = Cowloader.load(cowName);
			Assert.assertThat(expected, CoreMatchers.not(actual));

			expected = TestUtils.loadExpected("cattle/cheese.cow");
			Assert.assertThat(expected, CoreMatchers.is(actual));
		} finally {
			env.clearOverrides();  // just in case
		}
	}

	/**
	 * Test empty COWPATH.
	 */
	@Test
	public void testEmptyCowpath() {
		MockCowEnvironment env = new MockCowEnvironment();
		try {
			env.setOverride("COWPATH", "");
			Cowloader.setCowEnvironment(env);
			String cowName = "cheese.cow";
			String expected = loadRawCowfile(cowName, "cowjar");
			String actual = Cowloader.load(cowName);
			Assert.assertThat(expected, CoreMatchers.is(actual));
		} finally {
			env.clearOverrides();  // just in case
		}
	}


	/**
	 * Test of listAllCowfiles method with cowjar on classpath.
	 * This tests that cowjars actually work when they are on the classpath.
	 */
	@Test
	public void testListWithCowjarOnClasspath() {
		try {
			String projectVersion = System.getProperty("project.version");
			String jarpath = "cowjar-js-" + projectVersion + ".jar";
			jarpath = String.join(File.separator, "..", "cowjar-js", "target", jarpath);
			File jarfile = new File(jarpath);
			/*
				Tests are run as part of mvn install, mvn package etc and the JAR will be present and this test can run.
				`mvn test` will run the tests but jars will not have been built so this test must be skipped.
			*/
			Assume.assumeTrue("jar required for this test " + jarfile.getAbsolutePath(), jarfile.exists());
			ClassLoader cl = this.getClass().getClassLoader();
			URLClassLoader classloader = new URLClassLoader(new URL[]{jarfile.toURI().toURL()}, cl);
			String[] result = Cowloader.listAllCowfiles(classloader);
			String[] expected = TestUtils.getCowjarCowNames(new String[]{"cowjar", "cowjar-js"});
			Assert.assertArrayEquals(expected, result);
		} catch (MalformedURLException ex) {
			Assert.fail(ex.getMessage());
		}
	}

	/**
	 * Test of listAllCowfiles method with directory on classpath.
	 */
	@Test
	public void testListWithDirectoryOnClasspath() {
		String projectVersion = System.getProperty("project.version");
		File cowdir = TestUtils.getPathToCows("cowjar-extra");
		ClassLoader cl = this.getClass().getClassLoader();
		URL dirUrl = null;
		try {
			dirUrl = cowdir.getParentFile().getCanonicalFile().toURI().toURL();
		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
		URLClassLoader classloader = new URLClassLoader(new URL[]{dirUrl}, cl);
		String[] result = Cowloader.listAllCowfiles(classloader);
		String[] expected = TestUtils.getCowjarCowNames(new String[]{"cowjar", "cowjar-extra"});
		Assert.assertArrayEquals(expected, result);
	}

	/**
	 * Adds cows directory from a cowjar project to the COWPATH.
	 * @param cowjarName The name of the cowjar project to use.
	 * @return The mock environment.
	 */
	public static MockCowEnvironment addCowjarCowsToCowpath(final String cowjarName) {
		MockCowEnvironment env = new MockCowEnvironment();
		File pathToCows = TestUtils.getPathToCows(cowjarName);
		env.setOverride("COWPATH", pathToCows.getAbsolutePath());
		Cowloader.setCowEnvironment(env);
		return env;
	}

	/**
	 * Loads the unparsed content of a cowfile from a cowjar directory in this project.
	 * @param cowName The name of the cowfile to load, e.g. "cheese.cow"
	 * @param cowjarName The name of the cowjar project to load from, e.g. "cowjar-js"
	 * @return The raw content of the cowfile (newlines may be replaced to system value).
	 */
	public static String loadRawCowfile(final String cowName, final String cowjarName) {
		try {
			File cowFile = null;
			File[] coreCowfiles = TestUtils.getCowjarCowFiles(cowjarName);
			for (File file : coreCowfiles) {
				if (cowName.equals(file.getName())) {
					cowFile = file;
					break;
				}
			}
			Assert.assertNotNull("Could not find " + cowName, cowFile);
			String result = TestUtils.loadExpected(new FileInputStream(cowFile));
			return result;
		} catch (FileNotFoundException ex) {
			Assert.fail(ex.getMessage());
		}
		return null;
	}
}
