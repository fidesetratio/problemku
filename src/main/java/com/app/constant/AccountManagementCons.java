package com.app.constant;

import org.springframework.stereotype.Component;

@Component
public class AccountManagementCons {

	public final Integer client_id = 20;
	public final Integer process_id_login = 1;
	public final Integer process_id_forgot_password = 2;
	public final Integer process_id_change_password = 3;
	public final Integer process_id_logout = 4;
	public final Integer process_id_clearkey = 5;

	public final Integer jenis_id_otp = 92;
	public final Integer menu_id_otp_forgot_password = 1;
	public final Integer menu_id_otp_login = 2;

	public final String error_message = "Maaf system sedang error, coba beberapa saat lagi atau hubungi administrator";
	public final String success_update = "Success update data";
	public final String success_getdata = "Success get data";
	public final String success_login = "Success login";
}