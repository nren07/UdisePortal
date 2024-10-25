package com.udise.portal.service.job.impl;

import com.udise.portal.dao.JobDao;
import com.udise.portal.dao.JobRecordDao;
import com.udise.portal.entity.Job;
import com.udise.portal.entity.JobRecord;
import com.udise.portal.enums.JobStatus;
import com.udise.portal.service.docker_manager.DockerManager;
import com.udise.portal.service.job.JobManager;
import com.udise.portal.service.job.JobRecordManager;
import com.udise.portal.vo.docker.DockerVo;
import com.udise.portal.vo.job.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@Slf4j
public class JobManagerImpl implements JobManager {


    private final SimpMessagingTemplate messagingTemplate;

    private static final Logger log = LogManager.getLogger(JobManagerImpl.class);
    @Autowired
    private JobDao jobDao;

    @Autowired
    private JobRecordManager jobRecordManager;
    @Autowired
    private JobRecordDao jobRecordDao;

    @Autowired
    private DockerManager dockerManager;

    @Autowired
    @Qualifier("taskExecutor") // Specify the bean name to be used
    private TaskExecutor taskExecutor;

//    @Value("${login.no.response.timeout-key:#{null}}")
//    private long timeout;

    public JobManagerImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public List<JobResVo> getJobs(Long userId) {
        List<Job> jobList= jobDao.getJobs(userId);
        List<JobResVo> res=new ArrayList<>();
        for(Job job:jobList){
            JobResVo obj=new JobResVo();
            BeanUtils.copyProperties(job, obj);
            res.add(obj);
        }
        return res;
    }

    public JobStartResponseVo startJob(Long jobId) throws IOException, InterruptedException {
        List<JobRecord>jobRecordList=jobRecordManager.getJobRecord(jobId);
        if(jobRecordList.size()>0){
            DockerVo dockerVo=dockerManager.createAndStartContainer(jobId);
            taskExecutor.execute(() -> {
                try {
                    startChrome(dockerVo, jobId, dockerVo.getContainerId(), jobRecordList);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            log.info("after start Chrome function call");
            return new JobStartResponseVo(dockerVo.getVncPort(),"Job Started");
        }else{
            return new JobStartResponseVo(null,"Record Not Found");
        }

    }


    @Async
    public void startChrome(DockerVo dockerVo, Long jobId,String containerId,List<JobRecord>jobRecordList) throws InterruptedException {
//        log.info("in start chrome function call");
        String url = String.format("http://",dockerVo.getContainerName(),":%d/wd/hub", dockerVo.getHostPort());
        String checkUrlStatus=String.format("http://",dockerVo.getContainerName(),":%d/", dockerVo.getHostPort());
        dockerManager.waitForContainerReady(checkUrlStatus);
        WebDriver driver = null; // Declare driver here
        Job job=jobDao.getById(Job.class,jobId);
        job.setJobStatus(JobStatus.IN_PROGRESS);
        log.info("Start event");
        String userid=String.valueOf(job.getAppUser().getId());
        try {
            // Set Chrome options and capabilities
            ChromeOptions chromeOptions = new ChromeOptions();
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
            driver = new RemoteWebDriver(new URL(url), capabilities);
            // Maximize the browser window
            // Set implicit wait and maximize the window
            driver.manage().timeouts().implicitlyWait(120000, TimeUnit.SECONDS);
            driver.manage().window().maximize();

            // Create WebDriverWait instance
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Navigate to the UDISE portal login page
            driver.get("https://sdms.udiseplus.gov.in/p2/v1/login?state-id=108");
            messagingTemplate.convertAndSend("/topic/"+userid, new SocketResponseVo("JOB_STARTED", "job started testing"));
            // Wait until the URL or page state changes after the manual click
            String currentUrl = driver.getCurrentUrl();

            while (driver.getCurrentUrl().equals(currentUrl)) {
                Thread.sleep(1000);  // Poll every second
            }
            Thread.sleep(10000);

            WebElement ele1=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//p[text()='Current Academic Year']")));
            ele1.click();
            WebElement ele2= wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space(text())='Close']")));
            ele2.click();
            WebElement ele3=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[@routerlinkactive='active' and @mattooltip='Progression Activity']")));
            ele3.click();
            Thread.sleep(500);
            WebElement ele4=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@class, 'AnText') and contains(text(), 'Progression Summary')]")));
            ele4.click();
            WebElement ele5=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='View/Update']")));
            ele5.click();
            WebElement ul = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[contains(@class, 'selectPromotion')]")));
            List<WebElement> selectElements = ul.findElements(By.tagName("select"));

            if (selectElements.size() < 2) {
                throw new RuntimeException("Expected at least two select elements but found: " + selectElements.size());
            }
            WebElement classSelect = selectElements.get(0);
            Thread.sleep(500);
            Select classDropdown = new Select(classSelect);
            Thread.sleep(500);
            List<WebElement>classOptions=classDropdown.getOptions();
            Thread.sleep(500);
            WebElement goBtn = ul.findElement(By.tagName("button"));
            for(WebElement option :classOptions){
                if(option.isEnabled()){
                    classDropdown.selectByVisibleText(option.getText());
                    wait.until(ExpectedConditions.visibilityOf(selectElements.get(1)));
                    WebElement sectionSelect = selectElements.get(1);
                    Select sectionDropdown = new Select(sectionSelect);
                    List<WebElement>sectionOptions=sectionDropdown.getOptions();
                    for(WebElement section:sectionOptions){
                        if(section.isEnabled()){
                            sectionDropdown.selectByVisibleText(section.getText());
                            goBtn.click();
                            List<JobRecord>records=jobRecordManager.getListByQuery("",jobId,option.getText(),section.getText());
                            try{
                                for(JobRecord record:records){
                                    // Wait for the search input to become clickable
                                    WebElement searchInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@data-placeholder='Search']")));
                                    DecimalFormat df=new DecimalFormat("0.##########");
                                    searchInput.clear();
                                    searchInput.sendKeys(df.format(record.getStudentPen()));
                                    Thread.sleep(50);

//                                    WebElement statusCell = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//td[@class='mat-cell cdk-cell cdk-column-status mat-column-status ng-star-inserted']")));
//                                    String statusText = statusCell.getText();
                                    //status is not done then
//                    if (!statusText.equals("Done") || true) {
                                    System.out.println("inside not done or pending");
                                    WebElement select1=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//td//select)[1]")));
                                    Select objSelect1 = new Select(select1);
                                    objSelect1.selectByVisibleText("Promoted/Passed with Examination");
                                    WebElement input2=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//input)[2]")));
                                    input2.clear();
                                    input2.sendKeys(df.format(record.getPercentage()));
                                    WebElement input3=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//input)[3]")));
                                    input3.clear();
                                    input3.sendKeys(df.format(record.getAttendance()));
                                    WebElement select2=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//td//select)[2]")));
                                    Select objSelect2 = new Select(select2);
                                    if(option.getText().equals("XII")){
                                        objSelect2.selectByVisibleText("Left School with TC/without TC");
                                        WebElement update = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Update']")));
                                        update.click();
                                        WebElement confirm = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Confirm')]")));
                                        confirm.click();


                                    }else{
                                        objSelect2.selectByVisibleText("Studying in Same School");
                                        WebElement select3=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//td//select)[3]")));
                                        Select objSelect3 = new Select(select3);
                                        objSelect3.selectByVisibleText(record.getSection());
                                        WebElement update = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Update']")));
                                        update.click();
                                    }

                                    WebElement okey = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Okay']")));
                                    okey.click();
                                    record.setJobStatus(JobStatus.COMPLETED);
                                    jobRecordDao.update(record);
                                }
                            }catch (Exception e){
                                log.error(e);
                            }
                        }
                    }
                }
            }
        } catch (MalformedURLException e) {
            log.error("Invalid hub URL: " + url, e);
        } catch (WebDriverException e) {
            log.error("WebDriver encountered an error", e);
        } catch (Exception e) {
            log.error("An unexpected error occurred", e);
        }
        finally {
            // Close the driver safely
            if (driver != null) {
                driver.quit();
            }
            messagingTemplate.convertAndSend("/topic/"+userid, new SocketResponseVo("JOB_ENDED", "job Ended testing"));
            dockerManager.stopAndRemoveContainer(containerId);
        }
    }
}
