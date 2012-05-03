/*
 * Copyright 2011 Carsten Pfeiffer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.outerj.daisy.diff.html;

import java.io.*;

/**
 * Support for reading files into strings from a given directory.
 * @author TREND
 * @version 04 Jul 2011
 */
public class TestHelper {
	public static final String OLD_NAME = "a.html";
	public static final String NEW_NAME = "b.html";
	public static final String ANCESTOR_NAME = "ancestor.html";
	public static final String EXPECTED_NAME = "expected.html";
	
	public static final String ENCODING = "UTF-8";
	
	private File testDir;
	private File resultsFile;
	
	public static boolean isTestDataDir(File aDir) {
		File tempExpectedFile = new File(aDir, OLD_NAME);
		return tempExpectedFile.exists();
	}
	
	/**
	 * Creates a new test helper. A file named "results.txt" is expected to
	 * reside inside this directory.
	 */
	public TestHelper(File aTestDir) {
		testDir = aTestDir;
		if (!testDir.exists()) {
			throw new IllegalArgumentException(testDir + " does not exist");
		}
		resultsFile = new File(testDir, EXPECTED_NAME);
	}
	
	public File getOld() throws IOException {
		File tempFile = new File(testDir, OLD_NAME);
		verifyExists(tempFile);
		return tempFile;
	}
	
	public File getNew() throws IOException {
		File tempFile = new File(testDir, NEW_NAME);
		verifyExists(tempFile);
		return tempFile;
	}
	
	public File getAncestor() throws IOException {
		File tempFile = new File(testDir, ANCESTOR_NAME);
		verifyExists(tempFile);
		return tempFile;
	}
	
	public boolean hasAncestor() throws IOException {
		try {
			return getAncestor() != null; 
		} catch (IOException ex) {
			return false;
		}
	}
	
	private void verifyExists(File aFile) throws IOException {
		if (!aFile.exists()) {
			throw new FileNotFoundException(aFile.getAbsolutePath() + " does not exist");
		}
	}

	/**
	 * Returns the test directory as passed into the constructor
	 * @return
	 */
	public File getTestDir() {
		return testDir;
	}
	
	/**
	 * Returns the contents of the "expected.html" file of the test directory,
	 * minus the html header and footer
	 * @return
	 * @throws IOException
	 */
	public String getExpectedResults() throws IOException {
		return readContents(resultsFile);
	}

	/**
	 * Returns the header that is added to every results file and expected results file.
	 * The header includes an online reference to the diff.css file at googlecode.com.
	 * Having it as a fixed, static reference eases the comparison.
	 */
    public String getHtmlHeader() {
		return "<html><head><link rel=\"stylesheet\" type=\"text/css\" href=\"http://daisydiff.googlecode.com/files/diff.css\"></link></head>\n";
	}
    public String getHtmlFooter() {
    	return "\n</html>";
    }
	
	/**
	 * Reads the contents of the given file into a String. The file contents
	 * is expected to be in UTF-8 encoding.
	 * @param aFile
	 * @return
	 * @throws IOException
	 */
	public String readContents(File aFile) throws IOException {
		Reader tempReader = new InputStreamReader(new FileInputStream(aFile), ENCODING);
		try {
			StringBuilder tempResult = new StringBuilder();
			char[] buf = new char[8192];
			int tempRead = 0;
			while ((tempRead = tempReader.read(buf)) >= 0) {
				tempResult.append(buf, 0, tempRead);
			}
			
			return tempResult.toString();
		} finally {
			tempReader.close();
		}
	}
}
