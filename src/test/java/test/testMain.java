package test;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.*;
import java.time.Duration;
import java.util.List;
import java.util.Scanner;
import org.openqa.selenium.support.ui.ExpectedConditions;


public class testMain {

    public static void main(String[] args) throws IOException {


        File f =new File("assets/idpswd.csv");
        if(f.exists()) {
            System.out.println("Bulundu");
        }
        else{
            System.out.println("Bulunamadı");
        }
        Scanner s = new Scanner(f);

        String satir =s.nextLine();
        String[] idpswd = satir.split(";");

        s.close();



        System.setProperty("webdriver.chrome.driver","drivers/chromedriver.exe");
        WebDriver driver =new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.network.com.tr/");


        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("search")));
        System.out.println("Sayfa yüklendi");


        Actions action = new Actions(driver);
        action.sendKeys(Keys.PAGE_DOWN).build().perform();

        WebElement searchField = driver.findElement(By.id("search"));
        searchField.sendKeys("ceket");
        searchField.sendKeys(Keys.RETURN);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("product__price")));


        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement seeMooreField = driver.findElement(By.className("productList__moreContent"));
        js.executeScript("arguments[0].scrollIntoView();", seeMooreField);

        String urlPage1 = driver.getCurrentUrl();
        action.sendKeys(Keys.PAGE_UP).build().perform();


        WebElement seeMoreButton = driver.findElement(By.xpath("//*[@id=\"pagedListContainer\"]/div[2]/div[2]/button"));
        seeMoreButton.click();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }


        String urlPage2 = driver.getCurrentUrl();

        System.out.println("Sayfa/url değişimi: "+!(urlPage1.equals(urlPage2)));


        WebElement discountedProduct = driver.findElement(By.className("product__discountPercent"));
        js.executeScript("arguments[0].scrollIntoView();", discountedProduct);
        action.sendKeys(Keys.PAGE_UP).build().perform();


        WebElement parent = discountedProduct.findElement(By.xpath(".."));


        WebElement parent2 = parent.findElement(By.xpath(".."));


        WebElement parent3 = parent2.findElement(By.xpath(".."));


        WebElement tr = parent3.findElement(By.className("radio-box__label"));


        WebElement priceElement = parent3.findElement((By.xpath("//span[@class='product__price -actual']")));
        String priceTL = priceElement.getAttribute("innerHTML");
        priceTL=priceTL.replace("\n","");
        priceTL=priceTL.replace(" ","");
        priceTL=priceTL.replace("TL","");
        priceTL=priceTL.replace(".","");
        priceTL=priceTL.replace(",","");


        WebElement trtest = tr.findElement(By.xpath(".."));


        List<WebElement> children2 = trtest.findElements(By.xpath("./child::*"));



        String selectedSize="";
        List<WebElement> trr = parent3.findElements(By.className("radio-box__label"));
        for (int i=0;i< trr.size();i++){
            WebElement trtr = trr.get(i).findElement(By.xpath(".."));;
            String trtrString = trtr.getAttribute("innerHTML");
            List<WebElement> children = trtr.findElements(By.xpath("./child::*"));
            if(children.get(0).isEnabled()){
                selectedSize=children.get(1).getAttribute("innerHTML");
                action.moveToElement(trtr).build().perform();
                trtr.click();
                System.out.println("Seçilen beden: "+selectedSize);
                break;
            }
        }

        WebElement cartGo= driver.findElement(By.className("header__basketSummary"));
        WebElement cartGo2= cartGo.findElement(By.className("button"));
        cartGo2.click();

        String yeniBeden = driver.findElement(By.className("cartItem__attrValue")).getAttribute("innerHTML");
        System.out.println("Beden kontrolü:"+ yeniBeden.equals(selectedSize));


        String yeniFiyat = driver.findElement(By.className("cartItem__prices")).findElements(By.xpath("./child::*")).get(0).getAttribute("innerHTML");
        yeniFiyat=yeniFiyat.replace("\n","");
        yeniFiyat=yeniFiyat.replace(" ","");
        yeniFiyat=yeniFiyat.replace("TL","");
        yeniFiyat=yeniFiyat.replace(".","");
        yeniFiyat=yeniFiyat.replace(",","");

        String yeniGecmisFiyat = driver.findElement(By.className("cartItem__prices")).findElements(By.xpath("./child::*")).get(1).getAttribute("innerHTML");
        yeniGecmisFiyat=yeniGecmisFiyat.replace("\n","");
        yeniGecmisFiyat=yeniGecmisFiyat.replace(" ","");
        yeniGecmisFiyat=yeniGecmisFiyat.replace("TL","");
        yeniGecmisFiyat=yeniGecmisFiyat.replace(".","");
        yeniGecmisFiyat=yeniGecmisFiyat.replace(",","");

        System.out.println("indirimli fiyat normal fiyattan düşük mü: "+(Integer.parseInt(yeniGecmisFiyat)>Integer.parseInt(yeniFiyat)));

        WebElement cntBtn=driver.findElement(By.className("continueButton"));
        cntBtn.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@type='submit']")));

        WebElement inputID = driver.findElement(By.xpath("//input[@class='input js-trim']"));
        WebElement inputPSWD = driver.findElement(By.id("n-input-password"));
        inputID.sendKeys(idpswd[0]);
        inputPSWD.sendKeys(idpswd[1]);
        WebElement girisyapbtn = driver.findElement(By.xpath("//button[@type='submit']"));
        System.out.println("Giriş yapılıyor.");
        girisyapbtn.click();

        WebElement logo = driver.findElement(By.className("header__logo"));
        logo.click();

        WebElement shoppingCart = driver.findElement(By.xpath("//button[@class='header__basketTrigger js-basket-trigger -desktop']"));
        shoppingCart.click();

        WebElement removeButton = driver.findElement(By.xpath("//div[@class='header__basketProductBtn header__basketModal -remove']"));
        removeButton.click();

        WebElement removeApproveButton = driver.findElement(By.xpath("//button[@class='btn -black o-removeCartModal__button']"));
        removeApproveButton.click();

        shoppingCart = driver.findElement(By.xpath("//button[@class='header__basketTrigger js-basket-trigger -desktop']"));
        shoppingCart.click();

        try {
            WebElement emptyCart = driver.findElement(By.xpath("//span[@class='header__emptyBasketText']"));
            System.out.println("sepet boş");

        }
        catch (Exception e){
            System.out.println("sepet boş değil");

        }
    }
}
