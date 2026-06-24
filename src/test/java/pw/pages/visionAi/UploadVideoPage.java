package pw.pages.visionAi;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import ai.PWActions;
import pw.base.PWBaseTest;
import pw.utils.ImageCompareUtil;
import pw.utils.TextCompareUtil;

public class UploadVideoPage

{
	// Global variable to store initial count
	private static int initialVideoCount = 0;
	private static int finalVideoCount = 0;

	// Global variables to store raw count
	private static int initialRawCount = 0;
	private static int finalRawCount = 0;

	// Global variables to store processing count
	private static int initialProcessingCount = 0;
	private static int finalProcessingCount = 0;
	
	////camera count
	private int initialCameraCount;
	private int finalCameraCount;

	//// recording count
	private int initialRecordingCount;
	private int finalRecordingCount;

	//// used storage (in GB) read from Analytics page
	private double initialUsedStorage;
	private double finalUsedStorage;

	//// search API count read from Analytics page
	private int initialSearchCount;
	private int finalSearchCount;

	//// analyse count read from Analytics page
	private int initialAnalyseCount;
	private int finalAnalyseCount;

	//// current balance / credit values read from Analytics page (TC_47)
	private double initialCurrentBalanceCredit;
	private double recentCount;
	private double newCurrentBalance;

	//// consumed count values read from Analytics page (TC_48)
	private double initialConsumedCount;
	private double newConsumedCount;

	// Instance variable for Playwright Page
	private Page page;

	public UploadVideoPage(Page page) {
		this.page = page;
	}

	// TC_01---------------------------------------------------------------------------------------------------------------------------------------

	public boolean ClickSigninBtn() {
		try {

			PWActions.click("//button[contains(text(),'Sign In')]", "Clicked on sign in");

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
		return true;
	}

	public boolean Isdisplaydashboard() {
		try {
			Thread.sleep(4000);
			boolean status = PWActions.isVisible("//button[contains(., 'Upload Video')]", "Dashboard is displayed");

			if (status) {
				return true;
			} else {
				PWBaseTest.getFailureContext().setErrorMessage("Dashboard not displayed");
				return false;
			}

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
	}

	public boolean UPload_Video() {
		try {

			PWActions.click("//button[contains(., 'Upload Video')] ", "Clicked on  Upload video");

			// NOTE: do NOT click the "select video" drop-zone here. That click opens the native
			// OS file picker, which Playwright cannot control (the dialog just stays open).
			// Instead, Uploadmultiplefile() sets the file directly on the hidden <input type=file>
			// via setInputFiles, which fires the same change event without any OS dialog.
			// (No waitFor on the input here: it is hidden, so a visible-wait would time out;
			//  setInputFiles auto-waits for the element to be attached.)
			Thread.sleep(1000);

			System.out.println("Done");
		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
		return true;
	}

	// Testcase_02---------------------------------------------------------------------------------------------------------------------------------------
	public boolean Create_Collection() {
		try {

			PWActions.click("(//button[contains(@class,'flex cursor-pointer')])[3]", "Clicked on Create collection");
			PWActions.click("//input[@placeholder='e.g., Educational Content']", "Clicked on name of collection");
			PWActions.fill("//input[@placeholder='e.g., Educational Content']", "Ravi's Collection",
					"Enter collection name");
			PWActions.click(
					"(//div[@class='w-5 h-5 rounded-md border flex items-center justify-center transition-colors mr-2 border-white/10 group-hover:border-white/30'])[1]",
					" select video from collection");
			PWActions.click("//button[@type='button']", "Clicked on Create");

			System.out.println("Done");
		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
		return true;
	}

	// Testcase_03---------------------------------------------------------------------------------------------------------------------------------------
	public boolean Edit_Collection() {
		try {
			PWActions.click("(//div[@class='relative aspect-video rounded-lg overflow-hidden mb-3 bg-zinc-800'])[1]",
					"Clicked on Created collection");
			PWActions.click(
					"//button[@class='p-2 rounded-lg hover:bg-white/5 text-zinc-400 hover:text-white transition-all border border-transparent hover:border-white/5 cursor-pointer']",
					"Clicked on Edit collection");

			// Wait for the modal/dialog to appear and find the INPUT field with correct
			// placeholder
			PWActions.waitFor("//input[@placeholder='Collection name']", "Wait for edit input field to appear", 30000);

			// Clear existing value and fill with new collection name
			PWActions.clear("//input[@placeholder='Collection name']", "Clear input field");
			PWActions.fill("//input[@placeholder='Collection name']", "Ravi new Collection",
					"Enter name of collection");

			// Click Save button using text content instead of index
			PWActions.click("//button[normalize-space()='Save']", "Clicked on Save");
			System.out.println("Done");
		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
		return true;
	}

	// TC_02-----------------------------------------------------------------------------------------------
//	public boolean ClickONTeams() {
//		try {
//
//			PWActions.click("(//div[@class='flex items-start justify-between gap-4'])[2]", "Clicked on sign in");
//			PWActions.click(
//					"//button[@class='inline-flex items-center gap-2 rounded-lg bg-violet-600 px-4 py-2 text-sm font-medium text-white shadow hover:bg-violet-700 transition disabled:opacity-50 disabled:cursor-not-allowed']",
//					"Clicked on sign in");
//			PWActions.click(
//					"//div[@class='w-9 h-9 rounded-full bg-violet-100 flex items-center justify-center flex-shrink-0']",
//					"Clicked on sign in");
//			System.out.println("Done");
//		} catch (Exception e) {
//			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
//			return false;
//		}
//		return true;
//	}
//
//	public boolean Uploadfile(String fileName) {
//		try {
//			PWActions.uploadFile("input[type='file']", fileName, "Uploading video file");
//			return true;
//
//		} catch (Exception e) {
//			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
//			return false;
//		}
//	}

	// TC_02---------------------------------------------------------------------------------------------------------------------------------------

	public boolean Uploadmultiplefile(String... fileNames) {
		try {
			for (String fileName : fileNames) {
				PWActions.uploadFile("input[type='file']", fileName, "Uploading video file: " + fileName);
			}
			return true;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
	}

	public boolean newuploadvideos(String... fileNames) {
		try {
			for (String fileName : fileNames) {
				PWActions.uploadFile("input[type='file']", fileName, "Uploading video file: " + fileName);
			}
			for (String fileName : fileNames) {
				PWActions.uploadFile("input[type='file']", fileName, "Uploading video file: " + fileName);
			}
			for (String fileName : fileNames) {
				PWActions.uploadFile("input[type='file']", fileName, "Uploading video file: " + fileName);
			}
			for (String fileName : fileNames) {
				PWActions.uploadFile("input[type='file']", fileName, "Uploading video file: " + fileName);
			}
			for (String fileName : fileNames) {
				PWActions.uploadFile("input[type='file']", fileName, "Uploading video file: " + fileName);
			}
			for (String fileName : fileNames) {
				PWActions.uploadFile("input[type='file']", fileName, "Uploading video file: " + fileName);
			}
			for (String fileName : fileNames) {
				PWActions.uploadFile("input[type='file']", fileName, "Uploading video file: " + fileName);
			}
			for (String fileName : fileNames) {
				PWActions.uploadFile("input[type='file']", fileName, "Uploading video file: " + fileName);
			}
			return true;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
	}

	public boolean UploadMultiplevideoSuccessfully() {
		try {

			PWActions.click("(//button[@type='button'])[6]", "Clicked on  Upload");

			String videoXpath = "//div[contains(@class,'p-3')]" + "[.//h3[starts-with(normalize-space(),'VI_')]"
					+ " and .//span[contains(.,'Just now')]]";

			boolean status = PWActions.isVisible(videoXpath, "Video Uploaded successfully");

			return status;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
	}

//	public boolean Uploadfile(String fileName) {
//		try {
//
//			// 🔥 Build full path dynamically
//			String projectPath = System.getProperty("user.dir");
//			String filePath = projectPath + "\\data\\video\\" + fileName;
//
//			System.out.println("Final Upload Path: " + filePath);
//
//			// 🔥 Use PWActions wrapper instead of driver
//			PWActions.uploadFile("input[type='file']", filePath, "Uploading video file");
//
//			System.out.println("File uploaded successfully");
//
//			return true;
//
//		} catch (Exception e) {
//
//			e.printStackTrace();
//			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
//
//			return false;
//		}
//	}

	public boolean CompleteUploadVideo() {
		try {

			PWActions.click("(//button[@type='button'])[6]", "Clicked on  Upload");
			PWActions.waitFor(
					"//div[contains(@class,'p-3')][.//h3[normalize-space()='VI_01'] and .//span[contains(.,'Just now')]]",
					"wait for Upload video", 120000);
			boolean status = PWActions.isVisible(
					"//div[contains(@class,'p-3')][.//h3[normalize-space()='VI_01'] and .//span[contains(.,'Just now')]]",
					"Video Uploaded successfully");

			return true;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
	}

	// TC_03---------------------------------------------------------------------------------------------------------------------------------------

	public boolean GetInitialCount() {
		try {

			PWActions.refresh("Refresh the page to get updated count");

			PWActions.waitFor("(//span[contains(@class,'text-2xl') and contains(@class,'font-semibold')])[1]",
					"Wait for count element to be visible", 30000);

			// Add wait to ensure count is fully loaded and stabilized
			Thread.sleep(2000);

			String countText = PWActions.getText(
					"(//span[contains(@class,'text-2xl') and contains(@class,'font-semibold')])[1]",
					"Get initial count");
			System.out.println("Initial count text is: " + countText);

			// Parse the count from the text
			initialVideoCount = Integer.parseInt(countText.trim());
			System.out.println("✅ Initial count stored globally: " + initialVideoCount);

			return true;
		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
	}

	public boolean GetFinalCount() {
		try {
			String countText = PWActions.getText(
					"(//span[contains(@class,'text-2xl') and contains(@class,'font-semibold')])[1]", "Get final count");
			System.out.println("Final count text is: " + countText);

			// Parse the count from the text
			finalVideoCount = Integer.parseInt(countText.trim());
			System.out.println("✅ Final count retrieved: " + finalVideoCount);

			return true;
		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
	}

	public boolean VerifyCountIncreasedByOne() {
		try {
			System.out.println("📊 Comparing counts...");
			System.out.println("Initial Count: " + initialVideoCount);
			System.out.println("Final Count: " + finalVideoCount);

			int difference = finalVideoCount - initialVideoCount;
			System.out.println("Difference: " + difference);

			if (difference == 1) {
				System.out.println("✅ TEST PASSED - Count increased by exactly 1");
				return true;
			} else {
				String errorMsg = "❌ TEST FAILED - Count did not increase by 1. Expected increase: 1, Actual increase: "
						+ difference;
				System.out.println(errorMsg);
				PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
				return false;
			}
		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
	}

	// TC_04---------------------------------------------------------------------------------------------------------------------------------------

	public boolean GetInitialRawCount() {
		try {

			PWActions.waitFor("//div[text()='RAW']/following-sibling::div[contains(@class,'items-baseline')]/span[1]",
					"Wait for raw count element to be visible", 30000);

			// Add wait to ensure count is fully loaded and stabilized
			Thread.sleep(2000);

			String countText = PWActions.getText(
					"//div[text()='RAW']/following-sibling::div[contains(@class,'items-baseline')]/span[1]",
					"Get initial raw count");
			System.out.println("Initial raw count text is: " + countText);

			// Parse the count from the text
			initialRawCount = Integer.parseInt(countText.trim());
			System.out.println("✅ Initial raw count stored globally: " + initialRawCount);

			return true;
		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
	}

	public boolean GetFinalRawCount() {
		try {
			PWActions.waitFor("//div[text()='RAW']/following-sibling::div[contains(@class,'items-baseline')]/span[1]",
					"Wait for raw count element to be visible", 30000);

			// Add wait to ensure count is fully updated after delete operation
			Thread.sleep(3000);

			String countText = PWActions.getText(
					"//div[text()='RAW']/following-sibling::div[contains(@class,'items-baseline')]/span[1]",
					"Get final raw count");
			System.out.println("Final raw count text is: " + countText);

			// Parse the count from the text
			finalRawCount = Integer.parseInt(countText.trim());
			System.out.println("✅ Final raw count retrieved: " + finalRawCount);

			return true;
		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
	}

	public boolean VerifyRawCountDecreasedByOne() {
		try {
			System.out.println("📊 Comparing raw counts...");
			System.out.println("Initial Raw Count: " + initialRawCount);
			System.out.println("Final Raw Count: " + finalRawCount);

			int difference = initialRawCount - finalRawCount;
			System.out.println("Difference: " + difference);

			if (difference == 1) {
				System.out.println("✅ TEST PASSED - Raw count decreased by exactly 1");
				return true;
			} else {
				String errorMsg = "❌ TEST FAILED - Raw count did not decrease by 1. Expected decrease: 1, Actual decrease: "
						+ difference;
				System.out.println(errorMsg);
				PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
				return false;
			}
		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
	}

	public boolean DeleteVideo() {
		try {

			PWActions.refresh("Refresh the page before delete");
			PWActions.waitFor("(//button[@type='button'])[2]", "Wait for delete button to be visible", 30000);
			PWActions.click("(//button[@type='button'])[2]", "delete video");
			PWActions.click(
					"//button[@class='flex cursor-pointer items-center gap-2 px-5 py-2 rounded-lg bg-red-500 text-white text-sm font-medium hover:bg-red-600 transition-all shadow-[0_0_20px_rgba(239,68,68,0.2)] disabled:opacity-40 disabled:cursor-not-allowed']",
					" click on delete tab");

			// Wait for deletion to be processed
			Thread.sleep(3000);
		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
		return true;
	}

	// TC_80
	// _---------------------------------------------------------------------------------------------------------------------------------------

	public boolean UPload_largelegth_video() {
		try {

			PWActions.click("(//button[text()='Upload Video'])[2]", "  Click on upload video");

			PWActions.waitFor("//div[text()='This video is longer than the maximum allowed 60 minutes.']",
					"Wait for video name element to be visible", 30000);

			boolean status = PWActions.isVisible(
					"//div[text()='This video is longer than the maximum allowed 60 minutes.']",
					" Error message for instagram link is displayed");

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
		return true;
	}

	// TC_05---------------------------------------------------------------------------------------------------------------------------------------

	public boolean UPload_You_Tube_video(String url) {
		try {

			PWActions.click(
					"//button[@class='flex cursor-pointer items-center gap-2 bg-white text-black px-4 py-2 rounded-lg font-medium hover:bg-zinc-200 transition-colors shadow-depth-1']",
					"Clicked on  Upload video");

			PWActions.waitFor(
					"//button[@class='flex-1 flex items-center justify-center gap-2 py-2 rounded-md text-sm font-medium transition-all cursor-pointer text-zinc-400 hover:text-white']",
					"Wait for you tube tab", 30000);
			PWActions.click(
					"//button[@class='flex-1 flex items-center justify-center gap-2 py-2 rounded-md text-sm font-medium transition-all cursor-pointer text-zinc-400 hover:text-white']",
					"Clicked on  YouTube URL tab");
			PWActions.click("input[type='url']", "Click on You tube placeholder");
			PWActions.fill("input[type='url']", url, "Enter You tube URL");
			PWActions.fill("//input[@placeholder='e.g. Car Crash']", "panchayatvideo", " enter name of video");
			PWActions.click("(//button[text()='Upload Video'])[2]", "  Click on upload video");

			PWActions.waitFor("//h3[contains(.,'panchayatvideo') and ancestor::div[.//span[contains(.,'Just now')]]]",
					"Wait for video name element to be visible", 30000);

			boolean status = PWActions.isVisible(
					"//h3[contains(.,'panchayatvideo') and ancestor::div[.//span[contains(.,'Just now')]]]",
					"You Tube Video Uploaded successfully");

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
		return true;
	}
	// TC_06---------------------------------------------------------------------------------------------------------------------------------------

	public boolean UPload_BrokenYou_Tube_video(String url) {
		try {

			PWActions.click(
					"//button[@class='flex cursor-pointer items-center gap-2 bg-white text-black px-4 py-2 rounded-lg font-medium hover:bg-zinc-200 transition-colors shadow-depth-1']",
					"Clicked on  Upload video");

			PWActions.waitFor(
					"//button[@class='flex-1 flex items-center justify-center gap-2 py-2 rounded-md text-sm font-medium transition-all cursor-pointer text-zinc-400 hover:text-white']",
					"Wait for you tube tab", 30000);
			PWActions.click(
					"//button[@class='flex-1 flex items-center justify-center gap-2 py-2 rounded-md text-sm font-medium transition-all cursor-pointer text-zinc-400 hover:text-white']",
					"Clicked on  YouTube URL tab");
			PWActions.click("input[type='url']", "Click on You tube placeholder");
			PWActions.fill("input[type='url']", url, "Enter You tube URL");
			PWActions.fill("//input[@placeholder='e.g. Car Crash']", " Broken Video", " enter name of video");
			PWActions.click("(//button[text()='Upload Video'])[2]", "  Click on upload video");

			PWActions.waitFor(
					"//div[text()=\"This video can't be imported because it is no longer available on YouTube.\"]",
					"Wait for video name element to be visible", 30000);

			boolean status = PWActions.isVisible(
					"//div[text()=\"This video can't be imported because it is no longer available on YouTube.\"]",
					" Error message for broken You Tube video is displayed");

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
		return true;
	}
	// TC_07---------------------------------------------------------------------------------------------------------------------------------------

	public boolean UPload_PrivateYou_Tube_video(String url) {
		try {

			PWActions.click(
					"//button[@class='flex cursor-pointer items-center gap-2 bg-white text-black px-4 py-2 rounded-lg font-medium hover:bg-zinc-200 transition-colors shadow-depth-1']",
					"Clicked on  Upload video");

			PWActions.waitFor(
					"//button[@class='flex-1 flex items-center justify-center gap-2 py-2 rounded-md text-sm font-medium transition-all cursor-pointer text-zinc-400 hover:text-white']",
					"Wait for you tube tab", 30000);
			PWActions.click(
					"//button[@class='flex-1 flex items-center justify-center gap-2 py-2 rounded-md text-sm font-medium transition-all cursor-pointer text-zinc-400 hover:text-white']",
					"Clicked on  YouTube URL tab");
			PWActions.click("input[type='url']", "Click on You tube placeholder");
			PWActions.fill("input[type='url']", url, "Enter You tube URL");
			PWActions.fill("//input[@placeholder='e.g. Car Crash']", "Private Video", " enter name of video");
			PWActions.click("(//button[text()='Upload Video'])[2]", "  Click on upload video");

			PWActions.waitFor("//div[text()=\"This video can't be imported because it is private.\"]",
					"Wait for video name element to be visible", 30000);

			boolean status = PWActions.isVisible(
					"//div[text()=\"This video can't be imported because it is private.\"]",
					" Error message for broken You Tube video is displayed");

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
		return true;
	}
	// TC_08---------------------------------------------------------------------------------------------------------------------------------------

	public boolean UPload_Instagram_video(String url) {
		try {

			PWActions.click(
					"//button[@class='flex cursor-pointer items-center gap-2 bg-white text-black px-4 py-2 rounded-lg font-medium hover:bg-zinc-200 transition-colors shadow-depth-1']",
					"Clicked on  Upload video");

			PWActions.waitFor(
					"//button[@class='flex-1 flex items-center justify-center gap-2 py-2 rounded-md text-sm font-medium transition-all cursor-pointer text-zinc-400 hover:text-white']",
					"Wait for you tube tab", 30000);
			PWActions.click(
					"//button[@class='flex-1 flex items-center justify-center gap-2 py-2 rounded-md text-sm font-medium transition-all cursor-pointer text-zinc-400 hover:text-white']",
					"Clicked on  YouTube URL tab");
			PWActions.click("input[type='url']", "Click on You tube placeholder");
			PWActions.fill("input[type='url']", url, "Enter You tube URL");
			PWActions.fill("//input[@placeholder='e.g. Car Crash']", "Instagram Video", " enter name of video");
			PWActions.click("(//button[text()='Upload Video'])[2]", "  Click on upload video");

			PWActions.waitFor("//div[text()=\"Enter a valid YouTube URL.\"]",
					"Wait for video name element to be visible", 30000);

			boolean status = PWActions.isVisible("//div[text()=\"Enter a valid YouTube URL.\"]",
					" Error message for instagram link is displayed");

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
		return true;
	}
	// TC_09---------------------------------------------------------------------------------------------------------------------------------------

	public boolean UPload_LargeUTube_video(String url) {
		try {

			PWActions.click(
					"//button[@class='flex cursor-pointer items-center gap-2 bg-white text-black px-4 py-2 rounded-lg font-medium hover:bg-zinc-200 transition-colors shadow-depth-1']",
					"Clicked on  Upload video");

			PWActions.waitFor(
					"//button[@class='flex-1 flex items-center justify-center gap-2 py-2 rounded-md text-sm font-medium transition-all cursor-pointer text-zinc-400 hover:text-white']",
					"Wait for you tube tab", 30000);
			PWActions.click(
					"//button[@class='flex-1 flex items-center justify-center gap-2 py-2 rounded-md text-sm font-medium transition-all cursor-pointer text-zinc-400 hover:text-white']",
					"Clicked on  YouTube URL tab");
			PWActions.click("input[type='url']", "Click on You tube placeholder");
			PWActions.fill("input[type='url']", url, "Enter You tube URL");
			PWActions.fill("//input[@placeholder='e.g. Car Crash']", "two hours video", " enter name of video");
			PWActions.click("(//button[text()='Upload Video'])[2]", "  Click on upload video");

			PWActions.waitFor("//div[text()='This video is longer than the maximum allowed 60 minutes.']",
					"Wait for video name element to be visible", 30000);

			boolean status = PWActions.isVisible(
					"//div[text()='This video is longer than the maximum allowed 60 minutes.']",
					" Error message for instagram link is displayed");

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
		return true;
	}
	// TC_10---------------------------------------------------------------------------------------------------------------------------------------

	public boolean processvideo() {
		try {

			PWActions.click(
					"//button[@class='px-4 py-1.5 hover:bg-white/5 rounded-lg text-xs font-medium transition-all cursor-pointer bg-zinc-100 text-zinc-900 shadow-depth-1 hover:bg-zinc-100']",
					" Click on raw filter");
			PWActions.click("(//input[@type='checkbox'])[1]", " select checkbox of video");
			PWActions.click(
					"//button[@class='flex items-center gap-1.5 px-3 py-1.5 rounded-lg bg-white text-black text-sm font-medium hover:bg-zinc-200 transition-colors cursor-pointer disabled:opacity-40 disabled:cursor-not-allowed shadow-[0_0_20px_rgba(255,255,255,0.1)]']",
					" Click on process Tab");

			Thread.sleep(3000);
		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
		return true;
	}

	public boolean IsdisplayProcessed() {
		try {

			PWActions.waitFor("//div[text()='Processing']", "Wait for video processing stat is visible", 30000);
			boolean status1 = PWActions.isVisible("//div[text()='Processing']",
					" Error message for instagram link is displayed");

			PWActions.click(
					"//button[@class='px-4 py-1.5 hover:bg-white/5 rounded-lg text-xs font-medium transition-all cursor-pointer bg-zinc-100 text-zinc-900 shadow-depth-1 hover:bg-zinc-100']",
					" select checkbox of video");

			PWActions.waitFor("(//span[text()='Processed'])[1]", "Wait for video processing tag", 30000);

			boolean status2 = PWActions.isVisible("(//span[text()='Processed'])[1]", "  proessed tag is displayed");

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
		return true;
	}

	// TC_13---------------------------------------------------------------------------------------------------------------------------------------
	public boolean processMultiplevideo() {
		try {

			PWActions.click(
					"//button[@class='px-4 py-1.5 hover:bg-white/5 rounded-lg text-xs font-medium transition-all cursor-pointer bg-zinc-100 text-zinc-900 shadow-depth-1 hover:bg-zinc-100']",
					"Click on raw filter");

			Thread.sleep(3000);

			// Store all checkbox elements
			List<ElementHandle> checkBoxes = PWBaseTest.getPage().querySelectorAll("//input[@type='checkbox']");

			// Click first two checkboxes
			for (int i = 0; i < Math.min(2, checkBoxes.size()); i++) {
				checkBoxes.get(i).click();
				Thread.sleep(1000);
			}

			PWActions.click(" (//button[@type='button'])[15]", "Click on  Process All Tab");

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}

		return true;
	}

	public boolean IsdisplaymultipleProcessed() {
		try {

			PWActions.waitFor("//div[text()='Processing']", "Wait for video processing stat is visible", 30000);
			boolean status1 = PWActions.isVisible("//div[text()='Processing']",
					" Error message for instagram link is displayed");

			List<ElementHandle> processedList = PWBaseTest.getPage().querySelectorAll("//span[text()='Processed']");

			for (int i = 0; i < Math.min(2, processedList.size()); i++) {
				System.out.println(processedList.get(i).isVisible());
			}

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
		return true;
	}
	// TC_13---------------------------------------------------------------------------------------------------------------------------------------

	public boolean GetInitialProcessingCount() {
		try {

			PWActions.waitFor("(//span[@class='text-2xl font-semibold text-white leading-none'])[2]",
					"Wait for processing count element to be visible", 30000);

			// Add wait to ensure count is fully loaded and stabilized
			Thread.sleep(2000);

			String countText = PWActions.getText("(//span[@class='text-2xl font-semibold text-white leading-none'])[2]",
					"Get initial processing count");
			System.out.println("📊 Initial Processing Count Text: " + countText);

			// Parse the count from the text
			initialProcessingCount = Integer.parseInt(countText.trim());
			System.out.println("✅ Initial processing count stored globally: " + initialProcessingCount);

			return true;
		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
	}
	// method one _processvideo
	// method one IsdisplayProcessed

	public boolean GetFinalProcessingCount() {
		try {
			// Add wait to ensure count is fully updated after processing
			Thread.sleep(3000);

			String countText = PWActions.getText("(//span[@class='text-2xl font-semibold text-white leading-none'])[2]",
					"Get final processing count");
			System.out.println("📊 Final Processing Count Text: " + countText);

			// Parse the count from the text
			finalProcessingCount = Integer.parseInt(countText.trim());
			System.out.println("✅ Final processing count retrieved: " + finalProcessingCount);

			return true;
		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
	}

	/**
	 * Verify that the processing count increased by exactly 1 Call this after:
	 * GetInitialProcessingCount() -> processvideo() -> IsdisplayProcessed() ->
	 * GetFinalProcessingCount()
	 */
	public boolean VerifyProcessingCountIncreasedByOne() {
		try {
			System.out.println("📊 ========== PROCESSING COUNT COMPARISON ==========");
			System.out.println("Initial Processing Count: " + initialProcessingCount);
			System.out.println("Final Processing Count: " + finalProcessingCount);

			int difference = finalProcessingCount - initialProcessingCount;
			System.out.println("Count Difference: " + difference);

			if (difference == 1) {
				System.out.println("✅ TEST PASSED - Processing count increased by exactly 1");
				System.out.println("========================================================");
				return true;
			} else {
				String errorMsg = "❌ TEST FAILED - Processing count did not increase by 1. Expected increase: 1, Actual increase: "
						+ difference;
				System.out.println(errorMsg);
				System.out.println("========================================================");
				PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
				return false;
			}
		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
	}
	// TC_14_00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000

	public boolean Check_Notification() {
		try {

			PWActions.click(
					" //button[@class='w-[40px] h-[40px] inline-flex cursor-pointer items-center justify-center rounded-lg transition-all group text-muted-foreground hover:bg-accent hover:text-foreground rim-light hover:shadow-depth-1 relative hover:bg-white/5']",
					"Clicked on Notification bell icon");
			PWActions.click(
					"//div[@class='w-12 h-12 rounded-full bg-white/5 border border-white/10 flex items-center justify-center']",
					"Clicked on select video");

			boolean status = PWActions.isVisible("(//div[.//p[text()='VI_22'] and .//p[text()='Video processed']])[8]",
					" Error message for broken You Tube video is displayed");

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
		return true;
	}
	// TC_14_00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000

	public boolean Search_video() {
		try {

			PWActions.click("//input[@placeholder='Search uploads...']", "Clicked on  placeholder of search video");
			PWActions.fill("//input[@placeholder='Search uploads...']", "VI_01.mp4", " video name is searched");
			boolean status = PWActions.isVisible("(//div[.//p[text()='VI_22'] and .//p[text()='Video processed']])[8]",
					" video is display After searching video name");

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
		return true;
	}
	// TC_16====================================================================================================

	public boolean VerifyProcessedVideo() {
		try {

			PWActions.click("//button[text()='Processed']", "Clicked on processed Tab");

			String xpath = "//span[@class='text-[10px] font-bold uppercase px-2 py-1 rounded-md border backdrop-blur-md bg-green-500/20 text-green-400 border-green-500/30']";

			Locator statusElements = page.locator(xpath);

			int count = statusElements.count();

			System.out.println("Total Status Elements Found: " + count);

			List<String> allStatuses = new ArrayList<>();

			for (int i = 0; i < count; i++) {

				String status = statusElements.nth(i).innerText().trim();

				allStatuses.add(status);

				System.out.println("Status " + (i + 1) + " : " + status);

				if (!status.equalsIgnoreCase("Processed")) {

					System.out.println("❌ Failed at index " + i + ". Expected: Processed, Actual: " + status);

					return false;
				}
			}

			System.out.println("All Statuses: " + allStatuses);
			System.out.println("✅ All videos are Processed");

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}

		return true;
	}

	// TC_17====================================================================================================
	public boolean VerifyRAWVideo() {
		try {

			PWActions.click("//button[text()='RAW']", "Clicked on processed Tab");

			String xpath = "//span[@class='text-[10px] font-bold uppercase px-2 py-1 rounded-md border backdrop-blur-md bg-yellow-500/20 text-yellow-400 border-yellow-500/30']";

			Locator statusElements = page.locator(xpath);

			int count = statusElements.count();

			System.out.println("Total Status Elements Found: " + count);

			List<String> allStatuses = new ArrayList<>();

			for (int i = 0; i < count; i++) {

				String status = statusElements.nth(i).innerText().trim();

				allStatuses.add(status);

				if (!status.equalsIgnoreCase("RAW")) {

					System.out.println("❌ Failed at index " + i + ". Expected: RAW, Actual: " + status);

					return false;
				}
			}

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}

		return true;
	}
	// TC_18====================================================================================================

	public boolean Verify_Video_Is_Playing() {
		try {

			PWActions.click(
					"(//div[@class='absolute inset-0 bg-black/40 opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center'])[1]",
					"Clicked on video thumbnail to play");

			boolean status = PWActions.isVisible("//video", "Video element is visible");

			return status;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
	}

	// TC_19====================================================================================================
	public boolean Delete_FirstCollection_From_API() {
		try {

			// Get Token
			String tokenData = (String) PWBaseTest.getPage().evaluate("() => localStorage.getItem('token')");

			String token = tokenData;

			// If token is stored as a JSON object, extract accessToken
			if (tokenData != null && tokenData.trim().startsWith("{")) {
				try {
					JSONObject tokenObj = new JSONObject(tokenData);
					token = tokenObj.getString("accessToken");
				} catch (Exception e) {
					// If parsing as JSON fails, use the token as-is
					System.out.println("⚠️ Token data is not JSON, using as-is");
				}
			}

			if (token == null || token.isEmpty()) {
				System.out.println("❌ Token not found");
				return false;
			}

			System.out.println("✅ Token Generated");

			// Create API Context
			APIRequestContext request = Playwright.create().request()
					.newContext(new APIRequest.NewContextOptions().setExtraHTTPHeaders(
							Map.of("Authorization", "Bearer " + token, "Content-Type", "application/json")));

			// GET Collections
			APIResponse getResponse = request
					.get("https://dev.vision.mikshi.ai/api/backend/v1/collections/list?page=1&page_size=10");

			if (getResponse.status() != 200) {
				System.out.println("❌ Collection API Failed");
				return false;
			}

			String responseBody = getResponse.text();

			System.out.println("Collection Response:");
			System.out.println(responseBody);

			// Parse JSON
			JSONObject json = new JSONObject(responseBody);

			// ✅ API returns "data" array, not "collections"
			JSONArray collections = json.getJSONArray("data");

			if (collections.length() == 0) {
				System.out.println("❌ No collections found");
				return false;
			}

			// ✅ Extract "collection_id" field from the response
			String collectionId = collections.getJSONObject(0).getString("collection_id");

			System.out.println("Collection Id : " + collectionId);
			System.out.println("Collection Name: " + collections.getJSONObject(0).getString("name"));

			// DELETE API
			String deleteUrl = "https://dev.vision.mikshi.ai/api/backend/v1/collections/delete/" + collectionId;
			System.out.println("📡 Hitting DELETE API: " + deleteUrl);

			APIResponse deleteResponse = request.delete(deleteUrl);

			System.out.println("Delete Status : " + deleteResponse.status());
			System.out.println("Delete Response : " + deleteResponse.text());

			if (deleteResponse.status() == 200 || deleteResponse.status() == 204) {

				System.out.println("✅ Collection Deleted Successfully");
				return true;
			}

			return false;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// ✅ NEW METHOD: Delete Collection using Specific Collection ID
	public boolean Delete_Collection_By_ID(String collectionId) {
		try {

			// Get Token
			String tokenData = (String) PWBaseTest.getPage().evaluate("() => localStorage.getItem('token')");

			String token = tokenData;

			// If token is stored as a JSON object, extract accessToken
			if (tokenData != null && tokenData.trim().startsWith("{")) {
				try {
					JSONObject tokenObj = new JSONObject(tokenData);
					token = tokenObj.getString("accessToken");
				} catch (Exception e) {
					// If parsing as JSON fails, use the token as-is
					System.out.println("⚠️ Token data is not JSON, using as-is");
				}
			}

			if (token == null || token.isEmpty()) {
				System.out.println("❌ Token not found");
				return false;
			}

			System.out.println("✅ Token Generated");
			System.out.println("🔍 Collection ID: " + collectionId);

			// Create API Context
			APIRequestContext request = Playwright.create().request()
					.newContext(new APIRequest.NewContextOptions().setExtraHTTPHeaders(
							Map.of("Authorization", "Bearer " + token, "Content-Type", "application/json")));

			// DELETE API - Using the provided Collection ID
			String deleteUrl = "https://dev.vision.mikshi.ai/api/backend/v1/collections/delete/" + collectionId;

			System.out.println("📡 Hitting DELETE API: " + deleteUrl);

			APIResponse deleteResponse = request.delete(deleteUrl);

			System.out.println("Delete Status : " + deleteResponse.status());
			System.out.println("Delete Response : " + deleteResponse.text());

			if (deleteResponse.status() == 200 || deleteResponse.status() == 204) {

				System.out.println("✅ Collection Deleted Successfully");
				return true;
			}

			System.out.println("❌ Delete failed with status: " + deleteResponse.status());
			return false;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public boolean Verify_CreateCollection() {
		try {

			PWActions.click(
					"//button[@class='flex cursor-pointer items-center gap-2 bg-white text-black px-4 py-2 rounded-lg font-medium hover:bg-zinc-200 transition-colors shadow-depth-1']",
					"Clicked on Create collection");
			PWActions.click("//input[@id='collection-name']", "Clicked on  Collection name field");
			PWActions.fill("//input[@id='collection-name']", "Sports", "Enter name of collection");
			PWActions.click("//textarea[@id='collection-description']", "Clicked on  Collection description  field");
			PWActions.fill("//textarea[@id='collection-description']", "This collection contains sports videos",
					"Enter description of collection");
			PWActions.click("(//button[@type='button'])[3]", "Clicked on create collection button");

			PWActions.waitFor("//h3[text()='Sports']", "wait for Appear collection", 120000);
			boolean status = PWActions.isVisible("//h3[text()='Sports']", "Collection is created successfully");

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
		return true;
	}

//	public boolean Verify_GetToken() {
//	    try {
//
//	        String token = (String) PWBaseTest.getPage().evaluate(
//	            "() => localStorage.getItem('token')"
//	        );
//
//	        if (token == null || token.isEmpty()) {
//	            System.out.println("❌ Token not found");
//	            return false;
//	        }
//
//	        System.out.println("✅ Token Found");
//	        System.out.println(token);
//
//	        return true;
//
//	    } catch (Exception e) {
//	        PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
//	        return false;
//	    }
//	}

	// TC_20====================================================================================================

	public boolean Verify_CreatesameNameCollection() {
		try {

			PWActions.click(
					"//button[@class='flex cursor-pointer items-center gap-2 bg-white text-black px-4 py-2 rounded-lg font-medium hover:bg-zinc-200 transition-colors shadow-depth-1']",
					"Clicked on Create collection");
			PWActions.click("//input[@id='collection-name']", "Clicked on  Collection name field");
			PWActions.fill("//input[@id='collection-name']", "Sports", "Enter name of collection");
			PWActions.click("//textarea[@id='collection-description']", "Clicked on  Collection description  field");
			PWActions.fill("//textarea[@id='collection-description']", "This collection contains sports videos",
					"Enter description of collection");
			PWActions.click("(//button[@type='button'])[3]", "Clicked on create collection button");
			PWActions.waitFor("//div[text()='A collection with this name already exists for this owner.']",
					"wait for Validation message ", 120000);
			boolean status = PWActions.isVisible(
					"//div[text()='A collection with this name already exists for this owner.']",
					" Not Able to create collection with same name");

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
		return true;
	}
	// TC_21====================================================================================================

	public boolean Add_singlevid_inCollection() {
		try {

			PWActions.click(" (//div[@class='absolute inset-0 bg-gradient-to-t from-black/60 to-transparent'])[1]",
					"Clicked on collection");
			PWActions.click(" //button[@title='Add videos']", "Clicked on  Add video button");
			PWActions.click(
					"(//div[@class='absolute right-2 top-2 flex h-6 w-6 items-center justify-center rounded-md border backdrop-blur-md transition-all border-white/20 glass-card text-transparent group-hover:border-white/40'])[1]",
					" Clicked on select video");
			PWActions.click("(//button[@type='button'])[3]", "Clicked on Add video button");
			PWActions.waitFor(
					"//span[@class='bg-zinc-800 text-zinc-400 text-xs px-2.5 py-1 rounded-full border border-white/5 shrink-0']",
					"wait for Count of collection ", 120000);
			boolean status = PWActions.isVisible(
					"//span[@class='bg-zinc-800 text-zinc-400 text-xs px-2.5 py-1 rounded-full border border-white/5 shrink-0']",
					" 1 video is added in collection");

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
		return true;
	}
	// TC_22====================================================================================================

	public boolean Verify_Edit_Collection() {
		try {
			PWActions.click("(//div[@class='absolute inset-0 bg-gradient-to-t from-black/60 to-transparent'])[1]",
					"Clicked on collection");
			PWActions.click("//button[@title='Edit collection']", "Clicked on Edit collection button");

			// Wait for edit modal to appear
			PWActions.waitFor("//input[@placeholder='Collection name']", "Wait for edit input field", 15000);
			Thread.sleep(1000);

			PWActions.clear("//input[@placeholder='Collection name']", "Clear collection name");
			PWActions.fill("//input[@placeholder='Collection name']", "Sports new", "Enter collection name");

			// Find and click save button - look for buttons within the modal
			PWActions.click("//button[normalize-space()='Save']", "Clicked on save button");

			// Wait for the modal to close and collection name to update
			Thread.sleep(2000);

			// Wait for updated collection name to appear in the DOM
			PWActions.waitFor("//h3[contains(text(),'Sports new')]", "Wait for updated collection name", 15000);

			String actualText = PWActions.getText("//h3[contains(text(),'Sports new')]", "Get updated collection name");

			System.out.println("📝 Updated collection name: " + actualText);

			if (actualText != null && !actualText.isEmpty()) {
				return actualText.trim().equalsIgnoreCase("Sports new");
			} else {
				PWBaseTest.getFailureContext().setErrorMessage("Collection name is null or empty");
				return false;
			}

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage("Error while editcollection name: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// TC_23====================================================================================================

	public boolean Add_Multiple_inCollection() {
		try {

			// Create Collection
			PWActions.click(
					"//button[@class='flex cursor-pointer items-center gap-2 bg-white text-black px-4 py-2 rounded-lg font-medium hover:bg-zinc-200 transition-colors shadow-depth-1']",
					"Clicked on Create collection");

			PWActions.click("//input[@id='collection-name']", "Clicked on Collection name field");
			PWActions.fill("//input[@id='collection-name']", "scenamatic collection", "Enter name of collection");

			PWActions.click("//textarea[@id='collection-description']", "Clicked on Collection description field");
			PWActions.fill("//textarea[@id='collection-description']", "This collection contains cenamatic videos",
					"Enter description of collection");

			PWActions.click("(//button[@type='button'])[3]", "Clicked on create collection button");

			Thread.sleep(2000);

			// Open Collection
			PWActions.click("(//div[@class='absolute inset-0 bg-gradient-to-t from-black/60 to-transparent'])[1]",
					"Clicked on collection");

			PWActions.click("//button[@title='Add videos']", "Clicked on Add video button");

			Thread.sleep(2000);

			String videoCheckbox = "//div[contains(@class,'glass-card') and contains(@class,'backdrop-blur-md')]";

			Locator videos = page.locator(videoCheckbox);

			videos.first().waitFor();

			int totalVideosAvailable = videos.count();
			int videosToSelect = Math.min(totalVideosAvailable, 8);

			System.out.println("📹 Total videos available: " + totalVideosAvailable);
			System.out.println("📹 Selecting first " + videosToSelect + " videos");

			// Select first 8 videos
//			for (int i = 0; i < videosToSelect; i++) {
//				try {
//					videos.nth(i).click();
//					System.out.println("✅ Selected video " + (i + 1));
//					Thread.sleep(500);
//				} catch (Exception e) {
//					System.out.println("❌ Failed to select video " + (i + 1) + ": " + e.getMessage());
//				}
//			}
			List<ElementHandle> elements = PWBaseTest.getPage().querySelectorAll(
					"//div[@class='absolute right-2 top-2 flex h-6 w-6 items-center justify-center rounded-md border backdrop-blur-md transition-all border-white/20 glass-card text-transparent group-hover:border-white/40']");

			for (int i = 0; i < Math.min(8, elements.size()); i++) {
				elements.get(i).click();
				Thread.sleep(1000);
			}

			// Click Add Videos button
			PWActions.click(
					"//button[@class='flex flex-1 cursor-pointer items-center justify-center gap-2 rounded-xl bg-white px-6 py-2.5 text-sm font-medium text-black shadow-[0_0_20px_rgba(255,255,255,0.1)] transition-all hover:bg-zinc-200 disabled:cursor-not-allowed disabled:opacity-40 disabled:hover:bg-white sm:flex-none']",
					"Clicked on Add Videos button");

			Thread.sleep(2000);

			String actualText = PWActions.getText(
					"//span[@class='bg-zinc-800 text-zinc-400 text-xs px-2.5 py-1 rounded-full border border-white/5 shrink-0']",
					"Get updated collection count");

			System.out.println("📊 Collection count: " + actualText);

			return actualText != null && (actualText.contains("Video") || actualText.contains("Videos"));

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// TC_24====================================================================================================
	public boolean Verify_Delete_Collection() {
		try {

			PWActions.click(
					"(//button[@class='absolute top-2 right-2 opacity-0 group-hover:opacity-100 p-1.5 rounded-md bg-black/60 backdrop-blur-md border border-white/10 text-zinc-200 hover:bg-white/5 hover:text-white hover:border-white/5 transition-all cursor-pointer disabled:cursor-not-allowed disabled:opacity-60 disabled:hover:bg-black/60 disabled:hover:text-zinc-200'])[2]",
					"Clicked on delete collection button");

			Locator yesButton = page.locator("//button[text()='Yes']");

			// Wait briefly for modal/button to appear
			yesButton.waitFor();

			boolean isVisible = yesButton.isVisible();

			if (isVisible) {
				System.out.println("✅ Yes button is displayed");
				return true;
			} else {
				System.out.println("❌ Yes button is not displayed");
				return false;
			}

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// TC_25====================================================================================================
	public boolean Verify_Search_Collection() {
		try {
			PWActions.click("//input[@placeholder='Search collections...']",
					"Clicked on collection search placeholder");
			PWActions.fill("//input[@placeholder='Search collections...']", "scenamatic collection",
					"search collection name");

			boolean status = PWActions.isVisible("//h3[text()='scenamatic collection']",
					" Searched collection  is displayed");

			if (status) {
				return true;
			} else {
				PWBaseTest.getFailureContext().setErrorMessage("Dashboard not displayed");
				return false;
			}

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
	}

	// TC_27====================================================================================================

//	public boolean Verify_Video_Not_disply() {
//		try {
//            // create collection
//			PWActions.click( "//button[@class='flex cursor-pointer items-center gap-2 bg-white text-black px-4 py-2 rounded-lg font-medium hover:bg-zinc-200 transition-colors shadow-depth-1']", "Clicked on Create collection");
//			PWActions.click("//input[@id='collection-name']", "Clicked on  Collection name field");
//			PWActions.fill("//input[@id='collection-name']", "Sports", "Enter name of collection");
//			PWActions.click("//textarea[@id='collection-description']", "Clicked on  Collection description  field");
//			PWActions.fill("//textarea[@id='collection-description']", "This collection contains sports videos",
//					"Enter description of collection");
//			PWActions.click("(//button[@type='button'])[3]", "Clicked on create collection button");
//            //Add video in collection
//			PWActions.click("(//div[@class='absolute inset-0 bg-gradient-to-t from-black/60 to-transparent'])[1]", "Clicked on collection");
//            PWActions.click("//button[@title='Add videos']", "Clicked on Add video button");
//        	PWActions.click( "(//div[@class='absolute right-2 top-2 flex h-6 w-6 items-center justify-center rounded-md border backdrop-blur-md transition-all border-white/20 glass-card text-transparent group-hover:border-white/40'])[1]",
//					" Clicked on select video");
//			PWActions.click("(//button[@type='button'])[3]", "Clicked on Add video button");
//            
//            
//
//		} catch (Exception e) {
//			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
//			return false;
//		}
//		return true;
//	}

	public boolean Verify_Video_Not_disply() {
		try {

			// Create collection
			PWActions.click("button[data-tour=collection-create]", "Clicked on Create collection");

			PWActions.click("//input[@id='collection-name']", "Clicked on Collection name field");

			PWActions.fill("//input[@id='collection-name']", "Politics", "Enter name of collection");

			PWActions.click("//textarea[@id='collection-description']", "Clicked on Collection description field");

			PWActions.fill("//textarea[@id='collection-description']", "This collection contains Politics videos",
					"Enter description of collection");

			PWActions.click("(//button[@type='button'])[3]", "Clicked on create collection button");

			Thread.sleep(2000);

			// Open collection
			PWActions.click("(//div[contains(@class,'bg-gradient-to-t')])[1]", "Clicked on collection");

			PWActions.click("//button[@title='Add videos']", "Clicked on Add video button");

			Thread.sleep(2000);

			// =========================
			// VIDEO SELECTION (COMBO XPATH)
			// =========================

			String videoCheckboxXpath = "//div[@role='checkbox'][.//p[normalize-space()='VI_01']]";

			// Click the specific video
			PWActions.click(videoCheckboxXpath, "Clicked on VI_01 video");

			// Add video
			PWActions.click(
					"//button[@class='flex flex-1 cursor-pointer items-center justify-center gap-2 rounded-xl bg-white px-6 py-2.5 text-sm font-medium text-black shadow-[0_0_20px_rgba(255,255,255,0.1)] transition-all hover:bg-zinc-200 disabled:cursor-not-allowed disabled:opacity-40 disabled:hover:bg-white sm:flex-none']",
					"Clicked on Add video button");

			PWActions.click("//button[@title='Add videos']", "Clicked on Add video button");

			// =========================
			// VERIFY VIDEO NOT DISPLAYED
			// =========================

			Locator video = page.locator(
					"/html/body//div[contains(@class,'custom-scrollbar') and contains(@class,'flex-1') and contains(@class,'overflow-y-auto') and contains(@class,'px-5') and contains(@class,'py-6') and contains(@class,'sm:px-7')]//div[@role='checkbox'][.//p[text()='VI_01']]");

			boolean isVisible = video.isVisible();

			if (!isVisible) {
				System.out.println("✅ VI_01 is NOT visible (removed successfully)");
				return true;
			} else {
				System.out.println("❌ VI_01 is still visible");
				return false;
			}

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	// *****************************************************CAMERA TEST CASES*******************************************************
   //TC_28====================================================================================================
	public boolean Validaton_Msg_Fr_Camera() {
		try {

			PWActions.click(
					"//button[@class='flex cursor-pointer items-center gap-2 bg-white text-black px-4 py-2 rounded-lg font-medium hover:bg-zinc-200 transition-colors shadow-depth-1']",
					"Clicked on Connect Cameara button");
			PWActions.click(
					"//button[@class='flex items-center gap-2 px-5 py-2 rounded-xl bg-white text-black text-sm font-medium hover:bg-zinc-200 transition-all shadow-[0_0_20px_rgba(255,255,255,0.1)] disabled:opacity-50 disabled:cursor-not-allowed cursor-pointer']",
					"Clicked on  Inner connect camera button");

			boolean cameraNameError = PWActions.isVisible("//p[text()='Camera name is required']",
					"Camera name validation message");

			boolean rtspUrlError = PWActions.isVisible("//p[text()='RTSP Stream URL is required']",
					"RTSP Stream URL validation message");

			return cameraNameError && rtspUrlError;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		} }
	//TC_29====================================================================================================
	
	public boolean ValidatonForWrongFormateURL() {
	    try {

	        PWActions.click(
	                "//button[@class='flex cursor-pointer items-center gap-2 bg-white text-black px-4 py-2 rounded-lg font-medium hover:bg-zinc-200 transition-colors shadow-depth-1']",
	                "Clicked on Connect Camera button");

	        PWActions.click(
	                "//input[@placeholder='Enter camera name']",
	                "Clicked on Camera name field");

	        PWActions.fill(
	                "//input[@placeholder='Enter camera name']",
	                "Test Camera",
	                "Enter camera name");

	        PWActions.click(
	                "(//input[@class='w-full bg-zinc-900/60 border border-white/10 rounded-xl py-2.5 px-4 focus:outline-none focus:ring-1 focus:ring-white/20 transition-all placeholder:text-zinc-600 text-white text-sm'])[2]",
	                "Clicked on RTSP Stream URL field");

	        PWActions.fill(
	                "(//input[@class='w-full bg-zinc-900/60 border border-white/10 rounded-xl py-2.5 px-4 focus:outline-none focus:ring-1 focus:ring-white/20 transition-all placeholder:text-zinc-600 text-white text-sm'])[2]",
	                "99999999999",
	                "Enter invalid RTSP Stream URL");

	        PWActions.click(
	                "//button[@class='flex items-center gap-2 px-5 py-2 rounded-xl bg-white text-black text-sm font-medium hover:bg-zinc-200 transition-all shadow-[0_0_20px_rgba(255,255,255,0.1)] disabled:opacity-50 disabled:cursor-not-allowed cursor-pointer']",
	                "Clicked on Inner connect camera button");

	        boolean rtspUrlError = PWActions.isVisible(
	                "//p[text()='Enter a valid RTSP URL (e.g. rtsp://user:pass@192.168.1.100:554/stream)']",
	                "RTSP URL validation message");

	        return rtspUrlError;

	    } catch (Exception e) {
	        PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
	        e.printStackTrace();
	        return false;
	    }
	}
	
	//TC_30====================================================================================================
 
	 
	public boolean ValidatoURL() {
	    try {

	        PWActions.click(
	                "//button[@class='flex cursor-pointer items-center gap-2 bg-white text-black px-4 py-2 rounded-lg font-medium hover:bg-zinc-200 transition-colors shadow-depth-1']",
	                "Clicked on Connect Camera button");

	        PWActions.click("//input[@placeholder='Enter camera name']",
	                "Clicked on Camera name field");

	        PWActions.fill("//input[@placeholder='Enter camera name']",
	                "Test Camera",
	                "Enter camera name");

	        PWActions.click(
	                "(//input[@class='w-full bg-zinc-900/60 border border-white/10 rounded-xl py-2.5 px-4 focus:outline-none focus:ring-1 focus:ring-white/20 transition-all placeholder:text-zinc-600 text-white text-sm'])[2]",
	                "Clicked on RTSP Stream URL field");

	        PWActions.fill(
	                "(//input[@class='w-full bg-zinc-900/60 border border-white/10 rounded-xl py-2.5 px-4 focus:outline-none focus:ring-1 focus:ring-white/20 transition-all placeholder:text-zinc-600 text-white text-sm'])[2]",
	                "rtsp://16.152.6.180:85454/",
	                "Enter RTSP Stream URL");

	        PWActions.click(
	                "//button[@class='flex items-center gap-2 px-5 py-2 rounded-xl bg-white text-black text-sm font-medium hover:bg-zinc-200 transition-all shadow-[0_0_20px_rgba(255,255,255,0.1)] disabled:opacity-50 disabled:cursor-not-allowed cursor-pointer']",
	                "Clicked on Inner connect camera button");

	        String rtspUrlErrorXpath = "//p[text()='Enter a valid RTSP URL (e.g. rtsp://user:pass@192.168.1.100:554/stream)']";
	        PWActions.waitFor(rtspUrlErrorXpath, "Wait for RTSP URL validation message", 30000);

	        boolean rtspUrlError = PWActions.isVisible(rtspUrlErrorXpath, "RTSP URL validation message");

	        return rtspUrlError;

	    } catch (Exception e) {
	        PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
	        e.printStackTrace();
	        return false;
	    }
	}
	//TC_31====================================================================================================
	
	public boolean ConnectCamera() {
	    try {

	        PWActions.click(
	                "//button[@class='flex cursor-pointer items-center gap-2 bg-white text-black px-4 py-2 rounded-lg font-medium hover:bg-zinc-200 transition-colors shadow-depth-1']",
	                "Clicked on Connect Camera button");

	        PWActions.click("//input[@placeholder='Enter camera name']",
	                "Clicked on Camera name field");

	        PWActions.fill("//input[@placeholder='Enter camera name']",
	                "Test Camera",
	                "Enter camera name");

	        PWActions.click(
	                "(//input[@class='w-full bg-zinc-900/60 border border-white/10 rounded-xl py-2.5 px-4 focus:outline-none focus:ring-1 focus:ring-white/20 transition-all placeholder:text-zinc-600 text-white text-sm'])[2]",
	                "Clicked on RTSP Stream URL field");

	        PWActions.fill(
	                "(//input[@class='w-full bg-zinc-900/60 border border-white/10 rounded-xl py-2.5 px-4 focus:outline-none focus:ring-1 focus:ring-white/20 transition-all placeholder:text-zinc-600 text-white text-sm'])[2]",
	                "rtsp://18.60.222.57:8554/webcam",
	                "Enter RTSP Stream URL");

	        PWActions.click(
	                "//button[@class='flex items-center gap-2 px-5 py-2 rounded-xl bg-white text-black text-sm font-medium hover:bg-zinc-200 transition-all shadow-[0_0_20px_rgba(255,255,255,0.1)] disabled:opacity-50 disabled:cursor-not-allowed cursor-pointer']",
	                "Clicked on Inner connect camera button");
	    	PWActions.waitFor("//h3[contains(@class,'font-medium') and normalize-space()='Test Camera']", " Wait for camera to be display", 80000);
	        boolean Camerastatus = PWActions.isVisible(  "//h3[contains(@class,'font-medium') and normalize-space()='Test Camera']", " Camera is connected and displayed in the list");

	        return Camerastatus;

	    } catch (Exception e) {
	        PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
	        e.printStackTrace();
	        return false;
	    }
	}
	//TC_32====================================================================================================
	
	public boolean validationfrsameCameraname() {
	    try {

	        PWActions.click(
	                "//button[@class='flex cursor-pointer items-center gap-2 bg-white text-black px-4 py-2 rounded-lg font-medium hover:bg-zinc-200 transition-colors shadow-depth-1']",
	                "Clicked on Connect Camera button");

	        PWActions.click("//input[@placeholder='Enter camera name']",
	                "Clicked on Camera name field");

	        PWActions.fill("//input[@placeholder='Enter camera name']",
	                "Test Camera",
	                "Enter camera name");

	        PWActions.click(
	                "(//input[@class='w-full bg-zinc-900/60 border border-white/10 rounded-xl py-2.5 px-4 focus:outline-none focus:ring-1 focus:ring-white/20 transition-all placeholder:text-zinc-600 text-white text-sm'])[2]",
	                "Clicked on RTSP Stream URL field");

	        PWActions.fill(
	                "(//input[@class='w-full bg-zinc-900/60 border border-white/10 rounded-xl py-2.5 px-4 focus:outline-none focus:ring-1 focus:ring-white/20 transition-all placeholder:text-zinc-600 text-white text-sm'])[2]",
	                "rtsp://18.60.222.57:8554/webcam",
	                "Enter RTSP Stream URL");

	        PWActions.click(
	                "//button[@class='flex items-center gap-2 px-5 py-2 rounded-xl bg-white text-black text-sm font-medium hover:bg-zinc-200 transition-all shadow-[0_0_20px_rgba(255,255,255,0.1)] disabled:opacity-50 disabled:cursor-not-allowed cursor-pointer']",
	                "Clicked on Inner connect camera button");
	    	PWActions.waitFor("//h3[contains(@class,'font-medium') and normalize-space()='Test Camera']", " Wait for camera to be display", 80000);
	        boolean sameCameranamemessage= PWActions.isVisible(  "//p[text()='A camera with this name already exists']", " same camera name validation message is displayed");

	        return sameCameranamemessage;

	    } catch (Exception e) {
	        PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
	        e.printStackTrace();
	        return false;
	    } }
	//TC_32====================================================================================================
	
	public boolean Deletecamera() {
	    try {

	        PWActions.click(
	                "//button[@class='absolute top-3 right-3 p-2 rounded-md bg-black/60 backdrop-blur-md border border-white/10 text-zinc-300 hover:text-red-400 hover:border-red-500/30 hover:bg-red-500/10 transition-all opacity-0 group-hover:opacity-100 cursor-pointer']",
	                "Clicked on delete Camera button");

	        PWActions.click("//button[@class='flex cursor-pointer items-center gap-2 px-5 py-2 rounded-lg bg-red-500 text-white text-sm font-medium hover:bg-red-600 transition-all shadow-[0_0_20px_rgba(239,68,68,0.2)] disabled:opacity-40 disabled:cursor-not-allowed']",
	                "Clicked on Confirm cameara delete button");

	    
	    	PWActions.waitFor("//span[text()='Camera deleted successfully']", " Wait for camera delete message", 80000);
	        boolean  deletecameramessage= PWActions.isVisible( "//span[text()='Camera deleted successfully']", " Camera is deleted sucessfully message is displayed");

	        return deletecameramessage;

	    } catch (Exception e) {
	        PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
	        e.printStackTrace();
	        return false;
	    } }
	
	//TC_33====================================================================================================
	
	public boolean GetInitialCameraCount() {
	    try {

	        PWActions.refresh("Refresh page");

	        String xpath1 = "(//span[@class='text-3xl font-bold text-white leading-tight'])[1]";
	        String xpath3 = "(//span[@class='text-3xl font-bold text-white leading-tight'])[3]";

	        PWActions.waitFor(xpath1, "Wait for camera count", 30000);

	        Thread.sleep(2000);

	        String count1 = PWActions.getText(xpath1, "Get first camera count").trim();
	        String count3 = PWActions.getText(xpath3, "Get third camera count").trim();

	        System.out.println("Camera Count 1: " + count1);
	        System.out.println("Camera Count 3: " + count3);

	        // Verify both counts are the same
	        if (!count1.equals(count3)) {
	            System.out.println("❌ Count mismatch between element 1 and element 3");
	            return false;
	        }

	        initialCameraCount = Integer.parseInt(count1);

	        System.out.println("✅ Initial Camera Count Stored: " + initialCameraCount);

	        return true;

	    } catch (Exception e) {
	        PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
	        e.printStackTrace();
	        return false;
	    }
	}
	public boolean GetFinalCameraCount() {
	    try {

	        PWActions.refresh("Refresh page");

	        String xpath1 = "(//span[@class='text-3xl font-bold text-white leading-tight'])[1]";
	        String xpath3 = "(//span[@class='text-3xl font-bold text-white leading-tight'])[3]";

	        PWActions.waitFor(xpath1, "Wait for camera count", 30000);

	        Thread.sleep(2000);

	        String count1 = PWActions.getText(xpath1, "Get first camera count").trim();
	        String count3 = PWActions.getText(xpath3, "Get third camera count").trim();

	        System.out.println("Final Camera Count 1: " + count1);
	        System.out.println("Final Camera Count 3: " + count3);

	        if (!count1.equals(count3)) {
	            System.out.println("❌ Final count mismatch between element 1 and element 3");
	            return false;
	        }

	        finalCameraCount = Integer.parseInt(count1);

	        System.out.println("✅ Final Camera Count Stored: " + finalCameraCount);

	        return true;

	    } catch (Exception e) {
	        PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
	        e.printStackTrace();
	        return false;
	    }
	}
	public boolean ValidateCameraCountIncrement() {
	    try {

	        System.out.println("Initial Camera Count: " + initialCameraCount);
	        System.out.println("Final Camera Count: " + finalCameraCount);

	        return finalCameraCount > initialCameraCount;

	        // If exactly one camera should be added:
	        // return finalCameraCount == initialCameraCount + 1;

	    } catch (Exception e) {
	        PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
	        e.printStackTrace();
	        return false;
	    }
	}
	
	// TC_34---------------------------------------------------------------------------------------------------------------------------------------

	public boolean GetInitialRecordingCount() {
		try {

			String recordingCountXpath = "(//span[@class='text-3xl font-bold text-white leading-tight'])[2]";

			PWActions.waitFor(recordingCountXpath, "Wait for recording count element to be visible", 30000);

			// Add wait to ensure count is fully loaded and stabilized
			Thread.sleep(2000);

			// Take count of recording and store it as initial recording count
			String countText = PWActions.getText(recordingCountXpath, "Get initial recording count").trim();
			System.out.println("Initial recording count text is: " + countText);

			initialRecordingCount = Integer.parseInt(countText);
			System.out.println("✅ Initial recording count stored globally: " + initialRecordingCount);

			// Click on start recording button
			PWActions.click( " //button[@class='mt-1 w-full flex items-center justify-center gap-2 bg-zinc-900 hover:bg-zinc-800 text-white text-sm font-medium py-2 rounded-lg border border-white/10 transition-all cursor-pointer disabled:opacity-70 disabled:cursor-wait']",
					" Click on  start recording button");

			Thread.sleep(3000);
		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
		return true;
	}

	public boolean GetFinalRecordingCount() {
		try {

			PWActions.refresh("Refresh the page to get updated recording count");

			String recordingCountXpath = "(//span[@class='text-3xl font-bold text-white leading-tight'])[2]";

			PWActions.waitFor(recordingCountXpath, "Wait for recording count element to be visible", 30000);

			// Add wait to ensure count is fully updated after start recording
			Thread.sleep(3000);

			String countText = PWActions.getText(recordingCountXpath, "Get final recording count").trim();
			System.out.println("Final recording count text is: " + countText);

			finalRecordingCount = Integer.parseInt(countText);
			System.out.println("✅ Final recording count retrieved: " + finalRecordingCount);

			return true;
		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
	}

	public boolean VerifyRecordingCountIncreasedByOne() {
		try {
			System.out.println("📊 Comparing recording counts...");
			System.out.println("Initial Recording Count: " + initialRecordingCount);
			System.out.println("Final Recording Count: " + finalRecordingCount);

			int difference = finalRecordingCount - initialRecordingCount;
			System.out.println("Difference: " + difference);

			if (difference == 1) {
				System.out.println("✅ TEST PASSED - Recording count increased by exactly 1");
				return true;
			} else {
				String errorMsg = "❌ TEST FAILED - Recording count did not increase by 1. Expected increase: 1, Actual increase: "
						+ difference;
				System.out.println(errorMsg);
				PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
				return false;
			}
		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
	}

	// TC_35---------------------------------------------------------------------------------------------------------------------------------------

	public boolean Maximize_And_Minimize_Camera() {
		try {

			// The whole video area is itself a clickable "Maximize live footage" control
			// (a <div role="button">). The corner <button> with the same aria-label is
			// hidden (opacity-0) until hover, so we use the always-visible video area.
			String maximizeArea = "(//div[@role='button' and @aria-label='Maximize live footage'])[1]";

			// The maximized overlay shows a larger REC badge (text-xs) than the card
			// badge (text-[10px]), which we use to detect the maximized state.
			String maximizedIndicator = "//span[contains(@class,'text-xs') and contains(@class,'font-bold') and contains(.,'REC')]";

			// ---- Maximize ----
			PWActions.waitFor(maximizeArea, "Wait for camera live footage to be visible", 30000);
			PWActions.click(maximizeArea, "Click on camera live footage to maximize");

			PWActions.waitFor(maximizedIndicator, "Wait for maximized live footage to appear", 30000);
			boolean isMaximized = PWActions.isVisible(maximizedIndicator, "Camera live footage is maximized");

			if (!isMaximized) {
				System.out.println("❌ Camera live footage is not maximized");
				PWBaseTest.getFailureContext().setErrorMessage("Camera live footage is not maximized");
				return false;
			}
			System.out.println("✅ Camera live footage is maximized successfully");

			// ---- Minimize (close the maximized overlay with Escape) ----
			System.out.println("Pressing Escape to minimize live footage");
			PWBaseTest.getPage().keyboard().press("Escape");

			// Maximized overlay closes -> the larger REC badge disappears
			PWActions.waitForHidden(maximizedIndicator, "Wait for maximized view to close", 30000);
			boolean isMinimized = !PWActions.isVisible(maximizedIndicator, "Camera live footage is minimized");

			if (!isMinimized) {
				System.out.println("❌ Camera live footage is not minimized");
				PWBaseTest.getFailureContext().setErrorMessage("Camera live footage is not minimized");
				return false;
			}
			System.out.println("✅ Camera live footage is minimized successfully");

			return true;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
	}

	// TC_36---------------------------------------------------------------------------------------------------------------------------------------

	public boolean Stop_Recording_Camera() {
		try {

			String liveTag = "//span[contains(.,'Live')]";
			String stopRecordingButton = "//button[contains(.,'Stop Recording')]";
			String notRecordingTag = "//span[contains(.,'Not recording')]";

			// Before stop recording, check the Live tag is displayed
			PWActions.waitFor(liveTag, "Wait for Live tag to be visible", 30000);
			boolean isLive = PWActions.isVisible(liveTag, "Live tag is displayed before stop recording");

			if (!isLive) {
				System.out.println("❌ Live tag is not displayed before stop recording");
				PWBaseTest.getFailureContext().setErrorMessage("Live tag is not displayed before stop recording");
				return false;
			}
			System.out.println("✅ Live tag is displayed before stop recording");

			// Click on stop recording button
			PWActions.click(stopRecordingButton, "Click on stop recording button");

			// After stop recording, check it changes into Not recording
			PWActions.waitFor(notRecordingTag, "Wait for Not recording tag to be visible", 30000);
			boolean isNotRecording = PWActions.isVisible(notRecordingTag,
					"Not recording tag is displayed after stop recording");

			if (!isNotRecording) {
				System.out.println("❌ Camera did not change to Not recording after stop recording");
				PWBaseTest.getFailureContext()
						.setErrorMessage("Camera did not change to Not recording after stop recording");
				return false;
			}
			System.out.println("✅ Camera changed to Not recording after stop recording");

			return true;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
	}

	// TC_37---------------------------------------------------------------------------------------------------------------------------------------

	public boolean Delete_TestCamera() {
		try {

			String deleteCameraButton = "//button[@aria-label='Delete camera']";
			String confirmDeleteButton = "//button[@class='flex cursor-pointer items-center gap-2 px-5 py-2 rounded-lg bg-red-500 text-white text-sm font-medium hover:bg-red-600 transition-all shadow-[0_0_20px_rgba(239,68,68,0.2)] disabled:opacity-40 disabled:cursor-not-allowed']";
			String successMessage = "//span[text()='Camera deleted successfully']";

			// Click on first delete camera button
			PWActions.waitFor(deleteCameraButton, "Wait for delete camera button to be visible", 30000);
			PWActions.click(deleteCameraButton, "Clicked on delete camera button");

			// Popup appears, wait for and click the confirm delete button
			PWActions.waitFor(confirmDeleteButton, "Wait for confirm delete button on popup", 30000);
			PWActions.click(confirmDeleteButton, "Clicked on confirm delete button");

			// Validate the success message
			PWActions.waitFor(successMessage, "Wait for camera deleted successfully message", 80000);
			boolean isDeleted = PWActions.isVisible(successMessage,
					"Camera deleted successfully message is displayed");

			if (!isDeleted) {
				System.out.println("❌ Camera deleted successfully message is not displayed");
				PWBaseTest.getFailureContext()
						.setErrorMessage("Camera deleted successfully message is not displayed");
				return false;
			}
			System.out.println("✅ Camera deleted successfully message is displayed");

			return true;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
	}

	// TC_38---------------------------------------------------------------------------------------------------------------------------------------

	// Helper: connect a camera with the given name
	private boolean ConnectCameraByName(String cameraName) {
		try {

			PWActions.click(
					"//button[@class='flex cursor-pointer items-center gap-2 bg-white text-black px-4 py-2 rounded-lg font-medium hover:bg-zinc-200 transition-colors shadow-depth-1']",
					"Clicked on Connect Camera button");

			PWActions.click("//input[@placeholder='Enter camera name']", "Clicked on Camera name field");
			PWActions.fill("//input[@placeholder='Enter camera name']", cameraName, "Enter camera name: " + cameraName);

			PWActions.click(
					"(//input[@class='w-full bg-zinc-900/60 border border-white/10 rounded-xl py-2.5 px-4 focus:outline-none focus:ring-1 focus:ring-white/20 transition-all placeholder:text-zinc-600 text-white text-sm'])[2]",
					"Clicked on RTSP Stream URL field");
			PWActions.fill(
					"(//input[@class='w-full bg-zinc-900/60 border border-white/10 rounded-xl py-2.5 px-4 focus:outline-none focus:ring-1 focus:ring-white/20 transition-all placeholder:text-zinc-600 text-white text-sm'])[2]",
					"rtsp://18.60.222.57:8554/webcam", "Enter RTSP Stream URL");

			PWActions.click(
					"//button[@class='flex items-center gap-2 px-5 py-2 rounded-xl bg-white text-black text-sm font-medium hover:bg-zinc-200 transition-all shadow-[0_0_20px_rgba(255,255,255,0.1)] disabled:opacity-50 disabled:cursor-not-allowed cursor-pointer']",
					"Clicked on Inner connect camera button");

			String cameraTile = "//h3[contains(@class,'font-medium') and normalize-space()='" + cameraName + "']";
			PWActions.waitFor(cameraTile, "Wait for camera '" + cameraName + "' to be displayed", 80000);
			boolean connected = PWActions.isVisible(cameraTile,
					"Camera '" + cameraName + "' is connected and displayed in the list");

			return connected;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public boolean Search_Camera() {
		try {

			String cameraA = "Test A Camera";
			String cameraB = "Test B Camera";
			String searchBox = "//input[@class='w-full bg-zinc-900 border border-white/5 rounded-xl py-2.5 pl-11 pr-4 focus:outline-none focus:ring-1 focus:ring-white/20 transition-all placeholder:text-zinc-600 text-white text-sm']";

			// Connect two cameras to compare
			if (!ConnectCameraByName(cameraA)) {
				System.out.println("❌ Failed to connect camera: " + cameraA);
				PWBaseTest.getFailureContext().setErrorMessage("Failed to connect camera: " + cameraA);
				return false;
			}
			System.out.println("✅ Connected camera: " + cameraA);

			if (!ConnectCameraByName(cameraB)) {
				System.out.println("❌ Failed to connect camera: " + cameraB);
				PWBaseTest.getFailureContext().setErrorMessage("Failed to connect camera: " + cameraB);
				return false;
			}
			System.out.println("✅ Connected camera: " + cameraB);

			// Search for the second camera by name
			PWActions.click(searchBox, "Clicked on camera search field");
			PWActions.fill(searchBox, cameraB, "Search camera by name: " + cameraB);

			// Verify the searched camera appears
			String cameraBTile = "//h3[contains(@class,'font-medium') and normalize-space()='" + cameraB + "']";
			PWActions.waitFor(cameraBTile, "Wait for searched camera to appear", 30000);
			boolean isSearchedCameraVisible = PWActions.isVisible(cameraBTile,
					"Searched camera '" + cameraB + "' is displayed after search");

			if (!isSearchedCameraVisible) {
				System.out.println("❌ Searched camera is not displayed: " + cameraB);
				PWBaseTest.getFailureContext().setErrorMessage("Searched camera is not displayed: " + cameraB);
				return false;
			}
			System.out.println("✅ Searched camera is displayed: " + cameraB);

			return true;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
	}

	// TC_39---------------------------------------------------------------------------------------------------------------------------------------

	public boolean Filter_Camera_By_Recording_Status() {
		try {

			String testACamera = "//h3[normalize-space()='Test A Camera']";
			String testACard = "//h3[normalize-space()='Test A Camera']/ancestor::div[contains(@class,'rounded-2xl')][1]";
			String startRecordingBtnA = testACard + "//button[contains(.,'Start Recording')]";
			String liveTagA = testACard + "//span[contains(@class,'bg-green-500/10') and contains(.,'Live')]";
			String liveTag = "//span[contains(@class,'bg-green-500/10') and contains(.,'Live')]";
			String notRecordingTag = "//span[@class='text-[10px] font-bold px-2 py-1 rounded-md border backdrop-blur-md flex items-center gap-1 bg-zinc-700/30 text-zinc-300 border-white/10']";
			String recordingFilter = "(//button[@class='px-4 py-1.5 rounded-full text-xs font-medium transition-all cursor-pointer bg-zinc-800/50 text-zinc-400 hover:text-white border border-white/5 hover:border-white/10'])[1]";
			String notRecordingFilter = "(//button[@class='px-4 py-1.5 rounded-full text-xs font-medium transition-all cursor-pointer bg-zinc-800/50 text-zinc-400 hover:text-white border border-white/5 hover:border-white/10'])[2]";

			// Click on Test A Camera, start its recording, then wait till the Live tag appears
			PWActions.waitFor(testACamera, "Wait for Test A Camera to be visible", 30000);
			PWActions.click(testACamera, "Clicked on Test A Camera");

			// Start recording on Test A Camera if it is not already recording
			if (page.locator(liveTagA).count() == 0) {
				PWActions.waitFor(startRecordingBtnA, "Wait for Test A Camera start recording button", 30000);
				PWActions.click(startRecordingBtnA, "Clicked on Test A Camera start recording button");
			}

			PWActions.waitFor(liveTagA, "Wait for Live tag on Test A Camera", 80000);
			System.out.println("✅ Live tag is displayed for Test A Camera");

			// Click on the Recording filter - only cameras with Live tag should appear
			PWActions.click(recordingFilter, "Clicked on Recording filter");
			Thread.sleep(2000);

			int liveCount = page.locator(liveTag).count();
			int notRecordingCountAfterRec = page.locator(notRecordingTag).count();

			System.out.println("Recording filter -> Live cameras: " + liveCount
					+ ", Not recording cameras: " + notRecordingCountAfterRec);

			if (liveCount < 1 || notRecordingCountAfterRec != 0) {
				System.out.println("❌ Recording filter shows non-live cameras");
				PWBaseTest.getFailureContext()
						.setErrorMessage("Recording filter did not show only Live cameras");
				return false;
			}
			System.out.println("✅ Recording filter shows only Live (recording) cameras");

			// Click on the Not recording filter - only cameras with Not recording tag should appear
			PWActions.click(notRecordingFilter, "Clicked on Not recording filter");
			Thread.sleep(2000);

			int notRecordingCount = page.locator(notRecordingTag).count();
			int liveCountAfterNotRec = page.locator(liveTag).count();

			System.out.println("Not recording filter -> Not recording cameras: " + notRecordingCount
					+ ", Live cameras: " + liveCountAfterNotRec);

			if (notRecordingCount < 1 || liveCountAfterNotRec != 0) {
				System.out.println("❌ Not recording filter shows recording cameras");
				PWBaseTest.getFailureContext()
						.setErrorMessage("Not recording filter did not show only Not recording cameras");
				return false;
			}
			System.out.println("✅ Not recording filter shows only Not recording cameras");

			return true;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
	}

	// TC_40 Create a camera and validate its creation date
	// ====================================================================================================
	// Pass condition: after creating the camera, the date shown on the new camera
	// card is TODAY's date (e.g. "16 Jun 2026, 01:08 pm"). Time is NOT checked.
	public boolean Create_Camera_And_Validate_CreationDate() {
		try {

			// Unique name so this test can be re-run without hitting the
			// "A camera with this name already exists" validation.
			String cameraName = "DateCheckCamera " + System.currentTimeMillis();

			// ---- Create the camera ----
			PWActions.click(
					"//button[@class='flex cursor-pointer items-center gap-2 bg-white text-black px-4 py-2 rounded-lg font-medium hover:bg-zinc-200 transition-colors shadow-depth-1']",
					"Clicked on Connect Camera button");

			PWActions.click("//input[@placeholder='Enter camera name']", "Clicked on Camera name field");
			PWActions.fill("//input[@placeholder='Enter camera name']", cameraName, "Enter camera name: " + cameraName);

			PWActions.click(
					"(//input[@class='w-full bg-zinc-900/60 border border-white/10 rounded-xl py-2.5 px-4 focus:outline-none focus:ring-1 focus:ring-white/20 transition-all placeholder:text-zinc-600 text-white text-sm'])[2]",
					"Clicked on RTSP Stream URL field");
			PWActions.fill(
					"(//input[@class='w-full bg-zinc-900/60 border border-white/10 rounded-xl py-2.5 px-4 focus:outline-none focus:ring-1 focus:ring-white/20 transition-all placeholder:text-zinc-600 text-white text-sm'])[2]",
					"rtsp://18.60.222.57:8554/webcam", "Enter RTSP Stream URL");

			PWActions.click(
					"//button[@class='flex items-center gap-2 px-5 py-2 rounded-xl bg-white text-black text-sm font-medium hover:bg-zinc-200 transition-all shadow-[0_0_20px_rgba(255,255,255,0.1)] disabled:opacity-50 disabled:cursor-not-allowed cursor-pointer']",
					"Clicked on Inner connect camera button");

			// Wait for the newly created camera card to appear
			String cameraTile = "//h3[contains(@class,'font-medium') and normalize-space()='" + cameraName + "']";
			PWActions.waitFor(cameraTile, "Wait for new camera '" + cameraName + "' to be displayed", 80000);

			// ---- Read the creation date shown on the camera card ----
			// Grab the whole card text (it contains e.g. "16 Jun 2026, 01:08 pm").
			String cardXpath = cameraTile + "/ancestor::div[contains(@class,'rounded-2xl')][1]";
			String cardText = PWActions.getText(cardXpath, "Get camera card text for creation date");
			System.out.println("🗂 Camera card text: " + cardText);

			// ---- Validation: today's date must be present on the card ----
			LocalDate today = LocalDate.now();
			String todayFormatted = today.format(DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH));

			if (cardText != null && cardText.contains(todayFormatted)) {
				System.out.println("✅ Camera card shows today's date: " + todayFormatted);
				return true;
			}

			String msg = "❌ Camera card does not show today's date (" + todayFormatted + "). Card text: " + cardText;
			System.out.println(msg);
			PWBaseTest.getFailureContext().setErrorMessage(msg);
			return false;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// TC_41 Start recording on Test B Camera and validate Stop Recording button appears
	// ====================================================================================================
	// Pass condition: after clicking "Start Recording" on the Test B Camera card,
	// the button changes to "Stop Recording" (i.e. recording started successfully).
	public boolean Start_Recording_TestBCamera() {
		try {

			String cameraBTile = "//h3[contains(.,'Test B Camera')]";
			String startRecordingBtnB = "(//h3[contains(.,'Test B Camera')]/ancestor::div[contains(@class,'flex-col')]//button[contains(.,'Start Recording')])[2]";
			String stopRecordingBtnB = "(//h3[normalize-space()='Test B Camera']/ancestor::div[contains(@class,'flex-col')]//button[contains(.,'Stop Recording')])[2]";

			// Make sure the Test B Camera card is available
			PWActions.waitFor(cameraBTile, "Wait for Test B Camera to be visible", 30000);

			// Click on the Start Recording button (only if it is not already recording)
			if (page.locator(stopRecordingBtnB).count() == 0) {
				PWActions.waitFor(startRecordingBtnB, "Wait for Test B Camera start recording button", 30000);
				PWActions.click(startRecordingBtnB, "Clicked on Test B Camera start recording button");
			} else {
				System.out.println("ℹ Test B Camera is already recording");
			}

			// After clicking start recording, verify the Stop Recording button appears
			PWActions.waitFor(stopRecordingBtnB, "Wait for Stop Recording button on Test B Camera", 80000);
			boolean isStopRecordingVisible = PWActions.isVisible(stopRecordingBtnB,
					"Stop Recording button is displayed on Test B Camera after start recording");

			if (!isStopRecordingVisible) {
				System.out.println("❌ Stop Recording button is not displayed on Test B Camera after start recording");
				PWBaseTest.getFailureContext()
						.setErrorMessage("Stop Recording button is not displayed on Test B Camera after start recording");
				return false;
			}
			System.out.println("✅ Stop Recording button is displayed on Test B Camera after start recording");

			return true;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// TC_42 Validate Videos page counts match Analytics page counts
	// ====================================================================================================
	// On the Videos page the xpath //span[@class='text-2xl font-semibold text-white leading-none']
	// holds 3 counts in order: [1] Total Uploads, [2] Processed, [3] RAW.
	// Then on the Analytics page the "Total Videos" card shows:
	//   - Total Videos value, "<n> processed", "<n> pending".
	// Pass condition: Total Uploads == Total Videos, Processed == processed, RAW == pending.
	public boolean Verify_Video_And_Analytics_Counts() {
		try {

			// ---------- VIDEOS PAGE ----------
			String videoCountXpath = "//span[@class='text-2xl font-semibold text-white leading-none']";

			PWActions.waitFor("(" + videoCountXpath + ")[1]", "Wait for Videos page counts to be visible", 30000);
			Thread.sleep(2000);

			Locator videoCounts = page.locator(videoCountXpath);
			int found = videoCounts.count();
			if (found < 3) {
				String msg = "❌ Expected 3 count elements on Videos page but found " + found;
				System.out.println(msg);
				PWBaseTest.getFailureContext().setErrorMessage(msg);
				return false;
			}

			int videoTotalUploads = extractNumber(videoCounts.nth(0).innerText());
			int videoProcessed = extractNumber(videoCounts.nth(1).innerText());
			int videoRaw = extractNumber(videoCounts.nth(2).innerText());

			System.out.println("📹 Videos page -> Total Uploads: " + videoTotalUploads + ", Processed: " + videoProcessed
					+ ", RAW: " + videoRaw);

			// ---------- ANALYTICS PAGE ----------
			PWActions.navigate("https://dev.vision.mikshi.ai/app/analytics", "Navigate to Analytics page");

			String totalVideosCard = "//div[normalize-space()='Total Videos']/ancestor::div[contains(@class,'glass-card')][1]";
			String analyticsTotalXpath = "(" + totalVideosCard + "//span[contains(@class,'text-3xl')])[1]";
			String analyticsProcessedXpath = totalVideosCard + "//span[contains(@class,'text-emerald-400')]";
			String analyticsPendingXpath = totalVideosCard + "//span[contains(@class,'text-amber-400')]";

			PWActions.waitFor(analyticsTotalXpath, "Wait for Analytics Total Videos card to be visible", 30000);
			Thread.sleep(2000);

			int analyticsTotal = extractNumber(PWActions.getText(analyticsTotalXpath, "Get Analytics Total Videos"));
			int analyticsProcessed = extractNumber(
					PWActions.getText(analyticsProcessedXpath, "Get Analytics processed count"));
			int analyticsPending = extractNumber(
					PWActions.getText(analyticsPendingXpath, "Get Analytics pending count"));

			System.out.println("📊 Analytics page -> Total Videos: " + analyticsTotal + ", processed: "
					+ analyticsProcessed + ", pending: " + analyticsPending);

			// ---------- COMPARE ----------
			boolean totalMatch = videoTotalUploads == analyticsTotal;
			boolean processedMatch = videoProcessed == analyticsProcessed;
			boolean rawMatch = videoRaw == analyticsPending;

			System.out.println("🔍 Total Uploads(" + videoTotalUploads + ") vs Total Videos(" + analyticsTotal + ") -> "
					+ totalMatch);
			System.out.println("🔍 Processed(" + videoProcessed + ") vs processed(" + analyticsProcessed + ") -> "
					+ processedMatch);
			System.out.println("🔍 RAW(" + videoRaw + ") vs pending(" + analyticsPending + ") -> " + rawMatch);

			if (totalMatch && processedMatch && rawMatch) {
				System.out.println("✅ TEST PASSED - Videos page counts match Analytics page counts");
				return true;
			}

			String errorMsg = "❌ TEST FAILED - Counts mismatch. Videos[Total=" + videoTotalUploads + ", Processed="
					+ videoProcessed + ", RAW=" + videoRaw + "] vs Analytics[Total=" + analyticsTotal + ", processed="
					+ analyticsProcessed + ", pending=" + analyticsPending + "]";
			System.out.println(errorMsg);
			PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
			return false;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// TC_43 Validate Cameras page counts match Analytics page counts
	// ====================================================================================================
	// On the Cameras page the cards show Total Cameras, Recording and Idle, each as
	//   <span class="text-zinc-500 ...">label</span><span class="text-3xl font-bold text-white leading-tight">n</span>.
	// On the Analytics page the "Total Cameras" card shows the total value and a
	//   "<active> active • <inactive> inactive" line.
	// Pass condition: Total Cameras == Total Cameras, Recording == active, Idle == inactive.
	public boolean Verify_Camera_And_Analytics_Counts() {
		try {

			// ---------- CAMERAS PAGE ----------
			String totalCamerasXpath = "//span[normalize-space()='Total Cameras']/following-sibling::span[contains(@class,'text-3xl')]";
			String recordingXpath = "//span[normalize-space()='Recording']/following-sibling::span[contains(@class,'text-3xl')]";
			String idleXpath = "//span[normalize-space()='Idle']/following-sibling::span[contains(@class,'text-3xl')]";

			PWActions.refresh("Refresh Cameras page");
			PWActions.waitFor(totalCamerasXpath, "Wait for Cameras page counts to be visible", 30000);
			Thread.sleep(2000);

			int cameraTotal = extractNumber(PWActions.getText(totalCamerasXpath, "Get Cameras page Total Cameras"));
			int cameraRecording = extractNumber(PWActions.getText(recordingXpath, "Get Cameras page Recording count"));
			int cameraIdle = extractNumber(PWActions.getText(idleXpath, "Get Cameras page Idle count"));

			System.out.println("📷 Cameras page -> Total Cameras: " + cameraTotal + ", Recording: " + cameraRecording
					+ ", Idle: " + cameraIdle);

			// ---------- ANALYTICS PAGE ----------
			PWActions.navigate("https://dev.vision.mikshi.ai/app/analytics", "Navigate to Analytics page");

			String totalCamerasCard = "//div[normalize-space()='Total Cameras']/ancestor::div[contains(@class,'glass-card')][1]";
			String analyticsTotalXpath = "(" + totalCamerasCard + "//span[contains(@class,'text-3xl')])[1]";
			String analyticsActiveInactiveXpath = totalCamerasCard + "//span[contains(@class,'text-zinc-400')]";

			PWActions.waitFor(analyticsTotalXpath, "Wait for Analytics Total Cameras card to be visible", 30000);
			Thread.sleep(2000);

			int analyticsTotal = extractNumber(PWActions.getText(analyticsTotalXpath, "Get Analytics Total Cameras"));

			// The active/inactive line looks like "2 active • 1 inactive" -> first number is
			// active (recording), second number is inactive (idle).
			String activeInactiveText = PWActions.getText(analyticsActiveInactiveXpath,
					"Get Analytics active/inactive line");
			java.util.regex.Matcher m = java.util.regex.Pattern.compile("(\\d+)").matcher(activeInactiveText);
			int analyticsActive = m.find() ? Integer.parseInt(m.group(1)) : -1;
			int analyticsInactive = m.find() ? Integer.parseInt(m.group(1)) : -1;

			System.out.println("📊 Analytics page -> Total Cameras: " + analyticsTotal + ", active: " + analyticsActive
					+ ", inactive: " + analyticsInactive + " (raw: '" + activeInactiveText + "')");

			// ---------- COMPARE ----------
			boolean totalMatch = cameraTotal == analyticsTotal;
			boolean recordingMatch = cameraRecording == analyticsActive;
			boolean idleMatch = cameraIdle == analyticsInactive;

			System.out.println("🔍 Total Cameras(" + cameraTotal + ") vs Total Cameras(" + analyticsTotal + ") -> "
					+ totalMatch);
			System.out.println("🔍 Recording(" + cameraRecording + ") vs active(" + analyticsActive + ") -> "
					+ recordingMatch);
			System.out.println("🔍 Idle(" + cameraIdle + ") vs inactive(" + analyticsInactive + ") -> " + idleMatch);

			if (totalMatch && recordingMatch && idleMatch) {
				System.out.println("✅ TEST PASSED - Cameras page counts match Analytics page counts");
				return true;
			}

			String errorMsg = "❌ TEST FAILED - Counts mismatch. Cameras[Total=" + cameraTotal + ", Recording="
					+ cameraRecording + ", Idle=" + cameraIdle + "] vs Analytics[Total=" + analyticsTotal + ", active="
					+ analyticsActive + ", inactive=" + analyticsInactive + "]";
			System.out.println(errorMsg);
			PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
			return false;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// TC_44 Validate used storage increases after uploading a video
	// ====================================================================================================
	// Used storage is shown on the Analytics page as a span like "3.4 GB" / "850 MB".
	// GETINITIALUSEDSTOREAGE reads it BEFORE the upload and stores it in initialUsedStorage.
	// validatecountincresed reads it AFTER the upload and passes only if it increased; if the value
	//   has not changed yet it refreshes the page and re-checks a few times (the backend may take a
	//   moment to recalculate used storage after the video is processed).
	private static final String ANALYTICS_URL = "https://dev.vision.mikshi.ai/app/analytics";
	private static final String VIDEOS_URL = "https://dev.vision.mikshi.ai/app/videos";
	// First span on the Analytics page whose text carries a storage unit. Adjust if the UI changes.
	private static final String USED_STORAGE_XPATH = "(//span[contains(.,'GB') or contains(.,'MB') or contains(.,'TB') or contains(.,'KB')])[1]";

	public boolean GETINITIALUSEDSTOREAGE() {
		try {
			// Read used storage from the Analytics page before uploading.
			PWActions.navigate(ANALYTICS_URL, "Navigate to Analytics page to read initial used storage");
			PWActions.waitFor(USED_STORAGE_XPATH, "Wait for used storage element to be visible", 30000);
			Thread.sleep(2000);

			String initialText = PWActions.getText(USED_STORAGE_XPATH, "Get initial used storage").trim();
			initialUsedStorage = parseStorageToMB(initialText);
			System.out.println("💾 Initial used storage: " + initialUsedStorage + " MB (raw: '" + initialText + "')");

			// Go back to the Videos page so the upload flow can run.
			PWActions.navigate(VIDEOS_URL, "Navigate back to Videos page for upload");
			return true;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public boolean CompleteUploadVideoforstoregused() {
		try {

			PWActions.click("//button[@class='px-5 py-2 rounded-xl bg-white text-black text-sm font-medium hover:bg-zinc-200 transition-all shadow-[0_0_20px_rgba(255,255,255,0.1)] disabled:opacity-40 disabled:cursor-not-allowed disabled:hover:bg-white cursor-pointer flex items-center gap-2']", "Clicked on  Upload");
			PWActions.waitFor(
					" //div[h3[normalize-space()='All_queries_03'] and .//span[contains(normalize-space(),'Just now')]]",
					"wait for Upload video", 160000);
			boolean status = PWActions.isVisible(
					"//div[h3[normalize-space()='All_queries_03'] and .//span[contains(normalize-space(),'Just now')]]",
					"Video Uploaded successfully");

			return true;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
	}








	public boolean validatecountincresed() {
		try {
			// Read used storage from the Analytics page after uploading.
			PWActions.navigate(ANALYTICS_URL, "Navigate to Analytics page to read final used storage");
			PWActions.waitFor(USED_STORAGE_XPATH, "Wait for used storage element to be visible", 30000);

			int maxAttempts = 5;
			for (int attempt = 1; attempt <= maxAttempts; attempt++) {
				Thread.sleep(2000);

				String finalText = PWActions.getText(USED_STORAGE_XPATH, "Get final used storage").trim();
				finalUsedStorage = parseStorageToMB(finalText);
				System.out.println("💾 Final used storage (attempt " + attempt + "/" + maxAttempts + "): "
						+ finalUsedStorage + " MB (raw: '" + finalText + "')");

				if (finalUsedStorage > initialUsedStorage) {
					System.out.println("✅ TEST PASSED - Used storage increased from " + initialUsedStorage + " MB to "
							+ finalUsedStorage + " MB after upload");
					return true;
				}

				// Count/storage has not changed yet -> refresh and check again.
				System.out.println("⏳ Used storage unchanged (" + finalUsedStorage + " MB). Refreshing and re-checking...");
				PWActions.refresh("Refresh Analytics page to re-read used storage");
				PWActions.waitFor(USED_STORAGE_XPATH, "Wait for used storage element after refresh", 30000);
			}

			String errorMsg = "❌ TEST FAILED - Used storage did not increase after upload. Initial=" + initialUsedStorage
					+ " MB, Final=" + finalUsedStorage + " MB";
			System.out.println(errorMsg);
			PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
			return false;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// TC_45 Validate search API count increases after running a "Detect Violence" search
	// ====================================================================================================
	// Flow: capture the current search count on Analytics, go to Conversations, pick the first video,
	//   click Done, choose "Detect Violence", send the request, then go back to Analytics and confirm
	//   the count went up (refresh + re-check a few times since the backend updates asynchronously).
	private static final String CONVERSATIONS_URL = "https://dev.vision.mikshi.ai/app/conversations";
	// Exact search-count locator on the Analytics page (provided by the team).
	private static final String SEARCH_API_COUNT_XPATH = "(//div[@class='text-3xl font-semibold text-white leading-none tracking-tight'])[1]";
	// Conversations page elements.
	private static final String SELECT_VIDEO_BUTTON = "//button[@class='w-full min-h-[280px] rounded-2xl border border-dashed border-white/20 bg-white/[0.03] hover:border-white/30 hover:bg-white/[0.05] transition-all duration-200 flex flex-col items-center justify-center text-center p-6 cursor-pointer']";
	private static final String SEND_BUTTON = "//button[@class='w-9 h-9 flex items-center justify-center rounded-xl bg-white/10 text-white hover:bg-white/20 transition-colors cursor-pointer disabled:opacity-40 disabled:cursor-not-allowed']";

	public boolean GetInitialSearchCount() {
		try {
			PWActions.navigate(ANALYTICS_URL, "Navigate to Analytics page to read initial search count");
			PWActions.waitFor(SEARCH_API_COUNT_XPATH, "Wait for search count element to be visible", 30000);
			Thread.sleep(2000);

			String initialText = PWActions.getText(SEARCH_API_COUNT_XPATH, "Get initial search count").trim();
			initialSearchCount = extractNumber(initialText);
			System.out.println("🔎 Initial search count: " + initialSearchCount + " (raw: '" + initialText + "')");

			// Move to the Conversations page so the detect-violence flow can run.
			PWActions.navigate(CONVERSATIONS_URL, "Navigate to Conversations page");
			return true;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public boolean RunDetectViolenceSearch() {
		try {
			// Open the video picker.
			PWActions.click(SELECT_VIDEO_BUTTON, "Clicked on select video");

			// Select the first video thumbnail from the list.
			String firstVideoInPicker = "(//img[@class='w-full h-full object-cover'])[1]";
			PWActions.waitFor(firstVideoInPicker, "Wait for video list to load", 30000);
			PWActions.click(firstVideoInPicker, "Selected first video from list");

			// Confirm the selection (Done).
			PWActions.click(
					"//button[@class='flex-1 h-12 rounded-xl bg-white text-black font-medium hover:bg-zinc-200 transition-colors disabled:opacity-50 disabled:cursor-not-allowed cursor-pointer']",
					"Clicked on Done");

			// Choose the "Detect Violence" option.
			PWActions.click(
					"(//button[@class='text-[11px] font-bold tracking-wider uppercase text-zinc-200 px-4 py-2 rounded-xl bg-zinc-900/50 border border-white/10 hover:bg-zinc-900 hover:border-white/25 hover:text-white transition-all duration-200 cursor-pointer rim-light'])[1]",
					"Clicked on Detect Violence");

			// Send the request.
			PWActions.click(SEND_BUTTON, "Clicked on Send");

			// Give the search request time to be submitted/processed before we navigate away,
			// otherwise the backend may not have incremented the search count yet.
			Thread.sleep(5000);
			return true;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public boolean ValidateSearchCountIncreased() {
		try {
			PWActions.navigate(ANALYTICS_URL, "Navigate back to Analytics page to read final search count");
			PWActions.waitFor(SEARCH_API_COUNT_XPATH, "Wait for search count element to be visible", 30000);

			int maxAttempts = 5;
			for (int attempt = 1; attempt <= maxAttempts; attempt++) {
				// Wait for the count to settle before reading.
				Thread.sleep(4000);

				// Refresh and wait again so we read the freshly-recalculated count.
				PWActions.refresh("Refresh Analytics page to re-read search count");
				PWActions.waitFor(SEARCH_API_COUNT_XPATH, "Wait for search count element after refresh", 30000);
				Thread.sleep(4000);

				String finalText = PWActions.getText(SEARCH_API_COUNT_XPATH, "Get final search count").trim();
				finalSearchCount = extractNumber(finalText);
				System.out.println("🔎 Final search count (attempt " + attempt + "/" + maxAttempts + "): "
						+ finalSearchCount + " (raw: '" + finalText + "')");

				if (finalSearchCount > initialSearchCount) {
					System.out.println("✅ TEST PASSED - Search count increased from " + initialSearchCount + " to "
							+ finalSearchCount + " after Detect Violence search");
					return true;
				}

				System.out.println("⏳ Search count unchanged (" + finalSearchCount + "). Re-checking...");
			}

			String errorMsg = "❌ TEST FAILED - Search count did not increase after Detect Violence search. Initial="
					+ initialSearchCount + ", Final=" + finalSearchCount;
			System.out.println(errorMsg);
			PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
			return false;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// TC_46 Validate analyse count increases after running an analyse on a video
	// ====================================================================================================
	// Same shape as TC_45, but tracks the 2nd count on the Analytics page (the "Analyse" count) and
	//   uses the analyse tab/option on the Conversations page.
	private static final String ANALYSE_COUNT_XPATH = "(//div[@class='text-3xl font-semibold text-white leading-none tracking-tight'])[2]";
	// Conversations page: the mode tab that switches into Analyze mode. Matched by its visible label so
	// it works in both states - the 'bg-white ... shadow-depth-1' class is the ACTIVE-state styling
	// (Search is active by default), so a class-based locator would click the wrong tab.
	private static final String ANALYSE_TAB = "//button[contains(@class,'flex-1') and contains(normalize-space(.),'Analyze')]";
	// First analyse option chip (same chip family as Detect Violence).
	private static final String ANALYSE_OPTION = "(//button[@class='text-[11px] font-bold tracking-wider uppercase text-zinc-200 px-4 py-2 rounded-xl bg-zinc-900/50 border border-white/10 hover:bg-zinc-900 hover:border-white/25 hover:text-white transition-all duration-200 cursor-pointer rim-light'])[1]";
	// Submit/send button shown after picking an analyse option.
	private static final String ANALYSE_SUBMIT_CARD = "//button[@class='w-9 h-9 flex items-center justify-center rounded-xl bg-white/10 text-white hover:bg-white/20 transition-colors cursor-pointer disabled:opacity-40 disabled:cursor-not-allowed']";

	public boolean GetInitialAnalyseCount() {
		try {
			PWActions.navigate(ANALYTICS_URL, "Navigate to Analytics page to read initial analyse count");
			PWActions.waitFor(ANALYSE_COUNT_XPATH, "Wait for analyse count element to be visible", 30000);
			Thread.sleep(2000);

			String initialText = PWActions.getText(ANALYSE_COUNT_XPATH, "Get initial analyse count").trim();
			initialAnalyseCount = extractNumber(initialText);
			System.out.println("📈 Initial analyse count: " + initialAnalyseCount + " (raw: '" + initialText + "')");

			// Move to the Conversations page so the analyse flow can run.
			PWActions.navigate(CONVERSATIONS_URL, "Navigate to Conversations page");
			return true;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public boolean RunAnalyseVideo() {
		try {
			// Switch to the analyse tab.
			PWActions.click(ANALYSE_TAB, "Clicked on analyse tab");

			// Open the video picker.
			PWActions.click(SELECT_VIDEO_BUTTON, "Clicked on select video");

			// Select the first video thumbnail from the list.
			String firstVideoInPicker = "(//img[@class='w-full h-full object-cover'])[1]";
			PWActions.waitFor(firstVideoInPicker, "Wait for video list to load", 30000);
			PWActions.click(firstVideoInPicker, "Selected first video from list");

			// Confirm the selection (Done).
			PWActions.click(
					"//button[@class='flex-1 h-12 rounded-xl bg-white text-black font-medium hover:bg-zinc-200 transition-colors disabled:opacity-50 disabled:cursor-not-allowed cursor-pointer']",
					"Clicked on Done");

			// Choose the first analyse option.
			PWActions.click(ANALYSE_OPTION, "Clicked on analyse option");

			// Click the glass-card submit container to trigger the analyse.
			PWActions.click(ANALYSE_SUBMIT_CARD, "Clicked on analyse submit card");

			// Give the analyse request time to be submitted/processed before we navigate away.
			Thread.sleep(4000);
			return true;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public boolean ValidateAnalyseCountIncreased() {
		try {
			PWActions.navigate(ANALYTICS_URL, "Navigate back to Analytics page to read final analyse count");
			PWActions.waitFor(ANALYSE_COUNT_XPATH, "Wait for analyse count element to be visible", 30000);

			int maxAttempts = 5;
			for (int attempt = 1; attempt <= maxAttempts; attempt++) {
				// Refresh before checking, then wait for the count to settle.
				PWActions.refresh("Refresh Analytics page to re-read analyse count");
				PWActions.waitFor(ANALYSE_COUNT_XPATH, "Wait for analyse count element after refresh", 30000);
				Thread.sleep(4000);

				String finalText = PWActions.getText(ANALYSE_COUNT_XPATH, "Get final analyse count").trim();
				finalAnalyseCount = extractNumber(finalText);
				System.out.println("📈 Final analyse count (attempt " + attempt + "/" + maxAttempts + "): "
						+ finalAnalyseCount + " (raw: '" + finalText + "')");

				if (finalAnalyseCount > initialAnalyseCount) {
					System.out.println("✅ TEST PASSED - Analyse count increased from " + initialAnalyseCount + " to "
							+ finalAnalyseCount + " after analyse");
					return true;
				}

				System.out.println("⏳ Analyse count unchanged (" + finalAnalyseCount + "). Re-checking...");
			}

			String errorMsg = "❌ TEST FAILED - Analyse count did not increase after analyse. Initial="
					+ initialAnalyseCount + ", Final=" + finalAnalyseCount;
			System.out.println(errorMsg);
			PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
			return false;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// TC_47 Validate current balance is reduced by the credits used after a Detect Violence search
	// ====================================================================================================
	// Flow (single "smart" method):
	//   1. Analytics -> read the current balance/credit  -> store as initialCurrentBalanceCredit.
	//   2. Conversations -> run the same flow as RunDetectViolenceSearch().
	//   3. Analytics -> read the recently-used count (rose-400) -> store as recentCount.
	//   4. Compute newCurrentBalance = initialCurrentBalanceCredit - recentCount.
	//   5. Re-read the current balance on Analytics and assert it equals newCurrentBalance.
	// Current balance / credit locator on the Analytics page.
	private static final String CURRENT_BALANCE_XPATH = "(//div[@class='mt-2 text-2xl font-semibold text-white leading-none'])[1]";
	// Recently-used count locator on the Analytics page.
	private static final String RECENT_COUNT_XPATH = "(//div[@class='text-sm font-semibold text-rose-400'])[1]";

	public boolean ValidateCurrentBalanceAfterSearch() {
		try {
			// 1) Analytics -> capture the current balance/credit before the search.
			PWActions.navigate(ANALYTICS_URL, "Navigate to Analytics page to read initial current balance");
			PWActions.waitFor(CURRENT_BALANCE_XPATH, "Wait for current balance element to be visible", 30000);
			Thread.sleep(2000);

			String initialBalanceText = PWActions.getText(CURRENT_BALANCE_XPATH, "Get initial current balance").trim();
			initialCurrentBalanceCredit = extractDecimal(initialBalanceText);
			System.out.println("💰 Initial current balance: " + initialCurrentBalanceCredit + " (raw: '"
					+ initialBalanceText + "')");

			// 2) Conversations -> run the Detect Violence search flow.
			PWActions.navigate(CONVERSATIONS_URL, "Navigate to Conversations page to run Detect Violence search");
			if (!RunDetectViolenceSearch()) {
				return false;
			}

			// 3) Back to Analytics -> read the recently-used count.
			PWActions.navigate(ANALYTICS_URL, "Navigate back to Analytics page to read recent count and balance");
			PWActions.waitFor(CURRENT_BALANCE_XPATH, "Wait for current balance element to be visible", 30000);
			// Refresh so we read the freshly-recalculated values, then let them settle.
			PWActions.refresh("Refresh Analytics page to re-read balance and recent count");
			PWActions.waitFor(CURRENT_BALANCE_XPATH, "Wait for current balance element after refresh", 30000);
			Thread.sleep(4000);

			String recentText = PWActions.getText(RECENT_COUNT_XPATH, "Get recent count").trim();
			recentCount = extractDecimal(recentText);
			System.out.println("🧮 Recent count (credits used): " + recentCount + " (raw: '" + recentText + "')");

			// 4) Expected balance after the search = initial balance - recent count.
			newCurrentBalance = initialCurrentBalanceCredit - recentCount;
			System.out.println("➖ Expected new balance = " + initialCurrentBalanceCredit + " - " + recentCount + " = "
					+ newCurrentBalance);

			// 5) Re-read the actual current balance and compare with the expected value.
			String actualBalanceText = PWActions.getText(CURRENT_BALANCE_XPATH, "Get current balance after search")
					.trim();
			double actualBalance = extractDecimal(actualBalanceText);
			System.out.println("💰 Actual current balance after search: " + actualBalance + " (raw: '"
					+ actualBalanceText + "')");

			// Compare with a small tolerance to absorb floating-point rounding.
			if (Math.abs(actualBalance - newCurrentBalance) < 0.001) {
				System.out.println("✅ TEST PASSED - Current balance matches. Expected=" + newCurrentBalance
						+ ", Actual=" + actualBalance);
				return true;
			}

			String errorMsg = "❌ TEST FAILED - Current balance mismatch. Initial=" + initialCurrentBalanceCredit
					+ ", RecentCount=" + recentCount + ", Expected=" + newCurrentBalance + ", Actual=" + actualBalance;
			System.out.println(errorMsg);
			PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
			return false;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// TC_48 Validate consumed count increases by the recently-used count after a Detect Violence search
	// ====================================================================================================
	// Same shape as TC_47 but the consumed-count grows: actual should equal initial + recentCount.
	//   1. Analytics -> read the consumed count -> store as initialConsumedCount.
	//   2. Conversations -> run the same flow as RunDetectViolenceSearch().
	//   3. Analytics -> read the recently-used count (rose-400) -> store as recentCount.
	//   4. Compute newConsumedCount = initialConsumedCount + recentCount.
	//   5. Re-read the consumed count on Analytics and assert it equals newConsumedCount.
	// Consumed count locator on the Analytics page (3rd element of the same family as the balance).
	private static final String CONSUMED_COUNT_XPATH = "(//div[@class='mt-2 text-2xl font-semibold text-white leading-none'])[3]";

	public boolean ValidateConsumedCountAfterSearch() {
		try {
			// 1) Analytics -> capture the consumed count before the search.
			PWActions.navigate(ANALYTICS_URL, "Navigate to Analytics page to read initial consumed count");
			PWActions.waitFor(CONSUMED_COUNT_XPATH, "Wait for consumed count element to be visible", 30000);
			Thread.sleep(2000);

			String initialConsumedText = PWActions.getText(CONSUMED_COUNT_XPATH, "Get initial consumed count").trim();
			initialConsumedCount = extractDecimal(initialConsumedText);
			System.out.println("📊 Initial consumed count: " + initialConsumedCount + " (raw: '" + initialConsumedText
					+ "')");

			// 2) Conversations -> run the Detect Violence search flow.
			PWActions.navigate(CONVERSATIONS_URL, "Navigate to Conversations page to run Detect Violence search");
			if (!RunDetectViolenceSearch()) {
				return false;
			}

			// 3) Back to Analytics -> read the recently-used count.
			PWActions.navigate(ANALYTICS_URL, "Navigate back to Analytics page to read recent and consumed count");
			PWActions.waitFor(CONSUMED_COUNT_XPATH, "Wait for consumed count element to be visible", 30000);
			// Refresh so we read the freshly-recalculated values, then let them settle.
			PWActions.refresh("Refresh Analytics page to re-read consumed and recent count");
			PWActions.waitFor(CONSUMED_COUNT_XPATH, "Wait for consumed count element after refresh", 30000);
			Thread.sleep(4000);

			String recentText = PWActions.getText(RECENT_COUNT_XPATH, "Get recent count").trim();
			recentCount = extractDecimal(recentText);
			System.out.println("🧮 Recent count (added): " + recentCount + " (raw: '" + recentText + "')");

			// 4) Expected consumed count after the search = initial consumed count + recent count.
			newConsumedCount = initialConsumedCount + recentCount;
			System.out.println("➕ Expected new consumed count = " + initialConsumedCount + " + " + recentCount + " = "
					+ newConsumedCount);

			// 5) Re-read the actual consumed count and compare with the expected value.
			String actualConsumedText = PWActions.getText(CONSUMED_COUNT_XPATH, "Get consumed count after search")
					.trim();
			double actualConsumed = extractDecimal(actualConsumedText);
			System.out.println("📊 Actual consumed count after search: " + actualConsumed + " (raw: '"
					+ actualConsumedText + "')");

			// Compare with a small tolerance to absorb floating-point rounding.
			if (Math.abs(actualConsumed - newConsumedCount) < 0.001) {
				System.out.println("✅ TEST PASSED - Consumed count matches. Expected=" + newConsumedCount + ", Actual="
						+ actualConsumed);
				return true;
			}

			String errorMsg = "❌ TEST FAILED - Consumed count mismatch. Initial=" + initialConsumedCount
					+ ", RecentCount=" + recentCount + ", Expected=" + newConsumedCount + ", Actual=" + actualConsumed;
			System.out.println(errorMsg);
			PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
			return false;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// TC_49 Create a team on the Settings page and verify it appears in the team list
	// ====================================================================================================
	// The test framework auto-navigates to the Settings page (navPath = "settings"), so this method
	//   starts from there: open the create-team form, enter the team name, submit, then verify the new
	//   team is visible in the list.
	private static final String CREATE_TEAM_BUTTON = "//button[@class='flex items-center gap-2 px-4 py-2 rounded-xl bg-zinc-900 text-white text-sm font-medium hover:bg-zinc-800 transition-all border border-white/10 cursor-pointer']";
	private static final String TEAM_NAME_INPUT = "//input[@class='flex-1 bg-zinc-900/60 border border-white/10 rounded-xl py-3 px-4 focus:outline-none focus:ring-1 focus:ring-white/20 transition-all placeholder:text-zinc-600 text-white text-sm']";
	private static final String CREATE_TEAM_SUBMIT = "//button[@class='px-6 rounded-xl bg-zinc-800 text-white text-sm font-medium hover:bg-zinc-700 transition-all border border-white/10 disabled:opacity-50 disabled:cursor-not-allowed cursor-pointer']";
	// Created-team card locator (verifies the team name and "1 member" are shown together).
	private static final String CREATED_TEAM_CARD = "(//div[.//*[contains(normalize-space(),'Automation Team')] and .//*[contains(normalize-space(),'1 member')]])[8]";

	public boolean CreateTeamAndVerify() {
		try {
			String teamName = "Automation Team";

			// 1) Open the create-team form/dialog.
			PWActions.waitFor(CREATE_TEAM_BUTTON, "Wait for Create Team button", 30000);
			PWActions.click(CREATE_TEAM_BUTTON, "Clicked on Create Team");

			// 2) Enter the team name.
			PWActions.waitFor(TEAM_NAME_INPUT, "Wait for team name input", 30000);
			PWActions.fill(TEAM_NAME_INPUT, teamName, "Entered team name '" + teamName + "'");

			// 3) Submit to create the team.
			PWActions.click(CREATE_TEAM_SUBMIT, "Clicked on Create/Submit team");

			// Give the backend a moment to create the team and the list to refresh.
			Thread.sleep(4000);

			// 4) Verify the newly-created team appears in the list.
			PWActions.waitFor(CREATED_TEAM_CARD, "Wait for created team to appear", 30000);

			if (PWActions.isVisible(CREATED_TEAM_CARD, "Check created team is visible")) {
				System.out.println("✅ TEST PASSED - Team '" + teamName + "' created and appears in the list");
				return true;
			}

			String errorMsg = "❌ TEST FAILED - Team '" + teamName + "' did not appear after creation";
			System.out.println(errorMsg);
			PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
			return false;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// TC_50 Validate the handover warning shows when the sole Admin tries to leave the team
	// ====================================================================================================
	// Open the "Automation Team" card, click "Leave team", and confirm the warning message appears:
	//   "Please handover Admin access to other before you exit".
	private static final String LEAVE_TEAM_BUTTON = "//button[@class='flex items-center gap-2 px-4 py-2 rounded-xl bg-zinc-900 text-white text-sm font-medium hover:bg-zinc-800 transition-all border border-white/10 cursor-pointer disabled:opacity-50 disabled:cursor-not-allowed']";
	private static final String HANDOVER_WARNING_MESSAGE = "//span[text()='Please handover Admin access to other before you exit']";

	public boolean ValidateLeaveTeamHandoverWarning() {
		try {
			// 1) Open the "Automation Team" card.
			PWActions.waitFor(CREATED_TEAM_CARD, "Wait for Automation Team card", 30000);
			PWActions.click(CREATED_TEAM_CARD, "Clicked on Automation Team");

			// 2) Try to leave the team.
			PWActions.waitFor(LEAVE_TEAM_BUTTON, "Wait for Leave team button", 30000);
			PWActions.click(LEAVE_TEAM_BUTTON, "Clicked on Leave team");

			// 3) Verify the handover warning message appears.
			PWActions.waitFor(HANDOVER_WARNING_MESSAGE, "Wait for handover warning message", 30000);

			if (PWActions.isVisible(HANDOVER_WARNING_MESSAGE, "Check handover warning message is visible")) {
				System.out.println("✅ TEST PASSED - Handover warning displayed when Admin tried to leave the team");
				return true;
			}

			String errorMsg = "❌ TEST FAILED - Handover warning message did not appear when Admin tried to leave the team";
			System.out.println(errorMsg);
			PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
			return false;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// TC_51 Validate the error message when inviting a user who has not signed in yet
	// ====================================================================================================
	// Open the "Automation Team" card, click Add user, enter an email that hasn't signed in, send the
	//   invite, then confirm the error message appears.
	private static final String ADD_USER_BUTTON = "(//button[@class='flex items-center gap-2 px-4 py-2 rounded-xl bg-zinc-900 text-white text-sm font-medium hover:bg-zinc-800 transition-all border border-white/10 cursor-pointer'])[2]";
	private static final String INVITE_EMAIL_INPUT = "//input[@placeholder='user@example.com']";
	private static final String SEND_INVITE_BUTTON = "//button[@class='flex-1 px-5 py-2.5 rounded-xl bg-zinc-700/60 text-white text-sm font-medium hover:bg-zinc-700 transition-all border border-white/10 disabled:opacity-50 disabled:cursor-not-allowed cursor-pointer']";
	private static final String INVITE_NOT_SIGNED_IN_MESSAGE = "//div[normalize-space()=\"This person hasn't signed in to the app yet. Ask them to sign in once, then send the invite again.\"]";

	public boolean ValidateInviteNotSignedInError() {
		try {
			String inviteEmail = "pranav@yopmail.com";

			// 1) Open the "Automation Team" card.
			PWActions.waitFor(CREATED_TEAM_CARD, "Wait for Automation Team card", 30000);
			PWActions.click(CREATED_TEAM_CARD, "Clicked on Automation Team");

			// 2) Open the Add user form.
			PWActions.waitFor(ADD_USER_BUTTON, "Wait for Add user button", 30000);
			PWActions.click(ADD_USER_BUTTON, "Clicked on Add user");

			// 3) Enter the email of a user who hasn't signed in yet.
			PWActions.waitFor(INVITE_EMAIL_INPUT, "Wait for invite email input", 30000);
			PWActions.fill(INVITE_EMAIL_INPUT, inviteEmail, "Entered invite email '" + inviteEmail + "'");

			// 4) Send the invite.
			PWActions.click(SEND_INVITE_BUTTON, "Clicked on Send invite");

			// 5) Verify the "not signed in" error message appears.
			PWActions.waitFor(INVITE_NOT_SIGNED_IN_MESSAGE, "Wait for not-signed-in error message", 30000);

			if (PWActions.isVisible(INVITE_NOT_SIGNED_IN_MESSAGE, "Check not-signed-in error message is visible")) {
				System.out.println("✅ TEST PASSED - 'Not signed in' error message displayed for invite to "
						+ inviteEmail);
				return true;
			}

			String errorMsg = "❌ TEST FAILED - 'Not signed in' error message did not appear for invite to "
					+ inviteEmail;
			System.out.println(errorMsg);
			PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
			return false;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// TC_52 Validate the error message when inviting with an invalid email format
	// ====================================================================================================
	// Same flow as TC_51 but enters an invalid email and expects the format-validation error.
	private static final String INVITE_INVALID_EMAIL_MESSAGE = "//p[text()='Please enter a valid email address (e.g. name@example.com).']";

	public boolean ValidateInviteInvalidEmailError() {
		try {
			String invalidEmail = "6647f3f8f";

			// 1) Open the "Automation Team" card.
			PWActions.waitFor(CREATED_TEAM_CARD, "Wait for Automation Team card", 30000);
			PWActions.click(CREATED_TEAM_CARD, "Clicked on Automation Team");

			// 2) Open the Add user form.
			PWActions.waitFor(ADD_USER_BUTTON, "Wait for Add user button", 30000);
			PWActions.click(ADD_USER_BUTTON, "Clicked on Add user");

			// 3) Enter an invalid email.
			PWActions.waitFor(INVITE_EMAIL_INPUT, "Wait for invite email input", 30000);
			PWActions.fill(INVITE_EMAIL_INPUT, invalidEmail, "Entered invalid email '" + invalidEmail + "'");

			// 4) The Send Invite button stays disabled for an invalid email, so we can't click it.
			//    Blur the field (Tab) to trigger the inline format validation instead.
			page.keyboard().press("Tab");

			// 5) Verify the invalid-email format error message appears.
			PWActions.waitFor(INVITE_INVALID_EMAIL_MESSAGE, "Wait for invalid-email error message", 30000);

			if (PWActions.isVisible(INVITE_INVALID_EMAIL_MESSAGE, "Check invalid-email error message is visible")) {
				System.out.println("✅ TEST PASSED - Invalid-email error message displayed for '" + invalidEmail + "'");
				return true;
			}

			String errorMsg = "❌ TEST FAILED - Invalid-email error message did not appear for '" + invalidEmail + "'";
			System.out.println(errorMsg);
			PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
			return false;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// TC_53 Validate the "Pending" status tag appears after sending an invite
	// ====================================================================================================
	// Same flow as TC_51 but with a valid email; after sending the invite, the invited user should show
	//   a yellow "Pending" status tag.
	private static final String PENDING_STATUS_TAG = "//span[@class='px-2.5 py-1 rounded-md border text-xs font-medium bg-yellow-500/10 border-yellow-500/30 text-yellow-300']";

	public boolean ValidateInvitePendingStatus() {
		try {
			String inviteEmail = "avision185@gmail.com";

			// 1) Open the "Automation Team" card.
			PWActions.waitFor(CREATED_TEAM_CARD, "Wait for Automation Team card", 30000);
			PWActions.click(CREATED_TEAM_CARD, "Clicked on Automation Team");

			// 2) Open the Add user form.
			PWActions.waitFor(ADD_USER_BUTTON, "Wait for Add user button", 30000);
			PWActions.click(ADD_USER_BUTTON, "Clicked on Add user");

			// 3) Enter a valid email.
			PWActions.waitFor(INVITE_EMAIL_INPUT, "Wait for invite email input", 30000);
			PWActions.fill(INVITE_EMAIL_INPUT, inviteEmail, "Entered invite email '" + inviteEmail + "'");

			// 4) Send the invite.
			PWActions.click(SEND_INVITE_BUTTON, "Clicked on Send invite");

			// Give the backend a moment to register the invite and the list to refresh.
			Thread.sleep(4000);

			// 5) Verify the "Pending" status tag appears.
			PWActions.waitFor(PENDING_STATUS_TAG, "Wait for Pending status tag", 30000);

			if (PWActions.isVisible(PENDING_STATUS_TAG, "Check Pending status tag is visible")) {
				System.out.println("✅ TEST PASSED - 'Pending' status tag displayed after sending invite to "
						+ inviteEmail);
				return true;
			}

			String errorMsg = "❌ TEST FAILED - 'Pending' status tag did not appear after sending invite to "
					+ inviteEmail;
			System.out.println(errorMsg);
			PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
			return false;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// TC_54 Validate the "already pending invite" message when re-inviting the same email
	// ====================================================================================================
	// Same flow as TC_53 with the same email; also selects the "Member" role from the dropdown before
	//   sending. Re-sending an invite to an already-invited email should show an "already exists" message.
	private static final String INVITE_ROLE_DROPDOWN = "//select[@class='w-full bg-zinc-900/60 border border-white/10 rounded-xl py-2.5 px-4 focus:outline-none focus:ring-1 focus:ring-white/20 transition-all text-white text-sm cursor-pointer']";
	private static final String INVITE_ALREADY_PENDING_MESSAGE = "//div[text()='A pending invite already exists for this email']";

	public boolean ValidateInviteAlreadyPending() {
		try {
			String inviteEmail = "avision185@gmail.com";

			// 1) Open the "Automation Team" card.
			PWActions.waitFor(CREATED_TEAM_CARD, "Wait for Automation Team card", 30000);
			PWActions.click(CREATED_TEAM_CARD, "Clicked on Automation Team");

			// 2) Open the Add user form.
			PWActions.waitFor(ADD_USER_BUTTON, "Wait for Add user button", 30000);
			PWActions.click(ADD_USER_BUTTON, "Clicked on Add user");

			// 3) Enter the same (already-invited) email.
			PWActions.waitFor(INVITE_EMAIL_INPUT, "Wait for invite email input", 30000);
			PWActions.fill(INVITE_EMAIL_INPUT, inviteEmail, "Entered invite email '" + inviteEmail + "'");

			// 4) Select the "Member" role from the dropdown.
			PWActions.waitFor(INVITE_ROLE_DROPDOWN, "Wait for role dropdown", 30000);
			PWActions.select(INVITE_ROLE_DROPDOWN, "Member", "Selected 'Member' role");

			// 5) Send the invite.
			PWActions.click(SEND_INVITE_BUTTON, "Clicked on Send invite");

			// 6) Verify the "already pending invite" message appears.
			PWActions.waitFor(INVITE_ALREADY_PENDING_MESSAGE, "Wait for already-pending message", 30000);

			if (PWActions.isVisible(INVITE_ALREADY_PENDING_MESSAGE, "Check already-pending message is visible")) {
				System.out.println("✅ TEST PASSED - 'Already pending invite' message displayed for " + inviteEmail);
				return true;
			}

			String errorMsg = "❌ TEST FAILED - 'Already pending invite' message did not appear for " + inviteEmail;
			System.out.println(errorMsg);
			PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
			return false;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// TC_55 Validate a pending invite is removed after cancelling it
	// ====================================================================================================
	// Settings -> open the Automation Team card: confirm the pending invite detail is visible, click
	//   cancel, then confirm the same detail is no longer displayed (the invite was removed).
	// Target the pending invite uniquely by the invited email so the check is specific to this invite.
	private static final String PENDING_INVITE_DETAIL = "//div[@class='text-white font-medium truncate' and normalize-space()='avision185@gmail.com']";
	private static final String CANCEL_INVITE_BUTTON = "//button[@class='p-2 rounded-lg text-zinc-400 hover:text-red-400 hover:bg-white/5 transition-all cursor-pointer disabled:opacity-50 disabled:cursor-not-allowed']";

	public boolean ValidateCancelPendingInvite() {
		try {
			// 1) Open the "Automation Team" card.
			PWActions.waitFor(CREATED_TEAM_CARD, "Wait for Automation Team card", 30000);
			PWActions.click(CREATED_TEAM_CARD, "Clicked on Automation Team");

			// 2) Precondition: the pending invite detail should be visible.
			PWActions.waitFor(PENDING_INVITE_DETAIL, "Wait for pending invite detail", 30000);
			if (!PWActions.isVisible(PENDING_INVITE_DETAIL, "Check pending invite detail is visible before cancel")) {
				String preMsg = "❌ TEST FAILED - Pending invite detail was not visible before cancelling";
				System.out.println(preMsg);
				PWBaseTest.getFailureContext().setErrorMessage(preMsg);
				return false;
			}

			// 3) Cancel the pending invite.
			PWActions.click(CANCEL_INVITE_BUTTON, "Clicked on cancel invite");

			// Give the list a moment to refresh after cancelling.
			Thread.sleep(3000);

			// 4) Verify the pending invite detail is no longer displayed -> test passes.
			PWActions.waitForHidden(PENDING_INVITE_DETAIL, "Wait for pending invite detail to disappear", 30000);

			if (!PWActions.isVisible(PENDING_INVITE_DETAIL, "Check pending invite detail is hidden after cancel")) {
				System.out.println("✅ TEST PASSED - Pending invite removed after cancelling");
				return true;
			}

			String errorMsg = "❌ TEST FAILED - Pending invite detail still displayed after cancelling";
			System.out.println(errorMsg);
			PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
			return false;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// TC_56 Create an API key and validate the success popup appears
	// ====================================================================================================
	// Settings -> API tab -> Create API key -> enter a name -> Create -> a success popup is shown.
	// Match the API Keys tab by its stable data-tour attribute (NOT the active-state class) so it works
	// even though "User Management" is the default-active tab on the Settings page.
	private static final String API_TAB = "//button[@data-tour='settings-apiKeys']";
	private static final String CREATE_API_KEY_BUTTON = "//button[@class='flex items-center gap-2 px-4 py-2 rounded-xl bg-zinc-900 text-white text-sm font-medium hover:bg-zinc-800 transition-all border border-white/10 cursor-pointer']";
	private static final String API_KEY_NAME_INPUT = "//input[@class='w-full bg-zinc-950/60 border border-white/10 rounded-xl py-2.5 px-4 focus:outline-none focus:ring-1 focus:ring-white/20 transition-all placeholder:text-zinc-500 text-white text-sm']";
	private static final String CREATE_API_KEY_SUBMIT = "//button[@class='px-6 py-2.5 rounded-xl bg-white text-zinc-900 text-sm font-medium hover:bg-zinc-100 transition-all disabled:opacity-50 disabled:cursor-not-allowed cursor-pointer']";
	private static final String API_KEY_SUCCESS_POPUP = "//h3[@class='text-white font-semibold text-lg']";

	public boolean CreateApiKeyAndVerify() {
		try {
			String apiKeyName = "1st API key";

			// 1) Open the API tab.
			PWActions.waitFor(API_TAB, "Wait for API tab", 30000);
			PWActions.click(API_TAB, "Clicked on API tab");

			// 2) Open the create-API-key form.
			PWActions.waitFor(CREATE_API_KEY_BUTTON, "Wait for Create API key button", 30000);
			PWActions.click(CREATE_API_KEY_BUTTON, "Clicked on Create API key");

			// 3) Enter the API key name.
			PWActions.waitFor(API_KEY_NAME_INPUT, "Wait for API key name input", 30000);
			PWActions.fill(API_KEY_NAME_INPUT, apiKeyName, "Entered API key name '" + apiKeyName + "'");

			// 4) Submit to create the API key.
			PWActions.click(CREATE_API_KEY_SUBMIT, "Clicked on Create");

			// 5) Verify the success popup appears.
			PWActions.waitFor(API_KEY_SUCCESS_POPUP, "Wait for API key success popup", 30000);

			if (PWActions.isVisible(API_KEY_SUCCESS_POPUP, "Check API key success popup is visible")) {
				System.out.println("✅ TEST PASSED - API key '" + apiKeyName + "' generated and success popup shown");
				return true;
			}

			String errorMsg = "❌ TEST FAILED - Success popup did not appear after creating API key '" + apiKeyName
					+ "'";
			System.out.println(errorMsg);
			PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
			return false;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// TC_57 Validate the expiration date shown when selecting the "3 months" option
	// ====================================================================================================
	// Settings -> API tab -> Create API key -> select "3 months" -> the displayed expiry date should equal
	//   today + 3 months (e.g. today 17 Jun 2026 -> "Sep 17, 2026"; today 1 Oct 2026 -> "Jan 1, 2027").
	private static final String EXPIRY_3_MONTHS_TAB = "//button[@class='px-4 py-2 rounded-xl text-sm font-medium border transition-all cursor-pointer bg-white text-zinc-900 border-white']";
	private static final String EXPIRY_DATE_TEXT = "//span[@class='text-zinc-200']";

	public boolean ValidateApiKeyExpiry3Months() {
		try {
			// 1) Open the API tab.
			PWActions.waitFor(API_TAB, "Wait for API tab", 30000);
			PWActions.click(API_TAB, "Clicked on API tab");

			// 2) Open the create-API-key form.
			PWActions.waitFor(CREATE_API_KEY_BUTTON, "Wait for Create API key button", 30000);
			PWActions.click(CREATE_API_KEY_BUTTON, "Clicked on Create API key");

			// 3) Select the "3 months" expiration option.
			PWActions.waitFor(EXPIRY_3_MONTHS_TAB, "Wait for 3 months option", 30000);
			PWActions.click(EXPIRY_3_MONTHS_TAB, "Selected 3 months expiration");

			// 4) Read the displayed expiry date.
			PWActions.waitFor(EXPIRY_DATE_TEXT, "Wait for expiry date text", 30000);
			String actualDate = PWActions.getText(EXPIRY_DATE_TEXT, "Get displayed expiry date").trim();

			// 5) Compute the expected date = today + 3 months, formatted like "Sep 17, 2026".
			java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("MMM d, yyyy",
					java.util.Locale.ENGLISH);
			String expectedDate = java.time.LocalDate.now().plusMonths(3).format(fmt);

			System.out.println("📅 Expiry date - expected: '" + expectedDate + "', actual: '" + actualDate + "'");

			if (actualDate.contains(expectedDate)) {
				System.out.println("✅ TEST PASSED - Expiry date matches today + 3 months (" + expectedDate + ")");
				return true;
			}

			String errorMsg = "❌ TEST FAILED - Expiry date mismatch. Expected '" + expectedDate + "', but displayed '"
					+ actualDate + "'";
			System.out.println(errorMsg);
			PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
			return false;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// TC_58 Validate the user can select a custom expiration date
	// ====================================================================================================
	// Settings -> API tab -> Create API key -> select "Custom" -> enter tomorrow's date and confirm the
	//   date input accepted it (e.g. running on 17 Jun 2026 -> 2026-06-18).
	// Match the "Custom" expiry option by its visible text (the active-state class alone is shared by
	// every selected tab, so it can't tell "Custom" apart from "3 months").
	private static final String EXPIRY_CUSTOM_TAB = "//button[contains(@class,'rounded-xl') and normalize-space(.)='Custom']";
	// Target the native date picker by its type — the border/ring classes toggle between red (invalid)
	// and white (valid) at runtime, so matching the full class string is brittle.
	private static final String CUSTOM_DATE_INPUT = "//input[@type='date']";

	public boolean ValidateApiKeyCustomDate() {
		try {
			// 1) Open the API tab.
			PWActions.waitFor(API_TAB, "Wait for API tab", 30000);
			PWActions.click(API_TAB, "Clicked on API tab");

			// 2) Open the create-API-key form.
			PWActions.waitFor(CREATE_API_KEY_BUTTON, "Wait for Create API key button", 30000);
			PWActions.click(CREATE_API_KEY_BUTTON, "Clicked on Create API key");

			// 3) Select the "Custom" expiration option.
			PWActions.waitFor(EXPIRY_CUSTOM_TAB, "Wait for Custom option", 30000);
			PWActions.click(EXPIRY_CUSTOM_TAB, "Selected Custom expiration");

			// 4) Enter tomorrow's date (native date inputs expect the yyyy-MM-dd format).
			String tomorrow = java.time.LocalDate.now().plusDays(1)
					.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			PWActions.waitFor(CUSTOM_DATE_INPUT, "Wait for custom date input", 30000);
			PWActions.fill(CUSTOM_DATE_INPUT, tomorrow, "Entered custom date '" + tomorrow + "'");

			// 5) Read the value back to confirm the input accepted the date.
			String enteredValue = page.locator(CUSTOM_DATE_INPUT).inputValue().trim();
			System.out.println("📅 Custom date - entered: '" + tomorrow + "', input value: '" + enteredValue + "'");

			if (tomorrow.equals(enteredValue)) {
				System.out.println("✅ TEST PASSED - User was able to select the custom date " + tomorrow);
				return true;
			}

			String errorMsg = "❌ TEST FAILED - Custom date not accepted. Expected '" + tomorrow + "', input value '"
					+ enteredValue + "'";
			System.out.println(errorMsg);
			PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
			return false;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// TC_59 Validate the created/expiry dates shown on the first API key card
	// ====================================================================================================
	// Settings -> API tab -> read the meta line of the first API key card, e.g.
	//   "Created Jun 3, 2026•Expires Sep 1, 2026". The "Created" date must equal today, and the
	//   "Expires" date must equal the created date + 3 calendar months (calendar-month billing, NOT a
	//   fixed 90-day period). Same day-of-month, clamped to the month's last day when it has fewer days
	//   (e.g. Created Jan 31 -> Expires Apr 30). LocalDate.plusMonths(3) implements exactly this rule.
	private static final String FIRST_API_KEY_META_TEXT = "(//div[@class='bg-zinc-900/50 border border-white/5 rounded-2xl p-5 rim-light shadow-depth-2'])[1]//div[@class='text-zinc-500 text-xs mt-1']";

	public boolean ValidateApiKeyCreatedAndExpiryDate() {
		try {
			// 1) Open the API tab.
			PWActions.waitFor(API_TAB, "Wait for API tab", 30000);
			PWActions.click(API_TAB, "Clicked on API tab");

			// 2) Read the meta line of the first API key card, e.g. "Created Jun 3, 2026•Expires Sep 1, 2026".
			PWActions.waitFor(FIRST_API_KEY_META_TEXT, "Wait for first API key card meta text", 30000);
			String metaText = PWActions.getText(FIRST_API_KEY_META_TEXT, "Get first API key created/expires text").trim();
			System.out.println("🔎 API key meta text: '" + metaText + "'");

			// 3) Split into the Created and Expires parts. The two are separated by a bullet (•/•).
			String afterCreated = metaText.replace("Created", "").trim();
			int expiresIdx = afterCreated.indexOf("Expires");
			if (expiresIdx < 0) {
				String errorMsg = "❌ TEST FAILED - Could not find 'Expires' in API key meta text: '" + metaText + "'";
				System.out.println(errorMsg);
				PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
				return false;
			}
			String createdPart = afterCreated.substring(0, expiresIdx).replaceAll("[••]", "").trim();
			String expiresPart = afterCreated.substring(expiresIdx).replace("Expires", "").trim();

			// 4) Parse both dates (format like "Jun 3, 2026").
			java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("MMM d, yyyy",
					java.util.Locale.ENGLISH);
			java.time.LocalDate createdDate = java.time.LocalDate.parse(createdPart, fmt);
			java.time.LocalDate expiresDate = java.time.LocalDate.parse(expiresPart, fmt);

			java.time.LocalDate today = java.time.LocalDate.now();
			java.time.LocalDate expectedExpiry = createdDate.plusMonths(3);

			System.out.println("📅 Created: '" + createdPart + "' (expected today: " + today.format(fmt) + ")");
			System.out.println("📅 Expires: '" + expiresPart + "' (expected created + 3 months: "
					+ expectedExpiry.format(fmt) + ")");

			// 5) Validate: Created == today and Expires == created + 3 months.
			boolean createdMatches = createdDate.equals(today);
			boolean expiresMatches = expiresDate.equals(expectedExpiry);

			if (createdMatches && expiresMatches) {
				System.out.println("✅ TEST PASSED - Created date is today and Expires date is 3 months later");
				return true;
			}

			String errorMsg = "❌ TEST FAILED - Date mismatch."
					+ (createdMatches ? "" : " Created='" + createdPart + "' but expected today '" + today.format(fmt) + "'.")
					+ (expiresMatches ? ""
							: " Expires='" + expiresPart + "' but expected '" + expectedExpiry.format(fmt) + "'.");
			System.out.println(errorMsg);
			PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
			return false;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// TC_60 Delete the first API key and validate it was removed
	// ====================================================================================================
	// Settings -> API tab -> click the delete (trash) icon on the first API key card -> confirm in the
	//   dialog -> the number of API key cards must drop by exactly one (i.e. that key is gone).
	private static final String API_KEY_CARD = "//div[@class='bg-zinc-900/50 border border-white/5 rounded-2xl p-5 rim-light shadow-depth-2']";
	private static final String FIRST_API_KEY_DELETE_BUTTON = "(//div[@class='bg-zinc-900/50 border border-white/5 rounded-2xl p-5 rim-light shadow-depth-2']//button[@class='p-2 rounded-lg text-zinc-400 hover:text-red-400 hover:bg-white/5 transition-all cursor-pointer shrink-0'])[1]";
	private static final String DELETE_CONFIRM_BUTTON = "//button[@class='flex cursor-pointer items-center gap-2 px-5 py-2 rounded-lg bg-red-500 text-white text-sm font-medium hover:bg-red-600 transition-all shadow-[0_0_20px_rgba(239,68,68,0.2)] disabled:opacity-40 disabled:cursor-not-allowed']";

	public boolean DeleteApiKeyAndVerify() {
		try {
			// 1) Open the API tab.
			PWActions.waitFor(API_TAB, "Wait for API tab", 30000);
			PWActions.click(API_TAB, "Clicked on API tab");

			// 2) Count the API key cards before deletion. Wait on the FIRST card only — the locator
			//    matches every card, so a strict waitFor on it would fail with "resolved to N elements".
			page.locator(API_KEY_CARD).first().waitFor();
			int countBefore = page.locator(API_KEY_CARD).count();
			System.out.println("🔢 API key cards before delete: " + countBefore);

			if (countBefore == 0) {
				String errorMsg = "❌ TEST FAILED - No API key card available to delete";
				System.out.println(errorMsg);
				PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
				return false;
			}

			// 3) Click the delete (trash) icon on the first API key card.
			PWActions.waitFor(FIRST_API_KEY_DELETE_BUTTON, "Wait for delete icon on first API key", 30000);
			PWActions.click(FIRST_API_KEY_DELETE_BUTTON, "Clicked delete on first API key");

			// 4) Confirm the deletion in the confirmation dialog.
			PWActions.waitFor(DELETE_CONFIRM_BUTTON, "Wait for delete confirmation button", 30000);
			PWActions.click(DELETE_CONFIRM_BUTTON, "Clicked confirm delete");

			// Give the backend and the list a moment to refresh.
			Thread.sleep(3000);

			// 5) Re-count the cards and verify exactly one was removed.
			int countAfter = page.locator(API_KEY_CARD).count();
			System.out.println("🔢 API key cards after delete: " + countAfter);

			if (countAfter == countBefore - 1) {
				System.out.println(
						"✅ TEST PASSED - API key deleted successfully (" + countBefore + " -> " + countAfter + ")");
				return true;
			}

			String errorMsg = "❌ TEST FAILED - API key was not deleted as expected. Before=" + countBefore + ", After="
					+ countAfter;
			System.out.println(errorMsg);
			PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
			return false;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// TC_61 Select the first 4 videos in a conversation and validate the "4 videos selected" text
	// ====================================================================================================
	// Conversations page -> open the Select video panel -> click the first 4 video tiles -> click Done ->
	//   the summary text (e.g. "4 videos selected") must report exactly 4.
	private static final String SELECT_VIDEO_TAB = "//div[@class='w-16 h-16 rounded-full bg-white/10 flex items-center justify-center text-zinc-300']";
	private static final String VIDEO_TILE = "//div[@class='relative aspect-video overflow-hidden bg-zinc-900']";
	private static final String SELECT_VIDEO_DONE_BUTTON = "//button[@class='flex-1 h-12 rounded-xl bg-white text-black font-medium hover:bg-zinc-200 transition-colors disabled:opacity-50 disabled:cursor-not-allowed cursor-pointer']";
	private static final String VIDEOS_SELECTED_TEXT = "//p[@class='mt-4 text-[14px] font-medium text-zinc-100']";

	public boolean SelectFourVideosAndVerify() {
		try {
			int videosToSelect = 4;

			// 1) Open the Select video panel.
			PWActions.waitFor(SELECT_VIDEO_TAB, "Wait for Select video tab", 30000);
			PWActions.click(SELECT_VIDEO_TAB, "Clicked on Select video");

			// 2) Wait for the video tiles and make sure at least 4 are available.
			//    The tile locator matches many elements, so wait on the FIRST one (strict-safe).
			page.locator(VIDEO_TILE).first().waitFor();
			int available = page.locator(VIDEO_TILE).count();
			System.out.println("🎞️ Video tiles available: " + available);

			if (available < videosToSelect) {
				String errorMsg = "❌ TEST FAILED - Need " + videosToSelect + " videos but only " + available
						+ " are available";
				System.out.println(errorMsg);
				PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
				return false;
			}

			// 3) Click the first 4 video tiles.
			for (int i = 0; i < videosToSelect; i++) {
				page.locator(VIDEO_TILE).nth(i).click();
				System.out.println("🎬 Selected video tile #" + (i + 1));
			}

			// 4) Click Done.
			PWActions.waitFor(SELECT_VIDEO_DONE_BUTTON, "Wait for Done button", 30000);
			PWActions.click(SELECT_VIDEO_DONE_BUTTON, "Clicked on Done");

			// 5) Read the summary text and validate it reports exactly 4 videos selected.
			PWActions.waitFor(VIDEOS_SELECTED_TEXT, "Wait for videos-selected text", 30000);
			String selectedText = PWActions.getText(VIDEOS_SELECTED_TEXT, "Get videos-selected text").trim();
			System.out.println("📝 Videos selected text: '" + selectedText + "'");

			double selectedCount = extractDecimal(selectedText);
			if (selectedCount == videosToSelect) {
				System.out.println("✅ TEST PASSED - " + selectedText);
				return true;
			}

			String errorMsg = "❌ TEST FAILED - Expected " + videosToSelect + " videos selected but text was '"
					+ selectedText + "'";
			System.out.println(errorMsg);
			PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
			return false;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// TC_62 / TC_63 share one flow: Conversations -> Select video "All_queries_03" -> Done -> pick a
	// query chip -> Send, then wait up to 90s for the result cards and assert every card's time range
	// falls within an allowed window. The two test cases differ only by which query chip is clicked and
	// the allowed window.
	private static final String VIDEO_BY_NAME_ALL_QUERIES_03 = "//div[text()='All_queries_03']";
	private static final String DETECT_VIOLENCE_BUTTON = "(//button[@class='text-[11px] font-bold tracking-wider uppercase text-zinc-200 px-4 py-2 rounded-xl bg-zinc-900/50 border border-white/10 hover:bg-zinc-900 hover:border-white/25 hover:text-white transition-all duration-200 cursor-pointer rim-light'])[1]";
	private static final String NUDITY_QUERY_BUTTON = "(//button[@class='text-[11px] font-bold tracking-wider uppercase text-zinc-200 px-4 py-2 rounded-xl bg-zinc-900/50 border border-white/10 hover:bg-zinc-900 hover:border-white/25 hover:text-white transition-all duration-200 cursor-pointer rim-light'])[2]";
	private static final String VEHICLE_QUERY_BUTTON = "(//button[@class='text-[11px] font-bold tracking-wider uppercase text-zinc-200 px-4 py-2 rounded-xl bg-zinc-900/50 border border-white/10 hover:bg-zinc-900 hover:border-white/25 hover:text-white transition-all duration-200 cursor-pointer rim-light'])[3]";
	private static final String DETECT_LOGO_QUERY_BUTTON = "(//button[@class='text-[11px] font-bold tracking-wider uppercase text-zinc-200 px-4 py-2 rounded-xl bg-zinc-900/50 border border-white/10 hover:bg-zinc-900 hover:border-white/25 hover:text-white transition-all duration-200 cursor-pointer rim-light'])[4]";
	private static final String DETECT_EMOTIONS_QUERY_BUTTON = "(//button[@class='text-[11px] font-bold tracking-wider uppercase text-zinc-200 px-4 py-2 rounded-xl bg-zinc-900/50 border border-white/10 hover:bg-zinc-900 hover:border-white/25 hover:text-white transition-all duration-200 cursor-pointer rim-light'])[5]";
	private static final String DETECT_ANIMALS_QUERY_BUTTON = "(//button[@class='text-[11px] font-bold tracking-wider uppercase text-zinc-200 px-4 py-2 rounded-xl bg-zinc-900/50 border border-white/10 hover:bg-zinc-900 hover:border-white/25 hover:text-white transition-all duration-200 cursor-pointer rim-light'])[6]";
	private static final String RESULT_CARD = "//div[@role='button' and contains(@class,'glass-card') and contains(@class,'rim-light')]";
	private static final String NO_MATCH_MESSAGE = "//p[normalize-space()='No matching moments found.']";
	private static final String NO_RELEVANT_MESSAGE = "//p[normalize-space()='No relevant searches found for this query.']";
	// Either "no results" message ends the 90s wait early and fails the test.
	private static final String NO_RESULTS_MESSAGE = NO_MATCH_MESSAGE + " | " + NO_RELEVANT_MESSAGE;
	// Relative to a result card: the div holding the "HH:MM:SS – HH:MM:SS" range.
	private static final String CARD_TIME_RANGE_REL = "xpath=.//div[@class='text-[12px] font-medium text-zinc-100']";

	// TC_62 Detect Violence results must all fall within 00:00:45 – 00:00:57.
	public boolean ValidateViolenceResultsInTimeRange() {
		return runQueryAndValidateRange(DETECT_VIOLENCE_BUTTON, "Detect Violence", 45, 57);
	}

	// TC_63 Nudity query results must all fall within 00:00:10 – 00:00:20.
	public boolean ValidateNudityResultsInTimeRange() {
		return runQueryAndValidateRange(NUDITY_QUERY_BUTTON, "Nudity", 10, 20);
	}

	// TC_64 Vehicle query results must all fall within 00:00:56 – 00:01:12.
	public boolean ValidateVehicleResultsInTimeRange() {
		return runQueryAndValidateRange(VEHICLE_QUERY_BUTTON, "Vehicle", 56, 72);
	}

	// TC_65 Detect Logo query results must all fall within 00:00:20 – 00:00:30.
	public boolean ValidateLogoResultsInTimeRange() {
		return runQueryAndValidateRange(DETECT_LOGO_QUERY_BUTTON, "Detect Logo", 20, 30);
	}

	// TC_66 Detect Emotions query results must all fall within 00:00:00 – 00:00:10.
	public boolean ValidateEmotionsResultsInTimeRange() {
		return runQueryAndValidateRange(DETECT_EMOTIONS_QUERY_BUTTON, "Detect Emotions", 0, 10);
	}

	// TC_67 Detect Animals query results must all fall within 00:00:30 – 00:00:45.
	public boolean ValidateAnimalsResultsInTimeRange() {
		return runQueryAndValidateRange(DETECT_ANIMALS_QUERY_BUTTON, "Detect Animals", 30, 45);
	}

	// Shared flow + range validation. startBoundarySec/endBoundarySec define the inclusive allowed window
	// (every card must have start >= startBoundarySec and end <= endBoundarySec). FAILS on
	// "No matching moments found.", on no cards within 90s, or on any out-of-range card.
	private boolean runQueryAndValidateRange(String queryButton, String queryLabel, int startBoundarySec,
			int endBoundarySec) {
		try {
			String rangeLabel = formatSeconds(startBoundarySec) + " – " + formatSeconds(endBoundarySec);

			// 1) Open the Select video panel.
			PWActions.waitFor(SELECT_VIDEO_TAB, "Wait for Select video tab", 30000);
			PWActions.click(SELECT_VIDEO_TAB, "Clicked on Select video");

			// 2) Pick the "All_queries_03" video.
			PWActions.waitFor(VIDEO_BY_NAME_ALL_QUERIES_03, "Wait for 'All_queries_03' video", 30000);
			PWActions.click(VIDEO_BY_NAME_ALL_QUERIES_03, "Selected 'All_queries_03' video");

			// 3) Confirm the selection (Done).
			PWActions.waitFor(SELECT_VIDEO_DONE_BUTTON, "Wait for Done button", 30000);
			PWActions.click(SELECT_VIDEO_DONE_BUTTON, "Clicked on Done");

			// 4) Choose the query chip (Detect Violence / Nudity / ...).
			PWActions.waitFor(queryButton, "Wait for '" + queryLabel + "' option", 30000);
			PWActions.click(queryButton, "Clicked on " + queryLabel);

			// 5) Send the request.
			PWActions.waitFor(SEND_BUTTON, "Wait for Send button", 30000);
			PWActions.click(SEND_BUTTON, "Clicked on Send");

			// 6) Wait up to 90s for either result cards or one of the "no results" messages. As soon as a
			//    "no results" message appears the wait ends early (no full 90s wait) and the test fails.
			System.out.println("⏳ Waiting up to 90s for " + queryLabel + " results...");
			page.locator(RESULT_CARD + " | " + NO_RESULTS_MESSAGE).first()
					.waitFor(new com.microsoft.playwright.Locator.WaitForOptions().setTimeout(90000));

			// Failure #1: a "no results" message was displayed
			//    ("No matching moments found." or "No relevant searches found for this query.").
			if (page.locator(NO_RESULTS_MESSAGE).count() > 0) {
				String noResultsText = page.locator(NO_RESULTS_MESSAGE).first().textContent().trim();
				String errorMsg = "❌ TEST FAILED - '" + noResultsText + "' was displayed for " + queryLabel;
				System.out.println(errorMsg);
				PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
				return false;
			}

			// Failure #2: no result cards appeared.
			int cardCount = page.locator(RESULT_CARD).count();
			System.out.println("🃏 Result cards found: " + cardCount);
			if (cardCount == 0) {
				String errorMsg = "❌ TEST FAILED - No result cards appeared within 90 seconds for " + queryLabel;
				System.out.println(errorMsg);
				PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
				return false;
			}

			// 7) Validate every card's time range is within the allowed window.
			StringBuilder violations = new StringBuilder();
			for (int i = 0; i < cardCount; i++) {
				String timeText = page.locator(RESULT_CARD).nth(i).locator(CARD_TIME_RANGE_REL).first()
						.textContent().trim();

				// Pull the two HH:MM:SS values (the trailing "· 00:04" is MM:SS, so it won't match).
				java.util.regex.Matcher m = java.util.regex.Pattern.compile("(\\d{1,2}):(\\d{2}):(\\d{2})")
						.matcher(timeText);
				Integer startSec = null, endSec = null;
				if (m.find()) {
					startSec = toSeconds(m.group(1), m.group(2), m.group(3));
				}
				if (m.find()) {
					endSec = toSeconds(m.group(1), m.group(2), m.group(3));
				}

				if (startSec == null || endSec == null) {
					violations.append("\n  • Card ").append(i + 1).append(" - could not parse time from '")
							.append(timeText).append("'");
					continue;
				}

				boolean inRange = startSec >= startBoundarySec && endSec <= endBoundarySec;
				System.out.println("  Card " + (i + 1) + ": '" + timeText + "' -> start=" + startSec + "s, end="
						+ endSec + "s, inRange=" + inRange);
				if (!inRange) {
					violations.append("\n  • Card ").append(i + 1).append(" out of range: '").append(timeText)
							.append("' (start=").append(startSec).append("s, end=").append(endSec).append("s)");
				}
			}

			if (violations.length() == 0) {
				System.out.println("✅ TEST PASSED - All " + cardCount + " " + queryLabel
						+ " result cards are within " + rangeLabel);
				return true;
			}

			String errorMsg = "❌ TEST FAILED - One or more " + queryLabel
					+ " result cards are outside the allowed range " + rangeLabel + ":" + violations;
			System.out.println(errorMsg);
			PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
			return false;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// Convert an HH:MM:SS triple into a total number of seconds.
	private int toSeconds(String hh, String mm, String ss) {
		return Integer.parseInt(hh) * 3600 + Integer.parseInt(mm) * 60 + Integer.parseInt(ss);
	}

	// Format a number of seconds as "HH:MM:SS" (used for human-readable range messages).
	private String formatSeconds(int totalSeconds) {
		int h = totalSeconds / 3600;
		int m = (totalSeconds % 3600) / 60;
		int s = totalSeconds % 60;
		return String.format("%02d:%02d:%02d", h, m, s);
	}

	// Extract the first decimal/integer number found in a string, ignoring thousands separators
	// (e.g. "1,234.50 credits" -> 1234.50). Returns -1 if none found.
	private double extractDecimal(String text) {
		if (text == null) {
			return -1d;
		}
		java.util.regex.Matcher m = java.util.regex.Pattern.compile("([0-9][0-9,]*(?:\\.[0-9]+)?)").matcher(text);
		if (!m.find()) {
			return -1d;
		}
		return Double.parseDouble(m.group(1).replace(",", ""));
	}

	// Parse a storage string such as "3.4 GB", "850 MB" or "2 TB" into a value in MB so that
	// before/after values can be compared even when the displayed unit changes.
	private double parseStorageToMB(String text) {
		if (text == null) {
			return 0d;
		}
		java.util.regex.Matcher m = java.util.regex.Pattern
				.compile("([0-9]+(?:\\.[0-9]+)?)\\s*(KB|MB|GB|TB)", java.util.regex.Pattern.CASE_INSENSITIVE)
				.matcher(text);
		if (!m.find()) {
			return 0d;
		}
		double value = Double.parseDouble(m.group(1));
		switch (m.group(2).toUpperCase()) {
		case "KB":
			return value / 1024d;
		case "MB":
			return value;
		case "GB":
			return value * 1024d;
		case "TB":
			return value * 1024d * 1024d;
		default:
			return value;
		}
	}

	// Extract the first integer found in a string (e.g. "12 videos" -> 12). Returns -1 if none.
	private int extractNumber(String text) {
		if (text == null) {
			return -1;
		}
		java.util.regex.Matcher m = java.util.regex.Pattern.compile("(\\d+)").matcher(text);
		return m.find() ? Integer.parseInt(m.group(1)) : -1;
	}

//-----------------------------------------------------------------------------
 
	public boolean ClickProccesedVideo() {
		try {
 
			// Click on Proceesed video
			PWActions.waitFor("(//div[@class='relative'])[3]", "wait for Processded video", 30000);
			PWActions.click("(//div[@class='relative'])[3]", "click on Processded video");

			// Click on Chat Tab
			PWActions.waitFor(" //button[normalize-space()='Chat about full video']", "wait for chat tab", 30000);
			PWActions.click("//button[normalize-space()='Chat about full video']", "chat about full video");

			// Click on Placeholder
			PWActions.click("//input[@placeholder='Ask a question about the video...']", "Clicked on Chat plceholder");
			PWActions.fill("//input[@placeholder='Ask a question about the video...']", "what is happening in video",
					"question Asked");

			// Click on send Tab
			PWActions.click("//button[text()='Send']", "Clicked on send Tab");
			return true;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
	}

	// dynamic reply compare
 
	public boolean CompareTextBYPython(String expectedText) {
		try {
			String actualText = PWActions.getText(
					"(//div[@class='prose prose-sm max-w-none text-slate-800 break-words'])[2]", "Get Actual Text");

			System.out.println("Actual text is: " + actualText);

			double similarity = TextCompareUtil.getSimilarity(expectedText, actualText);

			System.out.println("Similarity Score: " + similarity);

			// 🔥 MAIN VALIDATION
			if (similarity < 0.7) {
				System.out.println("❌ Text meaning NOT matching");
				return false;
			}

			System.out.println("✅ Text meaning matching");
			return true;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			return false;
		}
	}

	// TC_68 Validate the Analyse-mode "Summarise" reply semantically matches the expected summary
	// ====================================================================================================
	// Conversations -> Analyze mode -> Select video "All_queries_03" -> Done -> Summarise -> Send.
	//   The reply text is compared to the expected summary (Excel column qc_expectedtext) using a
	//   sentence-embedding cosine similarity (python/similarity.py via TextCompareUtil). The app rewords
	//   its answer every run, so we match MEANING (similarity >= threshold), not exact text.
	// NOTE: reuses the existing ANALYSE_TAB constant (defined for TC_46) which matches the inactive
	//   mode-tab class and takes [1] -> the Analyze tab (Search is the active tab by default). The
	//   'bg-white ... shadow-depth-1' class from the prompt is the ACTIVE-state styling and would match
	//   whichever tab is currently selected, so it is intentionally not used here.
	// Analyze-mode query chips (same button family as the Search-mode chips, distinguished by position).
	private static final String SUMMARISE_QUERY_BUTTON = "(//button[@class='text-[11px] font-bold tracking-wider uppercase text-zinc-200 px-4 py-2 rounded-xl bg-zinc-900/50 border border-white/10 hover:bg-zinc-900 hover:border-white/25 hover:text-white transition-all duration-200 cursor-pointer rim-light'])[1]";
	private static final String KEY_EVENTS_QUERY_BUTTON = "(//button[@class='text-[11px] font-bold tracking-wider uppercase text-zinc-200 px-4 py-2 rounded-xl bg-zinc-900/50 border border-white/10 hover:bg-zinc-900 hover:border-white/25 hover:text-white transition-all duration-200 cursor-pointer rim-light'])[2]";
	private static final String CONTENT_SUITABILITY_QUERY_BUTTON = "(//button[@class='text-[11px] font-bold tracking-wider uppercase text-zinc-200 px-4 py-2 rounded-xl bg-zinc-900/50 border border-white/10 hover:bg-zinc-900 hover:border-white/25 hover:text-white transition-all duration-200 cursor-pointer rim-light'])[3]";
	private static final String HASHTAG_QUERY_BUTTON = "(//button[@class='text-[11px] font-bold tracking-wider uppercase text-zinc-200 px-4 py-2 rounded-xl bg-zinc-900/50 border border-white/10 hover:bg-zinc-900 hover:border-white/25 hover:text-white transition-all duration-200 cursor-pointer rim-light'])[4]";
	private static final String SETTING_QUERY_BUTTON = "(//button[@class='text-[11px] font-bold tracking-wider uppercase text-zinc-200 px-4 py-2 rounded-xl bg-zinc-900/50 border border-white/10 hover:bg-zinc-900 hover:border-white/25 hover:text-white transition-all duration-200 cursor-pointer rim-light'])[5]";
	private static final String TITLE_QUERY_BUTTON = "(//button[@class='text-[11px] font-bold tracking-wider uppercase text-zinc-200 px-4 py-2 rounded-xl bg-zinc-900/50 border border-white/10 hover:bg-zinc-900 hover:border-white/25 hover:text-white transition-all duration-200 cursor-pointer rim-light'])[6]";
	private static final String ANALYSE_REPLY_CONTAINER = "//div[@class='rounded-2xl border border-white/10 bg-zinc-900/40 px-4 py-3 rim-light']";
	private static final double SIMILARITY_THRESHOLD = 0.7;

	// TC_68 Summarise reply must semantically match the expected summary.
	public boolean ValidateSummariseAnalysis(String expectedText) {
		return runAnalyseQueryAndCompare(SUMMARISE_QUERY_BUTTON, "Summarise", expectedText);
	}

	// TC_69 Key Events reply must semantically match the expected text.
	public boolean ValidateKeyEventsAnalysis(String expectedText) {
		return runAnalyseQueryAndCompare(KEY_EVENTS_QUERY_BUTTON, "Key Events", expectedText);
	}

	// TC_70 Content Suitability reply must semantically match the expected text.
	public boolean ValidateContentSuitabilityAnalysis(String expectedText) {
		return runAnalyseQueryAndCompare(CONTENT_SUITABILITY_QUERY_BUTTON, "Content Suitability", expectedText);
	}

	// TC_71 Hashtag reply must semantically match the expected text.
	public boolean ValidateHashtagAnalysis(String expectedText) {
		return runAnalyseQueryAndCompare(HASHTAG_QUERY_BUTTON, "Hashtag", expectedText);
	}

	// TC_72 Setting reply must semantically match the expected text.
	public boolean ValidateSettingAnalysis(String expectedText) {
		return runAnalyseQueryAndCompare(SETTING_QUERY_BUTTON, "Setting", expectedText);
	}

	// TC_73 Title reply must semantically match the expected text.
	public boolean ValidateTitleAnalysis(String expectedText) {
		return runAnalyseQueryAndCompare(TITLE_QUERY_BUTTON, "Title", expectedText);
	}

	// Shared Analyze-mode flow (TC_68 - TC_73): Analyze -> Select video "All_queries_03" -> Done -> click
	// the given query chip -> Send, then poll for the real reply and compare its MEANING to expectedText
	// (Excel column qc_expectedtext) via sentence-embedding similarity (>= SIMILARITY_THRESHOLD). The app
	// rewords its answer every run, so an exact string compare would always fail.
	private boolean runAnalyseQueryAndCompare(String queryButton, String queryLabel, String expectedText) {
		try {
			// 1) Switch to Analyze mode.
			PWActions.waitFor(ANALYSE_TAB, "Wait for Analyze tab", 30000);
			PWActions.click(ANALYSE_TAB, "Clicked on Analyze tab");

			// 2) Open the Select video panel and pick "All_queries_03".
			PWActions.waitFor(SELECT_VIDEO_TAB, "Wait for Select video tab", 30000);
			PWActions.click(SELECT_VIDEO_TAB, "Clicked on Select video");
			PWActions.waitFor(VIDEO_BY_NAME_ALL_QUERIES_03, "Wait for 'All_queries_03' video", 30000);
			PWActions.click(VIDEO_BY_NAME_ALL_QUERIES_03, "Selected 'All_queries_03' video");

			// 3) Confirm the selection (Done).
			PWActions.waitFor(SELECT_VIDEO_DONE_BUTTON, "Wait for Done button", 30000);
			PWActions.click(SELECT_VIDEO_DONE_BUTTON, "Clicked on Done");

			// 4) Choose the query chip (Summarise / Key Events / ...).
			PWActions.waitFor(queryButton, "Wait for '" + queryLabel + "' option", 30000);
			PWActions.click(queryButton, "Clicked on " + queryLabel);

			// 5) Send the request.
			PWActions.waitFor(SEND_BUTTON, "Wait for Send button", 30000);
			PWActions.click(SEND_BUTTON, "Clicked on Send");

			// 6 & 7) Poll for the real reply and compare its meaning to the expected text.
			return awaitReplyAndCompare(queryLabel, expectedText);

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// TC_74 Type a free-text question in Analyze mode and validate the reply meaning.
	// Conversations -> Analyze -> Select video "All_queries_03" -> Done -> type the question into the chat
	//   input -> Send, then compare the reply MEANING to the expected text (Excel column qc_expectedtext).
	private static final String ANALYSE_QUESTION_INPUT = "//input[@class='flex-1 bg-transparent border-none outline-none text-[14px] text-white placeholder:text-zinc-600 px-2']";
	// TC_81 The question, once sent, is echoed back in the chat header/title with this style.
	private static final String ANALYSE_QUESTION_DISPLAYED = "//span[@class='text-[12px] truncate flex-1 text-white font-semibold']";

	public boolean ValidateCustomQuestionAnalysis(String question, String expectedText) {
		try {
			// 1) Switch to Analyze mode.
			PWActions.waitFor(ANALYSE_TAB, "Wait for Analyze tab", 30000);
			PWActions.click(ANALYSE_TAB, "Clicked on Analyze tab");

			// 2) Open the Select video panel and pick "All_queries_03".
			PWActions.waitFor(SELECT_VIDEO_TAB, "Wait for Select video tab", 30000);
			PWActions.click(SELECT_VIDEO_TAB, "Clicked on Select video");
			PWActions.waitFor(VIDEO_BY_NAME_ALL_QUERIES_03, "Wait for 'All_queries_03' video", 30000);
			PWActions.click(VIDEO_BY_NAME_ALL_QUERIES_03, "Selected 'All_queries_03' video");

			// 3) Confirm the selection (Done).
			PWActions.waitFor(SELECT_VIDEO_DONE_BUTTON, "Wait for Done button", 30000);
			PWActions.click(SELECT_VIDEO_DONE_BUTTON, "Clicked on Done");

			// 4) Type the free-text question into the chat input.
			PWActions.waitFor(ANALYSE_QUESTION_INPUT, "Wait for question input", 30000);
			PWActions.fill(ANALYSE_QUESTION_INPUT, question, "Entered question '" + question + "'");

			// 5) Send the request.
			PWActions.waitFor(SEND_BUTTON, "Wait for Send button", 30000);
			PWActions.click(SEND_BUTTON, "Clicked on Send");

			// 6 & 7) Poll for the real reply and compare its meaning to the expected text.
			return awaitReplyAndCompare("Custom question", expectedText);

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// TC_81 Type a free-text question in Analyze mode and validate it is ECHOED BACK in the chat.
	// Conversations -> Analyze -> Select video "All_queries_03" -> Done -> type the question into the chat
	//   input -> Send. We do NOT validate the reply; instead we read the question shown via
	//   ANALYSE_QUESTION_DISPLAYED and PASS only if it equals the question we entered.
	public boolean ValidateQuestionDisplayedInChat(String question) {
		try {
			// 1) Switch to Analyze mode.
			PWActions.waitFor(ANALYSE_TAB, "Wait for Analyze tab", 30000);
			PWActions.click(ANALYSE_TAB, "Clicked on Analyze tab");

			// 2) Open the Select video panel and pick "All_queries_03".
			PWActions.waitFor(SELECT_VIDEO_TAB, "Wait for Select video tab", 30000);
			PWActions.click(SELECT_VIDEO_TAB, "Clicked on Select video");
			PWActions.waitFor(VIDEO_BY_NAME_ALL_QUERIES_03, "Wait for 'All_queries_03' video", 30000);
			PWActions.click(VIDEO_BY_NAME_ALL_QUERIES_03, "Selected 'All_queries_03' video");

			// 3) Confirm the selection (Done).
			PWActions.waitFor(SELECT_VIDEO_DONE_BUTTON, "Wait for Done button", 30000);
			PWActions.click(SELECT_VIDEO_DONE_BUTTON, "Clicked on Done");

			// 4) Type the free-text question into the chat input.
			PWActions.waitFor(ANALYSE_QUESTION_INPUT, "Wait for question input", 30000);
			PWActions.fill(ANALYSE_QUESTION_INPUT, question, "Entered question '" + question + "'");

			// 5) Send the request.
			PWActions.waitFor(SEND_BUTTON, "Wait for Send button", 30000);
			PWActions.click(SEND_BUTTON, "Clicked on Send");

			// 6) Read the question echoed back in the chat and compare it to what we typed.
			// The title first shows "Untitled conversation" and only updates to the real question after a
			// few seconds, so poll (max 30s) until it is no longer the placeholder / matches what we typed,
			// instead of reading the stale text immediately.
			PWActions.waitFor(ANALYSE_QUESTION_DISPLAYED, "Wait for the displayed question", 30000);
			String entered = question == null ? "" : question.trim();
			String displayed = "";
			long deadline = System.currentTimeMillis() + 30000;
			while (System.currentTimeMillis() < deadline) {
				String raw = page.locator(ANALYSE_QUESTION_DISPLAYED).first().textContent();
				displayed = raw == null ? "" : raw.trim();
				// Stop as soon as the title matches, or has changed away from the "Untitled conversation"
				// placeholder to whatever the app finally renders.
				if (displayed.equals(entered)
						|| (!displayed.isEmpty() && !displayed.equalsIgnoreCase("Untitled conversation"))) {
					break;
				}
				Thread.sleep(1000);
			}
			System.out.println("📝 Entered question : '" + entered + "'");
			System.out.println("🖥️ Displayed question: '" + displayed + "'");

			if (displayed.equals(entered)) {
				System.out.println("✅ TEST PASSED - Displayed question matches the entered question");
				return true;
			}

			String errorMsg = "❌ TEST FAILED - Displayed question '" + displayed
					+ "' does not match the entered question '" + entered + "'";
			System.out.println(errorMsg);
			PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
			return false;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// TC_82 Delete one conversation from History and validate the History count drops by exactly one.
	// Conversations -> read "History (N)" -> open the first conversation's "..." menu -> Delete ->
	//   Confirm delete -> re-read "History (N-1)". PASS only if the new count == old count - 1.
	private static final String HISTORY_COUNT_LABEL = "//span[@class='text-[13px] font-semibold']";
	private static final String HISTORY_FIRST_ITEM_MENU = "(//button[@class='opacity-0 cursor-pointer group-hover:opacity-100 text-zinc-400 hover:text-white p-1 rounded transition-opacity'])[1]";
	private static final String HISTORY_DELETE_OPTION = "//button[@class='w-full flex cursor-pointer items-center gap-2 px-3 py-1.5 text-[11px] text-red-300 hover:bg-red-500/10 hover:text-red-200 text-left']";
	private static final String HISTORY_CONFIRM_DELETE = "//button[@class='flex cursor-pointer items-center gap-2 px-5 py-2 rounded-lg bg-red-500 text-white text-sm font-medium hover:bg-red-600 transition-all shadow-[0_0_20px_rgba(239,68,68,0.2)] disabled:opacity-40 disabled:cursor-not-allowed']";

	public boolean ValidateHistoryCountDecreasesAfterDelete() {
		try {
			// 1) Read the current History count (e.g. "History (24)" -> 24).
			PWActions.waitFor(HISTORY_COUNT_LABEL, "Wait for History count label", 30000);
			int currentCount = readHistoryCount();
			System.out.println("📊 Current history count: " + currentCount);
			if (currentCount <= 0) {
				String errorMsg = "❌ TEST FAILED - No conversations available to delete (count = " + currentCount + ")";
				System.out.println(errorMsg);
				PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
				return false;
			}

			// 2) Open the first conversation's "..." menu.
			PWActions.waitFor(HISTORY_FIRST_ITEM_MENU, "Wait for first conversation menu", 30000);
			PWActions.click(HISTORY_FIRST_ITEM_MENU, "Clicked on first conversation '...' menu");

			// 3) Click the Delete option.
			PWActions.waitFor(HISTORY_DELETE_OPTION, "Wait for Delete option", 30000);
			PWActions.click(HISTORY_DELETE_OPTION, "Clicked on Delete");

			// 4) Confirm the delete.
			PWActions.waitFor(HISTORY_CONFIRM_DELETE, "Wait for Confirm delete button", 30000);
			PWActions.click(HISTORY_CONFIRM_DELETE, "Clicked on Confirm delete");

			// 5) Re-read the count, polling until it reflects the deletion (max 30s).
			int expectedCount = currentCount - 1;
			int newCount = currentCount;
			long deadline = System.currentTimeMillis() + 30000;
			while (System.currentTimeMillis() < deadline) {
				newCount = readHistoryCount();
				if (newCount == expectedCount) {
					break;
				}
				Thread.sleep(1000);
			}
			System.out.println("📊 History count after delete: " + newCount);

			if (newCount == expectedCount) {
				System.out.println("✅ TEST PASSED - History count decreased by one (" + currentCount + " -> "
						+ newCount + ")");
				return true;
			}

			String errorMsg = "❌ TEST FAILED - History count did not decrease by one (was " + currentCount
					+ ", now " + newCount + ")";
			System.out.println(errorMsg);
			PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
			return false;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// Reads the History label (e.g. "History (24)") and returns the number inside the parentheses.
	private int readHistoryCount() {
		String raw = page.locator(HISTORY_COUNT_LABEL).first().textContent();
		String text = raw == null ? "" : raw.trim();
		java.util.regex.Matcher m = java.util.regex.Pattern.compile("\\d+").matcher(text);
		if (m.find()) {
			return Integer.parseInt(m.group());
		}
		throw new RuntimeException("Could not parse a number from History label: '" + text + "'");
	}

	// TC_83 Rename the first conversation in History and validate the title actually changes.
	// Conversations -> read the first conversation title (initial text) -> open its "..." menu -> Rename ->
	//   clear the title field -> type "Automation history" -> press Enter -> re-read the title. PASS only if
	//   the title is no longer the initial text (i.e. it got edited).
	private static final String CONVERSATION_TITLE_FIRST = "(//span[@class='text-[12px] truncate flex-1 text-zinc-200 font-medium group-hover:text-white'])[1]";
	private static final String HISTORY_RENAME_OPTION = "//button[@class='w-full cursor-pointer flex items-center gap-2 px-3 py-1.5 text-[11px] text-zinc-200 hover:bg-white/5 hover:text-white text-left']";

	public boolean ValidateRenameConversation() {
		try {
			String newTitle = "Automation history";

			// 1) Read the first conversation's current title (the initial text).
			PWActions.waitFor(CONVERSATION_TITLE_FIRST, "Wait for first conversation title", 30000);
			String rawInitial = page.locator(CONVERSATION_TITLE_FIRST).first().textContent();
			String initialText = rawInitial == null ? "" : rawInitial.trim();
			System.out.println("📝 Initial conversation title: '" + initialText + "'");

			// 2) Open the first conversation's "..." menu.
			PWActions.waitFor(HISTORY_FIRST_ITEM_MENU, "Wait for first conversation menu", 30000);
			PWActions.click(HISTORY_FIRST_ITEM_MENU, "Clicked on first conversation '...' menu");

			// 3) Click the Rename option.
			PWActions.waitFor(HISTORY_RENAME_OPTION, "Wait for Rename option", 30000);
			PWActions.click(HISTORY_RENAME_OPTION, "Clicked on Rename");

			// 4) Focus the title field, clear it, and type the new title.
			PWActions.waitFor(CONVERSATION_TITLE_FIRST, "Wait for editable title field", 30000);
			page.locator(CONVERSATION_TITLE_FIRST).first().click();
			page.keyboard().press("Control+A");
			page.keyboard().press("Delete");
			page.keyboard().type(newTitle);

			// 5) Hit Enter to confirm the rename.
			page.keyboard().press("Enter");

			// 6) Re-read the title at the same location, polling until it changes away from the initial text.
			String displayed = initialText;
			long deadline = System.currentTimeMillis() + 30000;
			while (System.currentTimeMillis() < deadline) {
				String raw = page.locator(CONVERSATION_TITLE_FIRST).first().textContent();
				displayed = raw == null ? "" : raw.trim();
				if (!displayed.equals(initialText)) {
					break;
				}
				Thread.sleep(1000);
			}
			System.out.println("📝 Conversation title after rename: '" + displayed + "'");

			if (!displayed.equals(initialText)) {
				System.out.println("✅ TEST PASSED - Conversation title was edited ('" + initialText + "' -> '"
						+ displayed + "')");
				return true;
			}

			String errorMsg = "❌ TEST FAILED - Conversation title was not edited (still '" + initialText + "')";
			System.out.println(errorMsg);
			PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
			return false;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// TC_84 Validate a past conversation's results are restored when re-opened from History.
	// Conversations -> Analyze -> Select video "VI_35_Emotions" -> Done -> Title query -> Settings query ->
	//   read the results container (history one) -> Start new conversation -> refresh -> open the first
	//   History item -> PASS only if the results container appears again.
	private static final String VIDEO_BY_NAME_VI_35_EMOTIONS = "//div[text()='VI_35_Emotions']";
	private static final String CONVERSATION_RESULT_CONTAINER = "//div[@class='max-w-[860px] mx-auto px-8 py-8 flex flex-col gap-6']";
	private static final String START_NEW_CONVERSATION_BUTTON = "//button[@class='w-10 h-10 rounded-xl bg-white text-black flex items-center justify-center hover:bg-zinc-200 transition-colors cursor-pointer shadow-depth-1']";
	private static final String HISTORY_FIRST_CONVERSATION_ITEM = "(//div[@class='group relative flex items-center justify-between gap-2 pl-4 pr-3 py-2 rounded-xl border transition-all duration-200 cursor-pointer rim-light bg-zinc-900/50 border-white/5 hover:border-white/15 hover:bg-zinc-900'])[1]";

	public boolean ValidateHistoryRestoredAfterReopen() {
		try {
			// 1) Switch to Analyze mode.
			PWActions.waitFor(ANALYSE_TAB, "Wait for Analyze tab", 30000);
			PWActions.click(ANALYSE_TAB, "Clicked on Analyze tab");

			// 2) Open the Select video panel and pick "VI_35_Emotions".
			PWActions.waitFor(SELECT_VIDEO_TAB, "Wait for Select video tab", 30000);
			PWActions.click(SELECT_VIDEO_TAB, "Clicked on Select video");
			PWActions.waitFor(VIDEO_BY_NAME_VI_35_EMOTIONS, "Wait for 'VI_35_Emotions' video", 30000);
			PWActions.click(VIDEO_BY_NAME_VI_35_EMOTIONS, "Selected 'VI_35_Emotions' video");

			// 3) Confirm the selection (Done).
			PWActions.waitFor(SELECT_VIDEO_DONE_BUTTON, "Wait for Done button", 30000);
			PWActions.click(SELECT_VIDEO_DONE_BUTTON, "Clicked on Done");

			// 4) Fire the Title query -> Send -> wait 6s, then the Settings query -> Send -> wait 5s.
			PWActions.waitFor(TITLE_QUERY_BUTTON, "Wait for Title query", 30000);
			PWActions.click(TITLE_QUERY_BUTTON, "Clicked on Title query");
			PWActions.waitFor(SEND_BUTTON, "Wait for Send button", 30000);
			PWActions.click(SEND_BUTTON, "Clicked on Send (Title query)");
			Thread.sleep(6000);

			PWActions.waitFor(SETTING_QUERY_BUTTON, "Wait for Settings query", 30000);
			PWActions.click(SETTING_QUERY_BUTTON, "Clicked on Settings query");
			PWActions.waitFor(SEND_BUTTON, "Wait for Send button", 30000);
			PWActions.click(SEND_BUTTON, "Clicked on Send (Settings query)");
			Thread.sleep(5000);

			// 5) Read the whole results container and store it as the original history.
			PWActions.waitFor(CONVERSATION_RESULT_CONTAINER, "Wait for results container", 90000);
			String rawHistory = page.locator(CONVERSATION_RESULT_CONTAINER).first().textContent();
			String historyOne = rawHistory == null ? "" : rawHistory.trim();
			System.out.println("📜 Captured conversation history (length " + historyOne.length() + ")");

			// 6) Start a brand new conversation (clears the current results).
			PWActions.waitFor(START_NEW_CONVERSATION_BUTTON, "Wait for Start new conversation button", 30000);
			PWActions.click(START_NEW_CONVERSATION_BUTTON, "Clicked on Start new conversation");

			// 7) Refresh the page.
			page.reload();

			// 8) Open the first conversation from History.
			PWActions.waitFor(HISTORY_FIRST_CONVERSATION_ITEM, "Wait for first History item", 30000);
			PWActions.click(HISTORY_FIRST_CONVERSATION_ITEM, "Clicked on first History item");

			// 9) PASS only if the results container appears again after re-opening the conversation.
			boolean appeared;
			try {
				page.locator(CONVERSATION_RESULT_CONTAINER).first()
						.waitFor(new com.microsoft.playwright.Locator.WaitForOptions().setTimeout(30000));
				appeared = page.locator(CONVERSATION_RESULT_CONTAINER).count() > 0;
			} catch (Exception ignore) {
				appeared = false;
			}

			if (appeared) {
				System.out.println("✅ TEST PASSED - Previous conversation history is displayed after re-opening");
				return true;
			}

			String errorMsg = "❌ TEST FAILED - Conversation history did not appear after re-opening from History";
			System.out.println(errorMsg);
			PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
			return false;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// TC_85 Validate the Embed graph: select a video in Embed mode and compare the rendered chart against a
	// baseline image stored in Excel (qc_images). Embed -> Source = Videos -> select <videoName> -> Done ->
	//   wait for the graph -> screenshot the chart -> compare with data/images/<expectedImageName>.
	//   PASS only if the image similarity is >= IMAGE_SIMILARITY_THRESHOLD.
	private static final String EMBED_TAB = "//button[contains(@class,'flex-1') and contains(normalize-space(.),'Embed')]";
	// The embed graph is rendered on a <canvas> (no per-dot DOM), so we screenshot the canvas and compare
	// it to the baseline image. The canvas is stretched by CSS, so its on-screen size (~1011x502) matches
	// the saved baseline even though its drawing-buffer attributes are smaller.
	private static final String EMBED_CHART_CONTAINER = "//canvas[contains(@class,'cursor-grab')]";
	private static final double IMAGE_SIMILARITY_THRESHOLD = 0.95;

	// TC_86 Embed source = Collections (not Videos): the source toggle is selected by its label text so it
	// works whether or not it is already active.
	private static final String EMBED_SOURCE_COLLECTIONS = "//button[normalize-space(.)='Collections']";

	public boolean ValidateEmbedGraphMatchesImage(String videoName, String expectedImageName) {
		try {
			String videoByName = "//div[text()='" + videoName + "']";
			expectedImageName = resolveBaselineName(expectedImageName, videoName);

			// 1) Switch to Embed mode.
			PWActions.waitFor(EMBED_TAB, "Wait for Embed tab", 30000);
			PWActions.click(EMBED_TAB, "Clicked on Embed tab");

			// 2) Open the Select video panel and pick the video.
			PWActions.waitFor(SELECT_VIDEO_TAB, "Wait for Select video tab", 30000);
			PWActions.click(SELECT_VIDEO_TAB, "Clicked on Select video");
			PWActions.waitFor(videoByName, "Wait for '" + videoName + "' video", 30000);
			PWActions.click(videoByName, "Selected '" + videoName + "' video");

			// 3) Confirm the selection (Done).
			PWActions.waitFor(SELECT_VIDEO_DONE_BUTTON, "Wait for Done button", 30000);
			PWActions.click(SELECT_VIDEO_DONE_BUTTON, "Clicked on Done");

			// 4) Screenshot the rendered graph and compare it to the baseline image.
			return compareEmbedGraphToBaseline(expectedImageName);

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// TC_86 Embed mode -> source = Collections -> Select -> pick the collection -> Done -> compare the
	// rendered graph against the baseline image (data/images/<expectedImageName>).
	public boolean ValidateEmbedCollectionGraphMatchesImage(String collectionName, String expectedImageName) {
		try {
			String collectionByName = "//div[text()='" + collectionName + "']";
			expectedImageName = resolveBaselineName(expectedImageName, collectionName);

			// 1) Switch to Embed mode.
			PWActions.waitFor(EMBED_TAB, "Wait for Embed tab", 30000);
			PWActions.click(EMBED_TAB, "Clicked on Embed tab");

			// 2) Switch the source from Videos to Collections.
			PWActions.waitFor(EMBED_SOURCE_COLLECTIONS, "Wait for Collections source", 30000);
			PWActions.click(EMBED_SOURCE_COLLECTIONS, "Selected Collections source");

			// 3) Open the Select panel and pick the collection.
			PWActions.waitFor(SELECT_VIDEO_TAB, "Wait for Select collection tab", 30000);
			PWActions.click(SELECT_VIDEO_TAB, "Clicked on Select collection");
			PWActions.waitFor(collectionByName, "Wait for '" + collectionName + "' collection", 30000);
			PWActions.click(collectionByName, "Selected '" + collectionName + "' collection");

			// 4) Confirm the selection (Done).
			PWActions.waitFor(SELECT_VIDEO_DONE_BUTTON, "Wait for Done button", 30000);
			PWActions.click(SELECT_VIDEO_DONE_BUTTON, "Clicked on Done");

			// 5) Screenshot the rendered graph and compare it to the baseline image.
			return compareEmbedGraphToBaseline(expectedImageName);

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// Falls back to "<source>_embed.png" when the Excel qc_images cell is blank for the TCID, so a missing
	// or mis-placed Excel value does not turn into a confusing file-write error.
	private String resolveBaselineName(String expectedImageName, String source) {
		if (expectedImageName == null || expectedImageName.trim().isEmpty()) {
			String fallback = source + "_embed.png";
			System.out.println("ℹ️ qc_images was blank - falling back to baseline '" + fallback + "'");
			return fallback;
		}
		return expectedImageName.trim();
	}

	// Shared tail for the Embed image tests: wait for the canvas to render and settle, screenshot it to
	// data/images/actual/, then compare with data/images/<expectedImageName>. PASS only if the image
	// similarity is >= IMAGE_SIMILARITY_THRESHOLD.
	private boolean compareEmbedGraphToBaseline(String expectedImageName) throws InterruptedException {
		// 1) Wait for the graph to render, then let it settle.
		PWActions.waitFor(EMBED_CHART_CONTAINER, "Wait for embed graph", 60000);
		Thread.sleep(3000);

		// 2) Make sure the baseline image exists.
		String expectedPath = "data" + File.separator + "images" + File.separator + expectedImageName;
		if (!new File(expectedPath).exists()) {
			String errorMsg = "❌ TEST FAILED - Baseline image not found: " + expectedPath;
			System.out.println(errorMsg);
			PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
			return false;
		}

		// 3) Screenshot the chart into data/images/actual/.
		File actualDir = new File("data" + File.separator + "images" + File.separator + "actual");
		if (!actualDir.exists() && !actualDir.mkdirs()) {
			String errorMsg = "❌ TEST FAILED - Could not create output folder: " + actualDir.getPath();
			System.out.println(errorMsg);
			PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
			return false;
		}
		String actualPath = actualDir.getPath() + File.separator + expectedImageName;
		page.locator(EMBED_CHART_CONTAINER).first()
				.screenshot(new Locator.ScreenshotOptions().setPath(Paths.get(actualPath)));
		System.out.println("🖼️ Saved actual graph screenshot: " + actualPath);

		// 4) Compare against the baseline.
		double similarity = ImageCompareUtil.getSimilarity(expectedPath, actualPath);
		System.out.println("🖼️ Image similarity: " + similarity + " (threshold " + IMAGE_SIMILARITY_THRESHOLD + ")");

		if (similarity >= IMAGE_SIMILARITY_THRESHOLD) {
			System.out.println("✅ TEST PASSED - Embed graph matches the baseline image");
			return true;
		}

		String errorMsg = "❌ TEST FAILED - Embed graph did not match the baseline image (similarity " + similarity
				+ " < " + IMAGE_SIMILARITY_THRESHOLD + "). Actual saved at " + actualPath;
		System.out.println(errorMsg);
		PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
		return false;
	}

	// TC_87 Click a plotted point on the Embed graph and maximize its video preview.
	// Embed -> Select video VI_35_Emotions -> Done -> click a point on the canvas -> the "Selected
	//   embedding" panel (with that point's video) opens -> click Maximize -> PASS only if the maximized
	//   video view appears.
	private static final String EMBED_SELECTED_PANEL = "//div[normalize-space(text())='Selected embedding']";
	private static final String EMBED_POINT_MAXIMIZE_BUTTON = "//button[@class='ml-auto p-1.5 rounded-md hover:bg-white/10 transition-colors cursor-pointer']";
	private static final String EMBED_MAXIMIZED_VIEW = "//div[@class='relative w-full max-w-[min(960px,calc((100vh-10rem)*16/9))] overflow-hidden rounded-2xl border border-white/15 bg-zinc-900 shadow-2xl']";

	public boolean ValidateEmbedPointClickAndMaximize(String videoName) {
		try {
			String videoByName = "//div[text()='" + videoName + "']";

			// 1) Switch to Embed mode.
			PWActions.waitFor(EMBED_TAB, "Wait for Embed tab", 30000);
			PWActions.click(EMBED_TAB, "Clicked on Embed tab");

			// 2) Open the Select video panel and pick the video.
			PWActions.waitFor(SELECT_VIDEO_TAB, "Wait for Select video tab", 30000);
			PWActions.click(SELECT_VIDEO_TAB, "Clicked on Select video");
			PWActions.waitFor(videoByName, "Wait for '" + videoName + "' video", 30000);
			PWActions.click(videoByName, "Selected '" + videoName + "' video");

			// 3) Confirm the selection (Done).
			PWActions.waitFor(SELECT_VIDEO_DONE_BUTTON, "Wait for Done button", 30000);
			PWActions.click(SELECT_VIDEO_DONE_BUTTON, "Clicked on Done");

			// 4) Wait for the graph to render and settle. The dots are painted on a <canvas> (no DOM per
			//   point), so we scan the canvas pixels for a coloured (non-background) dot and click it. This
			//   works at any canvas size, unlike a hard-coded pixel.
			PWActions.waitFor(EMBED_CHART_CONTAINER, "Wait for embed graph", 60000);
			Thread.sleep(3000);

			Object found = page.locator(EMBED_CHART_CONTAINER).first().evaluate(
					"c => { const ctx = c.getContext('2d'); const w = c.width, h = c.height;"
							+ " const d = ctx.getImageData(0, 0, w, h).data; const rect = c.getBoundingClientRect();"
							+ " for (let y = 0; y < h; y++) { for (let x = 0; x < w; x++) { const i = (y * w + x) * 4;"
							+ " const r = d[i], g = d[i + 1], b = d[i + 2], a = d[i + 3]; if (a < 200) continue;"
							+ " const mx = Math.max(r, g, b), mn = Math.min(r, g, b);"
							+ " if (mx > 120 && (mx - mn) > 60) { return { x: x * rect.width / w, y: y * rect.height / h }; }"
							+ " } } return null; }");

			if (found == null) {
				String errorMsg = "❌ TEST FAILED - No plotted point was found on the embed canvas to click";
				System.out.println(errorMsg);
				PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
				return false;
			}

			@SuppressWarnings("unchecked")
			Map<String, Object> point = (Map<String, Object>) found;
			double pointX = ((Number) point.get("x")).doubleValue();
			double pointY = ((Number) point.get("y")).doubleValue();
			page.locator(EMBED_CHART_CONTAINER).first()
					.click(new Locator.ClickOptions().setPosition(pointX, pointY));
			System.out.println("🖱️ Clicked a plotted point at (" + pointX + ", " + pointY + ")");

			// 5) The "Selected embedding" panel with the point's video should open.
			PWActions.waitFor(EMBED_SELECTED_PANEL, "Wait for 'Selected embedding' panel", 30000);

			// 6) Click Maximize on the preview.
			PWActions.waitFor(EMBED_POINT_MAXIMIZE_BUTTON, "Wait for Maximize button", 30000);
			PWActions.click(EMBED_POINT_MAXIMIZE_BUTTON, "Clicked on Maximize");

			// 7) PASS only if the maximized video view appears.
			boolean maximized;
			try {
				page.locator(EMBED_MAXIMIZED_VIEW).first()
						.waitFor(new com.microsoft.playwright.Locator.WaitForOptions().setTimeout(30000));
				maximized = page.locator(EMBED_MAXIMIZED_VIEW).count() > 0;
			} catch (Exception ignore) {
				maximized = false;
			}

			if (maximized) {
				System.out.println("✅ TEST PASSED - Point video opened and maximized");
				return true;
			}

			String errorMsg = "❌ TEST FAILED - Maximized video view did not appear after clicking Maximize";
			System.out.println(errorMsg);
			PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
			return false;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// TC_88 Changing the conversation source mid-chat shows the "source selection changed" warning.
	// Select video All_queries_03 -> Done -> Detect Violence -> Send -> switch source to Collections ->
	//   open Select -> pick Automation_collection -> PASS only if the yellow warning
	//   "Source selection changed — sending a message will start a new conversation." is displayed.
	private static final String SOURCE_CHANGED_WARNING = "//div[@class='text-center text-[12px] font-medium text-yellow-200 bg-yellow-500/10 border border-yellow-500/30 rounded-lg px-4 py-2']";

	public boolean ValidateSourceChangeWarning() {
		try {
			// 1) Select a video and confirm (start point of the conversation).
			PWActions.waitFor(SELECT_VIDEO_TAB, "Wait for Select video tab", 30000);
			PWActions.click(SELECT_VIDEO_TAB, "Clicked on Select video");
			PWActions.waitFor(VIDEO_BY_NAME_ALL_QUERIES_03, "Wait for 'All_queries_03' video", 30000);
			PWActions.click(VIDEO_BY_NAME_ALL_QUERIES_03, "Selected 'All_queries_03' video");
			PWActions.waitFor(SELECT_VIDEO_DONE_BUTTON, "Wait for Done button", 30000);
			PWActions.click(SELECT_VIDEO_DONE_BUTTON, "Clicked on Done");

			// 2) Fire the Detect Violence query and send it (creates an active conversation).
			PWActions.waitFor(DETECT_VIOLENCE_BUTTON, "Wait for Detect Violence query", 30000);
			PWActions.click(DETECT_VIOLENCE_BUTTON, "Clicked on Detect Violence");
			PWActions.waitFor(SEND_BUTTON, "Wait for Send button", 30000);
			PWActions.click(SEND_BUTTON, "Clicked on Send");
			Thread.sleep(2000);

			// 3) Change the source to Collections mid-conversation.
			PWActions.waitFor(COLLECTION_SOURCE_TOGGLE, "Wait for Collections source toggle", 30000);
			PWActions.click(COLLECTION_SOURCE_TOGGLE, "Clicked on Collections source");

			// 4) Open the picker, select a collection, and confirm (Done).
			PWActions.waitFor(SELECT_VIDEO_TAB, "Wait for Select collection tab", 30000);
			PWActions.click(SELECT_VIDEO_TAB, "Clicked on Select collection");
			PWActions.waitFor(COLLECTION_BY_NAME_AUTOMATION, "Wait for 'Automation_collection'", 30000);
			PWActions.click(COLLECTION_BY_NAME_AUTOMATION, "Selected 'Automation_collection'");
			PWActions.waitFor(SELECT_VIDEO_DONE_BUTTON, "Wait for Done button", 30000);
			PWActions.click(SELECT_VIDEO_DONE_BUTTON, "Clicked on Done");

			// 5) PASS only if the "source selection changed" warning is displayed.
			boolean shown;
			try {
				page.locator(SOURCE_CHANGED_WARNING).first()
						.waitFor(new com.microsoft.playwright.Locator.WaitForOptions().setTimeout(30000));
				shown = page.locator(SOURCE_CHANGED_WARNING).count() > 0;
			} catch (Exception ignore) {
				shown = false;
			}

			if (shown) {
				String warningText = page.locator(SOURCE_CHANGED_WARNING).first().textContent();
				System.out.println("✅ TEST PASSED - Source change warning displayed: '"
						+ (warningText == null ? "" : warningText.trim()) + "'");
				return true;
			}

			String errorMsg = "❌ TEST FAILED - Source change warning was not displayed after changing the source";
			System.out.println(errorMsg);
			PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
			return false;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// TC_89 Starting a fresh conversation clears the current result block.
	// Select video All_queries_03 -> Done -> Detect Violence -> Send -> the result block is visible ->
	//   click "Start new conversation" -> PASS only if the result block is no longer displayed.
	private static final String CONVERSATION_RESULT_BLOCK = "//div[@class='rounded-2xl border border-white/10 bg-zinc-900/40 px-4 py-3 rim-light']";

	public boolean ValidateFreshConversationClearsResult() {
		try {
			// 1) Select a video and confirm.
			PWActions.waitFor(SELECT_VIDEO_TAB, "Wait for Select video tab", 30000);
			PWActions.click(SELECT_VIDEO_TAB, "Clicked on Select video");
			PWActions.waitFor(VIDEO_BY_NAME_ALL_QUERIES_03, "Wait for 'All_queries_03' video", 30000);
			PWActions.click(VIDEO_BY_NAME_ALL_QUERIES_03, "Selected 'All_queries_03' video");
			PWActions.waitFor(SELECT_VIDEO_DONE_BUTTON, "Wait for Done button", 30000);
			PWActions.click(SELECT_VIDEO_DONE_BUTTON, "Clicked on Done");

			// 2) Fire the Detect Violence query and send it.
			PWActions.waitFor(DETECT_VIOLENCE_BUTTON, "Wait for Detect Violence query", 30000);
			PWActions.click(DETECT_VIOLENCE_BUTTON, "Clicked on Detect Violence");
			PWActions.waitFor(SEND_BUTTON, "Wait for Send button", 30000);
			PWActions.click(SEND_BUTTON, "Clicked on Send");

			// 3) The result block should now be visible.
			boolean visibleAfterSend;
			try {
				page.locator(CONVERSATION_RESULT_BLOCK).first()
						.waitFor(new com.microsoft.playwright.Locator.WaitForOptions().setTimeout(60000));
				visibleAfterSend = page.locator(CONVERSATION_RESULT_BLOCK).first().isVisible();
			} catch (Exception ignore) {
				visibleAfterSend = false;
			}
			if (!visibleAfterSend) {
				String errorMsg = "❌ TEST FAILED - Result block was not displayed after sending the query";
				System.out.println(errorMsg);
				PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
				return false;
			}
			System.out.println("✅ Result block is visible after sending the query");

			// 4) Start a brand new conversation.
			PWActions.waitFor(START_NEW_CONVERSATION_BUTTON, "Wait for Start new conversation button", 30000);
			PWActions.click(START_NEW_CONVERSATION_BUTTON, "Clicked on Start new conversation");

			// 5) PASS only if the result block is no longer displayed.
			boolean hiddenAfterFresh;
			try {
				page.locator(CONVERSATION_RESULT_BLOCK).first()
						.waitFor(new com.microsoft.playwright.Locator.WaitForOptions().setTimeout(30000)
								.setState(com.microsoft.playwright.options.WaitForSelectorState.HIDDEN));
				hiddenAfterFresh = true;
			} catch (Exception ignore) {
				hiddenAfterFresh = page.locator(CONVERSATION_RESULT_BLOCK).count() == 0;
			}

			if (hiddenAfterFresh) {
				System.out.println("✅ TEST PASSED - Result block is cleared after starting a fresh conversation");
				return true;
			}

			String errorMsg = "❌ TEST FAILED - Result block is still displayed after starting a fresh conversation";
			System.out.println(errorMsg);
			PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
			return false;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// TC_90 Invite a user to "Automation Team", accept the invite from Gmail in a SEPARATE Edge browser,
	// and confirm it lands on the dashboard.
	// Settings -> open Automation Team -> Add user -> enter <inviteEmail> -> Send invite. Then, in a brand
	//   new Edge browser (independent of PWBaseTest), open Gmail -> open the invite email -> click
	//   "Join Automation Team" -> PASS only if the dashboard image (//img[@class='h-[97%]']) appears.
	private static final String JOIN_TEAM_EMAIL_SUBJECT = "(//span[@class='bqe' and contains(text(),\"You've been invited to join Automation Team on Mikshi\")])[2]";
	private static final String JOIN_TEAM_LINK = "//a[normalize-space()='Join Automation Team']";
	private static final String DASHBOARD_IMAGE = "//img[@class='h-[97%]']";
	// Member-side: Settings link, the Automation Team card, and the "vision ai" member row (Member badge).
	private static final String MEMBER_SETTINGS_LINK = "//a[@aria-label='Settings' and @href='/app/settings']";
	private static final String MEMBER_AUTOMATION_TEAM = "//button[.//div[normalize-space()='Automation Team']]";
	private static final String TEAM_MEMBER_VISION_AI = "//div[contains(@class,'flex items-center justify-between') and .//div[normalize-space()='vision ai'] and .//span[normalize-space()='Member']]";
	// Gmail delete (trash) button for the opened invitation email.
	private static final String GMAIL_DELETE_BUTTON = "(//div[contains(@class,'T-I') and contains(@class,'nX') and @act='10'])[2]";

	public boolean ValidateTeamInviteEmailAndJoin(String inviteEmail) {
		com.microsoft.playwright.Playwright edgePlaywright = null;
		com.microsoft.playwright.BrowserContext edgeContext = null;
		try {
			// ---- Part A: send the invite in the main (Chrome) browser ----
			// 1) Open the "Automation Team" card.
			PWActions.waitFor(CREATED_TEAM_CARD, "Wait for Automation Team card", 30000);
			PWActions.click(CREATED_TEAM_CARD, "Clicked on Automation Team");

			// 2) Open the Add user form.
			PWActions.waitFor(ADD_USER_BUTTON, "Wait for Add user button", 30000);
			PWActions.click(ADD_USER_BUTTON, "Clicked on Add user");

			// 3) Enter the invite email and send the invite.
			PWActions.waitFor(INVITE_EMAIL_INPUT, "Wait for invite email input", 30000);
			PWActions.fill(INVITE_EMAIL_INPUT, inviteEmail, "Entered invite email '" + inviteEmail + "'");
			PWActions.click(SEND_INVITE_BUTTON, "Clicked on Send invite");
			System.out.println("📨 Invite sent to " + inviteEmail);

			// ---- Part B: accept the invite from Gmail in a SEPARATE Edge browser ----
			// Launched here (not via PWBaseTest) using the user's REAL Edge profile ("Default" under Edge's
			// "User Data"), so the already-signed-in Gmail session (avision185@gmail.com) is reused and we do
			// NOT hit the Google sign-in page. NOTE: all Edge windows must be closed first (the profile is
			// locked while Edge is running). If the account lives in a different profile, change
			// "--profile-directory=Default" to that profile folder name (e.g. "Profile 1").
			String edgeUserData = System.getenv("LOCALAPPDATA") + "\\Microsoft\\Edge\\User Data";
			edgePlaywright = com.microsoft.playwright.Playwright.create();
			edgeContext = edgePlaywright.chromium().launchPersistentContext(
					java.nio.file.Paths.get(edgeUserData),
					new com.microsoft.playwright.BrowserType.LaunchPersistentContextOptions()
							.setChannel("msedge")
							.setHeadless(false)
							.setIgnoreHTTPSErrors(true)
							.setViewportSize(null)
							.setArgs(java.util.Arrays.asList("--profile-directory=Default", "--start-maximized")));
			// First open the application so the already-logged-in session is active. The "Join" link from
			// the email lands on the app, so it must be signed in here for the dashboard to appear.
			Page appPage = edgeContext.pages().isEmpty() ? edgeContext.newPage() : edgeContext.pages().get(0);
			appPage.navigate("https://dev.vision.mikshi.ai/app/");
			appPage.waitForLoadState();
			Thread.sleep(3000);
			System.out.println("🌐 Opened the application (should be signed in via the Edge profile)");

			// Then open Gmail in a new tab.
			Page gmailPage = edgeContext.newPage();
			gmailPage.navigate("https://mail.google.com/mail/u/0/#inbox");

			// 4) Wait for the invite email to arrive (reload the inbox until it shows, max 2 min).
			boolean emailFound = false;
			long deadline = System.currentTimeMillis() + 120000;
			while (System.currentTimeMillis() < deadline) {
				if (gmailPage.locator(JOIN_TEAM_EMAIL_SUBJECT).count() > 0) {
					emailFound = true;
					break;
				}
				Thread.sleep(5000);
				gmailPage.navigate("https://mail.google.com/mail/u/0/#inbox");
			}
			if (!emailFound) {
				String errorMsg = "❌ TEST FAILED - Invite email did not arrive in Gmail within 2 minutes";
				System.out.println(errorMsg);
				PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
				return false;
			}

			// 5) Open the invite email.
			gmailPage.locator(JOIN_TEAM_EMAIL_SUBJECT).first().click();

			// 6) Click "Join Automation Team" (usually opens the app in a new tab).
			gmailPage.locator(JOIN_TEAM_LINK).first()
					.waitFor(new com.microsoft.playwright.Locator.WaitForOptions().setTimeout(30000));
			Page joinPage;
			try {
				joinPage = edgeContext.waitForPage(
						new com.microsoft.playwright.BrowserContext.WaitForPageOptions().setTimeout(20000),
						() -> gmailPage.locator(JOIN_TEAM_LINK).first().click());
			} catch (Exception openedInSameTab) {
				joinPage = gmailPage;
			}
			joinPage.waitForLoadState();

			// 7) Wait for the dashboard (user landed in the app), then refresh twice so the new membership
			//   syncs into the UI.
			try {
				joinPage.locator(DASHBOARD_IMAGE).first()
						.waitFor(new com.microsoft.playwright.Locator.WaitForOptions().setTimeout(60000));
				System.out.println("🌐 Landed on the dashboard after joining");
			} catch (Exception ignore) {
				System.out.println("⚠️ Dashboard image not seen; continuing to verify membership anyway");
			}
			joinPage.reload();
			joinPage.waitForLoadState();
			Thread.sleep(2000);
			joinPage.reload();
			joinPage.waitForLoadState();
			Thread.sleep(2000);

			// 8) Open Settings, then the Automation Team, and validate "vision ai" appears as a Member.
			joinPage.locator(MEMBER_SETTINGS_LINK).first()
					.waitFor(new com.microsoft.playwright.Locator.WaitForOptions().setTimeout(30000));
			joinPage.locator(MEMBER_SETTINGS_LINK).first().click();

			joinPage.locator(MEMBER_AUTOMATION_TEAM).first()
					.waitFor(new com.microsoft.playwright.Locator.WaitForOptions().setTimeout(30000));
			joinPage.locator(MEMBER_AUTOMATION_TEAM).first().click();

			// Refresh at least twice so the member list is up to date before validating.
			joinPage.reload();
			joinPage.waitForLoadState();
			Thread.sleep(2000);
			joinPage.reload();
			joinPage.waitForLoadState();
			Thread.sleep(2000);

			boolean memberVisible;
			try {
				joinPage.locator(TEAM_MEMBER_VISION_AI).first()
						.waitFor(new com.microsoft.playwright.Locator.WaitForOptions().setTimeout(30000));
				memberVisible = joinPage.locator(TEAM_MEMBER_VISION_AI).first().isVisible();
			} catch (Exception ignore) {
				memberVisible = false;
			}

			// After validating, go back to Gmail and delete the invitation email (best-effort cleanup - does
			// not affect the pass/fail result, which is decided by the member-row visibility above).
			try {
				gmailPage.bringToFront();
				gmailPage.locator(GMAIL_DELETE_BUTTON).first()
						.waitFor(new com.microsoft.playwright.Locator.WaitForOptions().setTimeout(15000));
				gmailPage.locator(GMAIL_DELETE_BUTTON).first().click();
				System.out.println("🗑️ Deleted the invitation email from Gmail");
			} catch (Exception delErr) {
				System.out.println("⚠️ Could not delete the invitation email: " + delErr.getMessage());
			}

			if (memberVisible) {
				System.out.println("✅ TEST PASSED - 'vision ai' is shown as a Member of Automation Team");
				return true;
			}

			String errorMsg = "❌ TEST FAILED - 'vision ai' Member row was not visible in Automation Team";
			System.out.println(errorMsg);
			PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
			return false;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		} finally {
			if (edgeContext != null) {
				try {
					edgeContext.close();
				} catch (Exception ignore) {
				}
			}
			if (edgePlaywright != null) {
				try {
					edgePlaywright.close();
				} catch (Exception ignore) {
				}
			}
		}
	}

	// TC_91 A member leaves "Automation Team" and the team is no longer visible.
	// (Runs entirely in a separate Edge browser as avison185.) Open app -> Profile -> Switch -> Automation
	//   Team -> Settings -> Automation Team -> Leave group -> Confirm -> refresh -> PASS only if Automation
	//   Team is NO LONGER visible.
	private static final String PROFILE_TAB = "//button[@class='w-[40px] h-[40px] inline-flex cursor-pointer items-center justify-center rounded-lg p-2 text-muted-foreground hover:bg-accent  hover:text-foreground transition-all rim-light hover:shadow-depth-1 relative hover:bg-white/5']";
	private static final String SWITCH_TAB = "//button[@class='flex items-center gap-1 px-3 py-1.5 rounded-lg border border-white/10 text-xs font-medium text-zinc-200 hover:text-white hover:bg-white/5 hover:border-white/25 hover:shadow-depth-1 transition-all cursor-pointer focus:outline-none focus:ring-1 focus:ring-white/30 shrink-0']";
	private static final String SWITCH_AUTOMATION_TEAM = "//button[.//p[text()='Automation Team']]";
	private static final String LEAVE_GROUP_BUTTON = "//button[@class='flex items-center gap-2 px-4 py-2 rounded-xl bg-zinc-900 text-white text-sm font-medium hover:bg-zinc-800 transition-all border border-white/10 cursor-pointer disabled:opacity-50 disabled:cursor-not-allowed']";
	private static final String LEAVE_GROUP_CONFIRM = "//button[@class='flex cursor-pointer items-center gap-2 px-5 py-2 rounded-lg bg-red-500 text-white text-sm font-medium hover:bg-red-600 transition-all shadow-[0_0_20px_rgba(239,68,68,0.2)] disabled:opacity-40 disabled:cursor-not-allowed']";

	public boolean ValidateMemberLeaveTeam() {
		com.microsoft.playwright.Playwright edgePlaywright = null;
		com.microsoft.playwright.BrowserContext edgeContext = null;
		try {
			// Launch Edge with the member's real Default profile (signed in as avison185), maximized.
			String edgeUserData = System.getenv("LOCALAPPDATA") + "\\Microsoft\\Edge\\User Data";
			edgePlaywright = com.microsoft.playwright.Playwright.create();
			edgeContext = edgePlaywright.chromium().launchPersistentContext(
					java.nio.file.Paths.get(edgeUserData),
					new com.microsoft.playwright.BrowserType.LaunchPersistentContextOptions()
							.setChannel("msedge")
							.setHeadless(false)
							.setIgnoreHTTPSErrors(true)
							.setViewportSize(null)
							.setArgs(java.util.Arrays.asList("--profile-directory=Default", "--start-maximized")));
			Page appPage = edgeContext.pages().isEmpty() ? edgeContext.newPage() : edgeContext.pages().get(0);
			appPage.navigate("https://dev.vision.mikshi.ai/app/");
			appPage.waitForLoadState();
			Thread.sleep(3000);

			// 1) Profile -> Switch -> Automation Team (switch into the team's workspace).
			edgeClick(appPage, PROFILE_TAB, "Clicked on Profile");
			edgeClick(appPage, SWITCH_TAB, "Clicked on Switch");
			edgeClick(appPage, SWITCH_AUTOMATION_TEAM, "Switched to Automation Team");

			// 2) Settings -> Automation Team (open the team).
			edgeClick(appPage, MEMBER_SETTINGS_LINK, "Clicked on Settings");
			edgeClick(appPage, MEMBER_AUTOMATION_TEAM, "Opened Automation Team");

			// 3) Leave the group and confirm.
			edgeClick(appPage, LEAVE_GROUP_BUTTON, "Clicked on Leave group");
			edgeClick(appPage, LEAVE_GROUP_CONFIRM, "Confirmed Leave group");

			// 4) Refresh (twice) so the change syncs, then re-open Settings to inspect the team list.
			appPage.reload();
			appPage.waitForLoadState();
			Thread.sleep(2000);
			appPage.reload();
			appPage.waitForLoadState();
			Thread.sleep(2000);
			edgeClick(appPage, MEMBER_SETTINGS_LINK, "Clicked on Settings to verify");
			Thread.sleep(2000);

			// 5) PASS only if Automation Team is NO LONGER visible (the member left it).
			boolean teamVisible = appPage.locator(MEMBER_AUTOMATION_TEAM).count() > 0
					&& appPage.locator(MEMBER_AUTOMATION_TEAM).first().isVisible();

			if (!teamVisible) {
				System.out.println("✅ TEST PASSED - Automation Team is no longer visible after leaving");
				return true;
			}

			String errorMsg = "❌ TEST FAILED - Automation Team is still visible after leaving the group";
			System.out.println(errorMsg);
			PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
			return false;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		} finally {
			if (edgeContext != null) {
				try {
					edgeContext.close();
				} catch (Exception ignore) {
				}
			}
			if (edgePlaywright != null) {
				try {
					edgePlaywright.close();
				} catch (Exception ignore) {
				}
			}
		}
	}

	// Helper: wait for and click a selector on a specific (non-framework) Playwright page, e.g. the Edge page.
	private void edgeClick(Page targetPage, String selector, String description) {
		targetPage.locator(selector).first()
				.waitFor(new com.microsoft.playwright.Locator.WaitForOptions().setTimeout(30000));
		targetPage.locator(selector).first().click();
		System.out.println("➡️ " + description);
	}

	// TC_00 One-time Google sign-in so the persistent profile holds an authenticated session for the suite.
	// Run TC_00 (priority 0) together with any test (e.g. TC_56); TC_00 signs in first and the persistent
	//   profile keeps the session, so later tests open already authenticated. Skips automatically when the
	//   profile is already logged in (the "Continue with Google" button won't be present).
	private static final String CONTINUE_WITH_GOOGLE_BTN = "//button[@class='mt-10 w-full h-12 inline-flex items-center justify-center gap-3 rounded-xl bg-white text-black font-medium hover:bg-zinc-100 transition-all shadow-depth-2 disabled:opacity-60 disabled:cursor-not-allowed cursor-pointer']";
	private static final String GOOGLE_EMAIL_INPUT = "(//input[@class='whsOnd zHQkBf'])[1]";
	private static final String GOOGLE_PASSWORD_INPUT = "(//input[@class='whsOnd zHQkBf'])[1]";
	private static final String GOOGLE_NEXT_BUTTON = "(//div[@class='VfPpkd-RLmnJb'])[2]";

	public boolean LoginWithGoogle(String email, String password) {
		try {
			// 0) If the profile is already authenticated, the "Continue with Google" button isn't shown -> skip.
			boolean needLogin;
			try {
				page.locator(CONTINUE_WITH_GOOGLE_BTN).first()
						.waitFor(new com.microsoft.playwright.Locator.WaitForOptions().setTimeout(8000));
				needLogin = true;
			} catch (Exception alreadyAuthenticated) {
				needLogin = false;
			}
			if (!needLogin) {
				System.out.println("✅ Already authenticated (persistent session) - skipping Google login");
				return true;
			}

			// Credentials must be configured (set GOOGLE_EMAIL / GOOGLE_PASSWORD as env vars, esp. in Jenkins).
			if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
				String errorMsg = "❌ GOOGLE_EMAIL / GOOGLE_PASSWORD not set - cannot perform Google login";
				System.out.println(errorMsg);
				PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
				return false;
			}

			// 1) Start the Google OAuth flow.
			PWActions.click(CONTINUE_WITH_GOOGLE_BTN, "Clicked Continue with Google");

			// 2) Enter the email and continue.
			PWActions.waitFor(GOOGLE_EMAIL_INPUT, "Wait for Google email field", 30000);
			PWActions.fill(GOOGLE_EMAIL_INPUT, email, "Entered Google email");
			PWActions.waitFor(GOOGLE_NEXT_BUTTON, "Wait for Next (email)", 30000);
			PWActions.click(GOOGLE_NEXT_BUTTON, "Clicked Next (email)");

			// 3) Enter the password and continue.
			PWActions.waitFor(GOOGLE_PASSWORD_INPUT, "Wait for Google password field", 30000);
			Thread.sleep(1500); // let the password screen settle in
			PWActions.fill(GOOGLE_PASSWORD_INPUT, password, "Entered Google password");
			PWActions.waitFor(GOOGLE_NEXT_BUTTON, "Wait for Next (password)", 30000);
			PWActions.click(GOOGLE_NEXT_BUTTON, "Clicked Next (password)");

			// 4) Wait to be redirected back to the app, signed in (poll up to 60s for a non-login app URL).
			long deadline = System.currentTimeMillis() + 60000;
			while (System.currentTimeMillis() < deadline) {
				String url = page.url();
				if (url.contains("dev.vision.mikshi.ai") && !url.contains("/login")
						&& !url.contains("accounts.google.com")) {
					break;
				}
				Thread.sleep(1000);
			}
			Thread.sleep(2000);

			if (page.url().contains("/login") || page.url().contains("accounts.google.com")) {
				String errorMsg = "❌ Google login did not complete - still on login/OAuth page: " + page.url();
				System.out.println(errorMsg);
				PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
				return false;
			}

			System.out.println("✅ Google login completed - now at " + page.url());
			return true;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// TC_107 After selecting a corrupt/unsupported file, the app shows a red error toast. PASS if it appears.
	private static final String UPLOAD_ERROR_TOAST = "//div[@class='pointer-events-auto inline-flex w-auto max-w-[560px] items-center gap-2.5 px-4 py-2.5 rounded-lg bg-zinc-900/95 backdrop-blur-md border border-red-500/30 shadow-depth-2 text-[13px] text-white animate-in fade-in slide-in-from-top-2 duration-200']";

	public boolean ValidateUploadErrorMessage() {
		try {
			PWActions.waitFor(UPLOAD_ERROR_TOAST, "Wait for upload error toast", 30000);
			if (PWActions.isVisible(UPLOAD_ERROR_TOAST, "Check upload error toast is visible")) {
				String raw = page.locator(UPLOAD_ERROR_TOAST).first().textContent();
				System.out.println("✅ TEST PASSED - Upload error message displayed: '"
						+ (raw == null ? "" : raw.trim()) + "'");
				return true;
			}
			String errorMsg = "❌ TEST FAILED - Upload error message did not appear";
			System.out.println(errorMsg);
			PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
			return false;
		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// TC_75 Validate that every video returned by a collection Detect Violence search has the expected name
	// ====================================================================================================
	// Conversations (Search) -> source = Collections -> pick "Automation_collection" -> Done -> Detect
	//   Violence -> Send. Then open each result card, read the video name, close it, and repeat for all.
	//   PASS only if EVERY video name equals expectedName (100%). The matching percentage is always
	//   reported (e.g. 5 of 10 named "VI_33_Violance" = 50%).
	private static final String COLLECTION_SOURCE_TOGGLE = "(//button[@class='flex items-center gap-1.5 px-3 py-1.5 rounded-lg text-xs font-medium transition-all duration-200 cursor-pointer bg-white/[0.03] text-zinc-400 border border-white/10 hover:text-white hover:border-white/20'])[1]";
	private static final String COLLECTION_BY_NAME_AUTOMATION = "//div[text()='Automation_collection']";
	private static final String COLLECTION_RESULT_CARD = "//div[@class='group flex items-center gap-3 p-2.5 rounded-xl glass-card border border-white/10 hover:border-white/20 hover:bg-white/[0.04] transition-all duration-200 cursor-pointer rim-light']";
	private static final String RESULT_VIDEO_NAME = "//div[@class='text-sm font-medium text-zinc-100 truncate']";
	private static final String RESULT_CLOSE_BUTTON = "//button[@class='p-2 rounded-lg text-zinc-400 hover:text-white hover:bg-white/5 transition-colors cursor-pointer shrink-0']";

	// TC_75 Detect Violence collection results must all be named the expected video.
	public boolean ValidateCollectionViolenceVideoNames(String expectedName) {
		return runCollectionQueryAndCheckNames(DETECT_VIOLENCE_BUTTON, "Detect Violence", expectedName);
	}

	// TC_76 Detect Nudity collection results must all be named the expected video.
	public boolean ValidateCollectionNudityVideoNames(String expectedName) {
		return runCollectionQueryAndCheckNames(NUDITY_QUERY_BUTTON, "Detect Nudity", expectedName);
	}

	// TC_77 Detect Vehicles collection results must all be named the expected video.
	public boolean ValidateCollectionVehicleVideoNames(String expectedName) {
		return runCollectionQueryAndCheckNames(VEHICLE_QUERY_BUTTON, "Detect Vehicles", expectedName);
	}

	// TC_78 Detect Logo collection results must all be named the expected video.
	public boolean ValidateCollectionLogoVideoNames(String expectedName) {
		return runCollectionQueryAndCheckNames(DETECT_LOGO_QUERY_BUTTON, "Detect Logo", expectedName);
	}

	// TC_79 Detect Emotions collection results must all be named the expected video.
	public boolean ValidateCollectionEmotionsVideoNames(String expectedName) {
		return runCollectionQueryAndCheckNames(DETECT_EMOTIONS_QUERY_BUTTON, "Detect Emotions", expectedName);
	}

	// TC_80 Detect Animals collection results must all be named the expected video.
	public boolean ValidateCollectionAnimalsVideoNames(String expectedName) {
		return runCollectionQueryAndCheckNames(DETECT_ANIMALS_QUERY_BUTTON, "Detect Animals", expectedName);
	}

	// Shared flow for TC_75 - TC_80: Collections source -> "Automation_collection" -> Done -> click the
	// given query chip -> Send, then open each result card, read its video name, and PASS only if EVERY
	// name equals expectedName. The matching percentage is logged for information only.
	private boolean runCollectionQueryAndCheckNames(String queryButton, String queryLabel, String expectedName) {
		try {
			// 1) Switch the source to Collections.
			PWActions.waitFor(COLLECTION_SOURCE_TOGGLE, "Wait for Collections source toggle", 30000);
			PWActions.click(COLLECTION_SOURCE_TOGGLE, "Clicked on Collections source");

			// 2) Open the collection picker (same dashed button used for videos).
			PWActions.waitFor(SELECT_VIDEO_BUTTON, "Wait for select collection button", 30000);
			PWActions.click(SELECT_VIDEO_BUTTON, "Clicked on select collection");

			// 3) Pick the "Automation_collection".
			PWActions.waitFor(COLLECTION_BY_NAME_AUTOMATION, "Wait for 'Automation_collection'", 30000);
			PWActions.click(COLLECTION_BY_NAME_AUTOMATION, "Selected 'Automation_collection'");

			// 4) Confirm the selection (Done).
			PWActions.waitFor(SELECT_VIDEO_DONE_BUTTON, "Wait for Done button", 30000);
			PWActions.click(SELECT_VIDEO_DONE_BUTTON, "Clicked on Done");

			// 5) Choose the query chip (Detect Violence / Nudity / Vehicles / ...).
			PWActions.waitFor(queryButton, "Wait for '" + queryLabel + "' option", 30000);
			PWActions.click(queryButton, "Clicked on " + queryLabel);

			// 6) Send the request.
			PWActions.waitFor(SEND_BUTTON, "Wait for Send button", 30000);
			PWActions.click(SEND_BUTTON, "Clicked on Send");

			// 7) Wait up to 90s for either result cards or a "no results" message.
			System.out.println("⏳ Waiting up to 90s for collection " + queryLabel + " results...");
			page.locator(COLLECTION_RESULT_CARD + " | " + NO_RESULTS_MESSAGE).first()
					.waitFor(new com.microsoft.playwright.Locator.WaitForOptions().setTimeout(90000));

			if (page.locator(NO_RESULTS_MESSAGE).count() > 0) {
				String noResultsText = page.locator(NO_RESULTS_MESSAGE).first().textContent().trim();
				String errorMsg = "❌ TEST FAILED - '" + noResultsText + "' was displayed";
				System.out.println(errorMsg);
				PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
				return false;
			}

			int total = page.locator(COLLECTION_RESULT_CARD).count();
			System.out.println("🃏 Result cards found: " + total);
			if (total == 0) {
				String errorMsg = "❌ TEST FAILED - No result cards appeared within 90 seconds";
				System.out.println(errorMsg);
				PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
				return false;
			}

			// 8) Open each card, read its video name, check against expectedName, then close it.
			java.util.List<String> names = new java.util.ArrayList<>();
			int matchCount = 0;
			for (int i = 0; i < total; i++) {
				// Re-resolve the list each iteration (the DOM re-renders after each close).
				page.locator(COLLECTION_RESULT_CARD).nth(i).click();

				page.locator(RESULT_VIDEO_NAME).first()
						.waitFor(new com.microsoft.playwright.Locator.WaitForOptions().setTimeout(30000));
				String raw = page.locator(RESULT_VIDEO_NAME).first().textContent();
				String name = raw == null ? "" : raw.trim();
				names.add(name);

				boolean matches = name.equalsIgnoreCase(expectedName);
				if (matches) {
					matchCount++;
				}
				System.out.println("  Card " + (i + 1) + " video name: '" + name + "' -> matches '" + expectedName
						+ "': " + matches);

				// Close the detail view and wait for the list to come back.
				page.locator(RESULT_CLOSE_BUTTON).first().click();
				page.locator(COLLECTION_RESULT_CARD).first()
						.waitFor(new com.microsoft.playwright.Locator.WaitForOptions().setTimeout(30000));
				Thread.sleep(500);
			}

			// Percentage is informational only (NOT the pass/fail criterion) - just so you can see how many
			// of the returned videos carried the expected name.
			double percentage = (matchCount * 100.0) / total;
			System.out.println("📋 All video names: " + names);
			System.out.println("ℹ️ (info only) Matching '" + expectedName + "': " + matchCount + "/" + total + " = "
					+ percentage + "%");

			// PASS/FAIL is decided purely by whether EVERY video matches the expected name.
			if (matchCount == total) {
				System.out.println("✅ TEST PASSED - All " + total + " videos are '" + expectedName + "'");
				return true;
			}

			String errorMsg = "❌ TEST FAILED - Not all videos are '" + expectedName + "' (" + matchCount + "/" + total
					+ " matched). Names: " + names;
			System.out.println(errorMsg);
			PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
			return false;

		} catch (Exception e) {
			PWBaseTest.getFailureContext().setErrorMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// Shared tail for Analyze-mode validations: poll until the real (non-"Thinking…") reply has stabilised,
	// then compare its MEANING to expectedText via sentence-embedding similarity (>= SIMILARITY_THRESHOLD).
	private boolean awaitReplyAndCompare(String queryLabel, String expectedText) throws InterruptedException {
		// The reply container first shows a "Thinking…" placeholder, then fills with the real reply. Poll
		// until it is no longer "Thinking…" and has stopped changing, then analyse immediately - we do NOT
		// block for a fixed 90s (90s is only a safety cap).
		System.out.println("⏳ Waiting for the " + queryLabel + " reply (polling, max 90s)...");
		String actualText = waitForAnalyseReply(90000);
		System.out.println("📝 Actual reply text:\n" + actualText);

		if (actualText.isEmpty() || actualText.toLowerCase().startsWith("thinking")) {
			String errorMsg = "❌ TEST FAILED - " + queryLabel + " reply did not arrive within 90s (last seen: '"
					+ actualText + "')";
			System.out.println(errorMsg);
			PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
			return false;
		}

		double similarity = TextCompareUtil.getSimilarity(expectedText, actualText);
		System.out.println("🤝 Similarity score: " + similarity + " (threshold " + SIMILARITY_THRESHOLD + ")");

		if (similarity >= SIMILARITY_THRESHOLD) {
			System.out.println("✅ TEST PASSED - " + queryLabel + " reply meaning matches expected (score="
					+ similarity + ")");
			return true;
		}

		String errorMsg = "❌ TEST FAILED - " + queryLabel + " reply meaning does not match. Similarity="
				+ similarity + " < threshold " + SIMILARITY_THRESHOLD;
		System.out.println(errorMsg);
		PWBaseTest.getFailureContext().setErrorMessage(errorMsg);
		return false;
	}

	// Poll the reply container until it shows a real (non-"Thinking…") reply that has stopped changing,
	// then return it. Returns AS SOON AS the reply is ready and stable - it does NOT block for the full
	// timeout. maxMs is only a safety cap; if it elapses while still "Thinking…"/empty, the last seen
	// text is returned and the caller treats it as a failure.
	private String waitForAnalyseReply(int maxMs) throws InterruptedException {
		long deadline = System.currentTimeMillis() + maxMs;
		String previous = "";
		int stableReads = 0;
		while (System.currentTimeMillis() < deadline) {
			int n = page.locator(ANALYSE_REPLY_CONTAINER).count();
			// Take the newest reply container (the latest message), guarding against zero matches.
			String raw = n == 0 ? "" : page.locator(ANALYSE_REPLY_CONTAINER).last().textContent();
			String current = raw == null ? "" : raw.trim();

			boolean ready = !current.isEmpty() && !current.toLowerCase().startsWith("thinking");
			if (ready && current.equals(previous)) {
				// Unchanged across consecutive polls -> streamed reply has finished.
				if (++stableReads >= 2) {
					return current;
				}
			} else {
				stableReads = 0;
			}
			previous = current;
			Thread.sleep(1000);
		}
		return previous;
	}
}











 