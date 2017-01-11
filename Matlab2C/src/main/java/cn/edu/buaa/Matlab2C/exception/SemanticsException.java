/** 
 * @author  LiuCong  
 * @file    SemanticsException.java 
 * @date    Date：2016年1月7日 下午8:37:07 
 * @version 1.0 
 */

package cn.edu.buaa.Matlab2C.exception;

import static cn.edu.buaa.Matlab2C.exception.Error.REDEFINE;
import static cn.edu.buaa.Matlab2C.exception.Error.MISMATCH;
import static cn.edu.buaa.Matlab2C.exception.Error.UNDEFINE;
import static cn.edu.buaa.Matlab2C.exception.Error.UNDEFINEDCHILD;

public class SemanticsException extends Matlab2CException {
	
	/*
	 * -1表示是include进来的
	 */
	public SemanticsException(String fileName, int line, Error error, String message){
		super(fileName+" "+error.toString()+":At line "+ line+";"+message);
	}
	
	public static SemanticsException mismatchException(String fileName, int line, String message) {
		return new SemanticsException(fileName, line, MISMATCH, message);
	}
	
	public static SemanticsException undefineException(String fileName, int line, String message) {
		return new SemanticsException(fileName, line, UNDEFINE, message);
	}
	
	public static SemanticsException undefinedChildException(String fileName, int line, String message) {
		return new SemanticsException(fileName, line, UNDEFINEDCHILD, message);
	}
	
	public static SemanticsException redefineException(String fileName, int line, String message) {
		return new SemanticsException(fileName, line, REDEFINE, message);
	}
	
	public static SemanticsException parameterErorException(String fileName, int line, String message) {
		return new SemanticsException(fileName, line, Error.PARAMETERERROR, message);
	}
	
	@Override
	public String getMessage() {
		return super.getMessage();
	}
}
