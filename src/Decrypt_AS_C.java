//解密后的AS发送给客户端的信息（AS通过客户端的密钥进行加密）
import java.io.Serializable;

public class Decrypt_AS_C implements Serializable{
	byte[] Keyct;
	String IDtgs;
	long TS2,Lifetime2;
	Ticket_tgs Tickettgs;
	
	public Decrypt_AS_C(byte[] Keyct,String IDtgs,long TS2,long Lifetime2,Ticket_tgs Tickettgs)
	{
		this.Keyct = Keyct;
		this.IDtgs = IDtgs;
		this.TS2 = TS2;
		this.Lifetime2 = Lifetime2;
		this.Tickettgs = Tickettgs;
	}
	
	public String getIDtgs() {
		return this.IDtgs;
	}
	
	public long getTS2() {
		return this.TS2;
	}
	
	public long getLifetime2() {
		return this.Lifetime2;
	}
	
	public byte[] getKeyct() {
		return this.Keyct;
	}
	
	public Ticket_tgs getTickettgs() {
		return this.Tickettgs;
	}
	
}