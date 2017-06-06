package listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

class RetryAnalyzer implements IRetryAnalyzer {

    private int retries = 0;
    private int maxRetries = 2;

    @Override
    public boolean retry(ITestResult result) {
        if (result.getStatus() == 2 || result.getStatus() == 3 || result.getStatus() == 4) {
            if (retries < maxRetries) {
                System.out.println("-----");
                retries ++;
                System.out.println("["+result.getMethod().getMethodName()+"]"+" Test method failed, result is skipped. Retrying ("+retries+"/"+maxRetries +")...");
                return true;
            }
        }
        return false;
    }
}
