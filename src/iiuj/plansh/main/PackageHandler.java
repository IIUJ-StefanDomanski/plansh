package iiuj.plansh.main;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteOrder;

import org.apache.commons.compress.compressors.gzip.*;
import org.apache.commons.compress.archivers.tar.*;
import org.apache.http.util.ByteArrayBuffer;

public class PackageHandler {

	void pack(String dir)
	{
		Path dir = new Path (name);
		try{
		ByteArrayOutputStream out = new ByteArrayOutputStream() ;
		TarArchiveOutputStream tr = new TarArchiveOutputStream(out);
		GzipCompressorOutputStream gzOut = new GzipCompressorOutputStream(new FileOutputStream(tmp));

		DirectoryStream<Path> stream = Files.newDirectoryStream(dir)
		for (Path file : stream)
		{	File f = file.toFile();
			TarArchiveEntry tae = new TarArchiveEntry(f);
			tae.setSize(f.length());
			tr.putArchiveEntry(tae);
		IOUtils.copy(new FileInputStream(f), tr);
		tr.closeArchiveEntry();
			
		}
		tr.finish();
		tr.close();
		int n = 0;
		int buffersize=2048;
		final byte[] buffer = new byte[buffersize];
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		while (-1 != (n = in.read(buffer))) {
		gzOut.write(buffer, 0, n);
		}
		gzOut.close();

		} catch (IOException | DirectoryIteratorException x) {
		    // IOException can never be thrown by the iteration.
		    // In this snippet, it can only be thrown by newDirectoryStream.
		    System.err.println(x);
		}
	}
	
	void unpack(String file)
	{
		try {
			GzipCompressorInputStream gzIn = new GzipCompressorInputStream(new BufferedInputStream(new FileInputStream(file)));

		int buffersize=2048;
		final byte[] buffer = new byte[buffersize];
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int n = 0;
		while (-1 != (n = gzIn.read(buffer))) {
		    out.write(buffer, 0, n);
		}
		gzIn.close();
		ByteArrayInputStream inTar = new ByteArrayInputStream(out.toByteArray());
		TarArchiveInputStream in = new TarArchiveInputStream(inTar);
		in.getNextEntry();
		in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
