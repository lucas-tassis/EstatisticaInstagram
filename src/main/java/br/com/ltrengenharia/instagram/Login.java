package br.com.ltrengenharia.instagram;

import java.io.IOException;
import java.util.Objects;

import javax.swing.JOptionPane;

import org.apache.http.client.ClientProtocolException;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramGetChallengeRequest;
import org.brunocvcunha.instagram4j.requests.InstagramRequest;
import org.brunocvcunha.instagram4j.requests.InstagramResetChallengeRequest;
import org.brunocvcunha.instagram4j.requests.InstagramSelectVerifyMethodRequest;
import org.brunocvcunha.instagram4j.requests.InstagramSendSecurityCodeRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramGetChallengeResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramLoginResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSelectVerifyMethodResult;

import br.com.ltrengenharia.dialogs.Dialogs;
import br.com.ltrengenharia.exceptions.LoginException;

public class Login {
	private Instagram4j instagram4j;

	public Instagram4j getInstagram4j() {
		return this.instagram4j;
	}

	public void logar(String usuario, String senha) throws LoginException {
		this.instagram4j = Instagram4j.builder().username(usuario).password(senha).build();
		this.instagram4j.setup();
		try {
			InstagramLoginResult instagramLoginResult = this.instagram4j.login();
			if (!Objects.equals(instagramLoginResult.getStatus(), "ok")) {
				Objects.equals(instagramLoginResult.getError_type(), "bad_password");
				if (Objects.equals(instagramLoginResult.getError_type(), "checkpoint_challenge_required")) {
					String challengeUrl = instagramLoginResult.getChallenge().getApi_path().substring(1);
					String resetChallengeUrl = challengeUrl.replace("challenge", "challenge/reset");
					InstagramGetChallengeResult getChallengeResult = (InstagramGetChallengeResult) this.instagram4j
							.sendRequest((InstagramRequest) new InstagramResetChallengeRequest(resetChallengeUrl));
					if (Objects.equals(getChallengeResult.getAction(), "close"))
						getChallengeResult = (InstagramGetChallengeResult) this.instagram4j
								.sendRequest((InstagramRequest) new InstagramGetChallengeRequest(challengeUrl));
					if (Objects.equals(getChallengeResult.getStep_name(), "select_verify_method")) {
						InstagramSelectVerifyMethodResult postChallengeResult = (InstagramSelectVerifyMethodResult) this.instagram4j
								.sendRequest((InstagramRequest) new InstagramSelectVerifyMethodRequest(challengeUrl,
										getChallengeResult.getStep_data().getChoice()));
						String securityCode = JOptionPane.showInputDialog(null, "Entre com o crecebido por SMS",
								"LTR Engeharia", 1);
						InstagramLoginResult securityCodeInstagramLoginResult = (InstagramLoginResult) this.instagram4j
								.sendRequest((InstagramRequest) new InstagramSendSecurityCodeRequest(challengeUrl,
										securityCode));
						if (!Objects.equals(securityCodeInstagramLoginResult.getStatus(), "ok"))
							throw new LoginException("Falha ao entrar na conta: " + usuario + ". Cinvtente novante");
					}
				}
			}
		} catch (ClientProtocolException e) {
			Dialogs.showErro(e.getMessage());
		} catch (IOException e) {
			Dialogs.showErro(e.getMessage());
		}
	}
}
