package basic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;



public class LaunchTest {
	private String _hubURL;
	private String _firefoxhubURL;
	private String _serverAddress;
	private String userName;
	private String passWord;
	private int _participantsPerSession;
	private int _participantsWihVideo;
	private int _waitTime;
	private int _sessionCount;
	private WebDriver driver;
	private ArrayList<String> sessionData;

	public LaunchTest() throws Exception {
		Properties prop = new Properties();
		FileInputStream ip = new FileInputStream(System.getProperty("user.dir")+"\\config.properties");
		prop.load(ip);
		this._hubURL = prop.getProperty("thehubURL");
		this._serverAddress = prop.getProperty("serverAddress");
		this.userName = prop.getProperty("userName");
		this.passWord = prop.getProperty("passWord");
		this._participantsPerSession = Integer.parseInt(prop.getProperty("participantCount"));
		this._participantsWihVideo = Integer.parseInt(prop.getProperty("videoCount"));
		this._waitTime = Integer.parseInt(prop.getProperty("delaySeconds"));
		this._sessionCount = Integer.parseInt(prop.getProperty("sessionCount"));
		this.sessionData = getData("Create_Emp","Class");

		System.out.println();
		System.out.println("hub - " + this._hubURL);
		System.out.println("sessions - " + this._sessionCount);
		System.out.println("server - " + this._serverAddress);
		System.out.println("User Name - " + this.userName);
		System.out.println("PassWord - " + this.passWord);
		System.out.println("participant - " + this._participantsPerSession);
		System.out.println("video - " + this._participantsWihVideo);
		System.out.println("wait time - " + this._waitTime);
		System.out.println();

	}


	private void createDriver() throws MalformedURLException {


		// Define Desired capabilities
		DesiredCapabilities cap = new DesiredCapabilities();

		cap.setBrowserName("chrome");
		cap.setPlatform(Platform.WINDOWS);

		// Chrome Options definitions
		ChromeOptions option = new ChromeOptions();
		option.merge(cap);

		// use fake media stream
		option.addArguments("use-fake-device-for-media-stream");
		option.addArguments("use-fake-ui-for-media-stream");

		// Set Hub URL
		// String hubURL = "http://10.0.1.4:4444/wd/hub";
		driver = new RemoteWebDriver(new URL(this._hubURL), option);
		this.driver.manage().timeouts().pageLoadTimeout(this._waitTime, TimeUnit.SECONDS);
		this.driver.manage().timeouts().implicitlyWait(this._waitTime, TimeUnit.SECONDS);
		this.driver.manage().window().maximize();
		this.driver.manage().deleteAllCookies();

		//	driver1.get(this._serverAddress + studentURL + "#config.startAudioOnly=true");
		for(int i=0; i<=this.sessionData.size()/3;i++)
		{
			this.driver.get(this._serverAddress + this.sessionData.get(i*3));
		}
		
	}

	public void excuteLogin() throws Exception {
		createDriver();
		for(int i=0; i<=this.sessionData.size()/3;i++)
		{
			driver.get(this._serverAddress + this.sessionData.get(i*3));
			driver.findElement(By.xpath("//input[@id='username']")).sendKeys(this.sessionData.get(i*3+1));
			driver.findElement(By.xpath("//input[@id='password']")).sendKeys(this.sessionData.get(i*3+2));
			driver.findElement(By.xpath("//button[@type='submit']")).click();
			System.out.println("User logged in");
			Thread.sleep(10000);

			driver.findElement(By.xpath("//button[@class='mat-focus-indicator pb-4 button-style mat-raised-button mat-button-base mat-accent']")).click();
			System.out.println("Device set");
			Thread.sleep(10000);
			// put if
			WebElement element= null;
			element =driver.findElement(By.xpath("//body/app-root[1]/block-ui[1]/session[1]/instructor-session[1]//app-timer[1]/section[1]"));
			if (element==null)
			{

				driver.get(this._serverAddress);
				System.out.println("Page reload");
				try {
					Thread.sleep(10000);
					driver.findElement(By.xpath("//button[@class='mat-focus-indicator pb-4 button-style mat-raised-button mat-button-base mat-accent']")).click();
				}
				catch(Exception e)
				{
					System.out.println("Exception in device set");
					System.out.println(e.getMessage());
				}

			}	
		}
		

	}


	public ArrayList<String> getData(String testcaseName, String sheetName)  {
		ArrayList<String> a = new ArrayList<String>();
		try {

			FileInputStream fis = new FileInputStream("C:\\Users\\karun\\Elevate\\Performance\\resources\\SessionDatails.xlsx");
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			int sheets = workbook.getNumberOfSheets();
			for (int i = 0; i < sheets; i++) {
				if (workbook.getSheetName(i).equalsIgnoreCase(sheetName)) {
					XSSFSheet sheet = workbook.getSheetAt(i);
					// Identify Testcases coloum by scanning the entire 1st row

					Iterator<Row> rows = sheet.iterator();// sheet is collection of rows
					Row firstrow = rows.next();
					Iterator<Cell> ce = firstrow.cellIterator();// row is collection of cells
					int k = 0;
					int coloumn = 0;
					while (ce.hasNext()) {
						Cell value = ce.next();

						if (value.getStringCellValue().equalsIgnoreCase("SessionId")) {
							coloumn = k;

						}

						k++;
					}
					System.out.println(coloumn);

					//// once coloumn is identified then scan entire testcase coloum to identify

					while (rows.hasNext()) {

						Row r = rows.next();

						//		if (r.getCell(coloumn).getStringCellValue().equalsIgnoreCase(testcaseName)) {

						//// after you grab purchase testcase row = pull all the data of that row and
						//// feed into test

						Iterator<Cell> cv = r.cellIterator();
						while (cv.hasNext()) {
							Cell c = cv.next();
							if (c.getCellTypeEnum() == CellType.STRING) {

								a.add(c.getStringCellValue());
							} else {

								a.add(NumberToTextConverter.toText(c.getNumericCellValue()));

							}
						}
						//	}

					}

				}
			}
		}catch(IOException ex) {
			System.out.println("Unable to read excel file");
		}
		// fileInputStream argument



		return a;

	}





}
