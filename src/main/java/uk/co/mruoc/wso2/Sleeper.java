package uk.co.mruoc.wso2;

public class Sleeper {

    public void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new WaitException(e);
        }
    }

}
