package listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

class RetryAnalyzer implements IRetryAnalyzer {

    private int attempts = 0;
    private int maxRetries = 3;

    /**
    * Returns true if the test method has to be retried, false otherwise.
    */
    @Override
    public boolean retry(ITestResult result) {
        if(attempts < maxRetries) {
            attempts++;
            return true;
        }
        return false;
    }

}
