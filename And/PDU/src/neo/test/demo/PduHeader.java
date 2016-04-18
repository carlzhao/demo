package neo.test.demo;


/**
struct pdu_protocol_header{
   uint8_t version;
   uint32_t cmd;
   uint16_t checksum;
   uint32_t seq;
   uint32_t key;
   uint8_t response_flag;	// 2 byte, Server端回应标识 :
   	  0 - [正常数据, 处理成功],
      1 - [正常数据, 处理失败]
      2 - [异常数据, 服务器拒绝处理]
      3 - [正常数据, 服务器忙, 可重试]
      10 - [服务器重定向]
      20 - [回执包],
      100 - [client请求, 非server回应]

   uint16_t response_info;	// 2 bytes, Server端回应附加信息
                 对于处理失败(1):  表示处理失败的错误号errcode
                 对于服务器忙(3):  表示重试时间(网络字节序)
                 对于服务器拒绝服务(2): 表示拒绝原因(网络字节序)
                 其中, 服务器拒绝服务原因定义如下:
                 使用的每bit表示不同的拒绝理由, 由低位字节至高分别定义为:
	      0x1: 当前协议版本
	      0x2: 当前协议命令字
	      0x4: 当前client类型
	      0x8: 当前client版本
	      0x10: 当前client子系统
                  相应的位置1表示拒绝, 置0表示不拒绝, 如5位全为0表示无理由拒绝.
                 例如, 服务器拒绝当前client类型的当前client版本, 则ServerResponseInfo的取值为0x12.
                 
   char reserved[1];	// 预留
   uint32_t len;		// 协议总长度
*/
public class PduHeader {
	public static int length = 23;
	private byte[] buf = new byte[1 + 4 + 2 + 4 + 4 + 1 + 2 + 1 + 4]; // 23
	
	public static class OFFSET {
        public static int VERSION = 0;
        public static int CMD = 0x01;
        public static int CHECKSUM = 0x05;
        public static int SEQ = 0x07;
        public static int KEY = 0x0B;
        public static int RESPONSE_FLAG = 0x0F;
        public static int RESPONSE_INFO = 0x10;
        public static int RESERVERD = 0x12;
        public static int LEN = 0x13;
	}
	
	public final int version = 0;
	public int cmd = 0;
	public int checksum = 0;
	public int seq = 0;
	public int key = 0;
	public short response_flag = 0;
	public short response_info = 0;
	public char reserved;
	public int len = 0;
	
	public PduHeader(int cmd, int len) {
		this.cmd = cmd;
		this.len = len;
		
		byte[] temp = new byte[4];

		temp = toLH(cmd);
		System.arraycopy(temp, 0, buf, OFFSET.CMD, temp.length);

		temp = toLH(len);
		System.arraycopy(temp, 0, buf, OFFSET.LEN, temp.length);
	}

	public static PduHeader decode(byte[] bArr) {
		int cmd = 0;
		int len = 0;

		byte[] temp = new byte[4];

		System.arraycopy(bArr, OFFSET.CMD, temp, 0, 4);
		cmd = vtolh(temp);

		System.arraycopy(bArr, OFFSET.LEN, temp, 0, 4);
		len = vtolh(temp);

		return new PduHeader(cmd, len);
	}
	
	private static byte[] toLH(int n) {
		byte[] b = new byte[4];
		b[0] = (byte) (n & 0xff);
		b[1] = (byte) (n >> 8 & 0xff);
		b[2] = (byte) (n >> 16 & 0xff);
		b[3] = (byte) (n >> 24 & 0xff);
		return b;
	}
	
	private static int vtolh(byte[] bArr) {
		int n = 0;
		for (int i = 0; i < bArr.length && i < 4; i++) {
			int left = i * 8;
			n += (bArr[i] << left);
		}
		return n;
	}

	public byte[] getBuf() {
		return buf;
	}
	
}
