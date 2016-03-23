package cn.com.oceancode.service;

public class TTReturnObj {
	String msg = null;
	Throwable e = null;

	public TTReturnObj(String msg) {
		super();
		this.msg = msg;
	}

	public TTReturnObj(String msg, Throwable e) {
		super();
		this.msg = msg;
		this.e = e;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Throwable getE() {
		return e;
	}

	public void setE(Throwable e) {
		this.e = e;
	}

	public boolean hasException() {
		if (getE() != null) {
			return true;
		}
		return false;
	}
}
