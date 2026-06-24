package ai;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.options.WaitUntilState;
import com.microsoft.playwright.Page;
import java.io.File;
import java.nio.file.Paths;
import pw.base.PWBaseTest;
import pw.utils.PWLog;

public class PWActions {
	public static void click(String locator, String stepName) {
		Page page = PWBaseTest.getPage();
		FailureContext ctx = PWBaseTest.getFailureContext();
		ctx.setActionContext(stepName, "click", locator);
		PWLog.Info(PWBaseTest.getCurrentClassName(), stepName);
		page.locator(locator).click();
	}

	// clear
	public static void clear(String locator, String stepName) {
		Page page = PWBaseTest.getPage();
		FailureContext ctx = PWBaseTest.getFailureContext();
		ctx.setActionContext(stepName, "Clear", locator);
		PWLog.Info(PWBaseTest.getCurrentClassName(), stepName);
		page.locator(locator).clear();
	}

	// refreshre
	public static void refresh(String stepName) {
		Page page = PWBaseTest.getPage();
		FailureContext ctx = PWBaseTest.getFailureContext();

		ctx.setActionContext(stepName, "refresh", "page");
		PWLog.Info(PWBaseTest.getCurrentClassName(), stepName);

		page.reload();
	}

	// new window
	public static Page createNewWindow(String stepName) {
		Page currentPage = PWBaseTest.getPage();
		FailureContext ctx = PWBaseTest.getFailureContext();

		ctx.setActionContext(stepName, "createNewWindow", "new tab");
		PWLog.Info(PWBaseTest.getCurrentClassName(), stepName);

		Page newPage = currentPage.context().newPage(); // create new tab

		return newPage;
	}

	public static void fill(String locator, String value, String stepName) {
		Page page = PWBaseTest.getPage();
		FailureContext ctx = PWBaseTest.getFailureContext();
		ctx.setActionContext(stepName, "fill", locator);
		PWLog.Info(PWBaseTest.getCurrentClassName(), stepName); // <-- important for step tracking
		page.locator(locator).fill(value);
	}

	public static void doubleClick(String locator, String stepName) {
		Page page = PWBaseTest.getPage();
		FailureContext ctx = PWBaseTest.getFailureContext();

		ctx.setActionContext(stepName, "doubleClick", locator);
		PWLog.Info(PWBaseTest.getCurrentClassName(), stepName);

		page.locator(locator).waitFor();

		page.locator(locator).dblclick();
	}

	public static void getText(String locator, String expectedText, String stepName) {
		Page page = PWBaseTest.getPage();
		FailureContext ctx = PWBaseTest.getFailureContext();

		ctx.setActionContext(stepName, "getText", locator);
		PWLog.Info(PWBaseTest.getCurrentClassName(), stepName);

		page.locator(locator).waitFor();

		String actualText = page.locator(locator).innerText();

		// Validate
		if (!actualText.trim().equals(expectedText)) {
			ctx.setErrorMessage("Text mismatch. Expected: " + expectedText + " but found: " + actualText);
			throw new RuntimeException("Text validation failed: " + actualText);
		}
	}

	public static boolean isVisible(String locator, String stepName) {
		Page page = PWBaseTest.getPage();
		FailureContext ctx = PWBaseTest.getFailureContext();
		ctx.setActionContext(stepName, "isVisible", locator);
		PWLog.Info(PWBaseTest.getCurrentClassName(), stepName);

		try {
			return page.locator(locator).isVisible();
		} catch (Exception e) {
			ctx.setErrorMessage(e.getMessage());
			return false;
		}
	}

	public static void select(String locator, String value, String stepName) {
		Page page = PWBaseTest.getPage();
		FailureContext ctx = PWBaseTest.getFailureContext();
		ctx.setActionContext(stepName, "select", locator);
		PWLog.Info(PWBaseTest.getCurrentClassName(), stepName);
		page.locator(locator).selectOption(value);
	}

	public static void navigate(String url, String stepName) {
		Page page = PWBaseTest.getPage();
		FailureContext ctx = PWBaseTest.getFailureContext();
		ctx.setActionContext(stepName, "navigate", url);
		PWLog.Info(PWBaseTest.getCurrentClassName(), stepName);
		page.navigate(url, new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
	}

	public static void waitFor(String locator, String stepName, int timeoutMs) {

		Page page = PWBaseTest.getPage();
		FailureContext ctx = PWBaseTest.getFailureContext();

		ctx.setActionContext(stepName, "waitFor", locator);

		PWLog.Info(PWBaseTest.getCurrentClassName(), stepName);

		page.locator(locator).waitFor(new Locator.WaitForOptions().setTimeout(timeoutMs));
	}

	public static void waitForURL(String urlPattern, String stepName) {

		Page page = PWBaseTest.getPage();
		FailureContext ctx = PWBaseTest.getFailureContext();

		ctx.setActionContext(stepName, "waitForURL", urlPattern);

		PWLog.Info(PWBaseTest.getCurrentClassName(), stepName);

		page.waitForURL(urlPattern);
	}

	public static void clickByRole(AriaRole role, String name, boolean exact, String stepName) {

		Page page = PWBaseTest.getPage();
		FailureContext ctx = PWBaseTest.getFailureContext();

		String locatorInfo = "role=" + role + ", name=" + name;

		ctx.setActionContext(stepName, "click", locatorInfo);

		PWLog.Info(PWBaseTest.getCurrentClassName(), stepName);

		page.getByRole(role, new Page.GetByRoleOptions().setName(name).setExact(exact)).click();
	}

	// wait for hidden element
	public static void waitForHidden(String locator, String stepName, int timeoutMs) {

		Page page = PWBaseTest.getPage();
		FailureContext ctx = PWBaseTest.getFailureContext();

		ctx.setActionContext(stepName, "waitForHidden", locator);

		PWLog.Info(PWBaseTest.getCurrentClassName(), stepName);

		page.locator(locator)
				.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN).setTimeout(timeoutMs));
	}

	// Uploadfile
	public static void uploadFile(String locator, String fileName, String stepName) {
		try {
			Page page = PWBaseTest.getPage();
			FailureContext ctx = PWBaseTest.getFailureContext();

			ctx.setActionContext(stepName, "uploadFile", locator);
			PWLog.Info(PWBaseTest.getCurrentClassName(), stepName);

			String baseDir = System.getProperty("user.dir");

			System.out.println("Project Path: " + baseDir);
			System.out.println("Received File Name: " + fileName);

			String filePath = Paths.get(baseDir, "data", "video", fileName).toString();

			File file = new File(filePath);

			System.out.println("Final File Path: " + filePath);

			if (!file.exists()) {
				throw new RuntimeException("File not found: " + file.getAbsolutePath());
			}

			page.locator(locator).setInputFiles(Paths.get(filePath));

			System.out.println("File uploaded successfully");

		} catch (Exception e) {
			System.out.println("File upload failed: " + e.getMessage());
			throw new RuntimeException(e);
		}
	}

	// Attach MULTIPLE files in a single setInputFiles call. Calling setInputFiles once per file replaces the
	// previous selection, so all files must be set together here.
	public static void uploadFiles(String locator, String stepName, String... fileNames) {
		try {
			Page page = PWBaseTest.getPage();
			FailureContext ctx = PWBaseTest.getFailureContext();
			ctx.setActionContext(stepName, "uploadFiles", locator);
			PWLog.Info(PWBaseTest.getCurrentClassName(), stepName);

			String baseDir = System.getProperty("user.dir");
			java.nio.file.Path[] paths = new java.nio.file.Path[fileNames.length];
			for (int i = 0; i < fileNames.length; i++) {
				java.nio.file.Path p = Paths.get(baseDir, "data", "video", fileNames[i]);
				if (!new File(p.toString()).exists()) {
					throw new RuntimeException("File not found: " + p);
				}
				paths[i] = p;
				System.out.println("Adding file: " + p);
			}

			page.locator(locator).setInputFiles(paths); // ALL files in one call

			System.out.println("All files attached (" + fileNames.length + ")");

		} catch (Exception e) {
			System.out.println("Multi-file upload failed: " + e.getMessage());
			throw new RuntimeException(e);
		}
	}

	// GetText------------------------------------------------------------------------------------------------
	public static String getText(String locator, String stepName) {
	    Page page = PWBaseTest.getPage();
	    FailureContext ctx = PWBaseTest.getFailureContext();

	    ctx.setActionContext(stepName, "getText", locator);
	    PWLog.Info(PWBaseTest.getCurrentClassName(), stepName);

	    String text = page.locator(locator).textContent();

	    return text != null ? text.trim() : "";
	}

}// EOF