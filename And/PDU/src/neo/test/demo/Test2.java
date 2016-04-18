package neo.test.demo;

public class Test2 {
	
	
	public static int vtolh(byte[] bArr) {
		int n = 0;
		for (int i = 0; i < bArr.length && i < 4; i++) {
			int left = i * 8;
			n += (bArr[i] << left);
		}
		return n;
	}
	
	public static int byte2int(byte b[]) {
		return b[3] & 0xff 
				| (b[2] & 0xff) << 8 
				| (b[1] & 0xff) << 16
				| (b[0] & 0xff) << 24;
	}
	
	
	public static byte[] int2byte(int n) {
		byte[] b = new byte[4];
		b[0] = (byte) (n >> 24);
		b[1] = (byte) (n >> 16);
		b[2] = (byte) (n >> 8);
		b[3] = (byte) (n);
		return b;
	}
	
	public static void main(String args[]) {
		byte[] buffer = int2byte(12);
		printHexString("-------------", buffer);
		
		int n = vtolh(buffer);
		System.out.println(n);
		
		n = byte2int(buffer);
		System.out.println(n);
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
