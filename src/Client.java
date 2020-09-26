import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.security.*;
import javax.crypto.spec.*;
import javax.crypto.*;
import javax.swing.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.security.PublicKey;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.Key;

public class Client extends JFrame implements Serializable{
	private static final long serialVersionUID = 1;
	private JTextField usernamefield;
	private JTextField receiverfield;
	private JPasswordField passwordfield;
	private JTextArea textArea;
	private JTextField messagefield;
	private JPanel panel1,panel2,panel3;
	private JButton connectButton ;
	private JButton sendButton;
	private JButton LoginButton;
	private JButton NewButton;
	private JButton OutButton;
	private JButton BuyButton;
	String KEY_c="99999999";
	byte[] Key_c=KEY_c.getBytes();
	String IDc,ADc; 
	String IDtgs="TgsServer",IDv="YyServer";
	long TS1,TS2,TS3,TS4,TS5;
	long Lifetime2=60000000,Lifetime4;
	Ticket_tgs ticket_tgs;
	Ticket_v ticket_v;
	Authenticator_tgs Auct;
	Authenticator_v Aucv;
	Decrypt_AS_C deac;
	Decrypt_TGS_C detc;
	byte[] subkey1;
	boolean pass;
	//socket变量
	Socket clientASSocket,clientTGSSocket,clientServerSocket;
	int port;
	ObjectOutputStream outstream,outstream2,outstream3;
	ObjectInputStream instream,instream2,instream3;
	String ip_AS = "192.168.43.215";
	String ip_TGS = "192.168.43.83";
	String ip_V = "192.168.43.195";
	DesSer des=new DesSer();
	Type_Switch change=new Type_Switch();
	
	public Client() throws Exception{
		super("Keberos客户端");
		Container container = getContentPane();
		container.setLayout( new BorderLayout());
		panel1 = new JPanel();
		panel1.setLayout( new FlowLayout());
		container.add(panel1,BorderLayout.NORTH);
		JLabel usernameLabel = new JLabel("帐号:");
		panel1.add(usernameLabel);
		usernamefield = new JTextField(10);
		panel1.add(usernamefield);
		JLabel passwordLabel = new JLabel("密码:");
		panel1.add(passwordLabel);
		passwordfield = new JPasswordField(10);
		panel1.add(passwordfield);	
		LoginButton = new JButton("登录");
		panel1.add(LoginButton);
		NewButton = new JButton("注册");
		panel1.add(NewButton);
		OutButton = new JButton("下线");
		panel1.add(OutButton);
		panel2 = new JPanel();
		panel2.setLayout( new FlowLayout());
		container.add(panel2,BorderLayout.WEST);
		connectButton = new JButton("连接");
		panel2.add(connectButton); 
		connectButton.addActionListener(
				new ActionListener(){
					public void actionPerformed (ActionEvent event)
					{
						try{	
							IDc = usernamefield.getText();
							textArea.append("开始认证...");
							authentication();
						}
						catch(Exception ex)
						{
							ex.printStackTrace();
						}
					}
				}
		);
		LoginButton.addActionListener(
				new ActionListener(){
					public void actionPerformed (ActionEvent event) {
						talkToServer0("0",usernamefield.getText(),passwordfield.getText());
					}
				}
		);
		NewButton.addActionListener(
				new ActionListener(){
					public void actionPerformed (ActionEvent event) {
						talkToServer1("1",usernamefield.getText(),passwordfield.getText());
					}
				}
		);
		OutButton.addActionListener(
				new ActionListener(){
					public void actionPerformed (ActionEvent event) {
						talkToServer2("2",usernamefield.getText(),passwordfield.getText());
					}
				}
		);
		textArea = new JTextArea(10,20);
		textArea.setEditable(false);
		container.add( new JScrollPane(textArea),BorderLayout.CENTER);
		panel3 = new JPanel();
		panel3.setLayout( new FlowLayout());
		container.add(panel3,BorderLayout.SOUTH);
		//JLabel receiverLabel = new JLabel("发送给:");
		//panel3.add(receiverLabel);
		//receiverfield = new JPasswordField(10);
		//panel3.add(receiverfield);
		messagefield = new JTextField(20);
		panel3.add(messagefield);
		sendButton = new JButton("查询");
		sendButton.addActionListener(
				new ActionListener(){
					public void actionPerformed (ActionEvent event)
					{
						try{	
							if(pass){
								talkToServer3("3",usernamefield.getText(),messagefield.getText());
							}
							else{
								textArea.append("认证失败！\n");
							}
						}
						catch(Exception ex)
						{
							ex.printStackTrace();
						}
					}
				}
		);
		/*LoginButton.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						
						talkToServer(usernamefield.getText());
						//C_TGS c_tgs = new C_TGS(IDv,deac.getTickettgs(),Auct);
						//C_S_Function c_s_login = new C_S_Function("1",usernamefield.getText(),passwordfield.getText());
					}
				}
				);*/
		panel3.add(sendButton);
		BuyButton = new JButton("购买");
		BuyButton.addActionListener(
				new ActionListener(){
					public void actionPerformed (ActionEvent event)
					{
						try{	
							if(pass){
								talkToServer3("4",usernamefield.getText(),messagefield.getText());
							}
							else{
								textArea.append("认证失败！\n");
							}
						}
						catch(Exception ex)
						{
							ex.printStackTrace();
						}
					}
				}
		);
		/*BuyButton.addActionListener(
				new ActionListener() {
					public void actionPerformer(ActionEvent event) {
						talkToServer3("4",usernamefield.getText(),messagefield.getText());
					}
				}
				);*/
		panel3.add(BuyButton);
		setSize(800, 500);
		setVisible(true);
	}
	
	public void initClientASSocket(int port)//初始化客户端到AS的socket套接字，端口号为5000
	{	
		this.port = port;
		try {
			clientASSocket = new Socket(ip_AS, port);
			outstream = new ObjectOutputStream(clientASSocket.getOutputStream());//outputStream把对象转成字节数据的输出到文件中保存，对象的输出过程称为序列化，可实现对象的持久存储。
			instream = new ObjectInputStream(clientASSocket.getInputStream());//将之前使用 ObjectOutputStream 序列化的原始数据恢复为对象，以流的方式读取对象。 
			outstream.flush();
			textArea.append("与AS通信的socket初始化完毕...\n");
		} 
		catch (Exception e) {
			e.printStackTrace();
		}             
	}

	public void initClientTGSSocket(int port){//初始化客户端到TGS的socket套接字，端口号为6000
		this.port = port;
		try {
			clientTGSSocket = new Socket(ip_TGS, port);
			outstream2 = new ObjectOutputStream(clientTGSSocket.getOutputStream());
			instream2 = new ObjectInputStream(clientTGSSocket.getInputStream());
			outstream2.flush();
			textArea.append("与TGS通信的socket初始化完毕...\n");
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void initClientServerSocket(int port){//初始化客户端到应用服务器的socket套接字，端口为7000
		this.port = port;
		try {
			clientServerSocket = new Socket(ip_V, port);
			outstream3 = new ObjectOutputStream(clientServerSocket.getOutputStream());
			instream3 = new ObjectInputStream(clientServerSocket.getInputStream());
			outstream3.flush();
			textArea.append("与Service Server通信的socket初始化完毕...\n");
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public long getTimeStamp()//获得当前时间戳
	{	
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		long now = Long.parseLong(df.format(System.currentTimeMillis()).toString());
		/*long now = System.currentTimeMillis();
		return now;*/
		return now;
	}
	
	public boolean isInSession(long ts1,long ts2,long Lifetime)//判断当前会话是否在有效期内
	{	
		if(ts1 + Lifetime > ts2)
			return true;
		else return false;
	}
	
	public boolean Step1(){//客户端与AS的认证过程
		textArea.append("开始向Authenticator Server发送认证请求...\n");
		try
		{
			TS1 = getTimeStamp();
			C_AS c_as = new C_AS(IDc,IDtgs,TS1);//将数据写入要发送的消息中
			textArea.append("要向AS发送的数据如下：\n");
			textArea.append("IDc："+IDc+"\n");
			textArea.append("IDtgs："+IDtgs+"\n");
			textArea.append("TS1："+TS1+"\n");
			sendmessage(c_as,outstream);
			while(true)
			{
				Object object = receivemessage(instream);
				if(object!=null&&object instanceof String)//如果用户名出错
				{
					String result = (String)object;
					if(result.equals("false"))
					{	
						textArea.append("用户名验证结果：错误的用户名,请重新启动客户端\nAS上未通过验证\n");
						return false;
					}
					else{
						textArea.append("AS验证出错,请重新启动客户端\nAS上未通过验证\n");
						return false;
					}
				}
				if(object!=null&&object instanceof AS_C)//用户名合法
				{
					textArea.append("用户名验证结果：正确\n");
					textArea.append("用户名验证完毕\n");
					AS_C as_c = (AS_C)object;;
					textArea.append("开始获取AS发送给客户端的数据：\n");
					byte[] Keyct0=as_c.getKeyct();
					textArea.append("Key_ct："+new String(Keyct0)+"\n");
					byte[] IDtgs0=as_c.getIDtgs();
					textArea.append("IDtgs："+new String(IDtgs0)+"\n");
					byte[] TS20=as_c.getTS2();
					textArea.append("TS2："+new String(TS20)+"\n");
					byte[] Lifetime20=as_c.getLifetime2();
					textArea.append("Lifetime2："+new String(Lifetime20)+"\n");
					Ticket_tgs Tickettgs=as_c.getTickettgs();
					textArea.append("Ticket_tgs："+Tickettgs+"\n");
					textArea.append("开始对AS发送给客户端的数据用客户端的密钥："+KEY_c+"进行解密\n");
					textArea.append("解密后的数据如下（ticket_tgs客户端无法解密）：\n");
					byte[] En_Keyct=des.decryptByDES(Keyct0, Key_c);
					textArea.append("Key_ct："+new String(En_Keyct)+"\n");
					byte[] En_IDtgs0=des.decryptByDES(IDtgs0, Key_c);
					String En_IDtgs=new String(En_IDtgs0);
					textArea.append("IDtgs："+En_IDtgs+"\n");
					byte[] En_TS20=des.decryptByDES(TS20, Key_c);
					long En_TS2=change.bytesToLong(En_TS20);
					textArea.append("TS2："+En_TS2+"\n");
					byte[] En_Lifetime20=des.decryptByDES(Lifetime20, Key_c);
					long En_Lifetime2=change.bytesToLong(En_Lifetime20);
					textArea.append("Lifetime2："+En_Lifetime2+"\n");
					deac=new Decrypt_AS_C(En_Keyct,En_IDtgs,En_TS2,En_Lifetime2,Tickettgs);
					textArea.append("数据解密完成\n");
					textArea.append("开始验证该会话是否在有效期内\n");
					if(!isInSession(TS1,deac.getTS2(),deac.getLifetime2())){//验证会话的有效期
						textArea.append("client端的时间合法性未通过验证\n");	
						return false;
					}
					textArea.append("client端的时间合法性验证通过\n");
					break;
				}
			}
			textArea.append("Client向AS验证完毕\n");
			instream.close();
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean Step2(){//客户端与TGS的认证过程
		textArea.append("开始向TGS发送认证请求\n");
		try
		{
			textArea.append("开始加密发给TGS的认证信息\n");
			textArea.append("要加密的数据如下：\n");
			textArea.append("IDc："+IDc+"\n");
			ADc = InetAddress.getLocalHost().getHostAddress();
			textArea.append("ADc："+ADc+"\n");
			TS3 = getTimeStamp();
			textArea.append("TS3："+TS3+"\n");
			textArea.append("通过客户端与TGS的共享密钥"+new String(deac.getKeyct())+"进行加密\n");
			textArea.append("加密后的数据如下：\n");
			byte[] IDcbyte2=IDc.getBytes();
			byte[] En_IDc2=des.encryptByDES(IDcbyte2, deac.getKeyct());
			textArea.append("IDc："+new String(En_IDc2)+"\n");
			byte[] ADcbyte2=ADc.getBytes();
			byte[] En_ADc2=des.encryptByDES(ADcbyte2, deac.getKeyct());
			textArea.append("ADc："+new String(En_ADc2)+"\n");
			byte[] TS3byte2=change.longToBytes(TS3);
			byte[] En_TS3=des.encryptByDES(TS3byte2, deac.getKeyct());
			textArea.append("TS3："+new String(En_TS3)+"\n");
			Auct=new Authenticator_tgs(En_IDc2,En_ADc2,En_TS3);
			textArea.append("发送给TGS的认证信息加密以及封装完成\n");
			C_TGS c_tgs = new C_TGS(IDv,deac.getTickettgs(),Auct);
			textArea.append("发送给TGS的整体信息封装完成\n");
			sendmessage(c_tgs,this.outstream2);
			textArea.append("信息发送成功\n");
			while(true)
			{	
				Object out2 = receivemessage(instream2);
				if(out2!=null&&out2 instanceof String)
				{
					String result = (String)out2;
					if(result.equals("false"))
					{
						textArea.append("TGS上验证未通过\n");
						return false;
					}
				}
				if(out2!=null&&out2 instanceof TGS_C)
				{
					TGS_C tgs_c = (TGS_C)out2;
					textArea.append("接收到了TGS发送给客户端的信息\n");
					byte[] En_Keycv=tgs_c.getKeycv();
					byte[] En_IDv=tgs_c.getIDv();
					byte[] En_TS4=tgs_c.getTS4();
					textArea.append("未解密的数据如下：\n");
					textArea.append("客户端和应用服务器的共享密钥："+new String(En_Keycv)+"\n");
					textArea.append("IDv："+new String(En_IDv)+"\n");
					textArea.append("TS4："+new String(En_TS4)+"\n");
					textArea.append("Ticket_v："+tgs_c.getTicket_v()+"\n");
					textArea.append("对数据进行解密\n");
					textArea.append("解密后的数据如下:\n");
					byte[] Keycv=des.decryptByDES(En_Keycv, deac.getKeyct());
					textArea.append("客户端和应用服务器的共享密钥："+new String(Keycv)+"\n");
					byte[] IDvbyte=des.decryptByDES(En_IDv, deac.getKeyct());
					String IDv_tgs=new String(IDvbyte);
					textArea.append("IDv："+IDv_tgs+"\n");
					byte[] TS4byte=des.decryptByDES(En_TS4, deac.getKeyct());
					long TS4_tgs=change.bytesToLong(TS4byte);
					textArea.append("TS4："+TS4_tgs+"\n");
					detc =new Decrypt_TGS_C(Keycv,IDv_tgs,TS4_tgs,tgs_c.getTicket_v());
					if(!isInSession(TS3,TS4_tgs,Lifetime2))//时间合法性验证
					{	
						textArea.append("TGS上时间验证未通过\n");
						return false;
					}
					
					if(!IDv_tgs.equals(this.IDv))//验证IDv是否为请求的应用服务器ID即"YyServer"
					{
						textArea.append("TGS上对ID service的验证未通过\n");
						return false;
					}
					break;
				}
			}
			textArea.append("TGS上的验证完毕\n");
			instream2.close();
			return true;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
	}
	
    public boolean Step3(){//客户端与应用服务器的认证过程
		textArea.append("开始向应用服务器发送请求\n");
		try
		{
			TS5 = getTimeStamp();
			textArea.append("开始生成发送给应用服务器的认证信息\n");
			textArea.append("具体数据如下：\n");
			textArea.append("IDc:"+IDc+"\n");
			textArea.append("ADc:"+ADc+"\n");
			textArea.append("TS5:"+TS5+"\n");
			textArea.append("开始对数据进行加密\n");
			textArea.append("加密后的数据如下：\n");
			byte[] IDc3=IDc.getBytes();
			byte[] ADc3=ADc.getBytes();
			byte[] TS53=change.longToBytes(TS5);
			byte[] En_IDc3=des.encryptByDES(IDc3, detc.getKeycv());
			byte[] En_ADc3=des.encryptByDES(ADc3, detc.getKeycv());
			byte[] En_TS53=des.encryptByDES(TS53, detc.getKeycv());
			textArea.append("IDc:"+new String(En_IDc3)+"\n");
			textArea.append("ADc:"+new String(En_ADc3)+"\n");
			textArea.append("TS5:"+new String(En_TS53)+"\n");
			Aucv=new Authenticator_v(En_IDc3,En_ADc3,En_TS53);
			textArea.append("认证信息封装完成\n");
			C_S c_s =new C_S(detc.getTicket_v(),Aucv);
			textArea.append("客户端发送给应用服务器的信息封装完成\n");
			sendmessage(c_s,outstream3);
			textArea.append("信息发送成功\n");
			while(true)
			{
				Object out3 = receivemessage(instream3);
				if(out3!=null&&out3 instanceof String)
				{	
					String result = (String)out3;
					if(result.equals("false"))
					{	
						textArea.append("应用服务器上验证未通过\n");
						return false;
					}
				}
				if(out3!=null && out3 instanceof S_C)
				{	
					S_C s_c = (S_C)out3;
					byte[] TS6byte=s_c.getTS6();
					byte[] En_TS6=des.decryptByDES(TS6byte, detc.getKeycv());
					long TS6 = change.bytesToLong(En_TS6);
					textArea.append("将从应用服务器获得消息解密后得到：\n");
					textArea.append("TS6:"+TS6+"\n");
					textArea.append("对其进行验证\n");
					if(TS5+1!=TS6)
					{
						textArea.append("客户端上对应用服务器传来的时间戳的验证未通过\n");
						return false;
					}else {
						textArea.append("客户端上对应用服务器传来的时间戳的验证通过\n");
						textArea.append("客户端可以开始和应用服务器进行通信\n");
						break;
					}
				}
			}
			return true;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
	}
	
	public boolean authentication()throws Exception//认证主流程
	{	
		initClientASSocket(5000);
		if(Step1()){//AS认证
			clientASSocket.close();
			initClientTGSSocket(6000);
			if(Step2()){//TGS认证
				clientTGSSocket.close();
				initClientServerSocket(7000);
				if(Step3()){//应用服务器认证
					pass = true;
					return true;
				}
			}
		}
		return false;
	}
	
	public void sendmessage(Object object,ObjectOutputStream outstream)//发送消息函数
	{
		try
		{
			outstream.writeObject(object);
			textArea.append("消息发送成功/n");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	//C_S client234=new C_S();
	public Object receivemessage(ObjectInputStream instream)//接收信息函数
	{
		try
		{
			return instream.readObject();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return null;
	}
	
	public void talkToServer0(String str1,String str2,String str3)//与应用服务器的通信函数,实现功能为登录
	{	
		try
		{	
			byte[] sessionKey = detc.getKeycv();
			byte[] func0 = des.encryptByDES(str1.getBytes(), sessionKey);
			byte[] Inputusername = des.encryptByDES(str2.getBytes(), sessionKey);
			byte[] Inputpassword = des.encryptByDES(str3.getBytes(), sessionKey);
			textArea.append("功能序号："+new String(func0)+"\n");
			textArea.append("用户名："+new String(Inputusername)+"\n");
			textArea.append("相关信息："+new String(Inputpassword)+"\n");
			//String user = usernamefield.getText();
			//byte[] username =des.encryptByDES(usernamefield.getText(), sessionKey)
			C_S_Function Buffer = new C_S_Function(func0,Inputusername,Inputpassword); 
					//des.encryptByDES(str.getBytes(), sessionKey);
			sendmessage(Buffer,outstream3);
			textArea.append("send a msg Service Server\n");
			while(true) {
				Object out0=receivemessage(instream3);
				if(out0!=null&&out0 instanceof byte[])
				{
					byte[] result0 = (byte[])out0;
					textArea.append("返回信息："+new String(result0)+"\n");
					byte[] result1 = des.decryptByDES(result0, sessionKey);
					String result=new String(result1);
					textArea.append(result);
					break;
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	public void talkToServer1(String str1,String str2,String str3)//与应用服务器的通信函数,实现功能为注册
	{	
		try
		{	
			byte[] sessionKey = detc.getKeycv();
			byte[] func0 = des.encryptByDES(str1.getBytes(), sessionKey);
			byte[] Inputusername = des.encryptByDES(str2.getBytes(), sessionKey);
			byte[] Inputpassword = des.encryptByDES(str3.getBytes(), sessionKey);
			textArea.append("功能序号："+new String(func0)+"\n");
			textArea.append("用户名："+new String(Inputusername)+"\n");
			textArea.append("相关信息："+new String(Inputpassword)+"\n");
			//String user = usernamefield.getText();
			//byte[] username =des.encryptByDES(usernamefield.getText(), sessionKey)
			C_S_Function Buffer = new C_S_Function(func0,Inputusername,Inputpassword); 
					//des.encryptByDES(str.getBytes(), sessionKey);
			sendmessage(Buffer,outstream3);
			textArea.append("send a msg Service Server\n");
			while(true) {
				Object out0=receivemessage(instream3);
				if(out0!=null&&out0 instanceof byte[])
				{
					byte[] result0 = (byte[])out0;
					textArea.append("返回信息："+new String(result0)+"\n");
					byte[] result1 = des.decryptByDES(result0, sessionKey);
					String result=new String(result1);
					textArea.append(result);
					break;
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	public void talkToServer2(String str1,String str2,String str3)//与应用服务器的通信函数,实现功能为下线
	{	
		try
		{	
			byte[] sessionKey = detc.getKeycv();
			byte[] func0 = des.encryptByDES(str1.getBytes(), sessionKey);
			byte[] Inputusername = des.encryptByDES(str2.getBytes(), sessionKey);
			byte[] Inputpassword = des.encryptByDES(str3.getBytes(), sessionKey);
			textArea.append("功能序号："+new String(func0)+"\n");
			textArea.append("用户名："+new String(Inputusername)+"\n");
			textArea.append("相关信息："+new String(Inputpassword)+"\n");
			//String user = usernamefield.getText();
			//byte[] username =des.encryptByDES(usernamefield.getText(), sessionKey)
			C_S_Function Buffer = new C_S_Function(func0,Inputusername,Inputpassword); 
					//des.encryptByDES(str.getBytes(), sessionKey);
			sendmessage(Buffer,outstream3);
			textArea.append("send a msg Service Server\n");
			while(true) {
				Object out0=receivemessage(instream3);
				if(out0!=null&&out0 instanceof byte[])
				{
					byte[] result0 = (byte[])out0;
					textArea.append("返回信息："+new String(result0)+"\n");
					byte[] result1 = des.decryptByDES(result0, sessionKey);
					String result=new String(result1);
					textArea.append(result);
					break;
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	public void talkToServer3(String str1,String str2,String str3)//与应用服务器的通信函数,实现功能为查询
	{	
		try
		{	
			byte[] sessionKey = detc.getKeycv();
			byte[] func0 = des.encryptByDES(str1.getBytes(), sessionKey);
			byte[] Inputusername = des.encryptByDES(str2.getBytes(), sessionKey);
			byte[] Inputgoods = des.encryptByDES(str3.getBytes(), sessionKey);
			textArea.append("功能序号："+new String(func0)+"\n");
			textArea.append("用户名："+new String(Inputusername)+"\n");
			textArea.append("相关信息："+new String(Inputgoods)+"\n");
			//String user = usernamefield.getText();
			//byte[] username =des.encryptByDES(usernamefield.getText(), sessionKey)
			C_S_Function Buffer = new C_S_Function(func0,Inputusername,Inputgoods); 
					//des.encryptByDES(str.getBytes(), sessionKey);
			sendmessage(Buffer,outstream3);
			textArea.append("send a msg Service Server\n");
			while(true) {
				Object out0=receivemessage(instream3);
				if(out0!=null&&out0 instanceof byte[])
				{
					byte[] result0 = (byte[])out0;
					textArea.append("返回信息："+new String(result0)+"\n");
					byte[] result1 = des.decryptByDES(result0, sessionKey);
					String result=new String(result1);
					textArea.append(result);
					break;
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static void main(String args[]) throws Exception{
		Client applocation = new Client();
		applocation.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
}