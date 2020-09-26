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
	//socket����
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
		super("Keberos�ͻ���");
		Container container = getContentPane();
		container.setLayout( new BorderLayout());
		panel1 = new JPanel();
		panel1.setLayout( new FlowLayout());
		container.add(panel1,BorderLayout.NORTH);
		JLabel usernameLabel = new JLabel("�ʺ�:");
		panel1.add(usernameLabel);
		usernamefield = new JTextField(10);
		panel1.add(usernamefield);
		JLabel passwordLabel = new JLabel("����:");
		panel1.add(passwordLabel);
		passwordfield = new JPasswordField(10);
		panel1.add(passwordfield);	
		LoginButton = new JButton("��¼");
		panel1.add(LoginButton);
		NewButton = new JButton("ע��");
		panel1.add(NewButton);
		OutButton = new JButton("����");
		panel1.add(OutButton);
		panel2 = new JPanel();
		panel2.setLayout( new FlowLayout());
		container.add(panel2,BorderLayout.WEST);
		connectButton = new JButton("����");
		panel2.add(connectButton); 
		connectButton.addActionListener(
				new ActionListener(){
					public void actionPerformed (ActionEvent event)
					{
						try{	
							IDc = usernamefield.getText();
							textArea.append("��ʼ��֤...");
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
		//JLabel receiverLabel = new JLabel("���͸�:");
		//panel3.add(receiverLabel);
		//receiverfield = new JPasswordField(10);
		//panel3.add(receiverfield);
		messagefield = new JTextField(20);
		panel3.add(messagefield);
		sendButton = new JButton("��ѯ");
		sendButton.addActionListener(
				new ActionListener(){
					public void actionPerformed (ActionEvent event)
					{
						try{	
							if(pass){
								talkToServer3("3",usernamefield.getText(),messagefield.getText());
							}
							else{
								textArea.append("��֤ʧ�ܣ�\n");
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
		BuyButton = new JButton("����");
		BuyButton.addActionListener(
				new ActionListener(){
					public void actionPerformed (ActionEvent event)
					{
						try{	
							if(pass){
								talkToServer3("4",usernamefield.getText(),messagefield.getText());
							}
							else{
								textArea.append("��֤ʧ�ܣ�\n");
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
	
	public void initClientASSocket(int port)//��ʼ���ͻ��˵�AS��socket�׽��֣��˿ں�Ϊ5000
	{	
		this.port = port;
		try {
			clientASSocket = new Socket(ip_AS, port);
			outstream = new ObjectOutputStream(clientASSocket.getOutputStream());//outputStream�Ѷ���ת���ֽ����ݵ�������ļ��б��棬�����������̳�Ϊ���л�����ʵ�ֶ���ĳ־ô洢��
			instream = new ObjectInputStream(clientASSocket.getInputStream());//��֮ǰʹ�� ObjectOutputStream ���л���ԭʼ���ݻָ�Ϊ���������ķ�ʽ��ȡ���� 
			outstream.flush();
			textArea.append("��ASͨ�ŵ�socket��ʼ�����...\n");
		} 
		catch (Exception e) {
			e.printStackTrace();
		}             
	}

	public void initClientTGSSocket(int port){//��ʼ���ͻ��˵�TGS��socket�׽��֣��˿ں�Ϊ6000
		this.port = port;
		try {
			clientTGSSocket = new Socket(ip_TGS, port);
			outstream2 = new ObjectOutputStream(clientTGSSocket.getOutputStream());
			instream2 = new ObjectInputStream(clientTGSSocket.getInputStream());
			outstream2.flush();
			textArea.append("��TGSͨ�ŵ�socket��ʼ�����...\n");
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void initClientServerSocket(int port){//��ʼ���ͻ��˵�Ӧ�÷�������socket�׽��֣��˿�Ϊ7000
		this.port = port;
		try {
			clientServerSocket = new Socket(ip_V, port);
			outstream3 = new ObjectOutputStream(clientServerSocket.getOutputStream());
			instream3 = new ObjectInputStream(clientServerSocket.getInputStream());
			outstream3.flush();
			textArea.append("��Service Serverͨ�ŵ�socket��ʼ�����...\n");
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public long getTimeStamp()//��õ�ǰʱ���
	{	
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		long now = Long.parseLong(df.format(System.currentTimeMillis()).toString());
		/*long now = System.currentTimeMillis();
		return now;*/
		return now;
	}
	
	public boolean isInSession(long ts1,long ts2,long Lifetime)//�жϵ�ǰ�Ự�Ƿ�����Ч����
	{	
		if(ts1 + Lifetime > ts2)
			return true;
		else return false;
	}
	
	public boolean Step1(){//�ͻ�����AS����֤����
		textArea.append("��ʼ��Authenticator Server������֤����...\n");
		try
		{
			TS1 = getTimeStamp();
			C_AS c_as = new C_AS(IDc,IDtgs,TS1);//������д��Ҫ���͵���Ϣ��
			textArea.append("Ҫ��AS���͵��������£�\n");
			textArea.append("IDc��"+IDc+"\n");
			textArea.append("IDtgs��"+IDtgs+"\n");
			textArea.append("TS1��"+TS1+"\n");
			sendmessage(c_as,outstream);
			while(true)
			{
				Object object = receivemessage(instream);
				if(object!=null&&object instanceof String)//����û�������
				{
					String result = (String)object;
					if(result.equals("false"))
					{	
						textArea.append("�û�����֤�����������û���,�����������ͻ���\nAS��δͨ����֤\n");
						return false;
					}
					else{
						textArea.append("AS��֤����,�����������ͻ���\nAS��δͨ����֤\n");
						return false;
					}
				}
				if(object!=null&&object instanceof AS_C)//�û����Ϸ�
				{
					textArea.append("�û�����֤�������ȷ\n");
					textArea.append("�û�����֤���\n");
					AS_C as_c = (AS_C)object;;
					textArea.append("��ʼ��ȡAS���͸��ͻ��˵����ݣ�\n");
					byte[] Keyct0=as_c.getKeyct();
					textArea.append("Key_ct��"+new String(Keyct0)+"\n");
					byte[] IDtgs0=as_c.getIDtgs();
					textArea.append("IDtgs��"+new String(IDtgs0)+"\n");
					byte[] TS20=as_c.getTS2();
					textArea.append("TS2��"+new String(TS20)+"\n");
					byte[] Lifetime20=as_c.getLifetime2();
					textArea.append("Lifetime2��"+new String(Lifetime20)+"\n");
					Ticket_tgs Tickettgs=as_c.getTickettgs();
					textArea.append("Ticket_tgs��"+Tickettgs+"\n");
					textArea.append("��ʼ��AS���͸��ͻ��˵������ÿͻ��˵���Կ��"+KEY_c+"���н���\n");
					textArea.append("���ܺ���������£�ticket_tgs�ͻ����޷����ܣ���\n");
					byte[] En_Keyct=des.decryptByDES(Keyct0, Key_c);
					textArea.append("Key_ct��"+new String(En_Keyct)+"\n");
					byte[] En_IDtgs0=des.decryptByDES(IDtgs0, Key_c);
					String En_IDtgs=new String(En_IDtgs0);
					textArea.append("IDtgs��"+En_IDtgs+"\n");
					byte[] En_TS20=des.decryptByDES(TS20, Key_c);
					long En_TS2=change.bytesToLong(En_TS20);
					textArea.append("TS2��"+En_TS2+"\n");
					byte[] En_Lifetime20=des.decryptByDES(Lifetime20, Key_c);
					long En_Lifetime2=change.bytesToLong(En_Lifetime20);
					textArea.append("Lifetime2��"+En_Lifetime2+"\n");
					deac=new Decrypt_AS_C(En_Keyct,En_IDtgs,En_TS2,En_Lifetime2,Tickettgs);
					textArea.append("���ݽ������\n");
					textArea.append("��ʼ��֤�ûỰ�Ƿ�����Ч����\n");
					if(!isInSession(TS1,deac.getTS2(),deac.getLifetime2())){//��֤�Ự����Ч��
						textArea.append("client�˵�ʱ��Ϸ���δͨ����֤\n");	
						return false;
					}
					textArea.append("client�˵�ʱ��Ϸ�����֤ͨ��\n");
					break;
				}
			}
			textArea.append("Client��AS��֤���\n");
			instream.close();
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean Step2(){//�ͻ�����TGS����֤����
		textArea.append("��ʼ��TGS������֤����\n");
		try
		{
			textArea.append("��ʼ���ܷ���TGS����֤��Ϣ\n");
			textArea.append("Ҫ���ܵ��������£�\n");
			textArea.append("IDc��"+IDc+"\n");
			ADc = InetAddress.getLocalHost().getHostAddress();
			textArea.append("ADc��"+ADc+"\n");
			TS3 = getTimeStamp();
			textArea.append("TS3��"+TS3+"\n");
			textArea.append("ͨ���ͻ�����TGS�Ĺ�����Կ"+new String(deac.getKeyct())+"���м���\n");
			textArea.append("���ܺ���������£�\n");
			byte[] IDcbyte2=IDc.getBytes();
			byte[] En_IDc2=des.encryptByDES(IDcbyte2, deac.getKeyct());
			textArea.append("IDc��"+new String(En_IDc2)+"\n");
			byte[] ADcbyte2=ADc.getBytes();
			byte[] En_ADc2=des.encryptByDES(ADcbyte2, deac.getKeyct());
			textArea.append("ADc��"+new String(En_ADc2)+"\n");
			byte[] TS3byte2=change.longToBytes(TS3);
			byte[] En_TS3=des.encryptByDES(TS3byte2, deac.getKeyct());
			textArea.append("TS3��"+new String(En_TS3)+"\n");
			Auct=new Authenticator_tgs(En_IDc2,En_ADc2,En_TS3);
			textArea.append("���͸�TGS����֤��Ϣ�����Լ���װ���\n");
			C_TGS c_tgs = new C_TGS(IDv,deac.getTickettgs(),Auct);
			textArea.append("���͸�TGS��������Ϣ��װ���\n");
			sendmessage(c_tgs,this.outstream2);
			textArea.append("��Ϣ���ͳɹ�\n");
			while(true)
			{	
				Object out2 = receivemessage(instream2);
				if(out2!=null&&out2 instanceof String)
				{
					String result = (String)out2;
					if(result.equals("false"))
					{
						textArea.append("TGS����֤δͨ��\n");
						return false;
					}
				}
				if(out2!=null&&out2 instanceof TGS_C)
				{
					TGS_C tgs_c = (TGS_C)out2;
					textArea.append("���յ���TGS���͸��ͻ��˵���Ϣ\n");
					byte[] En_Keycv=tgs_c.getKeycv();
					byte[] En_IDv=tgs_c.getIDv();
					byte[] En_TS4=tgs_c.getTS4();
					textArea.append("δ���ܵ��������£�\n");
					textArea.append("�ͻ��˺�Ӧ�÷������Ĺ�����Կ��"+new String(En_Keycv)+"\n");
					textArea.append("IDv��"+new String(En_IDv)+"\n");
					textArea.append("TS4��"+new String(En_TS4)+"\n");
					textArea.append("Ticket_v��"+tgs_c.getTicket_v()+"\n");
					textArea.append("�����ݽ��н���\n");
					textArea.append("���ܺ����������:\n");
					byte[] Keycv=des.decryptByDES(En_Keycv, deac.getKeyct());
					textArea.append("�ͻ��˺�Ӧ�÷������Ĺ�����Կ��"+new String(Keycv)+"\n");
					byte[] IDvbyte=des.decryptByDES(En_IDv, deac.getKeyct());
					String IDv_tgs=new String(IDvbyte);
					textArea.append("IDv��"+IDv_tgs+"\n");
					byte[] TS4byte=des.decryptByDES(En_TS4, deac.getKeyct());
					long TS4_tgs=change.bytesToLong(TS4byte);
					textArea.append("TS4��"+TS4_tgs+"\n");
					detc =new Decrypt_TGS_C(Keycv,IDv_tgs,TS4_tgs,tgs_c.getTicket_v());
					if(!isInSession(TS3,TS4_tgs,Lifetime2))//ʱ��Ϸ�����֤
					{	
						textArea.append("TGS��ʱ����֤δͨ��\n");
						return false;
					}
					
					if(!IDv_tgs.equals(this.IDv))//��֤IDv�Ƿ�Ϊ�����Ӧ�÷�����ID��"YyServer"
					{
						textArea.append("TGS�϶�ID service����֤δͨ��\n");
						return false;
					}
					break;
				}
			}
			textArea.append("TGS�ϵ���֤���\n");
			instream2.close();
			return true;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
	}
	
    public boolean Step3(){//�ͻ�����Ӧ�÷���������֤����
		textArea.append("��ʼ��Ӧ�÷�������������\n");
		try
		{
			TS5 = getTimeStamp();
			textArea.append("��ʼ���ɷ��͸�Ӧ�÷���������֤��Ϣ\n");
			textArea.append("�����������£�\n");
			textArea.append("IDc:"+IDc+"\n");
			textArea.append("ADc:"+ADc+"\n");
			textArea.append("TS5:"+TS5+"\n");
			textArea.append("��ʼ�����ݽ��м���\n");
			textArea.append("���ܺ���������£�\n");
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
			textArea.append("��֤��Ϣ��װ���\n");
			C_S c_s =new C_S(detc.getTicket_v(),Aucv);
			textArea.append("�ͻ��˷��͸�Ӧ�÷���������Ϣ��װ���\n");
			sendmessage(c_s,outstream3);
			textArea.append("��Ϣ���ͳɹ�\n");
			while(true)
			{
				Object out3 = receivemessage(instream3);
				if(out3!=null&&out3 instanceof String)
				{	
					String result = (String)out3;
					if(result.equals("false"))
					{	
						textArea.append("Ӧ�÷���������֤δͨ��\n");
						return false;
					}
				}
				if(out3!=null && out3 instanceof S_C)
				{	
					S_C s_c = (S_C)out3;
					byte[] TS6byte=s_c.getTS6();
					byte[] En_TS6=des.decryptByDES(TS6byte, detc.getKeycv());
					long TS6 = change.bytesToLong(En_TS6);
					textArea.append("����Ӧ�÷����������Ϣ���ܺ�õ���\n");
					textArea.append("TS6:"+TS6+"\n");
					textArea.append("���������֤\n");
					if(TS5+1!=TS6)
					{
						textArea.append("�ͻ����϶�Ӧ�÷�����������ʱ�������֤δͨ��\n");
						return false;
					}else {
						textArea.append("�ͻ����϶�Ӧ�÷�����������ʱ�������֤ͨ��\n");
						textArea.append("�ͻ��˿��Կ�ʼ��Ӧ�÷���������ͨ��\n");
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
	
	public boolean authentication()throws Exception//��֤������
	{	
		initClientASSocket(5000);
		if(Step1()){//AS��֤
			clientASSocket.close();
			initClientTGSSocket(6000);
			if(Step2()){//TGS��֤
				clientTGSSocket.close();
				initClientServerSocket(7000);
				if(Step3()){//Ӧ�÷�������֤
					pass = true;
					return true;
				}
			}
		}
		return false;
	}
	
	public void sendmessage(Object object,ObjectOutputStream outstream)//������Ϣ����
	{
		try
		{
			outstream.writeObject(object);
			textArea.append("��Ϣ���ͳɹ�/n");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	//C_S client234=new C_S();
	public Object receivemessage(ObjectInputStream instream)//������Ϣ����
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
	
	public void talkToServer0(String str1,String str2,String str3)//��Ӧ�÷�������ͨ�ź���,ʵ�ֹ���Ϊ��¼
	{	
		try
		{	
			byte[] sessionKey = detc.getKeycv();
			byte[] func0 = des.encryptByDES(str1.getBytes(), sessionKey);
			byte[] Inputusername = des.encryptByDES(str2.getBytes(), sessionKey);
			byte[] Inputpassword = des.encryptByDES(str3.getBytes(), sessionKey);
			textArea.append("������ţ�"+new String(func0)+"\n");
			textArea.append("�û�����"+new String(Inputusername)+"\n");
			textArea.append("�����Ϣ��"+new String(Inputpassword)+"\n");
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
					textArea.append("������Ϣ��"+new String(result0)+"\n");
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
	public void talkToServer1(String str1,String str2,String str3)//��Ӧ�÷�������ͨ�ź���,ʵ�ֹ���Ϊע��
	{	
		try
		{	
			byte[] sessionKey = detc.getKeycv();
			byte[] func0 = des.encryptByDES(str1.getBytes(), sessionKey);
			byte[] Inputusername = des.encryptByDES(str2.getBytes(), sessionKey);
			byte[] Inputpassword = des.encryptByDES(str3.getBytes(), sessionKey);
			textArea.append("������ţ�"+new String(func0)+"\n");
			textArea.append("�û�����"+new String(Inputusername)+"\n");
			textArea.append("�����Ϣ��"+new String(Inputpassword)+"\n");
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
					textArea.append("������Ϣ��"+new String(result0)+"\n");
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
	public void talkToServer2(String str1,String str2,String str3)//��Ӧ�÷�������ͨ�ź���,ʵ�ֹ���Ϊ����
	{	
		try
		{	
			byte[] sessionKey = detc.getKeycv();
			byte[] func0 = des.encryptByDES(str1.getBytes(), sessionKey);
			byte[] Inputusername = des.encryptByDES(str2.getBytes(), sessionKey);
			byte[] Inputpassword = des.encryptByDES(str3.getBytes(), sessionKey);
			textArea.append("������ţ�"+new String(func0)+"\n");
			textArea.append("�û�����"+new String(Inputusername)+"\n");
			textArea.append("�����Ϣ��"+new String(Inputpassword)+"\n");
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
					textArea.append("������Ϣ��"+new String(result0)+"\n");
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
	public void talkToServer3(String str1,String str2,String str3)//��Ӧ�÷�������ͨ�ź���,ʵ�ֹ���Ϊ��ѯ
	{	
		try
		{	
			byte[] sessionKey = detc.getKeycv();
			byte[] func0 = des.encryptByDES(str1.getBytes(), sessionKey);
			byte[] Inputusername = des.encryptByDES(str2.getBytes(), sessionKey);
			byte[] Inputgoods = des.encryptByDES(str3.getBytes(), sessionKey);
			textArea.append("������ţ�"+new String(func0)+"\n");
			textArea.append("�û�����"+new String(Inputusername)+"\n");
			textArea.append("�����Ϣ��"+new String(Inputgoods)+"\n");
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
					textArea.append("������Ϣ��"+new String(result0)+"\n");
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