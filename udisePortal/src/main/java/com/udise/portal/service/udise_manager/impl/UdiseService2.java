package com.udise.portal.service.udise_manager.impl;

import com.udise.portal.dao.JobRecordDao;
import com.udise.portal.entity.Job;
import com.udise.portal.entity.JobRecord;
import com.udise.portal.enums.Category;
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
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.List;
import java.util.Locale;
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

    @Autowired
    private ErrorLogManager errorLogManager;

    public UdiseService2(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Async
    public void startChromeService(DockerVo dockerVo, String containerId, List<JobRecord> jobRecordList, Job job,Map<Long,Boolean> liveJobs) throws InterruptedException, IOException {
        Long jobId= job.getId();
        String url = String.format("http://%s:%d/wd/hub", dockerVo.getContainerName(), 4444); //for prod
        int loginTimeOut=vncLoginTimeOut;
//        String url = String.format("http://localhost:%d/wd/hub",  dockerVo.getHostPort()); //for dev
        try{
            WebDriver driver = null; // Declare driver her
            job.setJobStatus(JobStatus.IN_PROGRESS);
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


                driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                driver.manage().window().maximize();

                // Create WebDriverWait instance
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

                // Navigate to the UDISE portal login page
                driver.get("https://sdms.udiseplus.gov.in/p2/v1/login?state-id=108");
                log.info("Site Rendered");
                messagingTemplate.convertAndSend("/topic/"+userid, new SocketResponseVo("JOB_STARTED", "job started testing"));
                // Wait until the URL or page state changes after the manual click
                String currentUrl = driver.getCurrentUrl();
                WebElement usernameField = wait.until(ExpectedConditions.elementToBeClickable(By.id("username-field")));
//                usernameField.sendKeys("08122604122");
                WebElement passwordField = driver.findElement(By.id("password-field"));
//                passwordField.sendKeys("Lokesh@888");
                messagingTemplate.convertAndSend("/topic/"+userid, new SocketResponseVo("JOB_STARTED", "job started testing"));

                while (driver.getCurrentUrl().equals(currentUrl) && loginTimeOut>=0) {
                    Thread.sleep(1000);
                    loginTimeOut--;  // Poll every second
                    log.info(loginTimeOut);
                }
                if(loginTimeOut<0){
                    return;
                }
                messagingTemplate.convertAndSend("/topic/"+userid, new SocketResponseVo("JOB_ENDED", "job Ended testing"));
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
                job.setJobStatus(JobStatus.PENDING);
                log.error("WebDriver encountered an error", e);
            } catch (Exception e) {
                job.setJobStatus(JobStatus.PENDING);
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
            if(record.getJobStatus()==JobStatus.COMPLETED || record.getJobStatus()==JobStatus.ALREADY_COMPLETED) continue;
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
                if(record.getStudentPen()!=null){
                    String pen=String.valueOf(record.getStudentPen());
                    for(int i=0;i<pen.length();i++) search_input.sendKeys(pen.substring(i,i+1));
                }else{
                    String nameOfStudent=record.getStudentName();
                    for(int i=0;i<nameOfStudent.length();i++) search_input.sendKeys(nameOfStudent.substring(i,i+1));
                }

                List<WebElement> rows = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("tbody[role='rowgroup'] tr")));
                WebElement row1=rows.get(0);
                List<WebElement> cols = row1.findElements(By.cssSelector("td"));
                WebElement name=cols.get(2);
                WebElement actionBtn = cols.get(5);
                WebElement dob = cols.get(4);
                if (!name.getText().equalsIgnoreCase(record.getStudentName()) || !dob.getText().equals(dateFormat.format(record.getDob()))) continue;
                if(actionBtn.getText().contains("Completed")){
                    record.setJobStatus(JobStatus.ALREADY_COMPLETED);
                    jobRecordDao.update(record);
                    continue;
                }
                WebElement gp=cols.get(6).findElement(By.xpath("//a[contains(text(), 'GP')]"));
                WebElement ep=cols.get(6).findElement(By.xpath("//a[contains(text(), 'EP')]"));
                WebElement fp=cols.get(6).findElement(By.xpath("//a[contains(text(), 'FP')]"));

                if(gp.getAttribute("class").contains("incomplete")){
                    Thread.sleep(2000);
                    gp.click();
                    Thread.sleep(1000);
                    generalProfileUpdate(wait, record, driver);
                    enrolmentProfileUpdate(wait, record, driver);
                    facilityProfileUpdate(wait, record, driver);
                    confirmProfileUpdate(wait,record,driver);
                }else if(ep.getAttribute("class").contains("incomplete")){
                    Thread.sleep(2000);
                    ep.click();
                    Thread.sleep(1000);
                    enrolmentProfileUpdate(wait, record, driver);
                    facilityProfileUpdate(wait, record, driver);
                    confirmProfileUpdate(wait,record,driver);
                }else if(fp.getAttribute("class").contains("incomplete")){
                    Thread.sleep(2000);
                    fp.click();
                    Thread.sleep(1000);
                    facilityProfileUpdate(wait, record, driver);
                    confirmProfileUpdate(wait,record,driver);
                }else continue;

            }catch (Throwable e){
                try{
                    log.error("Okay button not found");
                    List<WebElement> buttons = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//button[contains(@style, 'display: inline-block')]")));
                    System.out.println(buttons);
                    buttons.get(0).click();
                    log.info("ok button not found");
                }catch (Exception exception){
                    log.error("ok button not found");
                }
                log.info("error while doing operation in student :{}",record.getStudentName());
                errorLogManager.logError(record,e, "Context info about this error", "ERROR", this.getClass().getName(), "fillPersonalDetails");
                try{
                    WebElement dashboardBtn= wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()=' School Dashboard ']"))); //DashBoard
                    dashboardBtn.click();
                    List<WebElement> rows = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("tbody[role='rowgroup'] tr")));
                    WebElement row1=rows.get(0);
                    List<WebElement> cols = row1.findElements(By.cssSelector("td"));
                    WebElement actionBtn = cols.get(7);
                    WebElement viewAndManageBtn = actionBtn.findElement(By.xpath("//a[contains(text(), 'View/Manage')]"));
                    viewAndManageBtn.click();
                }catch (Exception ex){
                    log.error("navigation code error in main try catch block");
                }
                if(record.getJobStatus()==JobStatus.COMPLETED) continue;
                record.setJobStatus(JobStatus.PENDING);
                jobRecordDao.update(record);
                log.info("error while doing operation in student :{}",record.getStudentName());
            }
        }
    }

    private void generalProfileUpdate(WebDriverWait wait,JobRecord record,WebDriver driver) throws Throwable {
        WebElement addressTextArea = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//textarea[@formcontrolname='address']")));

        if(addressTextArea.getAttribute("value").isBlank()){
            if(record.getAddress()!=null) addressTextArea.sendKeys(record.getAddress());
            else throw new RuntimeException("Address not present in your record for the student "+record.getStudentName());
        }

        WebElement pincodeField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='pincode']")));

        if(pincodeField.getAttribute("value").isBlank()){
            if(record.getPinCode()!=null) pincodeField.sendKeys(String.valueOf(record.getPinCode()));
            else throw new RuntimeException("Pin not present in your record for the student "+record.getStudentName());
        }

        WebElement studentMobile = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@formcontrolname='primaryMobile']")));

        if(studentMobile.getAttribute("value").isBlank()){
            if(record.getFatherMoNumber()!=null) studentMobile.sendKeys(String.valueOf(record.getFatherMoNumber()));
            else throw new RuntimeException("Mobile Number not present in your record for the student "+record.getStudentName());
        }

        List<WebElement> saveButtons = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//button[./span[normalize-space(text())='Save']]")));
        WebElement saveButton = saveButtons.get(0);
        wait.until(ExpectedConditions.elementToBeClickable(saveButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", saveButton);
        Thread.sleep(2000);
        WebElement dropdownContainer = driver.findElement(By.cssSelector(".ng-select-container"));

        WebElement selectedValue = dropdownContainer.findElement(By.cssSelector(".ng-value-label"));

        if(selectedValue.getText().equals("Select")){
            if(record.getMotherTongue()!=null){
                dropdownContainer.click();
                // Step 3: Wait for the options to become visible
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ng-option"))); // Ensure ng-option is loaded
                // Step 4: Search for the desired option (optional)
                WebElement inputField = dropdownContainer.findElement(By.cssSelector("input[aria-autocomplete='list']"));
                inputField.sendKeys(record.getMotherTongue()); // Replace with the desired text
                WebElement dropdownPanel = driver.findElement(By.cssSelector(".ng-dropdown-panel"));
                WebElement desiredOption = dropdownPanel.findElement(By.cssSelector(".ng-star-inserted"));
                desiredOption.click();
            }else throw new RuntimeException("Mother Tongue  not present in your record for the student "+record.getStudentName());
        }

        WebElement categoryDropDown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@formcontrolname='socCatId']")));
        Select categorySelect = new Select(categoryDropDown);
        String categorySelectValue = categorySelect.getFirstSelectedOption().getAttribute("value"); // or getText() for visible text

        if(categorySelectValue.isBlank()){
            if(record.getCategory()!=null){
                if(record.getCategory()== Category.General)  categorySelect.selectByVisibleText("1 - GENERAL");
                else if(record.getCategory()== Category.OBC) categorySelect.selectByVisibleText("4 - OBC");
                else if(record.getCategory()== Category.SC) categorySelect.selectByVisibleText("2 - SC");
                else  categorySelect.selectByVisibleText("3 - ST");
            }else throw new RuntimeException("Category not present");
        }

        WebElement minorityDropDown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@formcontrolname='minorityId']")));
        Select minoritySelect = new Select(minorityDropDown);
        String minoritySelectedValue = minoritySelect.getFirstSelectedOption().getAttribute("value"); // or getText() for visible text
        System.out.println(minoritySelectedValue);

        if(minoritySelectedValue.isBlank()){
            if(record.getMinorityGroup()!=null){
                minoritySelect.selectByVisibleText(record.getMinorityGroup());
            }else throw new RuntimeException("Minority select value is not present");
        }

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", saveButton);
        Thread.sleep(2000);

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
        String bloodGroupSelectedValue = bloodGroupSelect.getFirstSelectedOption().getAttribute("value"); // or getText() for visible text

        if(bloodGroupSelectedValue.isBlank()){
            bloodGroupSelect.selectByValue("9");
        }

        wait.until(ExpectedConditions.elementToBeClickable(saveButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", saveButton);
        saveButton.click();
        WebElement closeBtnEP = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Close')]")));
        closeBtnEP.click();
        List<WebElement> nextBtns = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//button[./span[normalize-space(text())='Next']]")));
        nextBtns.get(0).click();
    }

    private void enrolmentProfileUpdate(WebDriverWait wait,JobRecord record,WebDriver driver) throws Throwable {
        WebElement admissionField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@formcontrolname='admnNumber']")));

        if(admissionField.getAttribute("value").isBlank()){
            if(record.getAdmissionNumber()!=null){
                admissionField.sendKeys(df.format(record.getAdmissionNumber()));
            }else throw new RuntimeException("Admission not not present");
        }

        WebElement mediumOfInstructionDropDown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@formcontrolname='mediumOfInstruction']")));
        Select mediumOfInstructionSelect = new Select(mediumOfInstructionDropDown);
        mediumOfInstructionSelect.selectByVisibleText("19-English");

        WebElement languageGroupDropDown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@formcontrolname='languageGroup']")));
        Select languageGroupSelect = new Select(languageGroupDropDown);
        languageGroupSelect.selectByVisibleText("English_Hindi");
        List<WebElement> saveButtons = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//button[./span[normalize-space(text())='Save']]")));
        WebElement saveButton = saveButtons.get(1);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", saveButton);
        Thread.sleep(2000);

        WebElement enrStatusPYDropDown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@formcontrolname='enrStatusPY']")));
        Select enrStatusPYSelect = new Select(enrStatusPYDropDown);
        String enrStatusPYSelectedValue = enrStatusPYSelect.getFirstSelectedOption().getAttribute("value"); // or getText() for visible text

        if(enrStatusPYSelectedValue.isBlank()){
            if(record.getStatusOfStudentPrevAcademic()!=null) enrStatusPYSelect.selectByVisibleText(record.getStatusOfStudentPrevAcademic());
            else throw new RuntimeException("Status of student in prev academic year is not present");
        }

        if(record.getStatusOfStudentPrevAcademic()!=null && record.getStatusOfStudentPrevAcademic().equals("2-Studied at Other School")){
            WebElement classPyDropDown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("select[@formcontrolname='classPY']")));
            Select classPySelect = new Select(classPyDropDown);
            String classPySelectedValue = classPySelect.getFirstSelectedOption().getAttribute("value"); // or getText() for visible text
            if(classPySelectedValue.isBlank()){
                if(record.getClassStudiedInPreviousAcademicYear()!=null) classPySelect.selectByVisibleText(record.getClassStudiedInPreviousAcademicYear());
                else throw new RuntimeException("class studied in prev academic year is not present");
            }

            WebElement examResultPyDropDown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("select[@formcontrolname='examResultPy']")));
            Select examResultPySelect = new Select(examResultPyDropDown);
            String examResultPySelectedValue = examResultPySelect.getFirstSelectedOption().getAttribute("value"); // or getText() for visible text
            if(examResultPySelectedValue.isBlank()){
                if(record.getResultOfExamination()!=null) examResultPySelect.selectByVisibleText(record.getResultOfExamination());
                else throw new RuntimeException("Exam result in prev year is not present");
            }

            WebElement marksObtained = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("input[@formcontrolname='examMarksPy']")));
            if(marksObtained.getAttribute("value").isBlank()){
                if(record.getPercentage()!=null) marksObtained.sendKeys(df.format(record.getPercentage())); //attendancePy
                else throw new RuntimeException("Student Percentage not present");
            }

            WebElement attendence = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("input[@formcontrolname='attendancePy']")));
            if(attendence.getAttribute("value").isBlank()){
                if(record.getAttendance()!=null) attendence.sendKeys(df.format(record.getAttendance())); //attendancePy
                else throw new RuntimeException("Student Attendance not present");
            }
        }

        WebElement enrUnderDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@formcontrolname='enrUnder']")));
        Select enrUnderSelect = new Select(enrUnderDropdown);
        System.out.println(enrUnderDropdown.getText());
        List<WebElement> options = enrUnderSelect.getAllSelectedOptions();
        if(options.isEmpty()){
            if(record.getEnrolledUnder()!=null) enrUnderSelect.selectByVisibleText(record.getEnrolledUnder());
            else throw new RuntimeException("Enrolled Under not present");
        }


        wait.until(ExpectedConditions.elementToBeClickable(saveButton));
        saveButton.click();
        WebElement closeBtnEP = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Close')]")));
        closeBtnEP.click();
        List<WebElement> nextBtns = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//button[./span[normalize-space(text())='Next']]")));
        nextBtns.get(1).click();
    }

    private void facilityProfileUpdate(WebDriverWait wait,JobRecord record,WebDriver driver) throws Throwable {
        if(record.isSLD()){
            WebElement screenedForSldRadio = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='radio' and @value='1' and @formcontrolname='screenedForSld']")));
            screenedForSldRadio.click();
        }else{
            WebElement screenedForSldRadio = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='radio' and @value='2' and @formcontrolname='screenedForSld']")));
            screenedForSldRadio.click();
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

        if(record.isASD()){
            WebElement autismSpectrumDisorderRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='radio' and @value='1' and @formcontrolname='autismSpectrumDisorder']")));
            autismSpectrumDisorderRadioBtn.click();
        }else{
            WebElement autismSpectrumDisorderRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='radio' and @value='2' and @formcontrolname='autismSpectrumDisorder']")));
            autismSpectrumDisorderRadioBtn.click();
        }

        if(record.isDigitalyLiterate()){
            WebElement digitalCapableRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//input[@type='radio' and @value='1' and @formcontrolname='digitalCapableYn']")));
            digitalCapableRadioBtn.click();
        }else{
            WebElement digitalCapableRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//input[@type='radio' and @value='2' and @formcontrolname='digitalCapableYn']")));
            digitalCapableRadioBtn.click();
        }
        //put javascript executer here
        List<WebElement> saveButtons = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//button[./span[normalize-space(text())='Save']]")));
        log.info("list of save btns:{}",saveButtons.size());
        WebElement saveButton = saveButtons.get(2);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", saveButton);
        Thread.sleep(1000);

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
        saveButton.click();
        log.info("After save button click FP");

        log.info("after btn click");
        WebElement closeBtnFP = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Close')]")));
        closeBtnFP.click();
        List<WebElement> nextBtns = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//button[./span[normalize-space(text())='Next']]"))
        );
        nextBtns.get(2).click();
    }

    private void confirmProfileUpdate(WebDriverWait wait,JobRecord record,WebDriver driver) throws Throwable {
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
        WebElement dashboardBtn= wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()=' School Dashboard ']"))); //DashBoard
        dashboardBtn.click();
        List<WebElement> rows = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("tbody[role='rowgroup'] tr")));
        WebElement row1=rows.get(0);
        List<WebElement> cols = row1.findElements(By.cssSelector("td"));
        WebElement actionBtn = cols.get(7);
        WebElement viewAndManageBtn = actionBtn.findElement(By.xpath("//a[contains(text(), 'View/Manage')]"));
        viewAndManageBtn.click();
    }

}




//    private void generalProfileUpdate(WebDriverWait wait,JobRecord record,WebDriver driver) throws InterruptedException {
//        try{
//            try{
//                WebElement aadharCheckBox = wait.until(
//                        ExpectedConditions.elementToBeClickable(
//                                By.id("flexCheckDefault")
//                        )
//                );
//                String aadhar=record.getAadharNumber();
//
//                if (aadharCheckBox.isDisplayed() && !aadharCheckBox.isSelected() && aadhar!=null) {
//                    aadharCheckBox.click();
//                    WebElement aadharUUID = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@formcontrolname='uuid']")));
//                    aadharUUID.sendKeys(aadhar);
//                    WebElement nameAsUuid = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@formcontrolname='nameAsUuid']")));
//                    nameAsUuid.sendKeys(record.getNameAsAadhar());
//                }
//            }catch (Exception e){
//                log.error("aadhar check failed");
//            }
//
//            WebElement addressTextArea = wait.until(ExpectedConditions.elementToBeClickable(
//                    By.xpath("//textarea[@formcontrolname='address']")));
//            if(addressTextArea.getAttribute("value").isBlank() && record.getAddress()!=null){
//                addressTextArea.sendKeys(record.getAddress());
//            }
//
//            WebElement pincodeField = wait.until(ExpectedConditions.elementToBeClickable
//                    (By.xpath("//input[@name='pincode']")));
//            if(pincodeField.getAttribute("value").isBlank() && record.getPinCode()!=null){
//                pincodeField.sendKeys(df.format(record.getPinCode()));
//            }
//
//            WebElement studentMobile = wait.until(ExpectedConditions.elementToBeClickable
//                    (By.xpath("//input[@formcontrolname='primaryMobile']")));
//            if(studentMobile.getAttribute("value").isBlank() && record.getFatherMoNumber()!=null){
//                studentMobile.sendKeys(df.format(record.getFatherMoNumber()));
//            }
//
//            WebElement dropdownContainer = driver.findElement(By.cssSelector(".ng-select-container"));
//            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dropdownContainer);
//            Thread.sleep(2000);
//            WebElement selectedValue = dropdownContainer.findElement(By.cssSelector(".ng-value-label"));
//            if(selectedValue.getText().equals("Select") && record.getMotherTongue()!=null){
//                dropdownContainer.click();
//                // Step 3: Wait for the options to become visible
//                wait.until(ExpectedConditions.visibilityOfElementLocated(
//                        By.cssSelector(".ng-option"))); // Ensure ng-option is loaded
//                // Step 4: Search for the desired option (optional)
//                WebElement inputField = dropdownContainer.findElement(By.cssSelector("input[aria-autocomplete='list']"));
//                inputField.sendKeys(record.getMotherTongue()); // Replace with the desired text
//                Actions actions = new Actions(driver);
//
//
//                WebElement dropdownPanel = driver.findElement(By.cssSelector(".ng-dropdown-panel"));
//                WebElement desiredOption = dropdownPanel.findElement(By.cssSelector(".ng-star-inserted"));
//                Thread.sleep(1000);
//                desiredOption.click();
//            }
//
//            WebElement categoryDropDown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@formcontrolname='socCatId']")));
//            Select categorySelect = new Select(categoryDropDown);
//            String categorySelectValue = categorySelect.getFirstSelectedOption().getAttribute("value"); // or getText() for visible text
//            if(categoryDropDown.isEnabled() && categorySelectValue.isBlank() && record.getCategory()!=null){
//                if(record.getCategory()== Category.General)  categorySelect.selectByVisibleText("1 - GENERAL");
//                else if(record.getCategory()== Category.OBC) categorySelect.selectByVisibleText("4 - OBC");
//                else if(record.getCategory()== Category.SC) categorySelect.selectByVisibleText("2 - SC");
//                else  categorySelect.selectByVisibleText("3 - ST");
//            }
//            WebElement minorityDropDown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@formcontrolname='minorityId']")));
//            Select minoritySelect = new Select(minorityDropDown);
//            String minoritySelectedValue = minoritySelect.getFirstSelectedOption().getAttribute("value"); // or getText() for visible text
//            if(minoritySelectedValue.isBlank() && record.getMinorityGroup()!=null){
//                minoritySelect.selectByVisibleText(record.getMinorityGroup());
//                log.info("after select minority group");
//            }
//
//            if(record.isBpl()){
//                WebElement isBplRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='isBplYN' and @type='radio' and following-sibling::label[text()='Yes']]")));
//                isBplRadioBtn.click();
//
//            }else{
//                WebElement isBplRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='isBplYN' and @type='radio' and following-sibling::label[text()='No']]")));
//                isBplRadioBtn.click();
//            }
//            if(record.isEws()){
//                WebElement ewsRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='ewsYN' and @type='radio' and following-sibling::label[text()='Yes']]")));
//                ewsRadioBtn.click();
//            }else{
//                WebElement ewsRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='ewsYN' and @type='radio' and following-sibling::label[text()='No']]")));
//                ewsRadioBtn.click();
//            }
//            if(record.isCwsn()){
//                WebElement cwsnRadioButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='cwsnYN' and @type='radio' and following-sibling::label[text()='Yes']]")));
//                cwsnRadioButton.click();
//            }else{
//                WebElement cwsnRadioButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='cwsnYN' and @type='radio' and following-sibling::label[text()='No']]")));
//                cwsnRadioButton.click();
//            }
//
//            WebElement isIndianNationalsYesRadioBtn= wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='natIndYN' and @type='radio' and following-sibling::label[text()='Yes']]")));
//            isIndianNationalsYesRadioBtn.click();
//
//            if(record.isOosc()){
//                WebElement outOfSchoolChildRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='ooscYN' and @type='radio' and following-sibling::label[text()='Yes']]")));
//                outOfSchoolChildRadioBtn.click();
//            }else{
//                WebElement outOfSchoolChildRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='ooscYN' and @type='radio' and following-sibling::label[text()='No']]")));
//                outOfSchoolChildRadioBtn.click();
//            }
//
//            WebElement bloodGroupDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@formcontrolname='bloodGroup']")));
//            System.out.println(bloodGroupDropdown.getAttribute("value"));
//            Select bloodGroupSelect = new Select(bloodGroupDropdown);
//            String bloodValue = bloodGroupSelect.getFirstSelectedOption().getAttribute("value");
//
//            if(bloodValue.isBlank()){
//                bloodGroupSelect.selectByValue("9");
//            }
//
//            List<WebElement> saveButtons = wait.until(
//                    ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//button[./span[normalize-space(text())='Save']]"))
//            );
//
//            WebElement saveButton = saveButtons.get(0);
//            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", saveButton);
//            Thread.sleep(5000);
//            saveButton.click();
//            WebElement closeBtnGP = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Close')]")));
//            closeBtnGP.click();
//
//            WebElement nextBtnGP = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class, 'btnnext')]")));
//            nextBtnGP.click();
//            log.info("next Btn clicked");
//        }catch (Exception e){
//
//            try{
//                List<WebElement> buttons = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
//                        By.xpath("//button[contains(@style, 'display: inline-block')]")
//                ));
//                System.out.println(buttons.get(0).getText());
//                buttons.get(0).click();
//            }catch (Exception exception){
//                log.error(exception);
//            }
//            errorLogManager.logError(record,e, "Context info about this error", "ERROR",
//                    this.getClass().getName(), "fillGeneralProfile");
//            log.info("Error In General Profile: {}",e.getCause());
//            WebElement ele3= wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()=' School Dashboard ']"))); //DashBoard
//            ele3.click();
//            log.info("after Dashboard ");
//            List<WebElement> rows = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("tbody[role='rowgroup'] tr")));
//            log.info("length of rows are : {}",rows.size());
//
//            WebElement row1=rows.get(0);
//            List<WebElement> cols = row1.findElements(By.cssSelector("td"));
//            WebElement actionBtn = cols.get(7);
//            WebElement viewAndManageBtn = actionBtn.findElement(By.xpath("//a[contains(text(), 'View/Manage')]"));
//            viewAndManageBtn.click();
//            e.printStackTrace();
//            throw e;
//        }
//    }
//
//    private void enrolmentProfileUpdate(WebDriverWait wait,JobRecord record,WebDriver driver) throws InterruptedException {
//        try{
//            System.out.println("admission number is :"+record.getAdmissionNumber());
//
//            WebElement admissionField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@formcontrolname='admnNumber']")));
//            if(record.getAdmissionNumber()!=null){
//                admissionField.clear();
//                admissionField.sendKeys(df.format(record.getAdmissionNumber()));
//            }
//            WebElement mediumOfInstructionDropDown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@formcontrolname='mediumOfInstruction']")));
//            Select mediumOfInstructionSelect = new Select(mediumOfInstructionDropDown);
//            // Select the first option by index (1-based index for the first option "19-English")
//            mediumOfInstructionSelect.selectByIndex(1); // 0 is the "Select" option, so 1 is "19-English"
//            // Locate the dropdown element
//            WebElement languageGroupDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@formcontrolname='languageGroup']")));
//
//            // Create a Select instance
//            Select languageGroupSelect = new Select(languageGroupDropdown);
//
//            // Select the first option by index (1-based index for "English_Hindi")
//            languageGroupSelect.selectByIndex(1); // 0 is the "Select" option, so 1 is "English_Hindi"
//            List<WebElement> saveButtons = wait.until(
//                    ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//button[./span[normalize-space(text())='Save']]"))
//            );
//
//
//            WebElement saveButton = saveButtons.get(1);
//
//            // Scroll the element into view
//            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", saveButton);
//            wait.until(ExpectedConditions.elementToBeClickable(saveButton));
//
//            // Try clicking the button
//            Thread.sleep(5000);
//            saveButton.click();
//            WebElement closeBtnGP = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Close')]")));
//            closeBtnGP.click();
//            List<WebElement> nextBtns = wait.until(
//                    ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//button[./span[normalize-space(text())='Next']]"))
//            );
//
//            nextBtns.get(1).click();
//        }catch (Exception e){
//            try{
//                List<WebElement> buttons = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
//                        By.xpath("//button[contains(@style, 'display: inline-block')]")
//                ));
//                System.out.println(buttons.get(0).getText());
//                buttons.get(0).click();
//            }catch (Exception exception){
//                log.error(exception);
//            }
//            errorLogManager.logError(record,e, "Context info about this error", "ERROR",
//                    this.getClass().getName(), "enrollmentProfileerror");
//            WebElement ele3= wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()=' School Dashboard ']"))); //DashBoard
//            ele3.click();
//            List<WebElement> rows = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("tbody[role='rowgroup'] tr")));
//
//            WebElement row1=rows.get(0);
//            List<WebElement> cols = row1.findElements(By.cssSelector("td"));
//            WebElement actionBtn = cols.get(7);
//            WebElement viewAndManageBtn = actionBtn.findElement(By.xpath("//a[contains(text(), 'View/Manage')]"));
//            viewAndManageBtn.click();
//            e.printStackTrace();
//            throw e;
//        }
//    }
//
//    private void facilityProfileUpdate(WebDriverWait wait,JobRecord record,WebDriver driver) throws InterruptedException {
//        try{
//            if(record.isSLD()){
//                WebElement screenedForSldRadio = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='radio' and @value='1' and @formcontrolname='screenedForSld']")));
//                screenedForSldRadio.click();
//            }else{
//                WebElement screenedForSldRadio = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='radio' and @value='2' and @formcontrolname='screenedForSld']")));
//                screenedForSldRadio.click();
//            }
//
//            if(record.isASD()){
//                WebElement autismSpectrumDisorderRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='radio' and @value='1' and @formcontrolname='autismSpectrumDisorder']")));
//                autismSpectrumDisorderRadioBtn.click();
//            }else{
//                WebElement autismSpectrumDisorderRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='radio' and @value='2' and @formcontrolname='autismSpectrumDisorder']")));
//                autismSpectrumDisorderRadioBtn.click();
//            }
//
//            if(record.isADHD()){
//                WebElement attentionDeficitHyperactiveDisorderRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='radio' and @value='1' and @formcontrolname='attentionDeficitHyperactiveDisorder']")));
//                attentionDeficitHyperactiveDisorderRadioBtn.click();
//            }else{
//                WebElement attentionDeficitHyperactiveDisorderRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='radio' and @value='2' and @formcontrolname='attentionDeficitHyperactiveDisorder']")));
//                attentionDeficitHyperactiveDisorderRadioBtn.click();
//            }
//
//            if(record.isGifted()){
//                WebElement giftedChildrenRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//input[@type='radio' and @value='1' and @formcontrolname='giftedChildrenYn']")));
//                giftedChildrenRadioBtn.click();
//            }else{
//                WebElement giftedChildrenRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//input[@type='radio' and @value='2' and @formcontrolname='giftedChildrenYn']")));
//                giftedChildrenRadioBtn.click();
//            }
//
//            if(record.isSportsChamp()){
//                WebElement olympdsNlcRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//input[@type='radio' and @value='1' and @formcontrolname='olympdsNlc']")));
//                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", olympdsNlcRadioBtn);
//                olympdsNlcRadioBtn.click();
//            }else{
//                WebElement olympdsNlcRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//input[@type='radio' and @value='2' and @formcontrolname='olympdsNlc']")));
//                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", olympdsNlcRadioBtn);
//                olympdsNlcRadioBtn.click();
//            }
//
//            if(record.isParticipatedNCC()){
//                WebElement nccNssRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//input[@type='radio' and @value='1' and @formcontrolname='nccNssYn']")));
//                nccNssRadioBtn.click();
//            }else{
//                WebElement nccNssRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//input[@type='radio' and @value='2' and @formcontrolname='nccNssYn']")));
//                nccNssRadioBtn.click();
//            }
//
//            if(record.isDigitalyLiterate()){
//                WebElement digitalCapableRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//input[@type='radio' and @value='1' and @formcontrolname='digitalCapableYn']")));
//                digitalCapableRadioBtn.click();
//            }else{
//                WebElement digitalCapableRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//input[@type='radio' and @value='2' and @formcontrolname='digitalCapableYn']")));
//                digitalCapableRadioBtn.click();
//            }
//
//            WebElement heightInCm = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//input[@formcontrolname='heightInCm']")));
//            if(heightInCm.getAttribute("value").isBlank()){
//                if(record.getHeight()!=null){
//                    heightInCm.sendKeys(df.format(record.getHeight()));
//                }else{
//                    throw new RuntimeException("Student Height not present in your record for the student "+record.getStudentName());
//                }
//            }
//            WebElement weightInKg = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//input[@formcontrolname='weightInKg']")));
//            if(weightInKg.getAttribute("value").isBlank()){
//                if(record.getWeight()!=null){
//                    weightInKg.sendKeys(df.format(record.getWeight()));
//                }else{
//                    throw new RuntimeException("Student Weight not present in your record for the student "+record.getStudentName());
//                }
//            }
//
//
//            WebElement distanceDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@formcontrolname='distanceFrmSchool']")));
//            Select distanceSelect = new Select(distanceDropdown);
//            distanceSelect.selectByValue("2");
//
//            // Locate the dropdown element
//            WebElement parentEducationDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@formcontrolname='parentEducation']")));
//            Select parentEducationSelect = new Select(parentEducationDropdown);
//            parentEducationSelect.selectByValue("5");
//
//            List<WebElement> saveButtons = wait.until(
//                    ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//button[./span[normalize-space(text())='Save']]"))
//            );
//            log.info("list of save btns:{}",saveButtons.size());
//            WebElement saveButton = saveButtons.get(2);
//            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", saveButton);
//            Thread.sleep(2000);
//            saveButton.click();
//            log.info("After save button click FP");
//
//            log.info("after btn click");
//            try{
//                WebElement closeBtnFP = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Close')]")));
//                Thread.sleep(1000);
//                closeBtnFP.click();
//            }catch (Exception e){
//                throw e;
//            }
//            try{
//                List<WebElement> nextBtns = wait.until(
//                        ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//button[./span[normalize-space(text())='Next']]"))
//                );
//                Thread.sleep(1000);
//                nextBtns.get(2).click();
//            }catch (Exception e){
//                throw e;
//            }
//            try{
//                WebElement fpCompleteDataBtn = wait.until(
//                        ExpectedConditions.elementToBeClickable(By.xpath("//button[span[normalize-space(text())='Complete Data']]"))
//                );
//                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", fpCompleteDataBtn);
//
//                wait.until(ExpectedConditions.elementToBeClickable(fpCompleteDataBtn));
//                Thread.sleep(1000);
//                fpCompleteDataBtn.click();
//
//                WebElement fpOkayBtn = wait.until(
//                        ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Okay')]"))
//                );
//                Thread.sleep(1000);
//                fpOkayBtn.click();
//                WebElement confirmOkeyBtn = wait.until(
//                        ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Okay')]"))
//                );
//                Thread.sleep(1000);
//                confirmOkeyBtn.click();
//                record.setJobStatus(JobStatus.COMPLETED);
//                jobRecordDao.update(record);
//            }catch (Exception e){
//                errorLogManager.logError(record,e, "Already profile completed", "ERROR",
//                        this.getClass().getName(), "facilityProfileUpdate Error");
//                e.printStackTrace();
//            }
//
//            WebElement ele3= wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()=' School Dashboard ']"))); //DashBoard
//            ele3.click();
//            log.info("after Dashboard ");
//            List<WebElement> rows = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("tbody[role='rowgroup'] tr")));
//            log.info("length of rows are : {}",rows.size());
//
//            WebElement row1=rows.get(0);
//            List<WebElement> cols = row1.findElements(By.cssSelector("td"));
//            WebElement actionBtn = cols.get(7);
//            WebElement viewAndManageBtn = actionBtn.findElement(By.xpath("//a[contains(text(), 'View/Manage')]"));
//            viewAndManageBtn.click();
//        }catch (Exception e){
//            errorLogManager.logError(record,e, "Context info about this error", "ERROR",
//                    this.getClass().getName(), "facilityProfileUpdate");
//            log.info("Error In Facility Profile");
//            List<WebElement>buttons=wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[.//button[contains(@style, 'display: inline-block')]]")));
//            buttons.get(0).click();
//            e.printStackTrace();
//            WebElement ele3= wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()=' School Dashboard ']"))); //DashBoard
//            ele3.click();
//            log.info("after Dashboard ");
//            List<WebElement> rows = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("tbody[role='rowgroup'] tr")));
//            log.info("length of rows are : {}",rows.size());
//
//            WebElement row1=rows.get(0);
//            List<WebElement> cols = row1.findElements(By.cssSelector("td"));
//            WebElement actionBtn = cols.get(7);
//            WebElement viewAndManageBtn = actionBtn.findElement(By.xpath("//a[contains(text(), 'View/Manage')]"));
//            viewAndManageBtn.click();
//            throw e;
//        }
//    }
