//�ͻ��˷��͸� TGS����֤��Ϣ���ö��ߵĹ�����Կ���м��ܣ�
import java.io.Serializable;
public class Authenticator_tgs implements Serializable{
	byte[] IDc,ADc,TS3;
	
	public Authenticator_tgs(byte[] IDc,byte[] ADc, byte[] TS3)
	{
		this.IDc = IDc;
		this.ADc = ADc;
		this.TS3 = TS3;
	}
	
	public byte[] getTS3()
	{
		return this.TS3;
	}
	
	public byte[] getIDc()
	{
		return this.IDc;
	}
	
	public byte[] getADc()
	{
		return this.ADc;
	}
	
}