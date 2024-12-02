package com.udise.portal.service.udise_manager.impl;

import com.udise.portal.dao.JobRecordDao;
import com.udise.portal.entity.Job;
import com.udise.portal.entity.JobRecord;
import com.udise.portal.enums.JobStatus;
import com.udise.portal.service.docker_manager.DockerManager;
import com.udise.portal.service.error_log.ErrorLogManager;
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
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UdiseService2 {
    private final SimpMessagingTemplate messagingTemplate;

    private static final Logger log = LogManager.getLogger(UdiseService1.class);
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); // Adjust the pattern as needed
    DecimalFormat df=new DecimalFormat("0.##########");
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

    private Map<Long,Boolean> liveJobs;

    @Autowired
    private ErrorLogManager errorLogManager;


    public UdiseService2(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }


    @Async
    public void startChromeService(DockerVo dockerVo, Long jobId, String containerId, List<JobRecord> jobRecordList, Job job) throws InterruptedException, IOException {
        String url = String.format("http://%s:%d/wd/hub", dockerVo.getContainerName(), 4444); //for prod
        int loginTimeOut=vncLoginTimeOut;
//        String url = String.format("http://localhost:%d/wd/hub",  dockerVo.getHostPort()); //for dev
        try{
            WebDriver driver = null; // Declare driver her
//            job.setJobStatus(JobStatus.IN_PROGRESS);
            log.info("Job Start");
            String userid=String.valueOf(job.getAppUser().getId());
            try {
                // Set Chrome options and capabilities
                ChromeOptions chromeOptions = new ChromeOptions();
                DesiredCapabilities capabilities = new DesiredCapabilities();
                capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
                driver = new RemoteWebDriver(new URL(url), capabilities);

//                driver=new ChromeDriver();
                log.info("Chrome Start");
//                messagingTemplate.convertAndSend("/topic/"+userid, new SocketResponseVo("JOB_STARTED", "job started testing"));

                driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
                driver.manage().window().maximize();

                // Create WebDriverWait instance
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

                // Navigate to the UDISE portal login page
                driver.get("https://sdms.udiseplus.gov.in/p2/v1/login?state-id=108");
                log.info("Site Rendered");
                messagingTemplate.convertAndSend("/topic/"+userid, new SocketResponseVo("JOB_STARTED", "job started testing"));
                // Wait until the URL or page state changes after the manual click
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
                System.out.println("after return ");
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
                update(wait,driver,jobId);
            } catch (WebDriverException e) {
                log.error("WebDriver encountered an error", e);
            } catch (Exception e) {
                log.error("An unexpected error occurred", e);
            }
            finally {
                // Close the driver safely
                log.info("in case of return finally block");
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


    public void update(WebDriverWait wait,WebDriver driver,Long jobId) throws InterruptedException {
        List<JobRecord> records = jobRecordManager.getJobRecord(jobId);
        ((JavascriptExecutor) driver).executeScript("document.body.style.zoom='80%'");
        for (JobRecord record : records) {
            if(record.getJobStatus()!=JobStatus.COMPLETED){
                try{
                    record.setJobStatus(JobStatus.IN_PROGRESS);
                    jobRecordDao.update(record);
                    WebElement sectionSearchContainer = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("ul.sectionSearch.mt-2")));
                    WebElement classDropdown = sectionSearchContainer.findElement(By.cssSelector("select.form-select.w210"));
                    Select classSelect = new Select(classDropdown);
                    classSelect.selectByVisibleText(record.getClassName());
                    WebElement sectionDropdown = sectionSearchContainer.findElement(By.cssSelector("select.form-select.w150"));
                    Select sectionSelect = new Select(sectionDropdown);

                    sectionSelect.selectByVisibleText(record.getSection());
                    WebElement search_input = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//input[@placeholder='Search']")));
                    search_input.clear();
                    search_input.sendKeys(df.format(record.getStudentPen()));
                    List<WebElement> rows = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("tbody[role='rowgroup'] tr")));
                    WebElement row1=rows.get(0);
                    List<WebElement> cols = row1.findElements(By.cssSelector("td"));
                    WebElement actionBtn = cols.get(5);
                    if(actionBtn.getText().contains("Completed")) continue;

                    WebElement gp = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), 'GP')]")));
                    WebElement ep = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), 'EP')]")));
                    WebElement fp = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), 'FP')]")));
                    if(gp.getAttribute("class").contains("incomplete")){
                        gp.click();
                        Thread.sleep(1000);
                        generalProfileUpdate(wait, record, driver);
                        enrolmentProfileUpdate(wait, record, driver);
                        facilityProfileUpdate(wait, record, driver);
                    }else if(ep.getAttribute("class").contains("incomplete")){
                        ep.click();
                        Thread.sleep(1000);
                        enrolmentProfileUpdate(wait, record, driver);
                        facilityProfileUpdate(wait, record, driver);
                    }else if(fp.getAttribute("class").contains("incomplete")){
                        fp.click();
                        Thread.sleep(1000);
                        facilityProfileUpdate(wait, record, driver);
                    }else continue;

                }catch (Exception e){
                    log.info("error while doing operation in student :{}",record.getStudentName());
                }
            }
        }
    }

    private void generalProfileUpdate(WebDriverWait wait,JobRecord record,WebDriver driver) throws InterruptedException {
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
            errorLogManager.logError(record,e, "Context info about this error", "ERROR",
                    this.getClass().getName(), "fillGeneralProfile");
            log.info("Error In General Profile: {}",e.getCause());
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
            e.printStackTrace();
            throw e;
        }
    }
    private void enrolmentProfileUpdate(WebDriverWait wait,JobRecord record,WebDriver driver) throws InterruptedException {
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
            log.info("Error In Enrolment Profile");
            errorLogManager.logError(record,e, "Context info about this error", "ERROR",
                    this.getClass().getName(), "enrollmentProfileerror");
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
            e.printStackTrace();
            throw e;
        }
    }
    private void facilityProfileUpdate(WebDriverWait wait,JobRecord record,WebDriver driver) throws InterruptedException {
        try{
            if(record.isSLD()){
                WebElement screenedForSldRadio = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='radio' and @value='1' and @formcontrolname='screenedForSld']")));
                screenedForSldRadio.click();
            }else{
                WebElement screenedForSldRadio = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='radio' and @value='2' and @formcontrolname='screenedForSld']")));
                screenedForSldRadio.click();
            }

            if(record.isASD()){
                WebElement autismSpectrumDisorderRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='radio' and @value='1' and @formcontrolname='autismSpectrumDisorder']")));
                autismSpectrumDisorderRadioBtn.click();
            }else{
                WebElement autismSpectrumDisorderRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='radio' and @value='2' and @formcontrolname='autismSpectrumDisorder']")));
                autismSpectrumDisorderRadioBtn.click();
            }

            if(record.isADHD()){
                WebElement attentionDeficitHyperactiveDisorderRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='radio' and @value='1' and @formcontrolname='attentionDeficitHyperactiveDisorder']")));
                attentionDeficitHyperactiveDisorderRadioBtn.click();
            }else{
                WebElement attentionDeficitHyperactiveDisorderRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='radio' and @value='2' and @formcontrolname='attentionDeficitHyperactiveDisorder']")));
                attentionDeficitHyperactiveDisorderRadioBtn.click();
            }

            if(record.isGifted()){
                WebElement giftedChildrenRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//input[@type='radio' and @value='1' and @formcontrolname='giftedChildrenYn']")));
                giftedChildrenRadioBtn.click();
            }else{
                WebElement giftedChildrenRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//input[@type='radio' and @value='2' and @formcontrolname='giftedChildrenYn']")));
                giftedChildrenRadioBtn.click();
            }

            if(record.isSportsChamp()){
                WebElement olympdsNlcRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//input[@type='radio' and @value='1' and @formcontrolname='olympdsNlc']")));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", olympdsNlcRadioBtn);
                olympdsNlcRadioBtn.click();
            }else{
                WebElement olympdsNlcRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//input[@type='radio' and @value='2' and @formcontrolname='olympdsNlc']")));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", olympdsNlcRadioBtn);
                olympdsNlcRadioBtn.click();
            }

            if(record.isParticipatedNCC()){
                WebElement nccNssRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//input[@type='radio' and @value='1' and @formcontrolname='nccNssYn']")));
                nccNssRadioBtn.click();
            }else{
                WebElement nccNssRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//input[@type='radio' and @value='2' and @formcontrolname='nccNssYn']")));
                nccNssRadioBtn.click();
            }

            if(record.isDigitalyLiterate()){
                WebElement digitalCapableRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//input[@type='radio' and @value='1' and @formcontrolname='digitalCapableYn']")));
                digitalCapableRadioBtn.click();
            }else{
                WebElement digitalCapableRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//input[@type='radio' and @value='2' and @formcontrolname='digitalCapableYn']")));
                digitalCapableRadioBtn.click();
            }

            WebElement heightInCm = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//input[@formcontrolname='heightInCm']")));
            if(heightInCm.getAttribute("value").isBlank()){
                heightInCm.sendKeys(df.format(record.getHeight()));
            }
            WebElement weightInKg = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//input[@formcontrolname='weightInKg']")));
            if(weightInKg.getAttribute("value").isBlank()){
                weightInKg.sendKeys(df.format(record.getWeight()));
            }


            WebElement distanceDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@formcontrolname='distanceFrmSchool']")));
            Select distanceSelect = new Select(distanceDropdown);
            distanceSelect.selectByValue("2");

            // Locate the dropdown element
            WebElement parentEducationDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@formcontrolname='parentEducation']")));
            Select parentEducationSelect = new Select(parentEducationDropdown);
            parentEducationSelect.selectByValue("5");

            List<WebElement> saveButtons = wait.until(
                    ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//button[./span[normalize-space(text())='Save']]"))
            );
            log.info("list of save btns:{}",saveButtons.size());
            WebElement saveButton = saveButtons.get(2);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", saveButton);
            Thread.sleep(2000);
            saveButton.click();
            log.info("After save button click FP");

            log.info("after btn click");
            WebElement closeBtnFP = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Close')]")));
            Thread.sleep(1000);
            closeBtnFP.click();
            List<WebElement> nextBtns = wait.until(
                    ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//button[./span[normalize-space(text())='Next']]"))
            );
            Thread.sleep(1000);
            nextBtns.get(2).click();
            try{
                WebElement fpCompleteDataBtn = wait.until(
                        ExpectedConditions.elementToBeClickable(By.xpath("//button[span[normalize-space(text())='Complete Data']]"))
                );
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", fpCompleteDataBtn);

                wait.until(ExpectedConditions.elementToBeClickable(fpCompleteDataBtn));
                Thread.sleep(1000);
                fpCompleteDataBtn.click();

                WebElement fpOkayBtn = wait.until(
                        ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Okay')]"))
                );
                Thread.sleep(1000);
                fpOkayBtn.click();
                WebElement confirmOkeyBtn = wait.until(
                        ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Okay')]"))
                );
                Thread.sleep(1000);
                confirmOkeyBtn.click();
                record.setJobStatus(JobStatus.COMPLETED);
                jobRecordDao.update(record);
            }catch (Exception e){
                errorLogManager.logError(record,e, "Already profile completed", "ERROR",
                        this.getClass().getName(), "facilityProfileUpdate Error");
                e.printStackTrace();
            }

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
        }catch (Exception e){
            errorLogManager.logError(record,e, "Context info about this error", "ERROR",
                    this.getClass().getName(), "facilityProfileUpdate");
            log.info("Error In Facility Profile");
            e.printStackTrace();
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
            throw e;
        }
    }

}
