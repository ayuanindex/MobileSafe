package com.ayuan.mobilesafe.db.domain;

import java.util.Objects;

public class BlackNumberInfo {
	private String phone;
	private String mode;

	public BlackNumberInfo() {
	}

	public BlackNumberInfo(String phone, String mode) {
		this.phone = phone;
		this.mode = mode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	@Override
	public String toString() {
		return "BlackNumberInfo{" +
				"phone='" + phone + '\'' +
				", mode='" + mode + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BlackNumberInfo that = (BlackNumberInfo) o;
		return Objects.equals(phone, that.phone) &&
				Objects.equals(mode, that.mode);
	}

	@Override
	public int hashCode() {

		return Objects.hash(phone, mode);
	}
}
