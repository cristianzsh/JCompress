/**
* JCompress 0.0.1
* @author Cristian Henrique (cristianmsbr@gmail.com)
*/

public class JCompress {
	private static Functions f = new Functions();

	public static void main (String[] args) {
		if (args.length == 0) {
			System.out
				.println("JCompress 0.0.1\nDeveloped by Cristian Henrique (cristianmsbr@gmail.com)\n\nUsage: JCompress -[flag] [file] [destination]\nType [JCompress -h] for help");
			return;
		}

		try {
			switch (args[0]) {
				case "-c":
					f.compress(args[1], args[2]);
					break;
				case "-u":
					f.unzip(args[1], args[2]);
					break;
				case "-l":
					f.listFiles(args[1]);
					break;
				case "-h":
					f.help();
			}
		} catch (Exception ex) {
			System.out.println("Invalid syntax! Type [JCompress -h] for help");
		}
	}
}