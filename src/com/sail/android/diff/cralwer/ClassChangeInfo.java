package com.sail.android.diff.cralwer;

public class ClassChangeInfo {
	
	public String packageName;
	public String methodName;
	public boolean isDepcrecated;
	
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public boolean isDepcrecated() {
		return isDepcrecated;
	}
	public void setDepcrecated(boolean isDepcrecated) {
		this.isDepcrecated = isDepcrecated;
	}
	
	
}
