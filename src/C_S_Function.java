//具体功能应用中客户端发给应用服务器的消息
import java.io.*;
public class C_S_Function implements Serializable{
	byte[] FunNUM,User,Message;
	public C_S_Function(byte[] FunNUM,byte[] User,byte[] Message)
	{
		this.FunNUM = FunNUM;//加密的类型，按钮，1234
		this.User = User;//加密的账号
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