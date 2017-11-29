import java.io.*;

public class FileDivider {
		
	public void divideFile(String fileName, String directory) {
		try {
			FileOutputStream file1 = new FileOutputStream(directory + "\\file1.txt");
			FileOutputStream file2 = new FileOutputStream(directory + "\\file2.txt");
			FileOutputStream file3 = new FileOutputStream(directory + "\\file3.txt");
			FileOutputStream file4 = new FileOutputStream(directory + "\\file4.txt");
			FileOutputStream file5 = new FileOutputStream(directory + "\\file5.txt");
			FileInputStream inputFile = new FileInputStream(fileName);
			double fileSize = inputFile.getChannel().size();
			
			int fileIndex = 0;
			while(fileIndex < (fileSize/5)) {
				file1.write(inputFile.read());
				fileIndex++;
			}
			while(fileIndex < 2*(fileSize/5)) {
				file2.write(inputFile.read());
				fileIndex++;
			}
			while(fileIndex < 3*(fileSize/5)) {
				file3.write(inputFile.read());
				fileIndex++;
			}
			while(fileIndex < 4*(fileSize/5)) {
				file4.write(inputFile.read());
				fileIndex++;
			}
			while(fileIndex < (fileSize)) {
				file5.write(inputFile.read());
				fileIndex++;
			}
			file1.close();
			file2.close();
			file3.close();
			file4.close();
			file5.close();
			inputFile.close();
		}
		catch (Exception e) {
			System.out.print("Error occured while making or writing to files.");
			System.exit(0);
		}

	}
}