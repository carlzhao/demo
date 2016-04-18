package neo.test.demo;

import java.nio.ByteBuffer;

/**
 * PDU打包工具类
 * 
 * @author carlzhao
 * @date 2016-03-01
 */
public class PDUtil {

	/*--------------------------------*
	 *|0x04|PDU_HEADER|JCE_ENCODE|0x05|
	 *--------------------------------*/

	ByteBuffer mBuffer = ByteBuffer.allocate(1024);

	public static int vtolh(byte[] bArr) {
		int n = 0;
		for (int i = 0; i < bArr.length && i < 4; i++) {
			int left = i * 8;
			n += (bArr[i] << left);
		}
		return n;
	}

	public byte[] encode(int cmd, byte[] buf) {
		byte[] result = new byte[buf.length + 25];

		try {
			int offset = 0;

			byte[] temp = { 0x04 };
			System.arraycopy(temp, 0, result, offset, 1);
			offset += 1;

			PduHeader header = new PduHeader(cmd, buf.length);
			System.arraycopy(header.getBuf(), 0, result, offset,
					header.getBuf().length);
			offset += header.getBuf().length;

			System.arraycopy(buf, 0, result, offset, buf.length);
			offset += buf.length;

			temp[0] = 0x05;
			System.arraycopy(temp, 0, result, offset, 1);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}

		return result;
	}

	public byte[] decode(byte[] buf) {
		if (!check(buf)) {
			return null;
		}

		byte[] result = null;

		try {
			byte[] header_buf = new byte[PduHeader.length];
			System.arraycopy(buf, 1, header_buf, 0, PduHeader.length);
			PduHeader header = PduHeader.decode(header_buf);
			result = new byte[header.len];
			System.arraycopy(buf, 1 + PduHeader.length, result, 0, header.len);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}

		return result;
	}

	public boolean check(byte[] buf) {
		byte[] temp = new byte[1];

		System.arraycopy(buf, 0, temp, 0, 1);
		int prefix = vtolh(temp);
		if (prefix != 0x04) {
			System.out.println("decode error !");
			return false;
		}

		int offset = buf.length - 1;
		System.arraycopy(buf, offset, temp, 0, 1);
		int suffix = vtolh(temp);
		if (suffix != 0x05) {
			System.out.println("decode error !");
			return false;
		}

		return true;
	}

	public static byte[] int2byte(int n) {
		byte[] b = new byte[4];
		b[0] = (byte) (n >> 24);
		b[1] = (byte) (n >> 16);
		b[2] = (byte) (n >> 8);
		b[3] = (byte) (n);
		return b;
	}

	public static void int2byte(int n, byte buf[], int offset) {
		buf[offset] = (byte) (n >> 24);
		buf[offset + 1] = (byte) (n >> 16);
		buf[offset + 2] = (byte) (n >> 8);
		buf[offset + 3] = (byte) n;
	}

	public static int byte2int(byte b[]) {
		return b[3] & 0xff | (b[2] & 0xff) << 8 | (b[1] & 0xff) << 16
				| (b[0] & 0xff) << 24;
	}

	public static byte[] short2byte(int n) {
		byte b[] = new byte[2];
		b[0] = (byte) (n >> 8);
		b[1] = (byte) n;
		return b;
	}

	// long到byte的转换
	public static byte[] long2byte(long n) {
		byte b[] = new byte[8];
		b[0] = (byte) (int) (n >> 56);
		b[1] = (byte) (int) (n >> 48);
		b[2] = (byte) (int) (n >> 40);
		b[3] = (byte) (int) (n >> 32);
		b[4] = (byte) (int) (n >> 24);
		b[5] = (byte) (int) (n >> 16);
		b[6] = (byte) (int) (n >> 8);
		b[7] = (byte) (int) n;
		return b;
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
