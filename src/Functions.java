import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;	
import java.util.zip.ZipFile;

/**
* JCompress 0.0.2
* @author Cristian Henrique (cristianmsbr@gmail.com)
*/

public class Functions {
	private List<String> fileList = new ArrayList<>();
	private byte[] buffer = new byte[1024];

	public void compress(String file, String destination) throws Exception {
		File f = new File(file);
		if (f.isDirectory()) {
			compressDirectory(f, destination);
			return;
		}

		FileInputStream in = new FileInputStream(file);
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(destination));
		zos.putNextEntry(new ZipEntry(f.getName()));
		System.out.println("Adding " + f.getName() + " in " + destination);

		int len;
		while ((len = in.read(buffer)) > 0) {
			zos.write(buffer, 0, len);
		}
		zos.close();
		in.close();
	}

	private void compressDirectory(File file, String destination) throws Exception {
		populateList(file);
		FileOutputStream fos = new FileOutputStream(destination);
		ZipOutputStream zos = new ZipOutputStream(fos);

		for (String path : fileList) {
			System.out.println("Adding: " + path);
			ZipEntry ze = new ZipEntry(path.substring(file.getAbsolutePath().length() + 1, path.length()));
			zos.putNextEntry(ze);

			FileInputStream fis = new FileInputStream(path);
			int len;

			while ((len = fis.read(buffer)) > 0) {
				zos.write(buffer, 0, len);
			}
			zos.closeEntry();
			fis.close();
		}
		zos.close();
		fos.close();
	}

	private void populateList(File file) {
		File[] files = file.listFiles();
		for (File f : files) {
			if (f.isFile()) {
				fileList.add(f.getAbsolutePath());
			} else {
				populateList(f);
			}
		}
	}

	public void unzip(String file, String destination) throws Exception {
		File dest = new File(destination);
		if (!dest.exists()) {
			dest.mkdir();
		}

		ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
		ZipEntry ze = zis.getNextEntry();

		while (ze != null) {
			File f = new File(destination + File.separator + ze.getName());
			System.out.println("Unzip: " + f.getAbsoluteFile());
			new File(f.getParent()).mkdirs();

			FileOutputStream fos = new FileOutputStream(f);
			int len;
			while ((len = zis.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}

			fos.close();
			ze = zis.getNextEntry();
		}
		zis.closeEntry();
		zis.close();
	}

	public void listFiles(String file) throws Exception {
		ZipFile f = new ZipFile(file);

		Enumeration<? extends ZipEntry> e = f.entries();

		while (e.hasMoreElements()) {
			ZipEntry entry = e.nextElement();
			System.out.println(entry.getName());
		}
		f.close();
	}

	public void help() {
		System.out.println("Usage: JCompress -[flag] [file] [destination]\n\nFlags:\n\t-c --> Compress file\n\t-u --> Unzip file\n\t-l --> List files\n\t-h --> Help");
	}
}