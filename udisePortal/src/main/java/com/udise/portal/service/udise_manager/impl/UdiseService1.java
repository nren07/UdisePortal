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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UdiseService1 {

    private final SimpMessagingTemplate messagingTemplate;

    private static final Logger log = LogManager.getLogger(UdiseService1.class);
    @Autowired
    private DockerManager dockerManager;

    @Autowired
    private JobRecordManager jobRecordManager;

    @Autowired
    private JobRecordDao jobRecordDao;
    @Value("${vnc-login-timeout:#{70}}")
    private int vncLoginTimeOut;

    public UdiseService1(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Async
    public void startChromeService(DockerVo dockerVo, Long jobId, String containerId, List<JobRecord> jobRecordList, Job job) throws InterruptedException, IOException {
        int loginTimeOut=vncLoginTimeOut;
        String url = String.format("http://%s:%d/wd/hub", dockerVo.getContainerName(), 4444); //for prod
//        String url = String.format("http://localhost:%d/wd/hub",  dockerVo.getHostPort()); //for dev
        try{
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
                // Maximize the browser window
                // Set implicit wait and maximize the window
                driver.manage().timeouts().implicitlyWait(2, TimeUnit.MINUTES);
                driver.manage().window().maximize();

                // Create WebDriverWait instance
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));

                // Navigate to the UDISE portal login page
                driver.get("https://sdms.udiseplus.gov.in/p2/v1/login?state-id=108");
                messagingTemplate.convertAndSend("/topic/"+userid, new SocketResponseVo("JOB_STARTED", "job started testing"));
                // Wait until the URL or page state changes after the manual click
                String currentUrl = driver.getCurrentUrl();

                while (driver.getCurrentUrl().equals(currentUrl) && loginTimeOut>=0) {
                    Thread.sleep(1000);
                    loginTimeOut--;  // Poll every second
                    log.info(loginTimeOut);
                }
                if(loginTimeOut<0){
                    return;
                }

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
                dockerManager.stopAndRemoveContainer(containerId,dockerVo);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
