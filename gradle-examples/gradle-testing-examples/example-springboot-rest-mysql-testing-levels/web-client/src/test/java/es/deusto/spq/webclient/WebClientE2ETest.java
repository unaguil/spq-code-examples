package es.deusto.spq.webclient;

import static org.assertj.core.api.Assertions.assertThat;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@Tag("e2e")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WebClientE2ETest {

    private static final String BASE = "http://localhost:8081";

    private Playwright playwright;
    private Browser browser;
    private Page page;

    @BeforeAll
    void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
    }

    @BeforeEach
    void newPage() {
        page = browser.newPage();
    }

    @AfterEach
    void closePage() {
        page.close();
    }

    @AfterAll
    void closeBrowser() {
        browser.close();
        playwright.close();
    }

    @Test
    void homePage_showsAllNavigationLinks() {
        page.navigate(BASE + "/");

        assertThat(page.locator("a[href*='/users/register']").count()).isGreaterThan(0);
        assertThat(page.locator("a[href*='/users/list']").count()).isGreaterThan(0);
        assertThat(page.locator("a[href*='/messages/post']").count()).isGreaterThan(0);
        assertThat(page.locator("a[href*='/messages/list']").count()).isGreaterThan(0);
    }

    @Test
    void registerUser_showsSavedConfirmation() {
        page.navigate(BASE + "/users/register");

        page.locator("input[name='login']").fill("playwright-user");
        page.locator("input[name='password']").fill("pw123");
        page.locator("button[type='submit']").click();

        String alertText = page.locator(".alert-success").textContent();
        assertThat(alertText).contains("Saved");
        assertThat(alertText).contains("playwright-user");
    }

    @Test
    void listUsers_showsRegisteredUser() {
        // Ensure user exists first
        page.navigate(BASE + "/users/register");
        page.locator("input[name='login']").fill("playwright-user");
        page.locator("input[name='password']").fill("pw123");
        page.locator("button[type='submit']").click();

        page.navigate(BASE + "/users/list");

        assertThat(page.locator("tbody tr td").allTextContents())
                .anyMatch(text -> text.contains("playwright-user"));
    }

    @Test
    void postMessage_showsEchoedMessage() {
        // Ensure user exists first
        page.navigate(BASE + "/users/register");
        page.locator("input[name='login']").fill("playwright-user");
        page.locator("input[name='password']").fill("pw123");
        page.locator("button[type='submit']").click();

        page.navigate(BASE + "/messages/post");
        page.locator("input[name='login']").fill("playwright-user");
        page.locator("input[name='password']").fill("pw123");
        page.locator("textarea[name='message']").fill("Hello from Playwright");
        page.locator("button[type='submit']").click();

        String alertText = page.locator(".alert-success").textContent();
        assertThat(alertText).contains("Hello from Playwright");
    }

    @Test
    void listMessages_showsPostedMessage() {
        // Ensure user and message exist first
        page.navigate(BASE + "/users/register");
        page.locator("input[name='login']").fill("playwright-user");
        page.locator("input[name='password']").fill("pw123");
        page.locator("button[type='submit']").click();

        page.navigate(BASE + "/messages/post");
        page.locator("input[name='login']").fill("playwright-user");
        page.locator("input[name='password']").fill("pw123");
        page.locator("textarea[name='message']").fill("Hello from Playwright");
        page.locator("button[type='submit']").click();

        page.navigate(BASE + "/messages/list");
        page.locator("input[name='login']").fill("playwright-user");
        page.locator("button[type='submit']").click();

        assertThat(page.locator(".list-group-item").allTextContents())
                .anyMatch(text -> text.contains("Hello from Playwright"));
    }
}
