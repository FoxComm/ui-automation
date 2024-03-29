package listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import static base.ConciseAPI.findInText;
import static com.codeborne.selenide.Selenide.close;

public class LogExtender implements ITestListener {

    private String testMethodName(ITestResult result) {
        return "[" + result.getMethod().getMethodName() + "]";
    }

    private boolean throwableNotNull(ITestResult result) {
        return result.getThrowable() != null;
    }

    private void printError(ITestResult result) {
        System.out.println("-----");
        System.out.println(result.getThrowable());
    }

    @Override
    public void onTestStart(ITestResult result) {
//        System.out.println("==== ==== ==== ====");
        System.out.println(testMethodName(result) + " Running test method...");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
//        System.out.println(testMethodName(result) + " PASSED!");
    }

    @Override
    public void onTestFailure(ITestResult result) {
//        System.out.println(testMethodName(result) + " FAILED!");
        if (throwableNotNull(result)) printError(result);
        if (findInText(result.getThrowable().toString(), "TimeoutException: timeout: cannot determine loading status")) {
            close();
        }
//        "TimeoutException: timeout: cannot determine loading status"
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        if (throwableNotNull(result)) printError(result);
        if (findInText(result.getThrowable().toString(), "TimeoutException: timeout: cannot determine loading status")) {
            close();
        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
//        System.out.println(testMethodName(result) + " FAILED WITH SUCCESS PERCENTAGE!");
//        if (throwableNotNull(result)) printError(result);
    }

    @Override
    public void onStart(ITestContext context) {}

    @Override
    public void onFinish(ITestContext context) {}

}
