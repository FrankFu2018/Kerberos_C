//Ӧ�÷�������Ʊ�ݣ���TGSʹ��Ӧ�÷���������Կ���ɣ�
import java.io.*;
@SuppressWarnings("serial")
public class Ticket_v implements Serializable{
	byte[] Keycv,IDc, ADc,IDv,TS4,Lifetime4;
	
	public Ticket_v(byte[] Keycv,byte[] IDc, byte[] ADc, byte[] IDv, byte[] TS4, byte[] Lifetime4)
	{	
		this.Keycv=Keycv;
		this.IDc = IDc;
		this.ADc = ADc;
		this.IDv = IDv;
		this.TS4 = TS4;
		this.Lifetime4 = Lifetime4;
	}
	
	public byte[] getKeycv()
	{
		return this.Keycv;
	}
	
	public byte[] getIDc()
	{
		return this.IDc;
	}
	
	public byte[] getADc()
	{
		return this.ADc;
	}
	
	public byte[] getIDv()
	{
		return this.IDv;
	}
	
	public byte[] getTS4()
	{
		return this.TS4;
	}
	
	public byte[] getLifetime4()
	{
		return this.Lifetime4;
	}
	
}