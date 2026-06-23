package listener;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import java.text.SimpleDateFormat;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.testng.IConfigurationListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

import com.microsoft.playwright.Page;

// Allure
import io.qameta.allure.Allure;

// Extent Reports
import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import pw.base.PWBaseTest;
import pw.utils.PWLog;

public class PWExecutionController implements ISuiteListener, ITestListener, IConfigurationListener {

	private String runFolder;
	private boolean rerun = false;
	private final Set<String> alreadyExecuted = new HashSet<>();

	private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss");
	public static final Map<String, String> SUITE_PARAMS = new ConcurrentHashMap<>();

	private static int totalTests = 0;
	private static int currentTest = 0;
	private static int passed = 0;
	private static int failed = 0;
	private static int skipped = 0;

	// Extent
	private static ExtentReports extent;
	private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

	// =========================
	// SUITE START
	// =========================
	@Override
	public void onStart(ISuite suite) {

		SUITE_PARAMS.clear();
		suite.getXmlSuite().getParameters().forEach((k, v) -> SUITE_PARAMS.put(k, v));

		XmlSuite xmlSuite = suite.getXmlSuite();
		SUITE_PARAMS.put("suiteName", suite.getName());

		if (xmlSuite.getFileName() != null) {
			File file = new File(xmlSuite.getFileName());
			SUITE_PARAMS.put("suiteFileName", file.getName().replace(".xml", ""));
		}

		String reports = suite.getParameter("reportFolderName");
		if (reports == null)
			reports = "reports" + File.separator + "RunID-";

		String suiteName = (xmlSuite.getFileName() != null)
				? new File(xmlSuite.getFileName()).getName().replace(".xml", "")
				: suite.getName();

		// Create folder FIRST
		String timestamp = new SimpleDateFormat("ddMMyyHHmmss").format(new Date());
		runFolder = reports + timestamp + "-" + suiteName;

		String currDir = System.getProperty("user.dir") + File.separator + runFolder;
		System.setProperty("execution.dir", currDir);

		new File(runFolder).mkdirs();

		// Extent setup
		String reportPath = System.getProperty("user.dir") + File.separator + "reports" + File.separator
				+ "ExtentReport.html";
		ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
		spark.config().setReportName("Automation Report");
		spark.config().setDocumentTitle("Execution Report");

		extent = new ExtentReports();
		extent.attachReporter(spark);

		PWLog.init(runFolder);
	}

	// =========================
	// TEST START
	// =========================
	@Override
	public void onStart(ITestContext context) {
		totalTests = context.getAllTestMethods().length;
		ensureCsvHeader();
	}

	@Override
	public void onTestStart(ITestResult result) {

		currentTest++;

		String className = result.getTestClass().getRealClass().getSimpleName();
		String methodName = result.getMethod().getMethodName();

		// Extent Test start
		ExtentTest extentTest = extent.createTest(className + "." + methodName);
		test.set(extentTest);

		System.out.println("START: " + className + "." + methodName);
	}

	// =========================
	// PASS
	// =========================
	@Override
	public void onTestSuccess(ITestResult result) {

		passed++;
		ExtentTest t = test.get();
		if (t != null) {
			t.pass("Test Passed");
		}

		captureScreenshot(result, "Success Screenshot");
		logResult(result, "PASSED");
	}

	// =========================
	// FAIL
	// =========================
	@Override
	public void onTestFailure(ITestResult result) {

		failed++;
		ExtentTest t = test.get();
		if (t != null) {
			t.fail(result.getThrowable());
		} else {
			// Test never started (its @BeforeMethod / browser launch failed). Surface the real cause
			// instead of NPE-ing on a null ExtentTest.
			System.out.println("⚠️ Test failed before it started (setup/browser-launch issue): "
					+ result.getThrowable());
		}

		captureScreenshot(result, "Failure Screenshot");
		logResult(result, "FAILED");
	}

	// =========================
	// SKIP
	// =========================
	@Override
	public void onTestSkipped(ITestResult result) {

		skipped++;
		ExtentTest t = test.get();
		if (t != null) {
			t.skip("Test Skipped");
		} else {
			System.out.println("⚠️ Test skipped (setup/@BeforeMethod failure): " + result.getThrowable());
		}
		logResult(result, "SKIPPED");
	}

	// =========================
	// CONFIG FAILURE (e.g. @BeforeMethod / browser launch) - surface the REAL cause, don't hide it.
	// =========================
	@Override
	public void onConfigurationFailure(ITestResult result) {
		System.out.println("❌ CONFIGURATION FAILURE in " + result.getMethod().getMethodName() + ": "
				+ result.getThrowable());
		if (result.getThrowable() != null) {
			result.getThrowable().printStackTrace();
		}
	}

	// =========================
	// SCREENSHOT (ALLURE + EXTENT)
	// =========================
	private void captureScreenshot(ITestResult result, String title) {
		try {
			Page page = PWBaseTest.getPage();
			if (page != null) {
				byte[] screenshot = page.screenshot();

				// Allure
				Allure.addAttachment(title, "image/png", new ByteArrayInputStream(screenshot), "png");

				// Extent
				String path = saveScreenshot(screenshot, result.getMethod().getMethodName());
				ExtentTest t = test.get();
				if (t != null && path != null) {
					t.addScreenCaptureFromPath(path);
				}
			}
		} catch (Exception e) {
			System.out.println("Screenshot error: " + e.getMessage());
		}
	}

	private String saveScreenshot(byte[] screenshot, String name) {
		try {
			String path = System.getProperty("execution.dir") + File.separator + name + ".png";
			File file = new File(path);

			try (FileOutputStream fos = new FileOutputStream(file)) {
				fos.write(screenshot);
			}
			return path;
		} catch (Exception e) {
			return null;
		}
	}

	// =========================
	// CSV LOG
	// =========================
	private void logResult(ITestResult result, String status) {

		File csvFile = new File(runFolder, "TC_Executed.csv");

		try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(csvFile, true)))) {

			String className = result.getTestClass().getRealClass().getSimpleName();
			String methodName = result.getMethod().getMethodName();

			LocalDate date = LocalDate.now();

			String startTime = LocalDateTime
					.ofInstant(Instant.ofEpochMilli(result.getStartMillis()), ZoneId.systemDefault()).format(TIME_FMT);

			String endTime = LocalDateTime
					.ofInstant(Instant.ofEpochMilli(result.getEndMillis()), ZoneId.systemDefault()).format(TIME_FMT);

			out.println(className + "," + methodName + "," + status + "," + date + "," + startTime + "," + endTime);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void ensureCsvHeader() {
		File csvFile = new File(runFolder, "TC_Executed.csv");
		if (!csvFile.exists()) {
			try (PrintWriter out = new PrintWriter(new FileWriter(csvFile, true))) {
				out.println("TestName,MethodName,Status,Date,StartTime,EndTime");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// =========================
	// SUITE END
	// =========================
	@Override
	public void onFinish(ISuite suite) {

	    System.out.println("Execution Completed");

	    // Flush extent report first
	    if (extent != null) {
	        extent.flush();
	    }

	    // Debug logs
//	    System.out.println("execution.dir = " + System.getProperty("execution.dir"));
//	    System.out.println("user.dir = " + System.getProperty("user.dir"));

	    // Source report path
	    File src = new File(
	            System.getProperty("execution.dir")
	                    + File.separator
	                    + "ExtentReport.html");

	    // Destination report path
	    File dest = new File(
	            System.getProperty("user.dir")
	                    + File.separator
	                    + "ExtentReport.html");

	    // Print full paths
//	    System.out.println("Source Path = " + src.getAbsolutePath());
	 //   System.out.println("Destination Path = " + dest.getAbsolutePath());

	    try {

	        // Check source file exists
	        if (src.exists()) {

	            // Create destination folder if not exists
	            if (dest.getParentFile() != null
	                    && !dest.getParentFile().exists()) {

	                dest.getParentFile().mkdirs();
	            }

	            // Copy report
	            Files.copy(
	                    src.toPath(),
	                    dest.toPath(),
	                    StandardCopyOption.REPLACE_EXISTING);

	            System.out.println("Report copied successfully.");

	        } else {

//	            System.out.println( "Source report file not found: "  + src.getAbsolutePath());

	            // List files inside execution directory
	            File executionDir = new File(System.getProperty("execution.dir"));

	            if (executionDir.exists() && executionDir.isDirectory()) {

	                System.out.println("Available files in execution directory:");

	                File[] files = executionDir.listFiles();

	                if (files != null) {
	                    for (File file : files) {
	                        System.out.println(file.getName());
	                    }
	                }

	            } else {

	                System.out.println("Execution directory does not exist.");
	            }
	        }

	    } catch (IOException e) {

	        System.out.println("Error while copying report:");
	        e.printStackTrace();
	    }

	    // Close logs
	    PWLog.close();
	}
}