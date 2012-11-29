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
