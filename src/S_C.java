//应用服务器向客户端发送的验证信息
import java.io.Serializable;
@SuppressWarnings("serial")
public class S_C implements Serializable{
	byte[] TS6;//TS6=TS5+1
	public S_C(byte[] TS6)
	{
		this.TS6 = TS6;
	}
	public byte[] getTS6() {
		return this.TS6;
	}
}