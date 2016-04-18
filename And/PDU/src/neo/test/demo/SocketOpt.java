package neo.test.demo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

enum Op_Type {
	GET_PARAM, 
	SER_PARAM, 
	REBOOT, 
	FACTORYRESET, 
	UPDATE, 
	VALUE_CHANGE_REPORT, 
	START, 
	HEART_BEAT, 
	GET_PARAM_REPLY, 
	SET_PARAM_REPLY, 
	REBOOT_REPLY, 
	FACTORYRESET_REPLY, 
	UPDATE_REPLY, 
	VALUE_CHANGE_REPORT_REPLY, 
	START_FINISH, 
	HEAT_BEAT_REPLY,
};

class Enum_Op_Type {
	public static final int GET_PARAM = 0;
	public static final int SER_PARAM = 1;
	public static final int REBOOT = 2;
	public static final int FACTORYRESET = 3;
	public static final int UPDATE = 4;
	public static final int VALUE_CHANGE_REPORT = 5;
	public static final int START = 6;
	public static final int HEART_BEAT = 7;
	public static final int GET_PARAM_REPLY = 8;
	public static final int SET_PARAM_REPLY = 9;
	public static final int REBOOT_REPLY = 10;
	public static final int FACTORYRESET_REPLY = 11;
	public static final int UPDATE_REPLY = 12;
	public static final int VALUE_CHANGE_REPORT_REPLY = 13;
	public static final int START_FINISH = 14;
	public static final int HEAT_BEAT_REPLY = 15;
};

class Msger_Info {
	/*
	 * ģ���C�ṹ�壺 
	 * typedef struct _msger_info { 
	 * 		char user[20]; 
	 * 		int cmd; 
	 * 		op_type type; 
	 * 		char msg[1024]; 
	 * 		int len; 
	 * } msger_info;
	 */
	
	public static int length = 1056;
	private byte[] buf = new byte[20 + 4 + 4 + 1024 + 4];

	public String name = "";
	public int cmd = 0;
	public int type = 0;
	public String msg = "";
	public int len = 0;

	private static byte[] toLH(int n) {
		byte[] b = new byte[4];
		b[0] = (byte) (n & 0xff);
		b[1] = (byte) (n >> 8 & 0xff);
		b[2] = (byte) (n >> 16 & 0xff);
		b[3] = (byte) (n >> 24 & 0xff);
		return b;
	}

	public Msger_Info(int cmd, int type, String msg, int len) {
		String name = "manger";

		this.name = name;
		this.cmd = cmd;
		this.type = type;
		this.msg = msg;
		this.len = len;

		byte[] temp = name.getBytes();
		System.arraycopy(temp, 0, buf, 0, temp.length);

		temp = toLH(cmd);
		System.arraycopy(temp, 0, buf, 20, temp.length);

		temp = toLH(type);
		System.arraycopy(temp, 0, buf, 24, temp.length);

		temp = msg.getBytes();
		System.arraycopy(temp, 0, buf, 28, temp.length);

		temp = toLH(len);
		System.arraycopy(temp, 0, buf, 1052, temp.length);
	}

	private static String toStr(byte[] valArr, int startpoint, int maxLen) {
		int index = 0;
		while (index + startpoint < valArr.length && index < maxLen) {
			if (valArr[index + startpoint] == 0) {
				break;
			}
			index++;
		}
		byte[] temp = new byte[index];
		System.arraycopy(valArr, startpoint, temp, 0, index);
		return new String(temp);
	}

	private static int vtolh(byte[] bArr) {
		int n = 0;
		for (int i = 0; i < bArr.length && i < 4; i++) {
			int left = i * 8;
			n += (bArr[i] << left);
		}
		return n;
	}

	public static Msger_Info getmsger_info(byte[] bArr) {
		String name = "";
		String msg = "";
		int cmd = 0;
		int type = 0;
		int len = 0;

		byte[] temp = new byte[4];

		name = toStr(bArr, 0, 20);

		System.out.print(name);

		System.arraycopy(bArr, 20, temp, 0, 4);
		cmd = vtolh(temp);

		System.arraycopy(bArr, 24, temp, 0, 4);
		type = vtolh(temp);

		msg = toStr(bArr, 28, 1024);

		System.arraycopy(bArr, 1052, temp, 0, 4);
		len = vtolh(temp);

		return new Msger_Info(cmd, type, msg, len);
	}

	/* ����Ҫ���͵����� */
	public byte[] getBuf() {
		return buf;
	}
};

public class SocketOpt {
	public static final int PORT_CLIENT = 23416;
	public static final int PORT_SERVER = 23415;
	public static Socket client_socket = null;
	public static ServerSocket server_socket = null;

	public static void ProcessMessage(Socket socket) {
		if (socket == null) {
			System.out.println("Server :" + "input  para  error !");
			return;
		}
		System.out.println("Server" + "Enter ProcessMessage !!!!!!");
		// KtSocket.sock_fd = socket;
		byte[] receive = new byte[Msger_Info.length];
		Msger_Info mmInfo = null;
		try {
			// socket.setSoTimeout(timeout);
			socket.getInputStream().read(receive);
			mmInfo = Msger_Info.getmsger_info(receive);
			System.out.println("Server:" + "received:name is :" + mmInfo.name
					+ ",msg is :" + mmInfo.msg + ",len is :" + mmInfo.len);

			switch (mmInfo.type) {
			case Enum_Op_Type.GET_PARAM:
				break;
			case Enum_Op_Type.START:
				break;
			default:
				break;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int Server_socket_init() {
		Thread ServerThread = null;
		try {
			server_socket = new ServerSocket(PORT_CLIENT);
			if (server_socket == null) {
				System.out.println("Server : The Server is cannot start !!!! ");
				return -1;
			}
			System.out
					.println("Server : The Server is start: " + server_socket);
			ServerThread = new Thread(new Runnable() {

				@Override
				public void run() {
					while (true) {
						try {
							Socket socket = server_socket.accept();
							ProcessMessage(socket);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			});
			ServerThread.start();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static Socket Client_socket_init() {
		Socket socket_client = null;
		InetAddress addr = null;

		try {
			addr = InetAddress.getByName("localhost");
			socket_client = new Socket(addr, PORT_SERVER);
			System.out.println("Server:socket = " + socket_client);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return socket_client;
	}

	/* send message ! */
	public static void send_message(Socket socket, int cmd, int type, String msg, int len) {
		if (socket == null || msg == null || len < 0) {
			System.out.println("Server : input  para  error !");
			return;
		}
		// byte[] receive = new byte[1056];
		int available = 0;
		// Msger_Info mmInfo=null;
		Msger_Info sendmsg = new Msger_Info(cmd, type, msg, msg.length());
		try {
			if ((available = socket.getInputStream().available()) >= 0) {
				// socket.getInputStream().read(new byte[available]);
				socket.getInputStream().skip(available);
			}
			socket.getOutputStream().write(sendmsg.getBuf());
			socket.getOutputStream().flush();
			// socket.getInputStream().read(receive);
			// mmInfo =Msger_Info .getmsger_info(receive);
			// System.out.println("received:name is :"+mmInfo.name+",msg is :"+mmInfo.msg+",len is :"+mmInfo.len);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
		// return mmInfo.msg;
	}

	public static String Receive_message(Socket socket, int len, int timeout) {
		if (socket == null || len < 0) {
			System.out.println("Server ��input  para  error !");
			return null;
		}
		byte[] receive = new byte[len];
		Msger_Info mmInfo = null;
		try {
			socket.setSoTimeout(timeout);
			socket.getInputStream().read(receive);
			mmInfo = Msger_Info.getmsger_info(receive);
			System.out.println("Server:" + "received:name is :" + mmInfo.name
					+ ",msg is :" + mmInfo.msg + ",len is :" + mmInfo.len);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (mmInfo.msg != null) {
			return mmInfo.msg;
		} else {
			return "";
		}
	}

	public static void socket_destory() {
		/* destory !!!! */

		try {
			if (client_socket != null) {
				client_socket.close();
			}
			if (server_socket != null) {
				server_socket.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		int count = 0;
		String valueString = "";
		client_socket = Client_socket_init();
		if (client_socket == null) {
			System.out.println("creat socket error !");
			return;
		}
		
		for (count = 0; count < 15; count++) {
			send_message(client_socket, 93, 0, "", 0);
			valueString = Receive_message(client_socket, Msger_Info.length, 3000);

			if (valueString != null)
				System.out.println("valueString is :" + valueString);

		}
		
		try {
			if (client_socket != null)
				client_socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}