package com.shiyi.controller.bean;

import javax.xml.bind.annotation.*;
import java.util.Set;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(propOrder = {}) 
public class ChannelBean {

	@XmlElement(name="name")
	private String name;
	
	@XmlElement(name="key")
	private String key;
	
	@XmlElement(name="signcheck")
	private boolean signcheck;
	
	@XmlElementWrapper(name = "allow")  
    @XmlElement(name = "url")  
	private Set<String> allow;  
	
	@XmlElementWrapper(name = "deny")  
    @XmlElement(name = "url")  
	private Set<String> deny;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public boolean isSigncheck() {
		return signcheck;
	}

	public void setSigncheck(boolean signcheck) {
		this.signcheck = signcheck;
	}

	public Set<String> getAllow() {
		return allow;
	}

	public void setAllow(Set<String> allow) {
		this.allow = allow;
	}

	public Set<String> getDeny() {
		return deny;
	}

	public void setDeny(Set<String> deny) {
		this.deny = deny;
	}  
	
	
}
