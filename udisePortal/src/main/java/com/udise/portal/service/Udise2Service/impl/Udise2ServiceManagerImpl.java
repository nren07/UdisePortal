package com.udise.portal.service.Udise2Service.impl;

import com.udise.portal.dao.JobRecordDao;
import com.udise.portal.entity.Job;
import com.udise.portal.entity.JobRecord;
import com.udise.portal.enums.JobStatus;
import com.udise.portal.service.Udise2Service.Udise2ServiceManager;
import com.udise.portal.service.docker_manager.DockerManager;
import com.udise.portal.service.job.JobRecordManager;
import com.udise.portal.service.udise1service.Udise1ServiceManager;
import com.udise.portal.service.udise1service.impl.Udise1ServiceManagerImpl;
import com.udise.portal.vo.docker.DockerVo;
import com.udise.portal.vo.job.JobStartResponseVo;
import com.udise.portal.vo.job.SocketResponseVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class Udise2ServiceManagerImpl implements Udise2ServiceManager {
    private final SimpMessagingTemplate messagingTemplate;

    private static final Logger log = LogManager.getLogger(Udise1ServiceManagerImpl.class);
    @Autowired
    private DockerManager dockerManager;

    @Autowired
    private JobRecordManager jobRecordManager;

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private JobRecordDao jobRecordDao;

    public Udise2ServiceManagerImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public JobStartResponseVo startJob(Long jobId, Job job) throws IOException, InterruptedException {
        List<JobRecord> jobRecordList=jobRecordManager.getJobRecord(jobId);
        if(jobRecordList.size()>0){
            DockerVo dockerVo=dockerManager.createAndStartContainer(jobId);
            if(dockerVo==null){
                return new JobStartResponseVo(null,"internal server error");
            }
            taskExecutor.execute(() -> {
                try {
                    startChrome(dockerVo, jobId, dockerVo.getContainerId(), jobRecordList,job);
                } catch (InterruptedException | IOException e) {
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
    public void startChrome(DockerVo dockerVo, Long jobId, String containerId, List<JobRecord> jobRecordList,Job job) throws InterruptedException, IOException {
//        log.info("in start chrome function call");
//        String url = String.format("http://%s:%d/wd/hub", dockerVo.getContainerName(), 4444); //for prod
//        String checkUrlStatus = String.format("http://%s:%d/", dockerVo.getContainerName(), 4444); //for prod
        String url = String.format("http://localhost:%d/wd/hub",  dockerVo.getHostPort()); //for dev
        String checkUrlStatus = String.format("http://localhost:%d/", dockerVo.getHostPort()); //for dev
        try{
            dockerManager.waitForContainerReady(checkUrlStatus,containerId,dockerVo);
            WebDriver driver = null; // Declare driver her
            job.setJobStatus(JobStatus.IN_PROGRESS);
            log.info("Start event");
            String userid=String.valueOf(job.getAppUser().getId());
            try {
                // Set Chrome options and capabilities
                ChromeOptions chromeOptions = new ChromeOptions();
                DesiredCapabilities capabilities = new DesiredCapabilities();
                capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
                driver = new RemoteWebDriver(new URL(url), capabilities);

                driver.manage().timeouts().implicitlyWait(5, TimeUnit.MINUTES);
                driver.manage().timeouts().pageLoadTimeout(3, TimeUnit.MINUTES);  // Increased page load timeout
                driver.manage().window().maximize();

                // Create WebDriverWait instance
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofMinutes(10));

                // Navigate to the UDISE portal login page
                driver.get("https://sdms.udiseplus.gov.in/p2/v1/login?state-id=108");
                messagingTemplate.convertAndSend("/topic/"+userid, new SocketResponseVo("JOB_STARTED", "job started testing"));
                // Wait until the URL or page state changes after the manual click
                String currentUrl = driver.getCurrentUrl();
                WebElement usernameField = wait.until(ExpectedConditions.elementToBeClickable(By.id("username-field")));
                usernameField.sendKeys("08122604246");
                WebElement passwordField = driver.findElement(By.id("password-field"));
                passwordField.sendKeys("64sugRS#");
                log.info("before");
//                while (driver.getCurrentUrl().equals(currentUrl) ) {
//                    Thread.sleep(1000);  // Poll every second
//                    log.info("inside");
//                }
//                log.info("after");
                Thread.sleep(10000);
                log.info("after");
                log.info(driver.getCurrentUrl());
                WebElement ele1=wait.until(ExpectedConditions.elementToBeClickable(By.className("clearfix"))); //current academic year
                ele1.click();
                Thread.sleep(10000);
                log.info("after current academic year");
                WebElement ele2= wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space(text())='Close']"))); //PopUp close
                ele2.click();
                Thread.sleep(10000);
                log.info("after close ");
                WebElement ele3= wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[@mattooltip='School Dashboard']"))); //DashBoard
                ele3.click();
                Thread.sleep(10000);
                log.info("after Dashboard ");
                List<WebElement> rows= Collections.singletonList(wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("tbody[role='rowgroup'] tr"))));
                // Loop through each row
                for (WebElement row : rows) {
                    // Get the individual columns (td elements) in the row
                    List<WebElement> cols = row.findElements(By.cssSelector("td"));
                    WebElement className = cols.get(0);
                    WebElement sectionName = cols.get(1);
                    WebElement totalBoys = cols.get(2);
                    WebElement totalGirls = cols.get(3);
                    WebElement Transgenders = cols.get(4);
                    WebElement totalEnrolment = cols.get(5);
                    WebElement totalIncomplete = cols.get(6);
                    WebElement actionBtn = cols.get(7);
                    List<WebElement> buttons = actionBtn.findElements(By.tagName("a"));
                    WebElement addBtn=buttons.get(0);
                    WebElement viewAndManageBtn=buttons.get(0);
                    if(totalIncomplete.getText()=="0"){
                        addBtn.click();
                    }else{
                        viewAndManageBtn.click();
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
//                dockerManager.stopAndRemoveContainer(containerId,dockerVo);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
