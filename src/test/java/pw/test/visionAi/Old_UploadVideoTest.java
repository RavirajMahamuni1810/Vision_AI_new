package pw.test.visionAi;

import java.lang.reflect.Method;
import java.util.Map;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pw.base.PWBaseTest;
import pw.base.TestMeta;
import pw.base.UserType;
import pw.pages.visionAi.UploadVideoPage;
import pw.utils.PWLog;
 

public class Old_UploadVideoTest extends PWBaseTest

{
	String sheetName = "UploadVideo";

	// TC_00 One-time Google sign-in (priority 0 -> runs FIRST). Enable this together with any test you want
	// to run (e.g. TC_56) so the persistent profile is authenticated before that test executes. It skips
	// automatically when already logged in. Set GOOGLE_EMAIL / GOOGLE_PASSWORD as env vars (e.g. in Jenkins).
	@TestMeta(user = UserType.ADMIN, navPath = "")
	@Test(dataProvider = "loginData", enabled = true, priority = 0, groups = { "Smoke" })
	public void M_689_VisionAi_Login_00(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();
		String email = System.getenv("GOOGLE_EMAIL");
		String password = System.getenv("GOOGLE_PASSWORD");

		// Sign in via Google once (auto-skips if the profile is already authenticated).
		if (UploadVideoPage.LoginWithGoogle(email, password)) {
			PWLog.Pass(className, "Authenticated session is ready for the suite");
		} else {
			PWLog.Fail(className, "Google sign-in failed: " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_01_ Upload single
	// video============================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "videos")
	@Test(dataProvider = "loginData", enabled = false, priority = 1, groups = { "Smoke" })
	public void M_689_VisionAi_Login_01(Method method, Map<String, String> testData) {
		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();
		System.out.println("Full Test Data: " + testData);

//		String fileType = testData.get("videoname");
		String fileName1 = testData.get("clipone");
		String Filetext = testData.get("expectedtext");

		if (UploadVideoPage.UPload_Video()) {
			PWLog.Pass(className, " Video Uploaded successfully");
		} else {
			PWLog.Fail(className,
					" Video not Uploaded sucessfully " + PWBaseTest.getFailureContext().getErrorMessage());
		}

		if (UploadVideoPage.Uploadmultiplefile(fileName1)) {
			PWLog.Pass(className, "Video uploaded successfully");
		} else {
			PWLog.Fail(className, "Video upload failed: " + PWBaseTest.getFailureContext().getErrorMessage());
		}

		if (UploadVideoPage.CompleteUploadVideo()) {
			PWLog.Pass(className, "Complete upload video successfully and video is Appear in video list");

		} else {
			PWLog.Fail(className, " Unable to complete upload video and video is not Appear in video list "
					+ PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// ===================================================================================================
	// TC_101 - TC_106: Upload videos in different formats and validate each uploads successfully.
	// TC_107: Upload a corrupt file and validate the error toast appears.
	// These run standalone (by priority 101-107). File comes from the Excel "qc_clipone" column for each
	// TCID, read from the "UploadVideo" sheet of MinopTandATestData.xlsm.
	// ===================================================================================================

	// TC_101 Upload a .3gp video and validate it uploads successfully (card title: 3_GP_format)
	@TestMeta(user = UserType.ADMIN, navPath = "videos")
	@Test(dataProvider = "loginData", enabled = false, priority = 101, groups = { "Smoke" })
	public void M_689_VisionAi_Login_101(Method method, Map<String, String> testData) {
		uploadVideoAndValidate(testData, "3_GP_format");
	}

	// TC_102 Upload a .avi video and validate it uploads successfully
	@TestMeta(user = UserType.ADMIN, navPath = "videos")
	@Test(dataProvider = "loginData", enabled = false, priority = 102, groups = { "Smoke" })
	public void M_689_VisionAi_Login_102(Method method, Map<String, String> testData) {
		uploadVideoAndValidate(testData, "avi_format");
	}

	// TC_103 Upload a .mkv video and validate it uploads successfully
	@TestMeta(user = UserType.ADMIN, navPath = "videos")
	@Test(dataProvider = "loginData", enabled = false, priority = 103, groups = { "Smoke" })
	public void M_689_VisionAi_Login_103(Method method, Map<String, String> testData) {
		uploadVideoAndValidate(testData, "mkv_format");
	}

	// TC_104 Upload a .mov video and validate it uploads successfully
	@TestMeta(user = UserType.ADMIN, navPath = "videos")
	@Test(dataProvider = "loginData", enabled = false, priority = 104, groups = { "Smoke" })
	public void M_689_VisionAi_Login_104(Method method, Map<String, String> testData) {
		uploadVideoAndValidate(testData, "mov_format");
	}

	// TC_105 Upload a .webm video and validate it uploads successfully
	@TestMeta(user = UserType.ADMIN, navPath = "videos")
	@Test(dataProvider = "loginData", enabled = false, priority = 105, groups = { "Smoke" })
	public void M_689_VisionAi_Login_105(Method method, Map<String, String> testData) {
		uploadVideoAndValidate(testData, "webm_format");
	}

	// TC_106 Upload a .wmv video and validate it uploads successfully
	@TestMeta(user = UserType.ADMIN, navPath = "videos")
	@Test(dataProvider = "loginData", enabled = false, priority = 106, groups = { "Smoke" })
	public void M_689_VisionAi_Login_106(Method method, Map<String, String> testData) {
		uploadVideoAndValidate(testData, "wmv_format");
	}

	// TC_107 Upload a corrupt file: the upload must FAIL (error shown), NOT complete. Passes only when the
	// error appears (the "Just now" completed card must never show).
	@TestMeta(user = UserType.ADMIN, navPath = "videos")
	@Test(dataProvider = "loginData", enabled = false, priority = 107, groups = { "Smoke" })
	public void M_689_VisionAi_Login_107(Method method, Map<String, String> testData) {
		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();
		String fileName = testData.get("clipone");

		if (UploadVideoPage.UPload_Video()) {
			PWLog.Pass(className, "Upload UI opened successfully");
		} else {
			PWLog.Fail(className, "Upload UI failed: " + PWBaseTest.getFailureContext().getErrorMessage());
		}

		if (UploadVideoPage.Uploadmultiplefile(fileName)) {
			PWLog.Pass(className, "Corrupt file '" + fileName + "' selected");
		} else {
			PWLog.Fail(className, "File selection failed: " + PWBaseTest.getFailureContext().getErrorMessage());
		}

		if (UploadVideoPage.ValidateCorruptUploadRejected()) {
			PWLog.Pass(className, "Corrupt file '" + fileName + "' was correctly rejected with an error");
		} else {
			PWLog.Fail(className, "Corrupt file did not show an error (upload not rejected): "
					+ PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// Shared upload-and-verify flow for TC_101 - TC_106 (valid formats). expectedTitle is the exact card <h3> text shown
	// for the uploaded video (e.g. "avi_format.avi", "3_GP_format").
	private void uploadVideoAndValidate(Map<String, String> testData, String expectedTitle) {
		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();
		String fileName = testData.get("clipone");

		if (UploadVideoPage.UPload_Video()) {
			PWLog.Pass(className, "Upload UI opened successfully");
		} else {
			PWLog.Fail(className, "Upload UI failed: " + PWBaseTest.getFailureContext().getErrorMessage());
		}

		if (UploadVideoPage.Uploadmultiplefile(fileName)) {
			PWLog.Pass(className, "Video '" + fileName + "' selected");
		} else {
			PWLog.Fail(className, "Video selection failed: " + PWBaseTest.getFailureContext().getErrorMessage());
		}

		if (UploadVideoPage.CompleteUploadVideo(expectedTitle)) {
			PWLog.Pass(className, "Video '" + expectedTitle + "' uploaded successfully and appears in the list");
		} else {
			PWLog.Fail(className, "Video '" + expectedTitle + "' not uploaded: "
					+ PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_02_ upload Multiple
	// video===========================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "videos")
	@Test(dataProvider = "loginData", enabled = true, priority = 2, groups = { "Smoke" })
	public void M_689_VisionAi_Login_02(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		String fileName1 = testData.get("clipone");
		String fileName2 = testData.get("cliptwo");
		String fileName3 = testData.get("clipthree");
//	    String fileName4 = testData.get("clipfour");

		if (UploadVideoPage.UPload_Video()) {
			PWLog.Pass(className, "Upload UI opened successfully");
		} else {
			PWLog.Fail(className, "Upload UI failed " + PWBaseTest.getFailureContext().getErrorMessage());
		}

		if (UploadVideoPage.Uploadmultiplefile(fileName1, fileName2, fileName3)) {
			PWLog.Pass(className, "All videos uploaded successfully");
		} else {
			PWLog.Fail(className, "Video upload failed " + PWBaseTest.getFailureContext().getErrorMessage());
		}
 
		 
		if (UploadVideoPage.UploadMultiplevideoSuccessfully("VI_01", "VI_02", "VI_15")) {
			PWLog.Pass(className, "All 3 videos uploaded successfully and appear in the list (Just now)");

		} else {
			PWLog.Fail(className, " Unable to complete upload multiple video and video is not Appear in video list "
					+ PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_03_ Total count increse After
	// Upload==================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "videos")
	@Test(dataProvider = "loginData", enabled = false, priority = 3, groups = { "Smoke" })
	public void M_689_VisionAi_Login_03(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		String fileName1 = testData.get("clipone");

//		if (UploadVideoPage.GetInitialCount()) {
//			PWLog.Pass(className, "Get initial count successfully");
//		} else {
//			PWLog.Fail(className, "Not gwt initial count " + PWBaseTest.getFailureContext().getErrorMessage());
//		}

		if (UploadVideoPage.GetInitialCount()) {
			PWLog.Pass(className, "Get initial count successfully");
		} else {
			PWLog.Fail(className, "Not gwt initial count " + PWBaseTest.getFailureContext().getErrorMessage());
		}

		if (UploadVideoPage.UPload_Video()) {
			PWLog.Pass(className, " Video Uploaded successfully");
		} else {
			PWLog.Fail(className,
					" Video not Uploaded sucessfully " + PWBaseTest.getFailureContext().getErrorMessage());
		}

		if (UploadVideoPage.Uploadmultiplefile(fileName1)) {
			PWLog.Pass(className, "Video uploaded successfully");
		} else {
			PWLog.Fail(className, "Video upload failed: " + PWBaseTest.getFailureContext().getErrorMessage());
		}

		if (UploadVideoPage.CompleteUploadVideo()) {
			PWLog.Pass(className, "Complete upload video successfully and video is Appear in video list");

		} else {
			PWLog.Fail(className, " Unable to complete upload video and video is not Appear in video list "
					+ PWBaseTest.getFailureContext().getErrorMessage());
		}

		if (UploadVideoPage.GetFinalCount()) {
			PWLog.Pass(className, "Final count retrieved successfully");
		} else {
			PWLog.Fail(className, "Failed to retrieve final count " + PWBaseTest.getFailureContext().getErrorMessage());
		}

		if (UploadVideoPage.VerifyCountIncreasedByOne()) {
			PWLog.Pass(className, "Count increased by one successfully");
		} else {
			PWLog.Fail(className, "Count did not increase by one " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_04_ raw count dedected After
	// delete------------------------------------------------------------------------------------
	@TestMeta(user = UserType.ADMIN, navPath = "videos")
	@Test(dataProvider = "loginData", enabled = false, priority = 3, groups = { "Smoke" })
	public void M_689_VisionAi_Login_04(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		String fileName1 = testData.get("clipone");

		if (UploadVideoPage.UPload_Video()) {
			PWLog.Pass(className, " Video Uploaded successfully");
		} else {
			PWLog.Fail(className,
					" Video not Uploaded sucessfully " + PWBaseTest.getFailureContext().getErrorMessage());
		}

		if (UploadVideoPage.Uploadmultiplefile(fileName1)) {
			PWLog.Pass(className, "Video uploaded successfully");
		} else {
			PWLog.Fail(className, "Video upload failed: " + PWBaseTest.getFailureContext().getErrorMessage());
		}

		if (UploadVideoPage.CompleteUploadVideo()) {
			PWLog.Pass(className, "Complete upload video successfully and video is Appear in video list");

		} else {
			PWLog.Fail(className, " Unable to complete upload video and video is not Appear in video list "
					+ PWBaseTest.getFailureContext().getErrorMessage());
		}

		if (UploadVideoPage.GetInitialRawCount()) {
			PWLog.Pass(className, "Get initial count successfully");
		} else {
			PWLog.Fail(className, "Not gwt initial count " + PWBaseTest.getFailureContext().getErrorMessage());
		}

		if (UploadVideoPage.DeleteVideo()) {
			PWLog.Pass(className, " delete video successfully");
		} else {
			PWLog.Fail(className,
					" Not delete video successfully  " + PWBaseTest.getFailureContext().getErrorMessage());
		}

		if (UploadVideoPage.GetFinalRawCount()) {
			PWLog.Pass(className, "Final count retrieved successfully");
		} else {
			PWLog.Fail(className, "Failed to retrieve final count " + PWBaseTest.getFailureContext().getErrorMessage());
		}

		if (UploadVideoPage.VerifyRawCountDecreasedByOne()) {
			PWLog.Pass(className, " count decreased by one successfully");
		} else {
			PWLog.Fail(className, "Count not decreased" + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_82_ large in length
	// video-------------------------------------------------------------------------------------------------
	// NOTE: renumbered from 80 -> 82 so the conversation collection test can use M_689_VisionAi_Login_80.
	@TestMeta(user = UserType.ADMIN, navPath = "videos")
	@Test(dataProvider = "loginData", enabled = false, priority = 1, groups = { "Smoke" })
	public void M_689_VisionAi_Login_82(Method method, Map<String, String> testData) {
		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();
		System.out.println("Full Test Data: " + testData);

		String fileName1 = testData.get("clipone");

		if (UploadVideoPage.UPload_Video()) {
			PWLog.Pass(className, " Click on upload and upload UI opened successfully");
		} else {
			PWLog.Fail(className, "Error while click on upload tab" + PWBaseTest.getFailureContext().getErrorMessage());
		}

		if (UploadVideoPage.Uploadmultiplefile(fileName1)) {
			PWLog.Pass(className, " Video select successfully and upload start");
		} else {
			PWLog.Fail(className, " Video not selected " + PWBaseTest.getFailureContext().getErrorMessage());
		}

		if (UploadVideoPage.UPload_largelegth_video()) {
			PWLog.Pass(className, "Complete large length upload video successfully and video is Appear in video list");

		} else {
			PWLog.Fail(className, " Unable to complete upload large length video and video is not Appear in video list "
					+ PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_81_large in size
	// video------------------------------------------------------------------------------------------------------
	@TestMeta(user = UserType.ADMIN, navPath = "videos")
	@Test(dataProvider = "loginData", enabled = false, priority = 1, groups = { "Smoke" })
	public void M_689_VisionAi_Login_81(Method method, Map<String, String> testData) {
		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();
		System.out.println("Full Test Data: " + testData);

//		String fileType = testData.get("videoname");
		String fileName1 = testData.get("clipone");
		String Filetext = testData.get("expectedtext");

		if (UploadVideoPage.UPload_Video()) {
			PWLog.Pass(className, " Video Uploaded successfully");
		} else {
			PWLog.Fail(className,
					" Video not Uploaded sucessfully " + PWBaseTest.getFailureContext().getErrorMessage());
		}

		if (UploadVideoPage.Uploadmultiplefile(fileName1)) {
			PWLog.Pass(className, "Video uploaded successfully");
		} else {
			PWLog.Fail(className, "Video upload failed: " + PWBaseTest.getFailureContext().getErrorMessage());
		}

		if (UploadVideoPage.CompleteUploadVideo()) {
			PWLog.Pass(className, "Complete upload video successfully and video is Appear in video list");

		} else {
			PWLog.Fail(className, " Unable to complete upload video and video is not Appear in video list "
					+ PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_05_ AddYouTube
	// video-------------------------------------------------------------------------------------------------------
	@TestMeta(user = UserType.ADMIN, navPath = "videos")
	@Test(dataProvider = "loginData", enabled = false, priority = 5, groups = { "Smoke" })
	public void M_689_VisionAi_Login_05(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();
		String youtubeurl = testData.get("url");

		if (UploadVideoPage.UPload_You_Tube_video(youtubeurl)) {
			PWLog.Pass(className, " You tube Video Uploaded successfully");
		} else {
			PWLog.Fail(className,
					"You tube video not upload successfully " + PWBaseTest.getFailureContext().getErrorMessage());
		}

	}

	// TC_06_ AddBroken YouTube
	// video------------------------------------------------------------------------------------------------
	@TestMeta(user = UserType.ADMIN, navPath = "videos")
	@Test(dataProvider = "loginData", enabled = false, priority = 6, groups = { "Smoke" })
	public void M_689_VisionAi_Login_06(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();
		String youtubeurl = testData.get("url");

		if (UploadVideoPage.UPload_BrokenYou_Tube_video(youtubeurl)) {
			PWLog.Pass(className, " You tube Video not  Uploaded successfully");
		} else {
			PWLog.Fail(className,
					"You tube video  upload successfully " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_07_ Add_Private YouTube
	// video------------------------------------------------------------------------------------------------
	@TestMeta(user = UserType.ADMIN, navPath = "videos")
	@Test(dataProvider = "loginData", enabled = false, priority = 7, groups = { "Smoke" })
	public void M_689_VisionAi_Login_07(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();
		String youtubeurl = testData.get("url");

		if (UploadVideoPage.UPload_PrivateYou_Tube_video(youtubeurl)) {
			PWLog.Pass(className, " You tube Video not  Uploaded successfully");
		} else {
			PWLog.Fail(className,
					"You tube video  upload successfully " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_08_ Add
	// InstaagramVideo------------------------------------------------------------------------------------------------------
	@TestMeta(user = UserType.ADMIN, navPath = "videos")
	@Test(dataProvider = "loginData", enabled = false, priority = 8, groups = { "Smoke" })
	public void M_689_VisionAi_Login_08(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();
		String youtubeurl = testData.get("url");

		if (UploadVideoPage.UPload_Instagram_video(youtubeurl)) {
			PWLog.Pass(className, " Instagram video not  Uploaded successfully");
		} else {
			PWLog.Fail(className,
					"Instagram video  upload successfully " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_09_
	// Add_LargeYouTube_Video---------------------------------------------------------------------------------------------------
	@TestMeta(user = UserType.ADMIN, navPath = "videos")
	@Test(dataProvider = "loginData", enabled = false, priority = 9, groups = { "Smoke" })
	public void M_689_VisionAi_Login_09(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();
		String youtubeurl = testData.get("url");

		if (UploadVideoPage.UPload_LargeUTube_video(youtubeurl)) {
			PWLog.Pass(className, "Large size video not  Uploaded successfully");
		} else {
			PWLog.Fail(className,
					"Large video  upload successfully " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_10 Single video
	// process-------------------------------------------------------------------------------------------------------
	@TestMeta(user = UserType.ADMIN, navPath = "videos")
	@Test(dataProvider = "loginData", enabled = false, priority = 9, groups = { "Smoke" })
	public void M_689_VisionAi_Login_10(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		if (UploadVideoPage.processvideo()) {
			PWLog.Pass(className, " video process start suceessfully");
		} else {
			PWLog.Fail(className, " error while video process" + PWBaseTest.getFailureContext().getErrorMessage());
		}

		if (UploadVideoPage.IsdisplayProcessed()) {
			PWLog.Pass(className, " After process video is display in video list suceessfully");
		} else {
			PWLog.Fail(className,
					"video is not display with proocesesed tag" + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_11 Multiple video
	// process=====================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "videos")
	@Test(dataProvider = "loginData", enabled = false, priority = 9, groups = { "Smoke" })
	public void M_689_VisionAi_Login_11(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		if (UploadVideoPage.processMultiplevideo()) {
			PWLog.Pass(className, " video process start suceessfully");
		} else {
			PWLog.Fail(className, " error while video process" + PWBaseTest.getFailureContext().getErrorMessage());
		}

		if (UploadVideoPage.IsdisplaymultipleProcessed()) {
			PWLog.Pass(className, " After process video is display in video list suceessfully");
		} else {
			PWLog.Fail(className,
					"video is not display with proocesesed tag" + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}
	// ===================================================================================================================================

	// TC_12 Check count Processed video process
	@TestMeta(user = UserType.ADMIN, navPath = "videos")
	@Test(dataProvider = "loginData", enabled = false, priority = 9, groups = { "Smoke" })
	public void M_689_VisionAi_Login_12(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		if (UploadVideoPage.GetInitialProcessingCount()) {
			PWLog.Pass(className, "Get initial count successfully");
		} else {
			PWLog.Fail(className, "Not gwt initial count " + PWBaseTest.getFailureContext().getErrorMessage());
		}

		if (UploadVideoPage.processvideo()) {
			PWLog.Pass(className, " video process start suceessfully");
		} else {
			PWLog.Fail(className, " error while video process" + PWBaseTest.getFailureContext().getErrorMessage());
		}

		if (UploadVideoPage.IsdisplayProcessed()) {
			PWLog.Pass(className, " After process video is display in video list suceessfully");
		} else {
			PWLog.Fail(className,
					"video is not display with proocesesed tag" + PWBaseTest.getFailureContext().getErrorMessage());
		}

		if (UploadVideoPage.GetFinalProcessingCount()) {
			PWLog.Pass(className, "Final count retrieved successfully");
		} else {
			PWLog.Fail(className, "Failed to retrieve final count " + PWBaseTest.getFailureContext().getErrorMessage());
		}

		if (UploadVideoPage.VerifyProcessingCountIncreasedByOne()) {
			PWLog.Pass(className, "Count increased by one successfully");
		} else {
			PWLog.Fail(className, "Count did not increase by one " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// ====================================================================================================================================
	// TC_13 Check user Able to delete
	@TestMeta(user = UserType.ADMIN, navPath = "videos")
	@Test(dataProvider = "loginData", enabled = false, priority = 3, groups = { "Smoke" })
	public void M_689_VisionAi_Login_13(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		String fileName1 = testData.get("clipone");

		if (UploadVideoPage.UPload_Video()) {
			PWLog.Pass(className, " Video Uploaded successfully");
		} else {
			PWLog.Fail(className,
					" Video not Uploaded sucessfully " + PWBaseTest.getFailureContext().getErrorMessage());
		}

		if (UploadVideoPage.Uploadmultiplefile(fileName1)) {
			PWLog.Pass(className, "Video uploaded successfully");
		} else {
			PWLog.Fail(className, "Video upload failed: " + PWBaseTest.getFailureContext().getErrorMessage());
		}

		if (UploadVideoPage.CompleteUploadVideo()) {
			PWLog.Pass(className, "Complete upload video successfully and video is Appear in video list");

		} else {
			PWLog.Fail(className, " Unable to complete upload video and video is not Appear in video list "
					+ PWBaseTest.getFailureContext().getErrorMessage());
		}

		if (UploadVideoPage.GetInitialRawCount()) {
			PWLog.Pass(className, "Get initial count successfully");
		} else {
			PWLog.Fail(className, "Not gwt initial count " + PWBaseTest.getFailureContext().getErrorMessage());
		}

		if (UploadVideoPage.DeleteVideo()) {
			PWLog.Pass(className, " delete video successfully");
		} else {
			PWLog.Fail(className,
					" Not delete video successfully  " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}
	// not Added validation

	// TC_14 Check Notification display
	// ================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "videos")
	@Test(dataProvider = "loginData", enabled = false, priority = 9, groups = { "Smoke" })
	public void M_689_VisionAi_Login_14(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		if (UploadVideoPage.processvideo()) {
			PWLog.Pass(className, " video process start suceessfully");
		} else {
			PWLog.Fail(className, " error while video process" + PWBaseTest.getFailureContext().getErrorMessage());
		}

		if (UploadVideoPage.IsdisplayProcessed()) {
			PWLog.Pass(className, " After process video is display in video list suceessfully");
		} else {
			PWLog.Fail(className,
					"video is not display with proocesesed tag" + PWBaseTest.getFailureContext().getErrorMessage());
		}
		if (UploadVideoPage.Check_Notification()) {
			PWLog.Pass(className, " After process video  Notification is recieved suceessfully");
		} else {
			PWLog.Fail(className,
					" Error while nofication recieved" + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_15 Check search video
	// display
	// ====================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "videos")
	@Test(dataProvider = "loginData", enabled = false, priority = 9, groups = { "Smoke" })
	public void M_689_VisionAi_Login_15(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		if (UploadVideoPage.Search_video()) {
			PWLog.Pass(className, " video is display after search suceessfully");
		} else {
			PWLog.Fail(className,
					"video is not display after search" + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_16 Check while click on processed All processed video should be display
	// ====================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "videos")
	@Test(dataProvider = "loginData", enabled = false, priority = 10, groups = { "Smoke" })
	public void M_689_VisionAi_Login_16(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		if (UploadVideoPage.VerifyProcessedVideo()) {
			PWLog.Pass(className, " When click on processed tag all processed video is display suceessfully");
		} else {
			PWLog.Fail(className, " When click on proccessed only processed video not display "
					+ PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_17 Check while click on processed All processed video should be display
	// ====================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "videos")
	@Test(dataProvider = "loginData", enabled = false, priority = 10, groups = { "Smoke" })
	public void M_689_VisionAi_Login_17(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		if (UploadVideoPage.VerifyRAWVideo()) {
			PWLog.Pass(className, " When click on RAW tag all RAW video is display suceessfully");
		} else {
			PWLog.Fail(className, " When click on RAW only RAW video not display "
					+ PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_18 Check Video playing After click on video
	// ====================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "videos")
	@Test(dataProvider = "loginData", enabled = false, priority = 11, groups = { "Smoke" })
	public void M_689_VisionAi_Login_18(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		if (UploadVideoPage.Verify_Video_Is_Playing()) {
			PWLog.Pass(className, " When click on video  video is playing suceessfully");
		} else {
			PWLog.Fail(className, " When click on video video is not playing successfully "
					+ PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_19 create Collection
	// ====================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "collections")
	@Test(dataProvider = "loginData", enabled = false, priority = 11, groups = { "Smoke" })
	public void M_689_VisionAi_Login_19(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		if (UploadVideoPage.Delete_FirstCollection_From_API()) {
			PWLog.Pass(className, " Deleted Existing Collection successfully");
		} else {
			PWLog.Fail(className,
					" Not deleted Existing collection" + PWBaseTest.getFailureContext().getErrorMessage());
		}
		if (UploadVideoPage.Verify_CreateCollection()) {
			PWLog.Pass(className, " Created  Collection successfully");
		} else {
			PWLog.Fail(className,
					"Deleted  Collection successfully " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_20 create same name Colection
	// ====================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "collections")
	@Test(dataProvider = "loginData", enabled = false, priority = 12, groups = { "Smoke" })
	public void M_689_VisionAi_Login_20(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		if (UploadVideoPage.Verify_CreatesameNameCollection()) {
			PWLog.Pass(className, " Not Able to create collection with same name");
		} else {
			PWLog.Fail(className,
					" Able to create collection with same name" + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_21 Add_singlevid_inCollection
	// ====================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "collections")
	@Test(dataProvider = "loginData", enabled = false, priority = 12, groups = { "Smoke" })
	public void M_689_VisionAi_Login_21(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		if (UploadVideoPage.Add_singlevid_inCollection()) {
			PWLog.Pass(className, "Sucessfully Added single video in collection");
		} else {
			PWLog.Fail(className,
					"Error while Added video in collection" + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_22 Edit_collection
	// ====================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "collections")
	@Test(dataProvider = "loginData", enabled = false, priority = 12, groups = { "Smoke" })
	public void M_689_VisionAi_Login_22(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		if (UploadVideoPage.Verify_Edit_Collection()) {
			PWLog.Pass(className, "Sucessfully  edit collection name ");
		} else {
			PWLog.Fail(className,
					"Error while  editcollection name" + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_23 Add_Multiple Video in _collection
	// ====================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "collections")
	@Test(dataProvider = "loginData", enabled = false, priority = 12, groups = { "Smoke" })
	public void M_689_VisionAi_Login_23(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();
        //  here Adding multiple video by creating new coolection
		if (UploadVideoPage.Add_Multiple_inCollection()) {
			PWLog.Pass(className, "Sucessfully Added Multiple video in collection");
		} else {
			PWLog.Fail(className, "Error while Added multiple video in collection"
					+ PWBaseTest.getFailureContext().getErrorMessage());
		}
	}
	// TC_24 Delete collection
	// ====================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "collections")
	@Test(dataProvider = "loginData", enabled = false, priority = 12, groups = { "Smoke" })
	public void M_689_VisionAi_Login_24(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		if (UploadVideoPage.Verify_Delete_Collection()) {
			PWLog.Pass(className, "Sucessfully delete collection");
		} else {
			PWLog.Fail(className, "Error while delete collection"
					+ PWBaseTest.getFailureContext().getErrorMessage());
		}
	}
	
	// TC_25 Search collection
	// ====================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "collections")
	@Test(dataProvider = "loginData", enabled = false, priority = 12, groups = { "Smoke" })
	public void M_689_VisionAi_Login_25(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		if (UploadVideoPage.Verify_Search_Collection()) {
			PWLog.Pass(className, " Collection search successfully");
		} else {
			PWLog.Fail(className, " Error while search collection"
					+ PWBaseTest.getFailureContext().getErrorMessage());
		}
	}
	// TC_26 Search collection ==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "collections")
	@Test(dataProvider = "loginData", enabled = false, priority = 26, groups = { "Smoke" })
	public void M_689_VisionAi_Login_26(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		if (UploadVideoPage.Verify_Video_Not_disply()) {
			PWLog.Pass(className, "ones video Added in collection that video is not  display in list next  time");
		} else {
			PWLog.Fail(className, "Already Added video is display in list "
					+ PWBaseTest.getFailureContext().getErrorMessage());
		}
		if (UploadVideoPage.Delete_FirstCollection_From_API()) {
			PWLog.Pass(className, " Deleted Existing Collection successfully");
		} else {
			PWLog.Fail(className,
					" Not deleted Existing collection" + PWBaseTest.getFailureContext().getErrorMessage());
		}}
		
	//*****************************************************CAMERA TEST CASES*******************************************************
	
	// TC_26  validation fr name and rstl==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "cameras")
	@Test(dataProvider = "loginData", enabled = false , priority = 27, groups = { "Smoke" })
	public void M_689_VisionAi_Login_27(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		if (UploadVideoPage.Validaton_Msg_Fr_Camera()) {
			PWLog.Pass(className, "validationn messageis display if connect without credential");
		} else {
			PWLog.Fail(className, " validationn message is not display if connect without credential "
					+ PWBaseTest.getFailureContext().getErrorMessage());
		} }
	// TC_28 validation fr rstl==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "cameras")
	@Test(dataProvider = "loginData", enabled = false, priority = 28, groups = { "Smoke" })
	public void M_689_VisionAi_Login_28(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		if (UploadVideoPage.ValidatonForWrongFormateURL()) {
			PWLog.Pass(className, " Error mesage is display if connect with wrong URL");
		} else {
			PWLog.Fail(className, "Error mesage is not display if connect with wrong URL "
					+ PWBaseTest.getFailureContext().getErrorMessage());
		} }
	// TC_29 validation fr rstl==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "cameras")
	@Test(dataProvider = "loginData", enabled = false, priority = 29, groups = { "Smoke" })
	public void M_689_VisionAi_Login_29(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		if (UploadVideoPage.ValidatoURL()) {
			PWLog.Pass(className, " Error mesage is display if connect with wrong URL");
		} else {
			PWLog.Fail(className, "Error mesage is not display if connect with wrong URL "
					+ PWBaseTest.getFailureContext().getErrorMessage());
		} }
	// TC_30  Connect camera==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "cameras")
	@Test(dataProvider = "loginData", enabled = false, priority = 30, groups = { "Smoke" })
	public void M_689_VisionAi_Login_30(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		if (UploadVideoPage.ConnectCamera()) {
			PWLog.Pass(className, " Camera is connected successfully and  camera stream is display");
		} else {
			PWLog.Fail(className, " Camera is not connected and camera stream is not display "
					+ PWBaseTest.getFailureContext().getErrorMessage());
		} 
		}	
	// TC_31  Connect same name camera==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "cameras")
	@Test(dataProvider = "loginData", enabled = false, priority = 31, groups = { "Smoke" })
	public void M_689_VisionAi_Login_31(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		if (UploadVideoPage.validationfrsameCameraname()) {
			PWLog.Pass(className, " While connect camera with same name validation message is display");
		} else {
			PWLog.Fail(className, " While connect camera with same name validation message is not display "
					+ PWBaseTest.getFailureContext().getErrorMessage());
		} }		
	// TC_32 delete camera==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "cameras")
	@Test(dataProvider = "loginData", enabled = false, priority = 32, groups = { "Smoke" })
	public void M_689_VisionAi_Login_32(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		if (UploadVideoPage.Deletecamera()) {
			PWLog.Pass(className, " Camera is deleted successfully");
		} else {
			PWLog.Fail(className, " Camera is not deleted successfully "
					+ PWBaseTest.getFailureContext().getErrorMessage());
		} }		
	// TC_33 TOTAL AND RAW COUNT==================================================================================================
		@TestMeta(user = UserType.ADMIN, navPath = "cameras")
		@Test(dataProvider = "loginData", enabled = false, priority = 33, groups = { "Smoke" })
		public void M_689_VisionAi_Login_33(Method method, Map<String, String> testData) {

			UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
			String className = this.getClass().getSimpleName();

			if (UploadVideoPage.GetInitialCameraCount()) {
				PWLog.Pass(className, "get nitial count of camera");
			} else {
				PWLog.Fail(className, "  Not get initial count of camera "
						+ PWBaseTest.getFailureContext().getErrorMessage());
			} 
			if (UploadVideoPage.ConnectCamera()) {
				PWLog.Pass(className, " Camera is connected successfully and  camera stream is display");
			} else {
				PWLog.Fail(className, " Camera is not connected and camera stream is not display "
						+ PWBaseTest.getFailureContext().getErrorMessage());
			} 
			if (UploadVideoPage.GetFinalCameraCount()) {
				PWLog.Pass(className, " get final count of camera");
			} else {
				PWLog.Fail(className, " Not get final count of camera "
						+ PWBaseTest.getFailureContext().getErrorMessage());
			} 
			if (UploadVideoPage.ValidateCameraCountIncrement()) {
				PWLog.Pass(className, " Count of camera increased by one successfully");
			} else {
				PWLog.Fail(className, " Count of camera did not increase by one "
						+ PWBaseTest.getFailureContext().getErrorMessage());
			 }}

		// TC_34 recording COUNT==================================================================================================
		@TestMeta(user = UserType.ADMIN, navPath = "cameras")
		@Test(dataProvider = "loginData", enabled = false, priority = 34, groups = { "Smoke" })
		public void M_689_VisionAi_Login_34(Method method, Map<String, String> testData) {

			UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
			String className = this.getClass().getSimpleName();

			if (UploadVideoPage.GetInitialRecordingCount()) {
				PWLog.Pass(className, " Get initial recording count and clicked on start recording successfully");
			} else {
				PWLog.Fail(className, " Not get initial recording count "
						+ PWBaseTest.getFailureContext().getErrorMessage());
			}
			if (UploadVideoPage.GetFinalRecordingCount()) {
				PWLog.Pass(className, " Get final recording count successfully");
			} else {
				PWLog.Fail(className, " Not get final recording count "
						+ PWBaseTest.getFailureContext().getErrorMessage());
			}
			if (UploadVideoPage.VerifyRecordingCountIncreasedByOne()) {
				PWLog.Pass(className, " Count of recording increased by one successfully");
			} else {
				PWLog.Fail(className, " Count of recording did not increase by one "
						+ PWBaseTest.getFailureContext().getErrorMessage());
			}}

		// TC_35 Maximize and Minimize camera live footage==================================================================================================
		@TestMeta(user = UserType.ADMIN, navPath = "cameras")
		@Test(dataProvider = "loginData", enabled = false, priority = 35, groups = { "Smoke" })
		public void M_689_VisionAi_Login_35(Method method, Map<String, String> testData) {

			UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
			String className = this.getClass().getSimpleName();

			if (UploadVideoPage.Maximize_And_Minimize_Camera()) {
				PWLog.Pass(className, " Camera live footage maximized and minimized successfully");
			} else {
				PWLog.Fail(className, " Camera live footage not maximized/minimized successfully "
						+ PWBaseTest.getFailureContext().getErrorMessage());
			}}

		// TC_36 Stop recording==================================================================================================
		@TestMeta(user = UserType.ADMIN, navPath = "cameras")
		@Test(dataProvider = "loginData", enabled = false, priority = 36, groups = { "Smoke" })
		public void M_689_VisionAi_Login_36(Method method, Map<String, String> testData) {

			UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
			String className = this.getClass().getSimpleName();

			if (UploadVideoPage.Stop_Recording_Camera()) {
				PWLog.Pass(className, " Recording stopped and camera changed to Not recording successfully");
			} else {
				PWLog.Fail(className, " Recording not stopped / camera did not change to Not recording "
						+ PWBaseTest.getFailureContext().getErrorMessage());
			}}

		// TC_37 Delete Test Camera==================================================================================================
		@TestMeta(user = UserType.ADMIN, navPath = "cameras")
		@Test(dataProvider = "loginData", enabled = false, priority = 37, groups = { "Smoke" })
		public void M_689_VisionAi_Login_37(Method method, Map<String, String> testData) {

			UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
			String className = this.getClass().getSimpleName();

			if (UploadVideoPage.Delete_TestCamera()) {
				PWLog.Pass(className, " Test Camera deleted successfully and confirmation message displayed");
			} else {
				PWLog.Fail(className, " Test Camera not deleted / confirmation message not displayed "
						+ PWBaseTest.getFailureContext().getErrorMessage());
			}}

		// TC_38 Search camera==================================================================================================
		@TestMeta(user = UserType.ADMIN, navPath = "cameras")
		@Test(dataProvider = "loginData", enabled = false, priority = 38, groups = { "Smoke" })
		public void M_689_VisionAi_Login_38(Method method, Map<String, String> testData) {

			UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
			String className = this.getClass().getSimpleName();

			if (UploadVideoPage.Search_Camera()) {
				PWLog.Pass(className, " Searched camera is displayed after searching successfully");
			} else {
				PWLog.Fail(className, " Searched camera is not displayed after searching "
						+ PWBaseTest.getFailureContext().getErrorMessage());
			}}

		// TC_39 Filter camera by recording status==================================================================================================
		@TestMeta(user = UserType.ADMIN, navPath = "cameras")
		@Test(dataProvider = "loginData", enabled = false, priority = 39, groups = { "Smoke" })
		public void M_689_VisionAi_Login_39(Method method, Map<String, String> testData) {

			UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
			String className = this.getClass().getSimpleName();

			if (UploadVideoPage.Filter_Camera_By_Recording_Status()) {
				PWLog.Pass(className, " Recording and Not recording filters show only matching cameras successfully");
			} else {
				PWLog.Fail(className, " Recording / Not recording filters did not show only matching cameras "
						+ PWBaseTest.getFailureContext().getErrorMessage());
			}}

		// TC_40 Create camera and validate creation date & time==================================================================================================
		@TestMeta(user = UserType.ADMIN, navPath = "cameras")
		@Test(dataProvider = "loginData", enabled = false, priority = 40, groups = { "Smoke" })
		public void M_689_VisionAi_Login_40(Method method, Map<String, String> testData) {

			UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
			String className = this.getClass().getSimpleName();

			if (UploadVideoPage.Create_Camera_And_Validate_CreationDate()) {
				PWLog.Pass(className, " Camera created and card shows today's date successfully");
			} else {
				PWLog.Fail(className, " Camera creation date validation failed "
						+ PWBaseTest.getFailureContext().getErrorMessage());
			}}

		// TC_41 Start recording on Test B Camera and validate Stop Recording button appears==================================================================================================
		@TestMeta(user = UserType.ADMIN, navPath = "cameras")
		@Test(dataProvider = "loginData", enabled = false, priority = 41, groups = { "Smoke" })
		public void M_689_VisionAi_Login_41(Method method, Map<String, String> testData) {

			UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
			String className = this.getClass().getSimpleName();

			if (UploadVideoPage.Start_Recording_TestBCamera()) {
				PWLog.Pass(className, " Clicked Start Recording on Test B Camera and Stop Recording button appeared successfully");
			} else {
				PWLog.Fail(className, " Stop Recording button did not appear after Start Recording on Test B Camera "
						+ PWBaseTest.getFailureContext().getErrorMessage());
			}}

		// TC_42 Validate Videos page counts match Analytics page counts==================================================================================================
		@TestMeta(user = UserType.ADMIN, navPath = "videos")
		@Test(dataProvider = "loginData", enabled = false, priority = 42, groups = { "Smoke" })
		public void M_689_VisionAi_Login_42(Method method, Map<String, String> testData) {

			UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
			String className = this.getClass().getSimpleName();

			if (UploadVideoPage.Verify_Video_And_Analytics_Counts()) {
				PWLog.Pass(className, " Videos page counts (Total Uploads / Processed / RAW) match Analytics page counts successfully");
			} else {
				PWLog.Fail(className, " Videos page counts did not match Analytics page counts "
						+ PWBaseTest.getFailureContext().getErrorMessage());
			}}

		// TC_43 Validate Cameras page counts match Analytics page counts==================================================================================================
		@TestMeta(user = UserType.ADMIN, navPath = "cameras")
		@Test(dataProvider = "loginData", enabled = false, priority = 43, groups = { "Smoke" })
		public void M_689_VisionAi_Login_43(Method method, Map<String, String> testData) {

			UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
			String className = this.getClass().getSimpleName();

			if (UploadVideoPage.Verify_Camera_And_Analytics_Counts()) {
				PWLog.Pass(className, " Cameras page counts (Total Cameras / Recording / Idle) match Analytics page counts successfully");
			} else {
				PWLog.Fail(className, " Cameras page counts did not match Analytics page counts "
						+ PWBaseTest.getFailureContext().getErrorMessage());
			}}

		// TC_44 Validate used storage increases after uploading a video==================================================================================================
		@TestMeta(user = UserType.ADMIN, navPath = "analytics")
		@Test(dataProvider = "loginData", enabled = false, priority = 44, groups = { "Smoke" })
		public void M_689_VisionAi_Login_44(Method method, Map<String, String> testData) {

			UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
			String className = this.getClass().getSimpleName();

		String fileName = testData.get("clipone");

		  if (UploadVideoPage.GETINITIALUSEDSTOREAGE()) {
			PWLog.Pass(className, "Initial used storage captured successfully");
		} else {
			PWLog.Fail(className, "Failed to capture initial used storage: " + PWBaseTest.getFailureContext().getErrorMessage());
		}

		
		if (UploadVideoPage.UPload_Video()) {
			PWLog.Pass(className, " Video Uploaded successfully");
		} else {
			PWLog.Fail(className,
					" Video not Uploaded sucessfully " + PWBaseTest.getFailureContext().getErrorMessage());
		}
       if (UploadVideoPage.Uploadmultiplefile(fileName)) {
			PWLog.Pass(className, "Video uploaded successfully");
		} else {
			PWLog.Fail(className, "Video upload failed: " + PWBaseTest.getFailureContext().getErrorMessage());
		}

		if (UploadVideoPage.CompleteUploadVideoforstoregused()) {
			PWLog.Pass(className, "Complete upload video successfully and video is Appear in video list");

		} else {
			PWLog.Fail(className, " Unable to complete upload video and video is not Appear in video list "
					+ PWBaseTest.getFailureContext().getErrorMessage());
		}

		  if (UploadVideoPage.validatecountincresed()) {
			PWLog.Pass(className, "Used storage increased after uploading the video successfully");
		} else {
			PWLog.Fail(className, "Used storage did not increase after upload: " + PWBaseTest.getFailureContext().getErrorMessage());
		}

	}

	// TC_45 Validate search API count increases after a Detect Violence search==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "analytics")
	@Test(dataProvider = "loginData", enabled = false, priority = 45, groups = { "Smoke" })
	public void M_689_VisionAi_Login_45(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		// 1) Analytics -> capture initial search count, then move to Conversations
		if (UploadVideoPage.GetInitialSearchCount()) {
			PWLog.Pass(className, "Initial search count captured successfully");
		} else {
			PWLog.Fail(className, "Failed to capture initial search count: " + PWBaseTest.getFailureContext().getErrorMessage());
		}

		// 2) Conversations -> select first video -> Done -> Detect Violence -> Send
		if (UploadVideoPage.RunDetectViolenceSearch()) {
			PWLog.Pass(className, "Detect Violence search submitted successfully");
		} else {
			PWLog.Fail(className, "Failed to submit Detect Violence search: " + PWBaseTest.getFailureContext().getErrorMessage());
		}

		// 3) Back to Analytics -> verify the search count increased
		if (UploadVideoPage.ValidateSearchCountIncreased()) {
			PWLog.Pass(className, "Search count increased after Detect Violence search successfully");
		} else {
			PWLog.Fail(className, "Search count did not increase after search: " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_46 Validate analyse count increases after running an analyse on a video==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "analytics")
	@Test(dataProvider = "loginData", enabled = false, priority = 46, groups = { "Smoke" })
	public void M_689_VisionAi_Login_46(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		// 1) Analytics -> capture initial analyse count, then move to Conversations
		if (UploadVideoPage.GetInitialAnalyseCount()) {
			PWLog.Pass(className, "Initial analyse count captured successfully");
		} else {
			PWLog.Fail(className, "Failed to capture initial analyse count: " + PWBaseTest.getFailureContext().getErrorMessage());
		}

		// 2) Conversations -> analyse tab -> select first video -> Done -> analyse option
		if (UploadVideoPage.RunAnalyseVideo()) {
			PWLog.Pass(className, "Analyse submitted successfully");
		} else {
			PWLog.Fail(className, "Failed to submit analyse: " + PWBaseTest.getFailureContext().getErrorMessage());
		}

		// 3) Back to Analytics -> verify the analyse count increased
		if (UploadVideoPage.ValidateAnalyseCountIncreased()) {
			PWLog.Pass(className, "Analyse count increased after analyse successfully");
		} else {
			PWLog.Fail(className, "Analyse count did not increase after analyse: " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_47 Validate current balance is reduced by the credits used after a Detect Violence search==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "analytics")
	@Test(dataProvider = "loginData", enabled = false, priority = 47, groups = { "Smoke" })
	public void M_689_VisionAi_Login_47(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		// Analytics -> capture balance -> run Detect Violence search -> back to Analytics ->
		//   read recent count -> assert (initial balance - recent count) == new balance
		if (UploadVideoPage.ValidateCurrentBalanceAfterSearch()) {
			PWLog.Pass(className, "Current balance correctly reduced by credits used after search");
		} else {
			PWLog.Fail(className, "Current balance did not match expected value: " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_48 Validate consumed count increases by the credits used after a Detect Violence search==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "analytics")
	@Test(dataProvider = "loginData", enabled = false, priority = 48, groups = { "Smoke" })
	public void M_689_VisionAi_Login_48(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		// Analytics -> capture consumed count -> run Detect Violence search -> back to Analytics ->
		//   read recent count -> assert (initial consumed count + recent count) == new consumed count
		if (UploadVideoPage.ValidateConsumedCountAfterSearch()) {
			PWLog.Pass(className, "Consumed count correctly increased by credits used after search");
		} else {
			PWLog.Fail(className, "Consumed count did not match expected value: " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_49 Create a team on the Settings page and verify it appears after creation==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "settings")
	@Test(dataProvider = "loginData", enabled = false, priority = 49, groups = { "Smoke" })
	public void M_689_VisionAi_Login_49(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		// Settings -> create team "Automation team" -> verify it appears in the team list
		if (UploadVideoPage.CreateTeamAndVerify()) {
			PWLog.Pass(className, "Team 'Automation team' created and appears successfully");
		} else {
			PWLog.Fail(className, "Failed to create or verify team: " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_50 Validate handover warning when the sole Admin tries to leave the team==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "settings")
	@Test(dataProvider = "loginData", enabled = false, priority = 50, groups = { "Smoke" })
	public void M_689_VisionAi_Login_50(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		// Settings -> open Automation Team -> click Leave team -> verify handover warning message appears
		if (UploadVideoPage.ValidateLeaveTeamHandoverWarning()) {
			PWLog.Pass(className, "Handover warning displayed when Admin tried to leave the team");
		} else {
			PWLog.Fail(className, "Handover warning not displayed: " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_51 Validate error message when inviting a user who has not signed in yet==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "settings")
	@Test(dataProvider = "loginData", enabled = false, priority = 51, groups = { "Smoke" })
	public void M_689_VisionAi_Login_51(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		// Settings -> open Automation Team -> Add user -> enter email -> Send invite -> verify error message
		if (UploadVideoPage.ValidateInviteNotSignedInError()) {
			PWLog.Pass(className, "Error message displayed when inviting a user who has not signed in");
		} else {
			PWLog.Fail(className, "Invite error message not displayed: " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_52 Validate error message when inviting with an invalid email format==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "settings")
	@Test(dataProvider = "loginData", enabled = false, priority = 52, groups = { "Smoke" })
	public void M_689_VisionAi_Login_52(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		// Settings -> open Automation Team -> Add user -> enter invalid email -> Send invite -> verify error message
		if (UploadVideoPage.ValidateInviteInvalidEmailError()) {
			PWLog.Pass(className, "Error message displayed when inviting with an invalid email format");
		} else {
			PWLog.Fail(className, "Invalid-email error message not displayed: " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_53 Validate Pending status tag appears after sending an invite==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "settings")
	@Test(dataProvider = "loginData", enabled = false, priority = 53, groups = { "Smoke" })
	public void M_689_VisionAi_Login_53(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		// Settings -> open Automation Team -> Add user -> enter valid email -> Send invite -> verify Pending tag
		if (UploadVideoPage.ValidateInvitePendingStatus()) {
			PWLog.Pass(className, "Pending status tag displayed after sending invite");
		} else {
			PWLog.Fail(className, "Pending status tag not displayed: " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_54 Validate already-pending message when re-inviting the same email==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "settings")
	@Test(dataProvider = "loginData", enabled = false, priority = 54, groups = { "Smoke" })
	public void M_689_VisionAi_Login_54(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		// Settings -> open Automation Team -> Add user -> enter same email -> select Member -> Send invite -> verify message
		if (UploadVideoPage.ValidateInviteAlreadyPending()) {
			PWLog.Pass(className, "Already-pending invite message displayed when re-inviting the same email");
		} else {
			PWLog.Fail(className, "Already-pending invite message not displayed: " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_55 Validate pending invite is removed after cancelling it==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "settings")
	@Test(dataProvider = "loginData", enabled = false, priority = 55, groups = { "Smoke" })
	public void M_689_VisionAi_Login_55(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		// Settings -> verify pending invite visible -> click cancel -> verify it is no longer displayed
		if (UploadVideoPage.ValidateCancelPendingInvite()) {
			PWLog.Pass(className, "Pending invite removed after cancelling");
		} else {
			PWLog.Fail(className, "Pending invite not removed after cancelling: " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	//************************************API KEY************************** */
	// TC_56 Create an API key and validate the success popup appears==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "settings")
	@Test(dataProvider = "loginData", enabled = false, priority = 56, groups = { "Smoke" })
	public void M_689_VisionAi_Login_56(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		// Settings -> API tab -> Create API key -> enter name -> Create -> verify success popup
		if (UploadVideoPage.CreateApiKeyAndVerify()) {
			PWLog.Pass(className, "API key created and success popup displayed");
		} else {
			PWLog.Fail(className, "API key creation success popup not displayed: " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_57 Validate expiry date shown when selecting 3 months expiration==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "settings")
	@Test(dataProvider = "loginData", enabled = false, priority = 57, groups = { "Smoke" })
	public void M_689_VisionAi_Login_57(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		// Settings -> API tab -> Create API key -> select 3 months -> verify expiry date == today + 3 months
		if (UploadVideoPage.ValidateApiKeyExpiry3Months()) {
			PWLog.Pass(className, "Expiry date correctly shows today + 3 months");
		} else {
			PWLog.Fail(className, "Expiry date did not match today + 3 months: " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_58 Validate the user can select a custom expiration date==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "settings")
	@Test(dataProvider = "loginData", enabled = false, priority = 58, groups = { "Smoke" })
	public void M_689_VisionAi_Login_58(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		// Settings -> API tab -> Create API key -> select Custom -> enter tomorrow's date -> verify accepted
		if (UploadVideoPage.ValidateApiKeyCustomDate()) {
			PWLog.Pass(className, "User was able to select a custom expiration date");
		} else {
			PWLog.Fail(className, "User could not select a custom expiration date: " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_59 Validate the created/expiry dates shown on the first API key card==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "settings")
	@Test(dataProvider = "loginData", enabled = false, priority = 59, groups = { "Smoke" })
	public void M_689_VisionAi_Login_59(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		// Settings -> API tab -> read first API key card -> verify Created == today and Expires == today + 3 months
		if (UploadVideoPage.ValidateApiKeyCreatedAndExpiryDate()) {
			PWLog.Pass(className, "API key Created date is today and Expires date is 3 months later");
		} else {
			PWLog.Fail(className, "API key created/expiry dates did not validate: " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_60 Delete the first API key and validate it was removed==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "settings")
	@Test(dataProvider = "loginData", enabled = false, priority = 60, groups = { "Smoke" })
	public void M_689_VisionAi_Login_60(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		// Settings -> API tab -> delete first API key -> confirm -> verify the card count dropped by one
		if (UploadVideoPage.DeleteApiKeyAndVerify()) {
			PWLog.Pass(className, "API key deleted successfully");
		} else {
			PWLog.Fail(className, "API key was not deleted: " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}
//******************************Conversation ***************************** */

	// TC_61 Select the first 4 videos in a conversation and validate the "4 videos selected" text==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "conversations")
	@Test(dataProvider = "loginData", enabled = false, priority = 61, groups = { "Smoke" })
	public void M_689_VisionAi_Login_61(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		// Conversations -> Select video -> click first 4 tiles -> Done -> verify "4 videos selected" text
		if (UploadVideoPage.SelectFourVideosAndVerify()) {
			PWLog.Pass(className, "4 videos selected and summary text validated");
		} else {
			PWLog.Fail(className, "4 videos were not selected as expected: " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_62 Validate Detect Violence search results all fall within 00:00:45 - 00:00:57==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "conversations")
	@Test(dataProvider = "loginData", enabled = false, priority = 62, groups = { "Smoke" })
	public void M_689_VisionAi_Login_62(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		// Conversations -> Select video 'All_queries_03' -> Done -> Detect Violence -> Send ->
		//   wait up to 90s -> every result card's time range must be within 00:00:45 - 00:00:57
		if (UploadVideoPage.ValidateViolenceResultsInTimeRange()) {
			PWLog.Pass(className, "All Detect Violence result cards are within the allowed time range");
		} else {
			PWLog.Fail(className, "Detect Violence results validation failed: " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_63 Validate Nudity query results all fall within 00:00:10 - 00:00:20==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "conversations")
	@Test(dataProvider = "loginData", enabled = false, priority = 63, groups = { "Smoke" })
	public void M_689_VisionAi_Login_63(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		// Conversations -> Select video 'All_queries_03' -> Done -> Nudity query -> Send ->
		//   wait up to 90s -> every result card's time range must be within 00:00:10 - 00:00:20
		if (UploadVideoPage.ValidateNudityResultsInTimeRange()) {
			PWLog.Pass(className, "All Nudity result cards are within the allowed time range");
		} else {
			PWLog.Fail(className, "Nudity results validation failed: " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_64 Validate Vehicle query results all fall within 00:00:56 - 00:01:12==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "conversations")
	@Test(dataProvider = "loginData", enabled = false, priority = 64, groups = { "Smoke" })
	public void M_689_VisionAi_Login_64(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		// Conversations -> Select video 'All_queries_03' -> Done -> Vehicle query -> Send ->
		//   wait up to 90s -> every result card's time range must be within 00:00:56 - 00:01:12
		if (UploadVideoPage.ValidateVehicleResultsInTimeRange()) {
			PWLog.Pass(className, "All Vehicle result cards are within the allowed time range");
		} else {
			PWLog.Fail(className, "Vehicle results validation failed: " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_65 Validate Detect Logo query results all fall within 00:00:20 - 00:00:30==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "conversations")
	@Test(dataProvider = "loginData", enabled = false, priority = 65, groups = { "Smoke" })
	public void M_689_VisionAi_Login_65(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		// Conversations -> Select video 'All_queries_03' -> Done -> Detect Logo query -> Send ->
		//   wait up to 90s -> every result card's time range must be within 00:00:20 - 00:00:30
		if (UploadVideoPage.ValidateLogoResultsInTimeRange()) {
			PWLog.Pass(className, "All Detect Logo result cards are within the allowed time range");
		} else {
			PWLog.Fail(className, "Detect Logo results validation failed: " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_66 Validate Detect Emotions query results all fall within 00:00:00 - 00:00:10==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "conversations")
	@Test(dataProvider = "loginData", enabled = false, priority = 66, groups = { "Smoke" })
	public void M_689_VisionAi_Login_66(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		// Conversations -> Select video 'All_queries_03' -> Done -> Detect Emotions query -> Send ->
		//   wait up to 90s -> every result card's time range must be within 00:00:00 - 00:00:10
		if (UploadVideoPage.ValidateEmotionsResultsInTimeRange()) {
			PWLog.Pass(className, "All Detect Emotions result cards are within the allowed time range");
		} else {
			PWLog.Fail(className, "Detect Emotions results validation failed: " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_67 Validate Detect Animals query results all fall within 00:00:30 - 00:00:45==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "conversations")
	@Test(dataProvider = "loginData", enabled = false, priority = 67, groups = { "Smoke" })
	public void M_689_VisionAi_Login_67(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		// Conversations -> Select video 'All_queries_03' -> Done -> Detect Animals query -> Send ->
		//   wait up to 90s -> every result card's time range must be within 00:00:30 - 00:00:45
		if (UploadVideoPage.ValidateAnimalsResultsInTimeRange()) {
			PWLog.Pass(className, "All Detect Animals result cards are within the allowed time range");
		} else {
			PWLog.Fail(className, "Detect Animals results validation failed: " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_68 Validate the Analyze-mode Summarise reply semantically matches the expected summary==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "conversations")
	@Test(dataProvider = "loginData", enabled = false, priority = 68, groups = { "Smoke" })
	public void M_689_VisionAi_Login_68(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		// Expected summary stored in Excel column qc_expectedtext (key 'expectedtext').
		String expectedText = testData.get("expectedtext");

		// Conversations -> Analyze -> Select video 'All_queries_03' -> Done -> Summarise -> Send ->
		//   compare reply MEANING with expected text via sentence-embedding similarity (>= 0.7).
		if (UploadVideoPage.ValidateSummariseAnalysis(expectedText)) {
			PWLog.Pass(className, "Summarise reply semantically matches the expected summary");
		} else {
			PWLog.Fail(className, "Summarise reply did not match expected summary: " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_69 Validate the Analyze-mode Key Events reply semantically matches the expected text==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "conversations")
	@Test(dataProvider = "loginData", enabled = false, priority = 69, groups = { "Smoke" })
	public void M_689_VisionAi_Login_69(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();
		String expectedText = testData.get("expectedtext");

		// Conversations -> Analyze -> Select video 'All_queries_03' -> Done -> Key Events -> Send ->
		//   compare reply MEANING with expected text via sentence-embedding similarity (>= 0.7).
		if (UploadVideoPage.ValidateKeyEventsAnalysis(expectedText)) {
			PWLog.Pass(className, "Key Events reply semantically matches the expected text");
		} else {
			PWLog.Fail(className, "Key Events reply did not match expected text: " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_70 Validate the Analyze-mode Content Suitability reply semantically matches the expected text==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "conversations")
	@Test(dataProvider = "loginData", enabled = false, priority = 70, groups = { "Smoke" })
	public void M_689_VisionAi_Login_70(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();
		String expectedText = testData.get("expectedtext");

		// Conversations -> Analyze -> Select video 'All_queries_03' -> Done -> Content Suitability -> Send ->
		//   compare reply MEANING with expected text via sentence-embedding similarity (>= 0.7).
		if (UploadVideoPage.ValidateContentSuitabilityAnalysis(expectedText)) {
			PWLog.Pass(className, "Content Suitability reply semantically matches the expected text");
		} else {
			PWLog.Fail(className, "Content Suitability reply did not match expected text: " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_71 Validate the Analyze-mode Hashtag reply semantically matches the expected text==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "conversations")
	@Test(dataProvider = "loginData", enabled = false, priority = 71, groups = { "Smoke" })
	public void M_689_VisionAi_Login_71(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();
		String expectedText = testData.get("expectedtext");

		// Conversations -> Analyze -> Select video 'All_queries_03' -> Done -> Hashtag -> Send ->
		//   compare reply MEANING with expected text via sentence-embedding similarity (>= 0.7).
		if (UploadVideoPage.ValidateHashtagAnalysis(expectedText)) {
			PWLog.Pass(className, "Hashtag reply semantically matches the expected text");
		} else {
			PWLog.Fail(className, "Hashtag reply did not match expected text: " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_72 Validate the Analyze-mode Setting reply semantically matches the expected text==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "conversations")
	@Test(dataProvider = "loginData", enabled = false, priority = 72, groups = { "Smoke" })
	public void M_689_VisionAi_Login_72(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();
		String expectedText = testData.get("expectedtext");

		// Conversations -> Analyze -> Select video 'All_queries_03' -> Done -> Setting -> Send ->
		//   compare reply MEANING with expected text via sentence-embedding similarity (>= 0.7).
		if (UploadVideoPage.ValidateSettingAnalysis(expectedText)) {
			PWLog.Pass(className, "Setting reply semantically matches the expected text");
		} else {
			PWLog.Fail(className, "Setting reply did not match expected text: " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_73 Validate the Analyze-mode Title reply semantically matches the expected text==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "conversations")
	@Test(dataProvider = "loginData", enabled = false, priority = 73, groups = { "Smoke" })
	public void M_689_VisionAi_Login_73(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();
		String expectedText = testData.get("expectedtext");

		// Conversations -> Analyze -> Select video 'All_queries_03' -> Done -> Title -> Send ->
		//   compare reply MEANING with expected text via sentence-embedding similarity (>= 0.7).
		if (UploadVideoPage.ValidateTitleAnalysis(expectedText)) {
			PWLog.Pass(className, "Title reply semantically matches the expected text");
		} else {
			PWLog.Fail(className, "Title reply did not match expected text: " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_74 Validate the Analyze-mode free-text question reply semantically matches the expected text==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "conversations")
	@Test(dataProvider = "loginData", enabled =  false, priority = 74, groups = { "Smoke" })
	public void M_689_VisionAi_Login_74(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();
		String expectedText = testData.get("expectedtext");
		String question = "tell me what happing in wedding";

		// Conversations -> Analyze -> Select video 'All_queries_03' -> Done -> type question -> Send ->
		//   compare reply MEANING with expected text via sentence-embedding similarity (>= 0.7).
		if (UploadVideoPage.ValidateCustomQuestionAnalysis(question, expectedText)) {
			PWLog.Pass(className, "Free-text question reply semantically matches the expected text");
		} else {
			PWLog.Fail(className, "Free-text question reply did not match expected text: " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_75 Validate every video in a collection Detect Violence search is named 'VI_33_Violance'==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "conversations")
	@Test(dataProvider = "loginData", enabled =  false, priority = 75, groups = { "Smoke" })
	public void M_689_VisionAi_Login_75(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();
		String expectedName = "VI_33_Violance";

		// Conversations -> Collections source -> 'Automation_collection' -> Done -> Detect Violence -> Send
		//   -> open each result card, read its video name -> PASS only if every name == expectedName.
		if (UploadVideoPage.ValidateCollectionViolenceVideoNames(expectedName)) {
			PWLog.Pass(className, "All returned videos are named '" + expectedName + "'");
		} else {
			PWLog.Fail(className, "Not all returned videos match '" + expectedName + "': " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_76 Validate every video in a collection Detect Nudity search is named 'VI_36_Nudity'==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "conversations")
	@Test(dataProvider = "loginData", enabled = false, priority = 76, groups = { "Smoke" })
	public void M_689_VisionAi_Login_76(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();
		String expectedName = "VI_36_Nudity";

		if (UploadVideoPage.ValidateCollectionNudityVideoNames(expectedName)) {
			PWLog.Pass(className, "All returned videos are named '" + expectedName + "'");
		} else {
			PWLog.Fail(className, "Not all returned videos match '" + expectedName + "': " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_77 Validate every video in a collection Detect Vehicles search is named 'VI_32_Vehicles'==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "conversations")
	@Test(dataProvider = "loginData", enabled = false, priority = 77, groups = { "Smoke" })
	public void M_689_VisionAi_Login_77(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();
		String expectedName = "VI_32_Vehicles";

		if (UploadVideoPage.ValidateCollectionVehicleVideoNames(expectedName)) {
			PWLog.Pass(className, "All returned videos are named '" + expectedName + "'");
		} else {
			PWLog.Fail(className, "Not all returned videos match '" + expectedName + "': " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_78 Validate every video in a collection Detect Logo search is named 'VI_31_Logos'==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "conversations")
	@Test(dataProvider = "loginData", enabled = false, priority = 78, groups = { "Smoke" })
	public void M_689_VisionAi_Login_78(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();
		String expectedName = "VI_31_Logos";

		if (UploadVideoPage.ValidateCollectionLogoVideoNames(expectedName)) {
			PWLog.Pass(className, "All returned videos are named '" + expectedName + "'");
		} else {
			PWLog.Fail(className, "Not all returned videos match '" + expectedName + "': " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_79 Validate every video in a collection Detect Emotions search is named 'VI_35_Emotions'==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "conversations")
	@Test(dataProvider = "loginData", enabled = false, priority = 79, groups = { "Smoke" })
	public void M_689_VisionAi_Login_79(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();
		String expectedName = "VI_35_Emotions";

		if (UploadVideoPage.ValidateCollectionEmotionsVideoNames(expectedName)) {
			PWLog.Pass(className, "All returned videos are named '" + expectedName + "'");
		} else {
			PWLog.Fail(className, "Not all returned videos match '" + expectedName + "': " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_80 Validate every video in a collection Detect Animals search is named 'VI_34_Animals'==================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "conversations")
	@Test(dataProvider = "loginData", enabled = false, priority = 80, groups = { "Smoke" })
	public void M_689_VisionAi_Login_80(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();
		String expectedName = "VI_34_Animals";

		if (UploadVideoPage.ValidateCollectionAnimalsVideoNames(expectedName)) {
			PWLog.Pass(className, "All returned videos are named '" + expectedName + "'");
		} else {
			PWLog.Fail(className, "Not all returned videos match '" + expectedName + "': " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_81 Validate the Analyze-mode free-text question is echoed back in the chat (no reply validation)=================================================================================================
	// NOTE: method named _83 because M_689_VisionAi_Login_81 / _82 are already used by the legacy large-file tests.
	@TestMeta(user = UserType.ADMIN, navPath = "conversations")
	@Test(dataProvider = "loginData", enabled = false, priority = 81, groups = { "Smoke" })
	public void M_689_VisionAi_Login_83(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();
		String question = "tell me what happing in wedding";

		// Conversations -> Analyze -> Select video 'All_queries_03' -> Done -> type question -> Send ->
		//   read the displayed question and PASS only if it equals the question we entered.
		if (UploadVideoPage.ValidateQuestionDisplayedInChat(question)) {
			PWLog.Pass(className, "Entered question is displayed correctly in the chat");
		} else {
			PWLog.Fail(className, "Displayed question did not match the entered question: " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_82 Delete one conversation from History and validate the History count decreases by one=================================================================================================
	// NOTE: method named _84 because M_689_VisionAi_Login_82 is already used by the legacy large-length test.
	@TestMeta(user = UserType.ADMIN, navPath = "conversations")
	@Test(dataProvider = "loginData", enabled = false, priority = 82, groups = { "Smoke" })
	public void M_689_VisionAi_Login_84(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		// Conversations -> read "History (N)" -> delete the first conversation -> confirm ->
		//   PASS only if the History count drops to N-1.
		if (UploadVideoPage.ValidateHistoryCountDecreasesAfterDelete()) {
			PWLog.Pass(className, "History count decreased by one after deleting a conversation");
		} else {
			PWLog.Fail(className, "History count did not decrease by one: " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_83 Rename the first conversation in History and validate the title actually changes=================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "conversations")
	@Test(dataProvider = "loginData", enabled = false, priority = 83, groups = { "Smoke" })
	public void M_689_VisionAi_Login_85(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		// Conversations -> read the first conversation title -> Rename -> clear -> type "Automation history" ->
		//   Enter -> PASS only if the title is no longer the initial text (it got edited).
		if (UploadVideoPage.ValidateRenameConversation()) {
			PWLog.Pass(className, "Conversation title was edited successfully after rename");
		} else {
			PWLog.Fail(className, "Conversation title was not edited: " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_84 Validate a past conversation's results are restored when re-opened from History=================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "conversations")
	@Test(dataProvider = "loginData", enabled = false, priority = 84, groups = { "Smoke" })
	public void M_689_VisionAi_Login_86(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		// Conversations -> Analyze -> Select 'VI_35_Emotions' -> Done -> Title query -> Settings query ->
		//   capture results -> Start new conversation -> refresh -> open first History item ->
		//   PASS only if the previous conversation results are displayed again.
		if (UploadVideoPage.ValidateHistoryRestoredAfterReopen()) {
			PWLog.Pass(className, "Previous conversation history is displayed after re-opening from History");
		} else {
			PWLog.Fail(className, "Conversation history did not appear after re-opening: " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_85 Validate the Embed graph matches the baseline image stored in Excel (qc_images)=================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "conversations")
	@Test(dataProvider = "loginData", enabled = false, priority = 85, groups = { "Smoke" })
	public void M_689_VisionAi_Login_87(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();
		String videoName = "VI_35_Emotions";
		String expectedImageName = testData.get("images");

		// Conversations -> Embed -> Select 'VI_35_Emotions' -> Done -> wait for graph -> screenshot the chart
		//   -> compare with data/images/<expectedImageName> -> PASS only if the images match.
		if (UploadVideoPage.ValidateEmbedGraphMatchesImage(videoName, expectedImageName)) {
			PWLog.Pass(className, "Embed graph matches the baseline image for '" + videoName + "'");
		} else {
			PWLog.Fail(className, "Embed graph did not match the baseline image: " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_86 Validate the Embed graph for a COLLECTION matches the baseline image stored in Excel (qc_images)=================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "conversations")
	@Test(dataProvider = "loginData", enabled = false, priority = 86, groups = { "Smoke" })
	public void M_689_VisionAi_Login_88(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();
		String collectionName = "Automation_collection";
		String expectedImageName = testData.get("images");

		// Conversations -> Embed -> Collections source -> Select 'Automation_collection' -> Done -> wait for
		//   graph -> screenshot the chart -> compare with data/images/<expectedImageName> -> PASS if they match.
		if (UploadVideoPage.ValidateEmbedCollectionGraphMatchesImage(collectionName, expectedImageName)) {
			PWLog.Pass(className, "Embed collection graph matches the baseline image for '" + collectionName + "'");
		} else {
			PWLog.Fail(className, "Embed collection graph did not match the baseline image: " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_87 Validate clicking a point on the Embed graph opens its video and can be maximized=================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "conversations")
	@Test(dataProvider = "loginData", enabled = false, priority = 87, groups = { "Smoke" })
	public void M_689_VisionAi_Login_89(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();
		String videoName = "VI_35_Emotions";

		// Conversations -> Embed -> Select 'VI_35_Emotions' -> Done -> click a point on the graph ->
		//   'Selected embedding' panel opens -> Maximize -> PASS only if the maximized video view appears.
		if (UploadVideoPage.ValidateEmbedPointClickAndMaximize(videoName)) {
			PWLog.Pass(className, "Clicked a graph point and the video maximized successfully");
		} else {
			PWLog.Fail(className, "Could not click a graph point / maximize the video: " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_88 Validate the "source selection changed" warning appears when the source changes mid-conversation=================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "conversations")
	@Test(dataProvider = "loginData", enabled = false, priority = 88, groups = { "Smoke" })
	public void M_689_VisionAi_Login_90(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		// Select video 'All_queries_03' -> Done -> Detect Violence -> Send -> switch source to Collections ->
		//   select 'Automation_collection' -> PASS only if the source-change warning is displayed.
		if (UploadVideoPage.ValidateSourceChangeWarning()) {
			PWLog.Pass(className, "Source selection changed warning is displayed");
		} else {
			PWLog.Fail(className, "Source selection changed warning was not displayed: " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_89 Validate starting a fresh conversation clears the current result block=================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "conversations")
	@Test(dataProvider = "loginData", enabled = false, priority = 89, groups = { "Smoke" })
	public void M_689_VisionAi_Login_91(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		// Select video 'All_queries_03' -> Done -> Detect Violence -> Send -> result block visible ->
		//   Start new conversation -> PASS only if the result block is no longer displayed.
		if (UploadVideoPage.ValidateFreshConversationClearsResult()) {
			PWLog.Pass(className, "Result block cleared after starting a fresh conversation");
		} else {
			PWLog.Fail(className, "Result block was not cleared after fresh conversation: " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_90 Invite a user to Automation Team, accept it from Gmail in a separate Edge browser, land on dashboard=================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "settings")
	@Test(dataProvider = "loginData", enabled =false, priority = 90, groups = { "Smoke" })
	public void M_689_VisionAi_Login_92(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();
		String inviteEmail = "avision185@gmail.com";

		// Settings -> Automation Team -> Add user -> enter email -> Send invite. Then in a separate Edge
		//   browser open Gmail -> open the invite email -> Join Automation Team -> PASS if dashboard appears.
		if (UploadVideoPage.ValidateTeamInviteEmailAndJoin(inviteEmail)) {
			PWLog.Pass(className, "Invite accepted from Gmail and navigated to the dashboard");
		} else {
			PWLog.Fail(className, "Could not accept invite / reach dashboard: " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	// TC_91 Member leaves Automation Team (in Edge) and the team is no longer visible=================================================================================================
	@TestMeta(user = UserType.ADMIN, navPath = "settings")
	@Test(dataProvider = "loginData", enabled =false, priority = 91, groups = { "Smoke" })
	public void M_689_VisionAi_Login_93(Method method, Map<String, String> testData) {

		UploadVideoPage UploadVideoPage = new UploadVideoPage(getPage());
		String className = this.getClass().getSimpleName();

		// (In Edge as avison185) Profile -> Switch -> Automation Team -> Settings -> Automation Team ->
		//   Leave group -> Confirm -> refresh -> PASS only if Automation Team is no longer visible.
		if (UploadVideoPage.ValidateMemberLeaveTeam()) {
			PWLog.Pass(className, "Automation Team no longer visible after the member left");
		} else {
			PWLog.Fail(className, "Automation Team still visible after leaving: " + PWBaseTest.getFailureContext().getErrorMessage());
		}
	}

	
	 
		
		
		 
		
		
		
		
		
		
		
		
	 
	 
	 
	
	
	
 
	
	
	
	
	
	
	
	

	@DataProvider(name = "loginData")
	public Object[][] loginData(Method method) {
		Map<String, String> rowData = GetExcelRow(method.getName(), "", sheetName);
		return new Object[][] { { rowData } };
	}
} // EOF
