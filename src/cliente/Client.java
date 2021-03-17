package cliente;

import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.Scanner;
public class Client {
	public static void main(String[] args) throws Exception{
		Socket s = new Socket("localhost", 6969);
		boolean fin = false;
		InputStream in = s.getInputStream();
		OutputStream out = s.getOutputStream();
		Scanner scan = new Scanner(System.in);
		byte[] b = new byte[2002];
		Random rand = new Random();
		String id;
		//Three hand shake
		
		/* Envio del SYN */
		int synint = rand.nextInt(9000) + 1000;
		String syn = synint + "";
		System.out.println("Se va a mandar el SYN: " + syn);
		out.write(syn.getBytes());
		System.out.println("Se mandó el SYN: " + syn);
		
		/* recepcion del SYN y ACK */
		in.read(b,0,b.length);
		String synack = new String(b);
		synack = synack.trim();
		System.out.println("Se recibió el synack: " + synack);
		int ack = Integer.parseInt(synack.split("-")[0]);
		int syn2 = Integer.parseInt(synack.split("-")[1]);
		if(!(syn2==synint+1)) {
			String e = -1+"";
			out.write(e.getBytes());
			System.err.println("Connection error");
			fin = true;
		}
		ack++;
		
		/* Envío del ultimo ACK */
		if(!fin) {
			String sack = ack+"";
			out.write(sack.getBytes());
		}
		
		
		in.read(b, 0, b.length);
		id = (new String(b)).trim();
		out.write(id.getBytes());
		
		while(!fin) {
			in.read(b, 0, b.length);
			String str = new String(b);
			System.out.println(str);
			if(str.equals("Gracias, vuelva pronto")) {
				fin = true;
			}else {
				str = scan.next();
				System.out.println("Se va a enviar el siguiente mensaje: " + str);
				out.write(str.getBytes());
			}
		}
		System.out.println("Cliente ha terminado");
		in.close();
		out.close();
		s.close();
		scan.close();
	}
}
