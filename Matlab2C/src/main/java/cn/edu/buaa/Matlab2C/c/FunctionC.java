package cn.edu.buaa.Matlab2C.c;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import cn.edu.buaa.Matlab2C.grammar.Matlab2C;

public class FunctionC {
	
	private String fileName;
	private StringBuilder cFile; // c文件
	private StringBuilder hFile; // 头文件
	// private StringBuilder staticFuntionString; //比较函数
	private StringBuilder functionString; // 定义的函数
	public static StringBuilder functionStaString = new StringBuilder();//函数中语句序列
	private HashMap<String, Boolean> staticFunctionName = new HashMap<String, Boolean>();// 记录静态函数的名称，防止重复

	public FunctionC(String fileName) {
		this.fileName = fileName;
		cFile = new StringBuilder();
		hFile = new StringBuilder();
		functionString = new StringBuilder();
		// staticFuntionString = new StringBuilder();
		// functionString = new StringBuilder();
		cFile.append("#include \"" + fileName + ".h\"\n\n");
		hFile.append("#ifndef _" + fileName + "_\n");
		hFile.append("#define _" + fileName + "_\n");
		hFile.append("#include \"./oop/util/BStar.h\"\n");
		hFile.append("#include \"./oop/util/List.h\"\n");
		hFile.append("#include \"./oop/util/Array.h\"\n");
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public StringBuilder getcFile() {
		return cFile;
	}

	public void addcFile(String cFile) {
		this.cFile.append(cFile);
	}

	public StringBuilder gethFile() {
		return hFile;
	}

	public void addhFile(String hFile) {
		this.hFile.append(hFile);
	}

	public boolean addStaticFunctionName(String functionName) {
		if (staticFunctionName.containsKey(functionName)) {
			return false;
		} else {
			staticFunctionName.put(functionName, true);
			return true;
		}
	}

	// public void addStaticFuntionString(String staticFunctionString) {
	// this.staticFuntionString.append(staticFunctionString);
	// }

	public void addFunctionString(String functionString) {
		this.functionString.append(functionString);
	}
	
	public boolean toFile() {
		this.hFile.append("\n\n#endif\n");
		File cFile = new File(Matlab2C.baseRoot + "/c/" + fileName + ".c");
		File hFile = new File(Matlab2C.baseRoot + "/c/" + fileName + ".h");
		FileWriter cFileWriter = null;
		FileWriter hFileWriter = null;
		try {
			if (!cFile.exists()) {
				cFile.createNewFile();
			}
			if (!hFile.exists()) {
				hFile.createNewFile();
			}
			cFileWriter = new FileWriter(cFile);
			hFileWriter = new FileWriter(hFile);
			cFileWriter.write(this.cFile.toString());
			cFileWriter.write("\n");
			cFileWriter.write(this.functionString.toString());
			hFileWriter.write(this.hFile.toString());
//			cFileWriter.flush();
//			hFileWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (cFileWriter != null) {
					cFileWriter.close();
				}
				if (hFileWriter != null) {
					hFileWriter.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

}
