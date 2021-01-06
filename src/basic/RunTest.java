package basic;

import java.net.MalformedURLException;



public class RunTest {
	public static void main(String[] args) throws MalformedURLException, InterruptedException {
		try {
			LaunchTest obj = new LaunchTest();
			obj.excuteLogin();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}

