
package cliente;

import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.Scanner;
import servidor.*;
public class Client extends Thread{
	public static void main(String[] args) throws Exception{
		Socket s = new Socket("localhost", 6969);
		boolean fin = false;
		InputStream in = s.getInputStream();
		OutputStream out = s.getOutputStream();
		FileOutputStream fos;
		Scanner scan = new Scanner(System.in);
		byte[] b = new byte[1024];
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
		
		////////////////////
		in.read(b, 0, b.length);
		id = (new String(b)).trim();
		out.write(id.getBytes());
		String str;
		
		while(!fin) {
			in.read(b, 0, b.length);
			str = new String(b);
			System.out.println(str);
			if(str.equals("Gracias, vuelva pronto")) {
				fin = true;
			}else if(str.contains("Selecciono la opcion 1")) {
				System.out.println("Por favor seleccione la cantidad de clientes para enviar el archivo:\nA-1\nB-5\nC-10");
				str = scan.next();
				String op = str;
				out.write(str.getBytes());
				
				in.read(b,0,b.length);
				str = new String(b);
				System.out.println(str);
				System.out.println("Confirmar el envio S/N");
				str = scan.next();
				out.write(str.getBytes());
				int c = 0;
				String nomArchivo = darNombreArchivo(op, id);
				String ruta = ServerThread.FOLDER_ROUTE+"\\ArchivosRecibidos\\"+nomArchivo;
				
				if(op.contains("A")){
					System.out.println("Te lo mando solo a ti");
					in.read(b,0,b.length);
					fos = new FileOutputStream(ruta);
					while(c<102400) {
						
						//System.out.println("Se va a a escribir algo");
						fos.write(b);
						//System.out.println("se 4escribio algo");
						in.read(b,0,b.length);
						//System.out.println("Se recibio un paquete");
						str = "recibido";
						//out.write(str.getBytes());
						//System.out.println("Paquete " + c + " leido");
						c++;
					}
					
					System.out.println("Se termino de leer");
					fos.flush();
					String llave = ServerThread.encypt(ruta);
					System.out.println("El hash es: " + llave);
					in.read(b,0,b.length);
					str = new String(b);
					System.out.println(str);
					
				}else if(op.contains("B")) {
					System.out.println("Ser manda a 5 personas");
				}else {
					System.out.println("Se manda a 10 personas");
				}
				
				
			}else if(str.contains("Selecciono la opcion 2")) {
				System.out.println("Por favor seleccione la cantidad de clientes para enviar el archivo:\nA-1\nB-5\nC-10");
				str = scan.next();
				String op = str;
				out.write(str.getBytes());
				
				in.read(b,0,b.length);
				str = new String(b);
				System.out.println(str);
				System.out.println("Confirmar el envio S/N");
				str = scan.next();
				out.write(str.getBytes());
				int c = 0;
				String nomArchivo = darNombreArchivo(op, id);
				String ruta = ServerThread.FOLDER_ROUTE+"\\ArchivosRecibidos\\"+nomArchivo;
				
				if(op.contains("A")){
					System.out.println("Te lo mando solo a ti");
					in.read(b,0,b.length);
					out.write(str.getBytes());
					in.read(b,0,b.length);
					fos = new FileOutputStream(ruta);
					while(!(new String(b)).contains("END")) {
						out.write(str.getBytes());
						//System.out.println("Se va a a escribir algo");
						fos.write(b);
						//System.out.println("se 4escribio algo");
						in.read(b,0,b.length);
						//System.out.println("Se recibio un paquete");
						str = "recibido";
						//
						
						//System.out.println("Paquete " + c + " leido");
						c++;
					}
					
					System.out.println("Se termino de leer");
					fos.flush();
					String llave = ServerThread.encypt(ruta);
					System.out.println("El hash es: " + llave);
					in.read(b,0,b.length);
					str = new String(b);
					System.out.println(str);
					
				}else if(op.contains("B")) {
					System.out.println("Ser manda a 5 personas");
				}else {
					System.out.println("Se manda a 10 personas");
				}
				
			}
			else {
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
	
	public static String darNombreArchivo(String opcion, String id) {
		String s = "Cliente"+id+"-Prueba-";
		if(opcion.equals("A")) {
			s+="1.test";
		}else if(opcion.equals("B")) {
			s+="5.test";
		}else {
			s+="10.test";
		}
		return s;
	}
	
	private String archivo;
	public Client(String fil) {
		archivo = fil;
	}
	@Override
	public void run() {
		try {
			System.out.println("Hola, soy un cliente");
			Socket s = new Socket("localhost", 6969);
			boolean fin = false;
			InputStream in = s.getInputStream();
			OutputStream out = s.getOutputStream();
			FileOutputStream fos;
			Scanner scan = new Scanner(System.in);
			byte[] b = new byte[1024];
			Random rand = new Random();
			String id;
			//Three hand shake
			
			/* Envio del SYN */
			int synint = rand.nextInt(9000) + 1000;
			String syn = synint + "";
			//System.out.println("Se va a mandar el SYN: " + syn);
			out.write(syn.getBytes());
			//System.out.println("Se mandó el SYN: " + syn);
			
			/* recepcion del SYN y ACK */
			in.read(b,0,b.length);
			String synack = new String(b);
			synack = synack.trim();
			//System.out.println("Se recibió el synack: " + synack);
			int ack = Integer.parseInt(synack.split("-")[0]);
			int syn2 = Integer.parseInt(synack.split("-")[1]);
			if(!(syn2==synint+1)) {
				String e = -1+"";
				out.write(e.getBytes());
				System.err.println("Connection errorrrr");
				fin = true;
			}
			ack++;

			/* Envío del ultimo ACK */
			if(!fin) {
				String sack = ack+"";
				out.write(sack.getBytes());


				////////////////////
				in.read(b, 0, b.length);
				id = (new String(b)).trim();
				out.write(id.getBytes());
				String str = "";
				
				in.read(b, 0, b.length);
				
				out.write(archivo.getBytes());
				
				in.read(b,0,b.length);
				
				out.write("A".getBytes());
				
				in.read();
				
				out.write("S".getBytes());
				
				int c = 0;
				String nomArchivo = darNombreArchivo("A",id);
				String ruta = ServerThread.FOLDER_ROUTE+"\\ArchivosRecibidos\\"+nomArchivo;
				
				if("A".contains("A")){
					//System.out.println("Te lo mando solo a ti");
					in.read(b,0,b.length);
					fos = new FileOutputStream(ruta);
					
					/*out.write(str.getBytes());*/
					in.read(b,0,b.length);
					
					while(!(new String(b)).contains("END")) {
						out.write(str.getBytes());
						//System.out.println("Se va a a escribir algo");
						fos.write(b);
						//System.out.println("se 4escribio algo");
						in.read(b,0,b.length);
						//System.out.println("Se recibio un paquete");
						str = "recibido";
						//
						
						//System.out.println("Paquete " + c + " leido");
						c++;
					}
					
					System.out.println("Agua de uwu");
					fos.flush();
					String llave = ServerThread.encypt(ruta);
					System.out.println("El hash de verificacion es: " + llave);
					in.read(b,0,b.length);
					str = new String(b);
					System.out.println(str);
				}
				
			}
			
			
		}catch(Exception e) {
			System.err.println(e.getMessage());
		}
		
	}

}
