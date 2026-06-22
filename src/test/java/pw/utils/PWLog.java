package pw.utils;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.microsoft.playwright.Page;
import org.apache.logging.log4j.ThreadContext;

import ai.AIFailureAnalyzer;
import ai.AISuiteCollector;
import ai.FailureContext;
import io.qameta.allure.Allure;
import pw.base.PWBaseTest;

/**
 * Author Tapan, Feb 26
 * 
 */
public final class PWLog {
	private static String baseFolder;
	private static final Object lock = new Object();
	private static PrintWriter frameworkWriter;
	private static final ConcurrentHashMap<String, PrintWriter> classWriters = new ConcurrentHashMap<>();
	// Step-level failure counter (per test thread)
	private static final ThreadLocal<Integer> stepFailureCount = ThreadLocal.withInitial(() -> 0);
	// Threshold per test
//	private static final ThreadLocal<Integer> stepFailureThreshold = ThreadLocal.withInitial(() -> 3);
	private static final ThreadLocal<List<String>> stepHistory = ThreadLocal.withInitial(ArrayList::new);

	private PWLog() {
	}

	// =============================
	// SUITE LEVEL COUNTERS
	// =============================
	private static final AtomicInteger totalPass = new AtomicInteger(0);
	private static final AtomicInteger totalFail = new AtomicInteger(0);
	private static final AtomicInteger totalTestCases = new AtomicInteger(0);

	public static void incrementPass() {
		totalPass.incrementAndGet();
	}

	public static void incrementFail() {
		totalFail.incrementAndGet();
	}

	public static void incrementTest() {
		totalTestCases.incrementAndGet();
	}

	public static int getTotalPass() {
		return totalPass.get();
	}

	public static int getTotalFail() {
		return totalFail.get();
	}

	public static int getTotalTestCases() {
		return totalTestCases.get();
	}

	// =============================
	// INIT
	// =============================
	public static void init(String runFolder) {
		baseFolder = runFolder;
		try {
			new File(baseFolder).mkdirs();
			frameworkWriter = new PrintWriter(new BufferedWriter(new FileWriter(baseFolder + "/framework.log", true)),
					true);
			
			// Set Log4j2 ThreadContext variable for dynamic log file routing
			String logFilePath = baseFolder + File.separator + "application.log";
			ThreadContext.put("logFileName", logFilePath);
		} catch (IOException e) {
			throw new RuntimeException("Failed to initialize logger", e);
		}
	}

	// =============================
	// FRAMEWORK LOGGING
	// =============================
	public static void info(String message) {
		framework("INFO", message);
	}

	public static void warn(String message) {
		framework("WARN", message);
	}

	public static void error(String message) {
		framework("ERROR", message);
	}

	private static void framework(String level, String message) {
		write(frameworkWriter, level, message);
	}

	// =============================
	// CLASS LOGGING
	// =============================
	public static void classLog(String className, String level, String message) {
		PrintWriter writer = classWriters.computeIfAbsent(className, name -> {
			try {
				return new PrintWriter(new BufferedWriter(new FileWriter(baseFolder + "/" + name + ".log", true)),
						true);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
		write(writer, level, message);
	}

	private static void write(PrintWriter writer, String level, String message) {
		if (writer == null) {
			System.err.println("PWLog ERROR: writer is null -> " + message);
			return;
		}
		synchronized (lock) {
			String time = new SimpleDateFormat("HH:mm:ss.SSS").format(new Date());
			String thread = Thread.currentThread().getName();
			writer.println(time + " [" + thread + "] [" + level + "] " + message);
			writer.flush(); // ensure immediate write
		}
	}

	public static void close() {
		synchronized (lock) {
			if (frameworkWriter != null)
				frameworkWriter.close();
			classWriters.values().forEach(PrintWriter::close);
			// Clear Log4j2 ThreadContext
			ThreadContext.clearAll();
		}
	}

	// =============================
	// STEP PASS
	// =============================
	public static void Pass(String className, String message) {
		writeToClass(className, "PASS", message);
		Allure.step("✅ [" + className + "] " + message, () -> {
			attachScreenshotIfRequired();
		});
		console("PASS", className, message);
		stepHistory.get().add("PASS - " + message);
	}

	// =============================
	// STEP FAIL
	// =============================
	public static void Fail(String className, String message) {
		stepHistory.get().add("FAIL - " + message);
		console("FAIL", className, message);
		writeToClass(className, "FAIL", message);
		Allure.step("❌ [" + className + "] " + message, () -> {
			attachScreenshotIfRequired();
		});
		
		Page page = PWBaseTest.getPage();
		String html = page.content();
		PWBaseTest.getFailureContext().setHtml(html);
		String url = page.url();
		PWBaseTest.getFailureContext().setUrl(url);
//		System.out.println("Console Logs: " + ctx.getConsoleLogs());
//		System.out.println("Failed APIs: " + ctx.getFailedApis());
		FailureContext ctx = PWBaseTest.getFailureContext();
		if (PWBaseTest.isAIImmediateMode()) {
			Allure.step("🤖 AI Root Cause Analysis", () -> {

				long start = System.currentTimeMillis();

				String aiResult = AIFailureAnalyzer.analyze(ctx);

				long end = System.currentTimeMillis();
				long duration = end - start;
				Optional<String> testUuid = Allure.getLifecycle().getCurrentTestCase();
				Optional<String> stepUuid = Allure.getLifecycle().getCurrentTestCaseOrStep();
				System.out.println("👉 Allure Test UUID: " + testUuid.orElse("NULL"));
				System.out.println("👉 Allure Step UUID: " + stepUuid.orElse("NULL"));
				Allure.addAttachment("AI Response Time", "text/plain", new ByteArrayInputStream(
						("Response Time: " + duration + " ms").getBytes(StandardCharsets.UTF_8)), ".txt");

				Allure.addAttachment("AI Failure Analysis", "text/markdown",
						new ByteArrayInputStream(aiResult.getBytes(StandardCharsets.UTF_8)), ".md");

			});
		} else {
			AISuiteCollector.addFailure(ctx);
		}
		// Mark test as failed
		throw new AssertionError(message);
	}

	// =============================
	// STEP INFO
	// =============================
	public static void Info(String className, String message) {
		writeToClass(className, "INFO", message);
		stepHistory.get().add("Info - " + message);
	}

	private static void writeToClass(String className, String level, String message) {
		classLog(className, level, message);
	}

	private static void attachScreenshotIfRequired() {
		try {
			String screenShot = PWBaseTest.mapAllVariables.get("screenShot");
			if ("Y".equalsIgnoreCase(screenShot)) {
				Page page = PWBaseTest.getPage();
				if (page != null) {
					byte[] screenshot = page.screenshot();
					Allure.addAttachment("screenshot", "image/png", new ByteArrayInputStream(screenshot), "png");
				}
			}
		} catch (Exception ignored) {
		}
	}

	// =============================
	// TEST END CHECK
	// =============================
	public static int getStepFailureCount() {
		return stepFailureCount.get();
	}

	public static void resetStepCounter(int threshold) {
		stepFailureCount.set(0);
//		stepFailureThreshold.set(threshold);
	}

	public static void clearStepContext() {
		stepFailureCount.remove();
//		stepFailureThreshold.remove();
	}

	private static void console(String status, String className, String message) {
		String time = java.time.LocalTime.now().toString();
		System.out.println(time + " " + status + " [" + className + "] " + message);
	}

	public static List<String> getStepHistory() {
		return stepHistory.get();
	}
}// EOF