package cliente;

import java.io.*;
import java.net.*;
import java.util.Scanner;
public class Client {
	public static void main(String[] args) throws Exception{
		Socket s = new Socket("localhost", 6969);
		boolean fin = false;
		InputStream in = s.getInputStream();
		OutputStream out = s.getOutputStream();
		Scanner scan = new Scanner(System.in);
		byte[] b = new byte[2002];
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
