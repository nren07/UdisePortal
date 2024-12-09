package com.udise.portal.service.udise_manager.impl;
import com.udise.portal.dao.JobRecordDao;
import com.udise.portal.entity.Job;
import com.udise.portal.entity.JobRecord;
import com.udise.portal.enums.Category;
import com.udise.portal.enums.JobStatus;
import com.udise.portal.service.docker_manager.DockerManager;
import com.udise.portal.service.error_log.ErrorLogManager;
import com.udise.portal.service.job_record_manager.JobRecordManager;
import com.udise.portal.vo.docker.DockerVo;
import com.udise.portal.vo.job.SocketResponseVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class UdiseAddStudent {
    private final SimpMessagingTemplate messagingTemplate;

    private static final Logger log = LogManager.getLogger(ProgressionActivity.class);
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

    public UdiseAddStudent(SimpMessagingTemplate messagingTemplate) {
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


            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.manage().window().maximize();

            // Create WebDriverWait instance
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

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
//            messagingTemplate.convertAndSend("/topic/"+userid, new SocketResponseVo("JOB_ENDED", "job Ended testing"));
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
//            System.out.println("udise code is :"+udiseCodeElement.getText());
//            if(!udiseCodeElement.getText().equals("08122604122")){
//                System.out.println("different school is there");
//                WebElement imageElement = wait.until(
//                        ExpectedConditions.visibilityOfElementLocated(
//                                By.cssSelector("img[src='assets/img/power.png']")
//                        )
//                );
//                imageElement.click();
//                return;
//            }
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
            addStudent(wait,driver,jobId,userid);
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

    public void addStudent(WebDriverWait wait,WebDriver driver,Long jobId,String userid) throws Exception {
        try{
            List<JobRecord> records = jobRecordManager.getJobRecord(jobId);
            ((JavascriptExecutor) driver).executeScript("document.body.style.zoom='80%'");
            for (JobRecord record : records) {
                if(record.getJobStatus()==JobStatus.COMPLETED || record.getJobStatus()==JobStatus.ALREADY_COMPLETED) continue;
                try{
                    record.setJobStatus(JobStatus.IN_PROGRESS);
                    WebElement sectionSearchContainer = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("ul.sectionSearch.mt-2")));
                    WebElement classDropdown = sectionSearchContainer.findElement(By.cssSelector("select.form-select.w210"));
                    Select classSelect = new Select(classDropdown);
                    classSelect.selectByVisibleText(record.getClassName());
                    WebElement sectionDropdown = sectionSearchContainer.findElement(By.cssSelector("select.form-select.w150"));
                    System.out.println(sectionDropdown.getText());
                    Select sectionSelect = new Select(sectionDropdown);
                    Thread.sleep(1000);
                    sectionSelect.selectByVisibleText(record.getSection());
                    Thread.sleep(1000);

                    WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//button[normalize-space(.//span)='Add Student']")
                    ));

                    WebElement search_input = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//input[@placeholder='Search']")));
                    search_input.clear();
                    String nameOfStudent=record.getStudentName();
                    for(int i=0;i<nameOfStudent.length();i++) search_input.sendKeys(nameOfStudent.substring(i,i+1));
                    Thread.sleep(1000);
                    List<WebElement> rows = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("tbody[role='rowgroup'] tr")));
                    List<WebElement> cols = rows.get(0).findElements(By.cssSelector("td"));
                    if(cols.size()==1 && cols.get(0).getAttribute("colspan").equals("12")){
                        addButton.click();
                        fillPersonalDetails(wait, record, driver,userid);
                        generalProfileUpdate(wait, record, driver,userid);
                        enrolmentProfileUpdate(wait, record, driver,userid);
                        facilityProfileUpdate(wait, record, driver,userid);
                        confirmProfileUpdate(wait,record,driver,userid);
                    }else{
                        Iterator<WebElement> rowIterator = rows.iterator();
                        boolean found=false;
                        while (rowIterator.hasNext()) {
                            WebElement row = rowIterator.next();
                            cols = row.findElements(By.cssSelector("td"));
                            WebElement name = cols.get(2);
                            WebElement entryStatus = cols.get(5);
                            WebElement dob = cols.get(4);

                            if (!name.getText().equals(record.getStudentName()) || !dob.getText().equals(dateFormat.format(record.getDob()))) continue;
                            found=true;
                            WebElement profile = cols.get(6);
                            if (entryStatus.getText().contains("Completed")) {
                                break;
                            }else{
                                WebElement gp=profile.findElement(By.xpath("//a[contains(text(), 'GP')]"));
                                WebElement ep=profile.findElement(By.xpath("//a[contains(text(), 'EP')]"));
                                WebElement fp=profile.findElement(By.xpath("//a[contains(text(), 'FP')]"));

                                if(gp.getAttribute("class").contains("incomplete")){
                                    Thread.sleep(2000);
                                    gp.click();
                                    Thread.sleep(1000);
                                    generalProfileUpdate(wait, record, driver,userid);
                                    enrolmentProfileUpdate(wait, record, driver,userid);
                                    facilityProfileUpdate(wait, record, driver,userid);
                                    confirmProfileUpdate(wait,record,driver,userid);
                                }else if(ep.getAttribute("class").contains("incomplete")){
                                    Thread.sleep(2000);
                                    ep.click();
                                    Thread.sleep(1000);
                                    enrolmentProfileUpdate(wait, record, driver,userid);
                                    facilityProfileUpdate(wait, record, driver,userid);
                                    confirmProfileUpdate(wait,record,driver,userid);
                                }else if(fp.getAttribute("class").contains("incomplete")){
                                    Thread.sleep(2000);
                                    fp.click();
                                    Thread.sleep(1000);
                                    facilityProfileUpdate(wait, record, driver,userid);
                                    confirmProfileUpdate(wait,record,driver,userid);
                                }else break;
                            }
                        }
                        if(!found){
                            addButton.click();
                            fillPersonalDetails(wait, record, driver,userid);
                            generalProfileUpdate(wait, record, driver,userid);
                            enrolmentProfileUpdate(wait, record, driver,userid);
                            facilityProfileUpdate(wait, record, driver,userid);
                            confirmProfileUpdate(wait,record,driver,userid);
                        }
                    }
                }catch (Throwable e){
                    try{
                        List<WebElement> buttons = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//button[contains(@style, 'display: inline-block')]")));
                        System.out.println(buttons);
                        buttons.get(0).click();
                    }catch ( Exception ex){
                        log.error("btn not clicked or not found");
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
                    if(record.getJobStatus()==JobStatus.COMPLETED || record.getJobStatus()==JobStatus.ALREADY_COMPLETED ) continue;
                    record.setJobStatus(JobStatus.PENDING);
                    jobRecordDao.update(record);
                    e.printStackTrace();
                }
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    private void fillPersonalDetails(WebDriverWait wait,JobRecord record, WebDriver driver,String userid) throws Throwable {
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
            if(record.getStateCode()!=null){
                studentCodeStateInput.sendKeys(df.format(record.getStateCode()));
            }
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
            for(int i=1;i<=4;i++){
                List<WebElement>checkBtns = driver.findElements(By.id("flexCheckDefault"+i)); //mode set checkbox for class
                for(WebElement checkbox:checkBtns) {
                    if(checkBtns.size()>1){
                        Thread.sleep(500);
                        checkbox.click();
                    }else checkbox.click();
                }
            }
            WebElement confirm=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space(text())='Confirm']"))); //mode set checkbox for dob
            confirm.click();

            WebElement fillProfileButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space(text())='Fill General Profile']")));
            fillProfileButton.click();
        }catch (Exception e){
            try{
                WebElement okay=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space(text())='Okay']"))); //mode set checkbox for dob
                okay.click();
                throw e;
            }catch (Exception ex){
                WebElement agreeButton = driver.findElement(By.xpath("//button[normalize-space(text())='I agree']"));
                agreeButton.click();
                WebElement fillProfileButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space(text())='Fill General Profile']")));
                fillProfileButton.click();
            }
        }

    }

    private void generalProfileUpdate(WebDriverWait wait,JobRecord record,WebDriver driver,String userid) throws Throwable {
        WebElement addressTextArea = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//textarea[@formcontrolname='address']")));
        messagingTemplate.convertAndSend("/topic/"+userid, new SocketResponseVo("ALERT", "general profile update start"));
        if(addressTextArea.getAttribute("value").isBlank()){
            if(record.getAddress()!=null) addressTextArea.sendKeys(record.getAddress());
            else {
                messagingTemplate.convertAndSend("/topic/"+userid, new SocketResponseVo("ALERT", "student name is not present"));
                throw new RuntimeException("Address not present in your record for the student "+record.getStudentName());
            }
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

    private void enrolmentProfileUpdate(WebDriverWait wait,JobRecord record,WebDriver driver,String userid) throws Throwable {
        WebElement admissionField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@formcontrolname='admnNumber']")));

        if(admissionField.getAttribute("value").isBlank()){
            if(record.getAdmissionNumber()!=null){
                admissionField.sendKeys(df.format(record.getAdmissionNumber()));
            }else throw new RuntimeException("Admission not not present");
        }

        WebElement mediumOfInstructionDropDown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@formcontrolname='mediumOfInstruction']")));
        Select mediumOfInstructionSelect = new Select(mediumOfInstructionDropDown);
        mediumOfInstructionSelect.selectByVisibleText("19-English");

        WebElement academicStreamDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@formcontrolname='academicStream']")));
        Select academicStreamSelect = new Select(academicStreamDropdown);
        String academicStreamSelectedValue = academicStreamSelect.getFirstSelectedOption().getAttribute("value"); // or getText() for visible text

        if(academicStreamSelectedValue.isBlank()){
            if(record.getAcademicStream()!=null){
                if(record.getAcademicStream().toLowerCase().equals("arts")){
                    academicStreamSelect.selectByValue("1");
                }else if(record.getAcademicStream().toLowerCase().equals("science")){
                    academicStreamSelect.selectByValue("2");
                }else academicStreamSelect.selectByValue("3");
                WebElement subjectGroupDropDown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ng-multiselect-dropdown[@formcontrolname='subjectGroup']")));
                WebElement searchInput = subjectGroupDropDown.findElement(By.xpath(".//div[@class='multiselect-dropdown']//input[@aria-label='multiselect-search']"));

                if(record.getSubjectGroup()!=null){
                    String[] subjects =record.getSubjectGroup().split(",");
                    for(String subject:subjects){
                        searchInput.clear();
                        searchInput.sendKeys(subject);
                        WebElement inputToSelect = subjectGroupDropDown.findElement(By.xpath(".//div[@class='dropdown-list']//input"));
                        inputToSelect.click();
                    }

                }else throw new RuntimeException("SubjectGroup is not present in your job");
            }

        }

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

    private void facilityProfileUpdate(WebDriverWait wait,JobRecord record,WebDriver driver,String userid) throws Throwable {
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

    private void confirmProfileUpdate(WebDriverWait wait,JobRecord record,WebDriver driver,String userid) throws Throwable {
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
