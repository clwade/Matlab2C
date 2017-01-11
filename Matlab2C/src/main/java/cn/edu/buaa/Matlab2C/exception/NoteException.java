/** 
 * @author  LiuCong  
 * @file    test.java
 * @date    Date：2015年12月20日 下午2:55:03 
 * @version 1.0 
 */

package cn.edu.buaa.Matlab2C.exception;

public class NoteException extends Matlab2CException{
	public NoteException(){
		super();
	}
	public NoteException(String fileName, int row){
		super(fileName+": NoteException:exception occured in line"+row);
	}
	@Override
	public String getMessage(){
		return super.getMessage();
	}
	
}
