package mainapp;

import static mainapp.testutils.TestUtils.businessTestFile;
import static mainapp.testutils.TestUtils.yakshaAssert;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runners.Parameterized.Parameters;


import com.shipping.test.ShippingTest;

public class MyApp {

	public static void main(String[] args) throws IOException {

		String sourcePath = "src/main/java/com/shipping/test/ShippingTest.java";
		String destPath = "src/test/java/com/shipping/test/ShippingTest.java";
		copyFile(sourcePath, destPath);
		
		Class<?> testClass = ShippingTest.class;
		Result result = JUnitCore.runClasses(testClass);
		int success = result.getRunCount() - result.getFailureCount();
		double successPer = ((double) success / result.getRunCount()) * 100;
		System.out.println("Success % : " + successPer);
		
		boolean hasBeforeAll = false;
        boolean hasBeforeEach = false;
        boolean hasAfterAll = false;
        boolean hasAfterEach = false;
        boolean hasParameters = false;

        for (Method method : testClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(BeforeClass.class)) {
                hasBeforeAll = true;
            }
            if (method.isAnnotationPresent(Before.class)) {
                hasBeforeEach = true;
            }
            if (method.isAnnotationPresent(AfterClass.class)) {
                hasAfterAll = true;
            }
            if (method.isAnnotationPresent(After.class)) {
                hasAfterEach = true;
            }
            if (method.isAnnotationPresent(Parameters.class)) {
            	hasParameters = true;
            }
        }
		
        yakshaAssert("Test Before All Used ", hasBeforeAll, businessTestFile);
        yakshaAssert("Test Before Each Used  ", hasBeforeEach, businessTestFile);
        yakshaAssert("Test After All Used ", hasAfterAll, businessTestFile);
        yakshaAssert("Test After Each Used  ", hasAfterEach, businessTestFile);
        yakshaAssert("Test Has Parameters  ", hasParameters, businessTestFile);
        
        
		
		for (int i = 10; i <= 100; i += 10) {
			if (i <= successPer)
				yakshaAssert("TestCasesSuccess " + i + "%", true, businessTestFile);
			else
				yakshaAssert("TestCasesSuccess " + i + "%", false, businessTestFile);
		}

		try {
			String command = "cmd /c mvn clean test";
			// For Windows, use something like: String command = "cmd /c echo Hello,
			// World!";
			Process process = Runtime.getRuntime().exec(command);
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
			int exitVal = process.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		String csvFile = "target/site/jacoco/jacoco.csv"; // Specify the path to your CSV file
		String linen = "";
		String cvsSplitBy = ","; // CSV delimiter, assuming it's a comma
		int rowNumber = 0;

		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

			while ((linen = br.readLine()) != null) {
				// Increment row number
				rowNumber++;

				// Use comma as separator
				String[] columns = linen.split(cvsSplitBy);

				// Check if the third column exists and contains "ShippingService"
				if (columns.length > 2 && columns[2].contains("ShippingService")) {
					// System.out.println("Text 'ShippingChargesService' found in row number: " +
					// rowNumber);
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (rowNumber > 0) {
			String[] values = new String[11]; // Array to store values from columns 4 to 14
			int targetRowNumber = rowNumber; // The specific row number you want to read from
			int currentRowNumber = 0;
			linen = "";
			try (BufferedReader br1 = new BufferedReader(new FileReader(csvFile))) {

				while ((linen = br1.readLine()) != null) {
					// Increment the current row number
					currentRowNumber++;

					// When the current row is the target row
					if (currentRowNumber == targetRowNumber) {
						// Split the line into columns
						String[] columns = linen.split(cvsSplitBy);

						// Assuming columns start at 0, so column 4 is index 3, and column 14 is index
						// 13
						for (int i = 3; i <= 13; i++) {
							// Check if the column exists in this row
							if (i < columns.length) {
								values[i - 3] = columns[i];
							} else {
								// If the column does not exist, assign a default value or leave it null
								values[i - 3] = ""; // Here assigning an empty string for missing columns
							}
						}

						// After storing values, break the loop as we've found the target row
						break;
					}
				}

				// Output the values for demonstration
				// System.out.println("Values from columns 4 to 14 of row " + targetRowNumber +
				// ": " + Arrays.toString(values));
				int missed = 0;
				int covered = 0;
				for (int i = 0; i < 10; i += 2) {
					missed += Integer.parseInt(values[i]);
					covered += Integer.parseInt(values[i + 1]);
				}
				int total = missed + covered;

				double coverage = ((double) covered / total) * 100;
				System.out.println("Coverage% : " + coverage);

				for (int i = 10; i <= 100; i += 10) {
					if (i <= coverage)
						yakshaAssert("TestSuccessfullCoverage " + i + "%", true, businessTestFile);
					else
						yakshaAssert("TestSuccessfullCoverage " + i + "%", false, businessTestFile);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			for (int i = 10; i <= 100; i += 10) {

				yakshaAssert("TestSuccessfullCoverage " + i + "%", false, businessTestFile);
			}
		}

	}

	public static void copyFile(String sourcePathStr, String destPathStr) {
		// Convert string paths to Path objects
		Path sourcePath = Paths.get(sourcePathStr);
		Path destPath = Paths.get(destPathStr);

		try {
			// Copy the file from source to destination
			Files.copy(sourcePath, destPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
			// System.out.println("File copied successfully.");
		} catch (IOException e) {
			// System.err.println("Error occurred while copying the file.");
			e.printStackTrace();
		}
	}

}
