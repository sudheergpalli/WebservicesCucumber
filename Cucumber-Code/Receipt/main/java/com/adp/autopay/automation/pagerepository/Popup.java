package com.adp.autopay.automation.pagerepository;

import org.openqa.selenium.By;

public class Popup
{

	public static By ClickReveallink= By.xpath(".//span[contains(@id, 'reveal') or contains(@id,'Reveal')]");
	public static By Clicklink= By.xpath(".//span[text()='Show Details']");
	public static By clickclosebutton= By.xpath(".//span[contains(@id, 'Close') or contains(@id,'close')]");
	public static By SSNUnmasked= By.xpath(".//td[text()='SSN']/following-sibling::td");
	public static By closeinuseractivity= By.xpath(".//*[@id='detailsDialog']/div/div[1]/span[contains(@class,'dijitDialogCloseIcon')]");
	
	
}
