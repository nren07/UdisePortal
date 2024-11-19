package com.udise.portal.service.udise_manager.impl;

import com.udise.portal.dao.JobRecordDao;
import com.udise.portal.entity.Job;
import com.udise.portal.entity.JobRecord;
import com.udise.portal.enums.JobStatus;
import com.udise.portal.service.docker_manager.DockerManager;
import com.udise.portal.service.job.job_record_manager.JobRecordManager;
import com.udise.portal.vo.docker.DockerVo;
import com.udise.portal.vo.job.SocketResponseVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UdiseService2 {
    private final SimpMessagingTemplate messagingTemplate;

    private static final Logger log = LogManager.getLogger(UdiseService1.class);
    @Autowired
    private DockerManager dockerManager;

    @Autowired
    private JobRecordManager jobRecordManager;

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private JobRecordDao jobRecordDao;

    @Value("${vnc-login-timeout:#{70}}")
    private int vncLoginTimeOut;

    private Map<Long,Boolean>liveJobs;

    public UdiseService2(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
        liveJobs=new HashMap<>();
    }


    @Async
    public void startChromeService(DockerVo dockerVo, Long jobId, String containerId, List<JobRecord> jobRecordList, Job job) throws InterruptedException, IOException {
//        log.info("in start chrome function call");
//        String url = String.format("http://%s:%d/wd/hub", dockerVo.getContainerName(), 4444); //for prod
        int loginTimeOut=vncLoginTimeOut;
//        String url = String.format("http://localhost:%d/wd/hub",  dockerVo.getHostPort()); //for dev
        try{
            WebDriver driver = null; // Declare driver her
            job.setJobStatus(JobStatus.IN_PROGRESS);
            log.info("Job Start");
            String userid=String.valueOf(job.getAppUser().getId());
            try {
                // Set Chrome options and capabilities
//                ChromeOptions chromeOptions = new ChromeOptions();
//                DesiredCapabilities capabilities = new DesiredCapabilities();
//                capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
//                driver = new RemoteWebDriver(new URL(url), capabilities);
//                System.setProperty("webdriver.chrome.driver", "C:/drivers/chromedriver.exe");
                driver = new ChromeDriver();
                log.info("Chrome Start");

                driver.manage().timeouts().implicitlyWait(6, TimeUnit.MINUTES);
                driver.manage().window().maximize();

                // Create WebDriverWait instance
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofMinutes(1));
                messagingTemplate.convertAndSend("/topic/"+userid, new SocketResponseVo("JOB_STARTED", "job started testing"));
                // Navigate to the UDISE portal login page
                driver.get("https://sdms.udiseplus.gov.in/p2/v1/login?state-id=108");
                log.info("Site Rendered");

                String currentUrl = driver.getCurrentUrl();
                WebElement usernameField = wait.until(ExpectedConditions.elementToBeClickable(By.id("username-field")));
                usernameField.sendKeys("08122604122");
                WebElement passwordField = driver.findElement(By.id("password-field"));
                passwordField.sendKeys("Lokesh@888");

                while (driver.getCurrentUrl().equals(currentUrl) && loginTimeOut>=0) {
                    Thread.sleep(1000);
                    loginTimeOut--;  // Poll every second
                    log.info(loginTimeOut);
                }
                if(loginTimeOut<0){
                    return;
                }

                WebElement ele1=wait.until(ExpectedConditions.elementToBeClickable(By.className("clearfix"))); //current academic year
                ele1.click();
                log.info("after current academic year");
                WebElement ele2= wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space(text())='Close']"))); //PopUp close
                ele2.click();
                log.info("after close ");
                WebElement ele3= wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()=' School Dashboard ']"))); //DashBoard
                ele3.click();
                log.info("after Dashboard ");
                List<WebElement> rows = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("tbody[role='rowgroup'] tr")));
                log.info("length of rows are : {}",rows.size());
                WebElement row1=rows.get(0);
                List<WebElement> cols = row1.findElements(By.cssSelector("td"));
                WebElement actionBtn = cols.get(7);
                WebElement viewAndManageBtn = actionBtn.findElement(By.xpath("//a[contains(text(), 'View/Manage')]"));
                viewAndManageBtn.click();
                update(wait,driver);
            } catch (WebDriverException e) {
                log.error("WebDriver encountered an error", e);
            } catch (Exception e) {
                log.error("An unexpected error occurred", e);
            }
            finally {
                if (driver != null) {
                    driver.quit();
                }
                liveJobs.remove(jobId);
                messagingTemplate.convertAndSend("/topic/"+userid, new SocketResponseVo("JOB_ENDED", "job Ended testing"));
                dockerManager.stopAndRemoveContainer(containerId,dockerVo);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void update(WebDriverWait wait,WebDriver driver) throws InterruptedException {
        // Locate the 'ul' container by class
        WebElement sectionSearchContainer = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("ul.sectionSearch.mt-2")));

        // Locate the 'Class' dropdown inside the 'ul' container
        WebElement classDropdown = sectionSearchContainer.findElement(By.cssSelector("select.form-select.w210"));
        Select classSelect = new Select(classDropdown);

        // Iterate over all 'Class' options
        List<WebElement> classOptions = classSelect.getOptions();
        for (WebElement classOption : classOptions) {
            String classText = classOption.getText();
            String classValue = classOption.getAttribute("value");
            System.out.println("Class: " + classText + " (Value: " + classValue + ")");
            Thread.sleep(500);
            if(classOption.isEnabled()){
                if(classOption.getText().equals("III")){
                    classSelect.selectByVisibleText(classOption.getText());
                    Thread.sleep(500);
                    log.info("inside if block");
                    WebElement sectionDropdown = sectionSearchContainer.findElement(By.cssSelector("select.form-select.w150"));
                    Select sectionSelect = new Select(sectionDropdown);
                    WebElement search_input = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//input[@placeholder='Search']")));

                    // Iterate over all 'Section' options
                    List<WebElement> sectionOptions = sectionSelect.getOptions();
                    int cnt1=0;
                    int cnt2=0;
                    int cnt3=0;
                    for (WebElement sectionOption : sectionOptions) {
                        if(sectionOption.isEnabled() && sectionOption.getText().equals("A")){
                            search_input.clear();
                            search_input.sendKeys("20046772697");
                            WebElement gp = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), 'GP')]")));
                            gp.click();
                            generalProfileUpdate(wait,driver);
                            Thread.sleep(10000);
                            enrolmentProfileUpdate(wait,driver);
                            Thread.sleep(10000);
                            facilityProfileUpdate(wait,driver);

//                            try{
//                                generalProfileUpdate(wait,driver);
//                            }catch (Exception e){
//                                e.printStackTrace();
//                                throw e;
////                                if(cnt1<=3){
////                                    cnt1++;
////                                    log.info("cnt1 is : {}",cnt1);
////                                    generalProfileUpdate(wait,driver);
////                                }else{
////                                    throw e;
////                                }
//                            }
//                            try{
//                                enrolmentProfileUpdate(wait);
//                            }catch (Exception e){
//                                e.printStackTrace();
//                                throw e;
////                                if(cnt2<=3){
////                                    cnt2++;
////                                    log.info("cnt2 is :{}",cnt2);
////                                    generalProfileUpdate(wait,driver);
////                                }else{
////                                    throw e;
////                                }
//                            }
//                            try{
//                                facilityProfileUpdate(wait,driver);
//                            }catch (Exception e){
//                                e.printStackTrace();
//                                if(cnt3<=3){
//                                    cnt3++;
//                                    log.info("cnt3 is :{}",cnt3);
//                                    facilityProfileUpdate(wait,driver);
//                                }else{
//                                    throw e;
//                                }
//                            }
                            Thread.sleep(20000);

//                            //go to dashboard
//                            List<WebElement> rows = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("tbody[role='rowgroup'] tr")));
//                            List<WebElement> cols = rows.get(4).findElements(By.cssSelector("td"));
//                            WebElement actionBtn = cols.get(7);
//                            List<WebElement> buttons = actionBtn.findElements(By.tagName("a"));
//                            WebElement viewAndManageBtn=buttons.get(0);
//                            viewAndManageBtn.click();
                            break;
                        }

                    }
                }
            }
        }
    }

    private void generalProfileUpdate(WebDriverWait wait,WebDriver driver) throws InterruptedException {
        try{
            Thread.sleep(5000);
            List<WebElement> saveButtons = wait.until(
                    ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//button[./span[normalize-space(text())='Save']]"))
            );
            log.info("list of save btns:{}",saveButtons.size());
            WebElement saveButton = saveButtons.get(0);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", saveButton);
            Thread.sleep(5000);
            saveButton.click();

            WebElement closeBtnGP = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Close')]")));
            closeBtnGP.click();
            WebElement nextBtnGP = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class, 'btnnext')]")));
            nextBtnGP.click();
            log.info("next Btn clicked");
        }catch (Exception e){
            log.info("Error In General Profile");
            e.printStackTrace();

            throw e;
        }
    }
    private void enrolmentProfileUpdate(WebDriverWait wait,WebDriver driver) throws InterruptedException {
        try{
            WebElement mediumOfInstructionDropDown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@formcontrolname='mediumOfInstruction']")));
            Select mediumOfInstructionSelect = new Select(mediumOfInstructionDropDown);
            // Select the first option by index (1-based index for the first option "19-English")
            mediumOfInstructionSelect.selectByIndex(1); // 0 is the "Select" option, so 1 is "19-English"
            // Locate the dropdown element
            WebElement languageGroupDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@formcontrolname='languageGroup']")));

            // Create a Select instance
            Select languageGroupSelect = new Select(languageGroupDropdown);

            // Select the first option by index (1-based index for "English_Hindi")
            languageGroupSelect.selectByIndex(1); // 0 is the "Select" option, so 1 is "English_Hindi"
            List<WebElement> saveButtons = wait.until(
                    ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//button[./span[normalize-space(text())='Save']]"))
            );
            log.info("list of save btns:{}",saveButtons.size());

            WebElement saveButton = saveButtons.get(1);

            // Scroll the element into view
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", saveButton);
            wait.until(ExpectedConditions.elementToBeClickable(saveButton));

            // Try clicking the button
            Thread.sleep(5000);
            saveButton.click();
            WebElement closeBtnEP = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Close')]")));
            closeBtnEP.click();

            List<WebElement> nextBtns = wait.until(
                    ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//button[./span[normalize-space(text())='Next']]"))
            );

            nextBtns.get(1).click();
            log.info("next Btn of ep clicked");
        }catch (Exception e){
            log.error("Error In Enrolment Profile");
            e.printStackTrace();
            throw e;
        }
    }

    private void facilityProfileUpdate(WebDriverWait wait,WebDriver driver) throws InterruptedException {
        try{
            Thread.sleep(5000);
            WebElement radioButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='olympdsNlc' and @value='2']")));
            radioButton.click();
            // Locate the dropdown element
            WebElement distanceDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@formcontrolname='distanceFrmSchool']")));

            // Create a Select instance
            Select distanceSelect = new Select(distanceDropdown);

            // Option 1: Select by index (e.g., first option other than "Select" is index 1)
            distanceSelect.selectByIndex(2); // Index 1 corresponds to "1 - Less than 1 km"

            // Locate the dropdown element
            WebElement parentEducationDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@formcontrolname='parentEducation']")));
            Select parentEducationSelect = new Select(parentEducationDropdown);
            parentEducationSelect.selectByIndex(4); // This will select "1 - Primary"
            log.info("befor save btn click");
            String mainWindowHandle = driver.getWindowHandle();
            log.info("Windows presents : {}",driver.getWindowHandles().size());

//            WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[span[contains(text(), ' Save ')]]")));
//            saveButton.click();
//            WebElement saveButtonFP = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[_ngcontent*='viw-c296']")));
//            saveButtonFP.click();
            List<WebElement> saveButtons = wait.until(
                    ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//button[./span[normalize-space(text())='Save']]"))
            );
            log.info("list of save btns:{}",saveButtons.size());
            WebElement saveButton = saveButtons.get(2);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", saveButton);
            Thread.sleep(5000);
            saveButton.click();
            log.info("After save button click FP");

            log.info("after btn click");
            WebElement closeBtnFP = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Close')]")));
            closeBtnFP.click();
            List<WebElement> nextBtns = wait.until(
                    ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//button[./span[normalize-space(text())='Next']]"))
            );

            nextBtns.get(2).click();
            Thread.sleep(5000);
            try{
                WebElement fpCompleteDataBtn = wait.until(
                        ExpectedConditions.elementToBeClickable(By.xpath("//button[span[normalize-space(text())='Complete Data']]"))
                );
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", fpCompleteDataBtn);

                wait.until(ExpectedConditions.elementToBeClickable(fpCompleteDataBtn));
                Thread.sleep(5000);
                fpCompleteDataBtn.click();

                WebElement fpOkayBtn = wait.until(
                        ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Okay')]"))
                );
                Thread.sleep(5000);
                fpOkayBtn.click();
                WebElement confirmOkeyBtn = wait.until(
                        ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Okay')]"))
                );
                confirmOkeyBtn.click();
            }catch (Exception e){
                e.printStackTrace();
            }

//            WebElement nextBtn2 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Next')]")));
//            nextBtn2.click();
            WebElement backToSchoolDashboardBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//button[span[normalize-space(text())='Back To School Dashboard']]"))
            );

// Scroll the element into view only when it's clickable
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", backToSchoolDashboardBtn);

// Optionally, wait for the element to become clickable after scrolling into view
            wait.until(ExpectedConditions.elementToBeClickable(backToSchoolDashboardBtn));

// Click the button
            Thread.sleep(5000);
            backToSchoolDashboardBtn.click();
//            generalProfileUpdate(wait,driver);
//            enrolmentProfileUpdate(wait,driver);
//            facilityProfileUpdate(wait,driver);

        }catch (Exception e){
            log.error("Error In Facility Profile");
            e.printStackTrace();
            throw e;
        }
    }


}
