package pw.base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Stream;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Video;
import com.microsoft.playwright.options.WaitUntilState;

import ai.FailureContext;
import ai.PWActions;
import io.qameta.allure.Allure;
import listener.PWExecutionController;
import pw.utils.PWEmail;
import pw.utils.PWLog;
import pw.utils.PWUtilities;

/**
 * Author Tapan, Feb 26
 * 
 */
@Listeners({ PWExecutionController.class, io.qameta.allure.testng.AllureTestNg.class })
public abstract class PWBaseTest {
	protected Playwright playwright;
	protected Browser browser;
	protected BrowserContext context;
	private static ThreadLocal<Page> page = new ThreadLocal<>();
	public static Map<String, String> mapExcel = new HashMap<>();
	String currDir = System.getProperty("user.dir") + "\\"; // FW dir
	protected Map<String, Map<String, String>> excelDataCache = new HashMap<>();
	private static final ThreadLocal<Integer> totalTestCases = ThreadLocal.withInitial(() -> 0);
	private String jenEnv = "", jenSendEmail = "", jenRerun = "";// Jenkins related variables
	private String reportTime;
	private String execSuiteName;
	public static Map<String, String> mapAllVariables = new HashMap<>();
	static String ts;
	private static final String AUTH_FOLDER = "src/test/resources/auth/";
	// AI
	private static ThreadLocal<FailureContext> failureContext = ThreadLocal.withInitial(FailureContext::new);
	private static boolean jenAIExecutionMode = true;
	private static ThreadLocal<String> currentClassName = new ThreadLocal<>();

 

	// new method for Automatic google login
	protected void launchBrowser(String browserName, Method method, Map<String, String> dataMap) {

	    TestMeta meta = null;
	    Class<?> testClass = method.getDeclaringClass();

	    // Annotation resolution
	    if (method.isAnnotationPresent(TestMeta.class)) {
	        meta = method.getAnnotation(TestMeta.class);
	    } else if (testClass.isAnnotationPresent(TestMeta.class)) {
	        meta = testClass.getAnnotation(TestMeta.class);
	    }

	    String remoteIP = mapAllVariables.get("remoteIP");
	    String hideBrowser = mapAllVariables.get("hideBrowser").trim();
	    String recordVideo = mapAllVariables.get("recordVideo").trim();
	    String baseUrl = mapAllVariables.get("url").trim();
	    String useProxy = PWUtilities.getConfigValue("config.properties", "useProxy");
	    String debugMode = mapAllVariables.get("debugMode");

	    try {

	        playwright = Playwright.create();

	        BrowserType browserType;

	        switch (browserName.toLowerCase()) {

	        case "firefox":
	            browserType = playwright.firefox();
	            break;

	        case "chrome":
	        case "chromium":
	            browserType = playwright.chromium();
	            break;

	        default:
	            System.out.println("No Browser Parameter passed : " + browserName);
	            return;
	        }

	        // =====================================================
	        // CLEAN BROWSER ARGS
	        // =====================================================

	        List<String> args = Arrays.asList(
	        	    "--start-maximized",
	        	    "--disable-dev-shm-usage",
	        	    "--no-sandbox",
	        	    "--disable-gpu",
	        	    "--disable-blink-features=AutomationControlled",
	        	    "--disable-extensions",
	        	    "--disable-plugins",
	        	    "--no-first-run",
	        	    "--no-default-browser-check",
	        	    "--disable-popup-blocking",

	        	    "--disable-session-crashed-bubble",
	        	    "--hide-crash-restore-bubble",
	        	    "--disable-infobars",
	        	    "--disable-features=TranslateUI"
	        	);

	        String userDataDir = "playwright-user-data";

	        // =====================================================
	        // IMPORTANT FIX
	        // =====================================================

	        BrowserType.LaunchPersistentContextOptions persistentOptions =
	                new BrowserType.LaunchPersistentContextOptions()
	                        .setHeadless(hideBrowser.equalsIgnoreCase("Y"))
	                        .setChannel("chrome")
	                        .setIgnoreHTTPSErrors(true)

	                        // THIS IS THE REAL MAXIMIZE FIX
	                        .setViewportSize(null)

	                        .setArgs(args)

	                        .setUserAgent(
	                                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
	                                "AppleWebKit/537.36 (KHTML, like Gecko) " +
	                                "Chrome/120.0.0.0 Safari/537.36"
	                        );

	        // Proxy
	        if ("true".equalsIgnoreCase(useProxy)) {
	            persistentOptions.setProxy(
	                    new com.microsoft.playwright.options.Proxy("http://127.0.0.1:8888")
	            );
	        }

	        // Slow motion for debugging
	        if ("Y".equalsIgnoreCase(debugMode)) {
	            persistentOptions.setSlowMo(8000);
	        }

	        // Video recording
	        if (recordVideo.equalsIgnoreCase("Y")) {
	            persistentOptions.setRecordVideoDir(Paths.get("target/videos"));
	        }

	        // =====================================================
	        // LAUNCH BROWSER
	        // =====================================================

	        context = browserType.launchPersistentContext(
	                Paths.get(userDataDir),
	                persistentOptions
	        );

	        // Hide webdriver flag
	        context.addInitScript(
	            "Object.defineProperty(navigator, 'webdriver', {get: () => undefined})"
	        );

	        // Use existing tab instead of creating new tab
	        if (context.pages().size() > 0) {
	            page.set(context.pages().get(0));
	        } else {
	            page.set(context.newPage());
	        }

	        // Close any extra tabs
	        while (context.pages().size() > 1) {
	            context.pages().get(context.pages().size() - 1).close();
	        }

	        // =====================================================
	        // PAGE
	        // =====================================================

	        if (context.pages().size() > 0) {
	            page.set(context.pages().get(0));
	        } else {
	            page.set(context.newPage());
	        }

	        // =====================================================
	        // AI LOGGING
	        // =====================================================

	        context.onConsoleMessage(msg -> {
	            String log = msg.type() + " : " + msg.text();
	            PWBaseTest.getFailureContext().addConsoleLog(log);
	        });

	        context.onResponse(response -> {
	            if (response.status() >= 400) {
	                String api = response.url() + " : " + response.status();
	                PWBaseTest.getFailureContext().addFailedApi(api);
	            }
	        });

	        Page page = getPage();

	        // =====================================================
	        // BUILD URL
	        // =====================================================

	        String finalUrl = baseUrl;

	        if (meta != null &&
	                meta.navPath() != null &&
	                !meta.navPath().isEmpty()) {

	            finalUrl = joinUrl(baseUrl, meta.navPath());
	        }

	        // =====================================================
	        // NAVIGATE
	        // =====================================================

	        page.navigate(
	                finalUrl,
	                new Page.NavigateOptions()
	                        .setWaitUntil(WaitUntilState.DOMCONTENTLOADED)
	                        .setTimeout(60000)
	        );

	        // =====================================================
	        // LOGIN CHECK
	        // =====================================================

	        boolean requiresAuth =
	                meta != null &&
	                meta.user() != UserType.NONE;

	        if (requiresAuth) {

	            boolean loggedIn = false;

	            if (meta.navPath() != null &&
	                    !meta.navPath().isEmpty()) {

	                loggedIn = page.url().contains(meta.navPath());
	            }

	            if (!loggedIn) {

	                performLogin(meta.user(), dataMap);

	                // Navigate again after login
	                if (meta.navPath() != null &&
	                        !meta.navPath().isEmpty()) {

	                    page.navigate(
	                            finalUrl,
	                            new Page.NavigateOptions()
	                                    .setWaitUntil(WaitUntilState.DOMCONTENTLOADED)
	                                    .setTimeout(60000)
	                    );
	                }

	                // Wait for redirect
	                if (meta.navPath() != null &&
	                        !meta.navPath().isEmpty()) {

	                    page.waitForURL("**" + meta.navPath() + "**");
	                }
	            }
	        }

	    } catch (Exception e) {

	        System.out.println("Browser Error: " + e.getMessage());
	        e.printStackTrace();

	        throw new RuntimeException(e);
	    }
	}

	private String joinUrl(String baseUrl, String path) {
		if (baseUrl.endsWith("/") && path.startsWith("/")) {
			return baseUrl + path.substring(1);
		}
		if (!baseUrl.endsWith("/") && !path.startsWith("/")) {
			return baseUrl + "/" + path;
		}
		return baseUrl + path;
	}

	/**
	 * Fits the browser window to desktop screen size (FULLY MAXIMIZED)
	 * This sets the viewport to match the full available screen resolution
	 * Browser will maximize to the full screen size without taskbar adjustments
	 */
	 
 

	// =========================================================================
	private void performLogin(UserType userType, Map<String, String> dataMap) {

		String baseUrl = mapAllVariables.get("url");
		String reportName = mapAllVariables.get("reportName").toLowerCase();
		Page page = getPage();

		switch (userType) {

		case ADMIN:

		    // ✅ STEP 1: Check if already logged in
		    try {
		        Thread.sleep(2000);

		        // Check if dashboard or expected element is visible
		        if (PWActions.isVisible("//h1[text()='Dashboard']", "Check if dashboard exists")) {
		            System.out.println("✅ Already logged in via persistent context. Skipping login.");
		            return;
		        }

		    } catch (Exception e) {
		        System.out.println("Dashboard not found, proceeding with login...");
		    }

			// ✅ STEP 2: If not logged in, perform Google login
			try {
				// Open Login Page
				PWActions.navigate(baseUrl, "Open Login Page");
				
				// Wait for page to load completely
				Thread.sleep(3000);
				
 
				Thread.sleep(1500);
				
 
				try {
					if (page.url().contains("accounts.google.com") || page.url().contains("google")) {
				 
 
					} else {
						System.out.println("✅ Successfully redirected from Google - login completed");
//						System.out.println("Current URL: " + page.url());
					}
				} catch (Exception e) {
					System.out.println("URL check skipped: " + e.getMessage());
				}
				
//				System.out.println("✅ Google login flow completed");

			} catch (Exception e) {
				System.err.println("❌ Login Error: " + e.getMessage());
				e.printStackTrace();
				throw new RuntimeException("Login failed: " + e.getMessage(), e);
			}

			// ✅ STEP 3: After successful login, fit window to desktop screen size
		 

			return;

		default:
			throw new IllegalArgumentException("Unsupported user type");
		}
	}

	// Page getter
	public static Page getPage() {
		return page.get();
	}

	protected void closeContext() {
		if (context != null) {
			context.close();
			context = null;
		}
	}

	protected void closeBrowser() {
		if (browser != null) {
			browser.close();
			playwright.close();
			browser = null;
		}
	}

	@BeforeSuite(alwaysRun = true)
	public void BeforeSuiteBase() {
		try {
			 
			ts = System.getProperty("timestamp");
			reportTime = ts;
			Map<String, String> allSuiteParam = PWExecutionController.SUITE_PARAMS;
			// Load all parameters automatically
			readSettingsExcel(allSuiteParam);
			mapExcel.put("executionRunTime", reportTime);
			mapExcel.put("suiteName", PWExecutionController.SUITE_PARAMS.get("suiteFileName"));
			mapAllVariables.putAll(mapExcel);
			for (Map.Entry<String, String> entry : allSuiteParam.entrySet()) {
				if (entry.getValue() != null && !entry.getValue().trim().isEmpty()) {
					mapAllVariables.put(entry.getKey(), entry.getValue().trim());
				}
			}
			// Now get XmlSuite safely from listener
			// Read settings excel (now use mapAllVariables instead of xmlTest)
			String reportFolderName = mapAllVariables.get("reportFolderName");
			String holdNewData = mapAllVariables.get("holdNewData");
			if (holdNewData.length() > 0 && (!"N".equals(holdNewData))) {
				PWUtilities.getExecutionID();
			} else {
				Files.deleteIfExists(Path.of("data", "addmaster.properties"));
			}

			String jenrerun = System.getenv("jenrerun") == null ? "" : System.getenv("jenrerun");
			if ("NotRunTC".equalsIgnoreCase(jenrerun)) {
				jenRerun = "NotRunTC";
			} else {
				PWUtilities.writeToPropsFile("data\\TS_Executed.properties", mapExcel.get("suiteName"),
						reportFolderName + reportTime + "-" + mapExcel.get("suiteName"));
			}
//			createAllureEnvironmentFile();
		} catch (Exception e) {
			System.err.println("❌ Error in Before Suite: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@AfterSuite(alwaysRun = true)
	@Parameters({ "sendEmail" })
	public void tearDown(@Optional("") String sendEmail) {
		String reportFolderName = mapAllVariables.get("reportFolderName");
		if (jenSendEmail.equalsIgnoreCase("Y")) {
			sendEmail = "Y";
		}
		if (sendEmail.equalsIgnoreCase("Y")) {
			PWEmail.SendEmail();
		}
		if (jenRerun.equalsIgnoreCase("Y")) {
			PWUtilities.writeToPropsFile("data\\TS_Executed.properties", execSuiteName,
					reportFolderName + reportTime + "-" + execSuiteName);
		}
	}

//Method method,
	@BeforeMethod(alwaysRun = true)
	public void BeforeMethodBase(ITestContext context, Method method, Object[] testData) {
		String className = method.getDeclaringClass().getSimpleName();
		PWBaseTest.setCurrentClassName(className);
		failureContext.set(new FailureContext());
		String deviceName = context.getCurrentXmlTest().getParameter("deviceName");
		if (deviceName == null) {
			deviceName = mapAllVariables.get("deviceName");
		}
		Map<String, String> dataMap = null;
		if (testData != null) {
			for (Object obj : testData) {
				if (obj instanceof Map) {
					dataMap = (Map<String, String>) obj;
					break;
				}
			}
		}
		launchBrowser(deviceName, method, dataMap);
		// ⏱️ Add 3-second delay for all tests
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			System.out.println("Sleep interrupted: " + e.getMessage());
		}
//		PWLog.resetStepCounter(Integer.parseInt(mapAllVariables.get("SkipCount")));
		// AI
		PWBaseTest.clearFailureContext();
	}

	/**
	 * Loads all the settings required for the driver
	 */
	private void readSettingsExcel(Map<String, String> allSuitePara) {
		String excelName = allSuitePara.get("excelName");
		try {
			String jenEnv = System.getenv("jenenv");
			if (jenEnv == null)
				jenEnv = "";
			String jenSendEmail = System.getenv("minopsendemail");
			if (jenSendEmail == null)
				jenSendEmail = "";
			if (jenSendEmail.length() > 1)
				System.out.println("Email Send : " + jenSendEmail + "," + jenEnv);
			int ienv = 1;
			if (jenEnv.equalsIgnoreCase("stage")) {
				ienv = 2; // Stage environment
			}
			if (jenEnv.equalsIgnoreCase("prod")) {
				ienv = 3; // Stage environment
			}
			if (verifyInputfile(currDir + "data\\Project.xlsx") == false) {
				System.out.println("Configuration file not found, Can't continue...");
				infoBox("Configuration file not found, Can't continue...", "Error.");
				throw new Exception("Configuration missing...");
			}
			if (verifyInputfile(currDir + "data\\" + excelName + "TestData.xlsm") == false) {
				System.out.println("Project test data file not found, Can't continue...");
				infoBox("Project file not found, Can't continue...", "Error.");
				throw new Exception("Configuration missing...");
			}
			try (FileInputStream fis = new FileInputStream(currDir + "data\\" + excelName + "TestData.xlsm");
					XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
				XSSFSheet sheet = workbook.getSheetAt(workbook.getSheetIndex("settings"));
				for (int row = 1; row <= sheet.getLastRowNum(); row++) {
					String key = getSafeCellValue(sheet, row, 0); // first column = key
					if (key.isEmpty())
						break;
					String value = resolveEnvPlaceholder(getSafeCellValue(sheet, row, ienv));
					// Step 2: Read all key-value pairs
					mapExcel.put(key, value);
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} catch (Exception e) {
			throw new RuntimeException("Fatal Error while reading Excel Settings. Aborting execution.", e);
		}
	}

	/**
	 * This function verifies file exist on the given path
	 * 
	 */
	private boolean verifyInputfile(String filename) {
		try {
			File file = new File(filename);
			if (file.exists()) {
				return true;
			}
		} catch (Exception e) {
			System.out.println("File does not exist: " + filename);
		}
		return false;
	}

	/**
	 * @param infoMessage
	 * @param titleBar
	 */
	private void infoBox(String infoMessage, String titleBar) {
		JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Tapan, 12/12/2021
	 * 
	 * @param cellIndex
	 * @param iRow
	 * @return
	 */
	private String getSafeCellValue(XSSFSheet sheet, int rowIndex, int colIndex) {
		try {
			XSSFRow row = sheet.getRow(rowIndex);
			if (row == null)
				return "";
			XSSFCell cell = row.getCell(colIndex);
			if (cell == null)
				return "";
			if (cell.getCellType() == CellType.BLANK)
				return "";
			return cell.toString().trim().replaceAll("\\.0$", "");
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Resolves ${ENV_NAME} placeholders found in Excel cell values to their
	 * corresponding environment variables. This lets secrets (API keys, passwords,
	 * DB connection strings) be injected at runtime by Jenkins/CI instead of being
	 * stored in the committed test-data workbook. If the environment variable is not
	 * set, the original ${...} placeholder is left intact so the failure is visible.
	 *
	 * Required environment variables (set these in Jenkins):
	 *   GEMINI_API_KEY, EMAIL_APP_PASSWORD, MINOP_PASSWORD, VISIONAI_PASSWORD,
	 *   MYSQL_CONN_QC, MYSQL_CONN_STAGE
	 */
	public static String resolveEnvPlaceholder(String value) {
		if (value == null || value.isEmpty() || value.indexOf("${") < 0)
			return value;
		java.util.regex.Matcher m = java.util.regex.Pattern.compile("\\$\\{([A-Za-z0-9_]+)\\}").matcher(value);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			String env = System.getenv(m.group(1));
			m.appendReplacement(sb, java.util.regex.Matcher.quoteReplacement(env != null ? env : m.group(0)));
		}
		m.appendTail(sb);
		return sb.toString();
	}

	private void analyzeTestNGXml(String xmlName) {
		System.out.println("🧾 Starting TestNG XML Analysis...");
		try {
			File xmlFile = new File(xmlName);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(xmlFile);
			doc.getDocumentElement().normalize();
			NodeList testList = doc.getElementsByTagName("test");
			int totalTests = testList.getLength();
			int classCount = 0;
			int totalEnabledMethods = 0;
			int totalDisabledMethods = 0;
			for (int i = 0; i < testList.getLength(); i++) {
				Element testElement = (Element) testList.item(i);
				// ---------------- GROUP FILTERS ----------------
				Set<String> includedGroups = new HashSet<>();
				Set<String> excludedGroups = new HashSet<>();
				NodeList groupNodes = testElement.getElementsByTagName("groups");
				if (groupNodes.getLength() > 0) {
					Element groupsElement = (Element) groupNodes.item(0);
					NodeList runNodes = groupsElement.getElementsByTagName("run");
					if (runNodes.getLength() > 0) {
						Element runElement = (Element) runNodes.item(0);
						NodeList includeGroups = runElement.getElementsByTagName("include");
						for (int g = 0; g < includeGroups.getLength(); g++) {
							includedGroups.add(((Element) includeGroups.item(g)).getAttribute("name"));
						}
						NodeList excludeGroups = runElement.getElementsByTagName("exclude");
						for (int g = 0; g < excludeGroups.getLength(); g++) {
							excludedGroups.add(((Element) excludeGroups.item(g)).getAttribute("name"));
						}
					}
				}
				// ---------------- CLASSES ----------------
				NodeList classList = testElement.getElementsByTagName("class");
				classCount += classList.getLength();
				for (int j = 0; j < classList.getLength(); j++) {
					Element classElement = (Element) classList.item(j);
					String className = classElement.getAttribute("name");
					try {
						Class<?> clazz = Class.forName(className);
						Method[] methods = clazz.getDeclaredMethods();
						int enabledInClass = 0;
						int disabledInClass = 0;
						for (Method method : methods) {
							if (!method.isAnnotationPresent(Test.class)) {
								continue;
							}
							Test testAnnotation = method.getAnnotation(Test.class);
							if (!testAnnotation.enabled()) {
								disabledInClass++;
								continue;
							}
							String[] methodGroups = testAnnotation.groups();
							boolean matchesGroup = true;
							// include groups
							if (!includedGroups.isEmpty()) {
								matchesGroup = Arrays.stream(methodGroups).anyMatch(includedGroups::contains);
							}
							// exclude groups override
							if (matchesGroup && !excludedGroups.isEmpty()) {
								if (Arrays.stream(methodGroups).anyMatch(excludedGroups::contains)) {
									matchesGroup = false;
								}
							}
							if (matchesGroup) {
								enabledInClass++;
							}
						}
						totalEnabledMethods += enabledInClass;
						totalDisabledMethods += disabledInClass;
					} catch (ClassNotFoundException e) {
						System.err.println("❌ Could not load class: " + className);
					}
				}
			}
			totalTestCases.set(totalDisabledMethods);
			// ---------------- SUMMARY ----------------
			System.out.println("\n🔍 TestNG Suite Analysis Summary:");
			System.out.println("• Total <test> tags: " + totalTests);
			System.out.println("• Total classes: " + classCount);
			System.out.println("• Total enabled tests (after group filter): " + totalEnabledMethods);
			System.out.println("• Total disabled tests: " + totalDisabledMethods);
		} catch (Exception e) {
			System.err.println("❌ Failed to analyze testng.xml: " + e.getMessage());
		}
	}

	protected Map<String, String> GetExcelRow(String tcid, String excelName, String sheetName) {
		String temp = mapAllVariables.get("excelName");
		if (excelName == null || excelName.isEmpty())
			excelName = temp;
		String testEnv = mapAllVariables.get("testenv");
		if (testEnv == null || testEnv.isEmpty()) {
			throw new RuntimeException("Failed to get the environment information");
		}
		String cacheKey = tcid + "|" + excelName + "|" + sheetName + "|" + testEnv;
		// 1️⃣ Check cache
		if (excelDataCache.containsKey(cacheKey)) {
			return excelDataCache.get(cacheKey);
		}
		String filePath = "data\\" + excelName + "TestData.xlsm";
		try (FileInputStream fis = new FileInputStream(filePath); XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
			XSSFSheet sheet = workbook.getSheet(sheetName);
			if (sheet == null) {
				throw new RuntimeException("Sheet not found: " + sheetName);
			}
			XSSFRow headerRow = sheet.getRow(0);
			int colCount = headerRow.getLastCellNum();
			List<String> headers = new ArrayList<>();
			// Normalize environment prefix
			String envPrefix = testEnv.toLowerCase() + "_";
			// 2️⃣ Process headers with strict env filtering
			for (int i = 0; i < colCount; i++) {
				String rawHeader = headerRow.getCell(i).getStringCellValue().trim().toLowerCase();
				if (!rawHeader.contains("_")) {
					// Normal column → always include
					headers.add(rawHeader);
				} else if (rawHeader.startsWith(envPrefix)) {
					// Matching environment column → strip prefix
					headers.add(rawHeader.substring(envPrefix.length()));
				} else {
					// Different environment encountered → stop further reading
					break;
				}
			}
			// 3️⃣ Find TCID row
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				XSSFRow row = sheet.getRow(i);
				if (row == null)
					continue;
				String currentTcid = row.getCell(0).getStringCellValue().trim();
				if (tcid.equals(currentTcid)) {
					Map<String, String> rowData = new HashMap<>();
					for (int j = 0; j < headers.size(); j++) {
						XSSFCell cell = row.getCell(j);
						String value = (cell == null) ? "" : resolveEnvPlaceholder(cell.toString().trim());
						rowData.put(headers.get(j), value);
					}
					excelDataCache.put(cacheKey, rowData);
					return rowData;
				}
			}
			throw new RuntimeException("TCID not found: " + tcid);
		} catch (Exception e) {
			throw new RuntimeException("Failed to load Excel data", e);
		}
	}

	@AfterMethod(alwaysRun = true)
	public void tearDown(ITestResult result) throws IOException {
		int stepFailures = PWLog.getStepFailureCount();
		PWLog.getStepHistory().clear();
		try {
			Page currentPage = getPage();
			if (currentPage != null) {
				String recordVideo = mapExcel.get("recordVideo").trim();
				Video video = currentPage.video();
				// Close page first (this finalizes the recording)
				currentPage.close();
				if ("Y".equalsIgnoreCase(recordVideo) && video != null) {
					if (result.getStatus() == ITestResult.FAILURE) {
						try {
							Path videoFile = Paths.get("target/videos",
									result.getTestClass().getName() + "_" + result.getMethod().getMethodName() + "_"
											+ Thread.currentThread().getId() + ".webm");
							Files.createDirectories(videoFile.getParent());
							video.saveAs(videoFile);
							Allure.addAttachment(result.getMethod().getMethodName() + " - Video", "video/webm",
									Files.newInputStream(videoFile), "webm");
						} catch (Exception e) {
							System.out.println("Video save failed: " + e.getMessage());
						}
					} else {
						// Delete video for passed tests
						video.delete();
					}
				}
				// Close context
				if (context != null) {
					context.close();
				}
				page.remove();
			}
		} finally {
			PWLog.clearStepContext();
		}
	}

	private void createAllureEnvironmentFile() {
		String resultsDir = System.getProperty("allure.results.directory");
		if (resultsDir == null)
			resultsDir = "target" + File.separator + "allure-results";
		Properties props = new Properties();
		props.setProperty("Environment", mapAllVariables.get("testenv"));
		props.setProperty("Browser", mapAllVariables.get("deviceName"));
		String tester = System.getProperty("user.name");
		props.setProperty("Tester", tester);
		props.setProperty("Suite", mapAllVariables.get("suiteName"));
		try (FileOutputStream fos = new FileOutputStream(resultsDir + "/environment.properties")) {
			props.store(fos, "Allure Environment");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void deleteStorageFiles() {
		Path authDir = Paths.get(AUTH_FOLDER);
		if (!Files.exists(authDir)) {
			return;
		}
		try (Stream<Path> files = Files.list(authDir)) {
			files.filter(path -> path.toString().endsWith(".json")).forEach(path -> {
				try {
					Files.delete(path);
//					System.out.println("Deleted storage file: " + path);
				} catch (IOException e) {
					System.out.println("Failed to delete: " + path);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 🔵 NEW: Delete persistent context data (user data directory)
	 * Use this if you want to force re-login on next test run
	 * This clears browser cache, cookies, and stored data
	 */
	public static void deletePersistentContextData() {
		try {
			Path userDataPath = Paths.get("playwright-user-data");
			if (Files.exists(userDataPath)) {
				Files.walk(userDataPath)
					.sorted((a, b) -> b.compareTo(a)) // Delete files first, then folders
					.forEach(path -> {
						try {
							Files.delete(path);
							System.out.println("✅ Deleted: " + path);
						} catch (IOException e) {
							System.out.println("Failed to delete: " + path);
						}
					});
				System.out.println("✅ Persistent context data cleared successfully");
			}
		} catch (Exception e) {
			System.err.println("Error deleting persistent context: " + e.getMessage());
		}
	}

	// AI
	public static FailureContext getFailureContext() {
		return failureContext.get();
	}

	public static void clearFailureContext() {
		failureContext.remove();
	}

	//
	public static boolean isAIImmediateMode() {
		return jenAIExecutionMode;
	}

	public static void setCurrentClassName(String className) {
		currentClassName.set(className);
	}

	public static String getCurrentClassName() {
		return currentClassName.get();
	}

	public static void testme() {
		try {
			URL url = new URL(
					"https://generativelanguage.googleapis.com/v1beta/models?key=AIzaSyDyMgKy_mRxAafaXFadLgr9pgULTNFSjMo");
			URLConnection conn = null;
			try {
				conn = url.openConnection();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);

			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		in.close();

	}
}// EOF
