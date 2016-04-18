package neo.test.demo;

import java.io.FileOutputStream;

public class Test {
	public static void main(String args[]) {
		FileOutputStream fos = null;

		String abc = "abcieiiwiww......\n";
		byte[] buffer = abc.getBytes();
		
		printHexString("-------------------", buffer);
		
		System.out.println("abc: length: " + buffer.length);

		PDUtil pdu = new PDUtil();
		byte[] data = pdu.encode(1, buffer);
		printHexString("-------------------", data);

		byte[] decoded = pdu.decode(data);
		printHexString("-------------------", decoded);
		
		try {
			fos = new FileOutputStream("D:\\out.log");
			fos.write(data, 0, data.length);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				fos.close();
			} catch (Exception e2) {
				System.out.println(e2);
			}
		}

	}

	public static void printHexString(String hint, byte[] b) {
		System.out.println(hint);
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			System.out.print(hex.toUpperCase() + " ");
		}
		
		System.out.println("");
	}
}
