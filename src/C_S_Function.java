//���幦��Ӧ���пͻ��˷���Ӧ�÷���������Ϣ
import java.io.*;
public class C_S_Function implements Serializable{
	byte[] FunNUM,User,Message;
	public C_S_Function(byte[] FunNUM,byte[] User,byte[] Message)
	{
		this.FunNUM = FunNUM;//���ܵ����ͣ���ť��1234
		this.User = User;//���ܵ��˺�
		this.Message = Message;//mima
	}
	
	public byte[] getMessage()
	{
		return this.Message;
	}
	public byte[] getUser()
	{
		return this.User;
	}
	public byte[] getFunNUM()
	{
		return this.FunNUM;
	}
}