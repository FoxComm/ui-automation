package base;

import com.codeborne.selenide.Screenshots;
import com.google.common.io.Files;
import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestResult;
import ru.yandex.qatools.allure.annotations.Attachment;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLConnection;

public class ScreenShooter implements IHookable {

    @Override
    public void run(IHookCallBack callBack, ITestResult testResult) {
        callBack.runTestMethod(testResult);
        if (testResult.getThrowable() != null) {
            try {
                takeScreenShotStep(testResult.getMethod().getMethodName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void takeScreenShotStep(String methodName) throws IOException {
        File lastScreenShot = Screenshots.getLastScreenshot();
        if (lastScreenShot != null) {
            byte[] bytes = Files.toByteArray(lastScreenShot);
            if (isByteArrIsText(bytes))
                takeScreenShot(methodName, bytes);
            else
                errorText(new String(bytes, "UTF-8"));
        } else {
            log("MyScreenShotListener: takeScreenShotStep: new ScreenShot");
            File newScreenShot = Screenshots.takeScreenShotAsFile();

            if (newScreenShot != null) {
                byte[] bytes = Files.toByteArray(newScreenShot);
                if (isByteArrIsText(bytes))
                    takeScreenShot(methodName, bytes);
                else
                    errorText(new String(bytes, "UTF-8"));
            } else {
                log("MyScreenShotListener: takeScreenShotStep: ScreenShot is null, return text attachment");
                errorText("ScreenShot is null");
            }
        }
    }

    private boolean isByteArrIsText(byte[] value) {
        String contentType = null;
        try {
            contentType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(value));
            if ("image/png".equals(contentType))
                return true;
            else {
                log("contentType is: " + contentType);
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Attachment(value = "Failure in method <{0}>", type = "text/html")
    private String errorText(String text) {
        return text;
    }

    @Attachment(value = "Failure in method <{0}>", type = "image/png")
    private byte[] takeScreenShot(String methodName, byte[] image) throws IOException {
        return image;
    }

    private void log(String text) {
        //nothing
    }

}
