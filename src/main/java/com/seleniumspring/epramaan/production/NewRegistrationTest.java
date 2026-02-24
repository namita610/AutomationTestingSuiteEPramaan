package com.seleniumspring.epramaan.production;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class NewRegistrationTest extends BaseTest {

    @Test(description = "Verify registration page loads")
    public void verifyRegistrationPage() {
        getDriver().get("https://epramaan.meripehchaan.gov.in/register/nsso/loadRegistrationForm.do");
        Assert.assertTrue(getDriver().getTitle().contains("e-Pramaan"), "Title mismatch");
    }
}
