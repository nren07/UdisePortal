package com.udise.portal.service.udise_manager.impl;
import com.udise.portal.dao.JobRecordDao;
import com.udise.portal.entity.ErrorLog;
import com.udise.portal.entity.Job;
import com.udise.portal.entity.JobRecord;
import com.udise.portal.enums.Category;
import com.udise.portal.enums.JobStatus;
import com.udise.portal.service.docker_manager.DockerManager;
import com.udise.portal.service.error_log.ErrorLogManager;
import com.udise.portal.service.job.job_record_manager.JobRecordManager;
import com.udise.portal.vo.docker.DockerVo;
import com.udise.portal.vo.job.SocketResponseVo;
import net.bytebuddy.implementation.bytecode.Throw;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class UdiseService3 {
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

    public UdiseService3(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Async
    public void startChromeService(DockerVo dockerVo, String containerId, List<JobRecord> jobRecordList, Job job,Map<Long,Boolean> liveJobs) throws InterruptedException, IOException {
        Long jobId=job.getId();
        String url = String.format("http://%s:%d/wd/hub", dockerVo.getContainerName(), 4444); //for prod
        int loginTimeOut=vncLoginTimeOut;
//        String url = String.format("http://localhost:%d/wd/hub",  dockerVo.getHostPort()); //for dev
        WebDriver driver = null; // Declare driver her
        job.setJobStatus(JobStatus.IN_PROGRESS);
        log.info("Job Start");
        String userid=String.valueOf(job.getAppUser().getId());
        try {
            //Set Chrome options and capabilities

            ChromeOptions chromeOptions = new ChromeOptions();
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
            driver = new RemoteWebDriver(new URL(url), capabilities);

//                driver=new ChromeDriver();
            log.info("Chrome Start");


            driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
            driver.manage().window().maximize();

            // Create WebDriverWait instance
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

            // Navigate to the UDISE portal login page
            driver.get("https://sdms.udiseplus.gov.in/p2/v1/login?state-id=108");
            log.info("Site Rendered");
//                messagingTemplate.convertAndSend("/topic/"+userid, new SocketResponseVo("JOB_STARTED", "job started testing"));
            // Wait until the URL or page state changes after the manual click
            String currentUrl = driver.getCurrentUrl();
            WebElement usernameField = wait.until(ExpectedConditions.elementToBeClickable(By.id("username-field")));
//            usernameField.sendKeys("08122604122");
            WebElement passwordField = driver.findElement(By.id("password-field"));
//            passwordField.sendKeys("Lokesh@888");
            messagingTemplate.convertAndSend("/topic/"+userid, new SocketResponseVo("JOB_STARTED", "job started testing"));

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

            WebElement udiseCodeElement = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.cssSelector("div.d-flex.flex-row.mb-1 span.form-read-olny")
                    )
            );
            System.out.println("udise code is :"+udiseCodeElement.getText());
            if(!udiseCodeElement.getText().equals("08122604122")){
                System.out.println("different school is there");
                WebElement imageElement = wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                By.cssSelector("img[src='assets/img/power.png']")
                        )
                );
                imageElement.click();
                return;
            }
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
            addStudent(wait,driver,jobId);
        } catch (MalformedURLException e) {
            log.error("Invalid hub URL: ", e);
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
    }

    public void addStudent(WebDriverWait wait,WebDriver driver,Long jobId) throws Exception {
        List<JobRecord> records = jobRecordManager.getJobRecord(jobId);
        ((JavascriptExecutor) driver).executeScript("document.body.style.zoom='80%'");
        for (JobRecord record : records) {
            if(record.getJobStatus()!=JobStatus.COMPLETED){
                try{
                    record.setJobStatus(JobStatus.IN_PROGRESS);
                    WebElement sectionSearchContainer = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("ul.sectionSearch.mt-2")));
                    // Locate the 'Class' dropdown inside the 'ul' container
                    WebElement classDropdown = sectionSearchContainer.findElement(By.cssSelector("select.form-select.w210"));
                    Select classSelect = new Select(classDropdown);
                    classSelect.selectByVisibleText(record.getClassName());
                    WebElement sectionDropdown = sectionSearchContainer.findElement(By.cssSelector("select.form-select.w150"));
                    Select sectionSelect = new Select(sectionDropdown);

                    sectionSelect.selectByVisibleText(record.getSection());

                    WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//button[normalize-space(.//span)='Add Student']")
                    ));
//                    addButton.click();

                    //
                    WebElement search_input = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//input[@placeholder='Search']")));
                    search_input.clear();
                    search_input.sendKeys(record.getStudentName());
                    Thread.sleep(2000);

                    try{

                        List<WebElement> rows = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("tbody[role='rowgroup'] tr")));
                        WebElement entryStatus;
                        WebElement dob;
                        WebElement row1;
                        WebElement profile;
                        row1=rows.get(0);
                        List<WebElement> cols = row1.findElements(By.cssSelector("td"));
                        entryStatus = cols.get(5);
                        dob=cols.get(4);
                        profile=cols.get(6);

                        if(entryStatus.getText().contains("Completed")){
                            continue;
                        }

                        WebElement gp = profile.findElement(By.xpath("//a[contains(text(), 'GP')]"));
                        gp.click();
                        generalProfileUpdate(wait, record, driver);
                        enrolmentProfileUpdate(wait, record, driver);
                        facilityProfileUpdate(wait, record, driver);
                    }catch (Exception e){
                        log.error(e);
                        addButton.click();
                        fillPersonalDetails(wait, record, driver);
                        generalProfileUpdate(wait, record, driver);
                        enrolmentProfileUpdate(wait, record, driver);
                        facilityProfileUpdate(wait, record, driver);
                        Thread.sleep(5000);
                    }
                }catch (Exception e){
                    log.info("error while doing operation in student :{}",record.getStudentName());
                    jobRecordDao.update(record);
                    e.printStackTrace();
                }
            }
        }
    }

    private void fillPersonalDetails(WebDriverWait wait,JobRecord record, WebDriver driver) throws InterruptedException {
        try{
            WebElement studentName=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@formcontrolname='studentName']"))); //student name
            studentName.sendKeys(record.getStudentName());
            WebElement genderDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@formcontrolname='gender']"))); //gender selection input element
            Select selectGender = new Select(genderDropdown);
            if(record.getGender().toLowerCase().equals("male")) selectGender.selectByVisibleText("1-Male");
            else if(record.getGender().toLowerCase().equals("female")) selectGender.selectByVisibleText("2-Female");
            else selectGender.selectByVisibleText("3-Transgender");
            WebElement dobInputField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@formcontrolname='dob']")));

            String formattedDateOfBirth = dateFormat.format(record.getDob());
            dobInputField.sendKeys(formattedDateOfBirth);
            WebElement studentCodeStateInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@formcontrolname='studentCodeState']")));
            studentCodeStateInput.sendKeys(df.format(record.getStateCode()));
            WebElement motherNameInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@formcontrolname='motherName']")));
            motherNameInput.sendKeys(record.getMotherName());
            WebElement fatherNameInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@formcontrolname='fatherName']")));
            fatherNameInput.sendKeys(record.getFatherName());
            WebElement aadharUUID = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@formcontrolname='uuid']")));
            String aadhar=record.getAadharNumber();
            aadharUUID.sendKeys(aadhar);
            if(!aadhar.equals("999999999999")){
                WebElement nameAsUuid = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@formcontrolname='nameAsUuid']")));
                nameAsUuid.sendKeys(record.getStudentName());
            }
            WebElement admnStartDate = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@formcontrolname='admnStartDate']"))); //admission start date
            String formattedDateOfAdmission = dateFormat.format(record.getDateOfAdmission());
            admnStartDate.sendKeys(formattedDateOfAdmission);
            WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[span[normalize-space(text())='Save']]")));
            log.info("after save loaded");
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", saveButton);
            Thread.sleep(2000);
            saveButton.click();
            List<WebElement>checkDefault1 = driver.findElements(By.id("flexCheckDefault1")); //mode set checkbox for class

            WebElement checkbox3 = wait.until(ExpectedConditions.elementToBeClickable(By.id("flexCheckDefault2"))); //mode set checkbox for father name

            WebElement checkbox4 = wait.until(ExpectedConditions.elementToBeClickable(By.id("flexCheckDefault3"))); //mode set checkbox for mother name

            WebElement checkbox5 = wait.until(ExpectedConditions.elementToBeClickable(By.id("flexCheckDefault4"))); //mode set checkbox for dob
            Thread.sleep(500);
            checkDefault1.get(0).click();

            checkDefault1.get(1).click();

            checkbox3.click();

            checkbox4.click();

            checkbox5.click();

            WebElement confirm=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space(text())='Confirm']"))); //mode set checkbox for dob
            confirm.click();
            try{
                WebElement fillProfileButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[normalize-space(text())='Fill General Profile']")));
                fillProfileButton.click();
            }catch (Exception e){
                try{
                    WebElement okeyButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Okay']")));
                    okeyButton.click();

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
                    errorLogManager.logError(record,e, "Context info about this error", "ERROR",
                            this.getClass().getName(), "fillPersonalDetails");
                    e.printStackTrace();
                    update(wait,driver,record);
                }catch (Exception exp){
                    throw exp;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("after back Btn in fill Personal profile");

            errorLogManager.logError(record,e, "Context info about this error", "ERROR",
                    this.getClass().getName(), "fillPersonalDetails");
            throw e;
        }
    }

    private void generalProfileUpdate(WebDriverWait wait,JobRecord record,WebDriver driver) throws InterruptedException {
        try{
            WebElement addressTextArea = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//textarea[@formcontrolname='address']")));
            if(addressTextArea.getAttribute("value").isBlank()){
                addressTextArea.sendKeys(record.getAddress());
            }

            WebElement pincodeField = wait.until(ExpectedConditions.elementToBeClickable
                    (By.xpath("//input[@name='pincode']")));
            if(pincodeField.getAttribute("value").isBlank()){
                pincodeField.sendKeys(df.format(record.getPinCode()));
            }

            WebElement studentMobile = wait.until(ExpectedConditions.elementToBeClickable
                    (By.xpath("//input[@formcontrolname='primaryMobile']")));
            if(studentMobile.getAttribute("value").isBlank()){
                studentMobile.sendKeys(df.format(record.getFatherMoNumber()));
            }

            WebElement dropdownContainer = driver.findElement(By.cssSelector(".ng-select-container"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dropdownContainer);
            Thread.sleep(2000);
            WebElement selectedValue = dropdownContainer.findElement(By.cssSelector(".ng-value-label"));
            if(selectedValue.getText().equals("Select")){
                dropdownContainer.click();
                // Step 3: Wait for the options to become visible
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(".ng-option"))); // Ensure ng-option is loaded
                // Step 4: Search for the desired option (optional)
                WebElement inputField = dropdownContainer.findElement(By.cssSelector("input[aria-autocomplete='list']"));
                inputField.sendKeys(record.getMotherTongue()); // Replace with the desired text
                Actions actions = new Actions(driver);


                WebElement dropdownPanel = driver.findElement(By.cssSelector(".ng-dropdown-panel"));
                WebElement desiredOption = dropdownPanel.findElement(By.cssSelector(".ng-star-inserted"));
                Thread.sleep(1000);
                desiredOption.click();
            }

            WebElement categoryDropDown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@formcontrolname='socCatId']")));

            Select categorySelect = new Select(categoryDropDown);
            if(record.getCategory()== Category.General)  categorySelect.selectByVisibleText("1 - GENERAL");
            else if(record.getCategory()== Category.OBC) categorySelect.selectByVisibleText("4 - OBC");
            else if(record.getCategory()== Category.SC) categorySelect.selectByVisibleText("2 - SC");
            else  categorySelect.selectByVisibleText("3 - ST");
            log.info("after select category");
            WebElement minorityDropDown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@formcontrolname='minorityId']")));
            Select minoritySelect = new Select(minorityDropDown);
            minoritySelect.selectByVisibleText(record.getMinorityGroup());
            log.info("after select minority group");

            if(record.isBpl()){
                WebElement isBplRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='isBplYN' and @type='radio' and following-sibling::label[text()='Yes']]")));
                isBplRadioBtn.click();

            }else{
                WebElement isBplRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='isBplYN' and @type='radio' and following-sibling::label[text()='No']]")));
                isBplRadioBtn.click();
            }
            if(record.isEws()){
                WebElement ewsRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='ewsYN' and @type='radio' and following-sibling::label[text()='Yes']]")));
                ewsRadioBtn.click();
            }else{
                WebElement ewsRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='ewsYN' and @type='radio' and following-sibling::label[text()='No']]")));
                ewsRadioBtn.click();
            }
            if(record.isCwsn()){
                WebElement cwsnRadioButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='cwsnYN' and @type='radio' and following-sibling::label[text()='Yes']]")));
                cwsnRadioButton.click();
            }else{
                WebElement cwsnRadioButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='cwsnYN' and @type='radio' and following-sibling::label[text()='No']]")));
                cwsnRadioButton.click();
            }

            WebElement isIndianNationalsYesRadioBtn= wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='natIndYN' and @type='radio' and following-sibling::label[text()='Yes']]")));
            isIndianNationalsYesRadioBtn.click();

            if(record.isOosc()){
                WebElement outOfSchoolChildRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='ooscYN' and @type='radio' and following-sibling::label[text()='Yes']]")));
                outOfSchoolChildRadioBtn.click();
            }else{
                WebElement outOfSchoolChildRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='ooscYN' and @type='radio' and following-sibling::label[text()='No']]")));
                outOfSchoolChildRadioBtn.click();
            }

            WebElement bloodGroupDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@formcontrolname='bloodGroup']")));
            Select bloodGroupSelect = new Select(bloodGroupDropdown);
            bloodGroupSelect.selectByValue("9");

            List<WebElement> saveButtons = wait.until(
                    ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//button[./span[normalize-space(text())='Save']]"))
            );

            WebElement saveButton = saveButtons.get(0);

            // Scroll the element into view
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", saveButton);
            wait.until(ExpectedConditions.elementToBeClickable(saveButton));

            Thread.sleep(2000);
            saveButton.click();
            Thread.sleep(2000);
            WebElement closeBtnEP = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Close')]")));
            closeBtnEP.click();
            Thread.sleep(2000);
            List<WebElement> nextBtns = wait.until(
                    ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//button[./span[normalize-space(text())='Next']]"))
            );

            nextBtns.get(0).click();

        }catch (Exception e){
            try{
                List<WebElement> buttons = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                        By.xpath("//button[contains(@style, 'display: inline-block')]")
                ));
                System.out.println(buttons.get(0).getText());
                buttons.get(0).click();
            }catch (Exception exception){
                log.error(exception);
            }
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
            WebElement admissionField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@formcontrolname='admnNumber']")));
            admissionField.clear();
            admissionField.sendKeys(df.format(record.getAdmissionNumber()));

            WebElement mediumOfInstructionDropDown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@formcontrolname='mediumOfInstruction']")));
            Select mediumOfInstructionSelect = new Select(mediumOfInstructionDropDown);

            mediumOfInstructionSelect.selectByVisibleText("19-English");

            WebElement languageGroupDropDown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@formcontrolname='languageGroup']")));

            Select languageGroupSelect = new Select(languageGroupDropDown);

            languageGroupSelect.selectByVisibleText("English_Hindi");

            WebElement enrStatusPYDropDown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@formcontrolname='enrStatusPY']")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", enrStatusPYDropDown);
            Select enrStatusPYSelect = new Select(enrStatusPYDropDown);
            enrStatusPYSelect.selectByVisibleText(record.getStatusOfStudentPrevAcademic());
            if(enrStatusPYDropDown.getText().equals("2-Studied at Other School")){
                WebElement classPyDropDown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("select[@formcontrolname='classPY']")));
                Select classPySelect = new Select(classPyDropDown);
                classPySelect.selectByVisibleText(record.getClassStudiedInPreviousAcademicYear());

                WebElement examResultPyDropDown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("select[@formcontrolname='examResultPy']")));
                Select examResultPySelect = new Select(examResultPyDropDown);
                examResultPySelect.selectByVisibleText(record.getResultOfExamination());

                WebElement marksObtained = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("input[@formcontrolname='examMarksPy']")));
                marksObtained.clear();
                marksObtained.sendKeys(df.format(record.getPercentage())); //attendancePy

                WebElement attendence = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("input[@formcontrolname='attendancePy']")));
                attendence.clear();
                attendence.sendKeys(df.format(record.getAttendance())); //attendancePy

            }

            WebElement enrUnderDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@formcontrolname='enrUnder']")));

            Select enrUnderSelect = new Select(enrUnderDropdown);

            enrUnderSelect.selectByVisibleText(record.getEnrolledUnder());

            List<WebElement> saveButtons = wait.until(
                    ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//button[./span[normalize-space(text())='Save']]"))
            );

            WebElement saveButton = saveButtons.get(1);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", saveButton);
            wait.until(ExpectedConditions.elementToBeClickable(saveButton));
            saveButton.click();
            WebElement closeBtnEP = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Close')]")));
            Thread.sleep(1000);
            closeBtnEP.click();
            List<WebElement> nextBtns = wait.until(
                    ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//button[./span[normalize-space(text())='Next']]"))
            );
            Thread.sleep(1000);
            nextBtns.get(1).click();
            log.info("next Btn of ep clicked");
        }catch (Exception e){
            try{
                List<WebElement> buttons = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                        By.xpath("//button[contains(@style, 'display: inline-block')]")
                ));
                System.out.println(buttons.get(0).getText());
                buttons.get(0).click();
            }catch (Exception exception){
                log.error(exception);
            }
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
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", heightInCm);
            if(heightInCm.getAttribute("value").isBlank()){
                heightInCm.sendKeys(df.format(record.getHeight()));
            }

            WebElement weightInKg = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//input[@formcontrolname='weightInKg']")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", weightInKg);
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

    public void update(WebDriverWait wait,WebDriver driver,JobRecord record) throws InterruptedException {
        try{
            WebElement sectionSearchContainer = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("ul.sectionSearch.mt-2")));
            WebElement classDropdown = sectionSearchContainer.findElement(By.cssSelector("select.form-select.w210"));
            Select classSelect = new Select(classDropdown);
            classSelect.selectByVisibleText(record.getClassName());
            Thread.sleep(1000);
            WebElement sectionDropdown = sectionSearchContainer.findElement(By.cssSelector("select.form-select.w150"));
            Select sectionSelect = new Select(sectionDropdown);

            sectionSelect.selectByVisibleText(record.getSection());

            WebElement search_input = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//input[@placeholder='Search']")));
            search_input.clear();
            search_input.sendKeys(record.getStudentName());
            Thread.sleep(2000);
            List<WebElement> rows = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("tbody[role='rowgroup'] tr")));
            WebElement row1=rows.get(0);
            List<WebElement> cols = row1.findElements(By.cssSelector("td"));
            WebElement actionBtn = cols.get(5);
            if(actionBtn.getText().contains("Completed")){
                throw new RuntimeException("Profile already completed");
            }

            WebElement gp = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), 'GP')]")));
            gp.click();
        }catch (Exception e){
            log.info("error while doing operation in student :{}",record.getStudentName());
            throw e;
        }
    }
}
