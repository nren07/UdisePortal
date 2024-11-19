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
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UdiseService3 {
    private final SimpMessagingTemplate messagingTemplate;

    private static final Logger log = LogManager.getLogger(UdiseService1.class);
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); // Adjust the pattern as needed
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


    public UdiseService3(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }


    @Async
    public void startChromeService(DockerVo dockerVo, Long jobId, String containerId, List<JobRecord> jobRecordList, Job job) throws InterruptedException, IOException {
//        String url = String.format("http://%s:%d/wd/hub", dockerVo.getContainerName(), 4444); //for prod
        int loginTimeOut=vncLoginTimeOut;
//        String url = String.format("http://localhost:%d/wd/hub",  dockerVo.getHostPort()); //for dev
        try{
            WebDriver driver = null; // Declare driver her
//            job.setJobStatus(JobStatus.IN_PROGRESS);
            log.info("Job Start");
            String userid=String.valueOf(job.getAppUser().getId());
            try {
                // Set Chrome options and capabilities
//                ChromeOptions chromeOptions = new ChromeOptions();
//                DesiredCapabilities capabilities = new DesiredCapabilities();
//                capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
//                driver = new RemoteWebDriver(new URL(url), capabilities);

                driver=new ChromeDriver();
                log.info("Chrome Start");
//                messagingTemplate.convertAndSend("/topic/"+userid, new SocketResponseVo("JOB_STARTED", "job started testing"));

                driver.manage().timeouts().implicitlyWait(2, TimeUnit.MINUTES);
                driver.manage().window().maximize();

                // Create WebDriverWait instance
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));

                // Navigate to the UDISE portal login page
                driver.get("https://sdms.udiseplus.gov.in/p2/v1/login?state-id=108");
                log.info("Site Rendered");
//                messagingTemplate.convertAndSend("/topic/"+userid, new SocketResponseVo("JOB_STARTED", "job started testing"));
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
//                WebElement addNewStudent = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class, 'mat-raised-button') and contains(@class, 'mat-primary') and span[text()='Add Student']]")));
//                addNewStudent.click();
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addStudent(WebDriverWait wait,WebDriver driver,Long jobId) throws Exception{
        try{
            // Locate the 'ul' container by class
            WebElement sectionSearchContainer = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("ul.sectionSearch.mt-2")));

            // Locate the 'Class' dropdown inside the 'ul' container
            WebElement classDropdown = sectionSearchContainer.findElement(By.cssSelector("select.form-select.w210"));
            Select classSelect = new Select(classDropdown);

            // Iterate over all 'Class' options
            List<WebElement> classOptions = classSelect.getOptions();
            for (WebElement classOption : classOptions) {
                if(classOption.isEnabled()){
                    if(classOption.getText().equals("I")){
                        classSelect.selectByVisibleText(classOption.getText());
                        Thread.sleep(500);
                        log.info("inside if block");
                        WebElement sectionDropdown = sectionSearchContainer.findElement(By.cssSelector("select.form-select.w150"));
                        Select sectionSelect = new Select(sectionDropdown);
                        WebElement search_input = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//input[@placeholder='Search']")));

                        // Iterate over all 'Section' options
                        List<WebElement> sectionOptions = sectionSelect.getOptions();
                        for (WebElement sectionOption : sectionOptions) {


                            if(sectionOption.isEnabled()){
                                sectionSelect.selectByVisibleText(classOption.getText());
                                List<JobRecord>records=jobRecordManager.getListByQuery("",jobId,classOption.getText(),sectionOption.getText());
                                try{
                                    for(JobRecord record:records){
                                        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(
                                                By.xpath("//button[normalize-space(.//span)='Add Student']")
                                        ));
                                        addButton.click();

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
                                        studentCodeStateInput.sendKeys(record.getStateCode().toString());
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
                                        Thread.sleep(5000);
                                        saveButton.click();
                                        List<WebElement>checkDefault1 = driver.findElements(By.id("flexCheckDefault1")); //mode set checkbox for class
                                        checkDefault1.get(0).click();
                                        Thread.sleep(1000);
//                                WebElement checkbox2 = wait.until(ExpectedConditions.elementToBeClickable(By.id("flexCheckDefault1"))); //mode set checkbox for name of student
                                        checkDefault1.get(1).click();
                                        Thread.sleep(1000);
                                        WebElement checkbox3 = wait.until(ExpectedConditions.elementToBeClickable(By.id("flexCheckDefault2"))); //mode set checkbox for father name
                                        checkbox3.click();
                                        Thread.sleep(1000);
                                        WebElement checkbox4 = wait.until(ExpectedConditions.elementToBeClickable(By.id("flexCheckDefault3"))); //mode set checkbox for mother name
                                        checkbox4.click();
                                        Thread.sleep(1000);
                                        WebElement checkbox5 = wait.until(ExpectedConditions.elementToBeClickable(By.id("flexCheckDefault4"))); //mode set checkbox for dob
                                        checkbox5.click();
                                        Thread.sleep(1000);
                                        WebElement confirm=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space(text())='Confirm']"))); //mode set checkbox for dob
                                        confirm.click();
                                        Thread.sleep(10000);
                                        WebElement gp = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), 'GP')]")));
                                        gp.click();
                                        generalProfileUpdate(wait);
                                        enrolmentProfileUpdate(wait);
                                        facilityProfileUpdate(wait,driver);
                                        Thread.sleep(5000);

//                            //go to dashboard
//                            List<WebElement> rows = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("tbody[role='rowgroup'] tr")));
//                            List<WebElement> cols = rows.get(4).findElements(By.cssSelector("td"));
//                            WebElement actionBtn = cols.get(7);
//                            List<WebElement> buttons = actionBtn.findElements(By.tagName("a"));
//                            WebElement viewAndManageBtn=buttons.get(0);
//                            viewAndManageBtn.click();
                                        break;
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            }

                        }
                    }
                }
            }



            // Locate the button by its visible text
            WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space(text())='Add New Student']")));

            // Click the button
            addButton.click();
//            System.out.println("Add New Student button clicked.");
            WebElement fillProfileButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[normalize-space(text())='Fill General Profile']")));

            // Click the button
            fillProfileButton.click();

            //general profile add
            generalProfileUpdate(wait);

            //enrolment profile add
            enrolmentProfileUpdate(wait);

            //Facility Profile
            facilityProfileUpdate(wait,driver);

            WebElement screenedForSldNoRadio = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='radio' and @value='2' and @formcontrolname='screenedForSld']")));
            screenedForSldNoRadio.click();

            WebElement autismSpectrumDisorderNoRadio = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='radio' and @value='2' and @formcontrolname='autismSpectrumDisorder']")));
            autismSpectrumDisorderNoRadio.click();

            WebElement attentionDeficitHyperactiveDisorderNoRadio = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='radio' and @value='2' and @formcontrolname='attentionDeficitHyperactiveDisorder']")));
            attentionDeficitHyperactiveDisorderNoRadio.click();

            WebElement giftedChildrenNoRadio = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//input[@type='radio' and @value='2' and @formcontrolname='giftedChildrenYn']")));
            giftedChildrenNoRadio.click();

            WebElement olympdsNlcNoRadio = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//input[@type='radio' and @value='2' and @formcontrolname='olympdsNlc']")));
            olympdsNlcNoRadio.click();

            WebElement nccNssNoRadio = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//input[@type='radio' and @value='2' and @formcontrolname='nccNssYn']")));
            nccNssNoRadio.click();

            WebElement digitalCapableNoRadio = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//input[@type='radio' and @value='2' and @formcontrolname='digitalCapableYn']")));
            digitalCapableNoRadio.click();

            WebElement heightInCm = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//input[@formcontrolname='heightInCm']")));
            heightInCm.sendKeys("80");

            WebElement weightInKg = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//input[@formcontrolname='weightInKg']")));
            weightInKg.sendKeys("17");

            WebElement distanceDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@formcontrolname='distanceFrmSchool']")));
            Select distanceSelect = new Select(distanceDropdown);
            distanceSelect.selectByValue("2");

            // Locate the dropdown element
            WebElement parentEducationDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@formcontrolname='parentEducation']")));
            Select parentEducationSelect = new Select(parentEducationDropdown);
            parentEducationSelect.selectByValue("5");

            WebElement fpSaveBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//button[contains(text(),'Save')]")));
            fpSaveBtn.click();

            WebElement fpCloseBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Close')]")));
            fpCloseBtn.click();

            WebElement fpNextBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Next')]")));
            fpNextBtn.click();

            WebElement fpCompleteDataBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Complete Data')]")));
            fpCompleteDataBtn.click();

            WebElement fpOkayBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Okay')]")));
            fpOkayBtn.click();

            WebElement backToSchoolDashboardBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Back to School Dashboard')]")));
            backToSchoolDashboardBtn.click();

        }catch (Exception e){
            throw e;
        }
    }

    private void generalProfileUpdate(WebDriverWait wait) throws InterruptedException {
        try{
            Thread.sleep(10000);
            WebElement addressTextArea = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("textarea[formcontrolname='address']")));
            addressTextArea.sendKeys("Address");
            WebElement pincodeField = wait.until(ExpectedConditions.elementToBeClickable
                    (By.xpath("//input[@name='pincode']")));
            WebElement studentMobile = wait.until(ExpectedConditions.elementToBeClickable
                    (By.xpath("//input[@formcontrolname='primaryMobile']")));
            studentMobile.sendKeys("9929237401");

            WebElement inputElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='ng-select-container']//div[@role='combobox']")));
            inputElement.click();  // Open the dropdown
            WebElement motherTounge = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='ng-select-container']//span[contains(text(), '42-HINDI - Hindi')]")));
            motherTounge.click();
            WebElement categoryDropDown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@formcontrolname='socCatId']")));

            Select categorySelect = new Select(categoryDropDown);
            categorySelect.selectByVisibleText("4 - OBC");

            WebElement selectElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@formcontrolname='minorityId']")));
            Select select = new Select(selectElement);
            select.selectByVisibleText("7-NA");
            // Wait until the 'No' radio button for the 'bplRadioBtn' field is clickable
            WebElement isBplRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='isBplYN' and @type='radio' and following-sibling::label[text()='No']]")));
            isBplRadioBtn.click();
            // Wait until the 'No' radio button for the 'ewsYN' field is clickable
            WebElement ewsNoRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='ewsYN' and @type='radio' and following-sibling::label[text()='No']]")));
            ewsNoRadioBtn.click();

            // Wait until the 'No' radio button for the 'cwsnYN' field is clickable
            WebElement cwsnNoRadioButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='cwsnYN' and @type='radio' and following-sibling::label[text()='No']]")));
            cwsnNoRadioButton.click();
            // Wait until the 'Yes' radio button for the 'natIndYN' field is clickable
            WebElement isIndianNationalsYesRadioBtn= wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='natIndYN' and @type='radio' and following-sibling::label[text()='Yes']]")));
            isIndianNationalsYesRadioBtn.click();

            // Wait until the 'No' radio button for the 'ooscYN' field is clickable
            WebElement noRadioButtonOfOutOfSchoolChild = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='ooscYN' and @type='radio' and following-sibling::label[text()='No']]")));
            noRadioButtonOfOutOfSchoolChild.click();

            // Wait until the dropdown is clickable
            WebElement bloodGroupDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.name("bloodGroup")));
            Select bloodGroupSelect = new Select(bloodGroupDropdown);
            bloodGroupSelect.selectByVisibleText("Under Investigation - Result will be updated soon");
            // Wait until the "Save" button is clickable
            WebElement gpSaveButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit' and contains(text(), 'Save')]")));
            gpSaveButton.click();

            WebElement gpCloseButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Close']")));
            gpCloseButton.click();

            // Wait until the "Next" button is clickable
            WebElement gpNextButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[.//span[contains(text(), 'Next')]]")));
            gpNextButton.click();
        }catch (Exception e){
            log.info("Error In General Profile");
            e.printStackTrace();
            throw e;
        }
    }
    private void enrolmentProfileUpdate(WebDriverWait wait) throws InterruptedException {
        try{
            WebElement admissionField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@formcontrolname='admnNumber']")));
            admissionField.sendKeys("YourAdminNumber");
            WebElement dateInputField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@formcontrolname='admnStartDate']")));
            dateInputField.sendKeys("05/07/2024");

            // Locate the dropdown element by its formcontrolname attribute
            WebElement mediumOfInstructionDropDown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@formcontrolname='mediumOfInstruction']")));
            Select mediumOfInstructionSelect = new Select(mediumOfInstructionDropDown);

            // Select the "19-English" option by its visible text
            mediumOfInstructionSelect.selectByVisibleText("19-English");

            // Locate the dropdown element by its formcontrolname attribute
            WebElement languageGroupDropDown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@formcontrolname='languageGroup']")));

            Select languageGroupSelect = new Select(languageGroupDropDown);
            // Select the "English_Hindi" option by its visible text
            languageGroupSelect.selectByVisibleText("English_Hindi");

            // Locate the dropdown element by its formcontrolname attribute
            WebElement enrStatusPYDropDown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@formcontrolname='enrStatusPY']")));

            Select enrStatusPYSelect = new Select(enrStatusPYDropDown);
            // Select the "Anganwadi/ECCE Center" option by its visible text
            enrStatusPYSelect.selectByVisibleText("4-None/Not Studying");

            // Locate the dropdown element by its formcontrolname attribute
            WebElement enrUnderDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@formcontrolname='enrUnder']")));

            Select enrUnderSelect = new Select(enrUnderDropdown);

            // Select the "2-EWS" option by its visible text
            enrUnderSelect.selectByVisibleText("0-None");

            // Locate the Save button by its class name or text
            WebElement epSaveButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[span[contains(text(),'Save')]")));
            epSaveButton.click();

            WebElement epCloseButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Close']")));
            epCloseButton.click();

            WebElement epNextButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[span[text()='Next ']]")));
            epNextButton.click();
        }catch (Exception e){
            log.info("Error In Enrolment Profile");
            e.printStackTrace();
            throw e;
        }
    }

    private void facilityProfileUpdate(WebDriverWait wait,WebDriver driver) throws InterruptedException {
        try{
            WebElement screenedForSldNoRadio = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='radio' and @value='2' and @formcontrolname='screenedForSld']")));
            screenedForSldNoRadio.click();

            WebElement autismSpectrumDisorderNoRadio = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='radio' and @value='2' and @formcontrolname='autismSpectrumDisorder']")));
            autismSpectrumDisorderNoRadio.click();

            WebElement attentionDeficitHyperactiveDisorderNoRadio = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='radio' and @value='2' and @formcontrolname='attentionDeficitHyperactiveDisorder']")));
            attentionDeficitHyperactiveDisorderNoRadio.click();

            WebElement giftedChildrenNoRadio = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//input[@type='radio' and @value='2' and @formcontrolname='giftedChildrenYn']")));
            giftedChildrenNoRadio.click();

            WebElement olympdsNlcNoRadio = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//input[@type='radio' and @value='2' and @formcontrolname='olympdsNlc']")));
            olympdsNlcNoRadio.click();

            WebElement nccNssNoRadio = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//input[@type='radio' and @value='2' and @formcontrolname='nccNssYn']")));
            nccNssNoRadio.click();

            WebElement digitalCapableNoRadio = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//input[@type='radio' and @value='2' and @formcontrolname='digitalCapableYn']")));
            digitalCapableNoRadio.click();

            WebElement heightInCm = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//input[@formcontrolname='heightInCm']")));
            heightInCm.sendKeys("80");

            WebElement weightInKg = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//input[@formcontrolname='weightInKg']")));
            weightInKg.sendKeys("17");

            WebElement distanceDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@formcontrolname='distanceFrmSchool']")));
            Select distanceSelect = new Select(distanceDropdown);
            distanceSelect.selectByValue("2");

            // Locate the dropdown element
            WebElement parentEducationDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@formcontrolname='parentEducation']")));
            Select parentEducationSelect = new Select(parentEducationDropdown);
            parentEducationSelect.selectByValue("5");

            WebElement fpSaveBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( "//button[contains(text(),'Save')]")));
            fpSaveBtn.click();

            WebElement fpCloseBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Close')]")));
            fpCloseBtn.click();

            WebElement fpNextBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Next')]")));
            fpNextBtn.click();

            WebElement fpCompleteDataBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Complete Data')]")));
            fpCompleteDataBtn.click();

            WebElement fpOkayBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Okay')]")));
            fpOkayBtn.click();

            WebElement backToSchoolDashboardBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Back to School Dashboard')]")));
            backToSchoolDashboardBtn.click();
        }catch (Exception e){
            log.info("Error In Facility Profile");
            e.printStackTrace();
            throw e;
        }
    }


}
