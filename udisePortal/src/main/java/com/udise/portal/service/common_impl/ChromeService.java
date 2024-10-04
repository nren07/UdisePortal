package com.udise.portal.service.common_impl;


import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.Iterator;

@Service
public class ChromeService {
    public void startChrome(String hubUrl,String username) throws MalformedURLException, InterruptedException {

        //String hubUrl = "http://localhost:4444/wd/hub"; // Update if using a different URL
        // Set Chrome options
        ChromeOptions options = new ChromeOptions();
//            options.addArguments("--headless"); // Optional: Run Chrome in headless mode
//            options.addArguments("--no-sandbox"); // Optional: Disable sandboxing for Docker
//            options.addArguments("--disable-dev-shm-usage"); // Optional: Disable shared memory usage for Docker
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        // Create a RemoteWebDriver instance
        WebDriver driver = new RemoteWebDriver(new URL(hubUrl), capabilities);
        System.out.printf("line 37");
        try {


            // Maximize the browser window
            driver.manage().window().maximize();
            // Navigate to the UDISE portal login page
            driver.get("https://sdms.udiseplus.gov.in/p2/v1/login?state-id=108");
            // Maximize the browser window
            driver.manage().window().maximize();

            // Create WebDriverWait instance
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Wait for the username field to be visible and input Institute Name
//            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username-field")));
            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username-field")));
            usernameField.sendKeys(username);
//            "08120905929"

            // Wait for the password field to be visible and input Username
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password-field")));
            passwordField.sendKeys("sbj@42XV");
            WebElement captchImg= wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("captchaImage")));
            System.out.printf(captchImg.getAttribute("src"));
//            String captchTxt=captchaReader(captchImg.getAttribute("src"));
//            Thread.sleep(10000);
//            System.out.printf(captchTxt);
//
//
//            WebElement captchField=wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("captcha")));
//            captchField.sendKeys(captchTxt);
            Thread.sleep(10000);
//            wait.until(new ExpectedCondition<Boolean>() {
//                @Override
//                public Boolean apply(WebDriver driver) {
//                    // Check if the CAPTCHA field is not empty
//                    return !captchField.getAttribute("value").isEmpty();
//                }
//            });

            // Click the Sign In button
            WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("submit-btn")));
            signInButton.click();

            // Wait for the next page or element to be visible
            WebElement nextElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/app-root/app-main-routing/div[2]/div/div/div[1]/ul/li[1]")));
            nextElement.click();

            Thread.sleep(4000);
            driver.findElement(By.xpath("//*[@id=\"page-content-wrapper\"]/main/div/div/div/app-school-dashboard-cy/div/div[2]/div/div/div[3]/button")).click();
            Thread.sleep(2000);
            driver.findElement(By.xpath("//*[@id=\"sidebar-wrapper\"]/app-side-nav/div/ul/li[7]")).click();

            Thread.sleep(2000);
            driver.findElement(By.xpath("//*[@id=\"page-content-wrapper\"]/main/div/div/div/app-module-choice/div/div/div[2]/div/a[3]")).click();

            Thread.sleep(2000);
            driver.findElement(By.xpath("//*[@id=\"mat-input-1\"]")).sendKeys("2");

            Thread.sleep(2500);

            driver.findElement(By.xpath("//*[@id=\"page-content-wrapper\"]/main/div/div/div/app-promotion-summary-section-wise/div/div[3]/table/tbody/tr/td[6]/button")).click();
            Thread.sleep(2000);


            String excelFilePath = "D:\\UDISE DATA\\CLASS.xls";
            File file = new File(excelFilePath);

            if (!file.exists()) {
                System.out.println("The file does not exist: " + excelFilePath);
                return;
            }

            try (FileInputStream fis = new FileInputStream(file);
                 HSSFWorkbook workbook = new HSSFWorkbook(fis)) {

                HSSFSheet sheet = workbook.getSheetAt(0);
                Iterator<Row> iterator = sheet.iterator();

                // Skip header row
                if (iterator.hasNext()) {
                    iterator.next();
                }

                // Format for removing decimal places
                DecimalFormat decimalFormat = new DecimalFormat("#");

                while (iterator.hasNext()) {
                    Row currentRow = iterator.next();

                    Cell studentNameCell = currentRow.getCell(0);
                    Cell workingDayCell = currentRow.getCell(1);
                    Cell percentageCell = currentRow.getCell(2);

                    String studentName = studentNameCell.getStringCellValue();

                    String workingDay = "";
                    String percentage = "";

                    if (workingDayCell.getCellType() == CellType.NUMERIC) {
                        workingDay = decimalFormat.format(workingDayCell.getNumericCellValue());
                    } else if (workingDayCell.getCellType() == CellType.STRING) {
                        workingDay = workingDayCell.getStringCellValue();
                    }

                    if (percentageCell.getCellType() == CellType.NUMERIC) {
                        percentage = decimalFormat.format(percentageCell.getNumericCellValue());
                    } else if (percentageCell.getCellType() == CellType.STRING) {
                        percentage = percentageCell.getStringCellValue();
                    }

                    System.out.println(studentName + " " + workingDay + " " + percentage + " ");
                    Thread.sleep(2000);
                    driver.findElement(By.xpath("//*[@id=\"mat-input-2\"]")).clear();
                    driver.findElement(By.xpath("//*[@id=\"mat-input-2\"]")).sendKeys(studentName);

                    Thread.sleep(3000);
                    Select objSelect1 = new Select(driver.findElement(By.xpath("(//td//select)[1]")));
                    objSelect1.selectByVisibleText("Promoted/Passed with Examination");

                    //Thread.sleep(2000);
                    driver.findElement(By.xpath("(//input)[2]")).clear();
                    driver.findElement(By.xpath("(//input)[2]")).sendKeys(percentage);

                    //Thread.sleep(2000);
                    driver.findElement(By.xpath("(//input)[3]")).clear();
                    driver.findElement(By.xpath("(//input)[3]")).sendKeys(workingDay);

                    //Thread.sleep(2000);
                    Select objSelect2 = new Select(driver.findElement(By.xpath("(//td//select)[2]")));
                    objSelect2.selectByVisibleText("Studying in Same School");

                    //Thread.sleep(1000);
                    Select objSelect3 = new Select(driver.findElement(By.xpath("(//td//select)[3]")));
                    objSelect3.selectByVisibleText("B");

                    Thread.sleep(1000);
                    driver.findElement(By.xpath("//*[@id=\"page-content-wrapper\"]/main/div/div/div/app-promotion/div[3]/div/table/tbody/tr/td[6]/button[1]")).click();

                    Thread.sleep(2000);
                    Actions actions = new Actions(driver);
                    //actions.sendKeys(Keys.TAB).perform();

                    Thread.sleep(1500);
                    //Actions actions = new Actions(driver);
                    actions.sendKeys(Keys.RETURN).perform();
                    Thread.sleep(10000);

                    //Actions actions2 = new Actions(driver);
                    //actions.sendKeys(Keys.RETURN).perform();
                    //Thread.sleep(2500);
                    // Uncomment if needed
                    // driver.findElement(By.xpath("/html/body/div[4]/div/div[6]/button[1]")).click();
                }
            } catch (IOException e) {
                // Handle potential exceptions related to file reading
//                e.printStackTrace();
                System.out.printf("error inside catch block ");
            }
        }
        catch (Exception e){
            System.out.printf("error main me hai");
        }
        finally {
            //Close the driver
            driver.quit();
        }
    }

//    public String  captchaReader(String imagePath) {
//
//
//        try {
//            BufferedImage captchaImage = downloadImage(imagePath);
//
//            Tesseract tesseract = new Tesseract();
//            return tesseract.doOCR(captchaImage);
//
//        } catch (TesseractException | IOException e) {
//            e.printStackTrace();
//            return "";
//        }
//    }
//    // Method to download image from URL

}
