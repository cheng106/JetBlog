package com.cheng.jetblog.web;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cheng
 * @since 2022/7/7 22:46
 **/
@Slf4j
@RestController
@RequestMapping("crawl")
public class CrawlController {

    @GetMapping
    public void testCrawl() throws InterruptedException {
        test();
    }

    private void test() throws InterruptedException {
        String SYSTEM_DRIVER = "webdriver.gecko.driver";
        String SYSTEM_PATH = "src/main/resources/driver/geckodriver";
        System.setProperty(SYSTEM_DRIVER, SYSTEM_PATH);

//        ChromeOptions chromeOptions = new ChromeOptions();
        WebDriver driver = new FirefoxDriver();
//        chromeOptions.addArguments("--headless");
//        WebDriver driver = new ChromeDriver(chromeOptions);

        driver.get("https://ithelp.ithome.com.tw/questions/10195825");
        String title = driver.getTitle();
        log.info("==========title:{}", title);
        WebElement element = driver.findElement(By.id("gLFyf.gsfi"));
        log.info("element:{}", element);
        element.sendKeys("Selenium Python");
    }
}
